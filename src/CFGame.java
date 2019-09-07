import java.util.*;

public class CFGame {
    //state[i][j]= 0 means that column i, row j is empty
    //state[i][j]= 1 means the i,j slot is filled by red
    //state[i][j]=-1 means the i,j slot is filled by black
    protected int[][] state;
    protected boolean isRedTurn;
    //linked list that stores the history of columns played in the game
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

    //play the chip in the desired column and output true if it is a legal move, false otherwise
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


    //returns the winner of the game, 1 if red, -1 if black, 0 if draw
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
        //a player has attained 4 in a row
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

    //checks if there are 3/4 filled horizontally with one empty slot
    //adds the empty spot to threatspots if applicable
    public boolean hCheck3 (int p, int i, int j){
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
                this.addRedthreatspot(new Pair(zeroCol, j));;
            } else {
                this.addBlackthreatspot(new Pair(zeroCol, j));
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

    //checks if there are 3/4 in a row vertically with one empty slot
    //adds empty spot to threatspots if applicable
    public boolean vCheck3 (int p, int i, int j){
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
                this.addRedthreatspot(new Pair(i, zeroRow));
            } else {
                this.addBlackthreatspot(new Pair(i, zeroRow));
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

    //checks if there are 3/4 in a row with one empty slot
    //adds empty spot to threatspots if applicable
    public boolean RdCheck3 (int p, int i, int j) {
        int count=0;
        int zeros=0;
        int zeroCol=0;
        int zeroRow=0;
        List<Integer> ld = new ArrayList<>();
        ld.add(state[i][j]);
        ld.add(state[i+1][j+1]);
        ld.add(state[i+2][j+2]);
        ld.add(state[i+3][j+3]);
        for (Integer x: ld) {
            if (x==p) {
                count++;
            }
            if (x==0) {
                zeros++;
            }
        }
        if (count==3 && zeros==1) {
            if (state[i][j]==0) {
                zeroCol=i;
                zeroRow=j;
            } else if (state[i+1][j+1]==0) {
                zeroCol=i+1;
                zeroRow=j+1;
            } else if (state[i+2][j+2]==0) {
                zeroCol=i+2;
                zeroRow=j+2;
            } else {
                zeroCol=i+3;
                zeroRow=j+3;
            }
            if (p==1) {
                this.addRedthreatspot(new Pair(zeroCol, zeroRow));
            } else {
                this.addBlackthreatspot(new Pair(zeroCol, zeroRow));
            }
            return true;
        } else {
            return false;
        }
    }

    //check for 3/4 in a row in the left diagonal beginning at (i,j)
    public boolean LdCheck ( int p, int i, int j) {
        if (state[i][j] == p && state[i - 1][j + 1] == p && state[i - 2][j + 2] == p && state[i - 3][j + 3] == p) {
            return true;
        } else {
            return false;
        }
    }

    //checks if there are 3 in a row with one empty slot
    //adds empty spot to threatspots if applicable
    public boolean LdCheck3 (int p, int i, int j) {
        int count=0;
        int zeros=0;
        int zeroCol=0;
        int zeroRow=0;
        List<Integer> ld = new ArrayList<>();
        ld.add(state[i][j]);
        ld.add(state[i-1][j+1]);
        ld.add(state[i-2][j+2]);
        ld.add(state[i-3][j+3]);
        for (Integer x: ld) {
            if (x==p) {
                count++;
            }
            if (x==0) {
                zeros++;
            }
        }
        if (count==3 && zeros==1) {
            if (state[i][j]==0) {
                zeroCol=i;
                zeroRow=j;
            } else if (state[i-1][j+1]==0) {
                zeroCol=i-1;
                zeroRow=j+1;
            } else if (state[i-2][j+2]==0) {
                zeroCol=i-2;
                zeroRow=j+2;
            } else {
                zeroCol=i-3;
                zeroRow=j+3;
            }
            if (p==1) {
                this.addRedthreatspot(new Pair(zeroCol, zeroRow));
            } else {
                this.addBlackthreatspot(new Pair(zeroCol, zeroRow));
            }
            return true;
        } else {
            return false;
        }
    }


    public double evaluateState() {

        this.getBlackthreatspots().clear();
        this.getRedthreatspots().clear();

        //start with some noise between 0-1
        double score=Math.random();

        List<Integer> players = new ArrayList <Integer>();
        players.add(1);
        players.add(-1);
        for (int p: players) {
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

            //check for threats of 3:
            int numChecks=0;

            //loop for horizontal check
            for (int i = 0; i <= 3; i++) {
                for (int j = 0; j < 6; j++) {
                    if (this.hCheck3(p, i, j)) {
                        numChecks=numChecks+1;
                    }
                }
            }

            //loop for vertical  check
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j <= 2; j++) {
                    if (this.vCheck3(p, i, j)) {
                        numChecks++;
                    }
                }
            }

            //loop for rightward diagonal check
            for (int i = 0; i <= 3; i++) {
                for (int j = 0; j <= 2; j++) {
                    if (this.RdCheck3(p, i, j)) {
                        numChecks++;
                    }
                }
            }

            //loop for leftward diagonal check
            for (int i = 6; i >= 3; i--) {
                for (int j = 0; j <= 2; j++) {
                    if (this.LdCheck3(p, i, j)) {
                        numChecks++;
                    }
                }
            }


            if (p==1) {
                score=score-250*numChecks;
            } else {
                score=score+250*numChecks;
            }
        }

        //a threat spot is a unique spot on the board in which one more tile will result in a win
        //ex. if red has 3 threat spots, then there are 3 currently empty spots on the board that would
        //imply a red win if red gets a tile in that spot

        score = score + 500*this.getBlackthreatspots().size();
        score = score - 500*this.getRedthreatspots().size();

        //for every column, we determine the lowermost row that red threatens
        List<Integer> minRowRed = new ArrayList();
        List<Integer> minRowBlack = new ArrayList<>();
        //fill it with 6s first (not even on the board)
        for (int x=0; x<7; x++) {
            minRowRed.add(6);
            minRowBlack.add(6);
        }
        for (Pair p: this.getRedthreatspots()) {
            //identify which column the pair represents
            for (int x=0; x<7; x++) {
                if ((int) p.getFirst()==x) {
                    //replace the lowest row if the pair represents a lower row than current
                    if (minRowRed.get(x)> (int) p.getSecond()) {
                        minRowRed.set(x, (int) p.getSecond());
                    }
                }
            }
        }
        for (Pair p: this.getBlackthreatspots()) {
            for (int x=0; x<7; x++) {
                if ((int) p.getFirst()==x) {
                    if (minRowBlack.get(x) > (int) p.getSecond()) {
                        minRowBlack.set(x, (int) p.getSecond());
                    }
                }
            }
        }

        for (int x=0; x<7; x++) {
            CFGame game = new CFGame();
            game.setState(this.getState());
            if (game.play(x)) {
                if (game.play(x) && game.winner()==-1) {

                }
            }
            CFGame game2 = new CFGame();
            game2.setRedTurn(false);
            game2.setState(this.getState());
            if (game2.play(x)) {
                if (game2.play(x) && game2.winner()==1) {
                    score=score-500;
                }
            }
            if (minRowBlack.get(x) < minRowRed.get(x)) {
                score = score + 500;
            }
            if (minRowRed.get(x)< minRowBlack.get(x)) {
                score = score - 500;
            }
        }

        return score;
    }


    public double oldEvaluateState() {
        double score=Math.random();
        return score;
    }

}



