package com.example.shanijoffe.hungry_monkey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static com.loopj.android.http.AsyncHttpClient.log;

public class LoginPage extends AppCompatActivity {

    EditText user_email_edit;
    EditText password_edit;
    Button loginButton;
    String user_email="";
    String user_pass="";
    static final int CODE_REQ=1;// for intent
    String line="";
    SharedPreferences preferences;
    String userName_res="",token=null;
    private static final String TAG = "MyActivity";
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    String user2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        ///
        setContentView( R.layout.activity_login_page );
        user_email_edit=(EditText)findViewById(R.id.txtv_email);
        password_edit=(EditText)findViewById(R.id.txt_password);
        loginButton=(Button)findViewById(R.id.btn_login);

        ////

        settings =getSharedPreferences( "myPrefsFile",MODE_PRIVATE );
        editor=settings.edit();

        //empty the fields.
        user_email_edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) { user_email_edit.setHint( "" );
            }
        });
        password_edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password_edit.setHint( "" );

            }
        } );


        log.i("params:",user_email + user_pass);
        Log.i(TAG,"before connection");


    }

    //login button
    public void OnClickLogin(View view) throws JSONException, ParseException, UnsupportedEncodingException {

        settings =getSharedPreferences( "myPrefsFile",MODE_PRIVATE );

        //  basic validation .
        if(( user_email_edit.getText().toString().trim().length() >0)&&(password_edit.getText().toString().trim().length() >0))
        {

            user_email = user_email_edit.getText().toString();
            user_pass = password_edit.getText().toString();

            //execute login -authentication to server.
            new SendPostRequest().execute();//authentication to server .

            //validate the response.
            Log.i( "response from server :", "name is "+userName_res+"token is "+token );


            if (line!=" " && userName_res !="" && token !="" )
            {
                Bundle user_det = new Bundle();
                user_det.putString("user_name",userName_res);   //putting the username to send to mainActivity.
                Log.i("token is",token);
                Intent i = new Intent( this, navigation_HM.class );
                i.putExtras(user_det);
                startActivityForResult( i, CODE_REQ );
            }
        }
        else
        {
            Log.i("dghbchd","sote");
            Toast.makeText(getApplicationContext(), "please fill All fileds   :" ,
                    Toast.LENGTH_LONG).show();

        }
    }
    public class SendPostRequest extends AsyncTask<String, Void, String>
    {
        protected void onPreExecute(){}
        protected String doInBackground(String... arg0)
        {
            try
            {
                URL url = new URL("http://newapp-env.eiymf2wfdn.eu-central-1.elasticbeanstalk.com/api/v1/auth"); // here is your URL path
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email",user_email);
                postDataParams.put("password",user_pass);
                Log.i("login -what im sending ", String.valueOf(postDataParams));
                //POST
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(50000 /* milliseconds */);
                conn.setConnectTimeout(50000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));//sending the json object to server after encoding .
                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();
                Log.i("responseCode login", String.valueOf( responseCode ) );
                if (responseCode == HttpsURLConnection.HTTP_OK)
                {

                    BufferedReader in=new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");

                    while((line = in.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                }
                else
                {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }
        @Override
        protected void onPostExecute(String result) {
            Log.i("login res is:",result);
            try {
                //handle the response .
                JSONObject jObj = new JSONObject(result);
                userName_res = jObj.getString("message");
                token=jObj.getString("token");

                Log.i( "response from server :", "name is "+userName_res+"token is "+token );

                Bundle user_det = new Bundle();
                user_det.putString("user_name",userName_res);//save username to send to different activity's.
                editor.putString( "user_token",token ).apply(); //save our token SP.
                editor.commit();
                //send username to main activity and open it.
                Intent i = new Intent( getApplicationContext(), navigation_HM.class );
                i.putExtras(user_det);
                startActivityForResult( i, CODE_REQ );

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), "userName_res :" + userName_res,
                    Toast.LENGTH_LONG).show();
        }
    }
    public String getPostDataString(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);
            if (first)
                first = false;
            else
                result.append("&");
            result.append( URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
}
