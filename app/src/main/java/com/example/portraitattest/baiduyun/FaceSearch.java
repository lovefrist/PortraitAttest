package com.example.portraitattest.baiduyun;

import com.example.portraitattest.utils.Base64Util;
import com.example.portraitattest.utils.FileUtil;
import com.example.portraitattest.utils.GsonUtils;
import com.example.portraitattest.utils.HttpUtil;

import java.util.*;

/**
 * 人脸搜索
 */
public class FaceSearch {

    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public static String faceSearch(byte[] img) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/search";
        try {
            Map<String, Object> map = new HashMap<>();
            String imgData = Base64Util.encode(img);
            map.put("image", imgData);
            map.put("group_id_list", "home,family");
            map.put("image_type", "BASE64");
            String param = GsonUtils.toJson(map);
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AuthService.getAuth("uw785yc31e9ec1LuQd8MFYqD","NUaxTQwCpNdz3LUkYxMGGtL9v97VeX4b");
            String result = HttpUtil.post(url, accessToken, "application/json", param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String faceDetect(byte[] img) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/detect";
        try {
            Map<String, Object> map = new HashMap<>();
            String imgData = Base64Util.encode(img);
            map.put("image", imgData);
            map.put("face_field", "faceshape,facetype");
            map.put("image_type", "BASE64");

            String param = GsonUtils.toJson(map);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AuthService.getAuth("uw785yc31e9ec1LuQd8MFYqD","NUaxTQwCpNdz3LUkYxMGGtL9v97VeX4b");

            String result = HttpUtil.post(url, accessToken, "application/json", param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String registerPortrait(String img ,String group,String userName) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
        try {
            Map<String, Object> map = new HashMap<>();
            String imgData = Base64Util.encode(img.getBytes());
            map.put("image", imgData);
            map.put("group_id_list", group);
            map.put("image_type", "BASE64");
            map.put("group_id", group);
            map.put("user_id", userName);
            map.put("liveness_control", "NORMAL");
            map.put("quality_control", "LOW");
            String param = GsonUtils.toJson(map);
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AuthService.getAuth("uw785yc31e9ec1LuQd8MFYqD","NUaxTQwCpNdz3LUkYxMGGtL9v97VeX4b");
            String result = HttpUtil.post(url, accessToken, "application/json", param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}