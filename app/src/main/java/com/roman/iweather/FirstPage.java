package com.roman.iweather;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yalantis.phoenix.PullToRefreshView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by RoMan on 2016/7/25.
 */
public class FirstPage extends BaseRefreshFragment {

    private PullToRefreshView mPullToRefreshView;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    TextView textView7;
    TextView textView8;
    TextView textView9;
    TextView textView10;
    TextView textView11;
    TextView textView12;
    TextView textView13;
    TextView textView14;
    TextView textView15;
    TextView textView16;
    TextView textView17;
    TextView textView18;
    TextView textView19;
    TextView textView20;
    TextView textView21;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    MyReciver receiver = new MyReciver();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.page_first, container, false);
        mPullToRefreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_refresh);
        textView1 = (TextView) rootView.findViewById(R.id.textView1);
        textView2 = (TextView) rootView.findViewById(R.id.textView2);
        textView3 = (TextView) rootView.findViewById(R.id.textView3);
        textView4 = (TextView) rootView.findViewById(R.id.textView4);
        textView5 = (TextView) rootView.findViewById(R.id.textView5);
        textView6 = (TextView) rootView.findViewById(R.id.textView6);
        textView7 = (TextView) rootView.findViewById(R.id.textView7);
        textView8 = (TextView) rootView.findViewById(R.id.textView8);
        textView9 = (TextView) rootView.findViewById(R.id.textView9);
        textView10 = (TextView) rootView.findViewById(R.id.textView10);
        textView11 = (TextView) rootView.findViewById(R.id.textView11);
        textView12 = (TextView) rootView.findViewById(R.id.textView12);
        textView13 = (TextView) rootView.findViewById(R.id.textView13);
        textView14 = (TextView) rootView.findViewById(R.id.textView14);
        textView15 = (TextView) rootView.findViewById(R.id.textView15);
        textView16 = (TextView) rootView.findViewById(R.id.textView16);
        textView17 = (TextView) rootView.findViewById(R.id.textView17);
        textView18 = (TextView) rootView.findViewById(R.id.textView18);
        textView19 = (TextView) rootView.findViewById(R.id.textView19);
        textView20 = (TextView) rootView.findViewById(R.id.textView20);
        textView21 = (TextView) rootView.findViewById(R.id.textView21);
        imageView1 = (ImageView) rootView.findViewById(R.id.imageView);
        imageView2 = (ImageView) rootView.findViewById(R.id.imageView4);
        imageView3 = (ImageView) rootView.findViewById(R.id.imageView5);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.apiTest(mainActivity.city);
                    }
                }, REFRESH_DELAY);
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        IntentFilter filter = new IntentFilter();
        filter.addAction("FirstPage");
        activity.registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(receiver);
    }

    class MyReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String update = intent.getStringExtra("update");
            String now_cond = intent.getStringExtra("now_cond");//当前天气
            String now_fl = intent.getStringExtra("now_fl");//当前体感温度
            String now_hum = intent.getStringExtra("now_hum");//当前湿度
            String now_tmp = intent.getStringExtra("now_tmp");//当前温度
            String now_wind_dir = intent.getStringExtra("now_wind_dir");//当前风向
            String now_wind_sc = intent.getStringExtra("now_wind_sc");//当前风力
            String now_vis = intent.getStringExtra("now_vis");//当前能见度
            String aqi = intent.getStringExtra("aqi"); //空气质量指数
            String no2 = intent.getStringExtra("no2");//二氧化氮1小时平均值(ug/m³)
            String pm10 = intent.getStringExtra("pm10");//PM10 1小时平均值(ug/m³)
            String pm25 = intent.getStringExtra("pm25"); //PM2.5 1小时平均值(ug/m³)
            String qlty = intent.getStringExtra("qlty");//空气质量类别
            String so2 = intent.getStringExtra("so2"); //二氧化硫1小时平均值(ug/m³)
            String d1_sr = intent.getStringExtra("d1_sr");//第一天日出时间
            String d1_ss = intent.getStringExtra("d1_ss");//日落时间
            String d1_cond_d = intent.getStringExtra("d1_cond_d");//白天天气
            String d1_cond_n = intent.getStringExtra("d1_cond_n");//夜间天气
            String d1_date = intent.getStringExtra("d1_date");
            String d1_tmp_max = intent.getStringExtra("d1_tmp_max");//最高气温
            String d1_tmp_min = intent.getStringExtra("d1_tmp_min");//最低气温
            String comf = intent.getStringExtra("comf");//舒适
            String cw = intent.getStringExtra("cw");//洗车
            String sport = intent.getStringExtra("sport");//运动
            String uv = intent.getStringExtra("uv");//紫外线
            textView1.setText(now_tmp+"℃");
            textView2.setText(now_cond);
            textView3.setText("体感温度："+now_fl+"℃"+"         "+"能见度："+now_vis+"km"
                    +"\n相对湿度："+now_hum+"%"+"          "+now_wind_dir+now_wind_sc+"级");
            textView4.setText("空气质量\n\n"+qlty);
            if (qlty==null){
                textView4.setText("空气质量\n\n无数据");
                textView4.setBackgroundResource(R.color.level05);}
            else if (qlty.equals("优"))
                textView4.setBackgroundResource(R.color.level01);
            else if(qlty.equals("良"))textView4.setBackgroundResource(R.color.level02);
            else if(qlty.equals("轻度污染"))textView4.setBackgroundResource(R.color.level03);
            else if(qlty.equals("中度污染"))textView4.setBackgroundResource(R.color.level04);
            else if(qlty.equals("重度污染"))textView4.setBackgroundResource(R.color.level05);
            else textView4.setBackgroundResource(R.color.level02);
            textView5.setText("空气指数\n\n"+aqi);
            if (aqi==null){
                textView5.setText("空气指数\n\n无数据");
                textView5.setBackgroundResource(R.color.level15);}
            else if ( Integer.parseInt(aqi) < 51)
                textView5.setBackgroundResource(R.color.level11);
            else if (Integer.parseInt(aqi)<101)textView5.setBackgroundResource(R.color.level12);
            else if (Integer.parseInt(aqi)<151)textView5.setBackgroundResource(R.color.level13);
            else if (Integer.parseInt(aqi)<201)textView5.setBackgroundResource(R.color.level14);
            else textView5.setBackgroundResource(R.color.level15);
            textView6.setText("PM2.5\n\n"+pm25);
            if (pm25==null){
                textView6.setText("PM2.5\n\n无数据");
                textView6.setBackgroundResource(R.color.level25);}
            else if ( Integer.parseInt(pm25) < 51)
                textView6.setBackgroundResource(R.color.level21);
            else if (Integer.parseInt(pm25)<101)textView6.setBackgroundResource(R.color.level22);
            else if (Integer.parseInt(pm25)<151)textView6.setBackgroundResource(R.color.level23);
            else if (Integer.parseInt(pm25)<201)textView6.setBackgroundResource(R.color.level24);
            else textView6.setBackgroundResource(R.color.level25);
            textView7.setText("PM10\n\n"+pm10);
            if (pm10==null){
                textView7.setText("PM10\n\n无数据");
                textView7.setBackgroundResource(R.color.level05);}
            else if ( Integer.parseInt(pm10) < 51)
                textView7.setBackgroundResource(R.color.level01);
            else if (Integer.parseInt(pm10)<101)textView7.setBackgroundResource(R.color.level02);
            else if (Integer.parseInt(pm10)<151)textView7.setBackgroundResource(R.color.level03);
            else if (Integer.parseInt(pm10)<201)textView7.setBackgroundResource(R.color.level04);
            else textView7.setBackgroundResource(R.color.level05);
            textView8.setText("二氧化氮\n\n"+no2);
            if (no2==null){
                textView8.setText("二氧化氮\n\n无数据");
                textView8.setBackgroundResource(R.color.level15);}
            else if ( Integer.parseInt(no2) < 51)
                textView8.setBackgroundResource(R.color.level11);
            else if (Integer.parseInt(no2)<101)textView8.setBackgroundResource(R.color.level12);
            else if (Integer.parseInt(no2)<151)textView8.setBackgroundResource(R.color.level13);
            else if (Integer.parseInt(no2)<201)textView8.setBackgroundResource(R.color.level14);
            else textView8.setBackgroundResource(R.color.level15);
            textView9.setText("二氧化硫\n\n"+so2);
            if (so2==null){
                textView9.setText("二氧化硫\n\n无数据");
                textView9.setBackgroundResource(R.color.level25);}
            else if ( Integer.parseInt(so2) < 51)
                textView9.setBackgroundResource(R.color.level21);
            else if (Integer.parseInt(so2)<101)textView9.setBackgroundResource(R.color.level22);
            else if (Integer.parseInt(so2)<151)textView9.setBackgroundResource(R.color.level23);
            else if (Integer.parseInt(so2)<201)textView9.setBackgroundResource(R.color.level24);
            else textView9.setBackgroundResource(R.color.level25);
            textView10.setText(d1_tmp_max+"℃");
            textView11.setText(d1_tmp_min+"℃");
            textView12.setText(getWeek(d1_date));
            textView13.setText(d1_cond_d);
            textView14.setText(d1_date);
            textView15.setText(d1_cond_n);
            textView16.setText(d1_sr);
            textView17.setText(d1_ss);
            textView18.setText(comf);
            textView19.setText(cw);
            textView20.setText(sport);
            textView21.setText(uv);
            try{
                matchPic(now_cond, update , d1_sr, d1_ss, imageView1);
            }catch (Exception e){
                System.out.print(now_cond);
            }
            try{
                matchPic_d(d1_cond_d,imageView2);
            }catch (Exception e){
                System.out.print(d1_cond_d);
            }
            try{
                matchPic_n(d1_cond_n,imageView3);
            }catch (Exception e){
                System.out.print(d1_cond_n);
            }
        }

        void matchPic(String weather, String nowtime, String srtime, String sstime,ImageView imageView){//根据时间匹配天气图片
            if (weather!=null & nowtime!=null & srtime!=null & sstime!=null ) {
                int nowint,srint,ssint;
                nowint = Integer.parseInt((nowtime.split(" ")[1]).split(":")[0])*100+Integer.parseInt((nowtime.split(" ")[1]).split(":")[1]);
                srint = Integer.parseInt(srtime.split(":")[0])*100+Integer.parseInt(srtime.split(":")[1]);
                ssint = Integer.parseInt(sstime.split(":")[0])*100+Integer.parseInt(sstime.split(":")[1]);
                if (nowint>srint & nowint<ssint) {
                    switch (Weather.valueOf(weather)) {
                        case 晴:
                            imageView.setImageResource(R.drawable.clear_day);
                            break;
                        case 多云:
                            imageView.setImageResource(R.drawable.mostly_cloudy);
                            break;
                        case 阵雨:
                            imageView.setImageResource(R.drawable.rainy_day);
                            break;
                        case 雾:
                            imageView.setImageResource(R.drawable.haze_day);
                            break;
                        case 阴:
                            imageView.setImageResource(R.drawable.cloudy_weather);
                            break;
                        case 雷阵雨:
                            imageView.setImageResource(R.drawable.storm_weather_day);
                            break;
                        case 小雨:
                            imageView.setImageResource(R.drawable.rainy_day);
                            break;
                        case 中雨:
                            imageView.setImageResource(R.drawable.rainy_day);
                            break;
                        case 大雨:
                            imageView.setImageResource(R.drawable.rainy_day);
                            break;
                        default:
                            imageView.setImageResource(R.drawable.clear_day);
                            break;
                    }
                }else{
                    switch (Weather.valueOf(weather)) {
                        case 晴:
                            imageView.setImageResource(R.drawable.clear_night);
                            break;
                        case 多云:
                            imageView.setImageResource(R.drawable.mostly_cloudy_night);
                            break;
                        case 阵雨:
                            imageView.setImageResource(R.drawable.rainy_night);
                            break;
                        case 雾:
                            imageView.setImageResource(R.drawable.haze_night);
                            break;
                        case 阴:
                            imageView.setImageResource(R.drawable.cloudy_weather);
                            break;
                        case 雷阵雨:
                            imageView.setImageResource(R.drawable.storm_weather_night);
                            break;
                        case 小雨:
                            imageView.setImageResource(R.drawable.rainy_night);
                            break;
                        case 中雨:
                            imageView.setImageResource(R.drawable.rainy_night);
                            break;
                        case 大雨:
                            imageView.setImageResource(R.drawable.rainy_night);
                            break;
                        default:
                            imageView.setImageResource(R.drawable.clear_night);
                            break;
                    }
                }
            }else  imageView.setImageResource(R.drawable.clear_day);
        }
        void matchPic_d(String weather, ImageView imageView){//匹配白天天气图片
            switch (Weather.valueOf(weather)) {
                case 晴:
                    imageView.setImageResource(R.drawable.clear_day_80);
                    break;
                case 多云:
                    imageView.setImageResource(R.drawable.mostly_cloudy_80);
                    break;
                case 阵雨:
                    imageView.setImageResource(R.drawable.rainy_day_80);
                    break;
                case 雾:
                    imageView.setImageResource(R.drawable.haze_day_80);
                case 阴:
                    imageView.setImageResource(R.drawable.cloudy_weather_80);
                    break;
                case 雷阵雨:
                    imageView.setImageResource(R.drawable.storm_weather_day_80);
                    break;
                case 小雨:
                    imageView.setImageResource(R.drawable.rainy_day_80);
                    break;
                case 中雨:
                    imageView.setImageResource(R.drawable.rainy_day_80);
                    break;
                case 大雨:
                    imageView.setImageResource(R.drawable.rainy_day_80);
                    break;
                default:
                    imageView.setImageResource(R.drawable.clear_day_80);
                    break;
            }
        }
        void matchPic_n(String weather, ImageView imageView){//匹配夜间天气图片
            switch (Weather.valueOf(weather)) {
                case 晴:
                    imageView.setImageResource(R.drawable.clear_night_80);
                    break;
                case 多云:
                    imageView.setImageResource(R.drawable.mostly_cloudy_night_80);
                    break;
                case 阵雨:
                    imageView.setImageResource(R.drawable.rainy_night_80);
                    break;
                case 雾:
                    imageView.setImageResource(R.drawable.haze_night_80);
                    break;
                case 雷阵雨:
                    imageView.setImageResource(R.drawable.storm_weather_night_80);
                    break;
                case 小雨:
                    imageView.setImageResource(R.drawable.rainy_night_80);
                    break;
                case 中雨:
                    imageView.setImageResource(R.drawable.rainy_night_80);
                    break;
                case 大雨:
                    imageView.setImageResource(R.drawable.rainy_night_80);
                    break;
                default:
                    imageView.setImageResource(R.drawable.clear_night_80);
                    break;
            }
        }
        String getWeek(String string) {//根据日期获取星期
            String Week = "";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(format.parse(string));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                Week += "星期天";
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 2) {
                Week += "星期一";
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 3) {
                Week += "星期二";
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 4) {
                Week += "星期三";
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 5) {
                Week += "星期四";
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 6) {
                Week += "星期五";
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 7) {
                Week += "星期六";
            }
            return Week;
        }
    }
    enum Weather{
        晴,多云,阴,阵雨,雾,雷阵雨,小雨,中雨,大雨;
    }
}
