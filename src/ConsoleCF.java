
import java.util.Scanner;
import java.util.Random;

public class ConsoleCF extends CFGame{

    private final CFPlayer player1;
    private final CFPlayer player2;
    private final int red;

    //constructor 1:sets up a human vs. ai game, with red player randomly assigned
    public ConsoleCF(CFPlayer ai){
        player1=ai;
        player2=new HumanPlayer();
        Random rand = new Random();
        red = rand.nextInt(2);//generate random int bt 0 and 1
    }

    //constructor 2: sets up AI vs. AI game, with red player randomly decided
    public ConsoleCF(CFPlayer ai1, CFPlayer ai2){
        player1=ai1;
        player2=ai2;
        Random rand= new Random();
        red = rand.nextInt(2);//generate random int bt 0 and 1
    }

    public void playOut(){

        while (!this.isGameOver()){
            if (red==0){//player one is red, goes first
                this.play(player1.nextMove(this));
                this.play(player2.nextMove(this));}


            else {//player 2 is red, goes first
                this.play(player2.nextMove(this));
                this.play(player1.nextMove(this));}

        }
        System.out.println("Game over!");
        System.out.println(this.getWinner() + " won!");
    }

    public String getWinner(){
        if (this.winner()==1){//red won
            if  (red==0){//if player 1 is red
                String winner=player1.getName();
                return winner;}
            //return player1.getName
            else {
                String winner=player2.getName();
                return winner;}
        }
        else if (this.winner()==-1){//black won
            if (red==0){//if player 1 is red
                String winner=player2.getName();//Player 2 wins
                return winner; }

            else {
                String winner=player1.getName();
                return winner;}
        }

        else
            return "draw";

    }


    private class HumanPlayer implements CFPlayer{
        @Override
        public int nextMove(CFGame g){
            //start with assumption that the move is invalid
            boolean valid=false;

            while (!valid){
                //prompt user for move
                Scanner input = new Scanner(System.in);
                System.out.println("Which column would you like to play next?");
                int num =input.nextInt();
                if (!g.play(num)) {
                    System.out.println("Invalid move.");
                } else {
                    return num;
                }
            }
            return 0;
        }

        @Override
        public String getName(){
            return "Human Player";
        }

    }

}

