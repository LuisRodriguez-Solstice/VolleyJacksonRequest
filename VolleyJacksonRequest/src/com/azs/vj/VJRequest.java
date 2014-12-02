package com.azs.vj;

import java.io.IOException;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VJRequest<T> extends JsonRequest<T> {

	private static ObjectMapper defaultMapper;
	
	private static synchronized ObjectMapper getDefaultMapper() {
		if (defaultMapper == null) {
			defaultMapper = new ObjectMapper();
			defaultMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		return defaultMapper;
	}
	
	private TypeReference<T> responseType;
	
	public VJRequest(int method, String url, TypeReference<T> responseType, Response.Listener<T> successListener, Response.ErrorListener errorListener) {
		super(method, url, null, successListener, errorListener);
		this.responseType = responseType;
	}
	
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(mapJson(jsonString, responseType), HttpHeaderParser.parseCacheHeaders(response));
		} catch (Exception e) {
			return Response.error(new ParseError(e));
		}
	}

	private T mapJson(String data, TypeReference<T> type) throws JsonParseException, JsonMappingException, IOException {
		return getDefaultMapper().readValue(data, type);
	}
}
