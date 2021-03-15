package com.fei.pavement.textphone2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.fei.pavement.textphoto.UploadPhoto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

//import retrofit2.http.Multipart;
//import retrofit2.http.Part;



/**
 * Created by lidong on 2016/1/28.
 */
public class FileUploadManager {

//    private static final String ENDPOINT = "http://223.4.183.204:8081";
private static final String ENDPOINT = "http://dyc.vip3gz.idcfengye.com";

    private static String TAG = FileUploadManager.class.getSimpleName();

    public interface FileUploadService {
        /**
         * 上传一张图片
         * @param //description
         * @param imgs
         * @return
         *///file"; filename="image.png"
        @Multipart
        @POST("/Myproject/uploadImage")
        Call<UploadPhoto> uploadImage(@Part MultipartBody.Part imgs, @Part("latitude") Double latitude,@Part("longitude") Double longitude);
//                @Part("fileName") String description,



        /**
         * 上传6张图片
          * @param description
         * @param imgs1
         * @param imgs2
         * @param imgs3
         * @param imgs4
         * @param imgs5
         * @param imgs6
         * @return
         */
        @Multipart
        @POST("/Myproject/uploadImage")
        Call<String> uploadImage(@Part("description") String description,
                                 @Part("file\"; filename=\"image.png\"") RequestBody imgs1,
                                 @Part("file\"; filename=\"image.png\"") RequestBody imgs2,
                                 @Part("file\"; filename=\"image.png\"") RequestBody imgs3,
                                 @Part("file\"; filename=\"image.png\"") RequestBody imgs4,
                                 @Part("file\"; filename=\"image.png\"") RequestBody imgs5,
                                 @Part("file\"; filename=\"image.png\"") RequestBody imgs6);

        /**
         * 简便写法
         * @param description
         * @param imgs1
         * @return
         */
        @Multipart
        @POST("/Myproject/uploadImage")
        Call<String> uploadImage(@Part("description") String description, @PartMap
                Map<String, RequestBody> imgs1);
    }

    private static final Retrofit sRetrofit = new Retrofit .Builder()
            .baseUrl(ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final FileUploadService apiManager = sRetrofit.create(FileUploadService.class);



//    public static void compressBitmap(String filePath, File file){
//        // 数值越高，图片像素越低
//        int inSampleSize = 2;
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        //采样率
//        options.inSampleSize = inSampleSize;
//        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        // 把压缩后的数据存放到baos中
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 ,baos);
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(baos.toByteArray());
//            fos.flush();
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * 上传图片
     * @param paths
     * @param
     */
    static File fileImage;
    public static void upload(ArrayList<String> paths, Double JingDu, Double WeiDu, final MainActivity2 activity2){
//        RequestBody[] requestBody= new RequestBody[6];
////        RequestBody[] requestBody= new RequestBody[6];
        MultipartBody.Part[] body=new MultipartBody.Part[6];
        if (paths.size()>0) {
//            for (int i=0;i<paths.size();i++) {
                File file=new File(paths.get(0));
                ImageCompress.Companion.getImages(new File(paths.get(0)),activity2,paths,JingDu,WeiDu,activity2);
//                fileImage=ImageCompress.Companion.getFile2();
//
//                fileImage=new File(paths.get(0));
//                Log.e("feifeiover","完成");
////                activity2.mPhoneImageView.setImageURI(Uri.fromFile(file));
////                RequestBody requestBody1=RequestBody.create(MediaType.parse("multipart/form-data"),new File(paths.get(i)));
////                MultipartBody.Part photo=MultipartBody.Part.createFormData("image")
//                RequestBody requestFile =RequestBody.create(MediaType.parse("multipart/form-data"), file);
//// MultipartBody.Part is used to send also the actual file name
//                 body[0] =MultipartBody.Part.createFormData("image", file.getName(), requestFile);
//                 Log.e("feifei1111",file.getName()+""+requestFile);
//                requestBody[i] =
//                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(paths.get(i)));
//            }
        }
//        Call<String> call = apiManager.uploadImage( desp,requestBody[0],requestBody[1],requestBody[2],requestBody[3],requestBody[4],requestBody[5]);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                Log.d(TAG, "onResponse() called with: " + "call = [" + call + "], response = [" + response + "]");
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.d(TAG, "onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
//            }
//        });

//        Call<UploadPhoto> call1=apiManager.uploadImage(body[0],WeiDu,JingDu);
//        call1.enqueue(new Callback<UploadPhoto>() {
//            @Override
//            public void onResponse(Call<UploadPhoto> call, Response<UploadPhoto> response) {
//                Log.e("fefeichenggong", "onResponse() called with: " + "call = [" + call.toString() + "], response = [" + response.toString() + "]");
//                UploadPhoto things1 = response.body();
//                if (things1 != null) {
//                    try {
//                        Log.e("feifeishilezhi","?????");
//
//                        Log.e("feifeishilezhi",things1.getCode()+"");
//                        Log.e("feifeishilezhi",things1.getMessage());
////                        Log.e("feifeishilezhi",things1.getData().toString());
//
//                        if(things1.getCode()==1){
//                            Log.e("feifeishilezhi",things1.getData().getUrl());
//                            activity2.initPhone(things1.getData().getUrl());
//                            Toast.makeText(activity2,"检查到异常路面",Toast.LENGTH_SHORT).show();
//                            activity2.unLoadBar();
//                        }else{
//                            if(fileImage!=null){
//                                activity2.mPhoneImageView.setImageURI(Uri.fromFile(fileImage));
//                            }
//                            activity2.unLoadBar();
//                            Toast.makeText(activity2,things1.getMessage(),Toast.LENGTH_SHORT).show();
//                        }
////                        Toast.makeText(activity2,things1.getMessage().toString(),Toast.LENGTH_SHORT).show();
//
//
//                    } catch (Exception e) {
//                        Toast.makeText(activity2,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
//                        activity2.unLoadBar();
//                        Log.e("feifei", e.getMessage());
//                    }
//                } else {
////                    activity2.mPhoneImageView.setImageURI(Uri.fromFile(fileImage));
//                    Toast.makeText(activity2,"上传超时",Toast.LENGTH_SHORT).show();
//                    activity2.unLoadBar();
//                    Log.e("feifei2", "shibai");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UploadPhoto> call, Throwable t) {
//                Toast.makeText(activity2,"上传超时",Toast.LENGTH_SHORT).show();
//                activity2.unLoadBar();
//                Log.e("feifei失败草","onFailure() called with: " + "call = [" + call.toString() + "], t = [" + t + "]");
//            }
//        });

    }

    /**
     *
     * @param paths
     * @param desp
     */
    public static void uploadMany(ArrayList<String> paths,String desp){
        Map<String,RequestBody> photos = new HashMap<>();
        if (paths.size()>0) {
            for (int i=0;i<paths.size();i++) {
                 photos.put("photos"+i+"\"; filename=\"icon.png",  RequestBody.create(MediaType.parse("multipart/form-data"), new File(paths.get(i))));
            }
        }
        Call<String> call1=apiManager.uploadImage(desp,photos);
        call1.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("fefeichenggong", "onResponse() called with: " + "call = [" + call + "], response = [" + response + "]");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("feifei失败草","onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
            }
        });
//        Call<String> stringCall = apiManager.uploadImage(desp, photos);
//        stringCall.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                Log.d(TAG, "onResponse() called with: " + "call = [" + call + "], response = [" + response + "]");
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.d(TAG, "onFailure() called with: " + "call = [" + call + "], t = [" + t + "]");
//            }
//        });
    }
}
