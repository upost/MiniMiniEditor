package de.androidnewcomer.miniminieditor;

import android.app.Activity;
import android.content.Intent;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.OutputStream;
import java.io.PrintWriter;

public class MainActivity extends Activity {

    private static final int REQUESTCODE_SAVE = 123;
    private EditText editText;
    private SoundPool soundPool = (new SoundPool.Builder()).build();
    private int coinsSoundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.text);
        coinsSoundId = soundPool.load(this, R.raw.coins, 1);
        findViewById(R.id.save).setOnClickListener(this::onSaveClicked);
    }

    private void onSaveClicked(View view) {
        soundPool.play(coinsSoundId,1,1,1,0,1);
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "");
        startActivityForResult(intent, REQUESTCODE_SAVE);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if(requestCode==REQUESTCODE_SAVE && resultCode==Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                try {
                    OutputStream stream = getContentResolver().openOutputStream(uri);
                    PrintWriter writer = new PrintWriter(stream);
                    writer.write(editText.getText().toString());
                    writer.flush();
                    stream.close();
                } catch (java.io.IOException e) {
                    Log.e(getLocalClassName(),"caught IOException",e);
                }
            }
        }

    }
}
