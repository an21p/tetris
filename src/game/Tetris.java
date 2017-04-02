package game;//game.Tetris.java

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by antonis on 27/11/2014.
 */
public class Tetris extends JApplet implements Runnable {

    public static final int SQUARE_SIZE = 32; // 32 by 32 pixels
    static int level = 250;
    static Board board;
    static Tetris game;


    Font font = new Font("Arial", Font.PLAIN,30);
    Thread clockThread = null;
    JFrame controlFrame;
    JPanel mainPanel, topPanel, nextPanel;
    Piece currentPiece = PieceCreator.createPiece();
    Piece nextPiece = PieceCreator.createPiece();
    Piece displayPiece = new Piece(nextPiece.contents,nextPiece.type);

    int score = 0;
    JButton scoreButton, play, nextL;
    JMenuItem start;

    //AudioClip audioClip;

    boolean gameOver=false, dropIt=false, paused = false;
    boolean newGame = false;

    public void init() { //applet initialisation
        game = this;
        addMenu();
        // SoundManager.start();
        //sets up the controls
        Controller tController = new Controller(this);
        addKeyListener(tController);
        addMouseListener(tController);
        setFocusable(true);
        requestFocusInWindow();


        //sets the size of the applet relative to the size of each square
        setSize(new Dimension(Board.COLUMNS * 2 * SQUARE_SIZE,
                (Board.ROWS) * SQUARE_SIZE));
        //setSize(640,690);

        setLayout(new BorderLayout());
        //adds name on the top of the frame
        //does not work in browser
/*        Frame c = (Frame)this.getParent().getParent();
        c.setTitle("Antonis Pishias - 1300931");
        c.setResizable(false);*/



        nextPanel = new JPanel();
        nextPanel.setLayout(new BoxLayout(nextPanel, BoxLayout.PAGE_AXIS));
        nextPanel.setBorder(BorderFactory.createEmptyBorder(64,128,64,64));


        scoreButton = new JButton("0");
        scoreButton.setEnabled(false);
        scoreButton.setFont(font);
        nextL = new JButton("NEXT TETROMINO");
        nextL.setEnabled(false);
        nextL.setFont(font);
        topPanel = new JPanel(new GridLayout(3,0));
        topPanel.add(scoreButton);
        topPanel.add(nextL);
        topPanel.add(nextPanel);
        topPanel.setPreferredSize
                (new Dimension(Board.COLUMNS * SQUARE_SIZE,
                        (Board.ROWS - 2) * SQUARE_SIZE));

        add(topPanel, BorderLayout.EAST);
 
        board = new Board();
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.add(board);
        mainPanel.setPreferredSize
                (new Dimension(Board.COLUMNS * SQUARE_SIZE,
                        (Board.ROWS - 1) * SQUARE_SIZE));
        add(mainPanel, BorderLayout.WEST);

        displayPiece.currentC=0;
        displayPiece.currentR=0;
        nextPanel.add(displayPiece);

        startTimer();
        //play sound





    }

    private void addMenu() {
        JMenuBar menubar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        JMenu g = new JMenu("Game");
        file.setMnemonic(KeyEvent.VK_G);

        JMenu l = new JMenu("Level");
        file.setMnemonic(KeyEvent.VK_L);
        JMenuItem ngame = new JMenuItem("New Game");
        ngame.setMnemonic(KeyEvent.VK_N);
        ngame.setToolTipText("Clears the board and starts a new game");
        ngame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                game.gameOver();
                game.reset();
            }
        });

        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        file.add(ngame);
        file.add(eMenuItem);
        menubar.add(file);


        start = new JMenuItem("Pause");
        start.setMnemonic(KeyEvent.VK_P);
        start.setToolTipText("Pauses and Unpaused the game");
        start.addActionListener(new Actions());


        JMenuItem controlsME = new JMenuItem("Controls");
        controlsME.setMnemonic(KeyEvent.VK_C);
        controlsME.setToolTipText("Game Controls");
        controlsME.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                game.showControls();
            }
        });

        g.add(start);
        g.add(controlsME);

        menubar.add(g);

        JMenuItem easy = new JMenuItem("Easy");
        easy.setToolTipText("More time to think");
        easy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                level = 350;
                game.gameOver();
                game.reset();
            }
        });

        JMenuItem medium = new JMenuItem("Medium");
        medium.setToolTipText("Regular Speed");
        medium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                level = 250;
                game.gameOver();
                game.reset();
            }
        });

        JMenuItem hard = new JMenuItem("Hard");
        hard.setToolTipText("No time to think");
        hard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                level = 150;
                game.gameOver();
                game.reset();
            }
        });

        JMenuItem expert = new JMenuItem("Expert");
        expert.setToolTipText("Not Recomended");
        expert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                level = 50;
                game.gameOver();
                game.reset();
            }
        });

        l.add(easy);
        l.add(medium);
        l.add(hard);
        l.add(expert);

        menubar.add(l);

        setJMenuBar(menubar);

    }

    private void showControls() {

        //opens a new JFrame including control information
        //does not work in web browser
        paused=true;
        controlFrame = new JFrame("Controls");
        controlFrame.setPreferredSize(new Dimension(SQUARE_SIZE*15,SQUARE_SIZE*10));
        JPanel panelC = new JPanel(new GridLayout(7,0));
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel leftL = new JLabel("LEFT ARROW/LEFT MOUSE BUTTON :       LEFT");
        JLabel rightL = new JLabel("RIGHT ARROW/RIGHT MOUSE BUTTON :  RIGHT");
        JLabel upL = new JLabel("UP ARROW/MIDDLE MOUSE BUTTON :     ROTATION ");
        JLabel downL = new JLabel("DOWN ARROW/SPACE-BAR :                   DROP");
        JLabel whiteL = new JLabel("WHITE BLOCKS ARE GHOSTS");
        JLabel twoL = new JLabel("THE TWO BLOCK LINE BURNS THROUGH COLUMNS");

        play = new JButton("Continue");
        play.addActionListener(new Actions());

        panelC.add(leftL);
        panelC.add(rightL);
        panelC.add(upL);
        panelC.add(downL);
        panelC.add(whiteL);
        panelC.add(twoL);
        panelC.add(play);

        controlFrame.getContentPane().add(panelC);
        controlFrame.pack();
        controlFrame.setVisible(true);

    }

    private void startTimer() {
        if (clockThread == null) {
            clockThread = new Thread(this, "Clock");
            clockThread.start();
        }
    }

    public void stopTimer() {
        clockThread = null;
    }




    public void addScore(int v) {
        score += v;
        scoreButton.setText(""+score);
    }


    static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ie) {
        }
    }

    void dropPiece() {
        // the main game function
        // checks if the top row is empty in the board (game is over)
        // else if the current piece can move down it does
        // else it is copied on the board at the current location
        // then a new shape is generated
        if(board.isRowEmpty(2)){
            mainPanel.add(currentPiece);
            currentPiece.repaint();


            if (board.canPaste(currentPiece)){
                board.repaint();
                currentPiece.fall();
               // mainPanel.remove(currentPiece);

            }
            else {
                dropIt =false;
                board.paste(currentPiece);
                board.repaint();
                mainPanel.remove(currentPiece);
                int deletedRows =0;
                for (int r = 2; r<22;r++){
                    if (board.isRowFull(r)) {
                        board.removeRow(r);
                        deletedRows++;
                    }
                }
                switch (deletedRows) {
                    case 0: /*addScore(0);*/  break;
                    case 1: addScore(40);     break;
                    case 2: addScore(100);    break;
                    case 3: addScore(300);    break;
                    case 4: addScore(1200);   break;
                }
                if (currentPiece.type==7){
                    board.removeColumn(currentPiece.currentC);
                    board.removeColumn(currentPiece.currentC+1);
                    addScore(20);
                }

                currentPiece = nextPiece;
                nextPanel.remove(displayPiece);
                nextPanel.repaint();
                nextPiece = PieceCreator.createPiece();
                displayPiece = new Piece(nextPiece.contents,nextPiece.type);
                displayPiece.currentC=0;
                displayPiece.currentR=0;
                nextPanel.add(displayPiece);
                nextPanel.repaint();


            }
        }
        else {
            gameOver();
        }
    }

    void gameOver() {
        //stops the game
        //play sound
        start.setText("New Game");
        nextL.setText("GAME OVER");
        stopTimer();
        gameOver=true;
        newGame=true;

    }

    public void reset(){
        // resets the game
        try{
            mainPanel.remove(currentPiece);
        }catch (Exception e){
        }
        //play sound
        scoreButton.setText("0");
        nextL.setText("NEXT TETROMINO");
        start.setText("Pause");
        newGame=false;
        paused = false;
        gameOver = false;
        mainPanel.remove(board);
        board = new Board();
        mainPanel.add(board);
        board.repaint();
        currentPiece = PieceCreator.createPiece();
        startTimer();


    }

    @Override
    public void run() {
        //game loop
        // calls dropPiece function when the game is not over and not aused 
        //game.Tetris game = new game.Tetris();
        Thread myThread = Thread.currentThread();
        while (clockThread == myThread) {
        if (!game.gameOver) {
            if (!game.paused) {


                   //canPaste =board.canPaste(currentPiece);



                try {
                    if (!dropIt) {
                        Thread.sleep(level);
                    }
                    game.dropPiece();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                mainPanel.add(currentPiece);
                while (game.paused) {
                    Tetris.sleep(50);
                }
                mainPanel.remove(currentPiece);
            }

        }
        else {
            stopTimer();
        }
        }
    }



    class Actions implements ActionListener {
        //a class that handles the main button-functions of the game
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (e.getSource() == play){
                    paused = false;
                    controlFrame.setVisible(false);
                    start.setVisible(true);

                }
                if (e.getSource() == start) {
                    if (paused) {
                        paused = false;
                        start.setText("Pause");
                    }
                    else {
                        paused = true;
                        start.setText("Start");
                    }
                    if (newGame){
                        reset();
                    }
                }
            }
            catch (Exception ex) {
                System.out.println("Error");
            }
        }
    }

    class Controller implements MouseListener, KeyListener {
        //this class controls the keyListener and mouseListener and the functions performed
        //by the individual controls
        //private static game.Tetris game;

        Controller(Tetris game) {
           // Controller.game = game;
        }


        //The following methods are the default implementations of the MouseListener interface.

        public void mousePressed(MouseEvent mouseEvent) {
        }

        public void mouseReleased(MouseEvent mouseEvent) {
        }

        public void mouseEntered(MouseEvent mouseEvent) {
        }

        public void mouseExited(MouseEvent mouseEvent) {
        }

        public void mouseClicked(MouseEvent mouseEvent) {

        //Below are the mouse events.
        //Move LEFT when Button1 is pressed
        //Move RIGHT when Button3 is pressed
        //ROTATE when Button2 is pressed
            switch (mouseEvent.getButton()) {
                case MouseEvent.BUTTON1: {
                    if (!paused) {currentPiece.moveSide(-1,board);}
                    break;
                }
                case MouseEvent.BUTTON3: {
                    if (!paused) {currentPiece.moveSide(+1,board);}
                    break;
                }
                case MouseEvent.BUTTON2: {
                    if (!paused && board.canRotate(currentPiece)) {currentPiece.rotateClockwise();}
                    break;
                }
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent event) {
            int key = event.getKeyCode();
            switch (key) {
                case KeyEvent.VK_UP:  // up arrow
                case KeyEvent.VK_KP_UP:
                    if (!paused && board.canRotate(currentPiece)) {currentPiece.rotateClockwise();}
                    break;
                case KeyEvent.VK_DOWN:  // down arrow
                case KeyEvent.VK_KP_DOWN:
                    if (!paused) {dropIt = true;}
                    break;
                case KeyEvent.VK_LEFT:  // left arrow
                case KeyEvent.VK_KP_LEFT:
                    if (!paused) {currentPiece.moveSide(-1,board);}
                    break;
                case KeyEvent.VK_RIGHT:  // right arrow
                case KeyEvent.VK_KP_RIGHT:
                    if (!paused) {currentPiece.moveSide(+1,board);}
                    break;
                case KeyEvent.VK_SPACE:  //  space bar
                    //currentPiece.drop();
                    if (!paused) {dropIt = true;}
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}

