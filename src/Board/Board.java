package Board;

import Pieces.pColor;
import java.util.ArrayList;
import java.util.List;

public class Board {
    List<Tile> boardArray = new ArrayList<Tile>();
    public Board(pColor tempColor){
        for(int i = 0;i<=63;i++){
            boardArray.add(i,new Tile(tempColor,i));
            if(tempColor == pColor.black){
                tempColor = pColor.white;
            }
            else{
                tempColor = pColor.black;
            }
            if(i == 7 || i == 15 || i == 23 || i == 31 || i == 39 || i == 47 || i == 55){
                if(tempColor == pColor.black){
                    tempColor = pColor.white;
                }
                else{
                    tempColor = pColor.black;
                }
            }
        }
    }
    public Board(Board board){
        for(int i = 0;i<=63;i++) {
            boardArray.add(new Tile(board.getBoardArray().get(i)));
        }
    }
    public List<Tile> getBoardArray() {
        return boardArray;
    }
}
