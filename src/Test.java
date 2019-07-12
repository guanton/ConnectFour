
public class Test {
    public static void main(String[] args) {

        CFPlayer ai1 = new minimaxAI();
        CFPlayer ai2 = new RandomAI();
        int n = 250;
        int winCount = 0;
        int draws = 0;
        for (int i=0; i<n; i++) {
            ConsoleCF game = new ConsoleCF(ai1, ai2);
            game.playOut();
            if (game.getWinner() == ai2.getName()) {
                winCount++;
            }
            if (game.getWinner()=="draw") {
                draws++;
            }
        }
        System.out.print(ai2.getName() + " beats " + ai1.getName()+ " ");
        System.out.print(((double) winCount)/((double) n)*100 + "%");
        System.out.print(" of the time.");
        System.out.println("there was a draw " + ((double) draws/(double) n)*100 + "% of the time. ");
    }


}