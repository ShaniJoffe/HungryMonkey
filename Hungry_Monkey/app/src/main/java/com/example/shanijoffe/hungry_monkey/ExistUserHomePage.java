//package com.example.shanijoffe.hungry_monkey;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.database.Cursor;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.SimpleCursorAdapter;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//public class ExistUserHomePage extends AppCompatActivity implements View.OnClickListener  {
//
//
//    Spinner spinner;
//    ArrayAdapter<CharSequence> adapter;
//    Button Open_Advanced_Search_Btn;
//    static final int CODE_REQ=1;
//    ListView lvDishTypeList;
//    EditText nameDishT;
//    EditText numDishT;
//    AssignmentsDbHelper         dbHandler;
//    SimpleCursorAdapter simpleCursorAdapter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_exist_user_home);
//
//        spinner=(Spinner) findViewById( R.id.sp_kosher );
//        adapter= ArrayAdapter.createFromResource( this,R.array.planets_array ,android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource( android.R.layout.simple_spinner_item );
//        spinner.setAdapter( adapter );
//        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.e("MainActivity","fuka u ");
//                Toast.makeText( getBaseContext(),adapterView.getItemAtPosition(i)+"isSalected",Toast.LENGTH_SHORT ).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        } );
////        Open_Advanced_Search_Btn=findViewById( R.id.adavanced_btn );
////        lvDishTypeList = (ListView) findViewById(R.id.productList);
////        nameDishT=findViewById( R.id.nameDT );
////        numDishT=findViewById( R.id.numDT );
////        dbHandler = new AssignmentsDbHelper(this);
//
//    }
//
//    public void OnClickShowAdvancedSearch(View view)
//    {
//        View v1 = findViewById(R.id.txtv_dis);
//        View v2 = findViewById(R.id.txtv_price);
//        View v3 = findViewById(R.id.txtv_kosher);
//        View v4 = findViewById(R.id.sk_dis);
//        View v5 = findViewById(R.id.sk_price);
//        View v6 = findViewById(R.id.sp_kosher);
//
//        v1.setVisibility(View.VISIBLE);
//        v2.setVisibility(View.VISIBLE);
//        v3.setVisibility(View.VISIBLE);
//        v4.setVisibility(View.VISIBLE);
//        v5.setVisibility(View.VISIBLE);
//        v6.setVisibility(View.VISIBLE);
//
//    }
//    public void onClickFav(View view)
//    {
////        Intent i = new Intent(this,FavioratesResults.class);
////        String s = "חיפוש מתקדם";
////        i.putExtra("myString",s);
////        startActivityForResult(i,CODE_REQ);
//        Log.i("inonClickFav","onClickFav");
//        displayDishList();
//
//    }
//    public void onClickHis(View view)
//    {
//        Intent i = new Intent(this,HistoryResults.class);
//        String s = "חיפוש מתקדם";
//        i.putExtra("myString",s);
//        startActivityForResult(i,CODE_REQ);
//
//    }
//    public void onClickBasic(View view)
//    {
//        Intent i = new Intent(this,BasicSearchActivity.class);
//        String s = "חיפוש מתקדם";
//        i.putExtra("myString",s);
//        startActivityForResult(i,CODE_REQ);
//
//    }
//
//    @Override
//    public void onClick(View view) {
//
//    }
//
//    public void newDishItem(View view)
//    {
//        try {
//            DishType p = new DishType( Integer.parseInt(numDishT.getText().toString()),nameDishT.getText().toString());
//            dbHandler.addProduct(p);
//            Toast.makeText(getApplicationContext(), "dish type added",
//                    Toast.LENGTH_LONG).show();
//            numDishT.setText("");
//            nameDishT.setText("");
//            displayDishList();
//        } catch (Exception e) {
//            numDishT.setText("Unable to add.\nTry again.");
//        }
//    }
//    @SuppressLint("LongLogTag")
//    private void displayDishList()
//    {
//        Log.i("indisplayDishList","displayDishList");
//        try
//        {
//
//            Cursor cursor = dbHandler.getAllProducts();
//            cursor.moveToFirst();
//            while (cursor.moveToNext())
//                Log.i("line", cursor.getInt(0) + ", " +"num" + cursor.getString(1)+ "name " +cursor.getString(2));
//            Log.i("im here indisplayDishList","indisplayDishList");
//            if (cursor == null)
//            {
//                numDishT.setText("Unable to generate cursor.");
//                return;
//            }
//            if (cursor.getCount() == 0)
//            {
//                numDishT.setText("No Products in the Database.");
//                return;
//            }
//            String[] columns = new String[] {
//                    AssignmentsDbHelper.COLUMN_ID,
//                    AssignmentsDbHelper.DISH_TYPE_NAME,
//
//            };
//            int[] boundTo = new int[] {
//                    R.id.pId,
//                    R.id.pName,
//
//            };
//            simpleCursorAdapter = new SimpleCursorAdapter(this,
//                    R.layout.single_dish_item,
//                    cursor,
//                    columns,
//                    boundTo,
//                    0);
//            lvDishTypeList.setAdapter(simpleCursorAdapter);
//        }
//        catch (Exception ex)
//        {
//            Log.i("indisplayDishList","displayDishList");
//            numDishT.setText("There was an error!");
//        }
//    }
//}
//
//
//
////
////        AsyncTask.execute(new Runnable() {
////                    @Override
////                    public void run() {
////                        // All your networking logic
////                        // should be here
////
////                        // Create URL
////                        URL githubEndpoint = null;
////                        try {
////                            githubEndpoint = new URL("https://rocky-thicket-82184.herokuapp.com/");
////                        } catch (MalformedURLException e) {
////                            e.printStackTrace();
////                        }
////
////// Create connection
////                        try {
////                            HttpsURLConnection myConnection = (HttpsURLConnection) githubEndpoint.openConnection();
////                            myConnection.setRequestProperty("User-Agent", "my-rest-app-v0.1");
////                            if (myConnection.getResponseCode() == 200) {
////                                Log.i(TAG,"success connection");
////                                // Success
////                                // Further processing here
////                            } else {
////                                // Error handling code goes here
////                            }
////                            InputStream responseBody = myConnection.getInputStream();
////                            InputStreamReader responseBodyReader =
////                                    new InputStreamReader(responseBody, "UTF-8");
////                            JsonReader jsonReader = new JsonReader(responseBodyReader);
////
////                            //POST
////
//////                            URL httpbinEndpoint = new URL("https://httpbin.org/post");
//////                            HttpsURLConnection myConnection
//////                                    = (HttpsURLConnection) httpbinEndpoint.openConnection();
//////
//////                            myConnection.setRequestMethod("POST");
//////                            // Create the data
//////                            String myData = "message=Hello";
//////
//////// Enable writing
//////                            myConnection.setDoOutput(true);
//////
//////// Write the data
//////                            myConnection.getOutputStream().write(myData.getBytes());
////
////
////                        }
////                        catch (IOException e) {
////                            e.printStackTrace();
////                        }
////
////                    }
////                });//AsyncHttpClient client=new AsyncHttpClient();
////        client.get("https://rocky-thicket-82184.herokuapp.com/", new AsyncHttpResponseHandler() {
////            @Override
////            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
////                if(responseBody!=null)
////                {
////                    user_name_edit.setText(new String(responseBody));
////                    JSONObject postDataParams = new JSONObject();
////                    try {
////                        postDataParams.put("username", user);
////
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////                    try {
////                        postDataParams.put("password", user_pass);
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////                    Log.e("params",postDataParams.toString());
////
////                }
////                view.setEnabled(true );
////
////            }
////
////            @Override
////            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
////
////            }
////        });