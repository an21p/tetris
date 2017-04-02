package game;// game.Board.java
// This class extends the game.Grid superclass
// It implements tests for the current piece (COLLISION DETECTION)
// It also overrides the paintComponent function
// It only prints the a 10x20 board plus the floor
import java.awt.*;

public class Board extends Grid {
    public static final int COLUMNS = 10;
    public static final int ROWS = 23;
    public static final Color ORANGE = new Color(200,100,60,20);
    public static final Color BLUE = new Color(0,0,20,20);


    public Board() { //board initialisation
        super(new int[ROWS][COLUMNS]);
        setSize(COLUMNS*Tetris.SQUARE_SIZE,
                ROWS*Tetris.SQUARE_SIZE);
 /*     can set the sides be white blocks (walls)
        for(int i = 3 ; i<ROWS;i++){
            contents[i][0] = 9;
            contents[i][COLUMNS-1] = 9;
        }*/
        //sets the last row as white blocks (floor)
        for(int i = 0 ; i<COLUMNS;i++){
            contents[22][i] = 9;
        }
    }

    public boolean canPaste(Piece p) {
    //transposes the square one down and checks if the board is empty at that position
        boolean isWhite=false;
        int nextR = (int) p.currentR+1;
        for (int x : p.contents[0]) {
            if (x==9) {
                isWhite = true;
                break;
            }
        }
        if (!isWhite)
        {
            try {
                for (int r=0;r<p.getRows();r++) {
                    if (r + nextR > Board.ROWS-2) {
                        return false;
                    } else {
                        for (int c = 0; c < p.getColumns(); c++) {
                            if (c + p.currentC > Board.COLUMNS) {
                                return false;
                            } else {
                                if (p.contents[r][c] != 0 && this.contents[r + nextR][c + p.currentC] != 0) {
                                    return false;

                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                return false;
            }


        }
        else {//if a block is white it drops to the bottom of the board
            try {
                for (int r=0;r<p.getRows();r++) {
                    if (r + nextR > Board.ROWS-2) {
                        return false;
                    } else {
                        for (int c = 0; c < p.getColumns(); c++) {
                            if (c + p.currentC > Board.COLUMNS) {
                                return false;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    public void paste(Piece p) {
        for (int r=0;r<p.getRows();r++){
            for (int c=0; c<p.getColumns();c++){
                if (p.contents[r][c] !=0) {
                    this.contents[r+(int)p.currentR][c+p.currentC] = p.contents[r][c];
                }
            }

        }
    }

    public boolean canMoveLeft(Piece p) { //checks if the block can move to the left
        boolean isWhite=false;
        for (int x : p.contents[0]) {
            if (x==9) {
                isWhite = true;
                break;
            }
        }

        int nextC = p.currentC-1;
        int nextR = (int) p.currentR;
        for (int c=0;c<p.getColumns();c++){
            if (c+nextC<0){
                return false;
            }
            else {
                for (int r=0; r<p.getRows();r++){
                    if (r+nextR>Board.ROWS-1){
                        return false;
                    } else if (!isWhite) {

                            if (p.contents[r][c] != 0 && this.contents[r+nextR][c+nextC] !=0){
                                return false;
                            }
                    }
                }
            }
        }
        Piece tmp = new Piece(p.contents, p.type);
        tmp.currentC = nextC;
        tmp.currentR = nextR-1;
        return this.canPaste(tmp);
    }


    public boolean canMoveRight(Piece p) {  //checks if the block can move to the right
        boolean isWhite=false;
        for (int x : p.contents[0]) {
            if (x==9) {
                isWhite = true;
                break;
            }
        }

        int nextC = p.currentC+1;
        int nextR = (int) p.currentR;
        for (int c=0;c<p.getColumns();c++){
            if (c+nextC>Board.COLUMNS-1){
                return false;
            }
            else {
                for (int r=0; r<p.getRows();r++){
                    if (r+nextR>Board.ROWS){
                        return false;
                    } else if (!isWhite) {
                        if (p.contents[r][c] != 0 && this.contents[r+nextR][c+nextC] !=0){
                        return false;
                    }

                    }
                }
            }
        }
        Piece tmp = new Piece(p.contents, p.type);
        tmp.currentC = nextC;
        tmp.currentR = nextR-1;
        return this.canPaste(tmp);
    }

    synchronized boolean canRotate(Piece p) { //check if the block can be rotated
        int newColumns = p.getRows();
        int newRows = p.getColumns();
        int[][] rotatedBoard = new int[newRows][newColumns];


        for (int r=0;r<newRows;r++){
            for (int c=0;c<newColumns;c++){
                rotatedBoard[r][c]=p.contents[newColumns-1-c][r];
            }
        }


        Piece rotatedPiece = new Piece(rotatedBoard, p.type);
        rotatedPiece.currentR=p.currentR;
        rotatedPiece.currentC=p.currentC;

        return this.canPaste(rotatedPiece);

    }

    public void removeColumn (int col) { //removes column (when a BRICK piece hits it)
        for (int r=0;r<ROWS-1;r++){
            this.contents[r][col]=0;
            repaint();
            Tetris.sleep(10);
    }
        repaint();
    }

    public void removeRow(int row) { //removes a full row and moves the above rows below
        for (int c=0;c<COLUMNS;c++){
            this.contents[row][c]=0;
            repaint();
            Tetris.sleep(45);


        }

        for (int r=row;r>0;r--){
            for (int c=0;c<COLUMNS;c++){
                this.contents[r][c]=this.contents[r-1][c];
            }
        }
        repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintStripes(g);
        for (int row = 2; row < contents.length; row++) {
            for (int col = 0; col < contents[row].length; col++) {
                paintSquare(row,col,g);
            }
        }
    }
    // paints a the background of the board
    void paintStripes(Graphics g) {
        g.setColor(BLUE);
        for (int i = 0; i < COLUMNS; i+=2) {
            g.fillRect(i*Tetris.SQUARE_SIZE,2*Tetris.SQUARE_SIZE,
                    Tetris.SQUARE_SIZE,Tetris.SQUARE_SIZE*(ROWS));
        }
        g.setColor(ORANGE);
        for (int i = 1; i < COLUMNS; i+=2) {
            g.fillRect(i*Tetris.SQUARE_SIZE,2*Tetris.SQUARE_SIZE,
                    Tetris.SQUARE_SIZE,Tetris.SQUARE_SIZE*(ROWS));
        }
        g.setColor(BLUE);
        for (int i = 2; i < ROWS; i+=2) {
            g.fillRect(0,i*Tetris.SQUARE_SIZE,
                    Tetris.SQUARE_SIZE*COLUMNS,Tetris.SQUARE_SIZE);
        }
        g.setColor(ORANGE);
        for (int i = 3; i < ROWS; i+=2) {
            g.fillRect(0,i*Tetris.SQUARE_SIZE,
                    Tetris.SQUARE_SIZE*COLUMNS,Tetris.SQUARE_SIZE);
        }
    }



}


