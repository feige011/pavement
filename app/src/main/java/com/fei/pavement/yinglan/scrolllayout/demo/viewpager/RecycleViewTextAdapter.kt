package com.fei.pavement.yinglan.scrolllayout.demo.viewpager

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fei.pavement.MapActivity
import com.fei.pavement.R
import com.fei.pavement.SeeAllKeng
import com.fei.pavement.textphone2.MainActivity2
import com.fei.pavement.textphone2.PhotoPickerActivity
import com.fei.pavement.textphone2.PhotoPreviewActivity
import com.fei.pavement.textphoto.TextPhotoActivity
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

//val textList :List<Data>)
class RecycleViewTextAdapter(
    val activity:Activity,
    val time :ArrayList<String> ,
    val JingDu: Double, val WeiDu: Double,
    val vSpeed: Double,
    var values: FloatArray,
    var zSpeed: ArrayList<Double>,
    var ySpeed: ArrayList<Double>,
    var xSpeed: ArrayList<Double>,
    var panduan: ArrayList<Double>,
    var outData: ArrayList<Double>,
    var v_data: ArrayList<Double>,
    var jingdu: ArrayList<Double>,
    var weidu: ArrayList<Double>,
    var bool:Boolean
) :
    RecyclerView.Adapter<RecycleViewTextAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_listview, parent, false)
//        textView = view.findViewById(R.id.text1)
        Log.e("111111","22222")
        val imageButtonSeeJW: ImageButton = view.findViewById(R.id.imageButtonSeeJW)
        imageButtonSeeJW.setOnClickListener {
            Log.e("dianjile","dianjile")
            MapActivity.isClick = MapActivity.isClick != true
            try {
                activity.tv_stop.text="STOP"
            }catch (e :java.lang.Exception){
                Log.e("Exception",e.message)
            }

        }
        val imageButtonSeeKeng: ImageButton = view.findViewById<ImageButton>(R.id.imageButtonSeeKeng)
        imageButtonSeeKeng.setOnClickListener {
            val intent=Intent(parent.context,SeeAllKeng::class.java)
            parent.context.startActivity(intent)
        }


//????????????
        val uploadPhoto:ImageButton=view.findViewById(R.id.uploadPhoto)
        uploadPhoto.setOnClickListener {
            Log.e("feifeimmm","?????????")
                val intent=Intent(parent.context,MainActivity2::class.java)
                intent.putExtra("JingDu",JingDu)
                intent.putExtra("WeiDu",WeiDu)
                parent.context.startActivity(intent)
        }


        if(bool){
            val sb = java.lang.StringBuilder()
//            var s="""${values[1]} ${values[2]} ${values[0]} """
//            sb.append()

            sb.append(
                """
                x????????????:${values.get(1)}

                """.trimIndent()
            )
            sb.append(
                """
                y????????????:${values.get(2)}

                """.trimIndent()
            )
            sb.append(
                """
                z????????????:${values[0]}

                """.trimIndent()
            )
            val buttonSave: ImageButton = view.findViewById<ImageButton>(R.id.imageButtonSave)



            buttonSave.setOnClickListener {
                try {
                    if (ContextCompat.checkSelfPermission(
                            parent.context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        println("ok")
                    } else {
                        ActivityCompat.requestPermissions(
                            activity, arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ), 1
                        )
                    }
                    val formatter = SimpleDateFormat("yyyy???MM???dd??? HH:mm:ss")
                    val curDate = Date(System.currentTimeMillis()) //??????????????????

                    val str: String = formatter.format(curDate)
                    val dir = Environment.getExternalStorageDirectory()
                    val dataFile =
                        File("$dir/fei", "${str}.txt")
//                    Toast.makeText(parent.context, dir.toString(), Toast.LENGTH_LONG).show()
                    if (!dataFile.exists()) {
                        dataFile.createNewFile()
                    }
                    val outStream = FileOutputStream(dataFile)
                    //                            outStream.write(new String("Hello").getBytes("utf-8"));
                    //                            outStream.flush();
                    //                            Toast.makeText(TextActivity.this,"????????????1",Toast.LENGTH_LONG).show();
                    for (i in weidu.indices) {
                        val s="${time[i]} ${xSpeed[i]} ${ySpeed[i]} ${zSpeed[i]} ${weidu[i]} ${jingdu[i]} ${v_data[i]}\n"
//                        var str = """
//                        ??????: ${jingdu[i]}
//
//                        """.trimIndent()
                        outStream.write(s.toByteArray())
//                        str = """
//                        ??????: ${weidu[i]}
//
//                        """.trimIndent()
//                        outStream.write(str.toByteArray())
//                        str = """
//                        x?????????: ${xSpeed[i]}
//
//                        """.trimIndent()
//                        outStream.write(str.toByteArray())
//                        str = """
//                        y?????????: ${ySpeed[i]}
//
//                        """.trimIndent()
//                        outStream.write(str.toByteArray())
//                        str = """
//                        z?????????: ${zSpeed[i]}
//
//                        """.trimIndent()
//                        outStream.write(str.toByteArray())
//                        str = """
//                        ?????????: ${v_data[i]}
//
//                        """.trimIndent()
//                        outStream.write(str.toByteArray())
                    }
                    Toast.makeText(parent.context, "????????????", Toast.LENGTH_LONG).show()

                    //                            outStream.close();
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            val textXYE: TextView = view.findViewById<TextView>(R.id.textXYZ)
            val textJingDu: TextView = view.findViewById<TextView>(R.id.textJingDu)
            val textWeiDu: TextView = view.findViewById<TextView>(R.id.textWeiDu)
            val textNumber: TextView = view.findViewById<TextView>(R.id.textNumber)
            textJingDu.text = "?????????: ${JingDu}"
            textWeiDu.text = "?????????: $WeiDu"
            textXYE.text = "????????????${vSpeed}\n$sb"
            textNumber.text = "?????????: " + jingdu.size
        }

        //???????????????????????????
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 1;
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


    }


}