package com.fei.pavement.textphone2.intent;

import android.content.Context;
import android.content.Intent;

import com.lidong.photopicker.ImageConfig;
import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.SelectModel;

import java.util.ArrayList;

/**
 * 选择照片
 * Created by foamtrace on 2015/8/25.
 */
public class PhotoPickerIntent extends Intent{

    public PhotoPickerIntent(Context packageContext) {
        super(packageContext, com.lidong.photopicker.PhotoPickerActivity.class);
    }

    public void setShowCarema(boolean bool){
        this.putExtra(com.lidong.photopicker.PhotoPickerActivity.EXTRA_SHOW_CAMERA, bool);
    }

    public void setMaxTotal(int total){
        this.putExtra(com.lidong.photopicker.PhotoPickerActivity.EXTRA_SELECT_COUNT, total);
    }

    /**
     * 选择
     * @param model
     */
    public void setSelectModel(com.lidong.photopicker.SelectModel model){
        this.putExtra(com.lidong.photopicker.PhotoPickerActivity.EXTRA_SELECT_MODE, Integer.parseInt(model.toString()));
    }

    /**
     * 已选择的照片地址
     * @param imagePathis
     */
    public void setSelectedPaths(ArrayList<String> imagePathis){
        this.putStringArrayListExtra(com.lidong.photopicker.PhotoPickerActivity.EXTRA_DEFAULT_SELECTED_LIST, imagePathis);
    }

    /**
     * 显示相册图片的属性
     * @param config
     */
    public void setImageConfig(com.lidong.photopicker.ImageConfig config){
        this.putExtra(com.lidong.photopicker.PhotoPickerActivity.EXTRA_IMAGE_CONFIG, config);
    }
}
