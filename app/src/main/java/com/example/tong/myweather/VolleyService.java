package com.example.tong.myweather;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


/**
 * Created by Tong on 2015/6/17.
 */
public class VolleyService {
    private Context mContext;
    private JSONObject mJSONObject;
    private Thread mThread;
    private String cur_city = "天津";
    private void VolleyService(Context context){
        mContext = context;
    }
    private void setCur_city(String curcity){
        this.cur_city = curcity;
    }



    public void run() {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String url = "http://api.map.baidu.com/telematics/v3/weather?location=" +
                cur_city +
                "&output=json&ak=640f3985a6437dad8135dae98d775a09";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.v("Success","Success.");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.v("Error",volleyError.toString());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}
