package com.ccoins.bff.service;

import com.ccoins.bff.dto.users.ClientTableDTO;

public interface ILoginService {

    ClientTableDTO loginClient(ClientTableDTO request);

}
