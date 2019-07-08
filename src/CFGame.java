import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CFGame {
    //state[i][j]= 0 means the i,j slot is empty
    //state[i][j]= 1 means the i,j slot is filled by red
    //state[i][j]=-1 means the i,j slot is filled by black

    private int[][] state;
    private boolean isRedTurn;

    //initialize the board (7 columns, 6 rows)
    {
        state = new int[7][6];
        for (int i=0; i<7; i++)
            for (int j=0; j<6; j++)
                state[i][j] = 0;
        isRedTurn = true; //red goes first
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

    //returns a column (0-6)
    public int minimax(int[][] state, int depth, boolean maximizingPlayer) {
        if (depth==0 || this.isGameOver()) {
            return maximizingColumn(int[] state);
        }
        //if rational, the maximizing player (black) will pick the slot that maximizes his score down the line
        if (maximizingPlayer) {
            //worst case
            double maxEval = Double.NEGATIVE_INFINITY;
            //create a list of the nextGames
            ArrayList<CFGame> nextGames = new ArrayList<>();
            for (int x=0; x<7; x++) {
                CFGame game = new CFGame();
                game.setRedTurn(false);
                game.setState(this.getState());
                game.play(x);
                nextGames.add(game);
            }
            ArrayList<Integer> nextScores = new ArrayList<>();
            for (int x=0; x<7; x++) {
                Integer score = nextGames.get(x).getScore();
                nextScores.add(score);
            }

        }
        if (!maximizingPlayer) {

        }
    }

    public int maximizingColumn(int[][] state) {

    }

    public int getScore() {
        int[][] state = this.getState();
        return 0;
    }

}



