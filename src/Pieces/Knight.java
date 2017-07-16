package Pieces;

import Board.Board;
import Board.Tile;

import java.util.ArrayList;
import java.util.List;

import static Board.Utils.*;

public class Knight{
    private int piecePosition;
    private final pColor color;
    private static final int[] candidateMoves = {-17,-15,-10,-6,6,10,15,17};
    List<Integer> possibleMoves = new ArrayList<Integer>();
    List<Integer> possibleTakes = new ArrayList<Integer>();
    private int candidateCoordinate;

    public Knight(pColor color,int piecePosition){
        this.color = color;
        this.piecePosition = piecePosition;
    }

    public List<Integer> getPossibleMoves() {
        return possibleMoves;
    }

    public List<Integer> getPossibleTakes() {
        return possibleTakes;
    }

    public void setPiecePosition(int piecePosition) {
        this.piecePosition = piecePosition;
    }

    public void calculatePossibleMoves(Board board){
        possibleMoves.clear();
        possibleTakes.clear();
        List<Tile> boardArray = board.getBoardArray();
        Tile tile;
        for(int x:candidateMoves){
            candidateCoordinate = piecePosition + x;
            if(candidateCoordinate < 0 || candidateCoordinate>63){
                continue;
            }
            if(firstColumn[piecePosition]&&(x == -17 || x == -10 || x == 6 || x == 15)){
                continue;
            }
            if(secondColumn[piecePosition]&&(x == -10 || x == 6)){
                continue;
            }
            if(seventhColumn[piecePosition]&&(x == -6 || x == 10)){
                continue;
            }
            if(eighthColumn[piecePosition]&&(x == -15 || x == -6 || x == 10 || x == 17)){
                continue;
            }
            tile = boardArray.get(candidateCoordinate);
            if(tile.isTileOcupied()){
                if(tile.getPieceColor() == color){
                    continue;
                }
                else{
                    possibleTakes.add(candidateCoordinate);
                }
            }
            else {possibleMoves.add(candidateCoordinate);
            }
        }
    }
}