package com.roman.iweather;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yalantis.phoenix.PullToRefreshView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RoMan on 2016/7/25.
 */
public class SecondPage extends BaseRefreshFragment {

    private PullToRefreshView mPullToRefreshView;
    MyReciver receiver = new MyReciver();
    List<Map<String, String>> weatherList;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.page_second, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setAdapter(new RecyclerAdapter());
        mPullToRefreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_refresh);
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
        filter.addAction("SecondPage");
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
            String d1_sr = intent.getStringExtra("d1_sr");//第一天日出时间
            String d1_ss = intent.getStringExtra("d1_ss");//日落时间
            String d1_cond_d = intent.getStringExtra("d1_cond_d");//白天天气
            String d1_cond_n = intent.getStringExtra("d1_cond_n");//夜间天气
            String d1_date = intent.getStringExtra("d1_date");
            String d1_tmp_max = intent.getStringExtra("d1_tmp_max");//最高气温
            String d1_tmp_min = intent.getStringExtra("d1_tmp_min");//最低气温

            String d2_sr = intent.getStringExtra("d2_sr");//第二天日出时间
            String d2_ss = intent.getStringExtra("d2_ss");//日落时间
            String d2_cond_d = intent.getStringExtra("d2_cond_d");//白天天气
            String d2_cond_n = intent.getStringExtra("d2_cond_n");//夜间天气
            String d2_date = intent.getStringExtra("d2_date");
            String d2_tmp_max = intent.getStringExtra("d2_tmp_max");//最高气温
            String d2_tmp_min = intent.getStringExtra("d2_tmp_min");//最低气温

            String d3_sr = intent.getStringExtra("d3_sr");//第一天日出时间
            String d3_ss = intent.getStringExtra("d3_ss");//日落时间
            String d3_cond_d = intent.getStringExtra("d3_cond_d");//白天天气
            String d3_cond_n = intent.getStringExtra("d3_cond_n");//夜间天气
            String d3_date = intent.getStringExtra("d3_date");
            String d3_tmp_max = intent.getStringExtra("d3_tmp_max");//最高气温
            String d3_tmp_min = intent.getStringExtra("d3_tmp_min");//最低气温

            String d4_sr = intent.getStringExtra("d4_sr");//第一天日出时间
            String d4_ss = intent.getStringExtra("d4_ss");//日落时间
            String d4_cond_d = intent.getStringExtra("d4_cond_d");//白天天气
            String d4_cond_n = intent.getStringExtra("d4_cond_n");//夜间天气
            String d4_date = intent.getStringExtra("d4_date");
            String d4_tmp_max = intent.getStringExtra("d4_tmp_max");//最高气温
            String d4_tmp_min = intent.getStringExtra("d4_tmp_min");//最低气温

            String d5_sr = intent.getStringExtra("d5_sr");//第一天日出时间
            String d5_ss = intent.getStringExtra("d5_ss");//日落时间
            String d5_cond_d = intent.getStringExtra("d5_cond_d");//白天天气
            String d5_cond_n = intent.getStringExtra("d5_cond_n");//夜间天气
            String d5_date = intent.getStringExtra("d5_date");
            String d5_tmp_max = intent.getStringExtra("d5_tmp_max");//最高气温
            String d5_tmp_min = intent.getStringExtra("d5_tmp_min");//最低气温

            String d6_sr = intent.getStringExtra("d6_sr");//第一天日出时间
            String d6_ss = intent.getStringExtra("d6_ss");//日落时间
            String d6_cond_d = intent.getStringExtra("d6_cond_d");//白天天气
            String d6_cond_n = intent.getStringExtra("d6_cond_n");//夜间天气
            String d6_date = intent.getStringExtra("d6_date");
            String d6_tmp_max = intent.getStringExtra("d6_tmp_max");//最高气温
            String d6_tmp_min = intent.getStringExtra("d6_tmp_min");//最低气温

            String d7_sr = intent.getStringExtra("d7_sr");//第一天日出时间
            String d7_ss = intent.getStringExtra("d7_ss");//日落时间
            String d7_cond_d = intent.getStringExtra("d7_cond_d");//白天天气
            String d7_cond_n = intent.getStringExtra("d7_cond_n");//夜间天气
            String d7_date = intent.getStringExtra("d7_date");
            String d7_tmp_max = intent.getStringExtra("d7_tmp_max");//最高气温
            String d7_tmp_min = intent.getStringExtra("d7_tmp_min");//最低气温

            String[] sr = {d1_sr,d2_sr,d3_sr,d4_sr,d5_sr,d6_sr,d7_sr};
            String[] ss = {d1_ss,d2_ss,d3_ss,d4_ss,d5_ss,d6_ss,d7_ss};
            String[] cond_d = {d1_cond_d,d2_cond_d,d3_cond_d,d4_cond_d,d5_cond_d,d6_cond_d,d7_cond_d};
            String[] cond_n = {d1_cond_n,d2_cond_n,d3_cond_n,d4_cond_n,d5_cond_n,d6_cond_n,d7_cond_n};
            String[] date = {d1_date,d2_date,d3_date,d4_date,d5_date,d6_date,d7_date};
            String[] tmp_max = {d1_tmp_max,d2_tmp_max,d3_tmp_max,d4_tmp_max,d5_tmp_max,d6_tmp_max,d7_tmp_max};
            String[] tmp_min = {d1_tmp_min,d2_tmp_min,d3_tmp_min,d4_tmp_min,d5_tmp_min,d6_tmp_min,d7_tmp_min};
            weatherList = new ArrayList<>();
            Map<String, String> map;
            for (int i=0 ; i<sr.length ; i++){
                map = new HashMap<>();
                map.put("sr",sr[i]);
                map.put("ss",ss[i]);
                map.put("cond_d",cond_d[i]);
                map.put("cond_n",cond_n[i]);
                map.put("date",date[i]);
                map.put("tmp_max",tmp_max[i]);
                map.put("tmp_min",tmp_min[i]);
                weatherList.add(map);
            }
            recyclerView.setAdapter(new RecyclerAdapter());
        }
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
    enum Weather{
        晴,多云,阴,阵雨,雾,雷阵雨,小雨,中雨,大雨;
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<WeatherHolder> {

        @Override
        public WeatherHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.page_second_recycler, parent, false);
            return new WeatherHolder(view);
        }

        @Override
        public void onBindViewHolder(WeatherHolder holder, int pos) {
            Map<String, String> data = weatherList.get(pos);
            holder.bindData(data);
        }

        @Override
        public int getItemCount() {
            return weatherList.size();
        }
    }

    private class WeatherHolder extends RecyclerView.ViewHolder {

        private View rootView;
        private TextView textView10;
        private TextView textView11;
        private TextView textView12;
        private TextView textView13;
        private TextView textView14;
        private TextView textView15;
        private TextView textView16;
        private TextView textView17;
        private ImageView imageView1;
        private ImageView imageView2;
        private RelativeLayout layout1;
        private RelativeLayout layout2;
        private RelativeLayout layout3;
        private RelativeLayout layout4;

        private Map<String, String> mData;

        public WeatherHolder(View itemView) {
            super(itemView);
            rootView = itemView;

            textView10 = (TextView) rootView.findViewById(R.id.textView10);
            textView11 = (TextView) rootView.findViewById(R.id.textView11);
            textView12 = (TextView) rootView.findViewById(R.id.textView12);
            textView13 = (TextView) rootView.findViewById(R.id.textView13);
            textView14 = (TextView) rootView.findViewById(R.id.textView14);
            textView15 = (TextView) rootView.findViewById(R.id.textView15);
            textView16 = (TextView) rootView.findViewById(R.id.textView16);
            textView17 = (TextView) rootView.findViewById(R.id.textView17);
            imageView1 = (ImageView)rootView.findViewById(R.id.imageView4);
            imageView2 = (ImageView)rootView.findViewById(R.id.imageView5);
            layout1 = (RelativeLayout)rootView.findViewById(R.id.layout1);
            layout2 = (RelativeLayout)rootView.findViewById(R.id.layout2);
            layout3 = (RelativeLayout)rootView.findViewById(R.id.layout3);
            layout4 = (RelativeLayout)rootView.findViewById(R.id.layout4);
        }

        public void bindData(Map<String, String> data) {
            mData = data;

            textView10.setText(data.get("tmp_max"));
            textView11.setText(data.get("tmp_min"));
            textView12.setText(getWeek(data.get("date")));
            textView13.setText(data.get("cond_d"));
            textView14.setText(data.get("date"));
            textView15.setText(data.get("cond_n"));
            textView16.setText(data.get("sr"));
            textView17.setText(data.get("ss"));
            try {
                matchPic_d(data.get("cond_d"),imageView1);
            }catch (Exception e){
                System.out.print(data.get("cond_d"));
            }
            try {
                matchPic_n(data.get("cond_n"),imageView2);
            }catch (Exception e){
                System.out.print(data.get("cond_n"));
            }
            if (getWeek(data.get("date")).equals("星期一")){
                layout1.setBackgroundResource(R.color.tmp1);
                layout2.setBackgroundResource(R.color.cond_d1);
                layout3.setBackgroundResource(R.color.cond_n1);
                layout4.setBackgroundResource(R.color.sun1);
            }else if (getWeek(data.get("date")).equals("星期二")){
                layout1.setBackgroundResource(R.color.tmp2);
                layout2.setBackgroundResource(R.color.cond_d2);
                layout3.setBackgroundResource(R.color.cond_n2);
                layout4.setBackgroundResource(R.color.sun2);
            }else if (getWeek(data.get("date")).equals("星期三")){
                layout1.setBackgroundResource(R.color.tmp3);
                layout2.setBackgroundResource(R.color.cond_d3);
                layout3.setBackgroundResource(R.color.cond_n3);
                layout4.setBackgroundResource(R.color.sun3);
            }else if (getWeek(data.get("date")).equals("星期四")){
                layout1.setBackgroundResource(R.color.tmp4);
                layout2.setBackgroundResource(R.color.cond_d4);
                layout3.setBackgroundResource(R.color.cond_n4);
                layout4.setBackgroundResource(R.color.sun4);
            }else if (getWeek(data.get("date")).equals("星期五")){
                layout1.setBackgroundResource(R.color.tmp5);
                layout2.setBackgroundResource(R.color.cond_d5);
                layout3.setBackgroundResource(R.color.cond_n5);
                layout4.setBackgroundResource(R.color.sun5);
            }else if (getWeek(data.get("date")).equals("星期六")){
                layout1.setBackgroundResource(R.color.tmp6);
                layout2.setBackgroundResource(R.color.cond_d6);
                layout3.setBackgroundResource(R.color.cond_n6);
                layout4.setBackgroundResource(R.color.sun6);
            }else{
                layout1.setBackgroundResource(R.color.tmp);
                layout2.setBackgroundResource(R.color.cond_d);
                layout3.setBackgroundResource(R.color.cond_n);
                layout4.setBackgroundResource(R.color.sun);
            }
        }
    }
}
