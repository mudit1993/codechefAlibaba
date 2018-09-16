package com.codechef.cah01.controller;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codechef.cah01.service.ConsumptionService;

@RestController
public class ReportController {

	@Autowired
	private ConsumptionService consumptionService;

	/**
	 * Fetches, populates and cumulates the scores of a particular year
	 * 
	 * @param year
	 *            of which the scores need to be populated
	 * @return cumulative scores of the year
	 * @throws URISyntaxException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/fetchReports", produces = "application/json")
	public Object fetchPopulateReports(@RequestParam(name = "year") Integer year) throws URISyntaxException {
		return consumptionService.populateData(year);
	}

	/**
	 * Gets the cumulative reports of a particular year
	 * 
	 * @param year
	 *            of which the cumulative reports need to be fetched
	 * @return Cumulative reports of a given year
	 * @throws URISyntaxException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/reports", produces = "application/json")
	public Object getResponse(@RequestParam(name = "year") Integer year,
			@RequestParam(name= "sortAvg",defaultValue="false") Boolean sortAvg) {
		return consumptionService.getScoresForYear(year,sortAvg);
	}

}
