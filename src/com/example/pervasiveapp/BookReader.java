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

import com.example.prediction.NRCLexiconToMapParser;
import com.example.prediction.PredictionHandler;

public class BookReader extends Activity {
	ListView bookList;
	Context context;
	
	boolean mExternalStorageAvailable = false;
	boolean mExternalStorageWriteable = false;

	PredictionHandler predictionHandler = PredictionHandler.getInstance();
	NRCLexiconToMapParser nrcLexiconToMapParser = NRCLexiconToMapParser
			.getInstance();
	
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
						
			try {
				// move to one-time startup code
				nrcLexiconToMapParser.parse();
				
				// predictionHandler
				// .predict("October arrived, spreading a damp chill over the grounds and into the castle. Madam Pomfrey, the nurse, was kept busy by a sudden spate of colds among the staff and students. Her Pepperup potion worked instantly, though it left the drinker smoking at the ears for several hours afterward. Ginny Weasley, who had been looking pale, was bullied into taking some by Percy. The steam pouring from under her vivid hair gave the impression that her whole head was on fire. Raindrops the size of bullets thundered on the castle windows for days on end; the lake rose, the flower beds turned into muddy streams, and Hagrid's pumpkins swelled to the size of garden sheds. Oliver Wood's enthusiasm for regular training sessions, however, was not dampened, which was why Harry was to be found, late one stormy Saturday afternoon a few days before Halloween, returning to Gryffindor Tower, drenched to the skin and splattered with mud. Even aside from the rain and wind it hadn't been a happy practice session. Fred and George, who had been spying on the Slytherin team, had seen for themselves the speed of those new Nimbus Two Thousand and Ones. They reported that the Slytherin team was no more than seven greenish blurs, shooting through the air like missiles. As Harry squelched along the deserted corridor he came across somebody who looked just as preoccupied as he was. Nearly Headless Nick, the ghost of Gryffindor Tower, was staring morosely out of a window, muttering under his breath, \". . . don't fulfill their requirements . . . half an inch, if that . . .\" I mean, nobody wishes more than I do that it had all been quick and clean, and my head had come off properly, I mean, it would have saved me a great deal of pain and ridicule. However - Nearly Headless Nick shook his letter open and read furiously: \"'We can only accept huntsmen whose heads have parted company with their bodies. You will appreciate that it would be impossible otherwise for members to participate in hunt activities such as Horseback Head-Juggling and Head Polo. It is with the greatest regret, therefore, that I must inform you that you do not fulfill our requirements. With very best wishes, Sir Patrick Delaney-Podmore.");

				// inception
				// predictionHandler.predict("Robert comes back to life in dream level 3 with Eames' aid and opens his own safe, finding within an image of his bed-stricken father muttering his last word. Robert acknowledges that his father was disappointed that he couldn't be him, but Maurice says, \"No...no. I was disappointed that you tried.\" Maurice then points to a cabinet where Robert finds the will...and a paper fan his father made for him once as a child. Tearfully, Robert looks up to see his father has passed and breaks down as the van hits the water."+
				//
				// "Dream 2, Arthur hits the detonator and the explosives force the elevator down, creating artificial gravity on the team."+
				//
				// "Dream 3, a series of explosions set by Eames rock the fort, collapsing the main floor."+
				//
				// "In limbo, the synchronization of kicks pulls on Ariadne and she calls for Cobb to join her. Cobb says that he will stay in limbo, but not with Mal. By this time, Saito has died and joined limbo as well. Cobb must find him but promises to return. Ariadne leaps off the side of the building and rides the kicks back to dream 1. In the van, Robert wakes up and escapes the submerged van with 'Browning'. Arthur and Ariadne share an oxygen tank with Yusuf before they escape the van, leaving Cobb."+
				//
				// "Robert and 'Browning' make it to shore where Robert reveals that his father really did want him to be his own man and that he's going to do just that and liquidate his father's company. Knowing the mission is a success, Eames drops the Browning mask."+
				//
				// "In limbo, Cobb washes ashore where the armed guard finds him. He is brought to the seaside palace where the elderly Japanese man recognizes his brass top. Cobb recalls what he was there to do and calls to Saito, asking him to come back with him and honor their arrangement. The elderly Saito reaches for Cobb's gun."+
				//
				// "Cobb wakes up on the airplane and looks around, startled, to see Arthur and Ariadne smiling at him. He looks at the now awake Saito who remembers, picking up his phone and dials. The plane lands in Los Angeles and Cobb nervously moves through customs where security checks his passport, but allows him passage through, welcoming him home. Cobb walks past the rest of the team and Robert, who pauses a moment as if recalling a half-remembered dream. Ahead of him, Cobb sees Miles calling him over. They drive home together where Cobb hesitates before taking out his brass top. He spins it on the table in the kitchen as his children appear at the back door. He runs to them, elated to see their faces again as the top continues to spin, wobbles a bit...and the screen turns to black.");

				predictionHandler
						.predict("The book tells the story of Horton the Elephant, who, in the afternoon of May 15 while splashing in a pool in the Jungle of Nool, hears a small speck of dust talking to him. Horton surmises that a small person lives on the speck and places it on a clover, vowing to protect it. He later discovers that the speck is actually a tiny planet, home to a community called Whoville, where microscopic creatures called Whos live. The Mayor of Whoville asks Horton to protect them from harm, which Horton happily agrees to, proclaiming throughout the book that \"a person’s a person, no matter how small.\"In his mission to protect the speck, Horton is ridiculed and harassed by the other animals in the jungle for believing in something that they are unable to see or hear. He is first criticized by a sour kangaroo and the little kangaroo in her pouch. The splash they make as they jump into the pool almost catches the speck, so Horton decides to find somewhere safer for it. However, news of his odd new behavior spreads quickly, and he is soon harassed by the Wickersham Brothers, a group of monkeys. They steal the clover from him and give it to Vlad Vlad-i-koff, an eagle. Vlad-i-koff flies the clover a long distance, Horton in pursuit, until the eagle drops it into a field of clovers. After a long search, Horton finally finds the clover with the speck on it. However, the Mayor informs him that Whoville is in bad shape from the fall, and Horton discovers that the sour kangaroo and the Wickersham family have caught up to him. They tie Horton up and threaten to boil the speck in a pot of \"Beezle-Nut\" oil. To save Whoville, Horton implores the little people to make as much noise as they can, to prove their existence. So almost everyone in Whoville shouts, sings, and plays instruments, but still no one but Horton can hear them. So the Mayor searches Whoville until he finds a \"very small shirker named JoJo\", who is playing with a yo-yo instead of making noise. The Mayor carries him to the top of Eiffelberg Tower, where Jojo lets out a loud \"Yopp!\", which finally makes the kangaroo and the Wickersham family hear the Whos. Now convinced of the Whos's existence, the other jungle animals vow to help Horton protect the tiny community.");
			} catch (IOException e) {
				Log.e(e.getCause().toString(), e.getMessage());
			}
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
}

