package com.example.mercadopago.dto;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MpNotifyDTO {
    private Long id;
    private Boolean liveMode;
    private String type;
    private OffsetDateTime dateCreated;
    private Long userID;
    private String apiVersion;
    private String action;
    private DataMpDTO data;

    @Override
    public String toString() {
        return "MpNotifyDTO [id=" + id + ", liveMode=" + liveMode + ", type=" + type + ", dateCreated=" + dateCreated
                + ", userID=" + userID + ", apiVersion=" + apiVersion + ", action=" + action + ", data="
                + data.toString() + "]";
    }
}