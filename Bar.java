import java.util.*;
/**
 * Write a description of class Bar here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bar
{
    // instance variables - replace the example below with your own
    private Note[][] notes;
    private ArrayList<Integer> kLinesWithNums;
    private ArrayList<Integer> kColsWithoutLines;
    private static final int kNumRows = 13;
    private static final int kNumCols = 16;
    
    
    public int numRows(){return kNumRows;}
    public int numCols(){return kNumCols;}
    public String toString(){
        StringBuilder sb = new StringBuilder();
            
        for(int row = 0; row < kNumRows; row++){
            for(int col = 0; col < kNumCols; col++){
                if(notes[row][col] != null){
                    sb.append(notes[row][col].getDuration() + " " + row + " " +  col + "\n");
                }
            }
        }
        
        return sb.toString();
    }
    
    public Bar duplicateBar(){
        Bar toRet = new Bar();
        
        for(int row = 0; row < kNumRows; row++){
            for(int col = 0; col < kNumCols; col++){
                if(notes[row][col] != null){
                    toRet.addNote(row, col, new Note(notes[row][col].getDuration(), row, col));
                }
            }
        }
    
        return toRet;
    }
    
    /**
     * Constructor for objects of class Bar
     */
    public Bar()
    {
        notes = new Note[kNumRows][kNumCols];
        kLinesWithNums = new ArrayList<Integer>();
        kLinesWithNums.add(2);
        kLinesWithNums.add(4);
        kLinesWithNums.add(6);
        kLinesWithNums.add(8);
        kLinesWithNums.add(10);
        
        
        kColsWithoutLines = new ArrayList<Integer>();
        kColsWithoutLines.add(16);
        
        for(int row = 0; row < kNumRows; row++){
            for(int col = 0; col < kNumCols; col++){
                notes[row][col] = null;
            }
        }
    }

    public void clear(){
        for(int row = 0; row < kNumRows; row++){
            for(int col = 0; col < kNumCols; col++){
                notes[row][col] = null;
            }
        }
    }
    
    
    public ArrayList<Note> getNotes(){
        ArrayList<Note> toRet = new ArrayList<Note>();
        
        for(int row = 0; row < kNumRows; row++){
            for(int col = 0; col < kNumCols; col++){
                if(notes[row][col] != null){
                    toRet.add(notes[row][col]);
                }
            }
        }
        
        return toRet;
    }
    
    public Note deleteNote(int row, int col){
        Note toRet = notes[row][col];
        notes[row][col] = null;
        return toRet;
    }
    
    public void deleteNote(Note toDelete){
        for(int row = 0; row < kNumRows; row++){
            for(int col = 0; col < kNumCols; col++){
                if(notes[row][col] == toDelete){
                    notes[row][col] = null;
                    row = kNumRows;
                    col = kNumCols;
                }
            }
        }
    }
    
    
    public void addNote(int row, int col, Note note){
        notes[row][col] = note;
    }
    
    public Scanner getBar(){
        StringBuilder sb = new StringBuilder();
        String[][] score = new String[kNumRows][kNumCols];
        
        
        for(int row = 0; row < kNumRows; row++){
            for(int col = 0; col < kNumCols; col++){
                if (notes[row][col] == null){
                    if(kLinesWithNums.contains(row) && !kColsWithoutLines.contains(col)) 
                    {
                        score[row][col] = "- ";
                    }
                    else {
                        score[row][col] = "  ";
                    }
                }
                else {
                    int curRow = row;
                    int numLines = 4;
                    
                    while(numLines-- != 0 && curRow != 0 ){//&& notes[curRow][col] == null){
                        if(kLinesWithNums.contains(curRow)) 
                        {
                            score[curRow--][col] = "-|";
                        }
                        else {
                            score[curRow--][col] = " |";
                        }
                        
                    }
                    
                    score[row][col] = notes[row][col].toString() + " ";
                }
            }
        }
        
        for(int row = 0; row < kNumRows; row++){
            for(int col = 0; col < kNumCols; col++){
                if (notes[row][col] != null){
                    score[row][col] = notes[row][col].toString() + " ";
                }
            }
        }
        
        for(int row = 0; row < kNumRows; row++){
            for(int col = 0; col < kNumCols; col++){
                sb.append(score[row][col]);
            }
            sb.append("\n");
        }
        
        
        return new Scanner(sb.toString());
    }
}
