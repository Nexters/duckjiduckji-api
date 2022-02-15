package com.nexters.duckji.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtils {
	private static final ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new JavaTimeModule());
	}

	public static <T> T convert(Object data, TypeReference<T> tTypeReference) {
		return mapper.convertValue(data, tTypeReference);
	}

	public static <T> String toString(T data) {
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
