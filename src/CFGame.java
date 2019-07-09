import java.util.*;

public class CFGame {
    //state[i][j]= 0 means the i,j slot is empty
    //state[i][j]= 1 means the i,j slot is filled by red
    //state[i][j]=-1 means the i,j slot is filled by black
    protected int[][] state;
    protected boolean isRedTurn;
    private Map<Double, Integer> minimaxLookup;

    public CFGame() {
        state = new int[7][6];
        for (int i=0; i<7; i++)
            for (int j=0; j<6; j++)
                state[i][j] = 0;
        isRedTurn = true; //red goes first
        minimaxLookup = new HashMap<>();
    }

    //return the minimax Map
    public Map<Double, Integer> getMinimaxLookup() {
        return minimaxLookup;
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

    //play the chip in the desired column
    public boolean play(int column) {

        for (int j=0;j<6;j++){
            if (state[column][j]==0){
                if (isRedTurn){
                    state[column][j]=1;
                    isRedTurn = false;
                    return true;
                }
                else {
                    state[column][j]=-1;
                    isRedTurn = true;
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

    public boolean vCheck ( int p, int i, int j){
        if (state[i][j] == p && state[i][j + 1] == p && state[i][j + 2] == p && state[i][j + 3] == p) {
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

    public boolean LdCheck ( int p, int i, int j) {
        if (state[i][j] == p && state[i - 1][j + 1] == p && state[i - 2][j + 2] == p && state[i - 3][j + 3] == p) {
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
            for (int i=0;i<4;i++){
                for (int j=0;j<=2;j++){
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

    //returns a score representing the best move for maximizingPlayer (black)
    public double minimax(int[][] state, boolean maximizingPlayer, int n) {
        System.out.println(n);
        //make copy of board
        CFGame c = new CFGame();
        c.setState(state);
        //whose turn is it? since black is maximizingPlayer, red is the opposite
        c.setRedTurn(!maximizingPlayer);
        //if the board state represents a finished game or if n==0 (BASE CASE)
        if (c.isGameOver() || n==0) {
            if (c.isGameOver()) {
                if (c.winner()==1) {
                    //black player lost
                    return Double.NEGATIVE_INFINITY;
                } else if (c.winner()==-1) {
                    //black player won
                    return Double.POSITIVE_INFINITY;
                } else if (c.winner()==0){
                    return 0;
                }
            }
            if (n==0) {
                return 1;
            }
        }
        //generate the next 7 board states if the game is not yet over
        ArrayList<CFGame> nextGames = new ArrayList<>();
        ArrayList<Double> nextScores = new ArrayList<>();
        if (maximizingPlayer) {
            for (int x = 0; x < 7; x++) {
                CFGame game = new CFGame();
                game.setRedTurn(false);
                game.setState(c.getState());
                game.play(x);
                nextGames.add(game);
            }
            for (int x = 0; x<7; x++) {
                CFGame g = nextGames.get(x);
                double child = minimax(g.getState(), false, n-1);
                System.out.println("Child score: " + child);
                nextScores.add(child);
                if (n==3) {
                    minimaxLookup.put(child,x);
                }
            }
            //return the best of these 7 scores
            return Collections.max(nextScores);
        } else {
            for (int x = 0; x < 7; x++) {
                CFGame game = new CFGame();
                game.setRedTurn(true);
                game.setState(c.getState());
                game.play(x);
                nextGames.add(game);
            }
            for (CFGame g: nextGames) {
                double child = minimax(g.getState(), true, n-1);
                nextScores.add(child);
            }
            System.out.println("here: " + minimaxLookup);
            return Collections.min(nextScores);
        }
    }


}



