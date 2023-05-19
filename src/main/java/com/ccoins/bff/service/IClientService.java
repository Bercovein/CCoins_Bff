package com.ccoins.bff.service;

import com.ccoins.bff.dto.users.ClientDTO;
import com.ccoins.bff.dto.ClientTableDTO;

public interface IClientService {

    ClientTableDTO loginClient(ClientTableDTO request);

    ClientDTO findOrCreateClient(ClientDTO request);

    ClientDTO findActiveByIp(String ip);

    ClientDTO newClient(ClientDTO request);

    void changeName(ClientDTO request);

    void logout(String client);
}
