package com.example.almostspotify;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class PlayerActivity extends ConsumerImpl implements Serializable {
    MediaMetadataRetriever metaRetriver;
    byte[] art;
    //ImageView album_art_player, play_button, stop, pause, forward, back;
    ImageView play_pause;
    ImageView album_art_player;
    String title;
    int ch;
    private MediaPlayer mediaPlayer;
    private ArrayList<MusicFile> playlist = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        setScreen();

        //get values from prev activity
        title = getIntent().getStringExtra("TITLE");
        ch = getIntent().getIntExtra("CHUNKS", 0);
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        playlist = (ArrayList<MusicFile>) args.getSerializable("list");
        play_pause = (ImageView) findViewById(R.id.play_pause_button);


        Log.e("LIST: ","PAME "+playlist.get(0).getArtist());


    }

    public void onStart() {
        super.onStart();
        int c = 0;
        for(MusicFile m : playlist) {
            AsyncTaskPlayer runner = new AsyncTaskPlayer(c);
            runner.execute(m);
            c++;
        }





    }
    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskPlayer extends AsyncTask<MusicFile, String, MusicFile> {
        int c;

        public AsyncTaskPlayer(int c) {
            this.c = c;
        }

        @SuppressLint("WrongThread")
        @Override
        protected MusicFile doInBackground(MusicFile... musicFiles) {
            MusicFile mf = musicFiles[0];

            return mf;

        }

        @SuppressLint("SdCardPath")
        protected void onPostExecute(MusicFile mf) {

            MediaMetadataRetriever metaRetriver;
            byte[] art;

            mediaPlayer = new MediaPlayer();

            byte[] bytearray = new byte[0];
            try {


                FileInputStream in = new FileInputStream("/data/user/0/com.example.almostspotify/cache/Chunks/" + title + c + ".mp3");

                metaRetriver = new MediaMetadataRetriever();
                metaRetriver.setDataSource("/data/user/0/com.example.almostspotify/cache/Chunks/" + title + c + ".mp3");
                try {
                    art = metaRetriver.getEmbeddedPicture();
                    Bitmap songImage = BitmapFactory
                            .decodeByteArray(art, 0, art.length);
                    album_art_player.setImageBitmap(songImage);
                } catch (Exception e) {
                    album_art_player.setBackgroundColor(Color.GRAY);
                }


                bytearray = new byte[in.available()];
                bytearray = toByteArray(in);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                File tempFile = File.createTempFile("mobile", "mp3", getCacheDir());
                //tempFile.deleteOnExit();
                FileOutputStream fos = new FileOutputStream(tempFile);
                fos.write(bytearray);
                fos.flush();
                fos.close();
                mediaPlayer.reset();

                FileInputStream fis = new FileInputStream(tempFile);
                mediaPlayer.setDataSource(fis.getFD());


                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(final MediaPlayer mp) {
                        Boolean temp = true;
                        //mp.start();
                        play_pause.setImageResource(R.drawable.ic_play);

                        play_pause.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(mp.isPlaying()) {
                                    mp.pause();
                                    play_pause.setImageResource(R.drawable.ic_play);
                                } else{
                                    mp.start();
                                    play_pause.setImageResource(R.drawable.ic_pause);

                                }
                            }
                        });

                    }



                });
                mediaPlayer.prepareAsync();


                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();

                    }
                });


            } catch (IOException ex) {
                String s = ex.toString();
                ex.printStackTrace();
            }
            Log.e("c: ", "c: " + c);

        }
    }




    public byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read = 0;
        byte[] buffer = new byte[1024];
        while (read != -1) {
            read = in.read(buffer);
            if (read != -1)
                out.write(buffer,0,read);
        }
        out.close();
        return out.toByteArray();
    }

    private void setScreen(){
        album_art_player = (ImageView) findViewById(R.id.album_art_player);
        play_pause = (ImageView) findViewById(R.id.play_pause_button);
        //play_button = (ImageView) findViewById(R.id.exo_play);
        //forward = (ImageView) findViewById(R.id.fast_forward);
        //back = (ImageView) findViewById(R.id.back);
        //pause = (ImageView) findViewById(R.id.exo_pause);
        //stop = (ImageView) findViewById(R.id.stop);
    }

}