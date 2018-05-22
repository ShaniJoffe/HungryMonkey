package com.example.shanijoffe.hungry_monkey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class hungryMonkey extends AppCompatActivity {
    TextView tv;
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash_screen1 );
        tv=(TextView)findViewById( R.id.tv_splash ) ;
        iv=(ImageView)findViewById( R.id.iv_splash ) ;
        Animation myanim= AnimationUtils.loadAnimation( this,R.anim.mytransition );
        tv.startAnimation(myanim);
        iv.startAnimation(myanim);
        final Intent i =new Intent(this,navigation_HM.class);
        Thread timer =new Thread(  )
        {
            public void run()
            {
                try {
                    sleep( 2000 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();

    }
}



