package com.xlibao.common.http;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chinahuangxc on 2017/01/22
 */
public class HttpRequest {

    public static String doGet(String host, Map<String, String> headers, Map<String, String> querys) throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpGet request = new HttpGet(buildUrl(host, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }
        return EntityUtils.toString(httpClient.execute(request).getEntity());
    }

    private static String buildUrl(String host, Map<String, String> querys) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }
        return sbUrl.toString();
    }

    private static HttpClient wrapClient(String host) {
        HttpClient httpClient = new DefaultHttpClient();
        if (host.startsWith("https://")) {
            sslClient(httpClient);
        }
        return httpClient;
    }

    private static void sslClient(HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] xcs, String str) {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String str) {
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String post(String url, Map<String, String> params) {
        return post(url, params, "UTF-8");
    }

    public static String post(String url, Map<String, String> params, String charset) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost post = postForm(url, params, charset);
        String body = invoke(httpclient, post);
        httpclient.getConnectionManager().shutdown();
        return body;
    }

    public static String requestUrl(String url, Map<String, String> data) throws IOException {
        HttpURLConnection conn;
        try {
            URL requestUrl = new URL(url);
            conn = (HttpURLConnection) requestUrl.openConnection();
        } catch (MalformedURLException e) {
            return e.getMessage();
        }
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        conn.setDoInput(true);
        conn.setDoOutput(true);

        PrintWriter writer = new PrintWriter(conn.getOutputStream());
        writer.print(httpBuildQuery(data));
        writer.flush();
        writer.close();

        String line;
        BufferedReader bufferedReader;
        StringBuilder sb = new StringBuilder();
        InputStreamReader streamReader = null;
        try {
            streamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
        } catch (IOException e) {
            streamReader = new InputStreamReader(conn.getErrorStream(), "UTF-8");
        } finally {
            if (streamReader != null) {
                bufferedReader = new BufferedReader(streamReader);
                sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
            }
        }
        return sb.toString();
    }

    public static String httpBuildQuery(Map<String, String> data) {
        String ret = "";
        String k, v;
        for (String s : data.keySet()) {
            k = s;
            v = data.get(k);
            try {
                ret += URLEncoder.encode(k, "utf8") + "=" + URLEncoder.encode(v, "utf8");
            } catch (UnsupportedEncodingException ignored) {
            }
            ret += "&";
        }
        return ret.substring(0, ret.length() - 1);
    }

    public static String get(String url) {
        DefaultHttpClient httpclient = new DefaultHttpClient();

        HttpGet get = new HttpGet(url);
        String body = invoke(httpclient, get);

        httpclient.getConnectionManager().shutdown();

        return body;
    }

    private static String invoke(DefaultHttpClient httpclient, HttpUriRequest httPost) {
        HttpResponse response = sendRequest(httpclient, httPost);

        return paseResponse(response);
    }

    private static String paseResponse(HttpResponse response) {
        HttpEntity entity = response.getEntity();

        String body = null;
        try {
            body = EntityUtils.toString(entity);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    private static HttpResponse sendRequest(DefaultHttpClient httpclient, HttpUriRequest httPost) {
        HttpResponse response = null;
        try {
            response = httpclient.execute(httPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static HttpPost postForm(String url, Map<String, String> params, String charset) {
        HttpPost httPost = new HttpPost(url);
        List<NameValuePair> nameValuePairs = new ArrayList<>();

        Set<String> keySet = params.keySet();
        nameValuePairs.addAll(keySet.stream().map(key -> new BasicNameValuePair(key, params.get(key))).collect(Collectors.toList()));
        try {
            httPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httPost;
    }

    public static String sendPost(String url, String xmlObj) {
        String result = null;
        HttpPost httpPost = new HttpPost(url);

        // 解决XStream对出现双下划线的bug
        // XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        // 将要提交给API的数据对象转换成XML格式数据Post给API
        // String postDataXML = xStreamForRequestPostData.toXML(xmlObj);
        // System.out.println("API，POST过去的数据是：");
        // System.out.println(xmlObj);
        // 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(xmlObj, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);

        //设置请求器的配置
        // httpPost.setConfig(requestConfig);
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (ConnectionPoolTimeoutException e) {
            System.out.println("http get throw ConnectionPoolTimeoutException(wait time out)");
        } catch (ConnectTimeoutException e) {
            System.out.println("http get throw ConnectTimeoutException");
        } catch (SocketTimeoutException e) {
            System.out.println("http get throw SocketTimeoutException");
        } catch (Exception e) {
            System.out.println("http get throw Exception");
        } finally {
            httpPost.abort();
        }
        return result;
    }

    public static String pkcs12Post(String certificatePath, String sshKey, String url, String parameters) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream inputStream = new FileInputStream(new File(certificatePath))) {
                keyStore.load(inputStream, sshKey.toCharArray());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, sshKey.toCharArray()).build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();

            HttpPost httpPost = new HttpPost(url);

            httpPost.setEntity(new StringEntity(parameters, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "请求失败";
    }

    public static byte[] readBytes(String urlPath) {
        // 此方法只能用于HTTP协议
        try {
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            DataInputStream in = new DataInputStream(connection.getInputStream());
            byte[] buffer = new byte[4096];
            byte[] datas = new byte[0];

            int count;
            while ((count = in.read(buffer)) > 0) {
                byte[] ioData = new byte[count + datas.length];

                if (datas.length > 0) {
                    System.arraycopy(datas, 0, ioData, 0, datas.length);
                }
                System.arraycopy(buffer, 0, ioData, datas.length, count);
                datas = ioData;
            }
            in.close();
            return datas;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}