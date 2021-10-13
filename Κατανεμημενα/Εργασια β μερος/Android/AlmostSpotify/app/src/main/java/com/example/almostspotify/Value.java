package com.example.almostspotify;

import java.io.Serializable;
import java.util.ArrayList;

public class Value<MusicFile> extends ArrayList<MusicFile> implements Serializable
{
    private MusicFile MusicFile;

    public MusicFile getData() {
        return this.MusicFile;
    }
}