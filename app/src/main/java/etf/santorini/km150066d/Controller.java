package etf.santorini.km150066d;

import java.util.ArrayList;

public class Controller {
    public static final int DRAW_PLAYER0 = 1;
    public static final int DRAW_PLAYER1 = 2;
    public static final int ERROR_POSITION_OCCUPIED = 3;
    public static final int SELECT_PLAYER0 = 4;
    public static final int SELECT_PLAYER1 = 5;
    public static final int SELECTED_PLAYER = 6;
    public static final int MOVE_PLAYER = 7;
    public static final int BUILD = 8;
    public static final int END_GAME = 9;

    private Model model;
    private ViewInterface viewInterface;
    private int x, y;
    private boolean gameOver = false;
    private int count = 0;

    public Controller(Model model, ViewInterface viewInterface) {
        this.model = model;
        this.viewInterface = viewInterface;
    }

    public interface ViewInterface {
        public void drawPlayer0(int x, int y);
        public void drawPlayer1(int x, int y);
        public void setMessage(String message);
        public void setToast(String toast);
        public void setPlayerName(String playerName);
        public void fadePlayer(int player, Point[] points);
        public void fadeFields(ArrayList<Point> points, int[][] matrix);
        public void removePlayer(int x, int y);
        public void unfadeAllFields(int[][] matrix);
        public void unfadePlayer(int player, Point[] points);
        public void disableNextButton();
        public void enableSaveButton();

    }

    public void clicked(int x, int y) {
        count++;
        if (!gameOver) {
            this.x = x;
            this.y = y;
            int code = model.clicked(x, y);
            resolveCode(code);
        }


        //added for human vs AI
//        if (!gameOver) {
//            if (count >= 7 && ((count - 7) % 3 == 0)) {
//                Decision decision = MiniMaxAlgorithm.miniMax(model.getMatrix(), model.getPlayers(), 1, 1 );
//                int figure = decision.getFigure();
//                Player p = model.getPlayers()[1];
//                int code = model.clicked(p.getFigurePoint(figure).getX(), p.getFigurePoint(figure).getY());
//                resolveCode(code);
//                code = model.clicked(decision.getMovePoint().getX(), decision.getMovePoint().getY());
//                resolveCode(code);
//                code = model.clicked(decision.getBuildPoint().getX(), decision.getBuildPoint().getY());
//                resolveCode(code);
//
//            }
//
//        }

    }

    public void resolveCode(int code) {
        switch (code) {
            case DRAW_PLAYER0:
                viewInterface.drawPlayer0(x, y);
                break;

            case DRAW_PLAYER1:
                viewInterface.drawPlayer1(x, y);
                break;

            case ERROR_POSITION_OCCUPIED:
                viewInterface.setToast(Messages.POSITION_TAKEN);
                break;

            case SELECT_PLAYER0:
                viewInterface.drawPlayer1(x, y);
                viewInterface.fadePlayer(1, model.getPointsFromPlayer(1));
                break;

            case SELECT_PLAYER1:
                viewInterface.fadePlayer(0, model.getPointsFromPlayer(0));
                break;

            case SELECTED_PLAYER:
            {
                Point[] points = model.getPointsFromPlayer(model.getTurn());
                Point[] pointsToFade = new Point[1];
                pointsToFade[0] = points[(model.getFigureChosen() + 1) % 2];
                viewInterface.fadePlayer(model.getTurn(), pointsToFade);
                viewInterface.setMessage(Messages.SELECT_FIELD);
                viewInterface.fadeFields(model.findNotNeighbours(true), model.getMatrix());
                //dodato
                ArrayList<Point> neighbours = model.findNeighbours(true);
                if (neighbours.size() == 0) {
                    currentIsTheLoser();
                }
                break;
            }

            case MOVE_PLAYER: {
                viewInterface.removePlayer(model.getPrevX(), model.getPrevY());
                if (model.getTurn() == 0) {
                    viewInterface.drawPlayer0(x, y);
                    viewInterface.setPlayerName(Messages.PLAYER_0_TURN);
                }
                else {
                    viewInterface.drawPlayer1(x, y);
                    viewInterface.setPlayerName(Messages.PLAYER_1_TURN);
                }
                viewInterface.unfadeAllFields(model.getMatrix());
                viewInterface.setMessage(Messages.SELECT_FIELD_TO_BUILD);
                viewInterface.fadeFields(model.findNotNeighbours(false), model.getMatrix());
                //dodato
                if (model.getMatrix()[x][y] == 3) {
                    currentIsTheWinner();
                    break;
                }
                ArrayList<Point> neighbours = model.findNeighbours(false); //ako nema gde da gradi
                if (neighbours.size() == 0) {
                    currentIsTheLoser();
                    break;
                }
                break;
            }

            case BUILD: {
                viewInterface.unfadeAllFields(model.getMatrix());
                viewInterface.setMessage(Messages.SELECT_FIGURE);
                if (model.getTurn() == 0) {
                    viewInterface.fadePlayer(1, model.getPointsFromPlayer(1));
                    viewInterface.unfadePlayer(0, model.getPointsFromPlayer(0));
                    viewInterface.setPlayerName(Messages.PLAYER_0_TURN);
                }
                else {
                    viewInterface.fadePlayer(0, model.getPointsFromPlayer(0));
                    viewInterface.unfadePlayer(1, model.getPointsFromPlayer(1));
                    viewInterface.setPlayerName(Messages.PLAYER_1_TURN);
                }
                break;
            }

            case END_GAME: {
                //gameOver = true;
                currentIsTheLoser();
            }

        }
    }

    public void currentIsTheWinner() {
        gameOver = true;
        if (model.getTurn() == 0) {
            viewInterface.setPlayerName(Messages.PLAYER_0_TURN);
            viewInterface.setMessage("is winner");
            viewInterface.fadePlayer(1, model.getPointsFromPlayer(1));
            viewInterface.unfadePlayer(0, model.getPointsFromPlayer(0));
        }
        else {
            viewInterface.setPlayerName(Messages.PLAYER_1_TURN);
            viewInterface.setMessage("is winner");
            viewInterface.fadePlayer(0, model.getPointsFromPlayer(0));
            viewInterface.unfadePlayer(1, model.getPointsFromPlayer(1));
        }
        viewInterface.unfadeAllFields(model.getMatrix());
        viewInterface.disableNextButton();
        viewInterface.enableSaveButton();
    }

    public void currentIsTheLoser() {
        gameOver = true;
        if (model.getTurn() == 0) {
            viewInterface.setPlayerName(Messages.PLAYER_1_TURN);
            viewInterface.setMessage("is winner");
            viewInterface.fadePlayer(0, model.getPointsFromPlayer(0));
            viewInterface.unfadePlayer(1, model.getPointsFromPlayer(1));
        }
        else {
            viewInterface.setPlayerName(Messages.PLAYER_0_TURN);
            viewInterface.setMessage("is winner");
            viewInterface.fadePlayer(1, model.getPointsFromPlayer(1));
            viewInterface.unfadePlayer(0, model.getPointsFromPlayer(0));
        }
        //viewInterface.unfadeAllFields(model.getMatrix());
        viewInterface.disableNextButton();
        viewInterface.enableSaveButton();
    }

    public void saveToFile() {
        model.saveToFile();
    }

    public void loadFromFile(String filename) { model.loadFromFile(filename);}

}
