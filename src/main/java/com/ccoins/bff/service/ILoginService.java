package com.ccoins.bff.service;

import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.dto.users.ClientTableDTO;

public interface ILoginService {

    void loginClient(ClientTableDTO request);

    void registerClient(ClientDTO request);
}
