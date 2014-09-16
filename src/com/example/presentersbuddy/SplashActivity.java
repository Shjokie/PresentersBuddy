package com.example.presentersbuddy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashActivity extends Activity {
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 5000;
int hesabu;
	TextView logo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		logo = (TextView) findViewById(R.id.logo);
		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/Bebas/BEBAS___.ttf");
		logo.setTypeface(face);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
				// Get the value for the run counter
				hesabu = pref.getInt("hesabu", 0);
				
				if(hesabu==0){
					SharedPreferences.Editor edito = pref.edit();
					edito.putInt("hesabu", ++hesabu);
					edito.commit(); 
					Intent i = new Intent(SplashActivity.this, HelpActivity.class);
					startActivity(i);
				}
				else{
					SharedPreferences.Editor editors = pref.edit();
					editors.putInt("hesabu", ++hesabu);
					editors.commit(); 
					Intent i = new Intent(SplashActivity.this, MainActivity.class);
					startActivity(i);
				}

				finish();
			}
		}, SPLASH_TIME_OUT);
	}

}
