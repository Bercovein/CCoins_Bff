package com.ccoins.bff.service;

public interface IRandomNameService {

    String randomGroupName();

    String randomFromFile(String path);

    String randomDefaultName();
}
