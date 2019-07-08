import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAI implements CFPlayer{


    @Override
    public int nextMove(CFGame g) {
        //randomly generate an int between 0 and 6 -? int candidate
        // subject to an if statement
        // a number between 0-6 gets generated
        // retrieve the board by calling get state
        int[][] state = g.getState();
        boolean illegal=true;
        Random r = new Random();
        int col = r.nextInt(7);

        while(illegal) {
            int[] colarray = state[col];
            List<Integer> listCol = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                listCol.add(new Integer(colarray[j]));
            }
            if (listCol.contains(0)) {
                illegal = false;
            } else {
                col = r.nextInt(7);
            }
        }
        return col;
    }

    @Override
    public String getName() {
        return "Random Player";
    }


}

