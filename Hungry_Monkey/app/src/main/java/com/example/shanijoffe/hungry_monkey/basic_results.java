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
    String val;
    TextView dish_price_txtv,dish_name_txtv,rest_name_txtv,Kosher_txtv,rest_address_txtv,dish_description_txtv;
    ArrayAdapter<String> adapter;
    protected void onCreate(Bundle savedInstanceState) {
        String[] teams={"Man Utd","Man City","Chelsea","Arsenal","Liverpool","Totenham","Man Utd","Man City","Chelsea","Arsenal","Liverpool","Totenham"};
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_basic_results );
        //connecting the UI
        dish_price_txtv=findViewById( R.id.dish_price_txtv );
        dish_name_txtv=findViewById( R.id.dish_name_txtv );
        rest_name_txtv=findViewById( R.id.rest_name_txtv );
        Kosher_txtv=findViewById( R.id.Kosher_txtv );
        rest_address_txtv=findViewById( R.id.rest_address_txtv );
        dish_description_txtv=findViewById( R.id.dish_description_txtv );
        Log.i("sup","in basic");
       // lv=(ListView) findViewById(R.id.listView1);
        //connecting  adapter
       // adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,teams);
      //  lv.setAdapter(adapter);

        //if(getIntent().hasExtra("rest_address2"))
        //{

            Intent i =getIntent();
            String rest_address2 = i.getStringExtra("rest_address2");
            String rest_location2 = i.getStringExtra("rest_location2");
            String rest_name2 = i.getStringExtra("rest_name2");
            String Kosher2 = i.getStringExtra("Kosher2");
            String dish_name2 = i.getStringExtra("dish_name2");
            String dish_price2 = i.getStringExtra("dish_price2");
            String dish_description2 = i.getStringExtra("dish_description2");
            System.out.println("rest_address2 "+rest_address2+"\n" );
            System.out.println("rest_location2 "+rest_location2+"\n" );
            System.out.println("Kosher2 "+Kosher2+"\n" );
            System.out.println("dish_name2 "+dish_name2+"\n" );
            System.out.println("dish_price2 "+dish_price2+"\n" );
            System.out.println("dish_description2 "+dish_description2+"\n" );


      //  }
        dish_price_txtv.setText( dish_price2 +"ש''ח "+" " );
        dish_name_txtv.setText( dish_name2 );
        rest_name_txtv.setText( rest_name2 );
        if (Kosher2.equals( "true" ))
        {

            Kosher_txtv.setText("כשרות מהדרין");
            System.out.println("כשר "+Kosher2+"\n" );
        }
        else{
            Kosher_txtv.setText("אין כשרות");
            System.out.println("לא כשר "+Kosher2+"\n" );
        }

        dish_description_txtv.setText(dish_description2 +".");
        rest_address_txtv.setText(rest_address2);


    }


}

