package com.example.shanijoffe.hungry_monkey;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class signUpPage extends AppCompatActivity
{

    EditText username;
    EditText password;
    EditText c_password;
    EditText email;
    Button btn_signUp1;
    JSONObject jObj;
    String token=null,userName_res="",passrowd_res="",status=null,userEmail;
    String line="";
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        ///
        setContentView( R.layout.activity_sign_up );
        username=findViewById( R.id.user_name_et );
        email=findViewById( R.id.user_email_et );
        password=findViewById( R.id.et_password );
        c_password=findViewById( R.id.confirmPass );

        btn_signUp1=(Button)findViewById( R.id.btn_signUp );

        ///defining the setting of  SharedPreferences.
        settings=getSharedPreferences( "myPrefsFile",MODE_PRIVATE  );
        editor=settings.edit();

        //signUp to the app
        btn_signUp1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName_res=username.getText().toString();
                passrowd_res=password.getText().toString();
                userEmail=email.getText().toString();
                if(!password.getText().toString().equals( c_password.getText().toString()))
                {
                    printOutMessage("נא אמת ססמא","שגיאת התחברות");

                //do things if these 2 are correct.
                    Toast.makeText(getApplicationContext(), "please confirm your password  :" ,
                            Toast.LENGTH_LONG).show();
                }
                else if(userName_res.equals( ""))
                {
                    printOutMessage("נא אמת ססמא","שגיאת התחברות");
                    Toast.makeText(getApplicationContext(), "please confirm your password  :" ,
                            Toast.LENGTH_LONG).show();
                }
                else if(userEmail.equals(""))
                {
                    printOutMessage("נא הכנס מייל","שגיאת התחברות");
                    Toast.makeText(getApplicationContext(), "please enter Email  :" ,
                            Toast.LENGTH_LONG).show();
                }
                else if(!isEmailValid(userEmail))
                {
                    Toast.makeText(getApplicationContext(), "please enter  Valid Email  " ,
                            Toast.LENGTH_LONG).show();
                }
                else if(isValidPassword(passrowd_res)) {
                    {
                        Toast.makeText(getApplicationContext(), "please enter  Valid Password  " ,
                                Toast.LENGTH_LONG).show();
                    }
                }

                else
                {

                    Toast.makeText(getApplicationContext(), "התחברת בהצלחה!! :) "+ userName_res + " " +passrowd_res + " " +userEmail,
                            Toast.LENGTH_LONG).show();
                    new SendPostRequest().execute();//registering to server .

                }
            }
        } );

    }
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }
    public void printOutMessage(String myMsg,String msgTitle)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(signUpPage.this);

        // set title
        alertDialogBuilder.setTitle(msgTitle);

        // set dialog message
        alertDialogBuilder
                .setMessage(myMsg)
                .setCancelable(false)
                .setPositiveButton("תודה", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity

                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public class SendPostRequest extends AsyncTask<String, Void, String>
    {
        AlertDialog alertDialog;
        String status=null;
        protected void onPreExecute(){
            alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
            alertDialog.setTitle("Sign up...");
        }
        protected String doInBackground(String... arg0)
        {
            Log.i("in onClickSignUp","heyoo");
            try
            {
                Log.i("in onClickSignUp","heyoo");
                URL url = new URL("http://newapp-env.eiymf2wfdn.eu-central-1.elasticbeanstalk.com/api/v1/users"); // here is your URL path
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("name",username.getText().toString());
                postDataParams.put("password",passrowd_res);
                postDataParams.put("email",userEmail);
                Log.i("sign up - sending json", String.valueOf(postDataParams));
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


             if(result.equals("false")){
                 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(signUpPage.this);

                 // set title
                 alertDialogBuilder.setTitle(" שגיאת התחברות !");

                 // set dialog message
                 alertDialogBuilder
                         .setMessage("עלייך להכניס ססמא ואימות ססמא נכונים")
                         .setCancelable(false)
                         .setPositiveButton("תודה", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int id) {
                                 // if this button is clicked, close
                                 // current activity

                             }
                         });


                 // create alert dialog
                 AlertDialog alertDialog = alertDialogBuilder.create();
//
//                 // show it
                alertDialog.show();
//                 Toast.makeText(getApplicationContext(), "wrong password/username :" ,
//                         Toast.LENGTH_LONG).show();

             }
             else{
                  // msg you get from success like "Login Success"

                     Intent i = new Intent(getApplicationContext(),LoginPage.class);
                     startActivity(i);

             }
        }

        private void setData(String data) throws JSONException {

            jObj = new JSONObject(data);
           // token=jObj.getString("token");
            userName_res = jObj.getString("name");
            status = jObj.getString("status");
            Toast.makeText(getApplicationContext(), " welcome ! :" +userName_res  ,
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
