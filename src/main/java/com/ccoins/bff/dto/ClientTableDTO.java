package com.ccoins.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientTableDTO {

    private String clientIp;

    private Long clientId;

    private String tableCode;

    private String nickName;

    private Long partyId;
}
