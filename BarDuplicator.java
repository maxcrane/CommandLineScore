import java.util.*;

/**
 * Write a description of class NoteDuplicator here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BarDuplicator implements Plugin
{
    
    
    public void doThing(Score score){
        ArrayList<Bar> bars = score.getBars();
        Scanner scan = score.getInScanner();
        
        
        System.out.println("Enter bar # to duplicate: 1-" + bars.size());
        int ndxToDup = scan.nextInt() - 1;
        System.out.println("Enter index # to insert: 0-" + bars.size());
        int ndxToPlace = scan.nextInt();
        
        
        Bar newBar = bars.get(ndxToDup).duplicateBar();
        score.addBar(ndxToPlace, newBar);
    }
    
    
    public String getMenuItem(){
        return " (dupb) duplicate bar ";
    }
    
    public String getCommandString(){
        return "dupb";
    }
}
