package game;// game.Piece.java
// A piece has a Top Left position at ROW : currentR and COLUMN : currentC
// It has a type which sets the shape and color

public class Piece extends Grid {
	int currentC;     // current COLUMN location on the board
	double currentR;  // current ROW    location on the board
    int type;

	public Piece(int shape[][],int type) {
		super(shape);
		currentC = 4;
		currentR = 0;
		updateLocation();
        this.type=type;
	}
		
	void updateSize() { //updates the size when rotated
		setSize(Tetris.SQUARE_SIZE*getColumns(),
				Tetris.SQUARE_SIZE*getRows());
	}
	
	void updateLocation() { //updated the location when top left is altered
		setLocation(Tetris.SQUARE_SIZE*currentC,
					(int) (Tetris.SQUARE_SIZE*currentR));
	}
	
	synchronized void moveDown() {
        //up to the row where topleft+number of rows is less than the MAX ROWS
        if(currentR+getRows()<Board.ROWS) {
            currentR++;
        }
        updateLocation();

	}

	synchronized void moveLeft() {
        //all checks are performed in board if the program comes here there is no chance of moving over the borders
        //but to avoid any unforeseen errors a check is applied so there is no ArrayOutOfBoundsException
        if(currentC>0) {
            currentC--;
        }
        updateLocation();
	}
	
	synchronized void moveRight() {
        //all checks are performed in board if the program comes here there is no chance of moving over the borders
        //but to avoid any unforeseen errors a check is applied so there is no ArrayOutOfBoundsException
        if(currentC+getColumns()< Board.COLUMNS) {
            currentC++;
        }
        updateLocation();
	}

    synchronized void moveSide(int x, Board board){
        //this checks if moves are possible
        if (x<0){        if (board.canMoveLeft(this)) {moveLeft();} }
        else if (x>0) {        if(board.canMoveRight(this)) {moveRight();} }
    }

	synchronized void rotateClockwise() {
        //clockwise rotation
        int newColumns = this.getRows();
        int newRows = this.getColumns();
        int[][] rotatedBoard = new int[newRows][newColumns];


            for (int r=0;r<newRows;r++){
                for (int c=0;c<newColumns;c++){
                    rotatedBoard[r][c]=this.contents[newColumns-1-c][r];
                }
            }


        this.contents = rotatedBoard;
        updateSize();

    }
	
	void fall() {
        //moves down and waits 5 seconds so the graphic of the block/piece appears to move
        moveDown();
        Tetris.sleep(5);

	}

}
