package org.cell.froopyland_interface.utils;

import com.alibaba.fastjson2.JSON;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.cell.froopyland_interface.entity.constants.InterfaceApi;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author yozora
 * @description okhttp util
 **/
public class OkHttpUtil {

    private static volatile OkHttpClient okHttpClient = null;

    private static final Semaphore semaphore = new Semaphore(0);
    private Map<String, String> headerMap;
    private Map<String, String> paramMap;
    private String url;
    private Request.Builder request;

    private OkHttpUtil(boolean isProxy) {
        if (okHttpClient == null) {
            synchronized (OkHttpUtil.class) {
                if (okHttpClient == null) {
                    TrustManager[] trustManagers = buildTrustManagers();
//                    isProxy = true;
                    if (isProxy) {
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
                        okHttpClient = new OkHttpClient.Builder().proxyAuthenticator(new Authenticator() {
                            @Override
                            public Request authenticate(Route route, Response response) throws IOException {
                                String credential = Credentials.basic("", "");
                                return response.request().newBuilder().header("Proxy-Authorization", credential).build();
                            }
                        }).proxy(proxy).connectTimeout(15, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).sslSocketFactory(createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0]).hostnameVerifier((hostName, session) -> true).retryOnConnectionFailure(true).connectionPool(new ConnectionPool(10, 10, TimeUnit.SECONDS)).build();
                    } else {
                        okHttpClient = new OkHttpClient.Builder()
                                .connectTimeout(15, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).sslSocketFactory(createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0]).hostnameVerifier((hostName, session) -> true).retryOnConnectionFailure(true).connectionPool(new ConnectionPool(10, 10, TimeUnit.SECONDS)).build();
                    }
                    addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36");
                }
            }
        }
    }

    /**
     * description: get collections
     *
     * @param chain chain name
     * @param next  next
     * @return java.lang.String
     * @author yozora
     **/
    public static String getOpenSeaNonFungibleTokenCollections(String chain, String next) {
        OkHttpUtil okHttpUtil = OkHttpUtil.builder(false)
                .url(InterfaceApi.OPEN_SEA_COLLECTIONS_URL)
                .addParam("chain", chain)
                .addParam("limit", "100");
        if (next != null) {
            okHttpUtil.addParam("next", next);
        }
        return okHttpUtil.addHeader("X-API-KEY", InterfaceApi.K_O).addHeader("Content-Type", "application/json; charset=utf-8").get().sync();
    }

    /**
     * description: get openSea NFT data by userAddress
     *
     * @param userAddress user address
     * @param next        next
     * @param pageSize    page size
     * @param chainName   chain name
     * @return java.lang.String
     * @author yozora
     **/
    public static String getOpenSeaNonFungibleTokenByUserAddress(String userAddress, String next, Integer pageSize, String chainName) {
        OkHttpUtil okHttpUtil = OkHttpUtil.builder(false)
                .url(InterfaceApi.OPEN_SEA_ACCOUNT_URL.replace("{chain}", chainName).replace("{address}", userAddress))
                .addParam("limit", String.valueOf(pageSize));
        if (next != null) {
            okHttpUtil.addParam("next", next);
        }
        return okHttpUtil.addHeader("X-API-KEY", InterfaceApi.K_O).addHeader("Content-Type", "application/json; charset=utf-8").get().sync();
    }

    /**
     * description: get openSea NFT data
     *
     * @param contractAddress contract address
     * @param chainName       chain name
     * @param next            next
     * @return java.lang.String
     * @author yozora
     **/
    public static String getOpenSeaNonFungibleToken(String contractAddress, String chainName, String next) {
        OkHttpUtil okHttpUtil = OkHttpUtil.builder(false)
                .url(InterfaceApi.OPEN_SEA_ASSETS_URL.replace("{chain}", chainName).replace("{address}", contractAddress))
                .addParam("limit", "50");
        if (StringUtils.isNoneBlank(next)) {
            okHttpUtil.addParam("next", next);
        }
        return okHttpUtil.addHeader("X-API-KEY", InterfaceApi.K_O).addHeader("Content-Type", "application/json; charset=utf-8").get().sync();
    }

    /**
     * description: get openSea NFT detail data
     *
     * @param contractAddress contract address
     * @param chainName       chain name
     * @param identifier      identifier
     * @return java.lang.String
     * @author yozora
     **/
    public static String getOpenSeaNonFungibleTokenDetail(String contractAddress, String chainName, String identifier) {
        OkHttpUtil okHttpUtil = OkHttpUtil.builder(false)
                .url(InterfaceApi.OPEN_SEA_ASSETS_DETAIL_URL
                        .replace("{chain}", chainName)
                        .replace("{address}", contractAddress)
                        .replace("{identifier}", identifier)
                );
        return okHttpUtil.addHeader("X-API-KEY", InterfaceApi.K_O).addHeader("Content-Type", "application/json; charset=utf-8").get().sync();
    }

    /**
     * description: detailed information about a specified contract
     *
     * @param contractAddress NFT contract address
     * @return java.lang.String
     * @author yozora
     **/
    public static String getNonFungibleTokenDescription(String contractAddress, int interfaceType) {
        if (interfaceType == InterfaceApi.INTERFACE_OPEN_SEA) {
            OkHttpUtil okHttpUtil = OkHttpUtil.builder(false).url(InterfaceApi.OPEN_SEA_CONTRACT_URL, contractAddress);
            return okHttpUtil.addHeader("X-API-KEY", InterfaceApi.K_O).addHeader("Content-Type", "application/json; charset=utf-8").get().sync();
        } else {
            OkHttpUtil okHttpUtil = OkHttpUtil.builder(false).url(InterfaceApi.NFT_SCAN_CONTRACT_URL, contractAddress);
            return okHttpUtil.addHeader("X-API-KEY", InterfaceApi.K_N).addHeader("Content-Type", "application/json; charset=utf-8").get().sync();
        }
    }

    /**
     * description: create OkHttpUtil instance
     *
     * @param isProxy is proxy
     * @return com.metaverse.trust.utils.OkHttpUtil
     * @author yozora
     **/
    public static OkHttpUtil builder(boolean isProxy) {
        return new OkHttpUtil(isProxy);
    }

    /**
     * description: set url
     *
     * @param url url
     * @return com.metaverse.trust.utils.OkHttpUtil
     * @author yozora
     **/
    public OkHttpUtil url(String url) {
        this.url = url;
        return this;
    }

    /**
     * description: set url
     *
     * @param url    url
     * @param params json params
     * @return com.metaverse.trust.utils.OkHttpUtil
     * @author yozora
     **/
    public OkHttpUtil url(String url, String... params) {
        StringBuilder urlBuilder = new StringBuilder(url);
        for (String param : params) {
            urlBuilder.append("/").append(param);
        }
        url = urlBuilder.toString();
        this.url = url;
        return this;
    }


    /**
     * description: add params
     *
     * @param key   key
     * @param value value
     * @return com.metaverse.trust.utils.OkHttpUtil
     * @author yozora
     **/
    public OkHttpUtil addParam(String key, String value) {
        if (paramMap == null) {
            paramMap = new LinkedHashMap<>(16);
        }
        paramMap.put(key, value);
        return this;
    }

    /**
     * description: add headers
     *
     * @param key   key
     * @param value value
     * @return com.metaverse.trust.utils.OkHttpUtil
     * @author yozora
     **/
    public OkHttpUtil addHeader(String key, String value) {
        if (headerMap == null) {
            headerMap = new LinkedHashMap<>(16);
        }
        headerMap.put(key, value);
        return this;
    }

    /**
     * description: get request
     *
     * @return com.metaverse.trust.utils.OkHttpUtil
     * @author yozora
     **/
    public OkHttpUtil get() {
        request = new Request.Builder().get();
        StringBuilder urlBuilder = new StringBuilder(url);
        if (paramMap != null) {
            urlBuilder.append("?");
            try {
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8)).append("&");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        request.url(urlBuilder.toString());
        return this;
    }

    /**
     * description: post request
     *
     * @param isJsonPost is json post
     * @return com.metaverse.trust.utils.OkHttpUtil
     * @author yozora
     **/
    public OkHttpUtil post(boolean isJsonPost) {
        RequestBody requestBody;
        if (isJsonPost) {
            String json = "";
            if (paramMap != null) {
                json = JSON.toJSONString(paramMap);
            }
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        } else {
            FormBody.Builder formBody = new FormBody.Builder();
            if (paramMap != null) {
                paramMap.forEach(formBody::add);
            }
            requestBody = formBody.build();
        }
        request = new Request.Builder().post(requestBody).url(url);
        return this;
    }

    /**
     * description: sync request
     *
     * @return java.lang.String
     * @author yozora
     **/
    public String sync() {
        setHeader(request);
        try {
            Response response = okHttpClient.newCall(request.build()).execute();
            assert response.body() != null;
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "request fail: " + e.getMessage();
        }
    }

    /**
     * description: async request
     *
     * @return java.lang.String
     * @author yozora
     **/
    public String async() {
        StringBuilder buffer = new StringBuilder();
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                semaphore.release();
                buffer.append("request fail: ").append(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                semaphore.release();
                assert response.body() != null;
                buffer.append(response.body().string());
            }
        });
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * description: async request
     *
     * @param callback com.metaverse.trust.utils.OkHttpUtil.Callback
     * @author yozora
     **/
    public void async(Callback callback) {
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(callback);
    }

    /**
     * description: set header
     *
     * @param request com.squareup.okhttp.Request
     * @author yozora
     **/
    private void setHeader(Request.Builder request) {
        if (headerMap != null) {
            try {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * description: create SSLSocketFactory
     *
     * @param trustAllCerts trustAllCerts
     * @return javax.net.ssl.SSLSocketFactory
     * @author yozora
     **/
    private static SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }

    private static TrustManager[] buildTrustManagers() {
        return new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }};
    }

    /**
     * 自定义一个接口回调
     */
    public interface ICallBack {

        void onSuccessful(Call call, String data);

        void onFailure(Call call, String errorMsg);

    }

}
