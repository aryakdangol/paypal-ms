package org.payments.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payments.common.entities.Transaction;
import com.payments.common.entities.User;
import com.payments.common.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.payments.clients.PaypalWebClient;
import org.payments.dto.CreateOrderRequestDTO;
import org.payments.dto.CreateOrderResponseDTO;
import org.payments.dto.paypal.PaypalCreateOrderDTO;
import org.payments.dto.paypal.PaypalCreateOrderResponseDTO;
import com.payments.common.repositories.TransactionRepository;
import org.payments.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Override
    public CreateOrderResponseDTO createOrder(Long userId, CreateOrderRequestDTO req) {

        String amount = req.getAmount();
        String currency = req.getCurrency();

        PaypalCreateOrderDTO paypalCreateOrderDTO = PaypalCreateOrderDTO.builder()
                .intent("CAPTURE")
                .paymentSource(PaypalCreateOrderDTO.PaymentSource.builder().paypal(PaypalCreateOrderDTO.Paypal.builder().experienceContext(PaypalCreateOrderDTO.ExperienceContext.builder().cancelUrl(hostUrl  + "/cancel").returnUrl(hostUrl + "/success").build()).build()).build())
                .purchaseUnits(List.of(PaypalCreateOrderDTO.PurchaseUnit.builder().amount(PaypalCreateOrderDTO.Amount.builder().currencyCode(currency).value(amount).build()).build()))
                .build();

        Optional<User> userObj = userRepository.findById(userId);

        if(userObj.isEmpty())
            throw new RuntimeException("User not Found Exception");

        User user = userObj.get();

       PaypalCreateOrderResponseDTO response;
        try {
            log.info("Create order request: {}", objectMapper.writeValueAsString(paypalCreateOrderDTO));
             response = paypalWebClient.createOrder(paypalCreateOrderDTO);

             log.info("Paypal Create Order Response: {}", objectMapper.writeValueAsString(response));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
    public void completeOrderSuccess() {

    }

    @Override
    public void completeOrderFailed() {

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
