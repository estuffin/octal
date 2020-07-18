package octal;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

public class Utils {
	
	public static String getClientIpAddress(HttpServletRequest request) {
	    String xForwardedForHeader = request.getHeader("X-Forwarded-For");
	    
	    if (xForwardedForHeader == null) {
	        return request.getRemoteAddr();
	    } else {
	        return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
	    }
	}
	
}
