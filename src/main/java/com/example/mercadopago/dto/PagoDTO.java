package com.example.mercadopago.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PagoDTO {
    
    private String payment_id;

    private String date_created;

    private String date_approved;

    private String money_release_date;

    private String status;

    private String status_detail;

    private TransactionDetailsDTO transaction_details;
}