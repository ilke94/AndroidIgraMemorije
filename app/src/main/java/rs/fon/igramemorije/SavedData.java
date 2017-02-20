package rs.fon.igramemorije;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Milos on 2/19/2017.
 */

public class SavedData {
    public static final int MAXSCORES=10;
    private static String FILENAME="HighScores";
    private static ArrayList<NameValuePair<Integer>> scores=new ArrayList<NameValuePair<Integer>>();
    private static String lastPlayer="Player";
    private static Context context;

    public static void initialize(Context _context){
        try{
            context=_context;
            FileInputStream inputFile=context.openFileInput(FILENAME);
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputFile));

            lastPlayer=bufferedReader.readLine();

            String line="";
            String name="";
            int value=0;
            for (int i=0;i<MAXSCORES;i++){
                line=bufferedReader.readLine();
                if (null==line){
                    break;
                }
                String[] tokens=line.split(",");
                if (2!=tokens.length){
                    break;
                }
                name=tokens[0];
                value=Integer.parseInt(tokens[1]);
                scores.add(new NameValuePair<Integer>(name,value));
            }
        } catch (IOException ex) {

        }
    }

    public static Boolean isHighScore(int score){
        if (scores.size()<MAXSCORES){
            return true;
        }

        int lowestHighScore=scores.get(scores.size()-1).getValue();

        if (score>lowestHighScore){
            return true;
        }
        return false;
    }

    public static void saveScore(String name,int score){
        if (!isHighScore(score)){
            return;
        }

        if (scores.size()==MAXSCORES){
            scores.remove(scores.size()-1);
        }

        lastPlayer=name;

        scores.add(new NameValuePair<Integer>(name,score));
        Collections.sort(scores, Collections.reverseOrder());

        writeScoresFile();
    }

    public static void clearScores(){
        scores.clear();
        writeScoresFile();
    }

    public static String getLastPlayer(){
        return lastPlayer;
    }

    public static ArrayList<NameValuePair<Integer>> getScores(){
        return scores;
    }

    private static void writeScoresFile(){
        try {
            FileOutputStream outputStream=context.openFileOutput(FILENAME,Context.MODE_PRIVATE);
            PrintStream printStream=new PrintStream(outputStream);

            printStream.println(lastPlayer);
            NameValuePair<Integer> pair=null;
            for (int i=0;i<scores.size();++i){
                pair=scores.get(i);
                printStream.println(pair.getName() + "," + pair.getValue().toString());
            }
            printStream.close();
        }catch (FileNotFoundException ex){

        }
    }
}
