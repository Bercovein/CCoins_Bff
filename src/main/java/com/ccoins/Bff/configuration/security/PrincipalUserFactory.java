package com.ccoins.Bff.configuration.security;

import com.ccoins.Bff.dto.users.OwnerDTO;

public class PrincipalUserFactory {

    public static PrincipalUser build(OwnerDTO owner, String secretPsw){
       return PrincipalUser.builder().email(owner.getEmail()).password(secretPsw).build();
    }
}
