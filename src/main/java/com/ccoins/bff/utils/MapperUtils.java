package com.ccoins.bff.utils;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class MapperUtils {

    public static Object map(Object obj, Class<?> cla){
        ModelMapper mapper =  new ModelMapper();
        return mapper.map(obj,cla);
    }

    public static List<Object> map(List<Object> list, Class<?> cla){

        List<Object> responseList = new ArrayList<>();
        list.forEach(o -> responseList.add(MapperUtils.map(o,cla)));
        return responseList;
    }
}
