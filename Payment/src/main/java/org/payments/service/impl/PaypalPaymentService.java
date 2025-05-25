package org.payments.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payments.common.Beans.StatusMapper;
import com.payments.common.Enums.TransactionStatus;
import com.payments.common.entities.Transaction;
import com.payments.common.entities.User;
import com.payments.common.repositories.UserRepository;
import com.payments.common.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.payments.clients.PaypalWebClient;
import org.payments.dto.CreateOrderRequestDTO;
import org.payments.dto.CreateOrderResponseDTO;
import org.payments.dto.paypal.PaypalCreateOrderDTO;
import org.payments.dto.paypal.PaypalCreateOrderResponseDTO;
import com.payments.common.repositories.TransactionRepository;
import org.payments.exceptions.TransactionException;
import org.payments.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service

public class PaypalPaymentService implements PaymentService {
    @Autowired
   ObjectMapper objectMapper;

    @Autowired
    PaypalWebClient paypalWebClient;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Value("${ngrok.url}")
    String hostUrl;

    @Autowired
    StatusMapper statusMapper;


    @Override
    public List<CreateOrderResponseDTO> findAllOrders(Long userId) {

        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);

        List<CreateOrderResponseDTO> response = new ArrayList<>();

        transactions.forEach(transaction -> {
            CreateOrderResponseDTO order = CreateOrderResponseDTO.builder()
                    .orderId(transaction.getOrderId())
                    .orderStatus(transaction.getOrderStatus())
                    .orderCreatedDate(transaction.getDateCreated())
                    .username(transaction.getUser().getUserName())
                    .build();
            response.add(order);
        });

        return response;

    }

    @Override
    public CreateOrderResponseDTO getOrder(String orderId, Long userId) {

        transactionRepository.findByOrderIdAndUserId(orderId, userId)
                .orElseThrow(() -> new TransactionException("Transaction not found for userId: " + userId + " and orderId: "+  orderId, 404));


        try{

            PaypalCreateOrderResponseDTO response = paypalWebClient.fetchOrder(orderId);
            return mapPaypalResponse(response);
        }

        catch (Exception e) {
            log.error("Error fetching orders from paypal for orderId: {} and userId: {} with message: {}", orderId, userId, e.getMessage());
            throw new TransactionException("Error fetching order from PayPal", 500);
        }
    }


    @Override
    public CreateOrderResponseDTO createOrder(Long userId, CreateOrderRequestDTO req) {

        String amount = req.getAmount();
        String currency = req.getCurrency();

        PaypalCreateOrderDTO paypalCreateOrderDTO = PaypalCreateOrderDTO.builder()
                .intent("CAPTURE")
                .paymentSource(PaypalCreateOrderDTO.PaymentSource.builder().
                                paypal(PaypalCreateOrderDTO.Paypal.builder()
                                        .experienceContext(PaypalCreateOrderDTO.ExperienceContext.builder()
                                                .cancelUrl(req.getCancelUrl())
                                                .returnUrl(req.getSuccessUrl()).build()).build()).build())
                .purchaseUnits(List.of(PaypalCreateOrderDTO.PurchaseUnit.builder().amount(PaypalCreateOrderDTO.Amount.builder().currencyCode(currency).value(amount).build()).build()))
                .build();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TransactionException("User not found with userId: " + userId, 404));

       PaypalCreateOrderResponseDTO response;
        try {
            log.info("Create order request: {}", objectMapper.writeValueAsString(paypalCreateOrderDTO));
             response = paypalWebClient.createOrder(paypalCreateOrderDTO);

             log.info("Paypal Create Order Response: {}", objectMapper.writeValueAsString(response));

        } catch (JsonProcessingException e) {
            log.error("Error while sending request to paypal API: {}", e.getMessage());
            throw new TransactionException("Error  creating orders", 500);
        }
        CreateOrderResponseDTO createOrderResponseDTO = mapPaypalResponse(response);
        //save to db
        Transaction transaction = Transaction.builder()
                .orderStatus(createOrderResponseDTO.getOrderStatus())
                .orderId(createOrderResponseDTO.getOrderId())
                .dateCreated(LocalDateTime.now())
                .dateModified(LocalDateTime.now())
                .user(user)
                .build();

        transactionRepository.save(transaction);

        createOrderResponseDTO.setUsername(user.getUserName());
        createOrderResponseDTO.setOrderCreatedDate(transaction.getDateCreated());

        return createOrderResponseDTO;

    }

    @Override
    public CreateOrderResponseDTO captureOrder(String orderId, Long userId){
        Transaction transaction =  transactionRepository.findByOrderIdAndUserId(orderId, userId)
                .orElseThrow(() -> new TransactionException("Transaction not found for userId: " + userId + " and orderId: "+  orderId, 404));

        String status = transaction.getOrderStatus();

        if(!statusMapper.getStatus(status).equals(TransactionStatus.NONTERMINAL)){
            throw new TransactionException("Order id" + transaction.getOrderId()  + " is already in terminal state: " + transaction.getOrderStatus(), 412);
        }

        try{
            PaypalCreateOrderResponseDTO response = paypalWebClient.captureOrder(orderId, "{}");

            CreateOrderResponseDTO createOrderResponseDTO = mapPaypalResponse(response);

            transaction.setOrderStatus(response.getStatus());
            transaction.setDateModified(LocalDateTime.now());

            Transaction modifiedTransaction = transactionRepository.save(transaction);

            createOrderResponseDTO.setOrderStatus(modifiedTransaction.getOrderStatus());

            return createOrderResponseDTO;

        }
        catch (Exception e){
            log.error("Error occurred capturing order for orderId: {} with cause: {}", orderId, e.getMessage());
            throw new TransactionException("Error occurred capturing order for orderID: " + orderId, 500);
        }
    }


    @Override
    public void completeOrderSuccess(String orderId) {

        PaypalCreateOrderResponseDTO response = paypalWebClient.fetchOrder(orderId);

        String status = response.getStatus();

        Transaction transaction = transactionRepository.findByOrderId(orderId)
                .orElseThrow(() -> new TransactionException("Order id: " + orderId + " not found", 404));

        transaction.setOrderStatus(status);
        transaction.setDateModified(LocalDateTime.now());

        Transaction modified = transactionRepository.save(transaction);

        //captureOrder(modified.getOrderId(), modified.getUser().getId());

        log.info("Order Approved for order id: {}", orderId);
    }

    @Override
    public void completeOrderFailed(String orderId) {

        PaypalCreateOrderResponseDTO response = paypalWebClient.fetchOrder(orderId);

        Transaction transaction = transactionRepository.findByOrderId(orderId)
                .orElseThrow(() -> new TransactionException("Order id: " + orderId + " not found", 404));

        transaction.setOrderStatus(Constants.TRANSACTION_CANCELLED);
        transaction.setDateModified(LocalDateTime.now());

        transactionRepository.save(transaction);

        log.info("Order failed for orderId: {}", orderId);
    }

    @Override
    public void notification() {

    }

    private CreateOrderResponseDTO mapPaypalResponse(PaypalCreateOrderResponseDTO response){

        String orderId = response.getId();
        String status = response.getStatus();
        AtomicReference<String> approveLink = new AtomicReference<>();
        AtomicReference<String> viewOrderLink = new AtomicReference<>();
        response.getLinks().forEach(link -> {
            if(link.getRel().equals("self"))
                viewOrderLink.set(link.getHref());
            else
                approveLink.set(link.getHref());
        });

        return CreateOrderResponseDTO.builder()
                .orderId(orderId)
                .orderStatus(status)
                .approveLink(approveLink.get())
                .viewOrderLink(viewOrderLink.get())
                .build();

    }

}
