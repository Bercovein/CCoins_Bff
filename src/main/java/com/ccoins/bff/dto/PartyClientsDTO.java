package com.ccoins.bff.dto;

import com.ccoins.bff.dto.users.ClientDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartyClientsDTO {

   private Long party;
   private List<ClientDTO> list;
}
