package com.example.prediction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reference: http://www.saifmohammad.com/WebDocs/NRC-TechReport-emotions-in-books-and-mail.pdf
 *
 */
public class NRCLexiconToMapParser {
	private String lexiconFileName = "NRCEmotionLexicon.txt"; 
	private static Map<String, List<String>> wordToEmotionsMap;
	public static NRCLexiconToMapParser nrcLexiconToMapParser;
	
	public Map<String, List<String>> getWordToEmotionsMap() {
		return wordToEmotionsMap;
	}

	public void setWordToEmotionsMap(Map<String, List<String>> wordToEmotionsMap) {
		this.wordToEmotionsMap = wordToEmotionsMap;
	}
	
	private NRCLexiconToMapParser() {}
	
	public static NRCLexiconToMapParser getInstance() {
		if (nrcLexiconToMapParser == null) 
			nrcLexiconToMapParser = new NRCLexiconToMapParser();
		return nrcLexiconToMapParser; 
	}

	public void parse() throws IOException {
		wordToEmotionsMap = new HashMap<String, List<String>>();
		InputStream inputStream =
				this.getClass().getClassLoader().getResourceAsStream(lexiconFileName);

				if (inputStream == null)
				{
				throw new FileNotFoundException("property file properties.properties"
				+ "' not found in the classpath");
				}
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		
		while ((line = bufferedReader.readLine()) != null) {
//			System.out.println(line);
			String [] entry = line.split(" ");
			List<String> entryList = new ArrayList<String>(Arrays.asList(entry));
			entryList.remove(0);
			entryList.remove("positive");
			entryList.remove("negative");
			wordToEmotionsMap.put(entry[0], entryList);
		}
		
	}
}
