package etf.santorini.km150066d;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nikolakis.santorinigame.R;

import java.io.File;

public class LoadActivity extends AppCompatActivity {
    private static final String directoryName = "Santorini_game";
    private LinearLayout linearLayout;
    private TextView currentTextView;
    private int mode = 0;
    private int level1 = 0, level2 = 0, branch1 = 0, branch2 = 0;
    private boolean step = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", 0);
        level1 = intent.getIntExtra("level1", 0);
        level2 = intent.getIntExtra("level2", 0);
        branch1 = intent.getIntExtra("branch1", 0);
        branch2 = intent.getIntExtra("branch2", 0);
        step = intent.getBooleanExtra("step", false);

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), directoryName);

            if (!directory.exists() && !directory.isDirectory()) {
                // create empty directory
                if (directory.mkdirs()) {
                    Log.i("CreateDir", "App dir created");
                } else {
                    Log.w("CreateDir", "Unable to create app dir!");
                }
            } else {
                Log.i("CreateDir", "App dir already exists");
            }

            File[] files = directory.listFiles();
            int size = files.length;

            linearLayout = findViewById(R.id.linearLayoutScrollView);

            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    final TextView textView = new TextView(this);
                    if (i == 0) {
                        textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        currentTextView = textView;
                    }
                    textView.setTextSize(25);
                    textView.setText(files[i].getName());
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (currentTextView != null) {
                                currentTextView.setBackground(null);
                            }
                            currentTextView = textView;
                            textView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        }
                    });
                    linearLayout.addView(textView);
                }

            } else {
                Button button = findViewById(R.id.startButton);
                button.setEnabled(false);
                TextView textView = findViewById(R.id.textViewFile);
                textView.setText("No saved files!");
            }
        }

    }

    public void onStartClicked(View view) {
       Intent intent = new Intent(this, GameActivity.class);
//        intent.putExtra("mode", mode);
//        intent.putExtra("level", level); //prepraviti
//        intent.putExtra("load", true);

        intent.putExtra("mode", mode);
        intent.putExtra("level1", level1); //prepraviti!
        intent.putExtra("level2", level2);
        intent.putExtra("branch1", branch1);
        intent.putExtra("branch2", branch2);
        intent.putExtra("step", step);
        intent.putExtra("load", true);
        intent.putExtra("filename", currentTextView.getText().toString());

        startActivity(intent);
    }
}
