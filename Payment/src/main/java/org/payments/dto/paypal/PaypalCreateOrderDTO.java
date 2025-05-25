package org.payments.dto.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaypalCreateOrderDTO {

    @JsonProperty("intent")
    public String intent;

    @JsonProperty("purchase_units")
    public List<PurchaseUnit> purchaseUnits;

    @JsonProperty("payment_source")
    public PaymentSource paymentSource;

    @Builder
    public static class PurchaseUnit {

        @JsonProperty("amount")
        private Amount amount;

        @JsonProperty("invoice_id")
        private String invoiceId;
    }

    @Builder
    public static class Amount{
        @JsonProperty("currency_code")
        private String currencyCode;

        @JsonProperty("value")
        private String value;
    }

    @Builder
    public static class PaymentSource{

        @JsonProperty("paypal")
        Paypal paypal;

    }

    @Builder
    public static class Paypal{
        @JsonProperty("experience_context")
        public ExperienceContext experienceContext;
    }


    @Builder
    public static  class ExperienceContext{
        @JsonProperty("return_url")
        String returnUrl;

        @JsonProperty("cancel_url")
        String cancelUrl;
    }

}


