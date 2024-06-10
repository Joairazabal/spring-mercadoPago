package com.example.mercadopago.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DataMpDTO {

    @Override
    public String toString() {
        return "DataMp [id=" + id + "]";
    }

    private String id;
}
