package com.example.portraitattest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portraitattest.R;
import com.example.portraitattest.model.FeatureInfo;

public class FeatureAdapter extends RecyclerView.Adapter {
    FeatureInfo featureInfo = null;
    String[] typeArray = {"年龄","性别","人脸数量","颜值","笑的状态","表情","脸型"};
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerViewHandler(LayoutInflater.from(parent.getContext()).inflate(R.layout.feature_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InnerViewHandler) {
            ((InnerViewHandler) holder).tvType.setText(typeArray[position]);
            if (featureInfo != null) {
                switch (position){
                    case 0:
                    ((InnerViewHandler) holder).tvTypeContent.setText(featureInfo.getAge());
                    break;
                    case 1:
                    ((InnerViewHandler) holder).tvTypeContent.setText(featureInfo.getGender());
                    break;
                    case 2:
                    ((InnerViewHandler) holder).tvTypeContent.setText(featureInfo.getFaceNum());
                    break;
                    case 3:
                    ((InnerViewHandler) holder).tvTypeContent.setText(featureInfo.getBeauty());
                    break;
                    case 4:
                    ((InnerViewHandler) holder).tvTypeContent.setText(featureInfo.getExpression());
                    break;
                    case 5:
                    ((InnerViewHandler) holder).tvTypeContent.setText(featureInfo.getEmotion());
                    break;
                    case 6:
                    ((InnerViewHandler) holder).tvTypeContent.setText(featureInfo.getFaceShape());
                    break;
                }
            }else {
                ((InnerViewHandler) holder).tvTypeContent.setText("还没有检测到");
            }
        }
    }
    public void setData( FeatureInfo info){
        this.featureInfo  = info;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 7;
    }
    class InnerViewHandler extends RecyclerView.ViewHolder{
        private TextView tvType;
        private TextView tvTypeContent;


        public InnerViewHandler(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_type);
            tvTypeContent = itemView.findViewById(R.id.tv_type_content);


        }
    }
}
