package game;// game.Grid.java
// The base of the program
// This is the superclass extended by both game.Board and the game.Piece classes

import javax.swing.JComponent;
import java.awt.*;
import static java.awt.Color.*;

public class Grid extends JComponent {

//collection of colors for each type of block

    static Color red = new Color(255,36,55,255);    //L1
    static Color blue = new Color(37, 112,180,255);   //L2
    static Color yellow = new Color(255,200,0,255); //T
    static Color magenta = new Color(230,0,200,255);//BOX
    static Color orange = new Color(255,154,0,255); //BAR
    static Color green = new Color(0,200,0,255);    //STEP1
    static Color cyan = new Color(86,186,236,255);  //STEP2
    static Color white = new Color(255,255,255,255);//GHOSTS




    static Color[] colors =
            {null, red, blue,
                    yellow, magenta, orange,green, cyan, black, white};
	int contents[][];  // each rows is an array of integers
	
	public Grid(int[][] contents) {

		this.contents = contents;
		Dimension d = new Dimension(getColumns()*Tetris.SQUARE_SIZE,
		                            getRows()*Tetris.SQUARE_SIZE);
		setSize(d);
		setPreferredSize(d);
		setOpaque(false);
	}
		
	public int getColumns() {
		return contents[0].length;
	}

	public int getRows() {
		return contents.length;
	}

    public boolean isRowFull(int row) { //checks if the row is full (used to remove full rows)
        for (int i=0;i<this.getColumns();i++){
            if (this.contents[row][i]==0) {
                return false;
            }
        }
        return true;
    }

    public boolean isRowEmpty(int row) {  //checks if the row is empty (used to check if game is over)
        for (int i=0;i<this.getColumns();i++){
            if (this.contents[row][i]!=0) {
                return false;
            }
        }
        return true;
    }

    void paintSquare(int row, int col, Graphics g) { //prints a square at a specific location
        //color based on the number in the piece
        g.setColor(colors[contents[row][col]]);
		if (contents[row][col] != 0) {
            //color-filled rect
			g.fill3DRect(Tetris.SQUARE_SIZE*col+1,
						Tetris.SQUARE_SIZE*row+1,
						Tetris.SQUARE_SIZE-2,
						Tetris.SQUARE_SIZE-2, true);
            //black boarder
            g.setColor(black);
            g.draw3DRect(Tetris.SQUARE_SIZE * col,
                    Tetris.SQUARE_SIZE * row,
                    Tetris.SQUARE_SIZE,
                    Tetris.SQUARE_SIZE, true);
        }
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int row = 0; row < contents.length; row++) {
			for (int col = 0; col < contents[row].length; col++) {
					paintSquare(row,col,g);
			}				
		}
	}
}
