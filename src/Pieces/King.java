package Pieces;

import Board.Board;
import Board.Tile;

import java.util.ArrayList;
import java.util.List;

import static Board.Utils.eighthColumn;
import static Board.Utils.firstColumn;

public class King{
    private int piecePosition;
    private final pColor color;
    private boolean hasMoved = false;
    private static final int[] candidateMoves = {-9,-8,-7,-1,1,7,8,9};
    List<Integer> possibleMoves = new ArrayList<Integer>();
    List<Integer> possibleTakes = new ArrayList<Integer>();
    private int candidateCoordinate;

    public King(pColor color,int piecePosition){
        this.color = color;
        this.piecePosition = piecePosition;
    }

    public int getPiecePosition() {
        return piecePosition;
    }

    public void setPiecePosition(int piecePosition) {
        this.piecePosition = piecePosition;
        hasMoved = true;
    }

    public List<Integer> getPossibleMoves() {
        return possibleMoves;
    }

    public List<Integer> getPossibleTakes() {
        return possibleTakes;
    }

    public void calculatePossibleMoves(Board board, boolean qsCastling,boolean ksCastling, boolean inCheck){
        possibleMoves.clear();
        possibleTakes.clear();
        List<Tile> boardArray = board.getBoardArray();
        Tile tile;
        for(int x:candidateMoves){
            candidateCoordinate = piecePosition + x;
            if(firstColumn[piecePosition]&&(x == -9 || x == 7 || x == -1)){
                continue;
            }
            if(eighthColumn[piecePosition]&&(x == 9 || x == -7 || x == 1)){
                continue;
            }
            if(candidateCoordinate < 0 || candidateCoordinate > 63) {
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
            else {
                possibleMoves.add(candidateCoordinate);
            }
        }
        if(boardArray.get(60).getPieceType() == pType.king && !inCheck) {
            if (qsCastling && !boardArray.get(57).isTileOcupied() && !boardArray.get(58).isTileOcupied() && !boardArray.get(59).isTileOcupied())
                possibleMoves.add(58);
            if (ksCastling && !boardArray.get(61).isTileOcupied() && !boardArray.get(62).isTileOcupied())
                possibleMoves.add(62);
        }else if(boardArray.get(59).getPieceType() == pType.king){
            if (qsCastling && !boardArray.get(60).isTileOcupied() && !boardArray.get(61).isTileOcupied() && !boardArray.get(62).isTileOcupied())
                possibleMoves.add(61);
            if (ksCastling && !boardArray.get(57).isTileOcupied() && !boardArray.get(58).isTileOcupied())
                possibleMoves.add(57);
        }
    }
}
