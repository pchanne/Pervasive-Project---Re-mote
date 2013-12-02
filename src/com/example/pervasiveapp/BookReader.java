package com.example.pervasiveapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import 	java.lang.reflect.Field;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.prediction.Prediction;
import com.google.api.services.prediction.PredictionScopes;
import com.google.api.services.prediction.model.Input;
import com.google.api.services.prediction.model.Output;
import com.google.api.services.prediction.model.Input.InputInput;

public class BookReader extends Activity {
	ListView bookList;
	Context context;
	
	boolean mExternalStorageAvailable = false;
	boolean mExternalStorageWriteable = false;
	
	static final int REQUEST_ACCOUNT_PICKER = 1;
	static final int REQUEST_AUTHORIZATION = 2;
	private static final String APPLICATION_NAME = "PredictMySentiment";

	static Prediction predictionService;
	private GoogleAccountCredential credential;
	
	
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
					FileHandler.setRandomfile();
					final String fileContents = FileHandler.read();
					Thread reader = new Thread(){
						public void run(){
							try{
								Intent readerIntent = new Intent("com.example.pervasiveapp.Reader").putExtra("KEY_FileContent",fileContents);
								startActivity(readerIntent);
							}catch(Exception e){
								Utils.setContext(context);
								Utils.showOKMessageBox("Exception in starting Reader", e.getMessage());
							}
							finally{
								finish();
							}
						}
					};
					reader.start();
				}
			});
			//credential = GoogleAccountCredential.usingOAuth2(this,
			//		Arrays.asList(PredictionScopes.PREDICTION));
			//startActivityForResult(credential.newChooseAccountIntent(),
			//		REQUEST_ACCOUNT_PICKER);
		}else{
			//show messagebox that storage is not accessible.
			Utils.setContext(context);
			Utils.showOKMessageBox("Storage not accessible!", "Please ensure that the app has permissions to access the external storage");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.book_reader, menu);
		return true;
	}

	public void populateBookList(){
		File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		String [] files = new String[100];
		if(downloadsDir!=null){
			files = downloadsDir.list();
			final ArrayList<String> fileList = new ArrayList<String>();
					for(int i=0;i<files.length;i++){
						if(files[i].contains(".txt"))
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
	
	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		switch (requestCode) {
		case REQUEST_ACCOUNT_PICKER:
			if (resultCode == RESULT_OK && data != null
					&& data.getExtras() != null) {
				String accountName = data
						.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				if (accountName != null) {
					credential.setSelectedAccountName(accountName);
					predictionService = getPredictionService(credential);
					getPredictionAccounts();
				}
			}

			break;
		case REQUEST_AUTHORIZATION:
			if (resultCode == Activity.RESULT_OK) {
				getPredictionAccounts();
			} else {
				startActivityForResult(credential.newChooseAccountIntent(),
						REQUEST_ACCOUNT_PICKER);
			}
		}
	}

	private Prediction getPredictionService(GoogleAccountCredential credential) {
		return new Prediction.Builder(AndroidHttp.newCompatibleTransport(),
				new GsonFactory(), credential).setApplicationName(
				APPLICATION_NAME).build();
	}
	
	private void getPredictionAccounts() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					predict(predictionService, "Is this sentence in English?");

					predict(predictionService, "¿Es esta frase en Español?");
					predict(predictionService,
							"Est-ce cette phrase en Français?");
				} catch (UserRecoverableAuthIOException e) {
					startActivityForResult(e.getIntent(), BookReader.REQUEST_AUTHORIZATION);
				} catch (IOException e) {
					Log.e(e.toString(), e.getMessage());
				}
			}
		});
		t.start();
	}

	private static void predict(Prediction prediction, String text)
			throws IOException {
		Log.e("predict", "calling prediction service");
		Input input = new Input();
		InputInput inputInput = new InputInput();
		inputInput.setCsvInstance(Collections.<Object> singletonList(text));
		input.setInput(inputInput);
		Output output = prediction.trainedmodels()
				.predict("63446654424", "textidentifier", input).execute();
		Log.e("Text: ", text);
		Log.e("Predicted language: ", output.getOutputLabel().toString());
	}
	

}

