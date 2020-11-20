package com.example.portraitattest.presenter.impl;

import com.example.portraitattest.model.impl.AuthenticateAModelImpl;
import com.example.portraitattest.model.inter.IAuthenticateAModel;
import com.example.portraitattest.presenter.inter.IAuthenticateAPresenter;
import com.example.portraitattest.view.inter.IAuthenticateAView;

import org.json.JSONObject;

public class AuthenticateAPresenterImpl implements IAuthenticateAPresenter {

    private final AuthenticateAModelImpl authenticateAModel;

    private AuthenticateAPresenterImpl(){
        authenticateAModel = AuthenticateAModelImpl.getInstance();
    }
    private static AuthenticateAPresenterImpl mInstance;

    public static AuthenticateAPresenterImpl getInstance() {
        if (mInstance==null) {
            synchronized (AuthenticateAPresenterImpl.class){
                if (mInstance==null) {
                    mInstance = new AuthenticateAPresenterImpl();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void faceSearch(String img) {
        new Thread(()->{
            JSONObject object = authenticateAModel.faceSearch(img);
        }).start();
    }
}
