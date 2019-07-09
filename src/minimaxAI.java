import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class minimaxAI implements CFPlayer{

    @Override
    public int nextMove(CFGame g) {

        for (int i = 0; i < 7; i++) {



        }


        int[][] s = g.getState();
        List<Integer> start = new ArrayList<>();
        for (int y=0; y<6; y++) {
            start.add(s[3][y]);
        }
        if (!start.contains(1) && !start.contains(-1)) {
            return 3;
        }
        List<Integer> start1 = new ArrayList<>();
        for (int y=0; y<6; y++) {
            start1.add(s[4][y]);
        }
        if (!start1.contains(1) && !start1.contains(-1)) {
            return 4;
        }
        return (int) g.minimax(g.getState(), true, 5).get(1);
    }

    @Override
    public String getName() {
        return "minimaxAI";
    }
}
