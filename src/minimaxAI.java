import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class minimaxAI implements CFPlayer{

    @Override
    public int nextMove(CFGame g) {
        long startTime = System.nanoTime();

        //start
        int countmid=0;
        int[][] s = g.getState();
        List<Integer> start = new ArrayList<>();
        for (int y=0; y<6; y++) {
            if (s[3][y] != 0) {
                countmid++;
            }
        }
        if (countmid==1 || countmid==0 || (countmid ==3 && g.colsPlayed.size()==3 )) {
            return 3;
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
        //regular
        if (!g.isRedTurn) {
            long endTime = System.nanoTime();
            return (int) g.minimax(g.getState(), true, 6, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false).getSecond();
        } else {
            long endTime = System.nanoTime();
            return (int) g.minimax(g.getState(), false, 6, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false).getSecond();
        }
    }

    @Override
    public String getName() {
        return "minimaxAI";
    }
}
