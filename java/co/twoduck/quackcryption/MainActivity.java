package co.twoduck.quackcryption;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private AppCompatActivity activity;
    private EditText textBox;
    private ImageView duckIcon;
    private ImageView settings;
    private MediaPlayer mp;
    private Preferences preferences;
    private SingleStep ss;
    private Toast t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        textBox = (EditText) findViewById(R.id.text_box);
        duckIcon = (ImageView) findViewById(R.id.duck);
        settings = (ImageView) findViewById(R.id.settings);
        mp = MediaPlayer.create(MainActivity.this, R.raw.quack);
        preferences = new Preferences(this);
        ss = new SingleStep();
        duckIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                overridePendingTransition(R.transition.slide_left_1, R.transition.slide_left_2);
            }
        });
    }

    private void refresh() {
        if (String.valueOf(textBox.getText()).startsWith("qUACkquaCk ")) {
            duckIcon.setImageResource(R.drawable.duck_unlocked);
            textBox.setText(ss.throughNormal(preferences.getKey(),
                    preferences.getInitVector(),
                    String.valueOf(textBox.getText()).substring(11)));
            textBox.setFocusableInTouchMode(true);
            textBox.setOnClickListener(null);
        } else {
            duckIcon.setImageResource(R.drawable.duck_locked);
            textBox.setText("qUACkquaCk " + ss.throughQuack(
                    preferences.getKey(), preferences.getInitVector(),
                    String.valueOf(textBox.getText())));
            textBox.setEnabled(false);
            textBox.setEnabled(true);
            textBox.setFocusable(false);
            textBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager)
                            getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("ENCRYPTED STRING",
                            String.valueOf(textBox.getText()));
                    clipboard.setPrimaryClip(clip);
                    if (t != null)
                        t.cancel();
                    t = Toast.makeText(activity, "Copied!", Toast.LENGTH_SHORT);
                    t.show();
                }
            });
        }
        try {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(MainActivity.this, R.raw.quack);
            } mp.start();
        } catch(Exception e) { e.printStackTrace(); }
    }
}
