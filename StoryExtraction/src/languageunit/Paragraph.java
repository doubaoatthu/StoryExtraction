package languageunit;

import Function.splitFunction;
import TextRank.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by vera on 15-3-13.
 */
public class Paragraph {
    public ArrayList<Sentence> sentences;
    public ArrayList<NounUnit> nounlist;
    public ArrayList<Word> keywordlist;
    public ArrayList<KeywordWeight> keywordWeights;
    private int paraSize;

    public Paragraph(String para){
        initialize();
        String[] sts = para.split("。");
        for(int i = 0; i < sts.length; i++){
            String sentence = sts[i];
            sentences.add(new Sentence(new splitFunction().splitStringByM160(sentence,true), this, i));
            sentences.get(sentences.size()-1).getNouns(nounlist);
            sentences.get(sentences.size()-1).getKeywords(keywordlist);
        }
        paraSize = sentences.size();
    }
    private void initialize(){
        sentences = new ArrayList<Sentence>();
        nounlist = new ArrayList<NounUnit>();
        keywordlist = new ArrayList<Word>();
    }
    public int getParaSize(){
        return paraSize;
    }
    public void printParagraph(){
        System.out.println("Paragraph");
        for(int i = 0; i < sentences.size(); i++){
            sentences.get(i).printSentence();
        }
        System.out.println("NounList");
      /*  for(NounUnit n:nounlist)
            n.printUnit();*/
        getKeywordRank();
    }
    public void getKeywordRank(){
       keywordWeights = new TextRankKeyword().getKeyword(keywordlist);
       for(int i = 0; i < keywordWeights.size() && i < 11/*11 means the rank i need*/; i++){
           System.out.print(keywordWeights.get(i).getWord() + " " + keywordWeights.get(i).getWeight() + "  ");
       }
       /*for(KeywordWeight k:keywordWeights)
           System.out.print(k.getWord()+" "+k.getWeight()+" ");*/
   }
    public boolean containsWord(String word){
        for(Sentence s:sentences){
            if(s.hasWord(word))
                return true;
        }
        return false;
    }
    public int getWordNum(){
        int toret = 0;
        for(Sentence s:sentences){
            toret += s.getWordNum();
        }
        return toret;
    }
    public int getFrequencyofWord(String w){
        int sum = 0;
        for(Sentence s:sentences)
            sum += s.getFrequencyofWord(w);
        return sum;
    }
    public ArrayList<Sentence> getSentences(){
            return sentences;
    }
    public String selectSentences(ArrayList<String> keyword_split, ArrayList<Integer> times){
        String toret = "";
        ArrayList<Integer> sentenceno = new SentenceRank(sentences, keyword_split, times, this).sentenceNo();
        for(int i = sentenceno.get(0); i <= sentenceno.get(1); i++){
            toret += sentences.get(i).getMysentence();
        }
        System.out.println(toret);
        return toret;
    }
}
