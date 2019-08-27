package etf.santorini.km150066d;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.nikolakis.santorinigame.R;

public class MainActivity extends AppCompatActivity {
    private static final int START_GAME = 1;
    private int mode = 0;
    private int level1 = 0, level2 = 0, branch1 = 0, branch2 = 0;
    private boolean step = true;
    LinearLayout[] layouts;
    LinearLayout layoutConfiguration, layoutConfiguration1, layoutConfiguration2;
    ImageView imageViewLevel1, imageViewLevel2, imageViewRoot1, imageViewRoot2, imageView_mode1, imageView_mode2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layoutMain = findViewById(R.id.layoutMain);
        layoutConfiguration = layoutMain.findViewWithTag("layout_configuration");
        layoutConfiguration.setVisibility(View.INVISIBLE);
        layoutConfiguration1 = layoutMain.findViewWithTag("layout_configuration1");
        layoutConfiguration1.setVisibility(View.INVISIBLE);
        layoutConfiguration2 = layoutMain.findViewWithTag("layout_configuration2");
        layoutConfiguration2.setVisibility(View.INVISIBLE);

        imageViewLevel1 = layoutMain.findViewWithTag("imageView_level1");
        imageViewLevel2 = layoutMain.findViewWithTag("imageView_level2");
        imageViewRoot1 = layoutMain.findViewWithTag("imageView_root1");
        imageViewRoot2 = layoutMain.findViewWithTag("imageView_root2");
        imageView_mode1 = layoutMain.findViewWithTag("imageView_mode1");
        imageView_mode2 = layoutMain.findViewWithTag("imageView_mode2");
        layouts = new LinearLayout[3];
        for (int i = 0; i < 3; i++)
            layouts[i] = layoutMain.findViewWithTag("layout_" + i);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == START_GAME) {

            }
        }
    }

    public void onStartClicked(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("mode", mode);
        intent.putExtra("level1", level1); //prepraviti!
        intent.putExtra("level2", level2);
        intent.putExtra("branch1", branch1);
        intent.putExtra("branch2", branch2);
        intent.putExtra("step", step);
        intent.putExtra("load", false);
        startActivityForResult(intent, START_GAME);
    }

    public void onHumanVsHumanClicked(View view) {
        layouts[mode].setBackground(null);

        mode = 0;
        layouts[mode].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        layoutConfiguration2.setVisibility(View.INVISIBLE);
        layoutConfiguration1.setVisibility(View.INVISIBLE);
        layoutConfiguration.setVisibility(View.INVISIBLE);
    }

    public void onHumanVsRobotClicked(View view) {
        layouts[mode].setBackground(null);

        mode = 1;
        layouts[mode].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        layoutConfiguration2.setVisibility(View.VISIBLE);
        layoutConfiguration1.setVisibility(View.INVISIBLE);
        layoutConfiguration.setVisibility(View.INVISIBLE);
    }

    public void onRobotVsRobotClicked(View view) {
        layouts[mode].setBackground(null);

        mode = 2;
        layouts[mode].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        layoutConfiguration2.setVisibility(View.VISIBLE);
        layoutConfiguration1.setVisibility(View.VISIBLE);
        layoutConfiguration.setVisibility(View.VISIBLE);
    }

    public void onLevelClicked(View view) {

    }

    public void onLoadClicked(View view) {
        Intent intent = new Intent(this, LoadActivity.class);
        intent.putExtra("mode", mode);
        intent.putExtra("level1", level1); //prepraviti!
        intent.putExtra("level2", level2);
        intent.putExtra("branch1", branch1);
        intent.putExtra("branch2", branch2);
        intent.putExtra("step", step);
        intent.putExtra("load", false);
        startActivity(intent);
    }

    public void onLevel1Clicked(View view) {
        level1 = (level1 + 1) % 3;
        switch (level1) {
            case 0:
                imageViewLevel1.setBackground(getResources().getDrawable(R.drawable.wall_1));
                break;

            case 1:
                imageViewLevel1.setBackground(getResources().getDrawable(R.drawable.wall_2));
                break;

            case 2:
                imageViewLevel1.setBackground(getResources().getDrawable(R.drawable.wall_3));
                break;
        }
    }

    public void onBranching1Clicked(View view) {
        branch1 = (branch1 + 1) % 3;
        switch (branch1) {
            case 0:
                imageViewRoot1.setBackground(getResources().getDrawable(R.drawable.wall_1));
                break;

            case 1:
                imageViewRoot1.setBackground(getResources().getDrawable(R.drawable.wall_2));
                break;

            case 2:
                imageViewRoot1.setBackground(getResources().getDrawable(R.drawable.wall_3));
                break;
        }
    }

    public void onLevel2Clicked(View view) {
        level2 = (level2 + 1) % 3;
        switch (level2) {
            case 0:
                imageViewLevel2.setBackground(getResources().getDrawable(R.drawable.wall_1));
                break;

            case 1:
                imageViewLevel2.setBackground(getResources().getDrawable(R.drawable.wall_2));
                break;

            case 2:
                imageViewLevel2.setBackground(getResources().getDrawable(R.drawable.wall_3));
                break;
        }
    }

    public void onBranching2Clicked(View view) {
        branch2 = (branch2 + 1) % 3;
        switch (branch2) {
            case 0:
                imageViewRoot2.setBackground(getResources().getDrawable(R.drawable.wall_1));
                break;

            case 1:
                imageViewRoot2.setBackground(getResources().getDrawable(R.drawable.wall_2));
                break;

            case 2:
                imageViewRoot2.setBackground(getResources().getDrawable(R.drawable.wall_3));
                break;
        }
    }

    public void onStepClicked(View view) {
        step = true;
        imageView_mode1.setBackground(getResources().getDrawable(R.drawable.wall_3));
        imageView_mode2.setBackground(null);
    }

    public void onFlowClicked(View view) {
        step = false;
        imageView_mode2.setBackground(getResources().getDrawable(R.drawable.wall_3));
        imageView_mode1.setBackground(null);
    }
}
