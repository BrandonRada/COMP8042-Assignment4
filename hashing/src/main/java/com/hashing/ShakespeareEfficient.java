package com.hashing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import java.net.URISyntaxException;

//A class which counts the number of each word in the shakespeare dataset
public class ShakespeareEfficient {
    final static String filePath = "/data/text.txt";
    private String text;
    Map<String, Integer> wordCounts;

    static class Pair<K, V> {
        K key;
        V value;

        Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public String toString(){
            return key + " : " + value;
        }
    }

    public static void main(String[] args) {
        long startTime;
        long endTime;

        startTime = System.currentTimeMillis();
        ShakespeareEfficient shakespeare = new ShakespeareEfficient();
        endTime = System.currentTimeMillis();
        System.out.println("Total execution time to read data: " + (endTime - startTime) + " milliseconds");
        
        startTime = System.currentTimeMillis();
        shakespeare.countWords();
        endTime = System.currentTimeMillis();
        System.out.println("Total time to count word occurrences: " + (endTime - startTime) + " milliseconds");

        startTime = System.currentTimeMillis();
        shakespeare.mostCommonWords(10).forEach(System.out::println);
        endTime = System.currentTimeMillis();
        System.out.println("Total time to find top occurring with map: " + (endTime - startTime)/1000 + " milliseconds");
    } 

    public ShakespeareEfficient(){
        readData();
    }

    public String[] getText(){
        return text.split("\n");
    }

    private void readData(){
        try {
            text = new String(Files.readAllBytes(Paths.get(getClass().getResource(filePath).toURI())));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            text = "";
        }
    }

    public List<Pair<String, Integer>> mostCommonWords(int top_n){
        List<Pair<String, Integer>> sortedWordCounts = new ArrayList<>();
        wordCounts.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(top_n)
            .forEach(entry -> sortedWordCounts.add(new Pair<>(entry.getKey(), entry.getValue())));
        return sortedWordCounts;
    }

    public void countWords(){
        //What should we choose as capacity?
        wordCounts = new HashMap<>(10000);

        //for each string in the dataset, records the count
        for (String word : text.split(" ")) {
            if(wordCounts.containsKey(word)){
                wordCounts.put(word, wordCounts.get(word) + 1);
            } else {
                wordCounts.put(word, 1);
            }
        }
    }

    public Map<String, Integer> wordCounts(){
        return wordCounts;
    }
}
