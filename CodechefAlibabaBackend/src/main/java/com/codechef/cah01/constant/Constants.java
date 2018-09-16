package com.codechef.cah01.constant;

public class Constants {

	public static final String TOKEN_ENDPOINT = "https://api.codechef.com/oauth/token";
	public static final String CLIENT_CREDENTIALS = "client_credentials";

	public static final String RESULT = "result";

	public static final String RANKLIST_ENDPOINT = "https://api.codechef.com/rankings/<resource>?sortOrder=asc&offset=<no1>&limit=30";
	public static final String FETCH_REPORT_AVG = "SELECT * from codechef_user WHERE year = <year> ORDER BY (codechef_user.`SCORE`/codechef_user.`NOOFCONTEST`) DESC";
	public static final String FETCH_REPORT = "SELECT * from codechef_user WHERE year = <year> ORDER BY score DESC";
	public static final String STATUS = "status";
	public static final Object OK = "OK";
	public static final String BEARER = "Bearer ";
	public static final String DATA = "data";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String CONTENT = "content";
	public static final String USERNAME = "username";
	public static final String CONTEST_CODE = "contestCode";
	public static final String CONTEST_SCORE = "totalScore";
	public static final String COUNTRY = "country";
	
}
