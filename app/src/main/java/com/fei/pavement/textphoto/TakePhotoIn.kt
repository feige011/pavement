package com.fei.pavement.textphoto

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fei.pavement.R
import kotlinx.android.synthetic.main.activity_text_photo_in.*
import me.shouheng.compress.Compress
import me.shouheng.compress.strategy.Strategies
import me.shouheng.compress.strategy.config.ScaleMode
import java.io.File
import java.net.URI

class TakePhotoIn : AppCompatActivity() {
    val fromAlbum=2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_photo_in)
        fromAlbumBtn.setOnClickListener {
            val intent=Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type="image/*"
            startActivityForResult(intent,fromAlbum);
//            private fun allPhoto() {
//                val intent = Intent()
//                intent.action = Intent.ACTION_GET_CONTENT
//                intent.type = "image/*"
//                startActivityForResult(intent, 111)
//            }

        }

//        val file= File("")
//        Compress.with(this, file)
//            .strategy(Strategies.compressor())
//            .setConfig(Bitmap.Config.ARGB_8888)
//            .setMaxHeight(1280f)
//            .setMaxWidth(720f)
//            .setScaleMode(ScaleMode.SCALE_WIDTH)
//            .asFlowable()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                //压缩成功 获取file文件
//            }, {
//                //压缩失败
//            })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){

            fromAlbum->{
                if(resultCode== Activity.RESULT_OK&&data!=null){
                    data.data?.let { uri->
                        val bitmap=getBitmapFromUri(uri)
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }

        }
    }
    private fun getBitmapFromUri(uri: Uri)=contentResolver.openFileDescriptor(uri,"r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }
}