package com.fei.pavement.textphoto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.model.LatLng;
import com.fei.pavement.R;
import com.fei.pavement.SearchPage;
import com.fei.pavement.See;
import com.fei.pavement.ThingsService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TextPhotoActivity extends AppCompatActivity {
    public static final String BASE_URL = "http://×××/UploadFileServer/";



    public static void uploadFile(String filePathName1) {

//        JingDu=intent
        // 生成Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://223.4.183.204:8081/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//        ThingsService appService = retrofit.create(ThingsService.class);
//        appService.searchPage("1", "200").enqueue(new Callback<SearchPage>() {
//            @Override
//            public void onResponse(Call<SearchPage> call, Response<SearchPage> response) {
//                SearchPage things1 = response.body();
//                if (things1 != null) {
//                    try {
//                        Log.e("feifei!!!!",things1.toString());
////                        for (int i = 0; i < things1.getList().size(); i++) {
////                            allSee.add(new See(Double.parseDouble(things1.getList().get(i).getLatitude()), Double.parseDouble(things1.getList().get(i).getLongitude())));
////                        }
////                        for (int i = 0; i < allSee.size(); i++) {
////                            Log.e("feifei444", "????");
////                            LatLng point = new LatLng(allSee.get(i).getJingdu(), allSee.get(i).getWeidu());
////                            me_add(point);
////                        }
//                    } catch (Exception e) {
//                        Log.e("feifei", e.getMessage());
//                    }
//                } else {
//                    Log.e("feifei2", "shibai");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SearchPage> call, Throwable t) {
//                Log.e("feifei", t.getMessage());
//            }
//        });

        // 生成Service
        FileUpload uploadService = retrofit.create(FileUpload.class);

        // 要上传的文件
        File file1 = new File(filePathName1);
//        File file2 = new File(filePathName2);

        // 创建 RequestBody，用于封装构建MultipartBody.Part。设置MediaType:application/octet-stream（一说：multipart/form-data，待检验）
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("image/png"), file1);
//        RequestBody requestBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), file2);
        // MultipartBody.Part  和后端约定好Key，这里的partName暂时用"file_key_*"
        Log.e("feifei",file1.getName());
        Log.e("feifei",requestBody1.toString());
        MultipartBody.Part partFile1 = MultipartBody.Part.createFormData("image", file1.getName(), requestBody1);
//        MultipartBody.Part partFile2 = MultipartBody.Part.createFormData("file_key_2", file2.getName(), requestBody2);
        // 添加参数用户名和密码，并且是文本类型，设置MediaType为文本类型（一说：multipart/form-data，待检验）
//        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), "username");
//        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), "password");

        // 执行请求
        Call<UploadPhoto> call = uploadService.uploadDouble(partFile1);
        call.enqueue(new Callback<UploadPhoto>() {

            @Override
            public void onResponse(Call<UploadPhoto> call, Response<UploadPhoto> response) {
                Log.e("feifei","成功了一半");
                UploadPhoto things1 = response.body();
                if (things1 != null) {
                    Log.e("feifei",things1.getCode()+"");
                    Log.e("feifei",things1.getMessage());
                    Log.e("feifei",things1.getData().getUrl());
                } else {
                    Log.e("feifei2", "shibai");
                }
            }

            @Override
            public void onFailure(Call<UploadPhoto> call, Throwable t) {
                Log.e("feifei",t.toString());
                Log.e("feifei","失败了草");
            }
        });
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_photo);
        Button buttonPhoto=findViewById(R.id.buttonPhoto);

//        getSharedPreferences("zhuyi.png")
        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String filename= Environment.getExternalStorageDirectory()+"/zhuyi.png";
                String filename="src/main/res/drawable/zhuyi.png";
                Log.e("feige011",filename);
                uploadFile(filename);
            }
        });

//
////Log应用拦截器
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
////创建OkHttpClient对象
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor)  //okHttp的Log应用拦截器
//                .retryOnConnectionFailure(true)
//                .connectTimeout(15, TimeUnit.SECONDS)
//                .build();
//
////创建Retrofit
//        Retrofit retrofit = new Retrofit.Builder()
//                .client(client) //设置OKHttpClient
//                .baseUrl(BASE_URL)  //设置baseUrl, baseUrl必须后缀"/"
//                .addConverterFactory(GsonConverterFactory.create())  //添加Gson转换器
//                .build();


//        FileUpload fileUpload = retrofit.create(FileUpload.class); //获取FileUpload的API

// 上传单一文件
//        String des = "a image";
//        RequestBody description = RequestBody.create( MediaType.parse("multipart/form-data"), des);
//
//        RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), new File("/sdcard/0/test.jpg"));
//        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
//
//        Call<UploadPhoto> call = fileUpload.uploadFile(description, body);
//        call.enqueue(new Callback<UploadPhoto>() {
//
//            @Override
//            public void onResponse(Call<UploadPhoto> call, Response<UploadPhoto> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<UploadPhoto> call, Throwable t) {
//
//            }
//        });
    }
}