
public class Test {
    public static void main(String[] args) {

        CFPlayer ai1 = new RandomAI();
        CFPlayer ai2 = new myAI();
        int n = 500;
        int winCount = 0;
        for (int i=0; i<n; i++) {
            ConsoleCF game = new ConsoleCF(ai1, ai2); game.playOut();
            if (game.getWinner()==ai2.getName())
                winCount++; }
        System.out.print(ai2.getName() + " beats " + ai1.getName()+ " ");
        System.out.print(((double) winCount)/((double) n)*100 + "%");
        System.out.print(" of the time.");
    }


}