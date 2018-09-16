package com.codechef.cah01.service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.codechef.cah01.constant.Constants;

@Component
public class TokenService {

	@Autowired
	private RestTemplate restTemplate;
	Logger logger = LoggerFactory.getLogger(TokenService.class);

	/**
	 * Creates Request body to fetch token
	 * @return request body to fetch token
	 */
	private String getRequestBody() {
		JSONObject requestBody = new JSONObject();
		requestBody.put("grant_type", "client_credentials");
		requestBody.put("scope", "public");
		requestBody.put("client_id", "id"); // dummy
		requestBody.put("client_secret", "secret"); // dummy
		return requestBody.toString();
	}

	/**
	 * Fetches the access token 
	 * @return The access token to access the APIs
	 */
	public String getToken() {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		logger.debug(getRequestBody());
		HttpEntity<Object> request = new HttpEntity<>(getRequestBody(), headers);
		ResponseEntity<String> responseObject = restTemplate.exchange(Constants.TOKEN_ENDPOINT, HttpMethod.POST,
				request, String.class);
		JSONObject response = new JSONObject(responseObject.getBody());
		if (response.getString(Constants.STATUS).equals(Constants.OK)) {
			return Constants.BEARER + response.getJSONObject(Constants.RESULT).getJSONObject(Constants.DATA)
					.getString(Constants.ACCESS_TOKEN);
		}
		return "";
	}
}
