package com.ccoins.bff.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventNamesEnum {

    UPDATE_COINS(null), //se modifican las coins de una mesa
    NEW_DEMAND(null), //se pide un premio desde una mesa
    NEW_PRIZE(null), //nuevo premio adquirido
    NEW_LEADER(null), //cambió el lider de la mesa
    NEW_CLIENT_TO_PARTY("El integrante ? se ha sumado a la mesa."); //se suma un cliente a la mesa

    private final String message;
}
