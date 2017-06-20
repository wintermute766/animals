package ru.sberbank.mobile.core.network;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;

import ru.sberbank.mobile.core.parser.ParserException;

/**
 * @author Alex Gontarenko
 */
public class DefaultHttpConnector implements IHttpConnector {

    private static final String LOG_TAG = "DefaultHttpConnector";

    public DefaultHttpConnector() {
    }

    @Override
    public <T> T sendAndReceive(@NonNull Request request, @NonNull IHttpResponseReceiver<T> httpResponseReceiver)
            throws ConnectorException {
        T result = null;
        try {
            result = sendAndReceiveInternal(request, httpResponseReceiver);
        } catch (SocketTimeoutException ex) {
            throw new ConnectorException(ex, ConnectorStatus.NETWORK_UNAVAILABLE);
        } catch (IOException ex) {
            throw new ConnectorException(ex, ConnectorStatus.NETWORK_UNAVAILABLE);
        }
        return result;
    }

    private <T> T sendAndReceiveInternal(@NonNull Request request, @NonNull IHttpResponseReceiver<T> httpResponseReceiver)
            throws IOException, ConnectorException {
        HttpURLConnection connection = null;
        T result = null;
        try {
            connection = prepareHttpURLConnection(request);
            connection.connect();
            if (request.getMethod().equals(Method.POST) && request.getBodySender() != null) {
                request.getBodySender().flushBody(connection.getOutputStream());
            }
            // на телефоне с 4.2.2 connection.getResponseCode() падало с эксепшеном,
            // из-за того что первый вызов connection.getHeaderField(0) возвращал null
            connection.getHeaderField(0);
            int responseCode = connection.getResponseCode();
            httpResponseReceiver.setHttpResponseCode(responseCode);

            InputStream inputStream = getInputStream(connection, responseCode);
            httpResponseReceiver.handleResponse(inputStream, connection.getHeaderFields());
            result = httpResponseReceiver.getResult();
        } catch (ParserException e) {
            throw new ConnectorException(e, ConnectorStatus.PARSER_FAIL);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    //>>> Вспомогательные методы

    @NonNull
    private HttpURLConnection prepareHttpURLConnection(@NonNull Request request)
            throws IOException, ConnectorException {
        URL url = new URL(request.composeURL());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        HttpURLConnection.setFollowRedirects(true);
        connection.setDoInput(true);
        if (request.getMethod().equals(Method.POST)) {
            connection.setDoOutput(true);
        }
        connection.setRequestMethod(request.getMethod().name());
        Map<String, String> headers = request.getHeaders();
        for (String key : headers.keySet()) {
            connection.setRequestProperty(key, headers.get(key));
        }
        connection.setInstanceFollowRedirects(true);
        connection.setUseCaches(false);
        connection.setDefaultUseCaches(false);
        connection.setConnectTimeout(request.getTimeout());
        return connection;
    }

    private InputStream getInputStream(HttpURLConnection connection, int responseCode)
            throws IOException {
        if (isSuccessResponseCode(responseCode)) {
            return connection.getInputStream();
        } else {
            return connection.getErrorStream();
        }
    }

    private boolean isSuccessResponseCode(int responseCode) {
        return responseCode >= 200 && responseCode < 300;
    }

    //<<< Вспомогательные методы
}
