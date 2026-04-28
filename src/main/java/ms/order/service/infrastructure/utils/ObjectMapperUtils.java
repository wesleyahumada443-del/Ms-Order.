package ms.order.service.infrastructure.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class ObjectMapperUtils {

    private static ObjectMapper mapper;

    public static ObjectMapper getMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setDateFormat(new StdDateFormat());
        return mapper;
    }
/*
    public static String toJson(Object object) {
        try {
            return getMapper().writeValueAsString(object);
        } catch (IOException e) {
            throw new Exception("Error processing object type %s to Json".formatted(object.getClass().getSimpleName()), e.getCause());
        }
    }*/
}
