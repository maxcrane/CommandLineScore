import java.io.*;
import java.util.*;

/**
 * Write a description of class ScoreSaver here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ScoreSaver
{
    public void writeScoreToFile(String fileName, Score score) {
        try{
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            
            ArrayList<Bar> bars = score.getBars();
            writer.println(bars.size());
            
            for(Bar b: bars){
                writer.println();
                writer.print(b.toString());
            }
            writer.close();
        }
        catch(Exception e){
            
        }
    }
    
    public ArrayList<Bar> loadScore(String fileName, Score score) {
        try{
            Scanner scan  = new Scanner(new File(fileName));
            int numBars = scan.nextInt();
            ArrayList<Bar> bars = new ArrayList<Bar>();
            for(int ndx = 0; ndx < numBars; ndx++){
                bars.add(new Bar());
            }
            
            //System.out.println("scan nextLine is " + scan.nextLine());
            scan.nextLine();
            int curBar = -1;
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                if(line.length() == 0){
                    curBar++;
                }
                else{
                    Scanner lineScan = new Scanner(line);
                    int duration = lineScan.nextInt();
                    int row = lineScan.nextInt();
                    int col = lineScan.nextInt();
                    bars.get(curBar).addNote(row, col, new Note(duration, row, col));
                }
            }
            return bars;
        }
        catch(IOException e){
            System.out.println("couldn't load score");
        }
        return null;
    }
}
