package com.roman.iweather;

import android.util.Log;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by RoMan on 2016/7/27.
 */
public class RequestData {
    public HashMap<String,Object> data;
    public String city;
    boolean isFinished = false;

    /*********调用API**********/
    public void apiTest(String string) {

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
                        data = getData(responseString);
                        Log.v("sdkdemo", (String) data.get("city")+"       Jsondata");
                        Log.v("sdkdemo", (String) data.get("now_wind_sc")+"       Jsondata");
                        city = (String) data.get("city");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("sdkdemo", "onComplete");
                    }

                    @Override
                    public void onError(int status, String responseString, Exception e) {
                        Log.i("sdkdemo", "onError, status: " + status);
                        Log.i("sdkdemo", "errMsg: " + (e == null ? "" : e.getMessage()));
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

    /********解析JSON*********/
    public HashMap<String,Object> getData(String string){
        HashMap<String,Object> map = new HashMap<String, Object>();
        try {
            JSONArray jsonArray = new JSONObject(string).getJSONArray("HeWeather data service 3.0");
            Log.v("Jsontest", "Jsontest>>"+ jsonArray);
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
            String now_wind_dir = nowObj.getJSONObject("wind").getString("dir");//当前风向
            map.put("now_wind_dir",now_wind_dir);
            String now_wind_sc = nowObj.getJSONObject("wind").getString("sc");//当前风力
            map.put("now_wind_sc",now_wind_sc);

            JSONArray hourlyArray = jsonArray.getJSONObject(0).getJSONArray("hourly_forecast");
            JSONObject hourlyObj1 = hourlyArray.getJSONObject(0);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("error>>>>>>>>>","..........................................................................");
        }
        return map;
    }
}
