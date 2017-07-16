package Pieces;

import Board.Board;
import Board.Tile;

import java.util.ArrayList;
import java.util.List;

import static Board.Utils.eighthColumn;
import static Board.Utils.firstColumn;

public class Rook{
    private int piecePosition;
    private final pColor color;
    private static final int[] candidateMoves = {-8,-1,1,8};
    List<Integer> possibleMoves = new ArrayList<Integer>();
    List<Integer> possibleTakes = new ArrayList<Integer>();
    private int candidateCoordinate;

    public Rook(pColor color,int piecePosition){
        this.color = color;
        this.piecePosition = piecePosition;
    }

    public int getPiecePosition() {
        return piecePosition;
    }

    public void setPiecePosition(int piecePosition) {
        this.piecePosition = piecePosition;
    }

    public List<Integer> getPossibleMoves() {
        return possibleMoves;
    }

    public List<Integer> getPossibleTakes() {
        return possibleTakes;
    }
    public void calculatePossibleMoves(Board board){
        possibleMoves.clear();
        possibleTakes.clear();
        List<Tile> boardArray = board.getBoardArray();
        Tile tile;
        for(int x:candidateMoves){
            candidateCoordinate = piecePosition;

            while (true){
                if(firstColumn[candidateCoordinate]&&(x == -1)){
                    break;
                }
                if(eighthColumn[candidateCoordinate]&&(x == 1)){
                    break;
                }
                candidateCoordinate = candidateCoordinate + x;
                if(candidateCoordinate < 0 || candidateCoordinate > 63) {
                    break;
                }
                tile = boardArray.get(candidateCoordinate);

                if(tile.isTileOcupied()){
                    if(tile.getPieceColor() == color){
                        break;
                    }
                    else{
                        possibleTakes.add(candidateCoordinate);
                        break;
                    }
                }
                else {
                    possibleMoves.add(candidateCoordinate);
                }
            }
        }
    }
}
