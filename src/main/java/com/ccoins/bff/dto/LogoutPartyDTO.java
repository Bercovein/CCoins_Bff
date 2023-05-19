package com.ccoins.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogoutPartyDTO {

   private List<Long> list;
   private Long partyId;
   private boolean banned = false;

}
