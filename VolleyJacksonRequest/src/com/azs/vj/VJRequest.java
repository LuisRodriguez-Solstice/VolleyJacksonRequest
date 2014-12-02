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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VJRequest<T> extends JsonRequest<T> {

	/**
	 * Set a default mapper for all jackson mapping.
	 * 
	 * If this method is called with a non-null ObjectMapper, all future
	 * requests that do not specify a custom mapper will use the configured
	 * default mapper.
	 * 
	 * @param defaultMapper
	 *            ObjectMapper to set as the default
	 */
	public static void setDefaultObjectMapper(ObjectMapper defaultMapper) {
		VJRequest.defaultMapper = defaultMapper;
	}

	/**
	 * Singular mapper associated with all requests that do not specify a custom
	 * ObjectMapper.
	 */
	private static ObjectMapper defaultMapper;

	/**
	 * Returns the default ObjectMapper.
	 * 
	 * If a default mapper doesn't already exist, create a new one with no
	 * special configurations.
	 * 
	 * @return ObjectMapper the default ObjectMapper
	 */
	private static synchronized ObjectMapper getDefaultMapper() {
		if (defaultMapper == null) {
			defaultMapper = new ObjectMapper();
		}
		return defaultMapper;
	}

	private TypeReference<T> responseType;
	private Map<String, String> params;
	private ObjectMapper mapper;

	/**
	 * Creates a new VJRequest.
	 * 
	 * @param method
	 *            int one of com.android.volley.Request.Method ints
	 * @param url
	 * @param params
	 *            any post parameters
	 * @param responseType
	 *            TypeReference<T> anonymous class that wraps the object that
	 *            the response will be mapped to
	 * @param mapper
	 *            ObjectMapper custom mapper to be used instead of the default
	 * @param successListener
	 *            Response.Listener<T> to be called on successful network
	 *            request and object mapping
	 * @param errorListener
	 *            Response.ErrorListener to be called on network error or object
	 *            mapping error
	 */
	public VJRequest(int method, String url, Map<String, String> params, TypeReference<T> responseType, ObjectMapper mapper, Response.Listener<T> successListener, Response.ErrorListener errorListener) {
		super(method, url, null, successListener, errorListener);

		this.responseType = responseType;
		this.params = params;
		this.mapper = mapper;
	}

	public VJRequest(int method, String url, Map<String, String> params, TypeReference<T> responseType, Response.Listener<T> successListener, Response.ErrorListener errorListener) {
		this(method, url, params, responseType, getDefaultMapper(), successListener, errorListener);
	}

	public VJRequest(int method, String url, TypeReference<T> responseType, Response.Listener<T> successListener, Response.ErrorListener errorListener) {
		this(method, url, null, responseType, getDefaultMapper(), successListener, errorListener);
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return params;
	}

	/**
	 * Parses a network response and maps it to the specified POJO.
	 */
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
