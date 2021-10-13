package com.example.almostspotify;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ui.PlayerView;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static com.example.almostspotify.Node.brokers;

public class ConsumerImpl extends Activity implements AdapterView.OnItemSelectedListener {

    private ImageButton play,pause;
    private Button button;
    private EditText artist, category, song;
    private com.example.almostspotify.Value<com.example.almostspotify.MusicFile> fr = new Value<MusicFile>();
    private com.example.almostspotify.MusicFile finalReply;
    private TextView finalResult, external_stor;
    private FileInputStream mFileName;
    public static final int SIZE = 524288;
    MediaPlayer mediaPlayer;
    private ImageView album_art;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_impl);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.strings, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        artist = (EditText) findViewById(R.id.edittext_artist);
        category = (EditText) findViewById(R.id.edittext_category);
        song = (EditText) findViewById(R.id.edittext_song);
        finalResult = (TextView) findViewById(R.id.progress);
        button= (Button) findViewById(R.id.run_async_button);
        album_art = (ImageView) findViewById(R.id.album_art);


    }

    public void onStart() {
        super.onStart();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String Artist = artist.getText().toString();
                String Category = category.getText().toString();
                String Song = song.getText().toString();
                runner.execute(Artist, Category, Song);
            }
        });


    }




    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskRunner extends AsyncTask<String, String, Value<MusicFile>> {
        ProgressDialog progressDialog;
        private String art;
        private String cat;
        private String son;

        @Override
        protected Value<MusicFile> doInBackground(String... params) {
            int c = 0;
            for (Broker b : brokers) {
                c++;
                if (c == 2)
                    break;
                publishProgress("Searching..."); // Calls onProgressUpdate()
                try {
                    Thread.sleep(3 * 1000);
                    art = params[0];
                    cat = params[1];
                    son = params[2];


                    Socket requestSocket = null;
                    ObjectOutputStream out = null;
                    ObjectInputStream in = null;
                    try {
                        requestSocket = new Socket(InetAddress.getByName(b.getIp()), b.getPort());
                        out = new ObjectOutputStream(requestSocket.getOutputStream());
                        in = new ObjectInputStream(requestSocket.getInputStream());
                        out.writeObject(art);
                        out.flush();
                        out.writeObject(cat);
                        out.flush();
                        out.writeObject(son);
                        out.flush();
                        long numOfChunks = (long) in.readObject();
                        byte[] fileContent = (byte[]) in.readObject();
                        int offset = (int) in.readObject();
                        long extra = (long) in.readObject();

                        for (int i = 0; i < numOfChunks; i++) {

                            finalReply = (MusicFile) in.readObject(); //finalreply has all the info and 1 chunk.
                            fr.add(finalReply);


                        }
                        checkExternalMedia();
                        for (int j = 0; j < numOfChunks; j++) {
                            writeToSDFile(fileContent, offset, j, extra, numOfChunks, fr.get(0).getTitle());

                            if (j == numOfChunks - 2 && extra != 0) {
                                offset = offset + (int) extra;
                            } else {
                                offset = offset + 524288;
                            }
                            if (j == numOfChunks - 2 && extra == 0)
                                break;
                        }


                    } catch (UnknownHostException unknownHost) {
                        System.err.println("You are trying to connect to an unknown host!");
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    } finally {
                        try {
                            in.close();
                            out.close();
                            requestSocket.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            return fr;

        }


        @Override
        protected void onPostExecute(Value<MusicFile> result) {
            progressDialog.dismiss();
            //pause.setVisibility(View.VISIBLE);
            String title_toBePassed = null;



            int c = 0;

            ArrayList<MusicFile> p = new ArrayList<>();

            for (MusicFile m : result) {
                if(c==0){
                    title_toBePassed = m.getTitle();
                }

                p.add(m);

                c++;

            }
            int sum = c;


            Intent intent = new Intent(ConsumerImpl.this, PlayerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("TITLE", title_toBePassed);
            intent.putExtra("CHUNKS", sum);
            Bundle args = new Bundle();
            args.putSerializable("list",(Serializable)p);
            intent.putExtra("BUNDLE",args);
            getApplicationContext().startActivity(intent);




        }


        protected void onPreExecute() {
            progressDialog = progressDialog.show(ConsumerImpl.this, "ProgressDialog", "Searching for artist: ");
        }

        @Override
        protected void onProgressUpdate(String... text) {
            finalResult.setText(text[0]);
        }
    }
/**
    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskPlayer extends AsyncTask<MusicFile, String, Void> {
        int c;

        public AsyncTaskPlayer(int c) {
            this.c = c;
        }

        @Override
        protected Void doInBackground(MusicFile... musicFiles) {
            MusicFile mf = musicFiles[0];

            //MediaPlayer mediaPlayer = new MediaPlayer();


           // MediaMetadataRetriever metaRetriver;
            //byte[] art;

            byte[] bytearray = new byte[0];
            try {


                FileInputStream in = new FileInputStream("/data/user/0/com.example.almostspotify/cache/Chunks/" + mf.getTitle() + c + ".mp3");
                Log.e("ASYNC2 ","Chunk is ready to be read: "+c);
                /**
               // metaRetriver = new MediaMetadataRetriever();
                //metaRetriver.setDataSource("/data/user/0/com.example.almostspotify/cache/Chunks/" + mf.getTitle() + c + ".mp3");
                try {
                    art = metaRetriver.getEmbeddedPicture();
                    Bitmap songImage = BitmapFactory
                            .decodeByteArray(art, 0, art.length);
                    album_art.setImageBitmap(songImage);
                }catch (Exception e) {
                    album_art.setBackgroundColor(Color.GRAY);
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
                        play.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mp.start();
                            }
                        });

                        pause.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mp.pause();
                            }
                        });

                    }
                });
                mediaPlayer.prepareAsync();
            } catch (IOException ex) {
                String s = ex.toString();
                ex.printStackTrace();
            }
            Log.e("c: ", "c: " + c);

            return null;
        }

    }**/


private void checkExternalMedia() {
    boolean mExternalStorageAvailable = false;
    boolean mExternalStorageWriteable = false;
    String state = Environment.getExternalStorageState();

    if (Environment.MEDIA_MOUNTED.equals(state)) {
        // Can read and write the media
        mExternalStorageAvailable = mExternalStorageWriteable = true;
    } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
        // Can only read the media
        mExternalStorageAvailable = true;
        mExternalStorageWriteable = false;
    } else {
        // Can't read or write
        mExternalStorageAvailable = mExternalStorageWriteable = false;
    }
    Log.e("CHECK", "EDW: " + mExternalStorageAvailable + mExternalStorageWriteable);
}

    private void writeToSDFile(byte[] content, int offset, int num, long extra, long numofchunks, String songName) throws IOException {

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(getCacheDir() + "/Chunks/");
        dir.mkdirs();
        //create a new file
        File file = new File(dir, songName + num + ".mp3");


        FileOutputStream f = new FileOutputStream(file);

        if (extra > 0 && num == numofchunks - 1) {
            f.write(content, offset + (int) extra, (int) extra);
            f.close();
        } else {
            f.write(content, offset + SIZE, SIZE);
            f.close();
        }


        //call player to play the song
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
