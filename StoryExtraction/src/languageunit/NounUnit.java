package languageunit;

import java.util.ArrayList;
import Logger.Logger;

/**
 * Created by vera on 15-3-15.
 */
public class NounUnit {
    private Word w;
    private ArrayList<Integer> position;
    private int frequency;
    public NounUnit(Word _w, int pos){
        w = new Word(_w.getWord(), _w.getLabel());
        position = new ArrayList<Integer>();
        position.add(pos);
        frequency = 1;
    }
    public boolean existedWord(Word _w){
        if(_w.getWord().equals(w.getWord()))
            return true;
        else
            return false;
    }
    public void addFrequency(int pos){
        position.add(pos);
        frequency++;
        return;
    }
    public void printUnit(){
        Logger.log(w.getLabel() + " " + frequency);
    }
    public Word getWord(){
        return w;
    }
}
