package etf.santorini.km150066d;

import java.util.ArrayList;

public class AlphaBetaAlgorithm extends Algorithm {

    @Override
    public Decision algorithm(int[][] matrix, Player[] players, int turn, int levelsToInspect, boolean isMax) {
        return algorithm(matrix, players, turn, levelsToInspect, isMax, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public Decision algorithm(int[][] matrix, Player[] players, int turn, int levelsToInspect, boolean isMax, int alpha, int beta) {
        ArrayList<Point> neighboursToMove = null;
        ArrayList<Point> neigboursToBuild = null;

//        if (levelsToInspect % 2 == 0)
//            isMax = true;
//        else
//            isMax = false;

        int func = 0;
        if (isMax)
            func = Integer.MIN_VALUE;
        else
            func = Integer.MAX_VALUE;

        Decision bestDecision = null;

        StepByStepResolver resolverZero = new StepByStepResolver();
        StepByStepResolver resolverOne = new StepByStepResolver();

        //for figure 0 and figure 2
        for (int k = 0; k < 2; k++) {
            neighboursToMove = Algorithm.findNeighbours(true, matrix, turn, k, players[0], players[1]);
            Point prevPoint = players[turn].getFigurePoint(k);
            int sizeN = neighboursToMove.size();
            for (int i = 0; i < sizeN; i++) {
                players[turn].setFigurePoint(k, neighboursToMove.get(i).getX(), neighboursToMove.get(i).getY());
                neigboursToBuild = Algorithm.findNeighbours(false, matrix, turn, k, players[0], players[1]);
                Point movePoint = neighboursToMove.get(i);
                int sizeB = neigboursToBuild.size();
                int bestVal;
                if (isMax)
                    bestVal = Integer.MIN_VALUE;
                else
                    bestVal = Integer.MAX_VALUE;
                for (int j = 0; j < sizeB; j++) {
                    Point buildPoint = neigboursToBuild.get(j);
                    matrix[buildPoint.getX()][buildPoint.getY()]++;
                    Decision decision = null;
                    if (levelsToInspect > 0)
                        decision = algorithm(matrix, players , (turn + 1) % 2, levelsToInspect - 1, !isMax, alpha, beta);
                    else
                        decision = new Decision(movePoint, buildPoint, k);
                    matrix[buildPoint.getX()][buildPoint.getY()]--;

                    if (decision != null) {
                        int currentFunc = 0;
                        if (levelsToInspect == 0) {
                            players[turn].setFigurePoint(k, prevPoint.getX(), prevPoint.getY());
                            currentFunc = matrix[movePoint.getX()][movePoint.getY()] + matrix[buildPoint.getX()][buildPoint.getY()] *
                                    (Algorithm.findManhattanDinstanceSum(buildPoint, players[turn]) - Algorithm.findManhattanDinstanceSum(buildPoint, players[(turn + 1) % 2])); //zamenjeni indeksi
                            players[turn].setFigurePoint(k, neighboursToMove.get(i).getX(), neighboursToMove.get(i).getY());
                        }
                        else {
                            currentFunc = decision.getFunctionValue();
                            if (isMax) {
                                bestVal = Math.max(bestVal, currentFunc);
                                alpha = Math.max(alpha, bestVal);
                            }
                            else {
                                bestVal = Math.min(bestVal, currentFunc);
                                beta = Math.min(beta, bestVal);
                            }

                            if (beta <= alpha) {
                                break;
                            }
                        }

                        decision.setFunctionValue(currentFunc);

                        if (((isMax) && (currentFunc > func)) || ((!isMax) && (currentFunc < func))) {
                            func = currentFunc;
                            bestDecision = new Decision(movePoint, buildPoint, k);
                            bestDecision.setFunctionValue(func);
                        }

                        if (k == 0) {
                            resolverZero.add(movePoint, buildPoint, currentFunc);
                        }
                        else {
                            resolverOne.add(movePoint, buildPoint, currentFunc);
                        }
                    }
                }

            }
            players[turn].setFigurePoint(k, prevPoint.getX(), prevPoint.getY());
        }


        if (bestDecision != null) {
            if (bestDecision.getFigure() == 0)
                bestDecision.setResolver(resolverZero);
            else
                bestDecision.setResolver(resolverOne);
        }

        return bestDecision;
    }
}
