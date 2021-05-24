package com.a1qa.common;

/**
 * Created by p.ordenko on 24.06.2015, 18:11.
 */
public class JstlFunction {

    /**
     * Truncate unnecessary params from URI
     * @param address Absolute or relative URI with all query params (e.g., http://localhost/test?var1=val1&var2=val2)
     * @param params Params which should be truncated from "address" argument. Can be multiple params, specified
     *               through coma ","
     * @return Address argument without "params" argument. Also, to end of string will be placed necessary symbol
     * ("?" or "&") that can be concatenated with necessary request param.
     * Return string example: "http://localhost/test?var1=val1&" or "http://localhost/test?"
     */
    public static String truncateParams(String address, String params) {
        String[] paramsToRemove = params.split(",");
        StringBuilder sb = new StringBuilder();
        String[] addressParts = address.split("\\?");
        String[] paths = addressParts[0].split("/");
        sb.append(paths[paths.length-1]).append("?");
        if (addressParts.length > 1) {
            String[] splittedParams = addressParts[1].split("&");
            for (String param : splittedParams) {
                if (!isStartsWiths(param, paramsToRemove)) {
                    sb.append(param).append("&");
                }
            }
        }
        return sb.toString();
    }

    //////////////////
    // Private methods
    //////////////////

    private static boolean isStartsWiths(String startsWith, String... params) {
        for (String param : params) {
            if (startsWith.startsWith(param.trim())) {
                return true;
            }
        }
        return false;
    }

}
