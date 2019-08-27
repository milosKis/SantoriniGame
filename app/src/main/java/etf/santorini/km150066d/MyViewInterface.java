package etf.santorini.km150066d;

import android.app.Application;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikolakis.santorinigame.R;

import java.util.ArrayList;

public class MyViewInterface implements Controller.ViewInterface {
    private int count;
    private int mode;
    private Application application;
    private FrameLayout[][] frameLayouts;
    private TextView[][] textViews;
    private ImageView[][] imageViews;
    private TextView textViewMessage, textViewPlayer;
    private Button nextButton, saveButton;

    public MyViewInterface(int count, int mode, Application application, FrameLayout[][] frameLayouts, TextView[][] textViews, ImageView[][] imageViews, TextView textViewMessage, TextView textViewPlayer, Button nextButton, Button saveButton) {
        this.count = count;
        this.mode = mode;
        this.application = application;
        this.frameLayouts = frameLayouts;
        this.textViews = textViews;
        this.imageViews = imageViews;
        this.textViewMessage = textViewMessage;
        this.textViewPlayer = textViewPlayer;
        this.nextButton = nextButton;
        this.saveButton = saveButton;
    }

    @Override
    public void drawPlayer0(int x, int y) {
        count++;
        if (mode == 2) {
            imageViews[x][y].setBackground(application.getDrawable(R.drawable.robot_1));
        }
        else {
            imageViews[x][y].setBackground(application.getDrawable(R.drawable.builder_1));
        }

        if (count == 1)
            textViewMessage.setText(Messages.PUT_1_FIGURE);
        else {
            count = 0;
            textViewMessage.setText(Messages.PUT_2_FIGURES);
            textViewPlayer.setText(Messages.PLAYER_1_TURN);
        }
    }

    @Override
    public void drawPlayer1(int x, int y) {
        count++;
        if ((mode == 1) || (mode == 2)) {
            imageViews[x][y].setBackground(application.getDrawable(R.drawable.robot_2));
        }
        else {
            imageViews[x][y].setBackground(application.getDrawable(R.drawable.builder_2));
        }
        if (count == 1)
            textViewMessage.setText(Messages.PUT_1_FIGURE);
        else {
            count = 0;
            textViewMessage.setText(Messages.SELECT_FIGURE);
            textViewPlayer.setText(Messages.PLAYER_0_TURN);
        }
    }

    @Override
    public void setMessage(String message) {
        textViewMessage.setText(message);
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setToast(String toast) {
        Toast.makeText(application, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fadePlayer(int player, Point[] points) {
        int size = points.length;
        if (player == 0) {
            if (mode == 2) {
                for (int i = 0; i < size; i++) {
                    imageViews[points[i].getX()][points[i].getY()].setBackground(application.getDrawable(R.drawable.robot_1_faded));
                }
            }
            else {
                for (int i = 0; i < size; i++) {
                    imageViews[points[i].getX()][points[i].getY()].setBackground(application.getDrawable(R.drawable.builder_1_faded));
                }
            }
        }
        else {
            if ((mode == 1) || (mode == 2)) {
                for (int i = 0; i < size; i++) {
                    imageViews[points[i].getX()][points[i].getY()].setBackground(application.getDrawable(R.drawable.robot_2_faded));
                }
            }
            else {
                for (int i = 0; i < size; i++) {
                    imageViews[points[i].getX()][points[i].getY()].setBackground(application.getDrawable(R.drawable.builder_2_faded));
                }
            }

        }

    }

    @Override
    public void fadeFields(ArrayList<Point> points, int[][] matrix) {
        int size = points.size();

        for (int i = 0; i < size; i++) {
            Point p = points.get(i);
            int x = p.getX();
            int y = p.getY();

            switch (matrix[x][y]) {
                case 0:
                    frameLayouts[x][y].setBackgroundColor(application.getResources().getColor(R.color.colorGray));
                    break;
                case 1:
                    frameLayouts[x][y].setBackground(application.getDrawable(R.drawable.wall_1_faded));
                    break;
                case 2:
                    frameLayouts[x][y].setBackground(application.getDrawable(R.drawable.wall_2_faded));
                    break;
                case 3:
                    frameLayouts[x][y].setBackground(application.getDrawable(R.drawable.wall_3_faded));
                    break;
                case 4:
                    frameLayouts[x][y].setBackground(application.getDrawable(R.drawable.dome_faded));
                    break;
            }
        }
    }

    @Override
    public void removePlayer(int x, int y) {
        imageViews[x][y].setBackground(null);
    }

    @Override
    public void unfadeAllFields(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++) {
                switch (matrix[i][j]) {
                    case 0:
                        frameLayouts[i][j].setBackgroundColor(application.getResources().getColor(R.color.colorPrimary));
                        break;
                    case 1:
                        frameLayouts[i][j].setBackground(application.getDrawable(R.drawable.wall_1));
                        break;
                    case 2:
                        frameLayouts[i][j].setBackground(application.getDrawable(R.drawable.wall_2));
                        break;
                    case 3:
                        frameLayouts[i][j].setBackground(application.getDrawable(R.drawable.wall_3));
                        break;
                    case 4:
                        frameLayouts[i][j].setBackground(application.getDrawable(R.drawable.dome));
                        break;
                }
            }
    }

    @Override
    public void unfadePlayer(int player, Point[] points) {
        int size = points.length;
        if (player == 0) {
            if (mode == 2) {
                for (int i = 0; i < size; i++) {
                    imageViews[points[i].getX()][points[i].getY()].setBackground(application.getDrawable(R.drawable.robot_1));
                }
            }
            else {
                for (int i = 0; i < size; i++) {
                    imageViews[points[i].getX()][points[i].getY()].setBackground(application.getDrawable(R.drawable.builder_1));
                }
            }

        }
        else {
            if ((mode == 1) || (mode == 2)) {
                for (int i = 0; i < size; i++) {
                    imageViews[points[i].getX()][points[i].getY()].setBackground(application.getDrawable(R.drawable.robot_2));
                }
            }
            else {
                for (int i = 0; i < size; i++) {
                    imageViews[points[i].getX()][points[i].getY()].setBackground(application.getDrawable(R.drawable.builder_2));
                }
            }

        }
    }

    @Override
    public void disableNextButton() {
        nextButton.setEnabled(false);
    }

    @Override
    public void enableSaveButton() {
        saveButton.setEnabled(true);
    }


    @Override
    public void setPlayerName(String playerName) {
        textViewPlayer.setText(playerName);
    }
}
