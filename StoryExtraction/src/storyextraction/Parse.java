package storyextraction;

import Logger.Logger;
import languageunit.Paragraph;
import Function.*;
import languageunit.Sentence;
import languageunit.Word;
import TextRank.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Parse {

  private boolean switchParser = true;
  public static final String path = "../../../";
  private String wfile = path + "output";
  private String titlelist = path + "documents/titlelist";
  private String docName = path + "documents/";
	private ArrayList<Paragraph> paragraph;
  private ArrayList<Double> Score;

	public void writeFile(String filepath, Paragraph p, String sen) {
		FileWriter writer;
		try {
			writer = new FileWriter(filepath, true);
            writer.write(sen+"\n" +
                    "\n");
            for(Sentence s: p.getSentences()){
                writer.write(s.getMysentence());
            }
            writer.write('\n');
			writer.close();
		} catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
	}

  public String produceAnswer(Paragraph p, String sentence) {
    StringBuilder ret = new StringBuilder();
    ret.append(sentence);
    for(Sentence s : p.getSentences())
      Logger.log(s.getMysentence());
    return ret.toString();
  }

	public void readFile(String filepath, ArrayList<String> searchKey_split){
		File file = new File(filepath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
            	tempString = tempString.replace("/","");
                tempString = tempString.replace(" ","");
                Paragraph temp_para = new Paragraph(tempString);
               /* boolean containsall = true;
                for(String subkey:searchKey_split){
                    if(!temp_para.containsWord(subkey)){
                        containsall = false;
                        break;
                    }
                }
                if(containsall)*/
                    paragraph.add(temp_para);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
	}
    public int searchFiles(String location){
        File file = new File(titlelist);
        BufferedReader reader = null;
        ArrayList<Integer> filelist = new ArrayList<Integer>();
        ArrayList<String> location_split = splitParse(location);
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                String[] parts = tempString.split("##");
                if(containLocation(location_split, parts[0])){
                    filelist.add(Integer.parseInt(parts[1]));
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        int score = 0;
        int selectedFile = 0;
        for(int i : filelist){
            int tmp = getScoreofFile(location_split, i);
            if(tmp > score){
                score = tmp;
                selectedFile = i;
            }
        }
        return selectedFile;
    }
    public boolean containLocation(ArrayList<String> location_split, String title){
        boolean contains = false;
        for(String s:location_split){
            if(title.contains(s)) {
                contains = true;
                break;
            }
        }
        return contains;
    }
    public int getScoreofFile(ArrayList<String> location_split, int fileno){
        File file = new File(docName+fileno);
        int [] locsplit_times = new int[location_split.size()];
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                int old_length = tempString.length();
                for(int i = 0; i < location_split.size(); i++){
                    String new_tmp = tempString.replace(location_split.get(i),"");
                    int new_length = new_tmp.length();
                    locsplit_times[i] += (old_length - new_length)/location_split.get(i).length();
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        int sum = 0;
        for(int i = 0; i < locsplit_times.length; i++){
            if(locsplit_times[i] == 0)
                return 0;
            sum += locsplit_times[i];
        }
        Logger.log("sum" + sum);
        return sum;

    }
    public ArrayList<String> splitParse(String toparse){
        if(switchParser) {
            ArrayList<String> toret = new ArrayList<String>();
            String location_split = new splitFunction().splitStringByM160(toparse, true);
            String[] parts = location_split.split(" ");
            for (String s : parts) {
                String[] segment = s.split("/");
                if (segment.length >= 2) {
                    Logger.log(segment[1]);
                    toret.add(segment[0]);
                }

            }
            Logger.log("split parse:" + toret);
            return toret;
        }
        else{
            ArrayList<String> toret = new ArrayList<String>();
            String[] parts = toparse.split(" ");
            for(String s : parts){
                if(s.length() > 0)
                    toret.add(s);
            }
            return toret;
        }
    }
    public int getMaxParagraph(){
        int maxnimei = 0;
        for(int i = 0; i < Score.size(); i++){
            if(Score.get(i) > Score.get(maxnimei))
                maxnimei = i;
        }
        if(Score.get(maxnimei) > 0)
            return maxnimei;
        else{
            return -1;
        }
    }
    public Parse(){

    }
    public void Answer(String input){
        paragraph = new ArrayList<Paragraph>();
        String searchArea = input;
        ArrayList<String> searchKey_split = splitParse(searchArea);
        int fileno = searchFiles(searchArea);
        readFile(docName + fileno, searchKey_split);
        OkapiBM25 bm25 = new OkapiBM25(paragraph,searchKey_split);
        Score = bm25.getScoreSet();
        bm25.PrintBM25();
        int maxParagraph = getMaxParagraph();
        if(maxParagraph >= 0) {
            String answer = paragraph.get(maxParagraph).selectSentences(searchKey_split, bm25.TimesEachSplit());
            writeFile(wfile, paragraph.get(maxParagraph), answer);
        }
        else{
            for(Paragraph pa : paragraph){
                writeFile(wfile, pa, "");
            }
        }
    }

    public String AnswerWithString(String input) {
      paragraph = new ArrayList<Paragraph>();
      String searchArea = input;
      ArrayList<String> searchKey_split = splitParse(searchArea);
      int fileno = searchFiles(searchArea);
      readFile(docName + fileno, searchKey_split);
      OkapiBM25 bm25 = new OkapiBM25(paragraph,searchKey_split);
      Score = bm25.getScoreSet();
      bm25.PrintBM25();
      int maxParagraph = getMaxParagraph();
      StringBuilder toRet = new StringBuilder();
      if(maxParagraph >= 0) {
        String answer = paragraph.get(maxParagraph).selectSentences(searchKey_split, bm25.TimesEachSplit());
        toRet.append(produceAnswer(paragraph.get(maxParagraph), answer));
      }
      else{
        for(Paragraph pa : paragraph){
          toRet.append(produceAnswer(pa, "Not Found"));
        }
      }
      return toRet.toString() + "\n===\n";
    }
}
/**
 * bug report: cannot parse 河南林县 into two parts. So does 黑龙江大兴安岭. T^ T.
 */
