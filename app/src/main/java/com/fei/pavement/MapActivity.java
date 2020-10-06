package com.fei.pavement;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.bikenavi.params.BikeNaviLaunchParam;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.poi.BaiduMapPoiSearch;

public class MapActivity extends Activity {
    public static final int LOCATION_CODE = 301;
    private LocationManager locationManager;
    private String locationProvider = null;
    private SensorManager mSensorManager;
    StringBuilder sb;
    StringBuilder sb2;
    int iii=0;
    StringBuilder sb3;

    private MapView mMapView = null;
    private BaiduMap mBaiduMap=null;
    private LocationClient mLocationClient;
    private void showUserLocation(BDLocation location) {
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        Log.e("feifei",location.getLatitude()+" "+location.getLongitude());
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(100.0f);// 设置地图放大比例
        mBaiduMap.setMapStatus(msu);
        msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
//            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
//            MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(200.0f);// 设置地图放大比例
//            mBaiduMap.setMapStatus(msu);
//            msu = MapStatusUpdateFactory.newLatLng(latLng);
//            mBaiduMap.animateMapStatus(msu);
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            showUserLocation(location);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
//普通地图 ,mBaiduMap是地图控制器对象
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        //定位初始化



        mLocationClient = new LocationClient(this);
//        MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
//通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
//        option.setIgnoreKillProcess(false);
//设置locationClientOption
        mLocationClient.setLocOption(option);

//注册LocationListener监听器

        MyLocationListener myLocationListener = new MyLocationListener();

        mLocationClient.registerLocationListener(myLocationListener);
//开启地图定位图层
        mLocationClient.start();
//        28.06973 113.016689
        LatLng point = new LatLng(28.06973, 113.016689);
//构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.zhuyi);
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option2 = new MarkerOptions()
                .position(point)
                .icon(bitmap);
//在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option2);

        // 获取传感器管理对象
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        onReg();
        String[] permissions = new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            requestPermissions(permissions, 202);
        }
        getLocation();

        thread.start();




//        LatLng startPt = new LatLng(40.047416,116.312143);
//        LatLng endPt = new LatLng(40.048424, 116.313513);
//        //构造BikeNaviLaunchParam
//        //.vehicle(0)默认的普通骑行导航
//        BikeNaviLaunchParam param = new BikeNaviLaunchParam().stPt(startPt).endPt(endPt).vehicle(0);
//        BikeNavigateHelper.getInstance().routePlanWithParams(param, new IBRoutePlanListener() {
//                    @Override
//                    public void onRoutePlanStart() {
//                        //执行算路开始的逻辑
//                    }
//
//                    @Override
//                    public void onRoutePlanSuccess() {
//                        //算路成功
//                        //跳转至诱导页面
//                        Intent intent = new Intent(BNaviMainActivity.this, BNaviGuideActivity.class);
//                        startActivity(intent);
//                    }
//            @Override
//            public void onRoutePlanFail(BikeRoutePlanError bikeRoutePlanError) {
//                //执行算路失败的逻辑
//            }
//        });


//        BikeNavigateHelper.getInstance().initNaviEngine(this, new IBEngineInitListener() {
//            @Override
//            public void engineInitSuccess() {
//                //骑行导航初始化成功之后的回调
//                routePlanWithParam();
//            }
//
//            @Override
//            public void engineInitFail() {
//                //骑行导航初始化失败之后的回调
//            }
//        });


//        showUserLocation(mLocationClient.getLastKnownLocation());
//        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
    }
//    MyLocationConfiguration(LocationClientOption.LocationMode mode,
//                            boolean enableDirection,
//                            BitmapDescriptor customMarker,
//                            int accuracyCircleFillColor,
//                            int accuracyCircleStrokeColor)

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //处理消息
            if(msg.what==0){
                if(sb!=null)
                    mTxtValue1.setText(sb.toString());
                //Toast.makeText(MainActivity.this,sb+"",0).show();
                /*if(sb3!=null)
                    mTxtValue3.setText(sb3.toString());*/
                if(sb2!=null)
                    mTxtValue6.setText(sb2.toString());
                String filename=
                        Environment.getExternalStorageDirectory()+"/rst.txt";
                appendMethodB(filename,sb+""+sb2+"\n");


            }
            return false;
        }
    });

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            boolean flag = true;
            while (flag) {
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);

                try {
                    thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });


    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
//        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
//        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//        mMapView.onDestroy();
    }

}
