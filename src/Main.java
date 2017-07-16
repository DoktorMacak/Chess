import Board.Board;
import Board.Tile;
import CustomEvents.customEvents;
import Pieces.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import CustomEvents.eventSender;

import java.util.ArrayList;
import java.util.List;


public class Main extends Application implements customEvents {
    static Board board;
    static Player player;
    Rook rook;
    Knight knight;
    Bishop bishop;
    Queen queen;
    King king;
    Pawn pawn;
    Check check;
    List<Integer> possibleMoves = new ArrayList<>();
    List<Integer> possibleTakes = new ArrayList<>();
    serverCommunication communication;
    Canvas canvas = new Canvas(560,560);
    GraphicsContext draw = canvas.getGraphicsContext2D();
    static Thread receiver;
    boolean qsCastling = true;
    boolean ksCastling = true;
    Tile workingTile;
    static boolean myTurn = true;
    static int kingPosition = 60;
    private boolean selectingMove = false;
    private boolean checkMate = false;
    private boolean staleMate = false;
    //Loading images
    Image blackKnight  = new Image("res/Black pieces/Knight.png", 50, 50, true, true);
    Image blackKing = new Image("res/Black pieces/King.png", 50, 50, true, true);
    Image blackQueen  = new Image("res/Black pieces/Queen.png", 50, 50, true, true);
    Image blackPawn = new Image("res/Black pieces/Pawn.png", 50, 50, true, true);
    Image blackBishop = new Image("res/Black pieces/Bishop.png", 50, 50, true, true);
    Image blackRook = new Image("res/Black pieces/Rook.png", 50, 50, true, true);

    Image whiteKnight = new Image("res/White pieces/Knight.png", 50, 50, true, true);
    Image whiteKing = new Image("res/White pieces/King.png", 50, 50, true, true);
    Image whiteQueen = new Image("res/White pieces/Queen.png", 50, 50, true, true);
    Image whiteBishop = new Image("res/White pieces/Bishop.png", 50, 50, true, true);
    Image whitePawn = new Image("res/White pieces/Pawn.png", 50, 50, true, true);
    Image whiteRook = new Image("res/White pieces/Rook.png", 50, 50, true, true);

    Image checkMateImage = new Image("res/Checkmate.png");
    Image staleMateImage = new Image("res/Stalemate.png");




    public static void main(String[] args){

        board = new Board(pColor.white);
        boardInitialSetup(board, pColor.black, pColor.white);
        launch();

        receiver.stop();



    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Main");

        Group group = new Group();
        Scene primaryScene = new Scene(group);
        //selecting server address
        VBox settings = new VBox();
        Label text = new Label("Unesite adresu servera");
        text.setStyle("-fx-font-size: 30");
        Scene introScene = new Scene(settings,400,200);
        TextField addressSelection = new TextField("123.123.123.123");
        Button start = new Button("Start");
        start.setOnAction(event -> {

            System.out.printf("1  "+addressSelection.getText());
            System.out.printf("2  "+addressSelection.getPromptText());
            System.out.printf("3  "+addressSelection.getPromptText());
            communication = new serverCommunication(addressSelection.getText());
            if(communication.isWhite()) {
                player = new Player(pColor.white);
            }else {
                player = new Player(pColor.black);
                myTurn = false;
                kingPosition = 59;
            }

            rook = new Rook(player.getColor(),56);
            knight = new Knight(player.getColor(),57);
            bishop = new Bishop(player.getColor(),58);
            queen = new Queen(player.getColor(),59);
            king = new King(player.getColor(),60);
            pawn = new Pawn(player.getColor(),48);
            check = new Check(player.getColor());
            receiver = new Thread(communication);
            receiver.start();
            System.out.println(player.getColor());
            boardInitialSetup(board, player.getColor(), player.getEnemyColor());
            System.out.println(player.getColor());
            primaryStage.setScene(primaryScene);
            drawBoard(draw);
        });
        start.setMinHeight(20);
        start.setMinWidth(100);

        settings.getChildren().addAll(text,addressSelection,start);
        settings.setSpacing(10);
        settings.setMargin(start,new Insets(10,0,0,150));

        primaryStage.setScene(introScene);
        //game
        group.getChildren().add(canvas);
        for(Tile tile:board.getBoardArray()){
            group.getChildren().add(tile);
        }
        eventSender x1 = new eventSender();
        group.setOnMouseClicked(event -> {
            event.consume();
            if(myTurn) {
                if (!selectingMove) {
                    possibleTakes.clear();
                    possibleMoves.clear();
                    for (Tile currentTile : board.getBoardArray()) {
                        if (currentTile.isMouseOver() && currentTile.isTileOcupied() && currentTile.getPieceColor() == player.getColor()) {
                            selectingMove = true;
                            workingTile = currentTile;
                            switch (currentTile.getPieceType()) {
                                case king: {
                                    king.setPiecePosition(currentTile.getPosition());
                                    king.calculatePossibleMoves(board,qsCastling,ksCastling,check.isInCheck(board,kingPosition));
                                    possibleMoves = king.getPossibleMoves();
                                    possibleTakes = king.getPossibleTakes();
                                    break;
                                }
                                case queen: {
                                    queen.setPiecePosition(currentTile.getPosition());
                                    queen.calculatePossibleMoves(board);
                                    possibleMoves = queen.getPossibleMoves();
                                    possibleTakes = queen.getPossibleTakes();
                                    break;
                                }
                                case bishop: {
                                    bishop.setPiecePosition(currentTile.getPosition());
                                    bishop.calculatePossibleMoves(board,kingPosition);
                                    possibleMoves = bishop.getPossibleMoves();
                                    possibleTakes = bishop.getPossibleTakes();
                                    break;
                                }
                                case rook: {
                                    rook.setPiecePosition(currentTile.getPosition());
                                    rook.calculatePossibleMoves(board);
                                    possibleMoves = rook.getPossibleMoves();
                                    possibleTakes = rook.getPossibleTakes();
                                    break;
                                }
                                case pawn: {
                                    pawn.setPiecePosition(currentTile.getPosition());
                                    pawn.calculatePossibleMoves(board);
                                    possibleMoves = pawn.getPossibleMoves();
                                    possibleTakes = pawn.getPossibleTakes();
                                    break;
                                }
                                case knight: {
                                    knight.setPiecePosition(currentTile.getPosition());
                                    knight.calculatePossibleMoves(board);
                                    possibleMoves = knight.getPossibleMoves();
                                    possibleTakes = knight.getPossibleTakes();
                                    break;
                                }
                            }
                        }
                    }
                    possibleMoves = check.testMoves(board, kingPosition, possibleMoves, workingTile.getPosition());
                    possibleTakes = check.testMoves(board, kingPosition, possibleTakes, workingTile.getPosition());
                    for (int x : possibleMoves) {
                        board.getBoardArray().get(x).setStroke(Color.LIME);
                    }
                    for (int x : possibleTakes) {
                        board.getBoardArray().get(x).setStroke(Color.RED);
                    }
                } else {
                    for (int x : possibleMoves) {
                        if (board.getBoardArray().get(x).isMouseOver()) {
                            board.getBoardArray().get(kingPosition).setStroke(Color.TRANSPARENT);
                            communication.sendMove(workingTile.getPosition(),x);
                            if(player.getColor() == pColor.white && x == 58 && kingPosition == 60){
                                board.getBoardArray().get(59).setPieceType(pType.rook, player.getColor());
                                board.getBoardArray().get(56).setPieceType(null, null);
                            }else if(player.getColor() == pColor.white && x == 62 && kingPosition == 60){
                                board.getBoardArray().get(61).setPieceType(pType.rook, player.getColor());
                                board.getBoardArray().get(63).setPieceType(null, null);
                            }else if(player.getColor() == pColor.black && x == 57 && kingPosition == 59){
                                board.getBoardArray().get(58).setPieceType(pType.rook, player.getColor());
                                board.getBoardArray().get(56).setPieceType(null, null);
                            }else if(player.getColor() == pColor.black && x == 61 && kingPosition == 59){
                                board.getBoardArray().get(60).setPieceType(pType.rook, player.getColor());
                                board.getBoardArray().get(63).setPieceType(null, null);
                            }
                            if (workingTile.getPieceType() == pType.king){
                                kingPosition = board.getBoardArray().get(x).getPosition();
                                qsCastling = false;
                                ksCastling = false;
                            }else if(workingTile.getPieceType() == pType.rook &&(qsCastling == true || ksCastling == true)){
                                if(workingTile.getPosition() == 56) qsCastling = false;
                                if(workingTile.getPosition() == 63) ksCastling = false;
                            }
                            board.getBoardArray().get(x).setPieceType(workingTile.getPieceType(), workingTile.getPieceColor());
                            workingTile.setPieceType(null, null);
                            myTurn = false;
                            workingTile = board.getBoardArray().get(x);
                            if(workingTile.getPosition() < 8 && workingTile.getPieceType() == pType.pawn){
                               workingTile.setPieceType(pType.queen,workingTile.getPieceColor());
                            }
                        }
                        board.getBoardArray().get(x).setStroke(Color.TRANSPARENT);
                    }
                    for (int x : possibleTakes) {
                        if (board.getBoardArray().get(x).isMouseOver()) {
                            board.getBoardArray().get(kingPosition).setStroke(Color.TRANSPARENT);
                            if (workingTile.getPieceType() == pType.king){
                                kingPosition = board.getBoardArray().get(x).getPosition();
                                qsCastling = false;
                                ksCastling = false;
                            }else if(workingTile.getPieceType() == pType.rook &&(qsCastling == true || ksCastling == true)){
                                if(workingTile.getPosition() == 56) qsCastling = false;
                                if(workingTile.getPosition() == 63) ksCastling = false;
                            }
                            communication.sendMove(workingTile.getPosition(),x);
                            board.getBoardArray().get(x).setPieceType(workingTile.getPieceType(), workingTile.getPieceColor());
                            workingTile.setPieceType(null, null);
                            myTurn = false;
                            board.getBoardArray().get(kingPosition).setStroke(Color.TRANSPARENT);
                            workingTile = board.getBoardArray().get(x);
                            if(workingTile.getPosition() < 8 && workingTile.getPieceType() == pType.pawn){
                                workingTile.setPieceType(pType.queen,workingTile.getPieceColor());
                            }
                        }
                        board.getBoardArray().get(x).setStroke(Color.TRANSPARENT);
                    }
                    possibleTakes.clear();
                    possibleMoves.clear();
                }
                if (possibleMoves.size() == 0 && possibleTakes.size() == 0) {
                    selectingMove = false;
                }
                drawBoard(draw);
            }
        });
        x1.addListener(this);
        drawBoard(draw);

        primaryStage.show();

    }

    private void drawBoard(GraphicsContext draw){
        for (Tile currentTile:board.getBoardArray()){
            if (currentTile.getColor() == pColor.white){
                draw.setFill(Color.WHITE);
            }
            else {
                draw.setFill(Color.BLACK);
            }
            draw.fillRect(currentTile.getxCoordinate(),currentTile.getyCoordinate(),100,100);
            if (currentTile.isTileOcupied()){
                if (currentTile.getPieceColor() == pColor.black) {
                    switch (currentTile.getPieceType()) {
                        case knight:
                            draw.drawImage(blackKnight, currentTile.getxCoordinate() + 10, currentTile.getyCoordinate() + 10);
                            break;
                        case king:
                            draw.drawImage(blackKing, currentTile.getxCoordinate() + 10, currentTile.getyCoordinate() + 10);
                            break;
                        case queen:
                            draw.drawImage(blackQueen, currentTile.getxCoordinate() + 10, currentTile.getyCoordinate() + 10);
                            break;
                        case pawn:
                            draw.drawImage(blackPawn, currentTile.getxCoordinate() + 10, currentTile.getyCoordinate() + 10);
                            break;
                        case rook:
                            draw.drawImage(blackRook, currentTile.getxCoordinate() + 10, currentTile.getyCoordinate() + 10);
                            break;
                        case bishop:
                            draw.drawImage(blackBishop, currentTile.getxCoordinate() + 10, currentTile.getyCoordinate() + 10);
                    }
                }
                else{
                    switch (currentTile.getPieceType()) {
                        case knight:
                            draw.drawImage(whiteKnight, currentTile.getxCoordinate() + 10, currentTile.getyCoordinate() + 10);
                            break;
                        case king:
                            draw.drawImage(whiteKing, currentTile.getxCoordinate() + 10, currentTile.getyCoordinate() + 10);
                            break;
                        case queen:
                            draw.drawImage(whiteQueen, currentTile.getxCoordinate() + 10, currentTile.getyCoordinate() + 10);
                            break;
                        case pawn:
                            draw.drawImage(whitePawn, currentTile.getxCoordinate() + 10, currentTile.getyCoordinate() + 10);
                            break;
                        case rook:
                            draw.drawImage(whiteRook, currentTile.getxCoordinate() + 10, currentTile.getyCoordinate() + 10);
                            break;
                        case bishop:
                            draw.drawImage(whiteBishop, currentTile.getxCoordinate() + 10, currentTile.getyCoordinate() + 10);
                    }
                }
            }
        }
    }


    @Override
    public void gotMove(int startingPosition, int targetPosition) {
        workingTile = board.getBoardArray().get(startingPosition);
        if(workingTile.getPieceType() == pType.king){
            if(player.getColor() == pColor.white){
                if(workingTile.getPosition() == 4 && targetPosition == 2){
                    board.getBoardArray().get(3).setPieceType(pType.rook, player.getEnemyColor());
                    board.getBoardArray().get(0).setPieceType(null, null);
                }else if(workingTile.getPosition() == 4 && targetPosition == 6){
                    board.getBoardArray().get(5).setPieceType(pType.rook, player.getEnemyColor());
                    board.getBoardArray().get(7).setPieceType(null, null);
                }
            }else {
                if(workingTile.getPosition() == 3 && targetPosition == 1){
                    board.getBoardArray().get(2).setPieceType(pType.rook, player.getEnemyColor());
                    board.getBoardArray().get(0).setPieceType(null, null);
                }else if(workingTile.getPosition() == 3 && targetPosition == 5){
                    board.getBoardArray().get(4).setPieceType(pType.rook, player.getEnemyColor());
                    board.getBoardArray().get(7).setPieceType(null, null);
                }
            }
        }
        board.getBoardArray().get(targetPosition).setPieceType(workingTile.getPieceType(), workingTile.getPieceColor());
        workingTile.setPieceType(null, null);
        myTurn = true;

        workingTile = board.getBoardArray().get(targetPosition);
        if(workingTile.getPosition() > 55 && workingTile.getPieceType() == pType.pawn){
            workingTile.setPieceType(pType.queen,workingTile.getPieceColor());
        }
        if (check.isInCheck(board, kingPosition)) {
            board.getBoardArray().get(kingPosition).setStroke(Color.RED);
        }
        drawBoard(draw);
    }

    private void isCheckOrStaleMate(){
        List<Integer> moves = new ArrayList<Integer>();
        List<Integer> tempMoves;
        for (Tile tile:board.getBoardArray()){
            if(tile.isTileOcupied()) {
                if (tile.getPieceColor() == player.getColor()) {
                    switch (tile.getPieceType()) {
                        case king: {
                            king.setPiecePosition(tile.getPosition());
                            king.calculatePossibleMoves(board, false, false, true);
                            tempMoves = king.getPossibleMoves();
                            check.testMoves(board, kingPosition, tempMoves, tile.getPosition());
                            moves.addAll(tempMoves);
                            tempMoves = king.getPossibleTakes();
                            check.testMoves(board, kingPosition, tempMoves, tile.getPosition());
                            moves.addAll(tempMoves);
                        }
                        case queen: {
                            queen.setPiecePosition(tile.getPosition());
                            queen.calculatePossibleMoves(board);
                            tempMoves = queen.getPossibleMoves();
                            check.testMoves(board, kingPosition, tempMoves, tile.getPosition());
                            moves.addAll(tempMoves);
                            tempMoves = queen.getPossibleTakes();
                            check.testMoves(board, kingPosition, tempMoves, tile.getPosition());
                            moves.addAll(tempMoves);
                        }
                        case bishop: {
                            bishop.setPiecePosition(tile.getPosition());
                            bishop.calculatePossibleMoves(board, kingPosition);
                            tempMoves = bishop.getPossibleMoves();
                            check.testMoves(board, kingPosition, tempMoves, tile.getPosition());
                            moves.addAll(tempMoves);
                            tempMoves = bishop.getPossibleTakes();
                            check.testMoves(board, kingPosition, tempMoves, tile.getPosition());
                            moves.addAll(tempMoves);
                        }
                        case rook: {
                            rook.setPiecePosition(tile.getPosition());
                            rook.calculatePossibleMoves(board);
                            tempMoves = rook.getPossibleMoves();
                            check.testMoves(board, kingPosition, tempMoves, tile.getPosition());
                            moves.addAll(tempMoves);
                            tempMoves = rook.getPossibleTakes();
                            check.testMoves(board, kingPosition, tempMoves, tile.getPosition());
                            moves.addAll(tempMoves);
                        }
                        case pawn: {
                            pawn.setPiecePosition(tile.getPosition());
                            pawn.calculatePossibleMoves(board);
                            tempMoves = pawn.getPossibleMoves();
                            check.testMoves(board, kingPosition, tempMoves, tile.getPosition());
                            moves.addAll(tempMoves);
                            tempMoves = pawn.getPossibleTakes();
                            check.testMoves(board, kingPosition, tempMoves, tile.getPosition());
                            moves.addAll(tempMoves);
                        }
                        case knight: {
                            knight.setPiecePosition(tile.getPosition());
                            knight.calculatePossibleMoves(board);
                            tempMoves = knight.getPossibleMoves();
                            check.testMoves(board, kingPosition, tempMoves, tile.getPosition());
                            moves.addAll(tempMoves);
                            tempMoves = knight.getPossibleTakes();
                            check.testMoves(board, kingPosition, tempMoves, tile.getPosition());
                            moves.addAll(tempMoves);
                        }
                    }
                }
            }
        }
       if (moves.size() == 0){
           if (check.isInCheck(board, kingPosition)){
               checkMate = true;
           }else {
               staleMate = true;
           }
       }
       moves.clear();
    }

    private static void boardInitialSetup(Board board, pColor playerColor, pColor enemyColor){
        System.out.println(playerColor);
        board.getBoardArray().get(0).setPieceType(pType.rook,enemyColor);
        board.getBoardArray().get(1).setPieceType(pType.knight,enemyColor);
        board.getBoardArray().get(2).setPieceType(pType.bishop,enemyColor);
        board.getBoardArray().get(3).setPieceType(pType.queen,enemyColor);
        board.getBoardArray().get(4).setPieceType(pType.king,enemyColor);
        board.getBoardArray().get(5).setPieceType(pType.bishop,enemyColor);
        board.getBoardArray().get(6).setPieceType(pType.knight,enemyColor);
        board.getBoardArray().get(7).setPieceType(pType.rook,enemyColor);

        board.getBoardArray().get(8).setPieceType(pType.pawn,enemyColor);
        board.getBoardArray().get(9).setPieceType(pType.pawn,enemyColor);
        board.getBoardArray().get(10).setPieceType(pType.pawn,enemyColor);
        board.getBoardArray().get(11).setPieceType(pType.pawn,enemyColor);
        board.getBoardArray().get(12).setPieceType(pType.pawn,enemyColor);
        board.getBoardArray().get(13).setPieceType(pType.pawn,enemyColor);
        board.getBoardArray().get(14).setPieceType(pType.pawn,enemyColor);
        board.getBoardArray().get(15).setPieceType(pType.pawn,enemyColor);

        board.getBoardArray().get(48).setPieceType(pType.pawn,playerColor);
        board.getBoardArray().get(49).setPieceType(pType.pawn,playerColor);
        board.getBoardArray().get(50).setPieceType(pType.pawn,playerColor);
        board.getBoardArray().get(51).setPieceType(pType.pawn,playerColor);
        board.getBoardArray().get(52).setPieceType(pType.pawn,playerColor);
        board.getBoardArray().get(53).setPieceType(pType.pawn,playerColor);
        board.getBoardArray().get(54).setPieceType(pType.pawn,playerColor);
        board.getBoardArray().get(55).setPieceType(pType.pawn,playerColor);

        board.getBoardArray().get(56).setPieceType(pType.rook,playerColor);
        board.getBoardArray().get(57).setPieceType(pType.knight,playerColor);
        board.getBoardArray().get(58).setPieceType(pType.bishop,playerColor);
        board.getBoardArray().get(59).setPieceType(pType.queen,playerColor);
        board.getBoardArray().get(60).setPieceType(pType.king,playerColor);
        board.getBoardArray().get(61).setPieceType(pType.bishop,playerColor);
        board.getBoardArray().get(62).setPieceType(pType.knight,playerColor);
        board.getBoardArray().get(63).setPieceType(pType.rook,playerColor);

        if (playerColor == pColor.black){
            board.getBoardArray().get(60).setPieceType(pType.queen,playerColor);
            board.getBoardArray().get(59).setPieceType(pType.king,playerColor);
            board.getBoardArray().get(4).setPieceType(pType.queen,enemyColor);
            board.getBoardArray().get(3).setPieceType(pType.king,enemyColor);
        }
    }
}
