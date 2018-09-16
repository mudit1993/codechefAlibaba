package com.codechef.cah01.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.codechef.cah01.constant.Constants;
import com.codechef.cah01.model.User;
import com.codechef.cah01.model.UserId;

@Repository
@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
public class UserRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public List<User> fetchListForYear(Integer year,Boolean sortAvg) {
		String query = sortAvg ? Constants.FETCH_REPORT_AVG : Constants.FETCH_REPORT;
		Query eQuery = entityManager.createNativeQuery(query.replace("<year>", year.toString()),
				User.class);
		return eQuery.getResultList();
	}

	public void updateRecords(JSONObject newRecords,Integer year) {

		if (newRecords.has(Constants.RESULT)) {
			JSONArray recordlist = newRecords.getJSONObject(Constants.RESULT).getJSONObject(Constants.DATA)
					.getJSONArray(Constants.CONTENT);
			persistUserList(recordlist,year);
		}
	}

	private void persistUserList(JSONArray recordlist,Integer year) {

		for (int i = 0; i < recordlist.length(); i++) {
			JSONObject nUser = recordlist.getJSONObject(i);
			String userName = nUser.getString(Constants.USERNAME);
			String contestCode = nUser.getString(Constants.CONTEST_CODE);
			UserId userId = new UserId(userName, getYearFromContestCode(nUser, contestCode));
			User user = entityManager.find(User.class, userId);
			Double score = new Double(nUser.getString(Constants.CONTEST_SCORE));
			if (user != null) {
				List<String> contestCodes = user.getContestCode();
				if (!contestCodes.contains(contestCode)) { // unique record
					user.setNoOfContest(user.getNoOfContest() + 1);
					contestCodes.add(contestCode);
					user.setContestCode(contestCodes);
					if(year == 2014 && score<=10) {
						score *= 100;
					}
					user.setScore(user.getScore() + score);
					entityManager.merge(user);
				}
			} else { // new record
				List<String> contestCodes = new ArrayList<>();
				if(year == 2014 && score<=10) {
						score *= 100;
				}
				contestCodes.add(contestCode);
				user = new User(userId, nUser.getString(Constants.COUNTRY), contestCodes,
						score, 1);
				entityManager.persist(user);
			}
		}
	}

	private Integer getYearFromContestCode(JSONObject nUser, String contestCode) {
		if (contestCode.endsWith("A") || contestCode.endsWith("B")) {
			return 2000 + new Integer(nUser.getString(Constants.CONTEST_CODE).substring(contestCode.length() - 3,
					contestCode.length() - 1));
		} else {
			return 2000 + new Integer(nUser.getString(Constants.CONTEST_CODE).substring(contestCode.length() - 2));
		}
	}

}
