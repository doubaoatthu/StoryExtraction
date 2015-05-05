package languageunit;

import storyextraction.Parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Word {
    private static HashSet<String> stopdict = new HashSet<String>();
    private static String stopdictPath = Parse.path + "documents/stopdict";
    private String word;
    private int label;
    public Word(String _word, int _label){
        word = _word;
        label = _label;
        if(stopdict.size() == 0)
            readStopdict();
    }
    public Word(String _word, String _label){
        word = _word;
        label = 0;
        matchLabel(_label);
        if(stopdict.size() == 0)
            readStopdict();
    }
    public boolean isNoun(){
        return (label >= 16 && label <= 22);
    }
    private static void readStopdict(){
        File file = new File(stopdictPath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                stopdict.add(tempString);
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
    public int getLabel(){
        return label;
    }
    public String getWord(){
        return word;
    }
    public void printWord(){
        System.out.print(word);
    }
    private void matchLabel(String _label){
        if(_label.equals("A".toLowerCase()))
            label = 1;// 形容词
        if(_label.equals("AD".toLowerCase()))
            label = 2;  // 副形词
        if(_label.equals("AN".toLowerCase()))
            label = 3;// 名形词
        if(_label.equals("B".toLowerCase()))
            label = 4;// 区别词
        if(_label.equals("C".toLowerCase()))
            label = 5;// 连词
        if(_label.equals("D".toLowerCase()))
            label = 6;// 副词
        if(_label.equals("E".toLowerCase()))
            label = 7;// 叹词
        if(_label.equals("F".toLowerCase()))
            label = 8;// 方位词
        if(_label.equals("G".toLowerCase()))
            label = 9;// 语素词
        if(_label.equals("H".toLowerCase()))
            label = 10;// 前接成分
        if(_label.equals("I".toLowerCase()))
            label = 11;// 成语
        if(_label.equals("J".toLowerCase()))
            label = 12;// 简称略语
        if(_label.equals("K".toLowerCase()))
            label = 13;// 后接成分
        if(_label.equals("L".toLowerCase()))
            label = 14;// 习用语
        if(_label.equals("M".toLowerCase()))
            label = 15;// 数词
        if(_label.equals("N".toLowerCase()))
            label = 16;// 名词
        if(_label.equals("NR".toLowerCase()))
            label = 17;// 人名
        if(_label.equals("NRF".toLowerCase()))
            label = 18;// 姓
        if(_label.equals("NRG".toLowerCase()))
            label = 19;// 名
        if(_label.equals("NS".toLowerCase()))
            label = 20;// 地名
        if(_label.equals("NT".toLowerCase()))
            label = 21;// 机构团体
        if(_label.equals("NZ".toLowerCase()))
            label = 22;// 其他专名
        if(_label.equals("NX".toLowerCase()))
            label = 23;// 非汉字串
        if(_label.equals("O".toLowerCase()))
            label = 24;// 拟声词
        if(_label.equals("P".toLowerCase()))
            label = 25;// 介词
        if(_label.equals("Q".toLowerCase()))
            label = 26;// 量词
        if(_label.equals("R".toLowerCase()))
            label = 27;// 代词
        if(_label.equals("S".toLowerCase()))
            label = 28;// 处所词
        if(_label.equals("T".toLowerCase()))
            label = 29;// 时间词
        if(_label.equals("U".toLowerCase()))
            label = 30;// 助词
        if(_label.equals("V".toLowerCase()))
            label = 31;// 动词
        if(_label.equals("VD".toLowerCase()))
            label = 32;  // 副动词
        if(_label.equals("VN".toLowerCase()))
            label = 33;// 名动词
        if(_label.equals("W".toLowerCase()))
            label = 34;// 标点符号
        if(_label.equals("X".toLowerCase()))
            label = 35;// 非语素字
        if(_label.equals("Y".toLowerCase()))
            label = 36;// 语气词
        if(_label.equals("Z".toLowerCase()))
            label = 37;  // 状态词
        if(_label.equals("AG".toLowerCase()))
            label = 38;  // 形语素
        if(_label.equals("BG".toLowerCase()))
            label = 39; // 区别语素
        if(_label.equals("DG".toLowerCase()))
            label = 40;  // 副语素
        if(_label.equals("MG".toLowerCase()))
            label = 41;// 数词性语素
        if(_label.equals("NG".toLowerCase()))
            label = 42;// 名语素
        if(_label.equals("QG".toLowerCase()))
            label = 43;    // 量语素
        if(_label.equals("RG".toLowerCase()))
            label = 44;    // 代语素
        if(_label.equals("TG".toLowerCase()))
            label = 45;    // 时语素
        if(_label.equals("VG".toLowerCase()))
            label = 46;    // 动语素
        if(_label.equals("YG".toLowerCase()))
            label = 47;    // 语气词语素
        if(_label.equals("ZG".toLowerCase()))
            label = 48;    // 状态词语素
        if(_label.equals("SOS".toLowerCase()))
            label = 49;    // 开始词
        if(_label.equals("WWW".toLowerCase()))
            label = 50;    // URL
        if(_label.equals("TELE".toLowerCase()))
            label = 51;   // 电话号码
        if(_label.equals("EMAIL".toLowerCase()))
            label = 52;    // email
        if(_label.equals("UNK".toLowerCase()))
            label = 0;   // 未知词性
    }
    public boolean isStopword(){
        return stopdict.contains(word);
    }
}
