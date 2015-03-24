import javax.sound.midi.*;
import java.io.*;
import java.util.*;

/**
 * Write a description of class MidiPlayer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MidiPlayer implements Plugin
{

    public HashMap<Integer, Integer> lengthMap;
    public HashMap<Integer, Integer> noteFromC;
 
    public void doThing(Score score){
        ArrayList<Bar> bars = score.getBars();
        
        makeFile(bars.size()*128);
        for(int ndx = 0; ndx < bars.size(); ndx++){
            Bar b = bars.get(ndx);
            ArrayList<Note> notes = b.getNotes();
            for(Note n: notes){
                addNote(n.getCol(), lengthMap.get(n.getDuration()), b.numRows() - n.getRow(), ndx);
            }
        }
        writeFile();
        playSong("temp.mid");
        
    }

    public String getMenuItem(){
        return "(p) play";
    }
    
    public String getCommandString(){
        return "p";
    }
    
    
    /**
     * Constructor for objects of class MidiPlayer
     */
    public MidiPlayer()
    {
        noteFromC = new HashMap<Integer, Integer>();
        noteFromC.put(1,0); //c
        noteFromC.put(2,2); //d
        noteFromC.put(3,4); //e
        noteFromC.put(4,5); //f
        noteFromC.put(5,7); //g
        noteFromC.put(6,9); //a
        noteFromC.put(7,11); //b
        noteFromC.put(8,12); //c
        noteFromC.put(9,14); //d
        noteFromC.put(10,16); //e
        noteFromC.put(11,17); //f
        noteFromC.put(12,19); //g
        noteFromC.put(13,21); //a
        
        lengthMap = new HashMap<Integer, Integer>();
        lengthMap.put(1, 16);
        lengthMap.put(2, 8);
        lengthMap.put(4, 4);
        lengthMap.put(8, 2);
        lengthMap.put(16, 1);
    }
    
    public void playSong(String fileName){
        try {
            // From file
            Sequence sequence = MidiSystem.getSequence(new File(fileName));
        
            // From URL
            //sequence = MidiSystem.getSequence(new URL("http://hostname/midiaudiofile"));
        
            // Create a sequencer for the sequence
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.setSequence(sequence);
        
            // Start playing
            sequencer.start();
        
        } catch (IOException e) {
        } catch (MidiUnavailableException e) {
        } catch (InvalidMidiDataException e) {
        }

    
    }
    
    Track t;   
    Sequence s;
  
  
   public void makeFile(int numTicksLong){
        //System.out.println("midifile begin, numTicks is "+ numTicksLong);
    	try
    	{
    	    //****  Create a new MIDI sequence with 16 ticks per beat  ****
    		s = new Sequence(javax.sound.midi.Sequence.PPQ, 16);
    
    		//****  Obtain a MIDI track from the sequence  ****
    		t = s.createTrack();
    
    		//****  General MIDI sysex -- turn on General MIDI sound set  ****
    		byte[] b = {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7};
    		SysexMessage sm = new SysexMessage();
    		sm.setMessage(b, 6);
    		MidiEvent me = new MidiEvent(sm,(long)0);
    		t.add(me);
    
    		//****  set tempo (meta event)  ****
    		MetaMessage mt = new MetaMessage();
            byte[] bt = {0x03,(byte) 0x01,(byte) 0x86,(byte) 0xA0};//0x02,) (byte)0x00, 0x00};
    		mt.setMessage(0x51 ,bt, 3);
    		me = new MidiEvent(mt,(long)0);
    		t.add(me);
    
    		//****  set track name 
    		mt = new MetaMessage();
    		String TrackName = new String("midifile track");
    		mt.setMessage(0x03 ,TrackName.getBytes(), TrackName.length());
    		me = new MidiEvent(mt,(long)0);
    		t.add(me);
    
    		//****  set omni on  ****
    		ShortMessage mm = new ShortMessage();
    		mm.setMessage(0xB0, 0x7D,0x00);
    		me = new MidiEvent(mm,(long)0);
    		t.add(me);
    
    		//****  set poly on  ****
    		mm = new ShortMessage();
    		mm.setMessage(0xB0, 0x7F,0x00);
    		me = new MidiEvent(mm,(long)0);
    		t.add(me);
    
    		//****  set instrument to Piano  ****
    		mm = new ShortMessage();
    		mm.setMessage(0xC0, 0x00, 0x00);
    		me = new MidiEvent(mm,(long)0);
    		t.add(me);
    	
    		//****  set end of track (meta event)
    		mt = new MetaMessage();
            byte[] bet = {}; // empty array
    		mt.setMessage(0x2F,bet,0);
    		me = new MidiEvent(mt, (long)numTicksLong);
    		t.add(me);
    	} //try
    		catch(Exception e)
    	{
    		System.out.println("Exception caught " + e.toString());
    	} //catch
        //System.out.println("midifile end ");
  } //main    
  
  
  public void addNote(int tickIndex, int tickDuration, int pitchFromC, int barNum){
      try{
           //System.out.println("pitch from c is " + pitchFromC + "// map is " + noteFromC.get(pitchFromC));
            //****  note on - middle C  ****
        	ShortMessage mm = new ShortMessage();
        	mm.setMessage(ShortMessage.NOTE_ON, 60 + noteFromC.get(pitchFromC), 100);
        	MidiEvent me = new MidiEvent(mm,(long) (tickIndex * 8) + (barNum * 128));
        	t.add(me);
        
        	//****  note off - middle C - 120 ticks later  ****
        	mm = new ShortMessage(); 
        	mm.setMessage(ShortMessage.NOTE_OFF, 60 + noteFromC.get(pitchFromC), 100);
        	me = new MidiEvent(mm,(long) ((tickIndex  + tickDuration) * 8) + (barNum * 128));
        	t.add(me);
      }
      catch(Exception e){
          System.out.println("error adding note");
      }
    
  }
  
  public void writeFile(){
      try{
          //****  write the MIDI sequence to a MIDI file  ****
          File f = new File("temp.mid");
          MidiSystem.write(s,1,f);
      }
      catch(Exception e)
      {
          System.out.println("Exception caught " + e.toString());
      } //catch
  }
}
