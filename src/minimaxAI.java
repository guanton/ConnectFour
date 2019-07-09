import java.util.Map;

public class minimaxAI implements CFPlayer{

    @Override
    public int nextMove(CFGame g) {
        Double minimaxScore = g.minimax(g.getState(), true, 3);
        Map<Double, Integer> lookup = g.getMinimaxLookup();
        Integer col = lookup.get(minimaxScore);
        return col;
    }

    @Override
    public String getName() {
        return "minimaxAI";
    }
}
