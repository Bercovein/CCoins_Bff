package com.ccoins.bff.service;

import com.ccoins.bff.dto.users.ClientTableDTO;

public interface ILoginService {

    void loginClient(ClientTableDTO request);

    void registerClient(ClientTableDTO request);
}
