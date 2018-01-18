package com.example.shanijoffe.hungry_monkey;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splashScreen extends AppCompatActivity {
    TextView tv;
    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        this.requestWindowFeature( Window.FEATURE_ACTION_BAR);
        this.requestWindowFeature( Window.FEATURE_NO_TITLE);
        setContentView( R.layout.activity_splash_screen );

        tv=(TextView)findViewById( R.id.tv_splash ) ;
        iv=(ImageView)findViewById( R.id.iv_splash ) ;
        Animation myanim= AnimationUtils.loadAnimation( this,R.anim.mytransition );
        tv.startAnimation(myanim);
        iv.startAnimation(myanim);

    }

}
