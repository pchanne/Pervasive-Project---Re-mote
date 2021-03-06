package com.example.pervasiveapp;

import java.io.IOException;

import com.example.prediction.PredictionHandler;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Reader extends Activity {

	TextView textReader;
	Button previousButton;
	Button nextButton;
	String fileText;
	public MusicPlayer player;
	private static String bookName;
	private ProgressBar progressBar;
	
	
   public static String getBookName() {
		return bookName;
	}

	public static void setBookName(String bookName) {
		Reader.bookName = bookName;
	}
	
	/*----------ARDUINO SPECIFIC MEMBERS ------
	 ------------------------------------------
	 */


private int Ard_data1 = 0;
   private int Ard_data2 = 0;
   Server server = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reader);
		try{
			progressBar = (ProgressBar) findViewById(R.id.progressBar1);
			
			String fileContents = getIntent().getStringExtra("KEY_FileContent");
			Log.i("PervasiveApp", "File Contents: "+"\n"+fileContents);
			textReader = (TextView)findViewById(R.id.ReaderTextView);
			setTitle(bookName);
			if(textReader!=null){
				textReader.setText(fileContents);
				String mood= null;
				mood = PredictionHandler.getInstance().predict(fileContents);
				player = new MusicPlayer();
				player.setContext(getApplicationContext());
				if(mood!=null && !mood.equalsIgnoreCase("neutral")){
						setProgressBar(mood);
						player.setMood(mood);
						player.doInBackground((Void[])null);
				}
					
					
			}
				
				//@TODO: send text to prediction algo, and set mood in the MusicPlayer class;
				
				
			
			//start tcp server to accept data from arduino button
			createTCPServer();
			
			if(server!=null){
				server.addListener(new AbstractServerListener() {

		            @Override
		            public void onReceive(Client client, byte[] data)
		            {
		                Log.d("PervasiveApp", "data0:"+data[0]+"; data1:"+data[1]);
		                if (data.length<2) Log.e("PervasiveApp", "The data less than 2 bytes:"+data.length);

		                Ard_data1 = data[0];
		                Ard_data2 = data[1];
		                
		                runOnUiThread(new Runnable() {
		                    //@Override
		                    public void run() {
		                        new UpdateData().execute(Ard_data1,Ard_data2);
		                    }
		                });
		                
		                //new UpdateData().execute(Ard_data1,Ard_data2);
		            }
		        });     
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
					String mood = PredictionHandler.getInstance().predict(fileText);
					if(mood!=null && !mood.equalsIgnoreCase("neutral")){
						if(player.getMood()!=null && !player.getMood().equalsIgnoreCase(mood)){
							if(player!=null){
								player.getPlayer().stop();
								player.getPlayer().release();
								player.setMood(mood);
								setProgressBar(mood);
							}else{
								player.setMood(mood);
								setProgressBar(mood);
							}
							player.doInBackground((Void[])null);
						}
					}
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
					String mood = PredictionHandler.getInstance().predict(fileText);
					if(mood!=null && !mood.equalsIgnoreCase("neutral")){
						if(player.getMood()!=null && !player.getMood().equalsIgnoreCase(mood)){
							if(player!=null){
								player.getPlayer().stop();
								player.getPlayer().release();
								player.setMood(mood);
								setProgressBar(mood);
							}else{
								player.setMood(mood);
								setProgressBar(mood);
							}
							player.doInBackground((Void[])null);
						}
					}
					textReader.setText(fileText);
				}else{
					Utils.setContext(Reader.this);
					Utils.showOKMessageBox("Cannot Navigate to Previous Page", "You have reached the first page.");
				}
			}
 
		});
 
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		player.getPlayer().pause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		player.getPlayer().start();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		player.getPlayer().stop();
		player.getPlayer().release();
		server.stop();
	}
	
	private void createTCPServer(){
		try
	      {
	          server = new Server(4568); //Port
	          server.start();            
	      } catch (IOException e)
	      {
	          Log.e("PervasiveApp", "Unable to start TCP server", e);
	          System.exit(-1);
	      }
	}
	
	@Override
	public void onBackPressed() {
	
		Thread bookReader = new Thread(){
			public void run(){
				try{
					Intent bookReaderIntent = new Intent("com.example.pervasiveapp.BookReader");
					startActivity(bookReaderIntent);
				}catch(Exception e){
					Utils.setContext(getApplicationContext());
					Utils.showOKMessageBox("Exception in starting BookReader", e.getMessage());
				}
				finally{
					finish();
				}
			}
		};
		bookReader.start();
		
		return;
	}
	
	/*----------------------------------------------
	 * HELPER TYPES
	 -----------------------------------------------*/
	
	class UpdateData extends AsyncTask<Integer, Integer, Integer[]> {
		
		// Called to initiate the background activity
        @Override
        protected Integer[] doInBackground(Integer... ArdState) {
            return (ArdState);  //Return to onPostExecute()
        }
        
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // Not used in this case
        }
        
        @Override
        protected void onPostExecute(Integer... result) {
        	
            if(result[0]!=null){
            	if(result[0] == 0){
            		//@Todo: Code to change song
            		Reader.this.player.getPlayer().stop();
            		Reader.this.player.setPlayer(null);
            		Reader.this.player.doInBackground((Void [])null);
            	}
            }
            
//            TextView txt = (TextView) Reader.this.textReader;
//            String newText = String.valueOf(result[0]) +"*****"+ Reader.this.textReader.getText();
//            txt.setText(newText);    // Print a random number from Arduino to activity
            
        }
    }
	
	private void setProgressBar(String mood) {
		if (mood.equalsIgnoreCase("joy")) {
			progressBar.setProgressDrawable(getResources().getDrawable(R.xml.progress_green));
		} else if (mood.equalsIgnoreCase("anger")) {
			progressBar.setProgressDrawable(getResources().getDrawable(R.xml.progress_red));
		} else if (mood.equalsIgnoreCase("anticipation")) {
			progressBar.setProgressDrawable(getResources().getDrawable(R.xml.progress_black));
		} else if (mood.equalsIgnoreCase("sadness")) {
			progressBar.setProgressDrawable(getResources().getDrawable(R.xml.progress_blue));
		} else if (mood.equalsIgnoreCase("fear")) {
			progressBar.setProgressDrawable(getResources().getDrawable(R.xml.progress_yellow));
		}
	}

}
