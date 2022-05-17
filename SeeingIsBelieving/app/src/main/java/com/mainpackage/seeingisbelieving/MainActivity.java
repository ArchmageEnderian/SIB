package com.mainpackage.seeingisbelieving;

import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private MediaRecorder mediaRecorder;
    private String fileName = Environment.getExternalStorageDirectory() + "/Music/record.wav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button recButton =  findViewById(R.id.RecButton);
        Button stopButton = findViewById(R.id.StopButton);

        recButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    recordStart(view);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });


        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    recordStop(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }






    public void recordStart(View v) {
        try {
            releaseRecorder();

            File outFile = new File(fileName);
            if (outFile.exists()) {
                outFile.delete();
            }

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setOutputFile(fileName);



            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
        }

    }

    public void recordStop(View v) {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
        }
    }

    private void releaseRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseRecorder();
    }

}