package com.blankfactor.MaintainMe.web.resource;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    private String token;
    private Long invoiceId;
    private Float payedAmount;
}
