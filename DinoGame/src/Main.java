import javax.swing.*;

public class Main {
    public static void main(String[] args) {
         int boardWidth = 750;
         int boardHeight = 250;

        JFrame frame = new JFrame("Dino Run");
        //frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DinoRun dinoRun = new DinoRun();
        frame.add(dinoRun);
        frame.pack();
        dinoRun.requestFocus();
        frame.setVisible(true);
    }
}