package CustomEvents;

import java.util.ArrayList;
import java.util.List;


public class eventSender{
    private static List<customEvents> listeners = new ArrayList<customEvents>();

    public void addListener(customEvents toAdd){
        listeners.add(toAdd);
    }

    public void sendMove(int startingPosition, int targetPosition) {
        for(customEvents x:listeners){
            x.gotMove(startingPosition,targetPosition);
        }
    }
}
