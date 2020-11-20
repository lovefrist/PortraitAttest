package com.example.portraitattest.model.impl;

import android.util.Log;

import com.example.portraitattest.model.inter.IAuthenticateAModel;
import com.example.portraitattest.presenter.impl.AuthenticateAPresenterImpl;
import com.example.portraitattest.utils.Base64Util;
import com.example.portraitattest.utils.HttpDESUtil;

import org.json.JSONObject;

public class AuthenticateAModelImpl implements IAuthenticateAModel {
    private static AuthenticateAModelImpl mInstance;

    public static AuthenticateAModelImpl getInstance() {
        if (mInstance==null) {
            synchronized (AuthenticateAModelImpl.class){
                if (mInstance==null) {
                    mInstance = new AuthenticateAModelImpl();
                }
            }
        }
        return mInstance;
    }
    @Override
    public JSONObject faceSearch(String img) {
        synchronized (this){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("img", img);
                String request = HttpDESUtil.sendRequest("portrait/faceSearch", jsonObject.toString());
                JSONObject object = new JSONObject(request);
                return object;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }
}
