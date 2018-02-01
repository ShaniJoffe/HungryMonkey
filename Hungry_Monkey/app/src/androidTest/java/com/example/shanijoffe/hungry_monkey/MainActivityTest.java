package com.example.shanijoffe.hungry_monkey;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by Shani Joffe on 1/29/2018.
 */
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule= new ActivityTestRule<MainActivity>(MainActivity.class);
    private  MainActivity mActiviy =null;


    @Before
    public void setUp() throws Exception {
        mActiviy=mActivityTestRule.getActivity();
    }
    @Test
    public void testLunch() throws IOException {

        HttpURLConnection conn = null;
        View view=mActiviy.findViewById( R.id.DishSearch );
        View view2=mActiviy.findViewById( R.id.welcome_txt );
        View view3=mActiviy.findViewById( R.id.fragment_container );
        //check the components
        assertNotNull( view );
        assertNotNull( view2 );
        assertNotNull( view3 );
        //check connection
        URL url2 = new URL( "http://hungrymonkey-env.vivacrpttt.eu-central-1.elasticbeanstalk.com/api/v1/basicSearch" );
        conn = (HttpURLConnection) url2.openConnection();
        int responseCode = conn.getResponseCode();
        assertTrue( responseCode!=200 );
    }
    @After
    public void tearDown() throws Exception {
        mActiviy=null;
    }

}