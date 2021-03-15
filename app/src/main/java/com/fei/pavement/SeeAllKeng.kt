package com.fei.pavement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_see_all_keng.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.lang.Double.parseDouble


class SeeAllKeng : AppCompatActivity() {
    var allSee=ArrayList<See>()
    val me_context=baseContext
//    override fun onBackPressed() {
//        intent= Intent(this,MapActivity::class.java)
//        startActivity(intent)
////        super.onBackPressed()
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_all_keng)
    ic_back_keng.setOnClickListener{
        finish()
    }
        allSee.add(See(113.021069,28.07684))
        allSee.add(See(113.021069, 113.020791))
        allSee.add(See( 113.022039,28.074226))
        allSee.add(See(113.023647,28.070027 ))
        allSee.add(See(113.018235,28.068082))
        allSee.add(See(113.014013,28.066735 ))
        allSee.add(See(113.015226,28.065301 ))
//        allSee.add(See(39.111795,121.728005))


//        allSee.add(See(28.07684, 113.021069))
//        allSee.add(See(28.077677, 113.020791))
//        allSee.add(See(28.074226, 113.022039))
//        allSee.add(See(28.070027, 113.023647))
//        allSee.add(See(28.068082, 113.018235))
//        allSee.add(See(28.066735, 113.014013))
//        allSee.add(See(28.065301, 113.015226))
//        allSee.add(See(39.111795,121.728005))
        val retrofit = Retrofit.Builder()
            .baseUrl("http://223.4.183.204:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val appService = retrofit.create(ThingsService::class.java)
        appService.searchPage("1","200").enqueue(object : Callback<SearchPage> {
            override fun onFailure(call: Call<SearchPage>, t: Throwable) {
                Log.e("feifei",t.message)
            }

            override fun onResponse(call: Call<SearchPage>, response: Response<SearchPage>) {
                val things1 = response.body()
                if (things1 != null) {

                    Log.e("feifei",allSee.size.toString())
                    try {
                    for(things in things1.list){
                        allSee.add(See(parseDouble(things.longitude), parseDouble(things.latitude)))
                    }
                    Log.e("feifei",allSee.size.toString())

//            val list=intent.getSerializableExtra("allSee")
                        val layoutMessage = LinearLayoutManager(me_context)
                        recycler_view.layoutManager = layoutMessage
                        val adapter =SeeAdapter (allSee)
                        recycler_view?.adapter = adapter
                    }catch (e:Exception){
                        Log.e("feifei",e.message)
                    }
                } else {
                    Log.e("feifei2","shibai")
                }
            }

        })



    }
}