package etf.santorini.km150066d;

import java.util.ArrayList;

public class StepByStepResolver {
    private ArrayList<Point> movePoints;
    private ArrayList<ArrayList<Point>> buildPoints;
    private ArrayList<ArrayList<Integer>> functionValues;

    public StepByStepResolver() {
        movePoints = new ArrayList<>();
        buildPoints = new ArrayList<>();
        functionValues = new ArrayList<>();
    }

    public void add(Point movePoint, Point buildPoint, int functionValue) {
        int size = movePoints.size();

        int i;
        for (i = 0; i < size; i++) {
            Point currentPoint = movePoints.get(i);
            if (currentPoint.equals(movePoint)) {
                ArrayList listOfBuildPoints = buildPoints.get(i);
                ArrayList listOfValues = functionValues.get(i);
                if (listOfBuildPoints == null) {
                    buildPoints.add(new ArrayList<Point>());
                    listOfBuildPoints = buildPoints.get(i);
                    functionValues.add(new ArrayList<Integer>());
                    listOfValues = functionValues.get(i);
                }
                listOfBuildPoints.add(buildPoint);
                listOfValues.add(functionValue);
                break;
            }
        }

        if (i == size) {
            movePoints.add(movePoint);
            buildPoints.add(new ArrayList<Point>());
            functionValues.add(new ArrayList<Integer>());
            buildPoints.get(buildPoints.size() - 1).add(buildPoint);
            functionValues.get(buildPoints.size() - 1).add(functionValue);
        }

    }

    public ArrayList<Decision> getBestBuildDecisionsForEveryMove() {
        ArrayList<Decision> bestDecisions = new ArrayList<>();
        int size = movePoints.size();

        for (int i = 0; i < size; i++) {
            Point movePoint = movePoints.get(i);
            ArrayList<Point> builds = buildPoints.get(i);
            ArrayList<Integer> vals = functionValues.get(i);
            int sizeBuild = builds.size();
            if (sizeBuild == 0)
                continue;
            int bestVal = vals.get(0);
            Point bestBuild = builds.get(0);
            for (int j = 1; j < sizeBuild; j++) {
                if (vals.get(j) > bestVal) {
                    bestVal = vals.get(j);
                    bestBuild = builds.get(j);
                }
            }
            bestDecisions.add(new Decision(movePoint, bestBuild, 0, bestVal)); //nije bitno koja je figura u pitanju
        }

        return bestDecisions;
    }

    public ArrayList<Decision> getBestBuildDecisionsForMove(Point movePoint) {
        ArrayList<Decision> bestDecisions = new ArrayList<>();

        int size = movePoints.size();
        for (int i = 0; i < size; i++) {
            Point currentPoint = movePoints.get(i);
            if (currentPoint.equals(movePoint)) {
                ArrayList<Point> builds = buildPoints.get(i);
                ArrayList<Integer> vals = functionValues.get(i);
                int sizeBuild = builds.size();
                for (int j = 0; j < sizeBuild; j++)
                    bestDecisions.add(new Decision(currentPoint, builds.get(j), 0 , vals.get(j))); // nije bitna figura
                return bestDecisions;
            }
        }

        return bestDecisions;
    }


}
