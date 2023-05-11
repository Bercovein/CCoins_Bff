package com.ccoins.bff.dto.coins;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedeemCodeRqDTO {

    @NotEmpty
    private String code;

    @NotNull
    private Long clientId;

    @NotNull
    private Long partyId;
}
