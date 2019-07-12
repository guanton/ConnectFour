import org.omg.CORBA.INTERNAL;

import java.lang.reflect.Method;
import java.util.*;

public class CFGame {
    //state[i][j]= 0 means the i,j slot is empty
    //state[i][j]= 1 means the i,j slot is filled by red
    //state[i][j]=-1 means the i,j slot is filled by black
    protected int[][] state;
    protected boolean isRedTurn;
    protected int lastColPlayed;
    Set<Pair>blackthreatspots;
    Set<Pair> redthreatspots;

    public CFGame() {
        state = new int[7][6];
        for (int i=0; i<7; i++)
            for (int j=0; j<6; j++)
                state[i][j] = 0;
        isRedTurn = true; //red goes first
        blackthreatspots = new HashSet<>();
        redthreatspots = new HashSet<>();
    }

    public Set<Pair> getBlackthreatspots() {
        return blackthreatspots;
    }

    public Set<Pair> getRedthreatspots() {
        return redthreatspots;
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

    public boolean playable(int column) {

        for (int j=0;j<6;j++){
            if (state[column][j]==0) {
                return true;
            }
        }
        return false;
    }

    //play the chip in the desired column
    public boolean play(int column) {

        for (int j=0;j<6;j++){
            if (state[column][j]==0){
                if (isRedTurn){
                    state[column][j]=1;
                    isRedTurn = false;
                    lastColPlayed = column;
                    return true;
                }
                else {
                    state[column][j]=-1;
                    isRedTurn = true;
                    lastColPlayed = column;
                    return true;
                }
            }
        }
        return false;
    }

    public void setRedTurn(boolean b) {
        isRedTurn = b;
    }

    public boolean isGameOver() {
        // we need to check for 4 in a row for rows, columns, and diagonals

        //first iteration: for point 0,0, player 1 (red)

        List<Integer> players = new ArrayList<Integer>();
        players.add(1);
        players.add(-1);

        for (int p : players) {
            //loop for horizontal check
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 6; j++) {
                    if (this.hCheck(p, i, j)) {
                        return true;
                    }
                }
            }
            //loop for vertical  check
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j <= 2; j++) {
                    if (this.vCheck(p, i, j)) {
                        return true;
                    }
                }
            }

            //loop for rightward diagonal check
            for (int i = 0; i <= 3; i++) {
                for (int j = 0; j <= 2; j++) {
                    if (this.RdCheck(p, i, j)) {
                        return true;
                    }
                }
            }

            //loop for leftward diagonal check
            for (int i = 6; i >= 3; i--) {
                for (int j = 0; j <= 2; j++) {
                    if (this.LdCheck(p, i, j)) {
                        return true;
                    }
                }
            }


        }

        //draw
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

    //check functions
    public boolean hCheck ( int p, int i, int j){
        if (state[i][j] == p && state[i + 1][j] == p && state[i + 2][j] == p && state[i + 3][j] == p) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hCheck3 (int[][] state, int p, int i, int j, CFGame g){
        int count=0;
        int zeros=0;
        int zeroCol=0;
        for (int x=i; x<i+4;x++) {
            if (state[x][j]==p) {
                count++;
            } else if (state[x][j]==0) {
                zeros++;
                zeroCol=x;
            }
        }
        if (count==3 && zeros==1) {
            if (p==1) {
                g.getRedthreatspots().add(new Pair(zeroCol, j));
            } else {
                g.getBlackthreatspots().add(new Pair(zeroCol, j));
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean vCheck (int p, int i, int j){
        if (state[i][j] == p && state[i][j + 1] == p && state[i][j + 2] == p && state[i][j + 3] == p) {
            return true;
        } else {
            return false;
        }
    }

    public boolean vCheck3 (int[][] state, int p, int i, int j, CFGame g){
        int count=0;
        int zeros=0;
        int zeroRow=0;
        for (int y=j; y<j+4;y++) {
            if (state[i][y]==p) {
                count++;
            } else if (state[i][y]==0) {
                zeroRow=y;
                zeros++;
            }
        }
        if (count==3 && zeros==1) {
            if (p==1) {
                g.getRedthreatspots().add(new Pair(i,zeroRow));
            } else {
                g.getBlackthreatspots().add(new Pair(i, zeroRow));
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean RdCheck ( int p, int i, int j) {
        if (state[i][j] == p && state[i + 1][j + 1] == p && state[i + 2][j + 2] == p && state[i + 3][j + 3] == p) {
            return true;
        } else {
            return false;
        }
    }

    public boolean RdCheck3 (int[][] state, int p, int i, int j, CFGame g) {
        int count=0;
        int zeros=0;
        int zeroCol=0;
        int zeroRow=0;
        for (int x=i; x<i+4;x++) {
            for (int y=j; y<j+4;y++) {
                if (state[x][y]==p) {
                    count++;
                } else if (state[x][y]==0) {
                    zeroCol=x;
                    zeroRow=y;
                    zeros++;
                }
            }
        }
        if (count==3 && zeros==1) {
            if (p==1) {
                g.getRedthreatspots().add(new Pair(zeroCol, zeroRow));
            } else {
                g.getBlackthreatspots().add(new Pair(zeroCol, zeroRow));
            }
            return true;
        } else {
            return false;
        }
    }



    public boolean LdCheck ( int p, int i, int j) {
        if (state[i][j] == p && state[i - 1][j + 1] == p && state[i - 2][j + 2] == p && state[i - 3][j + 3] == p) {
            return true;
        } else {
            return false;
        }
    }

    public boolean LdCheck3 (int[][] state, int p, int i, int j, CFGame g) {
        int count=0;
        int zeros=0;
        int zeroCol=0;
        int zeroRow=0;
        for (int x=i; x>i-4;x--) {
            for (int y=j; y<j+4;y++) {
                if (state[x][y]==p) {
                    count++;
                } else if (state[x][y]==0) {
                    zeroCol=x;
                    zeroRow=y;
                    zeros++;
                }
            }
        }
        if (count==3 && zeros==1) {
            if (p==1) {
                g.getRedthreatspots().add(new Pair(zeroCol, zeroRow));
            } else {
                g.getBlackthreatspots().add(new Pair(zeroCol, zeroRow));
            }
            return true;
        } else {
            return false;
        }
    }

    //assume this is only called if the game is over
    public int winner() {

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

    //returns a pair, first coordinate is the score of the move, second coordinate is the column played
    public List<Number> minimax(int[][] state, boolean maximizingPlayer, int n, double alpha, double beta, boolean old) {
        //make copy of the board that represents "state"
        CFGame c = new CFGame();
        CFGame c_= new CFGame();
        c.setState(state);
        c_.setState(state);
        //whose turn is it? since black is maximizingPlayer, red is the opposite
        c.setRedTurn(!maximizingPlayer);
        c_.setRedTurn(!maximizingPlayer);
        //if the board state represents a finished game or if n==0 (BASE CASE)
        if (c.isGameOver() || n==0) {
            if (c.isGameOver()) {
                if (c.winner()==1) {
                    //black player lost
                    Number[] arr = {Double.NEGATIVE_INFINITY, lastColPlayed};
                    return Arrays.asList(arr);
                } else if (c.winner()==-1) {
                    //black player won
                    Number[] arr = {Double.POSITIVE_INFINITY, lastColPlayed};
                    return Arrays.asList(arr);
                } else if (c.winner()==0){
                    Number[] arr = {0.001, lastColPlayed};
                    return Arrays.asList(arr);
                }
            }
            //BASE CASE
            if (n==0) {
                if (!old) {
                    Number[] arr = {evaluateState(state), lastColPlayed};
                    return Arrays.asList(arr);
                } else {
                    Number[] arr = {oldEvaluateState(state), lastColPlayed};
                    return Arrays.asList(arr);
                }
            }
        }
        if (maximizingPlayer) {
            //initially, assume the worst
            double maxVal = Double.NEGATIVE_INFINITY;
            //copy of game where it is black's turn
            CFGame game_ = new CFGame();
            game_.setRedTurn(false);
            int col=3;
            //first, pick a random column, if nothing better is found
            //then this column will be played
            //now we see if there are any better moves
            for (int x = 0; x < 7; x++) {
                //make a copy of the board
                CFGame game = new CFGame();
                game.setRedTurn(false);
                game.setState(c.getState());
                //consider the move, if valid
                if (game.play(x)) {
                    //retrieve its score recursively
                    double currVal = (double) minimax(game.getState(), false, n-1, alpha, beta, old).get(0);
                    if ((double) currVal>maxVal) {
                        maxVal= (double) currVal;
                        col = x;
                    }
                    alpha = Math.max(alpha, currVal);
                    if (beta<=alpha) {
                        break;
                    }
                }
            }
            if (c_.playable(col)) {
                Number[] arr = {maxVal, col};
                return Arrays.asList(arr);
            } else {
                boolean illegal=true;
                Random r = new Random();
                while (illegal) {
                    int newcol = r.nextInt(7);
                    if (c_.playable(newcol)) {
                        Number[] arr = {maxVal, newcol};
                        return Arrays.asList(arr);
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
                game.setState(c.getState());
                //consider the move, if valid
                if (game.play(x)) {
                    //retrieve its score recursively
                    double currVal = (double) minimax(game.getState(), true, n-1, alpha, beta, old).get(0);
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
            if (c_.playable(col)) {
                Number[] arr = {minVal, col};
                return Arrays.asList(arr);
            } else {
                boolean illegal=true;
                Random r = new Random();
                while (illegal) {
                    int newcol = r.nextInt(7);
                    if (c_.playable(newcol)) {
                        Number[] arr = {minVal, newcol};
                        return Arrays.asList(arr);
                    }
                }
            }
        }
        Number[] arr = {0, 0};
        return Arrays.asList(arr);
    }

    public double evaluateState(int[][] state) {
        //some noise between 0-1
        double score=Math.random();
        CFGame c__= new CFGame();

        //if red's move allows black to win on the next turn, add 1000 points
        for (int x=0; x<7; x++) {
            CFGame c = new CFGame();
            CFGame c_=new CFGame();
            c.setState(state);
            c.setRedTurn(true);
            c_.setState(state);
            c_.setRedTurn(true);
            //red plays column x
            if (c.play(x)) {
                if (c.isGameOver()) {
                    //red can win
                    if (c.winner()==1) {
                        score = score-1000000;
                    }
                }
                //black plays same column and wins the game
                if (c_.play(x)) {
                    if (c_.isGameOver() && c_.winner()==-1) {
                        score=score+10000;
                    }
                }
            }
        }
        //if black's move allows red to win on the next turn, deduct 1000 points
        for (int x=0; x<7; x++) {
            CFGame c = new CFGame();
            CFGame c_= new CFGame();
            c.setState(state);
            c.setRedTurn(false);
            c_.setState(state);
            c_.setRedTurn(false);
            //black plays column x
            if (c.play(x)) {
                if (c.isGameOver() && c.winner()==-1) {
                    score=score+800000;
                }
                //red plays the same column
                if (c_.play(x)) {
                    if (c_.isGameOver() && c.winner()==1) {
                        score=score-10000;
                    }
                }
            }
        }

        //award positioning points accordingly (center spots are valued)
        for (int j=0; j<4;j++) {
            for (int i=2; i<=4; i++) {
                if (state[i][j]==-1) {
                    score=score+500;
                } else if (state[i][j]==-1) {
                    score=score-300;
                }
            }
        }

        //check for threats of 3
        List<Integer> players = new ArrayList <Integer>();
        players.add(1);
        players.add(-1);
        for (int p: players) {

            //loop for horizontal check
            for (int i = 0; i <= 3; i++) {
                for (int j = 0; j < 6; j++) {
                    if (this.hCheck3(state, p, i, j, c__)) {
                        if (p == 1) {
                            score = score - 500;
                        } else {
                            score = score + 500;
                        }
                        if (i==2 && j==2) {
                            if (p==1) {
                                System.out.println("hey!");
                                score=score-2000;
                            } else {
                                score=score+1500;
                            }
                        }
                    }
                }
            }

            //loop for vertical  check
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j <= 2; j++) {
                    if (this.vCheck3(state, p, i, j, c__)) {
                        if (p == 1) {
                            score = score - 500;
                        } else {
                            score = score + 500;
                        }
                    }
                }
            }

            //loop for rightward diagonal check
            for (int i = 0; i <= 3; i++) {
                for (int j = 0; j <= 2; j++) {
                    if (this.RdCheck3(state, p, i, j, c__)) {
                        if (p == 1) {
                            score = score - 500;
                        } else {
                            score = score + 500;
                        }
                    }
                }
            }

            //loop for leftward diagonal check
            for (int i = 6; i >= 3; i--) {
                for (int j = 0; j <= 2; j++) {
                    if (this.LdCheck3(state, p, i, j, c__)) {
                        if (p == 1) {
                            score = score - 500;
                        } else {
                            score = score + 500;
                        }
                    }
                }
            }
        }


        for (int x=0; x<7; x++) {
            for (int y=0; y<5; y++) {
                if (c__.getRedthreatspots().contains(new Pair(x,y))) {
                    if (c__.getRedthreatspots().contains(new Pair(x,y+1))) {
                        for (Pair p: c__.getRedthreatspots()) {
                            System.out.println("("+ p.getFirst() + ", " + p.getSecond()+")");
                        }
                        score = -99999999999.9;
                    }
                }
                if (c__.getBlackthreatspots().contains(new Pair(x,y))) {
                    if (c__.getBlackthreatspots().contains(new Pair(x,y+1))) {
                        score = 99999999999.9;
                    }
                }

            }
        }



        return score;
    }

    public double oldEvaluateState(int[][] state) {
        double score=Math.random();
        return score;
    }

}



