package Pieces;

import Board.Board;
import Board.Tile;

import java.util.ArrayList;
import java.util.List;

import static Board.Utils.*;
import static Board.Utils.eighthColumn;

public class Check {
    private int kingPosition;
    private final pColor color;
    private static final int[] pawnCheck = {-7,-9};
    private static final int[] knightCheck = {-17,-15,-10,-6,6,10,15,17};
    private static final int[] kingCheck = {-9,-8,-7,-1,1,7,8,9};
    private static final int[] bishopCheck = {-9,-7,7,9};
    private static final int[] rookCheck = {-8,-1,1,8};
    List<Integer> possibleMoves = new ArrayList<Integer>();
    List<Integer> possibleTakes = new ArrayList<Integer>();
    private int candidateCoordinate;
    private boolean isCheck = false;
    Board tempBoard = new Board(pColor.white);


    public Check(pColor color) {
        this.color = color;
    }

    public void setPiecePosition(int kingPosition) {
        this.kingPosition = kingPosition;
    }

    public void checkPawn(Board board) {
        List<Tile> boardArray = board.getBoardArray();
        Tile tile;
        for (int x : pawnCheck) {
            candidateCoordinate = kingPosition + x;
            if(eighthColumn[kingPosition]&& x== -7){
                continue;
            }
            if(firstColumn[kingPosition]&& x == -9 ){
                continue;
            }
            if (candidateCoordinate < 0 || candidateCoordinate > 63) {
                continue;
            }
            tile = boardArray.get(candidateCoordinate);

            if (tile.isTileOcupied()) {
                if (tile.getPieceColor() == color) {
                    continue;
                } else {
                    if (tile.getPieceType() == pType.pawn) {
                        isCheck = true;
                    }
                }
            }
        }
    }
    public void checkKnight(Board board) {
        List<Tile> boardArray = board.getBoardArray();
        Tile tile;
        for (int x : knightCheck) {
            candidateCoordinate = kingPosition + x;
            if(candidateCoordinate < 0 || candidateCoordinate>63){
                continue;
            }
            if(firstColumn[kingPosition]&&(x == -17 || x == -10 || x == 6 || x == 15)){
                continue;
            }
            if(secondColumn[kingPosition]&&(x == -10 || x == 6)){
                continue;
            }
            if(seventhColumn[kingPosition]&&(x == -6 || x == 10)){
                continue;
            }
            if(eighthColumn[kingPosition]&&(x == -15 || x == -6 || x == 10 || x == 17)){
                continue;
            }
            tile = boardArray.get(candidateCoordinate);

            if (tile.isTileOcupied()) {
                if (tile.getPieceColor() == color) {
                    continue;
                } else {
                    if(tile.getPieceType() == pType.knight) {
                        isCheck = true;
                    }
                }
            }
        }
    }
    public void checkKing(Board board){
        List<Tile> boardArray = board.getBoardArray();
        Tile tile;
        for(int x:kingCheck){
            candidateCoordinate = kingPosition + x;
            if(firstColumn[kingPosition]&&(x == -9 || x == 7 || x == -1)){
                continue;
            }
            if(eighthColumn[kingPosition]&&(x == 9 || x == -7 || x == 1)){
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
                    if(tile.getPieceType() == pType.king){
                        isCheck = true;
                    }
                }
            }
        }
    }
    public void checkBishop(Board board){
        List<Tile> boardArray = board.getBoardArray();
        Tile tile;
        for(int x:bishopCheck){
            candidateCoordinate = kingPosition;

            while (true){
                if(firstColumn[candidateCoordinate]&&(x == -9 || x == 7)){
                    break;
                }
                if(eighthColumn[candidateCoordinate]&&(x == 9 || x == -7)){
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
                        if(tile.getPieceType() == pType.bishop || tile.getPieceType() == pType.queen){
                            isCheck = true;
                        }
                        break;
                    }
                }
            }
        }
    }
    public void checkRook(Board board){
        List<Tile> boardArray = board.getBoardArray();
        Tile tile;
        for(int x:rookCheck){
            candidateCoordinate = kingPosition;

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
                        if(tile.getPieceType() == pType.rook || tile.getPieceType() == pType.queen){
                            isCheck = true;
                        }
                        break;
                    }
                }
            }
        }
    }

    public boolean isInCheck(Board board,int kingPosition){
        this.kingPosition = kingPosition;
        isCheck = false;
        checkRook(board);
        checkBishop(board);
        checkKing(board);
        checkKnight(board);
        checkPawn(board);
        return isCheck;
    }

    public List<Integer> testMoves(Board board, int kingPosition, List<Integer> moves, int workingTilePosition){
        List<Integer> testingMoves = moves;
        List<Integer> toRemove = new ArrayList<Integer>();
        Tile workingTile;
        int king;
        for(int i = 0; i < testingMoves.size();i++){
            king = kingPosition;
            Board testBoard = new Board(board);
            workingTile = testBoard.getBoardArray().get(workingTilePosition);
            if (workingTile.getPieceType() == pType.king){
                king = testingMoves.get(i);
            }
            testBoard.getBoardArray().get(testingMoves.get(i)).setPieceType(workingTile.getPieceType(),
                    workingTile.getPieceColor());
            workingTile.setPieceType(null, null);
            if (isInCheck(testBoard, king)) toRemove.add(i);
        }
        if(toRemove.size() > 0) for (int x = toRemove.size()-1; x >= 0; x--) {
            int f = toRemove.get(x);
            testingMoves.remove(f);
        }
        return testingMoves;
    }
}