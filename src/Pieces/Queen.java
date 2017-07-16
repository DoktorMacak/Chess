package Pieces;

import Board.Board;
import Board.Tile;

import java.util.ArrayList;
import java.util.List;

import static Board.Utils.eighthColumn;
import static Board.Utils.firstColumn;

public class Queen{
    private int piecePosition;
    private final pColor color;
    private static final int[] candidateMoves = {-9,-8,-7,-1,1,7,8,9};
    List<Integer> possibleMoves = new ArrayList<Integer>();
    List<Integer> possibleTakes = new ArrayList<Integer>();
    private int candidateCoordinate;

    public Queen(pColor color,int piecePosition){
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
                if(firstColumn[candidateCoordinate]&&(x == -9 || x == 7 || x == -1)){
                    break;
                }
                if(eighthColumn[candidateCoordinate]&&(x == 9 || x == -7 || x == 1)){
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
