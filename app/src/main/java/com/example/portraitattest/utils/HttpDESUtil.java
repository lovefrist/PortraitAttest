package com.example.portraitattest.utils;

import com.example.portraitattest.info.Constants;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * @author Hacker
 * @filename HttpUtil
 * @datetime 2020/1/23 16:54
 * @effect
 */
public class HttpDESUtil {

    private static String sAesKey;

    public static String sendRequest(String url, String json) throws Exception{
        sAesKey = AESUtile.getAesKey();

        //加密JSON字符串
        String encrypt = AESUtile.encrypt(json, sAesKey);
        //加密AES密钥
        String encrypt1 = RSAUtils.encrypt(sAesKey, Constants.RSA_PUBLIC_KEY_NAME);
        OkHttpClient client =  new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(5000, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(MediaType.parse("Content-Type:Application/json"),encrypt1+encrypt);
//        Request request = new Request.Builder().url("http://192.168.3.91:8080/"+url).post(body).build();
        Request request = new Request.Builder().url("http://39.106.192.145:8080/portrait/"+url).post(body).build();
        String string = Objects.requireNonNull(client.newCall(request).execute().body()).string();
        String decrypt = AESUtile.decrypt(string, sAesKey);
        return decrypt;
    }
}
