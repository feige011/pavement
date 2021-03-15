package com.fei.pavement


import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.lang.Exception


class MapsActivity : Activity() {
    private lateinit var latLng: LatLng
    lateinit var mMapView: MapView
    private lateinit var mBaiduMap: BaiduMap
    private lateinit var myLocationListener: MyLocationListener
    private lateinit var mLocationClient: LocationClient
    private var first=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        mMapView = findViewById(R.id.bmapView)
        val builder = MapStatus.Builder()
        mBaiduMap = mMapView.map
        mBaiduMap.mapType = BaiduMap.MAP_TYPE_NORMAL
        mBaiduMap.isMyLocationEnabled = true;
        mLocationClient = LocationClient(applicationContext);


//通过LocationClientOption设置LocationClient相关参数
        val option = LocationClientOption()
        option.setCoorType("bd09ll") // 设置坐标类型
        option.setScanSpan(1000)
        option.setIgnoreKillProcess(false);
        option.isOpenGps = true; // 打开gps

//设置locationClientOption
        mLocationClient.locOption = option

//注册LocationListener监听器

//注册LocationListener监听器
        latLng = LatLng(4.9E-3244, 9E-324)
        myLocationListener = MyLocationListener(mMapView, mBaiduMap, latLng,first)
        mLocationClient.registerLocationListener(myLocationListener)
//开启地图定位图层
//开启地图定位图层
        mLocationClient.start()
        mLocationClient.requestLocation()
        //把定位点再次显现出来
//        MyLocationConfiguration(
//            LocationClientOption.LocationMode mode,
//           val boolean enableDirection,
//            BitmapDescriptor customMarker,
//            int accuracyCircleFillColor,
//            int accuracyCircleStrokeColor)
//       val  mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
//        MyLocationConfiguration(mCurrentMode,true,)
        //定义Maker坐标点

        //定义Maker坐标点
        val point =
            LatLng(39.11, 121.724)
//构建Marker图标
//构建Marker图标
        try {
            val bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_zhuyi_fill)
//构建MarkerOption，用于在地图上添加Marker
//构建MarkerOption，用于在地图上添加Marker

            val option2 = MarkerOptions()
                .position(point)
                .icon(bitmap)


//在地图上添加Marker，并显示
//在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option2)
        }catch (e:Exception){
            Log.e("feifei",e.message)
        }


    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause()
    }

    override fun onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
//        mMapView = null;
        super.onDestroy();
    }

    class MyLocationListener(val mMapView: MapView?, val mBaiduMap: BaiduMap, var latLng: LatLng,var first:Boolean) :
        BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            //mapView 销毁后不在处理新接收的位置
            latLng = LatLng(location.latitude, location.longitude)
            if (location == null || mMapView == null) {
                Log.e("feifei", "????????????")
                return
            }
            Log.e("feifei", (location.latitude.toString() + location.longitude))
            val locData = MyLocationData.Builder()
                .accuracy(location.radius) // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(location.direction).latitude(location.latitude)
                .longitude(location.longitude).build()
            mBaiduMap.setMyLocationData(locData)
            if(first){
                val mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng)
                mBaiduMap.animateMapStatus(mapStatusUpdate)
                first=false
            }

        }
    }


}


