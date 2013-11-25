package com.example.pervasiveapp;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import 	java.lang.reflect.Field;

public class BookReader extends Activity {
	ListView bookList;
	Context context;
	
	boolean mExternalStorageAvailable = false;
	boolean mExternalStorageWriteable = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_reader);
		this.context = getApplicationContext();
		bookList = (ListView)findViewById(R.id.booklistView);
		checkExternalStorage();
		if(mExternalStorageAvailable){
			populateBookList();
			bookList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position,
						long id) {
					// Book name
					String  fileName = (String) bookList.getItemAtPosition(position);
					FileHandler.setFileName(fileName);
					FileHandler.setContext(context);
					final String fileContents = FileHandler.readFile();
	
					Thread reader = new Thread(){
						public void run(){
							try{
								Intent readerIntent = new Intent("com.example.pervasiveapp.Reader").putExtra("KEY_FileContent",fileContents);
								startActivity(readerIntent);
							}catch(Exception e){
								
							}
							finally{
								finish();
							}
						}
					};
					reader.start();
				}
			});
		}else{
			//show messagebox that storage is not accessible.
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.book_reader, menu);
		return true;
	}

	public void populateBookList(){
//		final Field[] fields = R.raw.class.getFields();
//		final ArrayList<String> list = new ArrayList<String>();
//
//		for(Field f: fields){
//			list.add(f.getName());
//		}
//		final BookListAdapter bookListAdapter = new BookListAdapter(this,android.R.layout.simple_list_item_1 , list);
//		bookList.setAdapter(bookListAdapter);
		File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		String [] files = new String[100];
		if(downloadsDir!=null){
			files = downloadsDir.list();
			final ArrayList<String> fileList = new ArrayList<String>();
					for(int i=0;i<files.length;i++){
						
						fileList.add(files[i]);
					}
					final BookListAdapter bookListAdapter = new BookListAdapter(this,android.R.layout.simple_list_item_1 , fileList);
					bookList.setAdapter(bookListAdapter);
		}
		

	}
	
	public void checkExternalStorage(){
		
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		
	}

	private class BookListAdapter extends ArrayAdapter<String>{

		public BookListAdapter(Context context, int resource, List<String> objects) {
			super(context, resource, objects);
		}


	}

}
