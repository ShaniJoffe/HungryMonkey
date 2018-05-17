package com.example.shanijoffe.hungry_monkey;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class SignUpActivity extends AppCompatActivity
{

    EditText username;
    EditText password;
    EditText c_password;
    EditText email;
    Button btn_signUp1;
    JSONObject jObj;
    String token=null,userName_res,status=null;
    String line="";
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        ///
        setContentView( R.layout.activity_sign_up );
        username=findViewById( R.id.user_name_et );
        password=findViewById( R.id.user_name_et );
        c_password=findViewById( R.id.user_name_et );

        btn_signUp1=(Button)findViewById( R.id.btn_signUp );

        ///defining the setting of  SharedPreferences.
        settings=getSharedPreferences( "myPrefsFile",MODE_PRIVATE  );
        editor=settings.edit();

        //signUp to the app
        btn_signUp1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!password.getText().toString().equals( c_password.getText().toString())){
//do things if these 2 are correct.
                    Toast.makeText(getApplicationContext(), "please confirm your password  :" ,
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    new SendPostRequest().execute();//registering to server .
                }
            }
        } );

    }

    public void goToSu(View view) {
        goToUrl ( "m.http://newapp-env.eiymf2wfdn.eu-central-1.elasticbeanstalk.com/api/v1/viewMenu/12");
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
    public class SendPostRequest extends AsyncTask<String, Void, String>
    {
        AlertDialog alertDialog;
        String status=null;
        protected void onPreExecute(){
            alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
            alertDialog.setTitle("Login Information....");
        }
        protected String doInBackground(String... arg0)
        {
            Log.i("in onClickSignUp","heyoo");
            try
            {
                Log.i("in onClickSignUp","heyoo");
                URL url = new URL("http://newapp-env.eiymf2wfdn.eu-central-1.elasticbeanstalk.com/api/v1/users"); // here is your URL path
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("name",username);
                postDataParams.put("password",password);
             //   postDataParams.put("id","1");

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
                Log.i("response code", String.valueOf( responseCode ) );

                if (responseCode == HttpsURLConnection.HTTP_OK)
                {
                    BufferedReader in=new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    Log.i("in signUp the obj is",postDataParams.toString());
                    while((line = in.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    Log.i("res is ", sb.toString());
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
            Log.i("usrn",result);

            try {
                setData( result );
            } catch (JSONException e) {
                e.printStackTrace();
            }

             if(result.contains("ok")) // msg you get from success like "Login Success"
            {
                Intent i = new Intent(getApplicationContext(),navigation_HM.class);
                startActivity(i);
            }
            else{
                 Toast.makeText(getApplicationContext(), "wrong password/username :" ,
                         Toast.LENGTH_LONG).show();

             }
        }

        private void setData(String data) throws JSONException {

            jObj = new JSONObject(data);
            token=jObj.getString("token");
            userName_res = jObj.getString("name");
            status = jObj.getString("status");
            if(token!=null)
            {
                Log.i("in token","token is not null");
                editor.putString( "user_token",token ).apply();
            }
            Toast.makeText(getApplicationContext(), " welcome ! :" +userName_res ,
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
