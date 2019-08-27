package etf.santorini.km150066d;

import java.io.Serializable;
import java.util.ArrayList;

public class Model implements Serializable {
    private int dimension;
    private int[][] matrix;
    private Player[] players;
    private int turn = 0;
    private int figureChosen = 0;
    private int player0Chosen = 0;
    private int player1Chosen = 0;
    private boolean selectFigure = false;
    private boolean move = false;
    private boolean build = false;
    private int prevX, prevY, validClicks = 0;
    private Controller controller;
    private MoveObserver moveObserver;

    public Model(int dimension) {
        this.dimension = dimension;
        matrix = new int[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            matrix[i] = new int[dimension];
            for (int j = 0; j < dimension; j++)
                matrix[i][j] = 0;
        }
//        matrix[4][3] = matrix[3][3] = matrix[3][4] = matrix[4][2] = 2;
//        for (int i = 0; i < 5; i++)
//            matrix[1][i] = 2;

        players = new Player[2];
        for (int i = 0; i < 2; i++)
            players[i] = new Player();

        moveObserver = new MoveObserver();
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setFigurePosition(int player, int figure, int x, int y) {
        if (player > 1 || player < 0 || figure > 1 || figure < 0 || x < 0 || x >= dimension || y < 0 || y >=dimension)
            return;
        players[player].setFigurePoint(figure, x, y);
    }

    public int clicked(int x, int y) {
        //moveObserver.saveToFile();
        if (player0Chosen < 2) {
            if (isSomebodyOnPosition(x, y))
                return Controller.ERROR_POSITION_OCCUPIED;
            players[turn].setFigurePoint(player0Chosen, x, y);
            player0Chosen++;
            validClicks++;
            if (player0Chosen == 2)
                turn = 1;
            moveObserver.addPoint(new Point(x, y)); //moveObserver!
            return Controller.DRAW_PLAYER0;
        }
        else if (player1Chosen < 2) {
            if (isSomebodyOnPosition(x, y))
                return Controller.ERROR_POSITION_OCCUPIED;
            players[turn].setFigurePoint(player1Chosen, x, y);
            player1Chosen++;
            validClicks++;
            if (player1Chosen == 2) {
                turn = 0;
                selectFigure = true;
                moveObserver.addPoint(new Point(x, y)); //moveObserver!
                return Controller.SELECT_PLAYER0;
            }

            moveObserver.addPoint(new Point(x, y)); //moveObserver!
            return Controller.DRAW_PLAYER1;
        }
        else if (selectFigure) {
            if (!isPlayerOnPosition(turn, x, y))
                return 0;
            findSelectedFigure(x, y);
            selectFigure = false;
            move = true;

            moveObserver.addPoint(new Point(x, y)); //moveObserver!
            validClicks++;
            return Controller.SELECTED_PLAYER;
        }
        else if (move) {
            ArrayList<Point> points = findNeighbours(true);
            Point currentPoint = new Point(x, y);
            int size = points.size();

            for (int i = 0; i < size; i++) {
                if (currentPoint.equals(points.get(i))) {
                    prevX = players[turn].getFigurePoint(figureChosen).getX();
                    prevY = players[turn].getFigurePoint(figureChosen).getY();
                    players[turn].setFigurePoint(figureChosen, x, y);
                    move = false;
                    build = true;
                    //dodato
                    //if (matrix[x][y] == 3)
                        //return Controller.END_GAME;
                    moveObserver.addPoint(new Point(x, y)); //moveObserver!
                    validClicks++;
                    return Controller.MOVE_PLAYER;
                }
            }
            return 0;
        }
        else if (build) {
            ArrayList<Point> points = findNeighbours(false);
            Point currentPoint = new Point(x, y);
            int size = points.size();

            for (int i = 0; i < size; i++) {
                if (currentPoint.equals(points.get(i))) {
                    matrix[x][y] = matrix[x][y] + 1;
                    prevX = x;
                    prevY = y;
                    build = false;
                    selectFigure = true;
                    turn = (turn + 1) % 2;
                    moveObserver.addPoint(new Point(x, y)); //moveObserver!
                    validClicks++;
                    return Controller.BUILD;
                }
            }
            return 0;

        }

        return 0;
    }

    public boolean isSomebodyOnPosition(int x, int y) {
        for (int i = 0; i < player0Chosen; i++)
            if (players[0].getFigurePoint(i).equals(new Point(x, y)))
                return true;

        for (int i = 0; i < player1Chosen; i++)
            if (players[1].getFigurePoint(i).equals(new Point(x, y)))
                return true;

        return false;
    }

    public boolean isPlayerOnPosition(int player, int x, int y) {
        Point[] points = players[player].getFigurePoints();
        for (int i = 0; i < points.length; i++)
            if (points[i].equals(new Point(x, y)))
                return true;
        return false;
    }

    public Point[] getPointsFromPlayer(int player) {
        return players[player].getFigurePoints();
    }

    public void findSelectedFigure(int x, int y) {
        Point[] points = getPointsFromPlayer(turn);
        int i = 0;
        for (i = 0; i < points.length; i++)
            if (points[i].equals(new Point(x, y)))
                break;
        //i = (i + 1) % 2;
        figureChosen = i;
    }

    public int getFigureChosen() {
        return figureChosen;
    }

    public void setFigureChosen(int figureChosen) {
        this.figureChosen = figureChosen;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public ArrayList<Point> findNeighbours(boolean move) {
        Point current = players[turn].getFigurePoint(figureChosen);
        int x = current.getX();
        int y = current.getY();

        ArrayList<Point> pointToMove = new ArrayList<>();
        Point[] pointToCheck = new Point[8];
        pointToCheck[0] = new Point(x - 1, y + 1);
        pointToCheck[1] = new Point(x, y + 1);
        pointToCheck[2] = new Point(x + 1, y + 1);
        pointToCheck[3] = new Point(x - 1, y);
        pointToCheck[4] = new Point(x + 1, y);
        pointToCheck[5] = new Point(x - 1, y - 1);
        pointToCheck[6] = new Point(x, y - 1);
        pointToCheck[7] = new Point(x + 1, y - 1);

        for (int i = 0; i < 8; i++) {
            int currX = pointToCheck[i].getX();
            int currY = pointToCheck[i].getY();

            if (move) { //find neighbours to move
                if (currX < 0 || currX >= dimension || currY < 0 || currY >= dimension || matrix[currX][currY] == 4 || (matrix[currX][currY] - matrix[x][y] > 1))
                    continue;
            }
            else { //find neighbours to build
                if (currX < 0 || currX >= dimension || currY < 0 || currY >= dimension || matrix[currX][currY] == 4)
                    continue;
            }


            if (isSomebodyOnPosition(currX, currY))
                continue;

            pointToMove.add(pointToCheck[i]);

        }
        return pointToMove;
    }

    public ArrayList<Point> findNotNeighbours(boolean move) {
        Point current = players[turn].getFigurePoint(figureChosen);
        int x = current.getX();
        int y = current.getY();

        ArrayList<Point> pointNotToMove = new ArrayList<>();
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++)
                pointNotToMove.add(new Point(i, j));

        Point[] pointToCheck = new Point[8];
        pointToCheck[0] = new Point(x - 1, y + 1);//
        pointToCheck[1] = new Point(x, y + 1);//
        pointToCheck[2] = new Point(x + 1, y + 1);//
        pointToCheck[3] = new Point(x - 1, y);//
        pointToCheck[4] = new Point(x + 1, y);//
        pointToCheck[5] = new Point(x - 1, y - 1);//
        pointToCheck[6] = new Point(x, y - 1);//
        pointToCheck[7] = new Point(x + 1, y - 1);//

        for (int i = 0; i < 8; i++) {
            int currX = pointToCheck[i].getX();
            int currY = pointToCheck[i].getY();
            if (currX < 0 || currX >= dimension || currY < 0 || currY >= dimension)
                continue;
                //pointNotToMove.remove(currX * dimension + currY);

            if (move) {
                if (matrix[currX][currY] < 4 && (matrix[currX][currY] - matrix[x][y] <= 1) && (!isSomebodyOnPosition(currX, currY))) {
                    for (int k = 0; k < pointNotToMove.size(); k++)
                        if (pointNotToMove.get(k).equals(new Point(currX, currY))) {
                            pointNotToMove.remove(k);
                            break;
                        }
                }
            }
            else {
                if (matrix[currX][currY] < 4 && (!isSomebodyOnPosition(currX, currY))) {
                    for (int k = 0; k < pointNotToMove.size(); k++)
                        if (pointNotToMove.get(k).equals(new Point(currX, currY))) {
                            pointNotToMove.remove(k);
                            break;
                        }
                }
            }


//           else if (isSomebodyOnPosition(currX, currY))
//                pointNotToMove.remove(currX * dimension + currY );

            //pointToMove.add(pointToCheck[i]);

        }
        return pointNotToMove;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int getPrevX() {
        return prevX;
    }

    public int getPrevY() {
        return prevY;
    }

    public Player[] getPlayers() {
        return players;
    }

    public static int[][] copyMatrix(int[][] matrix) {
        int[][] newMatrix = new int[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            newMatrix[i] = new int[matrix.length];
            for (int j = 0; j < matrix.length; j++) {
                newMatrix[i][j] = matrix[i][j];
            }
        }
        return newMatrix;
    }

    public void saveToFile() {
        moveObserver.saveToFile();
    }

    public void loadFromFile(String filename) { moveObserver.loadFromFile(filename, controller);}

    public int getValidClicks() {
        return validClicks;
    }
}
