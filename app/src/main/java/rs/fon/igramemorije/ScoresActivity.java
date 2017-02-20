package rs.fon.igramemorije;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class ScoresActivity extends AppCompatActivity {

    private TextView[] nameTextView=new TextView[SavedData.MAXSCORES];
    private TextView[] scoreTextView=new TextView[SavedData.MAXSCORES];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        for (int i=0;i<SavedData.MAXSCORES;i++){
            int nameId=getResources().getIdentifier("high_score_name" + i,"id",getPackageName());
            nameTextView[i]= (TextView) findViewById(nameId);

            int scoreId=getResources().getIdentifier("high_score" +i, "id",getPackageName());
            scoreTextView[i]= (TextView) findViewById(scoreId);
        }

        ArrayList<NameValuePair<Integer>> scores=SavedData.getScores();
        for (int i=0;i<scores.size();i++){
            NameValuePair<Integer> pair=scores.get(i);
            nameTextView[i].setText(pair.getName());
            scoreTextView[i].setText(pair.getValue().toString());
        }
    }

    public void clearScoresButtonClicked(View view){
        SavedData.clearScores();

        for (int i=0;i<SavedData.MAXSCORES;i++){
            nameTextView[i].setText("");
            scoreTextView[i].setText("");
        }
    }
}
