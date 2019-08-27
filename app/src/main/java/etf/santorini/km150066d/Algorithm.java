package etf.santorini.km150066d;

import java.util.ArrayList;

public abstract class Algorithm {
    public abstract Decision algorithm(int[][] matrix, Player[] players, int turn, int levelsToInspect, boolean isMax);

    public static ArrayList<Point> findNeighbours(boolean move, int[][] matrix, int turn, int figure, Player p1, Player p2) {
        Point current = null;
        if (turn == 0)
            current = p1.getFigurePoint(figure);
        else
            current = p2.getFigurePoint(figure);
        //Point current = players[turn].getFigurePoint(figureChosen);
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
                if (currX < 0 || currX >= matrix.length || currY < 0 || currY >= matrix.length || matrix[currX][currY] == 4 || (matrix[currX][currY] - matrix[x][y] > 1))
                    continue;
            }
            else { //find neighbours to build
                if (currX < 0 || currX >= matrix.length || currY < 0 || currY >= matrix.length || matrix[currX][currY] == 4)
                    continue;
            }


            if (Algorithm.isSomebodyOnPosition(new Point(currX, currY), p1, p2))
                continue;

            pointToMove.add(pointToCheck[i]);

        }
        return pointToMove;
    }

    public static Decision getNextDecision(int[][] matrix, Player[] players, int turn) {
        ArrayList<Point> neighboursToMove = null;
        ArrayList<Point> neigboursToBuild = null;
        ArrayList<Decision> decisions = new ArrayList<Decision>();

        //for figure 0
        neighboursToMove = findNeighbours(true, matrix, turn, 0, players[0], players[1]);
        Point prevPoint = players[turn].getFigurePoint(0);
        int sizeN = neighboursToMove.size();
        if (sizeN > 1)
            sizeN = 1;
        for (int i = 0; i < sizeN; i++) {
            players[turn].setFigurePoint(0, neighboursToMove.get(i).getX(), neighboursToMove.get(i).getY());
            neigboursToBuild = findNeighbours(false, matrix, turn, 0, players[0], players[1]);
            int sizeB = neigboursToBuild.size();
            if (sizeB > 1)
                sizeB = 1;
            for (int j = 0; j < sizeB; j++) {
                Point point = neigboursToBuild.get(j);
                matrix[point.getX()][point.getY()]++;
                decisions.add(new Decision(neighboursToMove.get(i), neigboursToBuild.get(j), 0));
                matrix[point.getX()][point.getY()]--;
            }

        }
        players[turn].setFigurePoint(0, prevPoint.getX(), prevPoint.getY());

        return decisions.get(0);
    }

    public static boolean isSomebodyOnPosition(Point p, Player p1, Player p2) {
        Point[] points1 = p1.getFigurePoints();
        Point[] points2 = p2.getFigurePoints();

        for (int i = 0; i < points1.length; i++)
            if (p.equals(points1[i]))
                return true;

        for (int i = 0; i < points2.length; i++)
            if (p.equals(points2[i]))
                return true;

        return false;

    }

    public static int findManhattanDinstanceSum(Point point, Player player) {
        int distance = 0;
        for (int i = 0; i < player.getFigurePoints().length; i++) {
            Point figurePoint = player.getFigurePoint(i);
            distance += Math.abs(point.getX() - figurePoint.getX()) + Math.abs(point.getY() - figurePoint.getY());
        }

        return distance;
    }

    public static int findDistanceSum(Point point, Player player) {
        int distance = 0;
        for (int i = 0; i < player.getFigurePoints().length; i++) {
            Point figurePoint = player.getFigurePoint(i);
            distance += Math.max(Math.abs(point.getX() - figurePoint.getX()), Math.abs(point.getY() - figurePoint.getY()));
        }

        return distance;
    }
}
