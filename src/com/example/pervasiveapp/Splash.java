package com.example.pervasiveapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		Thread splashTimer = new Thread(){
			public void run(){
				try{
					sleep(3000);
					Intent fileHandlerIntent = new Intent("com.example.pervasiveapp.FileHandler");
					startActivity(fileHandlerIntent);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				finally{
					finish();
				}
			}
		};
		
		splashTimer.start();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

}
