package com.ccoins.bff.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ClosePartyEnum {

    CLOSED_PARTY(null, "Se quitaron los clientes de la mesa y se ha cerrado."),
    CLIENTS_ALREADY_ON_PARTY(null, "Se quitaron los clientes de la mesa.");

    final String code;
    final String message;

}
