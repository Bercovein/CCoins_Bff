package com.ccoins.bff.dto.prizes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientPartyDTO {

    private Long id;
    private Long client;

    private Long party;

    private boolean active;

    private boolean leader;

}
