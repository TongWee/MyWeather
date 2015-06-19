package com.example.tong.myweather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
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
    private TextView atom_weather = null;
    private TextView atom_temp = null;
    private Handler handler;
    private ProgressBar progressBar;

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
                    tom_temp.setText(weather_data_tom.getString("temperature"));
                    atom_temp.setText(weather_data_atom.getString("temperature"));
                    atom_weather.setText(weather_data_atom.getString("date"));

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
        cur_temp = (TextView)findViewById(R.id.cur_temp);
        cur_weather = (TextView)findViewById(R.id.cur_weather);
        today_temp = (TextView)findViewById(R.id.today_temp);
        tom_temp = (TextView)findViewById(R.id.tom_temp);
        atom_weather = (TextView)findViewById(R.id.atom_weather);
        atom_temp = (TextView)findViewById(R.id.atom_temp);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
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
                String cur_city = editText.getText().toString();
                MyHandler myhandler = new MyHandler();
                MyThread myThread = new MyThread(myhandler,MainActivity.this);
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
