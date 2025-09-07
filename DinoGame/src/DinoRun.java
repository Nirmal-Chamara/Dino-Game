import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Objects;

public class DinoRun extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 750;
    int boardHeight = 300;

    //Images
    Image dinosaurImg;
    Image dinosaurDeadImg;
    Image dinosaurJumpImg;
    Image cactus1Img;
    Image cactus2Img;
    Image cactus3Img;
    Image bigCactus1Img;
    Image bigCactus2Img;
    Image trackImage;
    Image gameOverImg;


    static class Block{
        int x;
        int y;
        int width;
        int height;
        Image img;

        Block(int x ,int y ,int width ,int height, Image img){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }
    //Track
    int trackHeight = 28;
    int trackX1 = 0;
    int trackX2 = boardWidth;
    int trackY = boardHeight - trackHeight;
    int trackSpeed = -12;

    //Dino
    int dinosaurWidth = 88;
    int dinosaurHeight = 94;
    int dinosaurX = 50;
    int dinosaurY =boardHeight - dinosaurHeight - trackHeight +10;


    Block dinosaur;

    //cactus
    int cactus1Width = 34;
    int cactus2Width = 69;
    int cactus3Width = 102;
    int bigCactus1Width = 50;
    int bigCactus2Width = 103;

    int cactusHeight = 70;
    int bigCactusHeight = 100;
    int cactusX = 700;
    int cactusY = boardHeight - cactusHeight - trackHeight +10;
    int BigCactusY = boardHeight - bigCactusHeight - trackHeight +15;
    ArrayList<Block> cactusArray;

    //GameOver
    int gameOverWidth = 386;
    int gameOverHeight = 40;
    int gameOverX = (boardWidth - gameOverWidth) / 2;
    int gameOverY = (boardHeight - gameOverHeight - 40) / 2;

    //physics
    int velocityX = -12; //cactus moving left speed
    int velocityY = 0;// dino jump speed
    int gravity = 1;

    boolean gameOver = false;
    int score = 0;

    Timer gameloop;
    Timer placeCactusTimer;

    public DinoRun(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.lightGray);
        setFocusable(true);
        addKeyListener(this);

        dinosaurImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./ChromeDinoImg/dino-run.gif"))).getImage();
        dinosaurDeadImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./ChromeDinoImg/dino-dead.png"))).getImage();
        dinosaurJumpImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./ChromeDinoImg/dino-jump.png"))).getImage();
        cactus1Img = new ImageIcon(Objects.requireNonNull(getClass().getResource("./ChromeDinoImg/cactus1.png"))).getImage();
        cactus2Img = new ImageIcon(Objects.requireNonNull(getClass().getResource("./ChromeDinoImg/cactus2.png"))).getImage();
        cactus3Img = new ImageIcon(Objects.requireNonNull(getClass().getResource("./ChromeDinoImg/cactus3.png"))).getImage();
        bigCactus1Img = new ImageIcon(Objects.requireNonNull(getClass().getResource("./ChromeDinoImg/big-cactus1.png"))).getImage();
        bigCactus2Img = new ImageIcon(Objects.requireNonNull(getClass().getResource("./ChromeDinoImg/big-cactus2.png"))).getImage();
        trackImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("./ChromeDinoImg/track.png"))).getImage();
        gameOverImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./ChromeDinoImg/game-over.png"))).getImage();
        //dino
        dinosaur = new Block(dinosaurX, dinosaurY, dinosaurWidth, dinosaurHeight, dinosaurImg);

        //cactus
        cactusArray = new ArrayList<>();

        //game timer
        gameloop = new Timer(1000/60, this); //1000/60 = 60 frames per 1000ms(1s)
        gameloop.start();

        //place cactus timer
        placeCactusTimer = new Timer(1500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                placeCactus();
            }
        });
        placeCactusTimer.start();
    }
    void placeCactus(){
        if (gameOver){
            return;
        }

        double placeCactusChance = Math.random(); // 0 - 0.99999
        if (placeCactusChance > .90){ //10% chance
            Block cactus = new Block(cactusX, cactusY, cactus3Width, cactusHeight, cactus3Img);
            cactusArray.add(cactus);
        }
        else if (placeCactusChance > .70) { // 20% chance
            Block cactus = new Block(cactusX, cactusY, cactus2Width, cactusHeight, cactus2Img);
            cactusArray.add(cactus);
        }
        else if (placeCactusChance > .50) { //20%
            Block cactus = new Block(cactusX, cactusY, cactus1Width, cactusHeight, cactus1Img);
            cactusArray.add(cactus);
        }
        else if (placeCactusChance > .40) { //10%
            Block cactus = new Block(cactusX, BigCactusY, bigCactus1Width, bigCactusHeight, bigCactus1Img);
            cactusArray.add(cactus);
        }
        else if (placeCactusChance > .35) { //10%
            Block cactus = new Block(cactusX, BigCactusY, bigCactus2Width, bigCactusHeight, bigCactus2Img);
            cactusArray.add(cactus);
        }

        if (cactusArray.size() > 10){
            cactusArray.remove(0); // remove 1st cactus from array list
        }
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        //dino
        g.drawImage(dinosaur.img, dinosaur.x, dinosaur.y, dinosaur.width, dinosaur.height, null);
        //track
        g.drawImage(trackImage, trackX1, trackY, boardWidth,trackHeight,null);
        g.drawImage(trackImage, trackX2, trackY, boardWidth,trackHeight,null);
        //cactus
        for (int i = 0; i < cactusArray.size(); i++){
            Block cactus = cactusArray.get(i);
            g.drawImage(cactus.img, cactus.x, cactus.y, cactus.width, cactus.height, null);
        }

        //game over
        g.setColor (new Color(73, 73, 73));
        g.setFont(new Font("Courier", Font.PLAIN, 32));
        if (gameOver){
            g.setColor(new Color(0,0,0,50)); //black with 60% overlay
            g.fillRect(0,0,boardWidth,boardHeight);
            g.drawImage(gameOverImg, gameOverX, gameOverY, null);
            //score
            g.setColor(new Color(93, 92, 92,255));
            String scoreText = "Score: " + score ;
            int textWidth = g.getFontMetrics().stringWidth(scoreText);
            g.drawString(scoreText,(boardWidth - textWidth)/2, gameOverY + gameOverHeight + 40);
            //Restart
            g.setFont(new Font("Courier", Font.BOLD, 18));
            int restartWidth = g.getFontMetrics().stringWidth("Press SPACE to Restart");
            g.drawString("Press SPACE to Restart", (boardWidth - restartWidth)/2, gameOverY + gameOverHeight + 70);
        }
        else {
            g.drawString("Score: "+String.valueOf(score), 10,35);
        }
    }

    public void move(){
        //dino
        velocityY += gravity;
        dinosaur.y  += velocityY;

        if (dinosaur.y > dinosaurY){
            dinosaur.y = dinosaurY;
            velocityY = 0;
            dinosaur.img = dinosaurImg;
        }

        //track
        trackX1 += trackSpeed;
        trackX2 += trackSpeed;
        //infinite loop
        if (trackX1 + boardWidth <=0){
            trackX1 = boardWidth;
        }
        if (trackX2 + boardWidth <=0){
            trackX2 = boardWidth;
        }

        //cactus
        for (int i = 0; i < cactusArray.size(); i++){
            Block cactus =cactusArray.get(i);
            cactus.x += velocityX;

            if (collision(dinosaur, cactus)){
                gameOver = true;
                dinosaur.img = dinosaurDeadImg;
            }
        }

        //score
        score++;
    }

    boolean collision(Block a, Block b){
        return a.x < b.x + b.width &&   // a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x &&  // a's top right corner passes  b's top left corner
                a.y < b.y + b.height && //a's bottom left corner doesn't reach b's bottom right corne
                a.y + a.height > b.y;   //a's top left corner passes  b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver){
            placeCactusTimer.stop();
            gameloop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            //System.out.println("JUMP!");
            if (dinosaur.y == dinosaurY) {
                velocityY=-17;
                dinosaur.img = dinosaurJumpImg;
            }
            if (gameOver){
                //restart game by resetting conditions
                dinosaur.y = dinosaurY;
                dinosaur.img = dinosaurImg;
                velocityY = 0;
                cactusArray.clear();
                score = 0;
                gameOver = false;
                gameloop.start();
                placeCactusTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
