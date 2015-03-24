import java.util.*;

/**
 * Write a description of class NoteDuplicator here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class NoteDuplicator implements Plugin
{
    public HashMap<Integer, Integer> lengthMap;
    
    
    
    public NoteDuplicator(){
        lengthMap = new HashMap<Integer, Integer>();
        lengthMap.put(1, 16);
        lengthMap.put(2, 8);
        lengthMap.put(4, 4);
        lengthMap.put(8, 2);
        lengthMap.put(16, 1);
    }
    
    
    public void doThing(Score score){
        ArrayList<Bar> theBar = score.getBars();
        ArrayList<Integer> indicators = score.lastNoteInfo();
        
        Bar toDup = theBar.get(indicators.get(2));
        int colIncrement = lengthMap.get(indicators.get(3));
        
        
        
        if(colIncrement + indicators.get(1)  >= 16){
            int barNum = indicators.get(2) + 1;
            if(barNum == theBar.size()){
                toDup = new Bar();
                score.addBar(toDup);
            }
            else{
                toDup = theBar.get(barNum);
            }
            int row = indicators.get(0);
            int col = indicators.get(1) + colIncrement - 16;
            
            toDup.addNote(row, col, new Note(indicators.get(3), row, col)); 
            setLastNoteInfo(score, row, col, barNum, indicators.get(3));
        }
        else{
            int row = indicators.get(0);
            int col = indicators.get(1) + colIncrement;
            
            toDup.addNote(row, col, new Note(indicators.get(3), row, col));
            setLastNoteInfo(score, row, col, indicators.get(2) ,indicators.get(3));
        }
    }
    
    
    public void setLastNoteInfo(Score score, int row, int col, int barNum, int duration){        
        ArrayList<Integer> toRet = new ArrayList<Integer>();
        toRet.add(row);
        toRet.add(col);
        toRet.add(barNum);
        toRet.add(duration);
        score.setLastNoteInfo(toRet);
    }
    
    
    public String getMenuItem(){
        return "(du) duplicate ";
    }
    
    public String getCommandString(){
        return "du";
    }
}
