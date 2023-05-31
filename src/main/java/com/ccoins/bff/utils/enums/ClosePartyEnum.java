package com.ccoins.bff.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ClosePartyEnum {

    CLOSED_PARTY(null, "Se quitaron los participantes de la mesa y se ha cerrado."),
    CLOSED_PARTY_ERROR("1", "Ha ocurrido un error al intentar quitar participantes de la mesa"),
    CLIENTS_ALREADY_ON_PARTY(null, "Se quitaron los participantes de la mesa.");

    final String code;
    final String message;

}
