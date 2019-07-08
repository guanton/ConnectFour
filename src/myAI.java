public class myAI implements CFPlayer {


    @Override
    public int nextMove(CFGame g) {
        RandomAI rando = new RandomAI();
        int col=rando.nextMove(g);
        boolean containszero = false;
        int[][] state = g.getState();

        for (int i = 0; i < 7; i++) {
            // prevent accumulation
            CFGame copyg = new CFGame();
            CFGame copyg_ = new CFGame();
            copyg.setState(g.getState());
            copyg_.setState(g.getState());

            //1) start with the "good " strategy, where we use the isGameOver function to play a move if it gives us a win, or to block a move if it gives the opponent a win.

            copyg.setRedTurn(true);
            copyg.play(i);
            // copyg.isGameOver returns true if your move would cause the win for you (if you;re red)
            // it assumes in other words that isRedTurn = true
            // does not account for if the oponent has a winning move because we would need to set isRedTurn = false
            // and THEN call play(i), to consider the opponent's winning moves
            if (copyg.isGameOver()) {
                return i;
            }

            copyg_.setRedTurn(false);
            copyg_.play(i);
            if (copyg_.isGameOver()) {
                return i;
            }
        }
        //if good strategy doesn't apply, and we aren't in the first 4 turns, then, return a random move
        return col;
    }

    @Override
    public String getName() {
        return "My AI";
    }


}




