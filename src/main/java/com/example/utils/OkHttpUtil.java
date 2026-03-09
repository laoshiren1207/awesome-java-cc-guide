package com.example.utils;


import okhttp3.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {


    private static final int READ_TIMEOUT = 100;
    private static final int CONNECT_TIMEOUT = 60;
    private static final int WRITE_TIMEOUT = 60;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient okHttpClient;

    private OkHttpUtil() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        // 读取超时
        clientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        // 连接超时
        clientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        // 写入超时
        clientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient = clientBuilder.build();
    }

    private static class OkHttpUtilHolder {
        private static final OkHttpUtil INSTANCE = new OkHttpUtil();
    }

    /**
     * 单例模式获取 OkHttpUtil
     *
     * @return {@link OkHttpUtil}
     */
    public static OkHttpUtil getInstance() {
        return OkHttpUtilHolder.INSTANCE;
    }

    /**
     * GET，同步方式，获取网络数据
     *
     * @param url 请求地址
     *
     * @return Response
     */
    public String getData(String url) {
        // 构造 Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call，得到 Response
        try (Response response = call.execute()) {
            if (response != null && response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * @param url URL
     * @param headers 请求头
     *
     * @return 响应
     */
    public String getDataWithHeaders(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        if (headers != null) {
            headers.forEach(builder::addHeader);
        }
        Request request = builder.get()
                .url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call，得到 Response
        try (Response response = call.execute()) {
            if (response != null && response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * POST 请求，同步方式，提交数据
     *
     * @param url        请求地址
     * @param bodyParams 请求参数
     *
     * @return Response
     */
    public String postData(String url, Map<String, String> bodyParams) {
        // 构造 RequestBody
        RequestBody body = setRequestBody(bodyParams);
        // 构造 Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call，得到 Response
        try (Response response = call.execute()) {
            if (response != null && response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * post header
     *
     * @param url url
     * @param bodyParams ?body
     * @param headers header
     *
     * @return 响应
     */
    public String postDataWithHeaders(String url, Map<String, String> bodyParams, Map<String, String> headers) {
        RequestBody body = setRequestBody(bodyParams);
        // 构造 Request
        Request.Builder requestBuilder = new Request.Builder();
        if (headers != null) {
            headers.forEach(requestBuilder::addHeader);
        }
        Request request = requestBuilder.post(body).url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call，得到 Response
        try (Response response = call.execute()) {
            if (response != null && response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * POST 请求，同步方式，提交数据
     *
     * @param url        请求地址
     * @param bodyParams 请求参数
     *
     * @return Response
     */
    public String postMap(String url, Map<String, Object> bodyParams) {

        // 构造 RequestBody
        RequestBody body = setRequestMap(bodyParams);
        // 构造 Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call，得到 Response
        try (Response response = call.execute()) {
            if (response != null && response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    /**
     * GET 请求，异步方式，获取网络数据
     *
     * @param url       请求地址
     * @param myNetCall 回调函数
     */
    public void getDataAsync(String url, final MyNetCall myNetCall) {
        // 构造 Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myNetCall.success(call, response);
            }
        });
    }

    /**
     * POST 请求，异步方式，提交数据
     *
     * @param url        请求地址
     * @param bodyParams 请求参数
     * @param myNetCall  回调函数
     */
    public void postDataAsync(String url, Map<String, String> bodyParams, final MyNetCall myNetCall) {
        // 构造 RequestBody
        RequestBody body = setRequestBody(bodyParams);
        // 构造 Request
        buildRequest(url, myNetCall, body);
    }

    /**
     * 同步 POST 请求，使用 JSON 格式作为参数
     *
     * @param url  请求地址
     * @param json JSON 格式参数
     *
     * @return 响应结果
     *
     * @throws IOException 异常
     */
    public String postJson(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json,JSON );
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response != null && response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
    }

    /**
     * 同步 POST 请求，使用 JSON 格式作为参数
     *
     * @param url  请求地址
     * @param json JSON 格式参数
     *
     * @return 响应结果
     *
     */
    public String postJsonWithHeaders(String url, String json, Map<String, String> headers) {
        RequestBody body = RequestBody.create(JSON, json);
        Request.Builder builder = new Request.Builder();
        if (headers != null) {
            headers.forEach(builder::addHeader);
        }
        Request request = builder.url(url)
                .post(body)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response != null && response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    /**
     * 异步 POST 请求，使用 JSON 格式作为参数
     *
     * @param url       请求地址
     * @param json      JSON 格式参数
     * @param myNetCall 回调函数
     *
     * @throws IOException 异常
     */
    public void postJsonAsync(String url, String json, final MyNetCall myNetCall) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        // 构造 Request
        buildRequest(url, myNetCall, body);
    }

    /**
     * 构造 POST 请求参数
     *
     * @param bodyParams 请求参数
     *
     * @return {@link RequestBody}
     */
    private RequestBody setRequestBody(Map<String, String> bodyParams) {
        RequestBody body = null;
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        if (bodyParams != null) {
            Iterator<String> iterator = bodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next();
                formEncodingBuilder.add(key, bodyParams.get(key));
            }
        }
        body = formEncodingBuilder.build();
        return body;
    }


    private RequestBody setRequestMap(Map<String, Object> bodyParams) {
        RequestBody body = null;
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        if (bodyParams != null) {
            Iterator<String> iterator = bodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next();
                if (Objects.isNull(bodyParams.get(key))) {
                    continue;
                }
                formEncodingBuilder.add(key, String.valueOf(bodyParams.get(key)));
            }
        }
        body = formEncodingBuilder.build();
        return body;
    }


    /**
     * 构造 Request 发起异步请求
     *
     * @param url       请求地址
     * @param myNetCall 回调函数
     * @param body      {@link RequestBody}
     */
    private void buildRequest(String url, MyNetCall myNetCall, RequestBody body) {
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myNetCall.success(call, response);
            }
        });
    }

    /**
     * 自定义网络回调接口
     */
    public interface MyNetCall {
        /**
         * 请求成功的回调处理
         *
         * @throws IOException 异常
         */
        void success(Call call, Response response) throws IOException;

        /**
         * 请求失败的回调处理
         *
         * @param call {@link Call}
         * @param e    异常
         */
        void failed(Call call, IOException e);
    }

    public String postXml(String url, String xml) throws IOException {
        MediaType mediaType = MediaType.parse("application/xml");
        RequestBody body = RequestBody.create(mediaType, xml);
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/xml")
                .build();
        try (Response response = getInstance().okHttpClient.newCall(request).execute()) {
            if (response != null && response.body() != null) {
                return response.body().string();
            }
            throw new IOException("Unexpected empty response");
        }
    }

    /**
     * GET，同步方式，获取网络数据
     *
     * @param url 请求地址
     *
     * @return Response
     */
    public Response getResponse(String url) throws IOException {
        // 构造 Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call，得到 Response
        return call.execute();
    }

}
