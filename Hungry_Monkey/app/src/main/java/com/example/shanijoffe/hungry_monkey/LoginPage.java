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
import java.net.URLDecoder;
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
    String userName_res="",token="";
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

    public void OnClick(View view) throws JSONException, ParseException, UnsupportedEncodingException {

        view.setEnabled( false );
        JSONParser parser_obj = new JSONParser();
        log.i( "hi", "in OnClick" );
        user = user_name_edit.getText().toString();
        user_pass = password_edit.getText().toString();
          new SendPostRequest().execute();
        Log.i( "response from server :", "name is "+userName_res+"token is "+token );
          if (line!=" " && userName_res !="" && token !="" )
          {

            Log.i( "response from server :", "name is "+userName_res+"token is "+token );
            Bundle user_det = new Bundle();
            user_det.putString("user_name",userName_res);//here i shpuld insert user_name
            Intent i = new Intent( this, MainActivity.class );
            i.putExtras(user_det);
            startActivityForResult( i, CODE_REQ );

          }

    }


    public class SendPostRequest extends AsyncTask<String, Void, String>
    {
        protected void onPreExecute(){}
        protected String doInBackground(String... arg0)
        {
            try
            {
                log.i("in doInBackground ","1");
                URL url = new URL("http://hungrymonkey-env.vivacrpttt.eu-central-1.elasticbeanstalk.com/api/v1/auth"); // here is your URL path
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("name",user);
                postDataParams.put("password",user_pass);
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
                    Log.i("hey dan","hey shan");
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
            Log.i("usrn",result);
            try {
                JSONObject jObj = new JSONObject(result);
                userName_res = jObj.getString("message");
                token=jObj.getString("token");
                Log.i("userName_res)",userName_res);

                Log.i( "response from server :", "name is "+userName_res+"token is "+token );
                Bundle user_det = new Bundle();
                user_det.putString("user_name",userName_res);//here i shpuld insert user_name
                Intent i = new Intent( getApplicationContext(), MainActivity.class );
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
