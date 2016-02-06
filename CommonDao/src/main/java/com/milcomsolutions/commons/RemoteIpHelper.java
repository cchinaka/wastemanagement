package com.milcomsolutions.commons;
import javax.servlet.http.HttpServletRequest;
 
public class RemoteIpHelper {
 
    private static final String UNKNOWN = "unknown";
 
    public static String getRemoteIpFrom(HttpServletRequest request) {
        String ip = null;
        int tryCount = 1;
 
        while (!isIpFound(ip) && tryCount <= 6) {
            switch (tryCount) {
                case 1:
                    ip = request.getHeader(HttpHeader.X_FORWARDED_FOR.key());
                    break;
                case 2:
                    ip = request.getHeader(HttpHeader.PROXY_CLIENT_IP.key());
                    break;
                case 3:
                    ip = request.getHeader(HttpHeader.WL_PROXY_CLIENT_IP.key());
                    break;
                case 4:
                    ip = request.getHeader(HttpHeader.HTTP_CLIENT_IP.key());
                    break;
                case 5:
                    ip = request.getHeader(HttpHeader.HTTP_X_FORWARDED_FOR.key());
                    break;
                default:
                    ip = request.getRemoteAddr();
            }
 
            tryCount++;
        }
 
        return ip;
    }
 
    private static boolean isIpFound(String ip) {
        return ip != null && ip.length() > 0 && !UNKNOWN.equalsIgnoreCase(ip);
    }
}