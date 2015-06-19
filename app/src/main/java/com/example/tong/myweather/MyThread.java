package com.example.tong.myweather;

import android.content.Context;
import android.os.Message;

import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by Tong on 2015/6/18.
 */
public class MyThread implements Runnable{
    private Handler myHandler;
    private Context mContext;
    private String Mycity = "天津";

    public void setMycity(String mycity) {
        Mycity = mycity;
    }
    public MyThread(Handler handler,Context context){
        this.myHandler = handler;
        this.mContext = context;
    }

    @Override
    public void run() {
        final Message msg = myHandler.obtainMessage();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String url = "http://api.map.baidu.com/telematics/v3/weather?location=" +
                Mycity +
                "&output=json&ak=640f3985a6437dad8135dae98d775a09";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.v("Success", "Success.");
                        msg.obj = jsonObject;
                        myHandler.sendMessage(msg);
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
