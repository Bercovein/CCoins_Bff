package com.ccoins.Bff.configuration.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleGrantedAuthorityMixIn {

    @JsonCreator     // anotacion de la api jackson para trabajar con json, indicamos que este constructor es por defecto cuando se cree los objetos authorities(que contiene los roles) desde el json
    public SimpleGrantedAuthorityMixIn(@JsonProperty("authority") String role) {   // indicamos el atributo del json que contiene el valor del role, este se va a inyectar en el constructor. mapeamos el json con el atributo role con la clase SimpleGrantedAuthority

    }
}
