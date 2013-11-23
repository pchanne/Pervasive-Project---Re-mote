package com.example.pervasiveapp;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
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

import 	java.lang.reflect.Field;

public class BookReader extends Activity {
	ListView bookList;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_reader);
		this.context = getApplicationContext();
		bookList = (ListView)findViewById(R.id.booklistView);
		populateBookList();
		bookList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				// Book name
				String  fileName = (String) bookList.getItemAtPosition(position);
				FileHandler.setFileName(fileName);
				FileHandler.setContext(context);
				final String fileContents = FileHandler.readFile(0);

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.book_reader, menu);
		return true;
	}

	public void populateBookList(){
		final Field[] fields = R.raw.class.getFields();
		final ArrayList<String> list = new ArrayList<String>();

		for(Field f: fields){
			list.add(f.getName());
		}
		final BookListAdapter bookListAdapter = new BookListAdapter(this,android.R.layout.simple_list_item_1 , list);
		bookList.setAdapter(bookListAdapter);

	}

	private class BookListAdapter extends ArrayAdapter<String>{

		public BookListAdapter(Context context, int resource, List<String> objects) {
			super(context, resource, objects);

			// TODO Auto-generated constructor stub
		}


	}

}
