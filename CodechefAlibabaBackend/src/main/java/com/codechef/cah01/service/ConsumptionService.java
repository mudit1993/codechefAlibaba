package com.codechef.cah01.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.codechef.cah01.constant.Constants;
import com.codechef.cah01.model.Months;
import com.codechef.cah01.model.User;
import com.codechef.cah01.repositories.UserRepository;

@Component
public class ConsumptionService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UserRepository userRepository;
	Logger logger = LoggerFactory.getLogger(ConsumptionService.class);

	private HashMap<Integer, Integer> gLoop;
	private HashMap<Integer, Integer> gMonth;

	/**
	 * Fetches the data from CodeChef API and cumulates in Database
	 * 
	 * @param year
	 *            of which the report is generated
	 * @return Object of cumulated report
	 * @throws URISyntaxException
	 */
	public Object populateData(Integer year) throws URISyntaxException {
		LocalDateTime time = LocalDateTime.now();
		Integer yearNow = time.getYear();
		Integer monthNow = 12;
		if (year < 2012 || year > yearNow) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		if (gLoop == null) {
			gLoop = new HashMap<>();
		}
		if (gMonth == null) {
			gMonth = new HashMap<>();
		}
		if (!gLoop.containsKey(year)) {
			gLoop.put(year, 0);
			gMonth.put(year, 1);
		}
		if (yearNow == year) {
			monthNow = time.getMonthValue();
		}
		ResponseEntity<String> response = null;
		boolean f = false;
		for (int i = gMonth.get(year); i <= monthNow; i++) {
			for (int j = gLoop.get(year); j <= 12; j++) {
				try {
					String contestCode = ((year == 2018 && i >= 3) || (year >= 2019))
							? (Months.valueOf(i).get() + ((year - 2000) + "A"))
							: (Months.valueOf(i).get() + ((year - 2000) + ""));
					response = getResponse(contestCode, (25 * j) + "");
					userRepository.updateRecords(new JSONObject(response.getBody()),year);
				} catch (Exception e) {
					logger.error(e.toString());
					if (response != null) {
						gMonth.put(year, i);
						gLoop.put(year, j);
					}
					f = true;
					break;
				}

			}
			if (f)
				break;
			gLoop.put(year, 0);
		}
		return getScoresForYear(year,false);
	}

	private ResponseEntity<String> getResponse(String contestCode, String offset)
			throws URISyntaxException {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		String token = tokenService.getToken();
		headers.add("Authorization", token);
		String urlString = Constants.RANKLIST_ENDPOINT.replace("<resource>", contestCode);
		urlString = urlString.replace("<no1>", offset);
		logger.info("URL Hit: " + urlString);
		URI uri = new URI(urlString);
		RequestEntity<Object> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, uri);
		return restTemplate.exchange(requestEntity, String.class);

	}

	/**
	 * fetches the total score for the year in order
	 * @param year of which the report is fetched
	 * @return List of users with scores
	 */
	public List<User> getScoresForYear(Integer year, Boolean sortAvg) {
		return userRepository.fetchListForYear(year,sortAvg);
	}

}
