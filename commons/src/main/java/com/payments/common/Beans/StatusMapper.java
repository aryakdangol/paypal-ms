package com.payments.common.Beans;

import com.payments.common.Enums.TransactionStatus;
import com.payments.common.utils.Constants;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StatusMapper {

    private final Map<String, TransactionStatus> terminalStatusMap = new HashMap<>();

    public StatusMapper(){
        terminalStatusMap.put(Constants.TRANSACTION_COMPLETED, TransactionStatus.COMPLETED);
        terminalStatusMap.put(Constants.TRANSACTION_CANCELLED, TransactionStatus.CANCELLED);
        terminalStatusMap.put(Constants.TRANSACTION_FAILED, TransactionStatus.FAILED);
    }

    public TransactionStatus getStatus(String orderStatus){
        return terminalStatusMap.getOrDefault(orderStatus, TransactionStatus.NONTERMINAL);
    }

}
