package com.example.shanijoffe.hungry_monkey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText c_password;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sign_up );
        username=findViewById( R.id.user_name_et );
        password=findViewById( R.id.user_name_et );
        c_password=findViewById( R.id.user_name_et );
        email=findViewById( R.id.user_name_et );




    }

    public void onClick(View view)
    {

    }
}
