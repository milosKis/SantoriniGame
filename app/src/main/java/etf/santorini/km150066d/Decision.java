package etf.santorini.km150066d;

public class Decision {
    private Point movePoint;
    private Point buildPoint;
    private int figure;
    private int functionValue;
    private StepByStepResolver resolver;


    public Decision(Point movePoint, Point buildPoint, int figure) {
        this.movePoint = movePoint;
        this.buildPoint = buildPoint;
        this.figure = figure;
    }

    public Decision(Point movePoint, Point buildPoint, int figure, int functionValue) {
        this.movePoint = movePoint;
        this.buildPoint = buildPoint;
        this.figure = figure;
        this.functionValue = functionValue;
    }

    public Point getMovePoint() {
        return movePoint;
    }

    public void setMovePoint(Point movePoint) {
        this.movePoint = movePoint;
    }

    public Point getBuildPoint() {
        return buildPoint;
    }

    public void setBuildPoint(Point buildPoint) {
        this.buildPoint = buildPoint;
    }

    public int getFigure() {
        return figure;
    }

    public void setFigure(int figure) {
        this.figure = figure;
    }

    public int getFunctionValue() {
        return functionValue;
    }

    public void setFunctionValue(int functionValue) {
        this.functionValue = functionValue;
    }

    public StepByStepResolver getResolver() {
        return resolver;
    }

    public void setResolver(StepByStepResolver resolver) {
        this.resolver = resolver;
    }
}
