package com.example.tong.myweather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {
    private TextView cur_temp = null;
    private TextView cur_weather = null;
    private TextView today_temp = null;
    private TextView tom_temp = null;
    private TextView today_weather = null;
    private TextView tom_weather = null;
    private TextView atom_weather = null;
    private TextView atom_date = null;
    private TextView atom_temp = null;
    private Handler myHandler;
    private SwipeRefreshLayout mSwipeLayout;
    private String cur_city = "天津";
    private MyThread myThread;

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.obj != null){
                JSONObject mJSONObject = (JSONObject)msg.obj;
                try {

                    JSONArray results = mJSONObject.getJSONArray("results");
                    JSONObject result = (JSONObject)results.opt(0);
                    JSONArray weather_data = result.getJSONArray("weather_data");
                    JSONObject weather_data_tod =  (JSONObject)weather_data.opt(0);
                    JSONObject weather_data_tom =  (JSONObject)weather_data.opt(1);
                    JSONObject weather_data_atom =  (JSONObject)weather_data.opt(2);

                    cur_temp.setText(weather_data_tod.getString("temperature").substring(0,2)+"°");
                    cur_weather.setText(result.getString("currentCity")+"|"+weather_data_tod.getString("weather"));

                    today_temp.setText(weather_data_tod.getString("temperature"));
                    today_weather.setText(weather_data_tod.getString("weather"));

                    tom_temp.setText(weather_data_tom.getString("temperature"));
                    tom_weather.setText(weather_data_tom.getString("weather"));

                    atom_date.setText(weather_data_atom.getString("date"));
                    atom_temp.setText(weather_data_atom.getString("temperature"));
                    atom_weather.setText(weather_data_atom.getString("weather"));


                }
                catch(JSONException ex)
                {
                    Log.v("Error", ex.toString());
                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myHandler = new MyHandler();
        mSwipeLayout = (SwipeRefreshLayout)findViewById(R.id.id_swipe);
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyThread myThread = new MyThread(myHandler,MainActivity.this);
                myThread.setMycity(cur_city);
                myThread.run();
            }
        });
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_light);


        cur_temp = (TextView)findViewById(R.id.cur_temp);
        cur_weather = (TextView)findViewById(R.id.cur_weather);

        today_temp = (TextView)findViewById(R.id.today_temp);
        today_weather = (TextView)findViewById(R.id.today_weather);

        tom_temp = (TextView)findViewById(R.id.tom_temp);
        tom_weather = (TextView)findViewById(R.id.tom_weather);

        atom_date = (TextView)findViewById(R.id.atom_date);
        atom_temp = (TextView)findViewById(R.id.atom_temp);
        atom_weather = (TextView)findViewById(R.id.atom_weather);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            initdialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void initdialog(){
        final EditText editText = new EditText(MainActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("PLease input city.");
        builder.setView(editText);
        builder.setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cur_city = editText.getText().toString();
                myThread = new MyThread(myHandler,MainActivity.this);
                myThread.setMycity(cur_city);
                myThread.run();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
