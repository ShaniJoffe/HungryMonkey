package com.example.shanijoffe.hungry_monkey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.loopj.android.http.AsyncHttpClient.log;

public class basic_results extends AppCompatActivity {


    ListView lv;
    JSONObject job;
    String Jsonoutput;
    String val;
    TextView PriceDish;
    TextView Dish_name;
    TextView nameRes;
    ArrayAdapter<String> adapter;
    protected void onCreate(Bundle savedInstanceState) {
        String[] teams={"Man Utd","Man City","Chelsea","Arsenal","Liverpool","Totenham","Man Utd","Man City","Chelsea","Arsenal","Liverpool","Totenham"};
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_basic_results );
        //connecting the UI
        nameRes=findViewById( R.id.nameRestxtv );
        Log.i("sup","in basic");
        lv=(ListView) findViewById(R.id.listView1);
        //connecting  adapter
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,teams);
        lv.setAdapter(adapter);
        if(getIntent().hasExtra("JSON_OBJECT"))
        {
            JsonObject mJsonObject;
            Intent i =getIntent();
             val = i.getStringExtra("JSON_OBJECT");
        }

//now parse this job to get your name and email.or anuy other data in jobj.

        log.i("my json :",val);
       // nameRes.setText( "heyooo" );

    }


}
