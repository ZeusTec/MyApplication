package com.roman.iweather;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SearchView;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    PopupWindow editWindow;
    PopupWindow aboutWindow;
    SearchView searchView;
    HashMap<String,Object> data;
    String city;
    String now_cond;
    int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("");
        if(toolbar==null)
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit:
                        View contentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.window_edit, null);
                        editWindow = new PopupWindow(contentView);
                        editWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                        editWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                        editWindow.setFocusable(true);
                        editWindow.setBackgroundDrawable(new BitmapDrawable());

                        searchView = (SearchView) contentView.findViewById(R.id.search);
                        searchView.setIconifiedByDefault(false);
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                apiTest(query);
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                return false;
                            }
                        });
                        searchView.setSubmitButtonEnabled(true);

                        editWindow.showAsDropDown(toolbar);
                        break;
                    case R.id.about:
                        View contentView1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.window_about, null);
                        aboutWindow = new PopupWindow(contentView1);
                        aboutWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                        aboutWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                        aboutWindow.setFocusable(true);
                        aboutWindow.setBackgroundDrawable(new BitmapDrawable());
                        int xPos = toolbar.getWidth() -aboutWindow.getWidth();
                        aboutWindow.showAsDropDown(toolbar,xPos,0);
                        break;
                }
                return true;
            }
        });

        List<Fragment> fragments=new ArrayList<Fragment>();
        fragments.add(new FirstPage());
        fragments.add(new SecondPage());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager(),fragments));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        currentPage = 0;
                        toolbar.setTitle(city);
                        break;
                    case 1:
                        currentPage = 1;
                        toolbar.setTitle(city+" "+now_cond);
                        break;
                    default:
                        toolbar.setTitle(city);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /*******从文件中获取city*******/
        if (new File("data").exists()){
            Log.v("file","file exits");
            data = getData(getDataFromFile(MainActivity.this,"data"));
            city = (String) data.get("city");
            now_cond = (String) data.get("now_cond");
        }
        else{
            Log.v("file","file not exit");
            InputStream inputStream = getResources().openRawResource(R.raw.data);
            if (inputStream != null){
                data = getData(getDataFromInputStream(inputStream));
                city = (String) data.get("city");
                now_cond = (String) data.get("now_cond");
            }else city = "beijing";
        }
        apiTest(city);
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public SectionPagerAdapter(FragmentManager fm,List<Fragment> fragments){
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }

    class Weather{
        String update;
        String now_cond;//当前天气
        String now_fl;//当前体感温度
        String now_hum;//当前湿度
        String now_tmp;//当前温度
        String now_wind_dir;//当前风向
        String now_wind_sc;//当前风力
        String now_vis;//当前能见度
        String d1_sr;//第一天日出时间
        String d1_ss;//日落时间
        String d1_cond_d;//白天天气
        String d1_cond_n;//夜间天气
        String d1_date;
        String d1_tmp_max;//最高气温
        String d1_tmp_min;//最低气温
        String d2_sr;//第二天日出时间
        String d2_ss;//日落时间
        String d2_cond_d;//白天天气
        String d2_cond_n;//夜间天气
        String d2_date;
        String d2_tmp_max;//最高气温
        String d2_tmp_min;//最低气温
        String d3_sr;//第三天日出时间
        String d3_ss;//日落时间
        String d3_cond_d;//白天天气
        String d3_cond_n;//夜间天气
        String d3_date;
        String d3_tmp_max;//最高气温
        String d3_tmp_min;//最低气温
        String d4_sr;//第四天日出时间
        String d4_ss;//日落时间
        String d4_cond_d;//白天天气
        String d4_cond_n;//夜间天气
        String d4_date;
        String d4_tmp_max;//最高气温
        String d4_tmp_min;//最低气温
        String d5_sr;//第五天日出时间
        String d5_ss;//日落时间
        String d5_cond_d;//白天天气
        String d5_cond_n;//夜间天气
        String d5_date;
        String d5_tmp_max;//最高气温
        String d5_tmp_min;//最低气温
        String d6_sr;//第六天日出时间
        String d6_ss;//日落时间
        String d6_cond_d;//白天天气
        String d6_cond_n;//夜间天气
        String d6_date;
        String d6_tmp_max;//最高气温
        String d6_tmp_min;//最低气温
        String d7_sr;//第七天日出时间
        String d7_ss;//日落时间
        String d7_cond_d;//白天天气
        String d7_cond_n;//夜间天气
        String d7_date;
        String d7_tmp_max;//最高气温
        String d7_tmp_min;//最低气温
        String aqi; //空气质量指数
        String no2;//二氧化氮1小时平均值(ug/m³)
        String pm10;//PM10 1小时平均值(ug/m³)
        String pm25; //PM2.5 1小时平均值(ug/m³)
        String qlty;//空气质量类别
        String so2; //二氧化硫1小时平均值(ug/m³)
        String comf;//舒适
        String cw;//洗车
        String sport;//运动
        String uv;//紫外线
        public Weather(HashMap<String, Object> hashMap) {
            update = (String) hashMap.get("update");
            now_cond = (String) hashMap.get("now_cond");
            now_fl = (String) hashMap.get("now_fl");
            now_hum = (String) hashMap.get("now_hum");
            now_tmp = (String) hashMap.get("now_tmp");
            now_wind_dir = (String) hashMap.get("now_wind_dir");
            now_wind_sc = (String) hashMap.get("now_wind_sc");
            now_vis = (String) hashMap.get("now_vis");
            comf = (String) hashMap.get("comf");
            cw = (String) hashMap.get("cw");
            sport = (String) hashMap.get("sport");
            uv = (String) hashMap.get("uv");
//            h1_date = (String) hashMap.get("h1_date");
//            h1_hum = (String) hashMap.get("h1_hum");
//            h1_tmp = (String) hashMap.get("h1_tmp");
//            h1_wind_dir = (String) hashMap.get("h1_wind_dir");
//            h1_wind_sc = (String) hashMap.get("h1_wind_sc");
//            h2_date = (String) hashMap.get("h2_date");
//            h2_hum = (String) hashMap.get("h2_hum");
//            h2_tmp = (String) hashMap.get("h2_tmp");
//            h2_wind_dir = (String) hashMap.get("h2_wind_dir");
//            h2_wind_sc = (String) hashMap.get("h2_wind_sc");
//            h3_date = (String) hashMap.get("h3_date");
//            h3_hum = (String) hashMap.get("h3_hum");
//            h3_tmp = (String) hashMap.get("h3_tmp");
//            h3_wind_dir = (String) hashMap.get("h3_wind_dir");
//            h3_wind_sc = (String) hashMap.get("h3_wind_sc");
            aqi = (String) hashMap.get("aqi");
            no2 = (String) hashMap.get("no2");
            pm10 = (String) hashMap.get("pm10");
            pm25 = (String) hashMap.get("pm25");
            qlty = (String) hashMap.get("qlty");
            so2 = (String) hashMap.get("so2");
            d1_sr = (String) hashMap.get("d1_sr");//第一天日出时间
            d1_ss = (String) hashMap.get("d1_ss");//日落时间
            d1_cond_d = (String) hashMap.get("d1_cond_d");//白天天气
            d1_cond_n = (String) hashMap.get("d1_cond_n");//夜间天气
            d1_date = (String) hashMap.get("d1_date");
            d1_tmp_max = (String) hashMap.get("d1_tmp_max");//最高气温
            d1_tmp_min = (String) hashMap.get("d1_tmp_min");//最低气温
            d2_sr = (String) hashMap.get("d2_sr");//第二天日出时间
            d2_ss = (String) hashMap.get("d2_ss");//日落时间
            d2_cond_d = (String) hashMap.get("d2_cond_d");//白天天气
            d2_cond_n = (String) hashMap.get("d2_cond_n");//夜间天气
            d2_date = (String) hashMap.get("d2_date");
            d2_tmp_max = (String) hashMap.get("d2_tmp_max");//最高气温
            d2_tmp_min = (String) hashMap.get("d2_tmp_min");//最低气温
            d3_sr = (String) hashMap.get("d3_sr");//第三天日出时间
            d3_ss = (String) hashMap.get("d3_ss");//日落时间
            d3_cond_d = (String) hashMap.get("d3_cond_d");//白天天气
            d3_cond_n = (String) hashMap.get("d3_cond_n");//夜间天气
            d3_date = (String) hashMap.get("d3_date");
            d3_tmp_max = (String) hashMap.get("d3_tmp_max");//最高气温
            d3_tmp_min = (String) hashMap.get("d3_tmp_min");//最低气温
            d4_sr = (String) hashMap.get("d4_sr");//第四天日出时间
            d4_ss = (String) hashMap.get("d4_ss");//日落时间
            d4_cond_d = (String) hashMap.get("d4_cond_d");//白天天气
            d4_cond_n = (String) hashMap.get("d4_cond_n");//夜间天气
            d4_date = (String) hashMap.get("d4_date");
            d4_tmp_max = (String) hashMap.get("d4_tmp_max");//最高气温
            d4_tmp_min = (String) hashMap.get("d4_tmp_min");//最低气温
            d5_sr = (String) hashMap.get("d5_sr");//第五天日出时间
            d5_ss = (String) hashMap.get("d5_ss");//日落时间
            d5_cond_d = (String) hashMap.get("d5_cond_d");//白天天气
            d5_cond_n = (String) hashMap.get("d5_cond_n");//夜间天气
            d5_date = (String) hashMap.get("d5_date");
            d5_tmp_max = (String) hashMap.get("d5_tmp_max");//最高气温
            d5_tmp_min = (String) hashMap.get("d5_tmp_min");//最低气温
            d6_sr = (String) hashMap.get("d6_sr");//第六天日出时间
            d6_ss = (String) hashMap.get("d6_ss");//日落时间
            d6_cond_d = (String) hashMap.get("d6_cond_d");//白天天气
            d6_cond_n = (String) hashMap.get("d6_cond_n");//夜间天气
            d6_date = (String) hashMap.get("d6_date");
            d6_tmp_max = (String) hashMap.get("d6_tmp_max");//最高气温
            d6_tmp_min = (String) hashMap.get("d6_tmp_min");//最低气温
            d7_sr = (String) hashMap.get("d7_sr");//第七天日出时间
            d7_ss = (String) hashMap.get("d7_ss");//日落时间
            d7_cond_d = (String) hashMap.get("d7_cond_d");//白天天气
            d7_cond_n = (String) hashMap.get("d7_cond_n");//夜间天气
            d7_date = (String) hashMap.get("d7_date");
            d7_tmp_max = (String) hashMap.get("d7_tmp_max");//最高气温
            d7_tmp_min = (String) hashMap.get("d7_tmp_min");//最低气温
        }
    }
    /******向Fragment传数据******/
    public void sendToFragment(){
        //向Fragment传递数据
        Weather weather = new Weather(data);
        Intent intent1 = new Intent();
        intent1.setAction("FirstPage");//firstpage
        intent1.putExtra("update",weather.update);
        intent1.putExtra("now_cond", weather.now_cond);
        intent1.putExtra("now_tmp",weather.now_tmp);
        intent1.putExtra("now_fl",weather.now_fl);
        intent1.putExtra("now_hum",weather.now_hum);
        intent1.putExtra("now_wind_dir",weather.now_wind_dir);
        intent1.putExtra("now_wind_sc",weather.now_wind_sc);
        intent1.putExtra("now_vis",weather.now_vis);
        intent1.putExtra("qlty",weather.qlty);
        intent1.putExtra("aqi",weather.aqi);
        intent1.putExtra("pm10",weather.pm10);
        intent1.putExtra("pm25",weather.pm25);
        intent1.putExtra("no2",weather.no2);
        intent1.putExtra("so2",weather.so2);
        intent1.putExtra( "d1_sr",weather.d1_sr);//第一天日出时间
        intent1.putExtra("d1_ss",weather.d1_ss);//日落时间
        intent1.putExtra("d1_cond_d",weather.d1_cond_d);//白天天气
        intent1.putExtra("d1_cond_n",weather.d1_cond_n);//夜间天气
        intent1.putExtra("d1_date",weather.d1_date);
        intent1.putExtra("d1_tmp_max",weather.d1_tmp_max);//最高气温
        intent1.putExtra("d1_tmp_min",weather.d1_tmp_min);//最低气温
        intent1.putExtra("comf",weather.comf);//舒适
        intent1.putExtra("cw",weather.cw);//洗车
        intent1.putExtra("sport",weather.sport);//运动
        intent1.putExtra("uv",weather.uv);//紫外线
        sendBroadcast(intent1);

        Intent intent2 = new Intent();
        intent2.setAction("SecondPage");//firstpage
        intent2.putExtra("d1_sr",weather.d1_sr);//第一天日出时间
        intent2.putExtra("d1_ss",weather.d1_ss);//日落时间
        intent2.putExtra("d1_cond_d",weather.d1_cond_d);//白天天气
        intent2.putExtra("d1_cond_n",weather.d1_cond_n);//夜间天气
        intent2.putExtra("d1_date",weather.d1_date);
        intent2.putExtra("d1_tmp_max",weather.d1_tmp_max);//最高气温
        intent2.putExtra("d1_tmp_min",weather.d1_tmp_min);//最低气温
        intent2.putExtra("d2_sr",weather.d2_sr);//第二天日出时间
        intent2.putExtra("d2_ss",weather.d2_ss);//日落时间
        intent2.putExtra("d2_cond_d",weather.d2_cond_d);//白天天气
        intent2.putExtra("d2_cond_n",weather.d2_cond_n);//夜间天气
        intent2.putExtra("d2_date",weather.d2_date);
        intent2.putExtra("d2_tmp_max",weather.d2_tmp_max);//最高气温
        intent2.putExtra("d2_tmp_min",weather.d2_tmp_min);//最低气温
        intent2.putExtra("d3_sr",weather.d3_sr);//第三天日出时间
        intent2.putExtra("d3_ss",weather.d3_ss);//日落时间
        intent2.putExtra("d3_cond_d",weather.d3_cond_d);//白天天气
        intent2.putExtra("d3_cond_n",weather.d3_cond_n);//夜间天气
        intent2.putExtra("d3_date",weather.d3_date);
        intent2.putExtra("d3_tmp_max",weather.d3_tmp_max);//最高气温
        intent2.putExtra("d3_tmp_min",weather.d3_tmp_min);//最低气温
        intent2.putExtra("d4_sr",weather.d4_sr);//第四天日出时间
        intent2.putExtra("d4_ss",weather.d4_ss);//日落时间
        intent2.putExtra("d4_cond_d",weather.d4_cond_d);//白天天气
        intent2.putExtra("d4_cond_n",weather.d4_cond_n);//夜间天气
        intent2.putExtra("d4_date",weather.d4_date);
        intent2.putExtra("d4_tmp_max",weather.d4_tmp_max);//最高气温
        intent2.putExtra("d4_tmp_min",weather.d4_tmp_min);//最低气温
        intent2.putExtra("d5_sr",weather.d5_sr);//第五天日出时间
        intent2.putExtra("d5_ss",weather.d5_ss);//日落时间
        intent2.putExtra("d5_cond_d",weather.d5_cond_d);//白天天气
        intent2.putExtra("d5_cond_n",weather.d5_cond_n);//夜间天气
        intent2.putExtra("d5_date",weather.d5_date);
        intent2.putExtra("d5_tmp_max",weather.d5_tmp_max);//最高气温
        intent2.putExtra("d5_tmp_min",weather.d5_tmp_min);//最低气温
        intent2.putExtra("d6_sr",weather.d6_sr);//第六天日出时间
        intent2.putExtra("d6_ss",weather.d6_ss);//日落时间
        intent2.putExtra("d6_cond_d",weather.d6_cond_d);//白天天气
        intent2.putExtra("d6_cond_n",weather.d6_cond_n);//夜间天气
        intent2.putExtra("d6_date",weather.d6_date);
        intent2.putExtra("d6_tmp_max",weather.d6_tmp_max);//最高气温
        intent2.putExtra("d6_tmp_min",weather.d6_tmp_min);//最低气温
        intent2.putExtra("d7_sr",weather.d7_sr);//第七天日出时间
        intent2.putExtra("d7_ss",weather.d7_ss);//日落时间
        intent2.putExtra("d7_cond_d",weather.d7_cond_d);//白天天气
        intent2.putExtra("d7_cond_n",weather.d7_cond_n);//夜间天气
        intent2.putExtra("d7_date",weather.d7_date);
        intent2.putExtra("d7_tmp_max",weather.d7_tmp_max);//最高气温
        intent2.putExtra("d7_tmp_min",weather.d7_tmp_min);//最低气温
        sendBroadcast(intent2);
    }

    /*********调用API**********/
    public void apiTest(final String string) {

        Parameters para = new Parameters();

        para.put("city", string);
        ApiStoreSDK.execute("http://apis.baidu.com/heweather/weather/free",
                ApiStoreSDK.GET,
                para,
                new ApiCallBack() {

                    @Override
                    public void onSuccess(int status, String responseString) {
                        Log.i("sdkdemo", "onSuccess" + responseString);
                        Log.v("sdkdemo", "status:"+status);
                        saveDataToFile(MainActivity.this, "data", responseString);//Json数据缓存入文件
                        data = getData(responseString);
                        city = (String) data.get("city");
                        now_cond = (String) data.get("now_cond");
                        switch (currentPage){
                            case 0:
                                toolbar.setTitle((String)data.get("city"));
                                break;
                            case 1:
                                toolbar.setTitle((String)data.get("city")+" "+(String) data.get("now_cond"));
                                break;
                            default:
                                toolbar.setTitle((String)data.get("city"));
                                break;
                        }
                        //向Fragment传递数据
                        sendToFragment();
                    }

                    @Override
                    public void onComplete() {
                        Log.i("sdkdemo", "onComplete");
                    }

                    @Override
                    public void onError(int status, String responseString, Exception e) {
                        Log.i("sdkdemo", "onError, status: " + status);
                        Log.i("sdkdemo", "errMsg: " + (e == null ? "" : e.getMessage()));
                        if (new File("data").exists()){
                            Log.v("file","file exits");
                            data = getData(getDataFromFile(MainActivity.this,"data"));
                        }
                        else{
                            Log.v("file","file not exit");
                            InputStream inputStream = getResources().openRawResource(R.raw.data);
                            data = getData(getDataFromInputStream(inputStream));
                        }
                        city = (String) data.get("city");
                        now_cond = (String) data.get("now_cond");
                        switch (currentPage){
                            case 0:
                                toolbar.setTitle((String)data.get("city"));
                                break;
                            case 1:
                                toolbar.setTitle((String)data.get("city")+" "+(String) data.get("now_cond"));
                                break;
                            default:
                                toolbar.setTitle((String)data.get("city"));
                                break;
                        }
                        //sendToFragment();
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                sendToFragment();
                            }
                        }.start();
                    }

                });

    }

    String getStackTrace(Throwable e) {
        if (e == null) {
            return "";
        }
        StringBuilder str = new StringBuilder();
        str.append(e.getMessage()).append("\n");
        for (int i = 0; i < e.getStackTrace().length; i++) {
            str.append(e.getStackTrace()[i]).append("\n");
        }
        return str.toString();
    }

    /********写入文件*********/
    private void saveDataToFile(Context context, String fileName, String data){
        FileOutputStream fileOutputStream=null;
        OutputStreamWriter outputStreamWriter=null;
        BufferedWriter bufferedWriter=null;
        try {
            fileOutputStream=context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStreamWriter=new OutputStreamWriter(fileOutputStream);
            bufferedWriter=new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (bufferedWriter!=null) {
                    bufferedWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    /********读取文件*********/
    private String getDataFromFile(Context context,String fileName){
        FileInputStream fileInputStream=null;
        InputStreamReader inputStreamReader=null;
        BufferedReader bufferedReader=null;
        StringBuilder stringBuilder=null;
        String line=null;
        try {
            stringBuilder=new StringBuilder();
            fileInputStream=context.openFileInput(fileName);
            inputStreamReader=new InputStreamReader(fileInputStream);
            bufferedReader=new BufferedReader(inputStreamReader);
            while((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (bufferedReader!=null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
    /********读取inputstream*********/
    private String getDataFromInputStream(InputStream inputStream){
        InputStreamReader inputStreamReader=null;
        BufferedReader bufferedReader=null;
        StringBuilder stringBuilder=null;
        String line=null;
        try {
            stringBuilder=new StringBuilder();
            inputStreamReader=new InputStreamReader(inputStream);
            bufferedReader=new BufferedReader(inputStreamReader);
            while((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (bufferedReader!=null) {
                    bufferedReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    /********解析JSON*********/
    private HashMap<String,Object> getData(String string){
        HashMap<String,Object> map = new HashMap<String, Object>();
        try {
            JSONArray jsonArray = new JSONObject(string).getJSONArray("HeWeather data service 3.0");
            JSONObject basicObj = jsonArray.getJSONObject(0).getJSONObject("basic");
            String city = basicObj.getString("city");//城市
            map.put("city",city);
            JSONObject updateObj = basicObj.getJSONObject("update");
            String update = updateObj.getString("loc");//更新时间
            map.put("update",update);
            JSONObject nowObj = jsonArray.getJSONObject(0).getJSONObject("now");
            String now_cond = nowObj.getJSONObject("cond").getString("txt");//当前天气
            map.put("now_cond",now_cond);
            String now_fl = nowObj.getString("fl");//当前体感温度
            map.put("now_fl",now_fl);
            String now_hum = nowObj.getString("hum");//当前湿度
            map.put("now_hum",now_hum);
            String now_tmp = nowObj.getString("tmp");//当前温度
            map.put("now_tmp",now_tmp);
            String now_vis = nowObj.getString("vis");//当前能见度
            map.put("now_vis",now_vis);
            String now_wind_dir = nowObj.getJSONObject("wind").getString("dir");//当前风向
            map.put("now_wind_dir",now_wind_dir);
            String now_wind_sc = nowObj.getJSONObject("wind").getString("sc");//当前风力
            map.put("now_wind_sc",now_wind_sc);

            JSONArray dailyArray = jsonArray.getJSONObject(0).getJSONArray("daily_forecast");
            JSONObject dailyObj1 = dailyArray.getJSONObject(0);//第一天
            String d1_sr = dailyObj1.getJSONObject("astro").getString("sr");//日出时间
            map.put("d1_sr",d1_sr);
            String d1_ss = dailyObj1.getJSONObject("astro").getString("ss");//日落时间
            map.put("d1_ss",d1_ss);
            String d1_cond_d = dailyObj1.getJSONObject("cond").getString("txt_d");//白天天气
            map.put("d1_cond_d",d1_cond_d);
            String d1_cond_n = dailyObj1.getJSONObject("cond").getString("txt_n");//夜间天气
            map.put("d1_cond_n",d1_cond_n);
            String d1_date = dailyObj1.getString("date");
            map.put("d1_date",d1_date);
            String d1_tmp_max = dailyObj1.getJSONObject("tmp").getString("max");//最高气温
            map.put("d1_tmp_max",d1_tmp_max);
            String d1_tmp_min = dailyObj1.getJSONObject("tmp").getString("min");//最低气温
            map.put("d1_tmp_min",d1_tmp_min);

            JSONObject dailyObj2 = dailyArray.getJSONObject(1);//第二天
            String d2_sr = dailyObj2.getJSONObject("astro").getString("sr");//日出时间
            map.put("d2_sr",d2_sr);
            String d2_ss = dailyObj2.getJSONObject("astro").getString("ss");//日落时间
            map.put("d2_ss",d2_ss);
            String d2_cond_d = dailyObj2.getJSONObject("cond").getString("txt_d");//白天天气
            map.put("d2_cond_d",d2_cond_d);
            String d2_cond_n = dailyObj2.getJSONObject("cond").getString("txt_n");//夜间天气
            map.put("d2_cond_n",d2_cond_n);
            String d2_date = dailyObj2.getString("date");
            map.put("d2_date",d2_date);
            String d2_tmp_max = dailyObj2.getJSONObject("tmp").getString("max");//最高气温
            map.put("d2_tmp_max",d2_tmp_max);
            String d2_tmp_min = dailyObj2.getJSONObject("tmp").getString("min");//最低气温
            map.put("d2_tmp_min",d2_tmp_min);

            JSONObject dailyObj3 = dailyArray.getJSONObject(2);//第三天
            String d3_sr = dailyObj3.getJSONObject("astro").getString("sr");//日出时间
            map.put("d3_sr",d3_sr);
            String d3_ss = dailyObj3.getJSONObject("astro").getString("ss");//日落时间
            map.put("d3_ss",d3_ss);
            String d3_cond_d = dailyObj3.getJSONObject("cond").getString("txt_d");//白天天气
            map.put("d3_cond_d",d3_cond_d);
            String d3_cond_n = dailyObj3.getJSONObject("cond").getString("txt_n");//夜间天气
            map.put("d3_cond_n",d3_cond_n);
            String d3_date = dailyObj3.getString("date");
            map.put("d3_date",d3_date);
            String d3_tmp_max = dailyObj3.getJSONObject("tmp").getString("max");//最高气温
            map.put("d3_tmp_max",d3_tmp_max);
            String d3_tmp_min = dailyObj3.getJSONObject("tmp").getString("min");//最低气温
            map.put("d3_tmp_min",d3_tmp_min);

            JSONObject dailyObj4 = dailyArray.getJSONObject(3);//第四天
            String d4_sr = dailyObj4.getJSONObject("astro").getString("sr");//日出时间
            map.put("d4_sr",d4_sr);
            String d4_ss = dailyObj4.getJSONObject("astro").getString("ss");//日落时间
            map.put("d4_ss",d4_ss);
            String d4_cond_d = dailyObj4.getJSONObject("cond").getString("txt_d");//白天天气
            map.put("d4_cond_d",d4_cond_d);
            String d4_cond_n = dailyObj4.getJSONObject("cond").getString("txt_n");//夜间天气
            map.put("d4_cond_n",d4_cond_n);
            String d4_date = dailyObj4.getString("date");
            map.put("d4_date",d4_date);
            String d4_tmp_max = dailyObj4.getJSONObject("tmp").getString("max");//最高气温
            map.put("d4_tmp_max",d4_tmp_max);
            String d4_tmp_min = dailyObj4.getJSONObject("tmp").getString("min");//最低气温
            map.put("d4_tmp_min",d4_tmp_min);

            JSONObject dailyObj5 = dailyArray.getJSONObject(4);//第五天
            String d5_sr = dailyObj5.getJSONObject("astro").getString("sr");//日出时间
            map.put("d5_sr",d5_sr);
            String d5_ss = dailyObj5.getJSONObject("astro").getString("ss");//日落时间
            map.put("d5_ss",d5_ss);
            String d5_cond_d = dailyObj5.getJSONObject("cond").getString("txt_d");//白天天气
            map.put("d5_cond_d",d5_cond_d);
            String d5_cond_n = dailyObj5.getJSONObject("cond").getString("txt_n");//夜间天气
            map.put("d5_cond_n",d5_cond_n);
            String d5_date = dailyObj5.getString("date");
            map.put("d5_date",d5_date);
            String d5_tmp_max = dailyObj5.getJSONObject("tmp").getString("max");//最高气温
            map.put("d5_tmp_max",d5_tmp_max);
            String d5_tmp_min = dailyObj5.getJSONObject("tmp").getString("min");//最低气温
            map.put("d5_tmp_min",d5_tmp_min);

            JSONObject dailyObj6 = dailyArray.getJSONObject(5);//第六天
            String d6_sr = dailyObj6.getJSONObject("astro").getString("sr");//日出时间
            map.put("d6_sr",d6_sr);
            String d6_ss = dailyObj6.getJSONObject("astro").getString("ss");//日落时间
            map.put("d6_ss",d6_ss);
            String d6_cond_d = dailyObj6.getJSONObject("cond").getString("txt_d");//白天天气
            map.put("d6_cond_d",d6_cond_d);
            String d6_cond_n = dailyObj6.getJSONObject("cond").getString("txt_n");//夜间天气
            map.put("d6_cond_n",d6_cond_n);
            String d6_date = dailyObj6.getString("date");
            map.put("d6_date",d6_date);
            String d6_tmp_max = dailyObj6.getJSONObject("tmp").getString("max");//最高气温
            map.put("d6_tmp_max",d6_tmp_max);
            String d6_tmp_min = dailyObj6.getJSONObject("tmp").getString("min");//最低气温
            map.put("d6_tmp_min",d6_tmp_min);

            JSONObject dailyObj7 = dailyArray.getJSONObject(6);//第七天
            String d7_sr = dailyObj7.getJSONObject("astro").getString("sr");//日出时间
            map.put("d7_sr",d7_sr);
            String d7_ss = dailyObj7.getJSONObject("astro").getString("ss");//日落时间
            map.put("d7_ss",d7_ss);
            String d7_cond_d = dailyObj7.getJSONObject("cond").getString("txt_d");//白天天气
            map.put("d7_cond_d",d7_cond_d);
            String d7_cond_n = dailyObj7.getJSONObject("cond").getString("txt_n");//夜间天气
            map.put("d7_cond_n",d7_cond_n);
            String d7_date = dailyObj7.getString("date");
            map.put("d7_date",d7_date);
            String d7_tmp_max = dailyObj7.getJSONObject("tmp").getString("max");//最高气温
            map.put("d7_tmp_max",d7_tmp_max);
            String d7_tmp_min = dailyObj7.getJSONObject("tmp").getString("min");//最低气温
            map.put("d7_tmp_min",d7_tmp_min);

            JSONObject suggestionObj = jsonArray.getJSONObject(0).getJSONObject("suggestion");
            String comf = suggestionObj.getJSONObject("comf").getString("txt");//舒适
            map.put("comf",comf);
            String cw = suggestionObj.getJSONObject("cw").getString("txt");//洗车
            map.put("cw",cw);
            String sport = suggestionObj.getJSONObject("sport").getString("txt");//运动
            map.put("sport",sport);
            String uv = suggestionObj.getJSONObject("uv").getString("txt");//紫外线
            map.put("uv",uv);

//            JSONArray hourlyArray = jsonArray.getJSONObject(0).getJSONArray("hourly_forecast");
//            JSONObject hourlyObj1 = hourlyArray.getJSONObject(0);
//            String h1_date = hourlyObj1.getString("date");
//            map.put("h1_date",h1_date);
//            String h1_hum = hourlyObj1.getString("hum");
//            map.put("h1_hum",h1_hum);
//            String h1_tmp = hourlyObj1.getString("tmp");
//            map.put("h1_tmp",h1_tmp);
//            JSONObject h1_wind = hourlyObj1.getJSONObject("wind");
//            String h1_wind_dir = h1_wind.getString("dir");
//            map.put("h1_wind_dir",h1_wind_dir);
//            String h1_wind_sc = h1_wind.getString("sc");
//            map.put("h1-wind_sc",h1_wind_sc);

//            JSONObject hourlyObj2 = hourlyArray.getJSONObject(1);
//            String h2_date = hourlyObj2.getString("date");
//            map.put("h2_date",h2_date);
//            String h2_hum = hourlyObj2.getString("hum");
//            map.put("h2_hum",h2_hum);
//            String h2_tmp = hourlyObj2.getString("tmp");
//            map.put("h2_tmp",h2_tmp);
//            JSONObject h2_wind = hourlyObj2.getJSONObject("wind");
//            String h2_wind_dir = h2_wind.getString("dir");
//            map.put("h2_wind_dir",h2_wind_dir);
//            String h2_wind_sc = h2_wind.getString("sc");
//            map.put("h2-wind_sc",h2_wind_sc);
//
//            JSONObject hourlyObj3 = hourlyArray.getJSONObject(2);
//            String h3_date = hourlyObj3.getString("date");
//            map.put("h3_date",h3_date);
//            String h3_hum = hourlyObj3.getString("hum");
//            map.put("h3_hum",h3_hum);
//            String h3_tmp = hourlyObj3.getString("tmp");
//            map.put("h3_tmp",h3_tmp);
//            JSONObject h3_wind = hourlyObj3.getJSONObject("wind");
//            String h3_wind_dir = h3_wind.getString("dir");
//            map.put("h3_wind_dir",h3_wind_dir);
//            String h3_wind_sc = h3_wind.getString("sc");
//            map.put("h3-wind_sc",h3_wind_sc);

            JSONObject aqiObj = jsonArray.getJSONObject(0).getJSONObject("aqi").getJSONObject("city");
            String aqi = aqiObj.getString("aqi"); //空气质量指数
            map.put("aqi",aqi);
            String no2 = aqiObj.getString("no2");//二氧化氮1小时平均值(ug/m³)
            map.put("no2",no2);
            String pm10 = aqiObj.getString("pm10");//PM10 1小时平均值(ug/m³)
            map.put("pm10",pm10);
            String pm25 = aqiObj.getString("pm25"); //PM2.5 1小时平均值(ug/m³)
            map.put("pm25",pm25);
            String qlty = aqiObj.getString("qlty");//空气质量类别
            map.put("qlty",qlty);
            String so2 = aqiObj.getString("so2"); //二氧化硫1小时平均值(ug/m³)
            map.put("so2",so2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }
}
