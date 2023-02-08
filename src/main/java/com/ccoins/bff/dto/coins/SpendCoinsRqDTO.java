package com.ccoins.bff.dto.coins;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpendCoinsRqDTO {

    private Long clientParty;
    private Long partyId;
    private Long prizeId;
    private Long prizePoints;
}
