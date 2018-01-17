package com.example.shanijoffe.hungry_monkey;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SecondFragment extends Fragment {

    View view;
    private String _name;
    private String _phone;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_second, container, false);
// get the reference of Button
        if(getArguments() == null )
        {
            _name = "Sample name";
            _phone = "05005050505050505";
        }
        else
        {
            _name = new String(getArguments().getString("name"));
            //_phone = new String(getArguments().getString("phone"));
        }
        TextView txtview = (TextView) view.findViewById(R.id.txtview);
        txtview.setText("Name: " + this._name + " Number: " + this._phone);
        return view;
    }


}