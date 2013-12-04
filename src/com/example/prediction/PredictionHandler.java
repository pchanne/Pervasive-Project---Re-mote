package com.example.prediction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PredictionHandler {
	
	private List<String> positiveEmotions = new ArrayList<String>(Arrays.asList("joy", "anticipation"));
	private List<String> negativeEmotions = new ArrayList<String>(Arrays.asList("anger", "fear", "sadness"));
	private static PredictionHandler predictionHandler;
	
	private PredictionHandler () {}
	
	public static PredictionHandler getInstance(){
		if (predictionHandler == null) {
			predictionHandler = new PredictionHandler();
		}
		return predictionHandler;
	}

	public String predict(String text) {
		Map<String, Integer> emotionalAtyachar;
		emotionalAtyachar = new ConcurrentHashMap<String, Integer>();
		List<String> tokens = new ArrayList<String>(Arrays.asList(text
				.split(" ")));
		NRCLexiconToMapParser nrcLexiconToMapParser = NRCLexiconToMapParser
				.getInstance();
//		Stemmer s = new Stemmer();
		for (String token : tokens) {
//			token = s.stem(token);
//			System.out.println(token);
			token = strip(token);
			if (nrcLexiconToMapParser.getWordToEmotionsMap().containsKey(token)) {
				List<String> emotions = nrcLexiconToMapParser
						.getWordToEmotionsMap().get(token);
				for (String emotion : emotions) {
					if (emotionalAtyachar.containsKey(emotion)) {
						emotionalAtyachar.put(emotion,
								emotionalAtyachar.get(emotion) + 1);
					} else {
						emotionalAtyachar.put(emotion, 1);
					}
				}
			}
		}
		System.out.println(emotionalAtyachar);
		return getMaxFrequencyCategory(emotionalAtyachar);
	}

	private String getMaxFrequencyCategory(Map <String, Integer> emotionalAtyachar) {

		String maximumEmotion = "";
		int totalEmotionCount = 0;
		float max = 0;

		for (String emotion : emotionalAtyachar.keySet()) {
			if (emotionalAtyachar.get(emotion) > max) {
				totalEmotionCount += emotionalAtyachar.get(emotion);
			}
		}

		int positive = 0;
		int negative = 0;
		for (String emotion : emotionalAtyachar.keySet()) {
			if (emotion.equals("surprise")) {
//				positive += emotionalAtyachar.get(emotion);
				int count = 0;
				if (emotionalAtyachar.containsKey("anticipation")) 
					count = emotionalAtyachar.get("anticipation");
				emotionalAtyachar.put("anticipation", count + emotionalAtyachar.get("surprise"));
				
			} else if (emotion.equals("joy") || emotion.equals("trust") || emotion.equals("anticipation")) {
				positive += emotionalAtyachar.get(emotion);
				if (emotion.equals("trust")){
					int count = 0;
					if (emotionalAtyachar.containsKey("joy"))
					count = emotionalAtyachar.get("joy");
					emotionalAtyachar.put("joy", count + emotionalAtyachar.get("trust"));
				}
			} else {
				if (emotion.equals("disgust")) {
					int count = 0;
					if (emotionalAtyachar.containsKey("anger"))
						count = emotionalAtyachar.get("anger");
					emotionalAtyachar.put("anger", count + emotionalAtyachar.get(emotion));
					
				}
				else negative += emotionalAtyachar.get(emotion);
				
			}
		}
		
		if (emotionalAtyachar.containsKey("disgust")) emotionalAtyachar.remove("disgust");
		if (emotionalAtyachar.containsKey("trust")) emotionalAtyachar.remove("trust");
		if (emotionalAtyachar.containsKey("surprise")) emotionalAtyachar.remove("surprise");
		
		
		System.out.println(positive);
		System.out.println(negative);
		if (positive > negative) {
			for (String emotion: negativeEmotions)
				emotionalAtyachar.remove(emotion);
		} else {
			for (String emotion: positiveEmotions)
				emotionalAtyachar.remove(emotion);
		}
		
		System.out.println(emotionalAtyachar);

		try {
			for (String emotion : emotionalAtyachar.keySet()) {
				float ratio = ((float) emotionalAtyachar.get(emotion))
						/ ((float) totalEmotionCount);
				if (ratio > max) {
					max = ratio;
					maximumEmotion = emotion;
				}
			}
		} catch (Exception e) {
			return "neutral";
		}
		if (maximumEmotion.equals(""))
			return "neutral";

		System.out.println(maximumEmotion);
		return maximumEmotion;
	}

	private String strip (String word) {
		word.endsWith("ed") {
			
		}
	}
}
