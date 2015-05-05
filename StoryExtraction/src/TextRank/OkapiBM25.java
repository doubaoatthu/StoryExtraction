package TextRank;

import languageunit.*;

import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by vera on 15-3-16.
 */
public class OkapiBM25 {
    public ArrayList<Paragraph> para;
    public ArrayList<String> query;
    public ArrayList<Double> IDF;
    public ArrayList<Double> UpperK;
    public ArrayList<Double> Score;
    public static final double k1 = 2;
    public static final double b = 0.75;
    public int N;
    public OkapiBM25(ArrayList<Paragraph> p, ArrayList<String> q){
        para = p;
        query = q;
        IDF = new ArrayList<Double>();
        UpperK = new ArrayList<Double>();
        Score = new ArrayList<Double>();
        N = para.size();
        getIDF();
        getUpperK();
        for(int i = 0; i < para.size(); i++){
            Score.add(getScore(para.get(i),i));
        }
    }
    private void getIDF(){
        ArrayList<Integer> nqi = new ArrayList<Integer>();
        for(String s:query)
            nqi.add(0);
        for(Paragraph p:para){
            for(int i = 0; i < query.size(); i++){
                if(p.containsWord(query.get(i))){
                    nqi.set(i,nqi.get(i)+1);
                }
            }
        }
        for(int i = 0; i < query.size(); i++){
            double tmp1 = N - nqi.get(i) + 0.5;
            double tmp2 = nqi.get(i) + 0.5;
            double tmp3 = tmp1/tmp2;
            IDF.add(Math.log(tmp3));
        }
    }
    private void getUpperK(){
        double avedl = 0;
        for(Paragraph p:para)
            avedl += p.getWordNum();
        avedl = avedl/para.size();
        for(Paragraph p: para){
            UpperK.add(k1*( 1 - b + ( b * p.getWordNum() / avedl)));
        }
    }

    private double getLowerFi(String q, Paragraph p){
        return p.getFrequencyofWord(q);
    }
    private double getScore(Paragraph p, int p_iter){
        double sum = 0;
        for(int i = 0; i < query.size(); i++){
            System.out.println("lowerf:"+getLowerFi(query.get(i), p) * (k1 + 1));
            sum += IDF.get(i)*(getLowerFi(query.get(i), p) * (k1 + 1))/(getLowerFi(query.get(i), p) + UpperK.get(p_iter));
        }
        return sum;
    }
    public ArrayList<Double> getScoreSet(){
        return Score;
    }
    public void PrintBM25(){
        System.out.println("QuerySize: \n   "+query);
        System.out.println("IDF:\n   "+ IDF);
        System.out.println("UpperK: \n   "+UpperK);
        System.out.println("Score: \n   "+Score);
    }
    public ArrayList<Integer> TimesEachSplit(){
        ArrayList<Integer> toret = new ArrayList<Integer>();
        for(int i = 0; i < query.size(); i++){
            int tmp = 0;
            for(Paragraph p:para){
                tmp += p.getFrequencyofWord(query.get(i));
            }
            toret.add(tmp);
        }
        return toret;
    }
}
/*
* bug report: IDF is negative, how to solve it?
*/