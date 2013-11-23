package com.example.pervasiveapp;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

public class Reader extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reader);
		String fileContents = getIntent().getStringExtra("KEY_FileContent");
		Log.i("PervasiveApp", "File Contents: "+"\n"+fileContents);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reader, menu);
		return true;
	}

}
