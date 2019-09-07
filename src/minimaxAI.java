import java.util.*;

public class minimaxAI implements CFPlayer {

    @Override
    public int nextMove(CFGame g) {
        //start
        int countmid=0;
        int[][] s = g.getState();
        List<Integer> start = new ArrayList<>();
        for (int y=0; y<6; y++) {
            if (s[3][y] != 0) {
                countmid++;
            }
        }


        //if dire
        for (int i = 0; i < 7; i++) {
            // prevent accumulation
            CFGame copyg = new CFGame();
            CFGame copyg_ = new CFGame();
            copyg.setState(g.getState());
            copyg_.setState(g.getState());

            //don't hesitate to end
            copyg.setRedTurn(false);
            copyg.play(i);
            if (copyg.isGameOver()) {
                return i;
            }

            //even if going to lose, play auto block
            copyg_.setRedTurn(true);
            copyg_.play(i);
            if (copyg_.isGameOver()) {
                return i;
            }
        }

        if (!g.colsPlayed.isEmpty()) {
            if (countmid==0 && g.colsPlayed.getLast()==1  && g.colsPlayed.size()==1) {
                return 2;
            } else if (countmid==0 && g.colsPlayed.getLast()==5  && g.colsPlayed.size()==1) {
                return 4;
            }
            if (countmid==1 || countmid==0 || (countmid ==3 && g.colsPlayed.size()==3 )) {
                return 3;
            }
        }

        //regular
        if (!g.isRedTurn) {
            long endTime = System.nanoTime();
            return (int) minimax(g, true, 4, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false).getSecond();
        } else {
            long endTime = System.nanoTime();
            return (int) minimax(g, false, 4, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false).getSecond();
        }
    }

    public Pair minimax(CFGame g, boolean maximizingPlayer, int n, double alpha, double beta, boolean old) {
        int[][] state = g.getState();
        //makes a copy of the board that represents "state"
        CFGame c_= new CFGame();
        c_.setState(state);
        //whose turn is it? since black is maximizingPlayer, red is the opposite
        c_.setRedTurn(!maximizingPlayer);
        //BASE CASES:
        //if the board state represents a finished game or if n==0 (last examined layer)
        if (c_.isGameOver() || n==0) {
            if (c_.isGameOver()) {
                if (c_.winner()==1) {
                    //black player lost
                    return new Pair(Double.NEGATIVE_INFINITY, g.colsPlayed.getLast());
                } else if (c_.winner()==-1) {
                    //black player won
                    return new Pair(Double.POSITIVE_INFINITY, g.colsPlayed.getLast());
                } else if (c_.winner()==0){
                    //draw
                    return new Pair(0.0, g.colsPlayed.getLast());
                }
            }
            //last examined layer
            if (n==0) {
                if (!old) {
                    return new Pair(g.evaluateState(), g.colsPlayed.getLast());
                } else {
                    return new Pair(g.oldEvaluateState(), g.colsPlayed.getLast());
                }
            }
        }
        //Black's POV
        if (maximizingPlayer) {
            //initially, assume the worst
            double maxVal = Double.NEGATIVE_INFINITY;
            //copy of game where it is black's turn
            CFGame game_ = new CFGame();
            game_.setRedTurn(false);
            int col=3;
            //first, pick the mid column, if nothing better is found
            //then this column will be played
            //now we see if there are any better moves
            for (int x = 0; x < 7; x++) {
                //we build the (up to) 7 sub-boards by playing each column on the original board
                CFGame game = new CFGame();
                game.setRedTurn(false);
                game.setState(c_.getState());
                //consider the move, if valid
                if (game.play(x)) {
                    //retrieve its score recursively
                    double currVal = (double) minimax(game, false, n-1, alpha, beta, old).getFirst();
                    if ((double) currVal>maxVal) {
                        maxVal= (double) currVal;
                        col = x;
                    }
                    //alpha beta pruning, if in the turn after, red (minimizing player) has a better option (beta),
                    //i.e. beta<=alpha, then we don't need to look at that subtree since black (maximizing) would
                    //never go down that path
                    alpha = Math.max(alpha, currVal);
                    if (beta<=alpha) {
                        break;
                    }
                }
            }
            if (maxVal>Double.NEGATIVE_INFINITY) {
                return new Pair(maxVal, col);
            } else {
                //if going to lose anyway, return random column
                boolean illegal=true;
                Random r = new Random();
                while (illegal) {
                    int newcol = r.nextInt(7);
                    if (c_.play(newcol)) {
                        return new Pair(maxVal, newcol);
                    }
                }
            }
        } else {
            //initially, assume the worst (for red), which means that Black wins
            double minVal = Double.POSITIVE_INFINITY;
            //copy of game where it is red's turn
            CFGame game_ = new CFGame();
            game_.setRedTurn(true);
            int col=3;
            //now we see if there are any better moves
            for (int x = 0; x < 7; x++) {
                //make a copy of the board
                CFGame game = new CFGame();
                game.setRedTurn(true);
                game.setState(c_.getState());
                //consider the move, if valid
                if (game.play(x)) {
                    //retrieve its score recursively
                    double currVal = (double) minimax(game, true, n-1, alpha, beta, old).getFirst();
                    if ((double) currVal<minVal) {
                        minVal= (double) currVal;
                        col = x;
                    }
                    beta = Math.min(beta, currVal);
                    if (beta<=alpha) {
                        break;
                    }
                }
            }
            if (minVal<Double.POSITIVE_INFINITY) {
                return new Pair(minVal, col);
            } else {
                //if you're going to lose anyway, just generate a random column
                boolean illegal=true;
                Random r = new Random();
                while (illegal) {
                    int newcol = r.nextInt(7);
                    if (c_.play(newcol)) {
                        return new Pair(minVal, newcol);
                    }
                }
            }
        }
        return new Pair(0,0);
    }

    @Override
    public String getName() {
        return "minimaxAI";
    }
}
