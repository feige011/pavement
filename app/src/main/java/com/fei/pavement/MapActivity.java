package com.fei.pavement;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.fei.pavement.yinglan.scrolllayout.demo.util.ScreenUtil;
import com.fei.pavement.yinglan.scrolllayout.demo.viewpager.RecycleViewTextAdapter;
import com.yinglan.scrolllayout.ScrollLayout;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import biz.source_code.dsp.filter.FilterPassType;
import biz.source_code.dsp.filter.IirFilterCoefficients;
import biz.source_code.dsp.filter.IirFilterDesignExstrom;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapActivity extends AppCompatActivity implements SensorEventListener {

    ArrayList<String> time=new ArrayList<>();
    ArrayList<See> allSee = new ArrayList();
    Double JingDu;
    Double WeiDu;
    Double vSpeed;
    public static final int LOCATION_CODE = 301;
    private LocationManager locationManager;
    private String locationProvider = null;
    private SensorManager mSensorManager;
    ArrayList<Double> zSpeed = new ArrayList<Double>();
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
    public static boolean isClick = false;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;

    private void showUserLocation(BDLocation location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        Log.e("feifei",location.getLatitude()+" "+location.getLongitude());
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);// ????????????????????????
        mBaiduMap.setMapStatus(msu);
        msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView ???????????????????????????????????????
            if (location == null || mMapView == null) {
                return;
            }
//            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
//            MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(200.0f);// ????????????????????????
//            mBaiduMap.setMapStatus(msu);
//            msu = MapStatusUpdateFactory.newLatLng(latLng);
//            mBaiduMap.animateMapStatus(msu);
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // ?????????????????????????????????????????????????????????0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            showUserLocation(location);
        }
    }

    public void me_add(LatLng point) {
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.zhuyi);

//??????MarkerOption???????????????????????????Marker

        OverlayOptions option2 = new MarkerOptions()
                .position(point)
//                .position(point)
                .icon(bitmap);

//??????????????????Marker????????????
        mBaiduMap.addOverlay(option2);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        refresh();
//        thread.start();
    }

    public void refresh(){
        //????????????????????????
        final TextView tv_stop = findViewById(R.id.tv_stop);
        tv_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_stop.setText("");
                isClick=false;
            }
        });
        initView();
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
//???????????? ,mBaiduMap????????????????????????
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        //???????????????
        mLocationClient = new LocationClient(this);
//        MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
//??????LocationClientOption??????LocationClient????????????
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // ??????gps
        option.setCoorType("bd09ll"); // ??????????????????
        option.setScanSpan(5000);
//        option.setIgnoreKillProcess(false);
//??????locationClientOption
        mLocationClient.setLocOption(option);

//??????LocationListener?????????
        MapActivity.MyLocationListener myLocationListener = new MapActivity.MyLocationListener();

        mLocationClient.registerLocationListener(myLocationListener);
//????????????????????????
        mLocationClient.start();
//        28.06973 113.016689
        init();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://223.4.183.204:8081/")
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
                            allSee.add(new See(Double.parseDouble(things1.getList().get(i).getLongitude()), Double.parseDouble(things1.getList().get(i).getLatitude())));
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

        // ???????????????????????????
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

    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "????????????????????????",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh();
    }


    //    private Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            //????????????
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
//????????????????????????????????????????????????????????????true??????????????????????????????
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onReg() {
        // ????????????????????????????????????
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // ????????????
        mSensorManager.unregisterListener(this);
    }
    public void ClearAll() {
                time.clear();
                zSpeed.clear();
                ySpeed.clear();
                xSpeed.clear();
                panduan.clear();
                outData.clear();
                v_data.clear();
                jingdu.clear();
                weidu.clear();
    }
    // ????????????????????????????????????????????????
    @Override
    public void onSensorChanged(SensorEvent event) {
        values = event.values;
        jingdu.add(JingDu);
        weidu.add(WeiDu);
        if(vSpeed<5){
            v_data.add(0.0);
        }else{
            v_data.add(vSpeed);
        }
        panduan.add((double) values[2]);
        xSpeed.add((double) values[0]);
        ySpeed.add((double) values[1]);
        zSpeed.add((double) values[2]);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//??????????????????
//        String str = ;
        time.add(formatter.format(curDate));
//        Log.e("2222222","22222");
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.list_recycler_view);
        if(isClick){
            RecycleViewTextAdapter adapter = new RecycleViewTextAdapter(this,
                    time,
                    JingDu, WeiDu, vSpeed,
                    values,
                    zSpeed,
                    ySpeed, xSpeed,
                    panduan,
                    outData,
                    v_data,
                    jingdu,
                    weidu,
                    isClick);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }

//        StringBuilder sb = new StringBuilder();
//        sb.append("z???:" + values[0] + "\n");
//        sb.append("x???:" + values[1] + "\n");
//        sb.append("y???:" + values[2] + "\n");
//        TextView textXYE = findViewById(R.id.textXYZ);
//        TextView textJingDu = findViewById(R.id.textJingDu);
//        TextView textWeiDu = findViewById(R.id.textWeiDu);
//        TextView textNumber = findViewById(R.id.textNumber);
//        textJingDu.setText("?????????: " + JingDu);
//        textWeiDu.setText("?????????: " + WeiDu);
//        textXYE.setText("???API???\n" + sb.toString());
//        textNumber.setText("?????????: " + jingdu.size());
        // ?????????????????????
        int type = event.sensor.getType();
        switch (type){
            case Sensor.TYPE_ACCELEROMETER:
                try{
//                    panduan.add((double)values[2]);
//                    [size]=values[2];
//                    size++;
//                    Log.e("feifei4",panduan.size()+"  "+v_data.size());
                    if(panduan.size()>=12100&&v_data.size()>=12100){
                        IirFilterCoefficients iirFilterCoefficients= IirFilterDesignExstrom.design(FilterPassType.lowpass, 5,10.0/50.0,13.0/50.0);
                        outData=butterworth.IIRFilter(panduan,iirFilterCoefficients.a,iirFilterCoefficients.b);
                        final List<Integer> list;
                        list=gs.show(outData,v_data);
                        for(int i=0;i<list.size();i++){
                            Log.e("feifei3"," "+ list.get(i) +"");
                        }
                        if(list.size()>10){
                            for(int i=0;i<list.size();i++){
                                if(i>10){
                                    final int finnal = i;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Log.e("feifei5",jingdu.get(finnal)+""+weidu.get(finnal));
                                                OkHttpClient client = new OkHttpClient();
                                                FormBody requestBody = new FormBody.Builder()
                                                        .add("longitude", jingdu.get(finnal)+"")
                                                        .add("latitude",weidu.get(finnal)+"")
                                                        .build();
                                                Request request = new  Request.Builder()
                                                        .url("http://223.4.183.204:8081/Myproject/saveLatitudeLongitude")
                                                        .post(requestBody)
                                                        .build();
                                                okhttp3.Response response =client.newCall(request).execute();
                                                String responseData = response.body().string();
                                                if (responseData != null) {
                                                    Log.e("feifei", responseData);

                                                    if(Integer.parseInt(responseData)==200) {
                                                        Log.e("feifei2",finnal+"");
                                                        LatLng point = new LatLng( weidu.get(list.get(finnal)),jingdu.get(list.get(finnal)));
//??????Marker??????
                                                        BitmapDescriptor bitmap = BitmapDescriptorFactory
                                                                .fromResource(R.drawable.zhuyi);
//??????MarkerOption???????????????????????????Marker
                                                        OverlayOptions option3 = new MarkerOptions()
                                                                .position(point)
                                                                .icon(bitmap);
//??????????????????Marker????????????
                                                        mBaiduMap.addOverlay(option3);
                                                    }
                                                }
                                            } catch (IOException e) {
                                                Log.e("feifei6",e.getMessage());
                                                e.printStackTrace();
                                            }finally {
                                                ClearAll();
                                            }
                                        }
                                    }).start();

                                    break;
                                }
                            }
                        }else{
                            ClearAll();
                        }


                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());
                    Log.e("feifei",e.getMessage());
                }
//                break;
//
        }
    }



    // ????????????????????????????????????????????????
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void getLocation() {
        mLocationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);                                //??????gps
        option.setCoorType("bd09ll");                           //?????????????????????bd09ll ?????????????????????????????????????????????type??????????????????????????????
        option.setPriority(LocationClientOption.NetWorkFirst);  //??????????????????
        option.setScanSpan(1000);                               //?????????????????????5???????????????????????????????????????????????????

        mLocationClient.setLocOption(option);
        mLocationClient.start();//??????????????????api????????????????????????????????????????????????????????????????????????

        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null)
                    return;
//                ?????????????????????????????????????????????????????????????????????????????????????????????
                JingDu = bdLocation.getLongitude();
                WeiDu = bdLocation.getLatitude();
                vSpeed = (double) bdLocation.getSpeed();
//                Log.e("feifeimmmm",JingDu.toString()+"   "+vSpeed.toString());
//                Log.e("!!!!feifei",jingdu.size()+"  "+weidu.size());
            }
        });
//        Log.e("feifei42",panduan.size()+"  "+v_data.size());
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        //????????????????????????????????????
//        List<String> providers = locationManager.getProviders(true);
//        locationProvider = LocationManager.GPS_PROVIDER;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                //????????????
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
//            } else {
//                //????????????????????????
//                locationManager.requestLocationUpdates(locationProvider, 1000, 0, locationListener);
//                Location location = locationManager.getLastKnownLocation(locationProvider);
//                for(int i=0;i<400;i++){
//                    if (location != null) {
//                        //???????????????
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
//            //????????????????????????
//            locationManager.requestLocationUpdates(locationProvider, 1000, 0, locationListener);
//            Location location = locationManager.getLastKnownLocation(locationProvider);
//            for(int i=0;i<400;i++){
//                if (location != null) {
//                    //?????????,???????????????????????????
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
        // Provider??????????????????????????????????????????????????????????????????????????????????????????
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        // Provider???enable???????????????????????????GPS?????????
        @Override
        public void onProviderEnabled(String provider) {
        }

        // Provider???disable???????????????????????????GPS?????????
        @Override
        public void onProviderDisabled(String provider) {
        }

        //??????????????????????????????????????????Provider?????????????????????????????????????????????
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                //?????????,???????????????????????????
//                if (panduan.size() >= 0) {
//                    for (int i = 0; i < 500; i++) {
                        vSpeed = (double)location.getSpeed();
                        JingDu = location.getLongitude();
                         WeiDu= location.getLatitude();
//                        jingdu.add(longitude);
//                        weidu.add(latitude);
//                        v_data.add(speed);
//                        Log.e("feifei43", "size3=" + v_data.size() + "   longitude" + longitude);
//                        change();
//                    }
//                }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "????????????", Toast.LENGTH_LONG).show();
                    try {
                        List<String> providers = locationManager.getProviders(true);
//                        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
//                            //?????????Network
//                            locationProvider = LocationManager.NETWORK_PROVIDER;
//                        }else if (providers.contains(LocationManager.GPS_PROVIDER)) {
//                            //?????????GPS
                        locationProvider = LocationManager.GPS_PROVIDER;
//                        }
                        //????????????????????????
                        locationManager.requestLocationUpdates(locationProvider, 1000, 0, locationListener);
                        Location location = locationManager.getLastKnownLocation(locationProvider);
//                        for (int i = 0; i < 400; i++) {
                            if (location != null) {
                                //?????????,???????????????????????????
                                vSpeed = (double)location.getSpeed();
                                JingDu = location.getLongitude();
                                WeiDu= location.getLatitude();
//                                jingdu.add(location.getLongitude());
//                                weidu.add(location.getLatitude());
//                                v_data.add((double) location.getSpeed());
//                                Log.e("feifei4", panduan.size() + "  " + v_data.size());
//                                change();
                            }
//                        }

                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "????????????", Toast.LENGTH_LONG).show();
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
        //???activity??????onResume?????????mMapView. onResume ()?????????????????????????????????
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
        //???activity??????onPause?????????mMapView. onPause ()?????????????????????????????????
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
        //???activity??????onDestroy?????????mMapView.onDestroy()?????????????????????????????????
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
        time.clear();
    }

    void init() {
//        ,
        allSee.add(new See(121.72708,39.111493 ));
        allSee.add(new See(113.021069,28.07684 ));
        allSee.add(new See(113.021069,28.077677));
        allSee.add(new See(113.022039,28.074226));
        allSee.add(new See( 113.022039,28.070027));
        allSee.add(new See(113.022039,28.068082));
        allSee.add(new See(113.022039,28.066735));
        allSee.add(new See( 113.022039,28.065301));

        //???????????????????????????
        for (int i = 0; i < allSee.size(); i++) {
            LatLng point = new LatLng(allSee.get(i).getWeidu(),allSee.get(i).getJingdu());
            me_add(point);
        }
    }


    /**
     * ??????view
     */

    private ScrollLayout mScrollLayout;
    private TextView text_foot;

    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            if (currentProgress >= 0) {
                float precent = 255 * currentProgress;
                if (precent > 255) {
                    precent = 255;
                } else if (precent < 0) {
                    precent = 0;
                }
                mScrollLayout.getBackground().setAlpha(255 - (int) precent);
            }
            if (text_foot.getVisibility() == View.VISIBLE)
                text_foot.setVisibility(View.GONE);
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            if (currentStatus.equals(ScrollLayout.Status.EXIT)) {
                text_foot.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onChildScroll(int top) {
        }
    };


    private void initView() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.root);
        mScrollLayout = (ScrollLayout) findViewById(R.id.scroll_down_layout);
        text_foot = (TextView) findViewById(R.id.text_foot);
//        ListView listView = (ListView) findViewById(R.id.list_view);
//        listView.setAdapter(new ListviewAdapter(this));
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.list_recycler_view);
        JingDu = 0.0;
        WeiDu = 0.0;
        vSpeed = 0.0;
        values = new float[10];
        values[0] = 0;
        zSpeed.add(0.0);
        ySpeed.add(0.0);
        xSpeed.add(0.0);
        panduan.add(0.0);
        outData.add(0.0);
        v_data.add(0.0);
        jingdu.add(0.0);
        weidu.add(0.0);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//??????????????????
//        String str = ;
        time.add(formatter.format(curDate));
        RecycleViewTextAdapter adapter = new RecycleViewTextAdapter(
                this,
                time,
                JingDu,
                WeiDu,
                vSpeed,
                values,
                zSpeed,
                ySpeed,
                xSpeed,
                panduan,
                outData,
                v_data,
                jingdu,
                weidu,
                isClick);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        /**?????? setting*/
        mScrollLayout.setMinOffset(0);
        mScrollLayout.setMaxOffset((int) (ScreenUtil.getScreenHeight(this) * 0.4));
        mScrollLayout.setExitOffset(ScreenUtil.dip2px(this, 50));
        mScrollLayout.setIsSupportExit(true);
        mScrollLayout.setAllowHorizontalScroll(true);
        mScrollLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        mScrollLayout.setToExit();

        mScrollLayout.getBackground().setAlpha(0);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollLayout.scrollToExit();
            }
        });
        text_foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollLayout.setToOpen();
            }
        });
    }

}
