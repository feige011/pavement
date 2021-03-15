package com.fei.pavement.textphoto

import java.util.*
import kotlin.collections.ArrayList

data class LoginResponse(val success:Boolean,val code:Int,val message:String,val data: Any)
data class Login(val phoneNumber:String,val password:String,val verificationCode:String,val rememberMe:Boolean,val type:Int)

data class RegisterResponse(val success:Boolean,val code:Int, val message:String,val data: Any)
data class Register(val phoneNumber:String,val password:String, val confirmPassword:String,val verificationCode:String)

data class TestTelephoneResponse(val success:Boolean,val code:Int,val message:String,val data:Any)

data class UploadPhoto(val code:Int,val message: String,val data:FileUrl)
data class FileUrl(val url:String)

//"code": 200,
//"message": "请求成功",
//"data": {
//    "path": "E:\\var\\images\\3_2_21_33_1.jpg"
//}