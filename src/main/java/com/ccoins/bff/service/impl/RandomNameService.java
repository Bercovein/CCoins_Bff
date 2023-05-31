package com.ccoins.bff.service.impl;

import com.ccoins.bff.exceptions.BadRequestException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.service.IRandomNameService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;

@Service
public class RandomNameService implements IRandomNameService {

    private final String animalsPath;

    private final String adjectivesPath;

    private final String defaultNamesPath;

    @Autowired
    public RandomNameService(@Value("${folder.files.animals.path}") String animalsPath,
                             @Value("${folder.files.adjectives.path}") String adjectivesPath,
                             @Value("${folder.files.default-names.path}") String defaultNamesPath) {
        this.animalsPath = animalsPath;
        this.adjectivesPath = adjectivesPath;
        this.defaultNamesPath = defaultNamesPath;
    }

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
        return this.randomFromFile(adjectivesPath).concat(" ").concat(this.randomFromFile(animalsPath));
    }

    @Override
    public String randomDefaultName(){
        return this.randomFromFile(defaultNamesPath);
    }

}
