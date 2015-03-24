import java.util.*;

/**
 * Write a description of class Score here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Score
{
    private static final int kNumLines = 6;
    private String menu;
    // instance variables - replace the example below with your own
    private ArrayList<Bar> bars;
    private Scanner in;
    
    private ScoreSaver scoreSave;
    private int lastRow;
    private int lastCol;
    private Note lastNoteModified;
    private int lastBarNum;
    private int lastNoteDuration;
    private String lastCommand;
    
    private ArrayList<Plugin> plugins;
    
    /**
     * Constructor for objects of class Score
     */
    public Score()
    {
        // initialise instance variables
        bars = new ArrayList<Bar>();
        plugins = new ArrayList<Plugin>();
        scoreSave = new ScoreSaver();
        NoteDuplicator noteDupe = new NoteDuplicator();
        BarReverser bReverse = new BarReverser();
        
        plugins.add(noteDupe);
        plugins.add(bReverse);
        plugins.add(new MidiPlayer());
        plugins.add(new BarDuplicator());
        menu = "(a) add note, (d) delete note, (u) undo, (ib) insert bar, (cb) clear bar, (db) delete bar\n";
        menu += "(clear), (save), (load), (quit)\n";
        menu += "plugins: ";
        
        for(Plugin p: plugins){
            menu += p.getMenuItem();
        }
    }
    
    public Scanner getInScanner(){
        return in;
    }
    
    public String getMenu(){
        return menu;
    }

    
    public boolean checkPlugins(String command){
        for(Plugin p: plugins){
            if(p.getCommandString().equals(command)){
                p.doThing(this);
                return true;
            }
        }
        return false;
    }
    
    
    public ArrayList<Bar> getBars()
    {
        return bars;
    }
    
    public ArrayList<Integer> lastNoteInfo(){
        ArrayList<Integer> toRet = new ArrayList<Integer>();
        toRet.add(lastRow);
        toRet.add(lastCol);
        toRet.add(lastBarNum);
        toRet.add(lastNoteDuration);
        return toRet;
    }
    
    public void setLastNoteInfo(ArrayList<Integer> list){
        lastRow = list.get(0);
        lastCol = list.get(1);
        lastBarNum = list.get(2);
        lastNoteDuration = list.get(3);
        
    }
    
    public void addBar(Bar toAdd){
        bars.add(toAdd);
    }
    
    public void addBar(int ndx, Bar toAdd){
        bars.add(ndx, toAdd);
    }
    
    public static String getKey(){
        StringBuilder toRet = new StringBuilder();
        
        toRet.append("W = whole     note\n");
        toRet.append("O = half      note\n");
        toRet.append("Q = qaurter   note\n");
        toRet.append("0 = eight     note\n");
        toRet.append("S = sixteentn note\n");
        
        return toRet.toString();
    }
    
    
    
    public static void main(String[] args){
        Score myScore = new Score();
        System.out.println(Score.getKey());
        
        Bar bar = new Bar();
        myScore.addBar(bar);
        Bar bar2 = new Bar();
        myScore.addBar(bar2);

        
        myScore.printScore();
        myScore.in = new Scanner(System.in);
        String cur = "";
        
        while(!cur.equalsIgnoreCase("quit")){
            System.out.println(myScore.getMenu());
            cur = myScore.in.nextLine();
            myScore.doCommand(cur);
        }
        
    }
    
    public void doCommand(String command){
        
        if(command.startsWith("cb")){
            System.out.println("Enter bar number: 1-" + bars.size());
            clearBar(Integer.parseInt(in.next()) - 1);
            printScore();
        }
        else if(command.equals("db")){
            System.out.println("Enter bar number: 1-" + bars.size());
            bars.remove(Integer.parseInt(in.next()) - 1);
            printScore();
        }
        else if(command.equals("ib")){
            System.out.println("Enter index to insert: 0-" + bars.size());
            int ndx = Integer.parseInt(in.next());
            if(ndx != bars.size()){
                bars.add(ndx, new Bar());
            }
            else{
                bars.add(new Bar());
            }
            
            printScore();
        }
        else if(command.equals("a")){
            addNoteDialouge();
        }
        else if(command.equals("d")){
            deleteNote();
        }
        else if(command.equals("u")){
            undoCommand();
        }
        else if(command.equals("save")){
            System.out.println("Enter filename to save to: ");
            String fileName = in.nextLine();
            scoreSave.writeScoreToFile(fileName, this);
        }
        else if(command.equals("load")){
            System.out.println("Enter filename to load: ");
            String fileName = in.nextLine();
            bars = scoreSave.loadScore(fileName, this);
            printScore();
        }
        if(checkPlugins(command) == true){
            printScore();
        }        
        lastCommand = command;
    }
    
    public void undoCommand(){
        if(lastCommand.startsWith("cb")){  
            printScore();
        }
        else if(lastCommand.startsWith("a")){
            bars.get(lastBarNum).deleteNote(lastRow, lastCol);
        }
        else if(lastCommand.equals("d")){
            bars.get(lastBarNum).addNote(lastRow, lastCol, lastNoteModified);
        }
        else if(lastCommand.equals("du")){
            bars.get(lastBarNum).deleteNote(lastRow, lastCol);
            lastCol--;
        }
        printScore();
    }
    
    /**
    public void duplicateNote(){
        if(lastCol != 16)
        {
            bars.get(lastBarNum).addNote(lastRow, lastCol+1, lastNoteModified.clone());
            lastCol++;
        }
        else
        {
            System.out.println("cannot duplicate");
        }
        printScore();
    }
    **/
    
    
    public void deleteNote(){
        int barNum, notePlacement, noteDuration, pitch;
        
        System.out.println("Enter bar number: 1-" + bars.size());
        barNum = Integer.parseInt(in.nextLine()) - 1;
        
        System.out.println("Enter note placement: 1-16");
        notePlacement = Integer.parseInt(in.nextLine()) - 1;
        
        System.out.println("Enter note pitch: 1-13 (c-g)");
        pitch =  13 - Integer.parseInt(in.nextLine());
        
        lastBarNum = barNum;
        lastRow = pitch;
        lastCol = notePlacement;
        lastNoteModified = bars.get(barNum).deleteNote(pitch, notePlacement);
        printScore();
    }
    
    public void addNoteDialouge(){
        int barNum, notePlacement, noteDuration, pitch;
        
        System.out.println("Enter bar number: 1-" + bars.size());
        barNum = Integer.parseInt(in.nextLine()) - 1;
        
        System.out.println("Enter note placement: 1-16");
        notePlacement = Integer.parseInt(in.nextLine()) - 1;
        
        System.out.println("Enter note duration:");
        System.out.println("1(whole note), 2(half note), 4(quarter note)");
        System.out.println("8(eight note), 16(sixteenth note)");
        noteDuration = Integer.parseInt(in.nextLine());
        
        System.out.println("Enter note pitch: 1-13");
        System.out.println("[c(1), d(2), e(3), f(4), g(5), a(6), b(7), c(8), d(9), e(10), f(11), g(12), a(13)]");
        pitch =  13 - Integer.parseInt(in.nextLine());
        
        lastNoteModified = new Note(noteDuration, pitch, notePlacement);
        lastBarNum = barNum;
        lastRow = pitch;
        lastCol = notePlacement;
        lastNoteDuration = noteDuration;
        bars.get(barNum).addNote(pitch, notePlacement, lastNoteModified);
        printScore();
    }
    
    
    
    public void clearBar(int barNum){
        bars.get(barNum).clear();
    }
    
    public void printScore(){
        //Scanner scan = bar.getBar();
        ArrayList<Scanner> scans = new ArrayList<Scanner>();
        int ndx = 0;
        int curLine = 1;
        int numPerLine = 4;
        
        for(Bar bar: bars){
            
            scans.add(bar.getBar());
            if(ndx % 4 == 0){
                System.out.println();
            }
        }
        
        
        
        while(ndx < scans.size() && scans.get(ndx).hasNextLine()){
            for(int altNdx = ndx; altNdx < curLine * numPerLine; altNdx++){
                if(altNdx < scans.size()){
                    System.out.print(scans.get(altNdx).nextLine());
                    System.out.print("|");
                }
            }
            System.out.println();
            if(!scans.get(ndx).hasNextLine()){
                curLine++;
                ndx += numPerLine;
            }
        }
    }
}
