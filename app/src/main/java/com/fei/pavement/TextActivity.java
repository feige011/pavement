package com.fei.pavement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.android.gms.common.internal.Objects;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import biz.source_code.dsp.filter.FilterPassType;
import biz.source_code.dsp.filter.IirFilterCoefficients;
import biz.source_code.dsp.filter.IirFilterDesignExstrom;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.os.Environment.getExternalStorageState;

public class TextActivity extends Activity implements SensorEventListener {
    ArrayList<See> allSee = new ArrayList();
    Double JingDu;
    Double WeiDu;
    Double vSpeed;
    public static final int LOCATION_CODE = 301;
    private LocationManager locationManager;
    private String locationProvider = null;
    private SensorManager mSensorManager;
    ArrayList<Double> zSpeed = new ArrayList<>();
    ArrayList<Double> ySpeed = new ArrayList<>();
    ArrayList<Double> xSpeed = new ArrayList<>();
    ArrayList<Double> panduan = new ArrayList<>();
    ArrayList<Double> outData = new ArrayList<>();
    ArrayList<Double> v_data = new ArrayList<>();
    ArrayList<Double> jingdu = new ArrayList<>();
    ArrayList<Double> weidu = new ArrayList<>();
    gaussion gs = new gaussion();
    Butterworth butterworth = new Butterworth();
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient;
    float[] values;

    private void showUserLocation(BDLocation location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        Log.e("feifei",location.getLatitude()+" "+location.getLongitude());
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);// 设置地图放大比例
        mBaiduMap.setMapStatus(msu);
        msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
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

    public void me_add(LatLng point) {
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.zhuyi);
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option2 = new MarkerOptions()
                .position(point)
//                .position(point)
                .icon(bitmap);
//在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option2);
    }

    private File dir = Environment.getExternalStorageDirectory();
    private File dataFile = new File(dir+"/fei","data.txt");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        //获取地图控件引用
        JingDu = 0.0;
        WeiDu = 0.0;
        Button buttonCopy = findViewById(R.id.buttonCopy);
        buttonCopy.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            if (ContextCompat.checkSelfPermission(TextActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {
                                System.out.println("ok");
                            }else {
                                ActivityCompat.requestPermissions(TextActivity.this,new String[] {
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                },1);
                            }
                            Toast.makeText(TextActivity.this,dir.toString(),Toast.LENGTH_LONG).show();
                            if (!dataFile.exists()) {
                                dataFile.createNewFile();
                            }
                            FileOutputStream outStream = new FileOutputStream(dataFile);
//                            outStream.write(new String("Hello").getBytes("utf-8"));
//                            outStream.flush();
//                            Toast.makeText(TextActivity.this,"打印成功1",Toast.LENGTH_LONG).show();

                            for(int i=0;i<weidu.size();i++){
                                String str="精度: "+jingdu.get(i)+"\r\n";
                                outStream.write(str.getBytes());
                                str="精度: "+ weidu.get(i)+"\r\n";
                                outStream.write(str.getBytes());
                                str="精度: "+xSpeed.get(i)+"\r\n";
                                outStream.write(str.getBytes());
                                str="精度: "+ySpeed.get(i)+"\r\n";
                                outStream.write(str.getBytes());
                                str="精度: "+zSpeed.get(i)+"\r\n";
                                outStream.write(str.getBytes());
                                str="精度: "+v_data.get(i)+"\r\n";
                                outStream.write(str.getBytes());
                            }
                            Toast.makeText(TextActivity.this,"打印成功",Toast.LENGTH_LONG).show();

//                            outStream.close();

                        } catch (Exception e) {

                            e.printStackTrace();

                        }

                    }
                });
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
        option.setScanSpan(5000);
//        option.setIgnoreKillProcess(false);
//设置locationClientOption
        mLocationClient.setLocOption(option);

//注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();

        mLocationClient.registerLocationListener(myLocationListener);
//开启地图定位图层
        mLocationClient.start();
//        28.06973 113.016689
        init();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://223.4.183.204:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ThingsService appService = retrofit.create(ThingsService.class);
//        appService.searchPage("1","200").enqueue(Object);
        appService.searchPage("1", "200").enqueue(new Callback<SearchPage>() {
            @Override
            public void onResponse(Call<SearchPage> call, Response<SearchPage> response) {
                SearchPage things1 = response.body();
                if (things1 != null) {
                    try {
                        for (int i = 0; i < things1.getList().size(); i++) {
                            allSee.add(new See(Double.parseDouble(things1.getList().get(i).getLatitude()), Double.parseDouble(things1.getList().get(i).getLongitude())));
                        }
                        for (int i = 0; i < allSee.size(); i++) {
                            Log.e("feifei444", "????");
                            LatLng point = new LatLng( allSee.get(i).getWeidu(),allSee.get(i).getJingdu());
                            me_add(point);
                        }
                    } catch (Exception e) {
                        Log.e("feifei", e.getMessage());
                    }
                } else {
                    Log.e("feifei2", "shibai");
                }
            }

            @Override
            public void onFailure(Call<SearchPage> call, Throwable t) {
                Log.e("feifei", t.getMessage());
            }
        });

        // 获取传感器管理对象
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        onReg();
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            requestPermissions(permissions, 202);
        }
        getLocation();

//        thread.start();
    }
//    private Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            //处理消息
//              if(msg.what==0){
//            }
//            return false;
//        }
//    });

    //    Thread thread = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            boolean flag = true;
//            while (flag) {
//                Message message = new Message();
//                message.what = 0;
////                handler.sendMessage(message);
//
//                try {
//                    thread.sleep(2);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    });
    public static void appendMethodB(String fileName, String content) {
        try {
//打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onReg() {
        // 为加速度传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 取消监听
        mSensorManager.unregisterListener(this);
    }

    // 当传感器的值改变的时候回调该方法
    @Override
    public void onSensorChanged(SensorEvent event) {
        values = event.values;
        jingdu.add(JingDu);
        weidu.add(WeiDu);
        v_data.add(vSpeed);
        panduan.add((double) values[2]);
        xSpeed.add((double) values[0]);
        ySpeed.add((double) values[1]);
        zSpeed.add((double) values[2]);
        StringBuilder sb = new StringBuilder();

        sb.append("z轴:" + values[0] + "\n");
        sb.append("x轴:" + values[1] + "\n");
        sb.append("y轴:" + values[2] + "\n");
        TextView textXYE = findViewById(R.id.textXYZ);
        TextView textJingDu = findViewById(R.id.textJingDu);
        TextView textWeiDu = findViewById(R.id.textWeiDu);
        TextView textNumber = findViewById(R.id.textNumber);
        textJingDu.setText("精度为: " + JingDu);
        textWeiDu.setText("维度为: " + WeiDu);
        textXYE.setText("旧API：\n" + sb.toString());
        textNumber.setText("个数为: " + jingdu.size());
        // 获取传感器类型
//        int type = event.sensor.getType();
//        switch (type){
//            case Sensor.TYPE_ACCELEROMETER:
//                try{
//                    panduan.add((double)values[2]);
////                    [size]=values[2];
////                    size++;
//                    Log.e("feifei4",panduan.size()+"  "+v_data.size());
//                    if(panduan.size()>=12100&&v_data.size()>=12100){
//                        IirFilterCoefficients iirFilterCoefficients= IirFilterDesignExstrom.design(FilterPassType.lowpass, 5,10.0/50.0,13.0/50.0);
//                        outData=butterworth.IIRFilter(panduan,iirFilterCoefficients.a,iirFilterCoefficients.b);
//                        final List<Integer> list;
//                        list=gs.show(outData,v_data);
//                        for(int i=0;i<list.size();i++){
//                            Log.e("feifei3"," "+ list.get(i) +"");
//                        }
//                        for(int i=0;i<list.size();i++){
//                            if(i>10){
//                                final int finnal = i;
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        try {
//                                            Log.e("feifei5",jingdu.get(finnal)+""+weidu.get(finnal));
//                                            OkHttpClient client = new OkHttpClient();
//                                            FormBody requestBody = new FormBody.Builder()
//                                                    .add("latitude", jingdu.get(finnal)+"")
//                                                    .add("longitude",weidu.get(finnal)+"")
//                                                    .build();
//                                            Request request = new  Request.Builder()
//                                                    .url("http://223.4.183.204:8080/Myproject/saveLatitudeLongitude")
//                                                    .post(requestBody)
//                                                    .build();
//                                            okhttp3.Response response =client.newCall(request).execute();
//                                            String responseData = response.body().string();
//                                            if (responseData != null) {
//                                                Log.e("feifei", responseData);
//
//                                                if(Integer.parseInt(responseData)==200) {
//                                                    Log.e("feifei2",finnal+"");
//                                                    LatLng point = new LatLng(jingdu.get(list.get(finnal)), weidu.get(list.get(finnal)));
////构建Marker图标
//                                                    BitmapDescriptor bitmap = BitmapDescriptorFactory
//                                                            .fromResource(R.drawable.zhuyi);
////构建MarkerOption，用于在地图上添加Marker
//                                                    OverlayOptions option3 = new MarkerOptions()
//                                                            .position(point)
//                                                            .icon(bitmap);
////在地图上添加Marker，并显示
//                                                    mBaiduMap.addOverlay(option3);
//                                                }
//                                            }
//                                        } catch (IOException e) {
//                                            Log.e("feifei6",e.getMessage());
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }).start();
//
//                                break;
//                            }
//                        }
//                        clearAll();
//                    }
//                }catch (Exception e){
//                    System.out.println(e.getMessage());
//                    Log.e("feifei",e.getMessage());
//                }
//                break;
//
//        }
    }

    // 当传感器精度发生改变时回调该方法
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void getLocation() {
        mLocationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);                                //打开gps
        option.setCoorType("bd09ll");                           //设置坐标类型为bd09ll 百度需要的坐标，也可以返回其他type类型，大家可以查看下
        option.setPriority(LocationClientOption.NetWorkFirst);  //设置网络优先
        option.setScanSpan(1000);                               //定时定位，每隔5秒钟定位一次。这个就看大家的需求了

        mLocationClient.setLocOption(option);
        mLocationClient.start();//这句代码百度api上给的没有，没有这个代码下面的回调方法不会执行的

        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null)
                    return;
//                这里可以获取经纬度，这是回调方法哦，怎么执行大家可以查看资料，
                JingDu = bdLocation.getLongitude();
                WeiDu = bdLocation.getLatitude();
                vSpeed = (double)bdLocation.getSpeed();
//                Log.e("!!!!feifei",jingdu.size()+"  "+weidu.size());
            }
        });
//        Log.e("feifei42",panduan.size()+"  "+v_data.size());
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        //获取所有可用的位置提供器
//        List<String> providers = locationManager.getProviders(true);
//        locationProvider = LocationManager.GPS_PROVIDER;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                //请求权限
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
//            } else {
//                //监视地理位置变化
//                locationManager.requestLocationUpdates(locationProvider, 1000, 0, locationListener);
//                Location location = locationManager.getLastKnownLocation(locationProvider);
//                for(int i=0;i<400;i++){
//                    if (location != null) {
//                        //输入经纬度
//                        v_data.add((double) location.getSpeed());
//                        jingdu.add(location.getLongitude());
//                        weidu.add(location.getLatitude());
//                        change();
//                        Log.e("feifei42",panduan.size()+"  "+v_data.size());
//                        }
//                    }
//
//            }
//        } else {
//            //监视地理位置变化
//            locationManager.requestLocationUpdates(locationProvider, 1000, 0, locationListener);
//            Location location = locationManager.getLastKnownLocation(locationProvider);
//            for(int i=0;i<400;i++){
//                if (location != null) {
//                    //不为空,显示地理位置经纬度
//                    jingdu.add(location.getLongitude());
//                    weidu.add(location.getLatitude());
//                    v_data.add((double) location.getSpeed());
//                    Log.e("feifei43",panduan.size()+"  "+v_data.size());
//                    change();
//                }
//            }
//
//        }
    }

    public LocationListener locationListener = new LocationListener() {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {
        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                //不为空,显示地理位置经纬度
                if (panduan.size() >= 0) {
                    for (int i = 0; i < 500; i++) {
                        double speed = location.getSpeed();
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        jingdu.add(longitude);
                        weidu.add(latitude);
                        v_data.add(speed);
                        Log.e("feifei43", "size3=" + v_data.size() + "   longitude" + longitude);
                        change();
                    }
                }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "申请权限", Toast.LENGTH_LONG).show();
                    try {
                        List<String> providers = locationManager.getProviders(true);
//                        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
//                            //如果是Network
//                            locationProvider = LocationManager.NETWORK_PROVIDER;
//                        }else if (providers.contains(LocationManager.GPS_PROVIDER)) {
//                            //如果是GPS
                        locationProvider = LocationManager.GPS_PROVIDER;
//                        }
                        //监视地理位置变化
                        locationManager.requestLocationUpdates(locationProvider, 1000, 0, locationListener);
                        Location location = locationManager.getLastKnownLocation(locationProvider);
                        for (int i = 0; i < 400; i++) {
                            if (location != null) {
                                //不为空,显示地理位置经纬度
                                jingdu.add(location.getLongitude());
                                weidu.add(location.getLatitude());
                                v_data.add((double) location.getSpeed());
                                Log.e("feifei4", panduan.size() + "  " + v_data.size());
                                change();
                            }
                        }

                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "缺少权限", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
//        thread.interrupt();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//        mMapView.onDestroy();
    }

    void change() {
        if (panduan.size() >= 18000 || v_data.size() >= 18000) {
            clearAll();
        }
    }

    void clearAll() {
        panduan.clear();
        outData.clear();
        v_data.clear();
        jingdu.clear();
        weidu.clear();
    }

    void init() {
//        ,
        allSee.add(new See(39.111493, 121.72708));
        allSee.add(new See(28.07684, 113.021069));
        allSee.add(new See(28.077677, 113.020791));
        allSee.add(new See(28.074226, 113.022039));
        allSee.add(new See(28.070027, 113.023647));
        allSee.add(new See(28.068082, 113.018235));
        allSee.add(new See(28.066735, 113.014013));
        allSee.add(new See(28.065301, 113.015226));

        //没服务端的时候调试
        for (int i = 0; i < allSee.size(); i++) {
            LatLng point = new LatLng( allSee.get(i).getWeidu(),allSee.get(i).getJingdu());
            me_add(point);
        }
    }

}
