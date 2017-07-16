package Board;

import Pieces.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Tile extends Rectangle {
    private final pColor color;
    private pType pieceType;
    private pColor pieceColor;
    private final int position;
    private final int xCoordinate;
    private final int yCoordinate;
    private boolean mouseOver;

    Tile(pColor color,int position){
        this.color = color;
        this.position = position;
        if (position > 55){
            this.yCoordinate = 490;
            this.xCoordinate = (position-56)*70;
        }
        else if(position > 47){
            this.yCoordinate = 420;
            this.xCoordinate = (position-48)*70;
        }
        else if(position > 39){
            this.yCoordinate = 350;
            this.xCoordinate = (position-40)*70;
        }
        else if(position > 31){
            this.yCoordinate = 280;
            this.xCoordinate = (position-32)*70;
        }
        else if(position > 23){
            this.yCoordinate = 210;
            this.xCoordinate = (position-24)*70;
        }
        else if(position > 15){
            this.yCoordinate = 140;
            this.xCoordinate = (position-16)*70;
        }
        else if(position > 7){
            this.yCoordinate = 70;
            this.xCoordinate = (position-8)*70;
        }
        else {
            this.yCoordinate = 0;
            this.xCoordinate = position*70;
        }
        setWidth(70);
        setHeight(70);
        setFill(Color.TRANSPARENT);
        setStroke(Color.TRANSPARENT);
        setStrokeWidth(3);
        relocate(this.xCoordinate, this.yCoordinate);
        setOnMouseEntered(event -> mouseOver = true );
        setOnMouseExited(event -> mouseOver = false);
    }

    public Tile(Tile tile){
        this. color = tile.getColor();
        this.pieceType = tile.getPieceType();
        this.pieceColor = tile.getPieceColor();
        this.position = tile.getPosition();
        this.xCoordinate = tile.getxCoordinate();
        this.yCoordinate = tile.getyCoordinate();
        this.mouseOver = tile.isMouseOver();
        setWidth(70);
        setHeight(70);
        setFill(Color.TRANSPARENT);
        setStroke(Color.TRANSPARENT);
        setStrokeWidth(3);
        relocate(this.xCoordinate, this.yCoordinate);
        setOnMouseEntered(event -> mouseOver = true);
        setOnMouseExited(event -> mouseOver = false);
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public pColor getColor() {
        return color;
    }

    public pColor getPieceColor() {
        return pieceColor;
    }

    public pType getPieceType() {
        return pieceType;
    }

    public void setPieceType(pType pieceType, pColor pieceColor) {
        this.pieceType = pieceType;
        this.pieceColor = pieceColor;
    }

    public int getPosition() {
        return position;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public boolean isTileOcupied(){
        if(pieceType == null){
            return false;
        }
        else{
            return true;
        }
    }
}
