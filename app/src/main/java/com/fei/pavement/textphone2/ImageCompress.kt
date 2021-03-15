package com.fei.pavement.textphone2

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fei.pavement.textphone2.FileUploadManager.FileUploadService
import com.fei.pavement.textphoto.UploadPhoto
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.shouheng.compress.Compress
import me.shouheng.compress.strategy.Strategies
import me.shouheng.compress.strategy.config.ScaleMode
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*

class ImageCompress :AppCompatActivity() {
    companion object{
        lateinit var file2:File;
        @SuppressLint("CheckResult")
        fun getImages(file: File, mContext: Context,  paths: ArrayList<String?>,
                      JingDu: Double?,
                      WeiDu: Double?,
                      activity2: MainActivity2) {
//        val compress = Compress.with(mContext, file)
//
//        val compressor = compress
//            .strategy(Strategies.compressor())
//            .setConfig(Bitmap.Config.ARGB_8888)
//            .setMaxHeight(100f)
//            .setMaxWidth(100f)
//            .setScaleMode(ScaleMode.SCALE_WIDTH)
//
//        compressor.launch()
//        // 方式 2：将压缩任务转换成 Flowable，使用 RxJava 指定任务的线程和获取结果的线程
//        val d = compressor
//            .asFlowable()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                ToastUtils.showShort("Success [Compressor,File,Flowable] $it")
//                displayResult(it.absolutePath)
//            }, {
//                ToastUtils.showShort("Error [Compressor,File,Flowable] : $it")
//            })
            file2=file


            Compress.with(mContext, file)
                .strategy(Strategies.compressor())
                .setConfig(Bitmap.Config.ARGB_8888)
                .setMaxHeight(1280f)
                .setMaxWidth(720f)
                .setScaleMode(ScaleMode.SCALE_WIDTH)
                .asFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //压缩成功 获取file文件
                    file2=it
                    Log.e("feifeiover","over")
                    upload(paths,JingDu,WeiDu,activity2)
                }, {
                    //压缩失败
                    activity2.unLoadBar()
                    Toast.makeText(mContext,"上传失败",Toast.LENGTH_SHORT).show()
                })


        }

        var fileImage: File? = null
        fun upload(
            paths: ArrayList<String?>,
            JingDu: Double?,
            WeiDu: Double?,
            activity2: MainActivity2
        ) {
//        RequestBody[] requestBody= new RequestBody[6];
////        RequestBody[] requestBody= new RequestBody[6];
            val body =
                arrayOfNulls<MultipartBody.Part>(6)
//            if (paths.size > 0) {
//            for (int i=0;i<paths.size();i++) {
                fileImage = file2
                //                activity2.mPhoneImageView.setImageURI(Uri.fromFile(file));
//                RequestBody requestBody1=RequestBody.create(MediaType.parse("multipart/form-data"),new File(paths.get(i)));
//                MultipartBody.Part photo=MultipartBody.Part.createFormData("image")
                val requestFile = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    file2
                )
                // MultipartBody.Part is used to send also the actual file name
                body[0] = MultipartBody.Part.createFormData(
                    "image",
                    file2.name,
                    requestFile
                )
                Log.e("feifei1111", file2.name + "" + requestFile)
                //                requestBody[i] =
//                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(paths.get(i)));
//            }
//            }
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
            val ENDPOINT = "http://dyc.vip3gz.idcfengye.com"
            val sRetrofit = Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val apiManager = sRetrofit.create(
                FileUploadService::class.java
            )
            val call1 = apiManager.uploadImage(body[0], WeiDu, JingDu)
            call1.enqueue(object : Callback<UploadPhoto?> {
                override fun onResponse(
                    call: Call<UploadPhoto?>,
                    response: Response<UploadPhoto?>
                ) {
                    Log.e(
                        "fefeichenggong",
                        "onResponse() called with: call = [$call], response = [$response]"
                    )
                    val things1 = response.body()
                    if (things1 != null) {
                        try {
                            Log.e("feifeishilezhi", "?????")
                            Log.e(
                                "feifeishilezhi",
                                things1.code.toString() + ""
                            )
                            Log.e("feifeishilezhi", things1.message)
                            //                        Log.e("feifeishilezhi",things1.getData().toString());
                            if (things1.code == 1) {
                                Log.e("feifeishilezhi", things1.data.url)
                                activity2.initPhone(things1.data.url)
                                Toast.makeText(activity2, "检查到异常路面", Toast.LENGTH_SHORT)
                                    .show()
                                activity2.unLoadBar()
                            } else {
                                if (fileImage != null) {
                                    activity2.mPhoneImageView.setImageURI(
                                        Uri.fromFile(
                                            fileImage
                                        )
                                    )
                                }
                                activity2.unLoadBar()
                                Toast.makeText(
                                    activity2,
                                    things1.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            //                        Toast.makeText(activity2,things1.getMessage().toString(),Toast.LENGTH_SHORT).show();
                        } catch (e: Exception) {
                            Toast.makeText(
                                activity2,
                                e.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            activity2.unLoadBar()
                            Log.e("feifei", e.message)
                        }
                    } else {
//                    activity2.mPhoneImageView.setImageURI(Uri.fromFile(fileImage));
                        Toast.makeText(activity2, "上传超时", Toast.LENGTH_SHORT).show()
                        activity2.unLoadBar()
                        Log.e("feifei2", "shibai")
                    }
                }

                override fun onFailure(
                    call: Call<UploadPhoto?>,
                    t: Throwable
                ) {
                    Toast.makeText(activity2, "上传超时", Toast.LENGTH_SHORT).show()
                    activity2.unLoadBar()
                    Log.e(
                        "feifei失败草",
                        "onFailure() called with: call = [$call], t = [$t]"
                    )
                }
            })
        }


    }


}