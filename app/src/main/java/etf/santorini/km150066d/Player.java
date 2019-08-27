package etf.santorini.km150066d;

public class Player {
    private static final int NUM_OF_FIGURES = 2;
    private Point[] figurePoints;

    public Player() {
        figurePoints = new Point[NUM_OF_FIGURES];
    }

    public void setFigurePoint(int figure, int x, int y) {
        if (figure < 0 || figure >= NUM_OF_FIGURES)
            return;

        figurePoints[figure] = new Point(x, y);
    }

    public Point getFigurePoint(int figure) {
        if (figure < 0 || figure >= NUM_OF_FIGURES)
            return null;

        return figurePoints[figure];
    }

    public Point[] getFigurePoints() {
        return figurePoints;
    }

    public void setFigurePoints(Point[] figurePoints) {
        this.figurePoints = figurePoints;
    }

    public static Player copyPlayer(Player p) {
        Player newPlayer = new Player();
        newPlayer.setFigurePoint(0, p.getFigurePoint(0).getX(), p.getFigurePoint(0).getY());
        newPlayer.setFigurePoint(1, p.getFigurePoint(1).getX(), p.getFigurePoint(1).getY());

        return newPlayer;
    }
}
