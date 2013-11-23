package com.example.pervasiveapp;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Reader extends Activity {

	TextView textReader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reader);
		try{
			String fileContents = getIntent().getStringExtra("KEY_FileContent");
			Log.i("PervasiveApp", "File Contents: "+"\n"+fileContents);
			textReader = (TextView)findViewById(R.id.ReaderTextView);
			if(textReader!=null){
				textReader.setText(fileContents);
			}
		}catch(Exception e){
			 Toast.makeText(getApplicationContext(),
                     "Error: "+e.getMessage(), Toast.LENGTH_LONG)
                     .show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reader, menu);
		return true;
	}

}
