package com.hanifshop.authentication_service.util;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Convenience class to convert POJO to JSON and vice versa.
 *
 * @author rk
 */
public class PojoJsonMapper {
    private final static ObjectMapper mapper = new ObjectMapper();

    static {
//        mapper.getSerializationConfig().setDateFormat(new SimpleDateFormat("dd MM yyyy HH:mm:ss"));
//        mapper.getDeserializationConfig().setDateFormat(new SimpleDateFormat("dd MM yyyy HH:mm:ss"));
    	mapper.setDateFormat(new SimpleDateFormat("dd MM yyyy HH:mm:ss"));
        mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationConfig(mapper.getSerializationConfig().withSerializationInclusion(Inclusion.NON_NULL));
    }

    /**
     * Convert POJO to its JSON string.
     * @param pojo POJO to be converted
     * @param <T> type parameter
     * @return JSON string
     */
    public static <T> String toJson(T pojo) {
        String result = null;
        try {
            result = mapper.writeValueAsString(pojo);
        } catch (IOException e) {
            e.printStackTrace();
            //-- PJMTJ = POJO JSON MAPPER TO JSON
        }

        return result;
    }

    /**
     * Convert JSON string to specified POJO class.
     * @param json JSON string
     * @param pojoClass POJO class
     * @param <T> type parameter
     * @return POJO object
     */
    public static <T> T fromJson(String json, Class<T> pojoClass) {
        T object = null;
        try {
            object = mapper.readValue(json, pojoClass);
        } catch (IOException e) {
            e.printStackTrace();
            //-- PJMFJ = POJO JSON MAPPER FROM JSON
        }

        return object;
    }

    /**
     * Convert JSON string to specified POJO class.
     * @param json JSON string
     * @param valueTypeRef value of type reference
     * @return Object
     */
    public static <T> T fromJson(String json, TypeReference<T> valueTypeRef) {
        T object = null;
        try {
            object = mapper.<T>readValue(json, valueTypeRef);
        } catch (IOException e) {
            e.printStackTrace();
            //-- PJMFJ = POJO JSON MAPPER FROM JSON
        }

        return object;
    }

    
    public static void Main(String[] args) {
    	List<String> categoryList = new ArrayList<String>();
    	categoryList.add("Pertanyaan");
    	categoryList.add("Kritik");
    	categoryList.add("Lain-Lain");
    	System.out.println(PojoJsonMapper.toJson(categoryList));
    }


}
