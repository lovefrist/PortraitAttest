package com.example.portraitattest.presenter.impl;

import com.example.portraitattest.model.impl.FeatureAModelImpl;
import com.example.portraitattest.model.inter.IFeatureAModel;
import com.example.portraitattest.presenter.inter.IFeatureAPresenter;
import com.example.portraitattest.view.inter.IFeatureAView;

public class FeatureAPresenterImpl implements IFeatureAPresenter {
    private  static  FeatureAPresenterImpl mInstance;

    public static FeatureAPresenterImpl getInstance() {
        if (mInstance==null) {
            synchronized (FeatureAPresenterImpl.class){
                if (mInstance != null) {
                    mInstance = new FeatureAPresenterImpl();
                }
            }
        }
        return mInstance;
    }

    private FeatureAPresenterImpl(){

    }
}
