
/**
 * Write a description of class Note here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Note
{
    private int duration; // 1 is a full note, 2 is a half note, 4 is a quarter note, and so on...
    private int row;
    private int col;
    
    public Note(int durat){
        this.duration = durat;
    }
    
    public Note(int durat, int row, int col){
        this.duration = durat;
        this.row = row;
        this.col = col;
    }
    
    public int getRow(){return row;}
    public int getCol(){return col;}
    
    public Note clone(){
        return new Note(duration);
    }
    
    public int getDuration(){
        return duration;
    }
    
    public String toString(){
        switch(duration){
            case 1:
            return "W";
            case 2:
            return "O";
            case 4:
            return "Q";
            case 8:
            return "0";          
            case 16:
            return "S";
        }
    
        return "   ";
    }
}
