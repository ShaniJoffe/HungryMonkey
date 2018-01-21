package com.example.shanijoffe.hungry_monkey;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Vector;




public class FirstFragment extends Fragment {

    String fake_data_string="{\n" +
            "    \"status\": 200,\n" +
            "    \"message\": [\n" +
            "        {\n" +
            "            \"_index\": \"hungrymonkeyrests\",\n" +
            "            \"_type\": \"restaurants\",\n" +
            "            \"_id\": \"2\",\n" +
            "            \"_score\": 0.39556286,\n" +
            "            \"_source\": {\n" +
            "                \"rest_name\": \"המסעדה של שני\",\n" +
            "                \"rest_zip\": \"666666\",\n" +
            "                \"Kosher\": true,\n" +
            "                \"rest_location\": {\n" +
            "                    \"lat\": 31.7824196,\n" +
            "                    \"lon\": 35.2970654\n" +
            "                },\n" +
            "                \"rest_address\": \"הכינור 19/4 מעלה אדומים\",\n" +
            "                \"rest_desc\": \"\",\n" +
            "                \"title\": {\n" +
            "                    \"menu\": [\n" +
            "                        {\n" +
            "                            \"dish_description\": \"ציפס מושלם בצבע ירוק\",\n" +
            "                            \"dish_id_inRest\": 2,\n" +
            "                            \"dish_name\": \"ציפס מדהייים\",\n" +
            "                            \"dish_price\": \"45\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"dish_description\": \"ציפוס לא משהו בצבע ירוק\",\n" +
            "                            \"dish_id_inRest\": 2.1,\n" +
            "                            \"dish_name\": \"ציפס ירוק \",\n" +
            "                            \"dish_price\": \"66\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            }\n" +
            "        },\n" +
            "        {\n" +
            "            \"_index\": \"hungrymonkeyrests\",\n" +
            "            \"_type\": \"restaurants\",\n" +
            "            \"_id\": \"1\",\n" +
            "            \"_score\": 0.2876821,\n" +
            "            \"_source\": {\n" +
            "                \"rest_name\": \"המסעדה של דניאל\",\n" +
            "                \"rest_zip\": \"6766\",\n" +
            "                \"Kosher\": true,\n" +
            "                \"rest_location\": {\n" +
            "                    \"lat\": 31.78300729999999,\n" +
            "                    \"lon\": 35.3103762\n" +
            "                },\n" +
            "                \"rest_address\": \"הרכס 149 מעלה אדומים\",\n" +
            "                \"rest_desc\": \"מסעדה דה בסט\",\n" +
            "                \"title\": {\n" +
            "                    \"menu\": [\n" +
            "                        {\n" +
            "                            \"dish_description\": \"טבעות בצל מעולותתתת ברוטב צילי מתוק\",\n" +
            "                            \"dish_id_inRest\": 1,\n" +
            "                            \"dish_name\": \"טבעות בצל\",\n" +
            "                            \"dish_price\": \"60\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"dish_description\": \"ציפס ברוטב חמוץ מתוק\",\n" +
            "                            \"dish_id_inRest\": 1.1,\n" +
            "                            \"dish_name\": \"ציפס חמוץ מתוק\",\n" +
            "                            \"dish_price\": \"50\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    ]\n" +
            "}";
    JSONObject fakedata;
    String[] teams={""};
    String name_res="",name_dish="",Kosher="",rest_address="",rest_desc="";
    JSONObject rest_location = new JSONObject();
    JSONObject menu = new JSONObject();
    View view;
    Button firstButton;
    TextView nres;
    Button det;
    String myValue;
    Button show_det_for_dish;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_first, container, false);

        return view;
    }
    public void onViewCreated(View view, Bundle savedInstanceState){

        Bundle bundle = this.getArguments();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        myValue = bundle.getString("name_res");
        ListView listView = (ListView) view.findViewById(R.id.list_view);

        try {
            fakedata= new JSONObject( fake_data_string );
        } catch (JSONException e) {
            e.printStackTrace();
        }
       Log.i("my fake data obj is:",fakedata.toString());
        try {
            name_res= (String) fakedata.get("message").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //temp data to test my list view
        Log.i("my fake name_res  is:",name_res);
        Vector<HashMap<String,String>> vector = new Vector<>();
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("name_res",myValue);
        hashMap.put("Dish_name","sushi");
        HashMap<String,String> hashMap2 = new HashMap<>();
        hashMap2.put("name_res",myValue);
        hashMap2.put("Dish_name","sushi");
        vector.add(hashMap);
        vector.add(hashMap2);
        HashMap<String,String> hashMap3 = new HashMap<>();
        hashMap3.put("name_res",myValue);
        hashMap3.put("Dish_name","sushi");
        vector.add(hashMap3);
        HashMap<String,String> hashMap4 = new HashMap<>();
        hashMap4.put("name_res",myValue);
        hashMap4.put("Dish_name","sushi");
        vector.add(hashMap4);
        HashMap<String,String> hashMap5 = new HashMap<>();
        hashMap5.put("name_res",myValue);
        hashMap5.put("Dish_name","sushi");
        vector.add(hashMap5);
        HashMap<String,String> hashMap6 = new HashMap<>();
        hashMap6.put("name_res",myValue);
        hashMap6.put("Dish_name","sushi");
        vector.add(hashMap6);
        Log.i("vector:", String.valueOf( vector));
        custom_adapter aa=new custom_adapter(getContext(),R.layout.single_dish_item,vector);
        listView.setAdapter(aa);
        aa.notifyDataSetChanged();


    }


}