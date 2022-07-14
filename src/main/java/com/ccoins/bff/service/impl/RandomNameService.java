package com.ccoins.bff.service.impl;

import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.service.IRandomNameService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;

@Service
@Slf4j
public class RandomNameService implements IRandomNameService {

    @Value("${folder.files.animals.path}")
    private String ANIMALS_PATH;

    @Value("${folder.files.adjectives.path}")
    private String ADJECTIVES_PATH;

    @Value("${folder.files.default-names.path}")
    private String DEFAULT_NAMES_PATH;

    @Override
    public String randomFromFile(String path) {

        try{
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(path);
            Object obj = parser.parse(reader);
            JSONObject pJsonObj = (JSONObject)obj;
            JSONArray array = (JSONArray)pJsonObj.get("list");

            int random = (int) (Math.random()*(array.size()));

            return (String)array.get(random);
        }catch(Exception e){
            throw new BadRequestException(ExceptionConstant.RANDOM_NAME_ERROR_CODE,
                    this.getClass(), ExceptionConstant.RANDOM_NAME_ERROR);
        }
    }

    @Override
    public String randomGroupName(){
        return this.randomFromFile(ADJECTIVES_PATH).concat(" ").concat(this.randomFromFile(ANIMALS_PATH));
    }

    @Override
    public String randomDefaultName(){
        return this.randomFromFile(DEFAULT_NAMES_PATH);
    }

}
