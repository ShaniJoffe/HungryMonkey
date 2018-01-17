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

import java.util.HashMap;
import java.util.Vector;

public class FirstFragment extends Fragment {

    String[] teams={""};

    View view;
    Button firstButton;
    TextView nres;
    Button det;
    String myValue;
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
        Log.i("vector:", String.valueOf( vector ) );
        custom_adapter aa=new custom_adapter(getContext(),R.layout.single_dish_item,vector);
        listView.setAdapter(aa);
        aa.notifyDataSetChanged();
    }
}