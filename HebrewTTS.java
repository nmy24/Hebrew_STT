package com.example.comchelper;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class HebrewTTS {

    // Used to conver to basic latin form
    private static String[] arabic = {"א","ב","ג","ד","ה","ו","ז","ח","ט","י","כ","ל","מ","נ","ס","ע","פ","צ","ק","ר","ש","ת"};
    private static String[] english = {"a","b","g","d","h","v","z","h","t","i","k","l","m","n","s","a","p","ch","k","r","sh","t"};
    // Used to fix some mistakes
    private static String[] mistakes = {"bay","dai","sad","bar","kah","aaa","kau","oan","tuo","yam","gar"," uo","saf","maz","maw","yaw","wab","kas","mach","wak","has","zam","aya","mar","tan","sar","way "," man ","hawak","rad","i ","bay","law","way","lalah"," maw "," maw?","maw ","yar","tak","zab","nay","aay"," aa","nai", "shalava", "navaah","raavat", "gaiaa","haiaia", "maiarab"};
    private static String[] fixes = {"bi","di","sed","ber","kh","aa","ku","on","tou","eym","gur"," wa","sif","muz","moo","eoo","ob","kos","mich","ok","hass","zom","aia","mer","taan","sur","oee "," min ","hook","red","y ","bi","loo","we","llah"," mo "," mo?","mo ","yer","tik","zeb","ni","ai"," a","ni", "shalom", "noa","reut", "guy", "haim", "meirab"};

    private static String[] arnumbers = {"0","١","1","2","3","4","5","6","7","8","9"};
    private static String[] ennumbers = {" efes "," ehad "," shtaiim "," shalosh "," arba "," hamesh "," shesh "," sabaa "," sheva "," shmone ", "tesha"};


    // The text to speech we will use
    private TextToSpeech tts;

    // So that it won't re convert a converted line
    private String latest = "";

    // To create the text to speech
    public boolean prepare(Context con){
        if(con != null)
        {

            tts = new TextToSpeech(con, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        Locale loc = new Locale("en", "IN");
                        tts.setLanguage(loc);
                    }
                }
            });
            return true;
        }
        else
        {
            return false;
        }
    }


    // To convert text to speech
    public boolean talk(String text){
        tts.setPitch((float)0.7);
        tts.setSpeechRate((float)0.5);
        if(!text.equals(latest)) {
            text = filter(text);
        }
        if(tts!=null && text != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
            return true;
        }else{
            return false;
        }
    }


    // Filtering the text into latin
    public String filter(String text) {

        while(text.contains("  ")){
            text = text.replace("  "," ");
        }
        text = " "+text+" ";
        // convert to basic latin
        text = convert(text,1);
        // fix mistakes
        text = convert(text,2);
        // convert numbers
        text = convert(text,3);
        latest = text;
        return text;
    }



    // Converting into latin
    private String convert(String text,int type){

        String[] fromlist = null;
        String[] tolist = null;

        if(type == 1){
            fromlist = arabic;
            tolist = english;
        }else if(type == 2){
            fromlist = mistakes;
            tolist = fixes;
        }else if(type == 3){
            fromlist = arnumbers;
            tolist = ennumbers;
        }

        for(int x = 0;x<fromlist.length;x++){
            if(text.contains(fromlist[x])){
                if(type == 1) {
                    text = text.replace(fromlist[x], tolist[x] + "a");
                }else{
                    text = text.replace(fromlist[x], tolist[x]);
                }
            }
        }
        if(type == 1)
            text = text.replace("a "," ");

        return text;
    }
}
