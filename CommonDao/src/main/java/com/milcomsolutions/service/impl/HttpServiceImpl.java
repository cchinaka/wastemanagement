package com.milcomsolutions.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLException;

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.ProtocolException;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.milcomsolutions.service.HttpService;


@Service("httpService")
public class HttpServiceImpl implements HttpService, InitializingBean {

    private static final Log LOG = LogFactory.getLog(HttpServiceImpl.class);

    @Value("${http.connection.timeout:10000}")
    private int connectionTimeout;

    @Value("${http.read.timeout:10000}")
    private int readTimeout;

    @Value("${http.proxy.enabled:false}")
    private boolean proxyEnabled;

    @Value("${http.proxy.port:80}")
    private String proxyServerPort;

    @Value("${http.proxy.host:127.0.0.1}")
    private String proxyServer;

    @Value("${http.max.connections:200}")
    private int maxConnection;

    private final Gson gson = new Gson();

    private PoolingHttpClientConnectionManager poolManager;


    @Override
    public <T> T postJson(String endpointURL, Map<String, String> params, final Class<T> returnClass) throws Exception {
        CloseableHttpResponse httpResponse = null;
        ResponseHandler<T> responseHandler = new ResponseHandler<T>() {

            @SuppressWarnings("unchecked")
            @Override
            public T handleResponse(final HttpResponse response) throws IOException {
                T responseT = null;
                StatusLine statusLine = response.getStatusLine();
                HttpEntity entity = response.getEntity();
                if ((statusLine.getStatusCode() >= 300) && (statusLine.getStatusCode() != 401)) {
                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }
                if (entity == null) {
                    throw new ClientProtocolException("Response contains no content");
                }
                String json = EntityUtils.toString(entity);
                EntityUtils.consumeQuietly(entity);
                Object obj = json;
                if (StringUtils.isNotBlank(json)) {
                    if (!returnClass.equals(String.class)) {
                        obj = gson.fromJson(json, returnClass);
                    }
                } else {
                    try {
                        obj = ConstructorUtils.invokeConstructor(returnClass, new Object[] {});
                    } catch (Exception e) {
                    }
                }
                responseT = (T) obj;
                return responseT;
            }
        };
        RequestConfig requestConfig = getRequestConfig();
        CloseableHttpClient httpclient = getHttpClient();
        T httpCallResponse = null;
        try {
            HttpPost httpPost = new HttpPost(endpointURL);
            httpPost.setConfig(requestConfig);
            if (params != null) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (Entry<String, String> param : params.entrySet()) {
                    nvps.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            }
            httpCallResponse = httpclient.execute(httpPost, responseHandler);
        } catch (Exception e) {
            HttpServiceImpl.LOG.error(e);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (Exception e) {
                }
            }
        }
        return httpCallResponse;
    }


    @Override
    public <T> T postJsonAsString(String endpointURL, String content, final Class<T> returnClass) throws Exception {
        T httpCallResponse = null;
        try {
            CloseableHttpResponse httpResponse = null;
            ResponseHandler<T> responseHandler = new ResponseHandler<T>() {

                @SuppressWarnings("unchecked")
                @Override
                public T handleResponse(final HttpResponse response) throws IOException {
                    T responseT = null;
                    StatusLine statusLine = response.getStatusLine();
                    HttpEntity entity = response.getEntity();
                    if ((statusLine.getStatusCode() >= 300) && (statusLine.getStatusCode() != 401)) {
                        throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                    }
                    if (entity == null) {
                        throw new ClientProtocolException("Response contains no content");
                    }
                    String json = EntityUtils.toString(entity);
                    EntityUtils.consumeQuietly(entity);
                    Object obj = null;
                    if (StringUtils.isNotBlank(json)) {
                        obj = gson.fromJson(json, returnClass);
                    } else {
                        try {
                            obj = ConstructorUtils.invokeConstructor(returnClass, new Object[] {});
                        } catch (Exception e) {
                        }
                    }
                    responseT = (T) obj;
                    return responseT;
                }
            };
            RequestConfig requestConfig = getRequestConfig();
            CloseableHttpClient httpclient = getHttpClient();
            try {
                HttpPost httpPost = new HttpPost(endpointURL);
                httpPost.setConfig(requestConfig);
                if (content != null) {
                    // HttpEntity entity = null;
                    // entity = new ByteArrayEntity(content.getBytes("UTF-8"));
                    //
                    @SuppressWarnings("deprecation")
                    StringEntity entity = new StringEntity(content.toString(), HTTP.UTF_8);
                    entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    httpPost.setEntity(entity);
                    httpCallResponse = httpclient.execute(httpPost, responseHandler);
                }
            } catch (Exception e) {
                HttpServiceImpl.LOG.error(e);
            } finally {
                if (httpResponse != null) {
                    try {
                        httpResponse.close();
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
        }
        return httpCallResponse;
    }


    @Override
    public <T> T getJson(String serviceURL, Map<String, String> params, final Class<T> returnClass) throws Exception {
        CloseableHttpResponse httpResponse = null;
        ResponseHandler<T> responseHandler = new ResponseHandler<T>() {

            @SuppressWarnings("unchecked")
            @Override
            public T handleResponse(final HttpResponse response) throws IOException {
                T responseT = null;
                StatusLine statusLine = response.getStatusLine();
                HttpEntity entity = response.getEntity();
                if ((statusLine.getStatusCode() >= 300) && (statusLine.getStatusCode() != 401)) {
                    throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                }
                if (entity == null) {
                    throw new ClientProtocolException("Response contains no content");
                }
                String json = EntityUtils.toString(entity);
                json = json.replaceFirst("rrr", "RRR");
                HttpServiceImpl.LOG.info(String.format("Response from gateway %s", json));
                EntityUtils.consumeQuietly(entity);
                Object obj = json;
                if (StringUtils.isNotBlank(json)) {
                    if (!returnClass.equals(String.class)) {
                        obj = gson.fromJson(json, returnClass);
                    }
                } else {
                    try {
                        obj = ConstructorUtils.invokeConstructor(returnClass, new Object[] {});
                    } catch (Exception e) {
                    }
                }
                responseT = (T) obj;
                return responseT;
            }
        };
        RequestConfig requestConfig = getRequestConfig();
        CloseableHttpClient httpclient = getHttpClient();
        T httpCallResponse = null;
        try {
            if (params != null) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (Entry<String, String> param : params.entrySet()) {
                    nvps.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
                String paramString = URLEncodedUtils.format(nvps, "utf-8");
                serviceURL += paramString;
            }
            HttpGet httpPost = new HttpGet(serviceURL);
            httpPost.setConfig(requestConfig);
            httpCallResponse = httpclient.execute(httpPost, responseHandler);
        } catch (Exception e) {
            HttpServiceImpl.LOG.error(e);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (Exception e) {
                }
            }
        }
        return httpCallResponse;
    }


    @Override
    public String postForm(String paymentGatewayUrl, Map<String, Object> postLoad) {
        String response = StringUtils.EMPTY;
        CloseableHttpResponse httpResponse = null;
        try {
            ResponseHandler<String> rh = new ResponseHandler<String>() {

                @Override
                public String handleResponse(final HttpResponse response) throws IOException {
                    String resp = null;
                    StatusLine statusLine = response.getStatusLine();
                    response.getEntity();
                    if (statusLine.getStatusCode() == 200) {
                        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        StringBuffer result = new StringBuffer();
                        String line = StringUtils.EMPTY;
                        while ((line = rd.readLine()) != null) {
                            result.append(line);
                        }
                        resp = result.toString();
                    } else {
                        HttpServiceImpl.LOG
                                .info(String.format("Error posting to server Response :%s %s", statusLine.getStatusCode(), statusLine.getReasonPhrase()));
                    }
                    return resp;
                }
            };
            CloseableHttpClient httpclient = getHttpClient();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeout).setConnectTimeout(connectionTimeout).build();
            HttpPost post = new HttpPost(paymentGatewayUrl);
            post.setConfig(requestConfig);
            // add header
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            Set<Entry<String, Object>> e = postLoad.entrySet();
            for (Entry<String, Object> ee : e) {
                if (ee.getValue() != null) {
                    postParams.add(new BasicNameValuePair(ee.getKey(), String.valueOf(ee.getValue())));
                }
            }
            String body = URLEncodedUtils.format(postParams, "UTF-8"); // use encoding of request
            StringEntity en = new StringEntity(body);
            // HttpEntity en = new UrlEncodedFormEntity(postParams);
            post.setEntity(en);
            response = httpclient.execute(post, rh);
        } catch (Exception e) {
            HttpServiceImpl.LOG.error(e);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (Exception e) {
                }
            }
        }
        return response;
    }


    private RequestConfig getRequestConfig() {
        RequestConfig requestConfig = null;
        if (proxyEnabled && StringUtils.isNotBlank(proxyServer) && StringUtils.isNotBlank(proxyServerPort) && StringUtils.isNumeric(proxyServerPort)) {
            HttpHost proxy = new HttpHost(proxyServer, Integer.parseInt(proxyServerPort));
            requestConfig = RequestConfig.custom().setSocketTimeout(readTimeout).setConnectTimeout(connectionTimeout).setProxy(proxy).build();
        } else {
            requestConfig = RequestConfig.custom().setSocketTimeout(readTimeout).setConnectTimeout(connectionTimeout).build();
        }
        return requestConfig;
    }


    private CloseableHttpClient getHttpClient() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        SSLConnectionSocketFactory sslsf = getSocketFactory();
        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(poolManager).setSSLSocketFactory(sslsf)
                .setHostnameVerifier(new AllowAllHostnameVerifier()).setRedirectStrategy(new PGLaxRedirectStrategy())
                .setRedirectStrategy(new LaxRedirectStrategy()).build();
        return httpclient;
    }


    private SSLConnectionSocketFactory getSocketFactory() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        SSLContextBuilder sslBuilder = new SSLContextBuilder();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslBuilder.build());
        sslBuilder.loadTrustMaterial(null, new TrustStrategy() {

            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        });
        return sslsf;
    }


    private class PGLaxRedirectStrategy extends LaxRedirectStrategy {

        public PGLaxRedirectStrategy() {
            super();
        }


        @Override
        protected URI createLocationURI(String location) throws ProtocolException {
            if (location.contains("?")) {
                String[] qry = location.split("\\?");
                if (qry.length == 2) {
                    try {
                        location = URIUtil.encodeQuery(location);
                    } catch (URIException e) {
                        HttpServiceImpl.LOG.error(e);
                    }
                }
            }
            return super.createLocationURI(location);
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustStrategy() {

            @Override
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        });
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create().register("http", new PlainConnectionSocketFactory())
                .register("https", sslsf).build();
        poolManager = new PoolingHttpClientConnectionManager(registry);
        poolManager.setMaxTotal(maxConnection);
    }


    @Override
    public <T> T postJsonStringWithBasicAuth(String endpointURL, String content, final Class<T> returnClass, String userName, String password) {
        // TODO Auto-generated method stub
        T httpCallResponse = null;
        try {
            URL aURL = new URL(endpointURL);
            System.out.println("protocol = " + aURL.getProtocol());
            System.out.println("authority = " + aURL.getAuthority());
            System.out.println("host = " + aURL.getHost());
            System.out.println("port = " + aURL.getPort());
            System.out.println("path = " + aURL.getPath());
            System.out.println("query = " + aURL.getQuery());
            System.out.println("filename = " + aURL.getFile());
            System.out.println("ref = " + aURL.getRef());
            CloseableHttpResponse httpResponse = null;
            ResponseHandler<T> responseHandler = new ResponseHandler<T>() {

                @SuppressWarnings("unchecked")
                @Override
                public T handleResponse(final HttpResponse response) throws IOException {
                    T responseT = null;
                    StatusLine statusLine = response.getStatusLine();
                    HttpEntity entity = response.getEntity();
                    if ((statusLine.getStatusCode() >= 300) && (statusLine.getStatusCode() != 401)) {
                        throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                    }
                    if (entity == null) {
                        throw new ClientProtocolException("Response contains no content");
                    }
                    String json = EntityUtils.toString(entity);
                    EntityUtils.consumeQuietly(entity);
                    Object obj = null;
                    if (StringUtils.isNotBlank(json)) {
                        try {
                            obj = gson.fromJson(json, returnClass);
                        } catch (Exception e) {
                            HttpServiceImpl.LOG.error(e);
                        }
                    } else {
                        try {
                            obj = ConstructorUtils.invokeConstructor(returnClass, new Object[] {});
                        } catch (Exception e) {
                        }
                    }
                    responseT = (T) obj;
                    return responseT;
                }
            };
            RequestConfig requestConfig = getRequestConfig();
            try {
                HttpGet httpPost = new HttpGet(endpointURL);
                HttpHost targetHost = new HttpHost(aURL.getHost());
                targetHost = new HttpHost(aURL.getHost(), aURL.getPort(), aURL.getProtocol());
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                        new UsernamePasswordCredentials(userName, password));
                AuthCache authCache = new BasicAuthCache();
                authCache.put(targetHost, new BasicScheme());
                // Add AuthCache to the execution context
                final HttpClientContext context = HttpClientContext.create();
                context.setCredentialsProvider(credsProvider);
                context.setAuthCache(authCache);
                httpPost.setConfig(requestConfig);
                // if (content != null) {
                // HttpEntity entity = null;
                // entity = new ByteArrayEntity(content.getBytes("UTF-8"));
                //
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
                CloseableHttpClient httpclient = getHttpClient(credsProvider);
                httpCallResponse = httpclient.execute(httpPost, responseHandler);
                // }
            } catch (Exception e) {
                HttpServiceImpl.LOG.error(e);
                HttpServiceImpl.LOG.error(e);
            } finally {
                if (httpResponse != null) {
                    try {
                        httpResponse.close();
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
        }
        return httpCallResponse;
    }


    private CloseableHttpClient getHttpClient(CredentialsProvider credsProvider) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        SSLConnectionSocketFactory sslsf = getSocketFactory();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).setConnectionManager(poolManager)
                .setSSLSocketFactory(sslsf).setHostnameVerifier(new AllowAllHostnameVerifier()).setRedirectStrategy(new PGLaxRedirectStrategy()).build();
        return httpclient;
    }


    @Override
    public <T> T postJsonHandlerRedirect(String endpointURL, Map<String, String> params, final Class<T> returnClass) throws Exception {
        CloseableHttpResponse httpResponse = null;
        ResponseHandler<T> responseHandler = new ResponseHandler<T>() {

            @SuppressWarnings("unchecked")
            @Override
            public T handleResponse(final HttpResponse response) throws IOException {
                T responseT = null;
                StatusLine statusLine = response.getStatusLine();
                HttpEntity entity = response.getEntity();
                int statusCode = statusLine.getStatusCode();
                if ((statusCode >= 300) && (statusCode != 401)) {
                    throw new HttpResponseException(statusCode, statusLine.getReasonPhrase());
                }
                if (entity == null) {
                    throw new ClientProtocolException("Response contains no content");
                }
                String json = EntityUtils.toString(entity);
                responseT = (T) json;
                return responseT;
            }
        };
        HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {

            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                exception.printStackTrace();
                if (executionCount >= 5) {
                    // Do not retry if over max retry count
                    return false;
                }
                if (exception instanceof InterruptedIOException) {
                    // Timeout
                    return false;
                }
                if (exception instanceof UnknownHostException) {
                    // Unknown host
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {
                    // Connection refused
                    return false;
                }
                if (exception instanceof SSLException) {
                    // SSL handshake exception
                    return false;
                }
                return false;
            }
        };
        RequestConfig requestConfig = null;
        if (proxyEnabled && StringUtils.isNotBlank(proxyServer) && StringUtils.isNotBlank(proxyServerPort) && StringUtils.isNumeric(proxyServerPort)) {
            HttpHost proxy = new HttpHost(proxyServer, Integer.parseInt(proxyServerPort));
            requestConfig = RequestConfig.custom().setSocketTimeout(readTimeout).setConnectTimeout(connectionTimeout).setProxy(proxy).build();
        } else {
            requestConfig = RequestConfig.custom().setSocketTimeout(readTimeout).setConnectTimeout(connectionTimeout).build();
        }
        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(poolManager).setRetryHandler(requestRetryHandler).build();
        T httpCallResponse = null;
        try {
            HttpPost httpPost = new HttpPost(endpointURL);
            httpPost.setConfig(requestConfig);
            if (params != null) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (Entry<String, String> param : params.entrySet()) {
                    nvps.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            }
            httpCallResponse = httpclient.execute(httpPost, responseHandler);
        } catch (Exception e) {
            throw e;
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (Exception e) {
                }
            }
        }
        return httpCallResponse;
    }


    @Override
    public String postString(String message, String url) throws Exception {
        HttpPost post = new HttpPost(url);
        String xml = message;
        HttpEntity entity = null;
        HttpResponse response = null;
        String result = null;
        try {
            entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
            post.setEntity(entity);
            // post.addHeader("content-type", "application/x-www-form-urlencoded");
            response = getHttpClient().execute(post);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if ((statusCode >= 300) && (statusCode != 401)) {
                throw new HttpResponseException(statusCode, statusLine.getReasonPhrase());
            }
            result = EntityUtils.toString(response.getEntity());
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } catch (ParseException e) {
            throw e;
        }
        return result;
    }
}
