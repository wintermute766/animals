package ru.sberbank.mobile.core.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import ru.sberbank.mobile.core.bean.io.Encoding;

/**
 * Created by QuickNick on 09/06/16.
 */
public class PostDataBuilder {

    private static final String LOG_TAG = PostDataBuilder.class.getSimpleName();

    public static String buildUrlEncodedData(Map<String, String> nameValuePairs, Encoding encoding) {
        if (nameValuePairs == null) {
            throw new NullPointerException("nameValuePairs is null");
        }
        if (encoding == null) {
            throw new NullPointerException("encoding is null");
        }
        StringBuilder data = new StringBuilder();
        try {
            String strEncoding = encoding.getName();
            for (Map.Entry<String, String> entry : nameValuePairs.entrySet()) {
                data.append(URLEncoder.encode(entry.getKey(), strEncoding))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), strEncoding))
                        .append("&");
            }
            // try to delete last '&' symbol
            if (data.length() != 0) {
                data.deleteCharAt(data.length() - 1);
            }
        } catch (UnsupportedEncodingException e) {
        }
        return data.toString();
    }
}
