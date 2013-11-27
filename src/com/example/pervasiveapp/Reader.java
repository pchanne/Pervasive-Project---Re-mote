package com.example.pervasiveapp;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Reader extends Activity {

	TextView textReader;
	Button previousButton;
	Button nextButton;
	String fileText;
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
			 Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(), Toast.LENGTH_LONG)
                     .show();
		}
		
		addPreviousButtonListener();
		addNextButtonListener();
	}

	private void addNextButtonListener() {
		
		nextButton = (Button) findViewById(R.id.buttonNext);
		 
		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fileText = FileHandler.readForward();
				if(fileText!=null){
					textReader.setText(fileText);
				}else{
					Utils.setContext(Reader.this);
					Utils.showOKMessageBox("Cannot Navigate to Next Page", "You have reached the last page.");
				}
			}
			
 
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reader, menu);
		return true;
	}
	
	public void addPreviousButtonListener() {
		 
		previousButton = (Button) findViewById(R.id.buttonPrev);
 
		previousButton.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				fileText = FileHandler.readBackward();
				if(fileText!=null){
					textReader.setText(fileText);
				}else{
					Utils.setContext(Reader.this);
					Utils.showOKMessageBox("Cannot Navigate to Previous Page", "You have reached the first page.");
				}
			}
 
		});
 
	}

}
