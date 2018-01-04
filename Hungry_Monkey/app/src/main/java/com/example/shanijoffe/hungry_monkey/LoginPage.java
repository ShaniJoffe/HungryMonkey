package com.example.shanijoffe.hungry_monkey;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import static com.loopj.android.http.AsyncHttpClient.log;

public class LoginPage extends AppCompatActivity {



    EditText user_name_edit;
    EditText password_edit;
    Button loginButton;
    String user="";
    String user_pass="";
    static final int CODE_REQ=1;
    String line="";
    private static final String TAG = "MyActivity";
    String user2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login_page );
        user_name_edit=(EditText)findViewById(R.id.txt_usrname);
        password_edit=(EditText)findViewById(R.id.txt_password);
        loginButton=(Button)findViewById(R.id.btn_login);

        log.i("params:",user + user_pass);
        Log.i(TAG,"before connection");
    }

    public void OnClick(View view)
    {
        view.setEnabled( false );

        log.i( "hi", "in OnClick" );
        user = user_name_edit.getText().toString();
        user_pass = password_edit.getText().toString();
        new SendPostRequest().execute();
//        if (line == line)
//        {
//            Intent i = new Intent( this, ExistUserHomePage.class );
//            String s = "משתשמש קיים  ";
//            i.putExtra( "myString", s );
//            startActivityForResult( i, CODE_REQ );
//        }
    }


    public class SendPostRequest extends AsyncTask<String, Void, String>
    {
        protected void onPreExecute(){}
        protected String doInBackground(String... arg0)
        {
            try
            {
                log.i("in doInBackground ","1");
                URL url = new URL("https://rocky-thicket-82184.herokuapp.com/api/v1/auth"); // here is your URL path
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("name",user);
                postDataParams.put("pass",user_pass);
                Log.e("params",postDataParams.toString());

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
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();
                Log.i("responseCode@:", String.valueOf( responseCode ) );

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null)
                    {
                        Log.i("line:",line);

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
            Toast.makeText(getApplicationContext(), result,
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
