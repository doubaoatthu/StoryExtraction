package TextRank;

import Logger.Logger;
import languageunit.*;
import languageunit.Sentence;

import java.util.ArrayList;

/**
 * Created by vera on 15-3-16.
 */
public class SentenceRank {
    private ArrayList<Sentence> sentences;
    private ArrayList<Double> sentence_self_weight;
    private ArrayList<String> searchKey;
    private ArrayList<Integer> timesofsplit;
    private ArrayList<Double> weightofKey;
    private Paragraph paragraph;
    private int startSentence, endSentence;
    private static final double thold = 1;
    private static final double thold2 = 1.5;
    public SentenceRank(ArrayList<Sentence> s, ArrayList<String>k, ArrayList<Integer> t, Paragraph p){
        sentences = s;
        searchKey = k;
        timesofsplit = t;
        paragraph = p;
        weightofKey = new ArrayList<Double>();
        sentence_self_weight = new ArrayList<Double>();
        setWeightofKey();
        setWeightofSentence();
        searchForward();
    }
    private void setWeightofKey(){
        double sum = 0;
        for(int i = 0; i < searchKey.size(); i++){
            double tmp1 = paragraph.getFrequencyofWord(searchKey.get(i));
            double tmp2 = timesofsplit.get(i);
            weightofKey.add(tmp1/tmp2);
            sum += tmp1/tmp2;
        }
        for(int i = 0; i < weightofKey.size(); i++){
            weightofKey.set(i, weightofKey.get(i)/sum);
        }
    }
    private void setWeightofSentence(){
        double sum = 0;
        for(Sentence s:sentences){
            double tmp_weight = 0;
            for(int i = 0; i < searchKey.size(); i++){
                tmp_weight += ((double)s.getFrequencyofWord(searchKey.get(i)))*weightofKey.get(i);
            }
            sentence_self_weight.add(tmp_weight);
            sum += tmp_weight;
        }
        for(int i = 0; i < sentence_self_weight.size(); i++){
            sentence_self_weight.set(i,sentence_self_weight.get(i)/sum * sentences.size());
        }
    }
    private void searchForward(){
        int start = 0;
        int end = 0;
        for(int i = 0; i < sentence_self_weight.size(); i++){
            if(sentence_self_weight.get(i) >= thold){
                start = i;
              Logger.log("Start sentence index:" + i + "\nSentence:"+sentences.get(i).getMysentence()+"\nweight:" + sentence_self_weight.get(i));
                break;
            }
        }
        int maxi = 0;
        for(int i = 0; i < sentence_self_weight.size(); i++){
            Logger.log("Find max sentence index:" + i + "\nSentence:"+paragraph.sentences.get(i).getMysentence()+"\nweight:" + sentence_self_weight.get(i));
            if(sentence_self_weight.get(i) >= sentence_self_weight.get(maxi))
                maxi = i;
        }
        Logger.log("Max sentence index:" + maxi + "\nSentence:"+sentences.get(maxi).getMysentence()+"\nweight:" + sentence_self_weight.get(maxi));
        ArrayList<Word> keylist = new ArrayList<Word>();
        for(int i = start; i <= maxi; i++){
            sentences.get(i).getKeywords(keylist);
        }
        for(int i = maxi+1; i < sentences.size(); i++){
            double nearbyWeight = getNearbyWeight(sentences.get(i),keylist);
            double totalWeight = nearbyWeight + sentence_self_weight.get(i);
            if(totalWeight >= thold2){
              Logger.log("check sentence index:" + i +"\nSentence"+sentences.get(i).getMysentence()+ "\ntotal weight:" + totalWeight);
                sentences.get(i).getKeywords(keylist);
            }
            else{
                end = i-1;
                break;
            }
        }
        startSentence = start;
        endSentence = end;
        //System.out.println("End:"+end);
    }
    public ArrayList<Integer> sentenceNo(){
      Logger.log("self weight: " + sentence_self_weight);
        ArrayList<Integer> toret = new ArrayList<Integer>();
        toret.add(startSentence);
        toret.add(endSentence);
        return toret;
    }
    private double getNearbyWeight(Sentence s, ArrayList<Word> keylist){
        double nearbyWeight = 0;
        ArrayList<KeywordWeight> weilist = new TextRankKeyword().getKeyword(keylist);
        for(int i = 0; i < weilist.size(); i++) {
            if (s.hasWord(weilist.get(i).getWord())) {
                nearbyWeight += weilist.get(i).getWeight();
            }
        }
        return nearbyWeight;
    }
}
