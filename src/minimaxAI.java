import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class minimaxAI implements CFPlayer{

    @Override
    public int nextMove(CFGame g) {
        //start
        int[][] s = g.getState();
        List<Integer> start = new ArrayList<>();
        for (int y=0; y<6; y++) {
            start.add(s[3][y]);
        }
        if (!start.contains(-1)) {
            return 3;
        }
        List<Integer> start1 = new ArrayList<>();
        for (int y=0; y<6; y++) {
            start1.add(s[4][y]);
        }
        if (!start1.contains(1) && !start1.contains(-1)) {
            return 4;
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
        return (int) g.minimax(g.getState(), true, 6, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false).get(1);
    }

    @Override
    public String getName() {
        return "minimaxAI";
    }
}
