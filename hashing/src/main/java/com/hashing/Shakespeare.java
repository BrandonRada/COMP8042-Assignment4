package com.hashing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import java.net.URISyntaxException;

//A class which counts the number of each word in the shakespeare dataset
public class Shakespeare {
    final static String filePath = "/data/text.txt";
    private String text;
    List<Pair<String, Integer>> wordCounts;

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
        Shakespeare shakespeare = new Shakespeare();
        endTime = System.currentTimeMillis();
        System.out.println("Total execution time to read data: " + (endTime - startTime) + " milliseconds");
        
        startTime = System.currentTimeMillis();
        shakespeare.countWords();
        endTime = System.currentTimeMillis();
        System.out.println("Total time to count word occurrences: " + (endTime - startTime) + " milliseconds");

        startTime = System.currentTimeMillis();
        shakespeare.mostCommonWords(10).forEach(System.out::println);
        endTime = System.currentTimeMillis();
        System.out.println("Total time to find top occurring with arraylist: " + (endTime - startTime)/1000 + " milliseconds");
    } 

    public Shakespeare(){
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
        wordCounts.sort((a, b) -> b.value - a.value);
        return wordCounts.subList(0, top_n);
    }

    public void countWords(){
        wordCounts = new ArrayList<>();

        //for each string in the dataset, records the count
        for (String word : text.split(" ")) {
            if(findWordIndex(word) == -1){
                wordCounts.add(new Pair<>(word, 1));
            } else {
                wordCounts.get(findWordIndex(word)).value++;
            }
        }
    }

    public List<Pair<String, Integer>> wordCounts(){
        return wordCounts;
    }

    private int findWordIndex(String word){
        //finds the index of the word in the wordCounts array, returns -1 if not found
        for(int counter = 0; counter < wordCounts.size(); counter++){
            if(wordCounts.get(counter).key.equals(word)){
                return counter;
            }
        }
        return -1;
    }

}
