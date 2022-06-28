package com.ccoins.Bff.dto.prizes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartyDTO {

    private Long id;

    private String name;

    private LocalDateTime startDate;

    private boolean active;

    private Long table;
}
