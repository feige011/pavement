package com.fei.pavement.textphone2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fei.pavement.MapActivity;
import com.fei.pavement.R;
import com.fei.pavement.textphone2.intent.PhotoPickerIntent;
import com.fei.pavement.textphone2.intent.PhotoPreviewIntent;
import com.fei.pavement.textphoto.FileUpload;
import com.fei.pavement.yinglan.scrolllayout.demo.MainActivity;

import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * @
 * @author lidong
 * @date 2016-02-29
 */
public class MainActivity2 extends AppCompatActivity {

    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;
    private ArrayList<String> imagePaths = new ArrayList<>();
    private ImageCaptureManager captureManager; // 相机拍照处理类

    private ProgressBar mProgress;
    private GridView gridView;
    private GridAdapter gridAdapter;
    private Button mButton;
    private TextView mTextView;
    public ImageView mPhoneImageView;
//    private String depp;
//    private EditText textView;
    private String TAG = MainActivity2.class.getSimpleName();

    Double WeiDu;
    Double JingDu;

//    @Override
//    public void onBackPressed() {
//        Intent intent= new Intent(this, MapActivity.class);
//        startActivity(intent);
//    }


//    override fun onBackPressed() {
//
////        super.onBackPressed()
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        ImageView mIcBack=findViewById(R.id.ic_back_main2);
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Log.e("feifei666","111");
        gridView = (GridView) findViewById(R.id.gridView);
        mButton = (Button) findViewById(R.id.button);
        mProgress=(ProgressBar) findViewById(R.id.progress_main2);
        mTextView=(TextView) findViewById(R.id.main2_text);
        mPhoneImageView=(ImageView)findViewById(R.id.phone_imageView);
//        textView= (EditText)findViewById(R.id.et_context);

        //接收图片
//        try {
//            initPhone();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //接收经纬度
        Intent intent=getIntent();
        JingDu=intent.getDoubleExtra("JingDu",0);
        WeiDu=intent.getDoubleExtra("WeiDu",0);

        Log.e("feifei666","111"+JingDu.toString());
        Log.e("feifei666",WeiDu.toString());


        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        gridView.setNumColumns(cols);

        // preview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgs = (String) parent.getItemAtPosition(position);
                if ("000000".equals(imgs) ){
                    PhotoPickerIntent intent = new PhotoPickerIntent(MainActivity2.this);
                    intent.setSelectModel(SelectModel.MULTI);
                    intent.setShowCarema(true); // 是否显示拍照
                    intent.setMaxTotal(6); // 最多选择照片数量，默认为6
                    intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
                    startActivityForResult(intent, REQUEST_CAMERA_CODE);
                }else{
                        PhotoPreviewIntent intent = new PhotoPreviewIntent(MainActivity2.this);
                        intent.setCurrentItem(position);
                        intent.setPhotoPaths(imagePaths);
                        Log.e("feifei?c",imagePaths.toString());
                        startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                }
//                try {
//                    Log.e("feifeiiamgePaths",imagePaths.get(0));
//                    initPhone(imagePaths.get(0));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                mPhoneImageView.setImageResource(imagePaths[0]);

            }
        });
        imagePaths.add("000000");
        gridAdapter = new GridAdapter(imagePaths);
        gridView.setAdapter(gridAdapter);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                depp =textView.getText().toString().trim()!=null?textView.getText().toString().trim():"woowoeo";
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
//                        Log.e("feifei?",depp);
//                        FileUploadManager.upload(imagePaths,depp);
//                        try {
////                            Bitmap bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(imagePaths.get(0))));
////                            mPhoneImageView.setImageBitmap(bitmap);
////                            Log.e("feifei123455",PhotoPagerAdapter.URLAll.toString());
////                            initPhone(PhotoPagerAdapter.URLAll.get(0));
//                        } catch (Exception e) {
//                            Log.e("feifei123456",e.getMessage());
//                            e.printStackTrace();
//                        }
                        FileUploadManager.upload(imagePaths,JingDu,WeiDu, MainActivity2.this);

//                        FileUploadManager.uploadMany(imagePaths, depp);
//                        FileUploadManager.upload(imagePaths,depp);
                    }
                }.start();
//                if(mProgress.getVisibility()==View.INVISIBLE){
//                    mProgress.setVisibility(View.GONE);
//                    mTextView.setText("正在上传");
//                }
//                mProgress.setVisibility(View.VISIBLE);
                onLoadBar();
//                progress_main2
//                Toast.makeText(MainActivity2.this,"上传成功",Toast.LENGTH_SHORT).show();
            }


        });
    }
    public void onLoadBar(){
        if(mProgress.getVisibility()==View.INVISIBLE){
            mProgress.setVisibility(View.VISIBLE);
//            mProgress.setVisibility(View.GONE);
            mTextView.setText("正在上传");
        }
    }
    public void unLoadBar(){
        if(mProgress.getVisibility()==View.VISIBLE){
            mProgress.setVisibility(View.INVISIBLE);
            mTextView.setText("");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    Log.d(TAG, "list: " + "list = [" + list.size());
                    loadAdpater(list);
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    Log.d(TAG, "ListExtra: " + "ListExtra = [" + ListExtra.size());
                    loadAdpater(ListExtra);
                    break;
            }
        }
    }

    private void loadAdpater(ArrayList<String> paths){
        if (imagePaths!=null&& imagePaths.size()>0){
            imagePaths.clear();
        }
        if (paths.contains("000000")){
            paths.remove("000000");
        }
        paths.add("000000");
        imagePaths.addAll(paths);
        gridAdapter  = new GridAdapter(imagePaths);
        gridView.setAdapter(gridAdapter);
        try{
            JSONArray obj = new JSONArray(imagePaths);
            Log.e("--", obj.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class GridAdapter extends BaseAdapter{
        private ArrayList<String> listUrls;
        private LayoutInflater inflater;
        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
            if(listUrls.size() == 7){
                listUrls.remove(listUrls.size()-1);
            }
            inflater = LayoutInflater.from(MainActivity2.this);
        }

        public int getCount(){
            return  listUrls.size();
        }
        @Override
        public String getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_image, parent,false);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            final String path=listUrls.get(position);
            if (path.equals("000000")){
//                holder.image.setImageResource(R.mipmap.ic_launcher);
                holder.image.setImageResource(R.drawable.ic_album_foreground);
            }else {
                Glide.with(MainActivity2.this)
                        .load(path)
                        .placeholder(R.mipmap.default_error)
                        .error(R.mipmap.default_error)
                        .centerCrop()
                        .crossFade()
                        .into(holder.image);
            }
            return convertView;
        }
          class ViewHolder {
             ImageView image;
        }
    }

    //获取图片

    public void initPhone(String url) throws InterruptedException {
        ImageView imageview = (ImageView) this.findViewById(R.id.phone_imageView);

        StrictMode.setThreadPolicy(new
                StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(
                new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                btn.setEnabled(false);
//            Thread.sleep(1000);
//                String strURL = "https://cheng123yy.oss-cn-hangzhou.aliyuncs.com/China_000007.jpg";
        String strURL = url;
                try {
                    Bitmap bitmap = getBitmap(strURL);
                    imageview.setImageBitmap(bitmap);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
//            }
//        });
    }
    public Bitmap getBitmap(String path) throws IOException {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
