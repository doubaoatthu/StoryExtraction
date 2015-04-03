package languageunit;

/**
 * Created by vera on 15-3-16.
 */
public class KeywordWeight {
    private String word;
    private float weight;
    public KeywordWeight(String w, float wi){
        word = ""+w;
        weight = wi;
    }
    public String getWord(){
        return word;
    }
    public float getWeight(){
        return weight;
    }
}
