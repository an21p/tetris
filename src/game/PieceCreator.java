package game;// PieceFactory.java
// This class creates a random piece
// I have not used the square to block way I have implemented collision detection in the game.Board class instead
// This eliminates the need of a base line structure such as the square
// This class can easily be extended to include more shapes, or choose between different arrays of objects based on
// difficulty

import java.util.Random;

public class PieceCreator {
    private static int created = 0; //holds the number of pieces created
    private static Random r = new Random();

    public static final int[][] BRICK =
            {{1,1}
            };

    public static final int[][] L1 =
            {{1,1},
            {0,1},
            {0,1}
            };

    public static final int[][] L2 =
            {{0,1},
            {0,1},
            {1,1}
            };

    public static final int[][] T =
            {{1,0},
            {1,1},
            {1,0}
            };

    public static final int[][] BOX =
            {{1,1},
            {1,1}
            };

    public static final int[][] LINE =
            {{1,1,1,1}
            };

    public static final int[][] S =
            {{1,0},
            {1,1},
            {0,1}
            };

    public static final int[][] Z =
            {{0,1},
            {1,1},
            {1,0}
            };

    public static final int[][][] SHAPES = {L1,L2,T,BOX,LINE,S,Z,BRICK};
    public static final int[][][] SHAPES2 = {BOX,LINE};  //remove row debugging
    public static final int[][][] SHAPES3 = {L1};       //rotation debugging

    public static Piece createPiece() {
        created++;
        //int[][] s = SHAPES3[(int) (Math.random()*SHAPES3.length)];
        //int[][] s = SHAPES2[(int) (Math.random()*SHAPES2.length)];
        //Random game.Piece
        int type = r.nextInt(SHAPES.length);
        if (type == 7 && created<50) { //limits the BRICKS(they become more frequent after 50 pieces have been created)
            type = r.nextInt(SHAPES.length);
            if (type > 5) {
                type = 7;
            }
        }
        int[][] s = SHAPES[type];


        //choosing a random color

        int rColor;
        while (true) { //limits the white blocks (they become more frequent after 50 pieces have been created)
            rColor = r.nextInt(Grid.colors.length);
            if (rColor == 9 && created<50) {
                rColor = r.nextInt(Grid.colors.length);
                if (rColor > 6) {
                    rColor = 9;
                }
            }
            if (rColor>0) {
                break;
            }
        }

        //colonising the piece
        for (int row = 0; row < s.length; row++) {
            for (int col = 0; col < s[row].length; col++) {
                if (s[row][col]!=0) {
                    if (rColor == 9){
                        s[row][col]=rColor;
                    }else{
                        s[row][col]=type+1;

                    }
                }
            }
        }

        return new Piece(s,type);

    }




}
