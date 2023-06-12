package com.ccoins.bff.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UpdateCoinsMessagesEnum {

    REDEEM_COINS("La party ha adquirido ? coins!"),
    PRIZE_SPEND_COINS("Alguien de la party ha canjeado un premio!"),
    DELIVER_PRIZE_OR_COINS("Se aprobó la entrega del premio!"),
    CANCEL_PRIZE_OR_COINS("Se canceló la entrega del premio."),
    ADJUST_PRIZE_OR_COINS("Se reembolsaron las coins gastadas.");

    private final String message;
}
