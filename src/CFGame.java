import java.util.*;

public class CFGame {

    //state[i][j]= 0 means that column i, row j is empty
    //state[i][j]= 1 means the i,j slot is filled by red
    //state[i][j]=-1 means the i,j slot is filled by black
    protected int[][] state;
    protected boolean isRedTurn;
    //linked list stores the history of columns played in the game (for undo feature)
    protected LinkedList<Integer> colsPlayed;
    //these sets represent the empty positions on the board in which a red or black tile
    //would end the game
    Set<Pair>blackthreatspots;
    Set<Pair> redthreatspots;

    //default constructor
    public CFGame() {
        state = new int[7][6];
        for (int i=0; i<7; i++)
            for (int j=0; j<6; j++)
                state[i][j] = 0;
        isRedTurn = true; //red goes first
        blackthreatspots = new HashSet<>();
        redthreatspots = new HashSet<>();
        colsPlayed = new LinkedList<>();
    }

    public Set<Pair> getBlackthreatspots() {
        return blackthreatspots;
    }

    public Set<Pair> getRedthreatspots() {
        return redthreatspots;
    }

    public void addBlackthreatspot(Pair p) {
        blackthreatspots.add(p);
    }

    public void addRedthreatspot(Pair p) {
        redthreatspots.add(p);
    }

    //return the board state
    public int[][] getState() {
        int[][] ret_arr = new int[7][6];
        for (int i=0; i<7; i++)
            for (int j=0; j<6; j++)
                ret_arr[i][j] = state[i][j];
        return ret_arr;
    }

    public void setState(int[][] state) {
        this.state= state;
    }

    public boolean isRedTurn() {
        return isRedTurn;
    }

    public void setRedTurn(boolean b) {
        isRedTurn = b;
    }

    public boolean playable(int column) {
        //checks if the column is playable without changing the state
        for (int j=0;j<6;j++){
            if (state[column][j]==0) {
                return true;
            }
        }
        return false;
    }

    //play the chip in the desired column
    public boolean play(int column) {
        //check if there are zeros from bottom up
        for (int j=0;j<6;j++){
            if (state[column][j]==0){
                if (isRedTurn){
                    state[column][j]=1;
                    isRedTurn = false;
                    colsPlayed.add(column);
                    return true;
                }
                else {
                    state[column][j]=-1;
                    isRedTurn = true;
                    colsPlayed.add(column);
                    return true;
                }
            }
        }
        return false;
    }


    public int winner() {
        //we need to check for four in a row horizontally, vertically, and diagonally for both players

        List<Integer> players = new ArrayList <Integer>();
        players.add(1);
        players.add(-1);

        for (int p: players) {

            //loop for horizontal check
            for (int i=0;i<=3;i++){
                for (int j=0;j<6;j++){
                    if (this.hCheck(p, i, j)){
                        return p;
                    }
                }
            }

            //loop for vertical  check
            for (int i=0;i<7;i++){
                for (int j=0;j<=2;j++){
                    if (this.vCheck(p, i, j)){
                        return p;
                    }
                }
            }

            //loop for rightward diagonal check
            for (int i=0;i<4;i++){
                for (int j=0;j<=2;j++){
                    if (this.RdCheck (p,i,j)){
                        return p;
                    }
                }
            }

            //loop for leftward diagonal check
            for (int i=6;i>=3;i--){
                for (int j=0;j<=2;j++){
                    if (this.LdCheck (p,i,j)){
                        return p;
                    }
                }
            }
        }
        return 0;
    }



    public boolean isGameOver() {
        //no player has 4 in a row yet
        if (this.winner()!=0) {
            return true;
        }

        //check for draw
        List<int[]> list1 = Arrays.asList(state);
        List<Integer> list2 = new ArrayList<>();

        for (int[] x : list1) {
            for (int i : x) {
                list2.add(Integer.valueOf(i));
            }
        }

        if (list2.contains(0)) {
            return false;
        } else {
            return true;
        }
    }



    //check for 4 in a row including and to the right of (i,j)
    public boolean hCheck ( int p, int i, int j){
        if (state[i][j] == p && state[i + 1][j] == p && state[i + 2][j] == p && state[i + 3][j] == p) {
            return true;
        } else {
            return false;
        }
    }

    //checks if there are 3 in a row with one empty slot
    //adds empty spot to threatspots if applicable
    public boolean hCheck3 (int[][] state, int p, int i, int j, CFGame g){
        int count=0;
        int zeros=0;
        int zeroCol=0;
        for (int x=i; x<i+4;x++) {
            if (state[x][j]==p) {
                count++;
            }
            if (state[x][j]==0) {
                zeros++;
                zeroCol=x;
            }
        }
        if (count==3 && zeros==1) {
            if (p==1) {
                g.addRedthreatspot(new Pair(zeroCol, j));;
            } else {
                g.addBlackthreatspot(new Pair(zeroCol, j));
            }
            return true;
        } else {
            return false;
        }
    }


    //check for 4 in a row including and to above (i,j)
    public boolean vCheck (int p, int i, int j){
        if (state[i][j] == p && state[i][j + 1] == p && state[i][j + 2] == p && state[i][j + 3] == p) {
            return true;
        } else {
            return false;
        }
    }

    //checks if there are 3 in a row with one empty slot
    //adds empty spot to threatspots if applicable
    public boolean vCheck3 (int[][] state, int p, int i, int j, CFGame g){
        int count=0;
        int zeros=0;
        int zeroRow=0;
        for (int y=j; y<j+4;y++) {
            if (state[i][y]==p) {
                count++;
            }
            if (state[i][y]==0) {
                zeroRow=y;
                zeros++;
            }
        }
        if (count==3 && zeros==1) {
            if (p==1) {
                g.addRedthreatspot(new Pair(i, zeroRow));
            } else {
                g.addBlackthreatspot(new Pair(i, zeroRow));
            }
            return true;
        } else {
            return false;
        }
    }

    //check for 4 in a row in the right diagonal beginning at (i,j)
    public boolean RdCheck ( int p, int i, int j) {
        if (state[i][j] == p && state[i + 1][j + 1] == p && state[i + 2][j + 2] == p && state[i + 3][j + 3] == p) {
            return true;
        } else {
            return false;
        }
    }

    //checks if there are 3 in a row with one empty slot
    //adds empty spot to threatspots if applicable
    public boolean RdCheck3 (int[][] state, int p, int i, int j, CFGame g) {
        int count=0;
        int zeros=0;
        int zeroCol=0;
        int zeroRow=0;
        for (int x=i; x<i+4;x++) {
            for (int y=j; y<j+4;y++) {
                if (state[x][y]==p) {
                    count++;
                }
                if (state[x][y]==0) {
                    zeroCol=x;
                    zeroRow=y;
                    zeros++;
                }
            }
        }
        if (count==3 && zeros==1) {
            if (p==1) {
                g.addRedthreatspot(new Pair(zeroCol, zeroRow));
            } else {
                g.addBlackthreatspot(new Pair(zeroCol, zeroRow));
            }
            return true;
        } else {
            return false;
        }
    }

    //check for 4 in a row in the left diagonal beginning at (i,j)
    public boolean LdCheck ( int p, int i, int j) {
        if (state[i][j] == p && state[i - 1][j + 1] == p && state[i - 2][j + 2] == p && state[i - 3][j + 3] == p) {
            return true;
        } else {
            return false;
        }
    }

    //checks if there are 3 in a row with one empty slot
    //adds empty spot to threatspots if applicable
    public boolean LdCheck3 (int[][] state, int p, int i, int j, CFGame g) {
        int count=0;
        int zeros=0;
        int zeroCol=0;
        int zeroRow=0;
        for (int x=i; x>i-4;x--) {
            for (int y=j; y<j+4;y++) {
                if (state[x][y]==p) {
                    count++;
                }
                if (state[x][y]==0) {
                    zeroCol=x;
                    zeroRow=y;
                    zeros++;
                }
            }
        }
        if (count==3 && zeros==1) {
            if (p==1) {
                g.addRedthreatspot(new Pair(zeroCol, zeroRow));
            } else {
                g.addBlackthreatspot(new Pair(zeroCol, zeroRow));
            }
            return true;
        } else {
            return false;
        }
    }

    //this method is only called from the minimaxAI class for returning its next move, but I put it here
    //because it's easier to access all the internal fields of CFGame
    //we assume that the maximizingPlayer is black. i.e. black tries to increase the score,
    //red tries to decrease it. if the score is high, then black is winning.

    //returns a pair, first coordinate is the score of the move, second coordinate is the column to be played
    public Pair minimax(int[][] state, boolean maximizingPlayer, int n, double alpha, double beta, boolean old) {
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
                    return new Pair(Double.NEGATIVE_INFINITY, colsPlayed.getLast());
                } else if (c_.winner()==-1) {
                    //black player won
                    return new Pair(Double.POSITIVE_INFINITY, colsPlayed.getLast());
                } else if (c_.winner()==0){
                    //draw
                    return new Pair(0.0, colsPlayed.getLast());
                }
            }
            //last examined layer
            if (n==0) {
                if (!old) {
                    return new Pair(evaluateState(state), colsPlayed.getLast());
                } else {
                    return new Pair(oldEvaluateState(state), colsPlayed.getLast());
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
                    double currVal = (double) minimax(game.getState(), false, n-1, alpha, beta, old).getFirst();
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
                    if (c_.playable(newcol)) {
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
                    double currVal = (double) minimax(game.getState(), true, n-1, alpha, beta, old).getFirst();
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
                    if (c_.playable(newcol)) {
                        return new Pair(minVal, newcol);
                    }
                }
            }
        }
        return new Pair(0,0);
    }



    public double evaluateState(int[][] state) {
        //some noise between 0-1
        double score=Math.random();
        int[][] s = this.getState();
        CFGame g = new CFGame();
        g.setState(state);
        //check for threats of 3
        List<Integer> players = new ArrayList <Integer>();
        players.add(1);
        players.add(-1);
        for (int p: players) {
            int numThreats=0;
            int numChecks=0;

            //award positioning points accordingly (center spots are valued)
            for (int j=0; j<4;j++) {
                for (int i=2; i<=4; i++) {
                    if (state[i][j]==p) {
                        if (p==1) {
                            score=score-50;
                            if (j==2) {
                                score=score-50;
                            }
                        } else {
                            score = score+50;
                        }
                    }
                }
            }

            //loop for horizontal check
            for (int i = 0; i <= 3; i++) {
                for (int j = 0; j < 6; j++) {
                    if (this.hCheck3(state, p, i, j, g)) {
                        numChecks=numChecks+1;
                        if (i==2 && j==2) {
                            numChecks=numChecks+1;
                        }
                    }
                }
            }

            //loop for vertical  check
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j <= 2; j++) {
                    if (this.vCheck3(state, p, i, j, g)) {
                        numChecks++;
                    }
                }
            }

            //loop for rightward diagonal check
            for (int i = 0; i <= 3; i++) {
                for (int j = 0; j <= 2; j++) {
                    if (this.RdCheck3(state, p, i, j, g)) {
                        numChecks++;
                    }
                }
            }

            //loop for leftward diagonal check
            for (int i = 6; i >= 3; i--) {
                for (int j = 0; j <= 2; j++) {
                    if (this.LdCheck3(state, p, i, j, g)) {
                        numChecks++;
                    }
                }
            }


            if (p==1) {
                score=score-500*numChecks;
            } else {
                score=score+500*numChecks;
            }
        }



        //a threat spot is a unique spot on the board in which one more tile will result in a win
        //ex. if red has 3 threat spots, then there are 3 currently empty spots on the board that would
        //imply a red win if red gets a tile in that spot

        score = score + 600*g.getBlackthreatspots().size();
        score = score - 600*g.getRedthreatspots().size();

        //particularly dangerous is when you have multiple threat spots in a given column

        List<Integer> threatcols = new ArrayList<>();
        for (Pair p: g.getRedthreatspots()) {
            if (!threatcols.contains(p.getFirst())) {
                threatcols.add((Integer) p.getFirst());
            } else {
                score=score-600;
            }
        }

        List<Integer> threatcolsblack = new ArrayList<>();
        for (Pair p: g.getBlackthreatspots()) {
            if (!threatcolsblack.contains(p.getFirst())) {
                threatcolsblack.add((Integer) p.getFirst());
            } else {
                score=score+600;
            }
        }

        return score;
    }


    public double oldEvaluateState(int[][] state) {
        double score=Math.random();
        return score;
    }

    /*
    public double checkPins(int[][] state) {
        //start off with score 0
        double score = 0;
        //create a copy of the board
        CFGame c__ = new CFGame();
        c__.setState(state);


        for (int x=0; x<7; x++) {
            //for each iteration, make a copy of the board
            CFGame c = new CFGame();
            c.setState(state);
            c.setRedTurn(true);
            c__.setRedTurn(true);
            //red plays column x
            if (c.play(x)) {
                if (c.isGameOver() && c.winner()==1) {
                    //red is one move away from winning
                    score=score-5000;
                    c__.setRedTurn(false);
                    //what if black plays red's winning move?
                    c__.play(x);
                    //if red can still win by playing the same column
                    //then that's a double pin, award 10 times the amount of points
                    if (c__.play(x)&& c.winner()==1 ) {
                        score = score-100000;
                    }
                }
                //black plays same column as red's last turn and wins
                if (c.play(x)) {
                    if (c.isGameOver() && c.winner()==-1) {
                        score=score+10000;
                    }
                }
            }
        }

        //symmetric procedure
        for (int x=0; x<7; x++) {
            CFGame c = new CFGame();
            c.setState(state);
            c.setRedTurn(false);
            //black plays column x
            if (c.play(x)) {
                if (c.isGameOver() && c.winner()==-1) {
                    score=score+5000;
                    c__.setRedTurn(true);
                    //red prevents
                    c__.play(x);
                    if (c__.play(x)&& c.winner()==-1 ) {
                        score = score+100000;
                    }
                }
                //red plays the same column
                if (c.play(x)) {
                    if (c.isGameOver() && c.winner()==1) {
                        score=score-10000;
                    }
                }
            }
        }
        return score;
    }
*/
}



