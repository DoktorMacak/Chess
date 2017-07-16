package Pieces;

import Board.Board;
import Board.Tile;

import java.util.ArrayList;
import java.util.List;

import static Board.Utils.eighthColumn;
import static Board.Utils.firstColumn;
import static Board.Utils.secondRow;

public class Pawn{
    private int piecePosition;
    private final pColor color;
    private static final int[] candidateMoves = {-7,-8,-9,-16};
    List<Integer> possibleMoves = new ArrayList<Integer>();
    List<Integer> possibleTakes = new ArrayList<Integer>();
    private int candidateCoordinate;
    private boolean canDouble;

    public Pawn(pColor color,int piecePosition){
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
        canDouble = true;
        for(int x:candidateMoves){
            candidateCoordinate = piecePosition + x;
            if (x == -16 && !secondRow[piecePosition]){
                continue;
            }
            if(candidateCoordinate < 0 || candidateCoordinate > 63) {
                continue;
            }
            if(firstColumn[piecePosition]&& x == -9 ){
                continue;
            }
            if(eighthColumn[piecePosition]&& x== -7){
                continue;
            }
            tile = boardArray.get(candidateCoordinate);

            if(tile.isTileOcupied()){
                if (x == -8){
                canDouble = false;
                }
                if(tile.getPieceColor() == color){
                    continue;
                }
                else{
                    if(x != -8 && x != -16) {
                        possibleTakes.add(candidateCoordinate);
                    }
                }
            }
            else {
                if(x != -7 && x != -9 &&(x != -16 || canDouble)) {
                    possibleMoves.add(candidateCoordinate);
                }
            }
        }
    }
}
