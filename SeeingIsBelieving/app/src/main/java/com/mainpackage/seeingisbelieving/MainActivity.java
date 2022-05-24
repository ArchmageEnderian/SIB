package com.mainpackage.seeingisbelieving;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Telephony;
import android.speech.*;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Объявление глобальных переменных
    Activity thisActivity = this;
    String[] requestedPermissions = {Manifest.permission.RECORD_AUDIO};
    Button startButton;
    String[] codeWordsForRecognize = {"Календарь", "Браузер", "Гугл", "Яндекс", "Камера", "Телефон", "Набери", "Telegram", "YouTube", "WhatsApp", "SMS", "телефон"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.button_start);


        //Объявление главной кнопки поиска
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(thisActivity,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
                {
                    try
                    {
                        checkPermission();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(), "Ошибка выдачи прав, попробуйте еще раз", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Access granted", Toast.LENGTH_LONG).show();
                    speechRecognizer();
                }

            }
        });


    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            ActivityCompat.requestPermissions(this, requestedPermissions,123);
        }
    }

    private void speechRecognizer()
    {
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 100000);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 2);
        startActivityForResult(speechRecognizerIntent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK && data != null)
        {
            switch (requestCode)
            {
                case 10:
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String[] textToParse = text.get(0).split(" ");
                    int flag = -9;
                    for (int i = 0; i < textToParse.length; i++)
                        for (int j = 0; j < codeWordsForRecognize.length; j++)
                        {
                            if (textToParse[i].equals(codeWordsForRecognize[j]) && flag == -9)
                            {
                                flag = j;
                                break;
                            }

                        }


                    switch (flag)
                    {
                        case 1:
                            Toast.makeText(getApplicationContext(),"HI", Toast.LENGTH_LONG).show();
                            break;
                        case 7:
                            openTelegram();
                            break;
                        case 8:
                            openYoutube("PMWVnRKpHrs");
                            break;
                        case 9:
                            openWhatsApp();
                            break;
                        case 10: //Fix it
                            openSMS();
                            break;
                        case 11: // Доделать нормально
                            openTel();
                            break;
                        case -9:
                            Toast.makeText(getApplicationContext(), "ЧТО ПРОИСХОДИТ?", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "Я НЕ ПОНИМАЮ", Toast.LENGTH_LONG).show();
                            break;
                    }
            }
        }
    }

    private void openTel()
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"));
        startActivity(intent);
    }

    private void openSMS(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"));
        if(intent != null){
            startActivity(intent);
        }
    }

    private void openWhatsApp()
    {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/"));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/"));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private void openTelegram()
    {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("org.telegram.messenger"));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/"));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private void openYoutube(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/"));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

}