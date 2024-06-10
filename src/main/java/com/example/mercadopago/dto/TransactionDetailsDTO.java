package com.example.mercadopago.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDetailsDTO {

    // moto neto recibido
    private Integer net_received_amount;

    // monto cobrado al pagador
    private Integer total_paid_amount;

}
