package com.azs.vj;

import java.io.IOException;
import java.util.Map;

import com.android.volley.AuthFailureError;
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
	private Map<String, String> params;
	private ObjectMapper mapper;
	
	public VJRequest(int method, String url, TypeReference<T> responseType, Response.Listener<T> successListener, Response.ErrorListener errorListener) {
		this(method, url, null, responseType, getDefaultMapper(), successListener, errorListener);
	}
	
	public VJRequest(int method, String url, Map<String, String> params, TypeReference<T> responseType, Response.Listener<T> successListener, Response.ErrorListener errorListener) {
		this(method, url, params, responseType, getDefaultMapper(), successListener, errorListener);
	}
	
	public VJRequest(int method, String url, Map<String, String> params, TypeReference<T> responseType, ObjectMapper mapper, Response.Listener<T> successListener, Response.ErrorListener errorListener) {
		super(method, url, null, successListener, errorListener);
		
		this.responseType = responseType;
		this.params = params;
		this.mapper = mapper;
	}
	
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return params;
	}
	
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(mapJson(jsonString), HttpHeaderParser.parseCacheHeaders(response));
		} catch (Exception e) {
			return Response.error(new ParseError(e));
		}
	}

	private T mapJson(String data) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(data, responseType);
	}
}
