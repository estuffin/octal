package octal;

import org.apache.http.HttpHeaders;

public interface Constants {
	// General
	String UTF_8 = "utf-8";
	Integer START_PAGE_NO = 1;
	String URL_PATH_SEPARATOR = "/";
	String USER_AGENT = "DigitalOcean API Client";
	String JSON_CONTENT_TYPE = "application/json";
	String FORM_URLENCODED_CONTENT_TYPE = "application/x-www-form-urlencoded";
	String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX";
	String RATE_LIMIT_JSON_STRUCT = "\"rate_limit\": { \"limit\": %s, \"remaining\": %s, \"reset\": \"%s\"}";
	String RATE_LIMIT_ELEMENT_NAME = "rate_limit";
	String LINKS_ELEMENT_NAME = "links";
	String META_ELEMENT_NAME = "meta";

	// HTTPS Scheme
	String HTTPS_SCHEME = "https";

	// HTTP Param name
	String PARAM_PAGE_NO = "page";
	String PARAM_PER_PAGE = "per_page";
	int DEFAULT_PAGE_SIZE = 25; // per DO doc

	String NO_CONTENT_JSON_STRUCT = "{\"response\": {\"request_status\": true, \"status_code\": %s}}";

	// HTTP Headers
	String HDR_USER_AGENT = "X-User-Agent";
	String HDR_CONTENT_TYPE = HttpHeaders.CONTENT_TYPE;
	String HDR_AUTHORIZATION = HttpHeaders.AUTHORIZATION;
	String HDR_RATE_LIMIT = "RateLimit-Limit";
	String HDR_RATE_REMAINING = "RateLimit-Remaining";
	String HDR_RATE_RESET = "RateLimit-Reset";
}
