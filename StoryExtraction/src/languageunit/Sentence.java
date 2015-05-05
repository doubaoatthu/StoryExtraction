package languageunit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Sentence {
	private ArrayList<Word> words;
    public Paragraph myPara;
    private HashMap<String,Integer> hashword;
    public int myPos;
    public Sentence(String sentence, Paragraph para, int pos){
        myPara = para;
        myPos = pos;
        words = new ArrayList<Word>();
        hashword = new HashMap<String, Integer>();
        int[] advisor = new int[10];
        String[] parts = sentence.split(" ");
        for(String s : parts){
            String[] segment = s.split("/");
            if(segment.length >= 2) {
                words.add(new Word(segment[0], segment[1]));
                if(!hashword.containsKey(segment[0]))
                    hashword.put(segment[0],1);
                else{
                    hashword.replace(segment[0],hashword.get(segment[0])+1);
                }
            }
        }
        processSentence();
    }
    private void processSentence(){

    }
    public void getNouns(ArrayList<NounUnit> nounlist){
        for(Word w:words) {
            if(w.isNoun()) {
                boolean existed = false;
                for (NounUnit noun : nounlist) {
                    if (noun.existedWord(w)) {
                        noun.addFrequency(myPos);
                        existed = true;
                        break;
                    }
                }
                if (!existed) {
                    nounlist.add(new NounUnit(w, myPos));
                }
            }
        }
        return;
    }
    public void getKeywords(ArrayList<Word> keywordlist){
        for(Word w:words) {
            if(!w.isStopword()) {
                keywordlist.add(w);
            }
        }
        return;
    }
    public void printSentence(){
       // System.out.println("===Sentence : with size:"+myPara.getParaSize()+" pos: "+myPos);
        for(int i = 0; i < words.size(); i++)
            words.get(i).printWord();
    }
    public boolean hasWord(String w){
        if(hashword.containsKey(w))
            return true;
        return false;
    }
    public int getWordNum(){
        return words.size();
    }
    public int getFrequencyofWord(String w){
        if(hashword.containsKey(w))
            return hashword.get(w);
        else
            return 0;
    }
    public String getMysentence(){
        String toret = "";
        for(Word w:words){
            toret += w.getWord();
        }
        toret += "。";
        return toret;
    }
}
