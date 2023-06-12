package com.ccoins.bff.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventNamesEnum {

    UPDATE_COINS(null), //se modifican las coins de una mesa
    NEW_DEMAND(null), //se pide un premio desde una mesa
    NEW_PRIZE("La party ha adquirido un premio! Que lo disfruten!"), //nuevo premio adquirido
    NEW_LEADER(null), //cambió el lider de la mesa
    YOU_ARE_THE_LEADER("¡Felicidades! Eres el líder de la mesa."), //cambió el lider de la mesa
    NEW_CLIENT_TO_PARTY("El integrante %s se ha sumado a la mesa."), //se suma un cliente a la mesa
    CLIENT_LEFT_THE_PARTY("El integrante %s se ha retirado de la party."), //se suma un cliente a la mesa
    LOGIN_CLIENT("Bienvenido a la party %s!"), //se suma un cliente a la mesa

    LOGOUT_CLIENT(null); // se elimina el cliente de la mesa

    private final String message;
}
