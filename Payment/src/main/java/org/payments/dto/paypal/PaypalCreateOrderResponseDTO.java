package org.payments.dto.paypal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaypalCreateOrderResponseDTO {

    private String id;
    private String status;
    List<Link> links;


    @Data
    public static class Link{
        @JsonProperty
        String href;

        @JsonProperty
        String rel;

        @JsonProperty
        String method;
    }

}
