import Pieces.pColor;

public class Player {
    private final pColor color;
    private final pColor enemyColor;


    public Player(pColor color){
        this.color = color;
        if(color == pColor.white){
            enemyColor = pColor.black;
        }
        else{
            enemyColor = pColor.white;
        }
    }

    public pColor getColor() {
        return color;
    }

    public pColor getEnemyColor() {
        return enemyColor;
    }
}
