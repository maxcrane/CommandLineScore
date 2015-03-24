import java.util.*;

/**
 * Write a description of class NoteDuplicator here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BarReverser implements Plugin
{
    public HashMap<Integer, Integer> lengthMap;
    
    
    public void doThing(Score score){
        ArrayList<Bar> theBar = score.getBars();
        Scanner inScan = score.getInScanner();
        
        
        System.out.println("Enter bar # to reverse: 1-" + theBar.size());
        int toRev = Integer.parseInt(inScan.nextLine()) - 1;
        Bar barToRev = theBar.get(toRev);
        
        ArrayList<Note> toReverse = new ArrayList<Note>();    
        
        
        
        toReverse = barToRev.getNotes();
        
        for(Note n: toReverse){
            barToRev.deleteNote(n);
            //System.out.println("deleteing note at " + n.getRow() + "/" + n.getCol());
            int newRow = n.getRow();
            int newCol = barToRev.numCols() - n.getCol() - 1;
            //System.out.println("adding note at " + newRow + "/" + newCol);
            barToRev.addNote(newRow, newCol, new Note(n.getDuration(), newRow, newCol));
        }
        
       
    }
    
    
    
    
    
    public String getMenuItem(){
        return "(rb) reverse bar ";
    }
    
    public String getCommandString(){
        return "rb";
    }
}
