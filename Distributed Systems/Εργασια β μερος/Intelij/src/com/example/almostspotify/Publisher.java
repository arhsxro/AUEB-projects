package com.example.almostspotify;
import java.io.IOException;
import java.util.ArrayList;

public interface Publisher extends Node {

    public ArrayList<MusicFile> getTopics();
    public void setTopics(ArrayList<MusicFile> topics);
    public void notifyFailure();
	void push(String artist, String category, String song) throws IOException;


    }
