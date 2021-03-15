package com.fei.pavement.textphoto;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileUpload {
    /**
     * 单文件上传
     * @param description
     * @param file @Part MultipartBody.Part file 使用MultipartBody.Part类发送文件file到服务器
     * @return 状态信息String
     */
    @Multipart
    @POST("UploadServerAddr")
    Call<UploadPhoto> uploadFile(@Part("description") RequestBody description, @Part MultipartBody.Part file);

    /**
     * 多文件上传：通过 List<MultipartBody.Part> 传入多个part实现
     * @param parts 每一个part代表一个文件
     * @return 状态信息String
     */
    @Multipart
    @POST("UploadServerAddr")
    Call<String> uploadFilesMultipartBodyParts(@Part() List<MultipartBody.Part> parts);

    /**
     * 通过 MultipartBody和@body作为参数来实现多文件上传
     * @param multipartBody MultipartBody包含多个Part
     * @return 状态信息String
     */
    @POST("UploadServerAddr")
    Call<String> uploadFilesMultipartBody(@Body MultipartBody multipartBody);

    @Multipart
    @POST("Myproject/uploadImage")
    Call<UploadPhoto> uploadDouble(@Part MultipartBody.Part file1);
}
