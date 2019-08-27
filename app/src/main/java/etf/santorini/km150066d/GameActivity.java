package etf.santorini.km150066d;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nikolakis.santorinigame.R;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private Controller.ViewInterface myViewInterface;
    private FrameLayout[][] frameLayouts;
    private ImageView[][] imageViews;
    private TextView[][] textViews;
    private Model model;
    private Controller controller;
    private TextView textViewPlayer;
    private TextView textViewMessage;
    private Button buttonSave, buttonNext;
    //private int clicks = 0;
    private Decision decision;
    private int turn = 0;
    private int moveNum = 0;
    private int mode;
    private int[] levels, branches;
    private boolean load, step;
    private String filename;
    private static final int MY_REQUEST_WRITE = 1;
    private static final int MY_REQUEST_READ = 2;
    private Algorithm[] algorithm = new Algorithm[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        levels = new int[2];
        branches = new int[2];

        Intent intent = getIntent();
        load = intent.getBooleanExtra("load", false);
        filename = intent.getStringExtra("filename");
        mode = intent.getIntExtra("mode", 0);
        levels[0] = intent.getIntExtra("level1", 0);
        levels[1] = intent.getIntExtra("level2", 0);
        branches[0] = intent.getIntExtra("branch1", 0);
        branches[1] = intent.getIntExtra("branch2", 0);
        step = intent.getBooleanExtra("step", false);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_WRITE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_READ);
        }

        LinearLayout linearLayout = findViewById(R.id.layout);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        textViewMessage = findViewById(R.id.textViewMessage);
        textViewPlayer = findViewById(R.id.textViewPlayer);

        frameLayouts = new FrameLayout[5][5];
        imageViews = new ImageView[5][5];
        textViews = new TextView[5][5];
        for (int i = 0; i < 5; i++) {
            frameLayouts[i] = new FrameLayout[5];
            imageViews[i] = new ImageView[5];
            textViews[i] = new TextView[5];
            for (int j = 0; j < 5; j++) {
                frameLayouts[i][j] = linearLayout.findViewWithTag("frame_" + i + "" + j);
                imageViews[i][j] = linearLayout.findViewWithTag("image_" + i + "" + j);
                textViews[i][j] = linearLayout.findViewWithTag("textViewFunc_" + i + "" + j);

                final int i_final = i;
                final int j_final = j;

                frameLayouts[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mode == 0) {
                            controller.clicked(i_final, j_final);
                            if ((model.getValidClicks() - 4) % 6 == 0)
                                buttonSave.setEnabled(true);
                            else
                                buttonSave.setEnabled(false);
                        }
                        else if (mode == 1) {

                            controller.clicked(i_final, j_final);
                            if ((model.getValidClicks() - 4) % 6 == 0)
                                buttonSave.setEnabled(true);
                            else
                                buttonSave.setEnabled(false);

                            if ((model.getValidClicks() >= 7) && ((model.getValidClicks() - 7) % 3 == 0)) {
                                Decision decision = algorithm[1].algorithm(model.getMatrix(), model.getPlayers(), 1, branches[turn], true);
                                if (decision == null) {
                                    controller.currentIsTheLoser();
                                }
                                else {
                                    int figure = decision.getFigure();
                                    Player p = model.getPlayers()[1];

                                    controller.clicked(p.getFigurePoint(figure).getX(), p.getFigurePoint(figure).getY());
                                    controller.clicked(decision.getMovePoint().getX(), decision.getMovePoint().getY());
                                    controller.clicked(decision.getBuildPoint().getX(), decision.getBuildPoint().getY());
                                    //new AsyncController().execute(model);
                                    controller.clicked(i_final, j_final);
                                    if ((model.getValidClicks() - 4) % 6 == 0)
                                        buttonSave.setEnabled(true);
                                    else
                                        buttonSave.setEnabled(false);
                                }

                            }
                        }
                        else {
                            if (model.getValidClicks() <= 4) {
                                controller.clicked(i_final, j_final);
//                                if ((model.getValidClicks() - 4) % 6 == 0)
//                                    buttonSave.setEnabled(true);
//                                else
//                                    buttonSave.setEnabled(false);
                            }
                            if (!step) {
                                if (model.getValidClicks() >= 4) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            while (true) {
                                                if (model.getValidClicks() >= 4) {
                                                    for (int i = 0; i < 2; i++) {
                                                        Decision decision = algorithm[i].algorithm(model.getMatrix(), model.getPlayers(), i, branches[i], true);
                                                        int figure = decision.getFigure();
                                                        Player p = model.getPlayers()[i];

                                                        final int finalFigure = figure;
                                                        final Player finalP1 = p;
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                controller.clicked(finalP1.getFigurePoint(finalFigure).getX(), finalP1.getFigurePoint(finalFigure).getY());

                                                            }
                                                        });

                                                        try {
                                                            Thread.sleep(1000);
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }


                                                        final Decision finalDecision = decision;
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                controller.clicked(finalDecision.getMovePoint().getX(), finalDecision.getMovePoint().getY());
                                                            }
                                                        });

                                                        try {
                                                            Thread.sleep(1000);
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }

                                                        final Decision finalDecision1 = decision;
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                controller.clicked(finalDecision1.getBuildPoint().getX(), finalDecision1.getBuildPoint().getY());
                                                            }
                                                        });

                                                        try {
                                                            Thread.sleep(2500);
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }).start();
                                }
                            }
                        }
                    }
                });
            }
        }


        buttonNext = findViewById(R.id.buttonNext);
        buttonSave = findViewById(R.id.buttonSave);
        myViewInterface = new MyViewInterface(0, mode, getApplication(), frameLayouts, textViews, imageViews, textViewMessage, textViewPlayer, buttonNext, buttonSave);

        model = new Model(5);
        controller = new Controller(model, myViewInterface);
        model.setController(controller);

        if (load)
            controller.loadFromFile(filename);

        if ((mode == 0) || (mode == 1) || !step)
            buttonNext.setEnabled(false);


        buttonSave.setEnabled(false);

        for (int i = 0; i < 2; i++) {
            if (levels[i] == 0)
                algorithm[i] = new MiniMaxAlgorithm();
            else if (levels[i] == 1)
                algorithm[i] = new AlphaBetaAlgorithm();
            else
                algorithm[i] = new CompetitiveAlgorithm();
        }

    }

    public void onNextClicked(View view) {
        if (model.getValidClicks() >= 4) {
            switch (moveNum) {
                case 0:
                    decision = algorithm[turn].algorithm(model.getMatrix(), model.getPlayers(), turn, branches[turn], true);
                    if (decision != null) {
                        int figure = decision.getFigure();
                        Player p = model.getPlayers()[turn];
                        controller.clicked(p.getFigurePoint(figure).getX(), p.getFigurePoint(figure).getY());
                        if ((model.getValidClicks() - 4) % 6 == 0)
                            buttonSave.setEnabled(true);
                        else
                            buttonSave.setEnabled(false);
                        moveNum = (moveNum + 1) % 4;

                    }
                    break;
                case 1:
                    if (decision != null) {
                        StepByStepResolver resolver = decision.getResolver();
                        ArrayList<Decision> decisions = resolver.getBestBuildDecisionsForEveryMove();
                        int size = decisions.size();
                        for (int i = 0; i < size; i++) {
                            Decision currentDecision = decisions.get(i);
                            Point point = currentDecision.getMovePoint();
                            textViews[point.getX()][point.getY()].setText(currentDecision.getFunctionValue() + "");
                        }
                        moveNum = (moveNum + 1) % 4;
                    }
                    break;
                case 2:
                    if (decision != null) {
                        StepByStepResolver resolver = decision.getResolver();
                        ArrayList<Decision> decisions = resolver.getBestBuildDecisionsForEveryMove();
                        int size = decisions.size();
                        for (int i = 0; i < size; i++) {
                            Decision currentDecision = decisions.get(i);
                            Point point = currentDecision.getMovePoint();
                            textViews[point.getX()][point.getY()].setText("");
                        }

                        decisions = resolver.getBestBuildDecisionsForMove(decision.getMovePoint());
                        size = decisions.size();
                        for (int i = 0; i < size; i++) {
                            Decision currentDecision = decisions.get(i);
                            Point point = currentDecision.getBuildPoint();
                            textViews[point.getX()][point.getY()].setText(currentDecision.getFunctionValue() + "");
                        }
                        controller.clicked(decision.getMovePoint().getX(), decision.getMovePoint().getY());
                        moveNum = (moveNum + 1) % 4;
                    }
                    break;
                case 3:
                    if (decision != null) {
                        StepByStepResolver resolver = decision.getResolver();
                        ArrayList<Decision> decisions = resolver.getBestBuildDecisionsForMove(decision.getMovePoint());
                        int size = decisions.size();
                        for (int i = 0; i < size; i++) {
                            Decision currentDecision = decisions.get(i);
                            Point point = currentDecision.getBuildPoint();
                            textViews[point.getX()][point.getY()].setText("");
                        }
                        controller.clicked(decision.getBuildPoint().getX(), decision.getBuildPoint().getY());
                        turn = (turn + 1) % 2;
                        moveNum = (moveNum + 1) % 4;
                        if ((model.getValidClicks() - 4) % 6 == 0)
                            buttonSave.setEnabled(true);
                        else
                            buttonSave.setEnabled(false);
                    }
                    break;
            }
        }
    }

    public void onSaveClicked(View view) {
        controller.saveToFile();
    }
}