package com.ccoins.bff.configuration.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtUserDTO {

    private Long id;

    @NotEmpty
    @Email
    private String email;



}
