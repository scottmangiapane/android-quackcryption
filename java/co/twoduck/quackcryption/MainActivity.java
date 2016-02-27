package co.twoduck.quackcryption;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText textBox;
    private Encryptor encryptor;
    private ImageView duckIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textBox = (EditText) findViewById(R.id.text_box);
        encryptor = new Encryptor();
        duckIcon = (ImageView) findViewById(R.id.duck);
        duckIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(textBox.getText()).startsWith("QuAcKqUaCk ")) {
                    duckIcon.setImageResource(R.drawable.duck_unlocked);
                    textBox.setText(encryptor.decrypt("QuackQuackQuack!", "RandomInitVector",
                            encryptor.quackReader(String.valueOf(
                                    textBox.getText())
                                    .substring(11))));
                    textBox.setFocusableInTouchMode(true);
                    textBox.setOnClickListener(null);
                } else {
                    duckIcon.setImageResource(R.drawable.duck_locked);
                    textBox.setText("QuAcKqUaCk " + encryptor.quackCryptor(encryptor.encrypt(
                            "QuackQuackQuack!", "RandomInitVector",
                            String.valueOf(textBox.getText()))));
                    textBox.setFocusable(false);
                    textBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClipboardManager clipboard = (ClipboardManager)
                                    getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("simple text", "Hello, World!");
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(getBaseContext(), "Copied!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.quack);
                if (mp != null)
                    mp.start();
            }
        });
    }
}
