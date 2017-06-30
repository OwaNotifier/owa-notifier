package info.kapable.utils.owanotifier;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import retrofit.mime.TypedInput;

@SuppressWarnings("rawtypes")
public class JacksonConverter implements Converter {
    private final ObjectMapper objectMapper;

    public JacksonConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Object fromBody(TypedInput body, Type type) throws JsonParseException, JsonMappingException, IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(type);
        return objectMapper.readValue(body.in(), javaType);
    }

	@Override
	public Object convert(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JavaType getInputType(TypeFactory arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JavaType getOutputType(TypeFactory arg0) {
		// TODO Auto-generated method stub
		return null;
	}

    //More code
}