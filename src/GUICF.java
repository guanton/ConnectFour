
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Scanner;

public class GUICF extends CFGame {
    private GameBoard this_board;
    private CFGame g;
    private CFPlayer redPlayer;
    private CFPlayer blackPlayer;
    private JButton[] buttons;
    private JButton playButton;
    private JLabel[][] squares;
    private boolean Human;
    private JLabel gameOverLabel;

    //initialization
    {
        buttons = new JButton[7];
        for (int i = 0; i < 7; i++) {
            //down arrow buttons for each column
            buttons[i] = new JButton("\u2193");
        }
        //set up empty board
        squares = new JLabel[7][6];
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 6; row++) {
                squares[col][row] = new JLabel();
                squares[col][row].setOpaque(true);
                squares[col][row].setBorder(BorderFactory.createLineBorder(Color.black));
            }
        }
    }

    public GUICF(CFPlayer ai) {
        //sets up and starts a human vs. AI game
        g = new CFGame();
        blackPlayer = ai;
        Human = true;
        displayBoard();
    }

    public GUICF(CFPlayer ai1, CFPlayer ai2) {
        //sets up and starts an AI vs. AI game
        g = new CFGame();
        Human = false;

        playButton = new JButton("Play");
        playButton.addActionListener(new ButtonListener(true, -1));

        if (Math.random() < 0.5) {
            redPlayer = ai1;
            blackPlayer = ai2;
        } else {
            redPlayer = ai2;
            blackPlayer = ai1;
        }
        displayBoard();
    }

    private void displayBoard() {
        JFrame frame = new JFrame("Connect Four");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this_board = new GameBoard();
        frame.getContentPane().add(this_board);
        frame.setSize(1500, 1000);
        frame.setVisible(true);
        this_board.setVisible(true);
    }

    private void playGUI(int c) {
        //play the column
        g.play(c);
        //retrieve the row
        int row = getRowThatJustGotPlayed(c);
        //paint corresponding cell
        if(g.isRedTurn())
            this_board.paint(c, row, -1); // black just played so label must be black
        else
            this_board.paint(c, row, 1); // red just played so label must be red
        //check winner
        if (g.isGameOver()) {
            if (g.winner() == 1)
                gameOverLabel.setText(redPlayer.getName() + " won!");
            if (g.winner() == -1)
                gameOverLabel.setText(blackPlayer.getName() + " won!");
            if (g.winner() == 0)
                gameOverLabel.setText("It's a draw!");
            return;
        }
    }

    private int getRowThatJustGotPlayed(int col){
        int[][] board = g.getState();
        for(int row = 0; row < 6; row++){
            if(board[col][row] == 0) {
                System.out.println(row + " " + col);
                return row-1; // return nonzero row which is below current row
            }
        }
        return 5;
    }


    //layout for the board
    private class GameBoard extends javax.swing.JPanel {
        private GameBoard() {
            //initialize empty board with extra row for buttons
            this.setLayout(new GridLayout(8,7));

            //make row with only Play button (or no play button if there is no human)
            gameOverLabel = new JLabel();
            this.add(gameOverLabel);

            for (int i = 0; i < 5; i++) {
                this.add(new JLabel());

                if (i == 1) {
                    if (Human) {
                        this.add(new JLabel());
                    }
                    else {
                        this.add(playButton);
                    }
                }
            }

            // make top row buttons
            for(int i = 0; i < 7; i++){
                buttons[i].addActionListener(new ButtonListener(false, i));
                this.add(buttons[i]);
            }
            for(int row = 5; row >= 0; row--){
                for(int col = 0; col < 7; col++){
                    this.add(squares[col][row]);
                }
            }
        }

        private void paint(int column, int row, int color) {
            //paints specified coordinate red or black
            if (color == 1)
                squares[column][row].setBackground(Color.RED);
            if (color == -1)
                squares[column][row].setBackground(Color.BLACK);
        }
    }

    public class ButtonListener implements ActionListener {
        boolean isPlayButton;
        int column;

        ButtonListener(boolean p, int c) {
            isPlayButton = p;
            column = c;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //AI vs AI
            if (!Human) {
                if (g.isRedTurn())
                    playGUI(redPlayer.nextMove(g));
                else
                    playGUI(blackPlayer.nextMove(g));
            }
            else {
                playGUI(column);
                if (g.isGameOver()) {
                    if (g.winner() == 1)
                        gameOverLabel.setText(redPlayer.getName() + " won!");
                    if (g.winner() == -1)
                        gameOverLabel.setText(blackPlayer.getName() + " won!");
                    if (g.winner() == 0)
                        gameOverLabel.setText("It's a draw!");
                    return;
                }
                playGUI(blackPlayer.nextMove(g));
            }
        }
    }

    class HumanPlayer implements CFPlayer{

        @Override
        public int nextMove(CFGame g) {
            return 0;
        }

        public String getName(){
            return "Human Player";
        }
    }

    public static void main(String[] args) {
        GUICF guiGame = new GUICF(new myAI());
    }

}