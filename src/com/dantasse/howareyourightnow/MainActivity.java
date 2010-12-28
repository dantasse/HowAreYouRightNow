package com.dantasse.howareyourightnow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements OnClickListener {

  private static final String FILENAME = "howareyourightnow.txt";
  private File dataFile;
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
  
  public List<Button> energyButtons = new ArrayList<Button>(); 
  public List<Button> stomachButtons = new ArrayList<Button>(); 
  public List<Button> moodButtons = new ArrayList<Button>(); 
  public Button exportButton;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    File storageDir = Environment.getExternalStorageDirectory();
    dataFile = new File(storageDir.getAbsolutePath() + "/" + FILENAME);

    LinearLayout energyLayout = (LinearLayout) findViewById(R.id.energy);
    LinearLayout stomachLayout = (LinearLayout) findViewById(R.id.stomach);
    LinearLayout moodLayout = (LinearLayout) findViewById(R.id.mood);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT /* width */,
        ViewGroup.LayoutParams.WRAP_CONTENT /* height */,
        (float) 1.0 /* weight */);
    for(int i = 1; i <= 5; i++) {
      Button energyButton = new Button(this);
      energyButton.setText(String.valueOf(i));
      energyButton.setOnClickListener(this);
      energyLayout.addView(energyButton, layoutParams);
      energyButtons.add(energyButton);
      Button stomachButton = new Button(this);
      stomachButton.setText(String.valueOf(i));
      stomachButton.setOnClickListener(this);
      stomachLayout.addView(stomachButton, layoutParams);
      stomachButtons.add(stomachButton);
      Button moodButton = new Button(this);
      moodButton.setText(String.valueOf(i));
      moodButton.setOnClickListener(this);
      moodLayout.addView(moodButton, layoutParams);
      moodButtons.add(moodButton);
    }
    exportButton = (Button) findViewById(R.id.export);
    exportButton.setOnClickListener(this);
  }

  public void onClick(View v) {
    for(Button energyButton : energyButtons) {
      if (energyButton.equals(v)) {
        writeLineToFileWithTimestamp("energy", energyButton.getText().toString());
      }
    }
    for(Button stomachButton : stomachButtons) {
      if (stomachButton.equals(v)) {
        writeLineToFileWithTimestamp("stomach", stomachButton.getText().toString());
      }
    }
    for(Button moodButton : moodButtons) {
      if (moodButton.equals(v)) {
        writeLineToFileWithTimestamp("mood", moodButton.getText().toString());
      }
    }
    if (exportButton.equals(v)) {
      try {
        StringBuilder sb = new StringBuilder();
        FileInputStream fis = new FileInputStream(dataFile);
        while(fis.available() > 0) {
          sb.append((char)fis.read());
        }
        fis.close();
        Log.d("HOWAREYOURIGHTNOW", sb.toString());
      } catch (IOException ioe) { 
        Log.w("HOWAREYOURIGHTNOW", ioe); 
      }
    }
  }

  /** Writes, for example: 1239511496730\t20101227-23:54:50\tenergy\t2\n
   * That is, current time in ms\tcurrent readable time\tvariable\tvalue.*/
  public void writeLineToFileWithTimestamp(String variable, String value) {
    Date currentDate = new Date();
    long currentTimeMs = currentDate.getTime();
    String currentTimeReadable = dateFormat.format(currentDate);
    String stringToWrite = 
      currentTimeMs + "\t" + currentTimeReadable + "\t" + variable + "\t" + value + "\n";
    try {
      // so far, MODE_WORLD_READABLE doesn't seem to do anything; guess I have
      // to use a ContentProvider or something.
      FileOutputStream fos = new FileOutputStream(dataFile, true /* append */);
      fos.write(stringToWrite.getBytes());
      fos.close();
    } catch (IOException ioe) {
      Log.w("HOWAREYOURIGHTNOW", ioe);
    }
  }
}
