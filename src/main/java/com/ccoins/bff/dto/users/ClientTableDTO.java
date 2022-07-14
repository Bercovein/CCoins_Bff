package com.ccoins.bff.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientTableDTO {

    @NotEmpty
    private String clientIp;

    @NotEmpty
    private String tableCode;

    private String nickName;

    private Long partyId;
}
