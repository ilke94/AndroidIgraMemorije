package rs.fon.igramemorije;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private enum GameState{
        GameOver,
        ComputerTurn,
        PlayerTurn
    }

    private GameState mState=GameState.GameOver;
    private Button[] mButtons=new Button[4];
    private TextView mLevelTextView;
    private TextView mStatusTextView;
    private ImageButton mStartButton;
    private List<Integer> mComputerClicks=new ArrayList<Integer>();
    private int mPlayerClick=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mButtons[0]= (Button) findViewById(R.id.button0);
        mButtons[1]= (Button) findViewById(R.id.button1);
        mButtons[2]= (Button) findViewById(R.id.button2);
        mButtons[3]= (Button) findViewById(R.id.button3);
        mLevelTextView= (TextView) findViewById(R.id.game_level);
        mStatusTextView= (TextView) findViewById(R.id.game_status);
        mStartButton= (ImageButton) findViewById(R.id.start_game);

        setButtonsClickable(false);
    }

    public void button0Clicked(View view){
        buttonClicked(0);
    }

    public void button1Clicked(View view){
        buttonClicked(1);
    }

    public void button2Clicked(View view){
        buttonClicked(2);
    }

    public void button3Clicked(View view){
        buttonClicked(3);
    }

    public void startGame(View view){
        mStartButton.setEnabled(false);
        doComputerTurn();
    }

    private void setState(GameState state){
        mState=state;
    }

    private void setButtonsClickable(Boolean clickable) {
        for (int i=0;i<mButtons.length;i++){
            mButtons[i].setClickable(clickable);
        }
    }

    private void buttonClicked(int button){
        if (mState!=GameState.PlayerTurn){
            return;
        }
        if (mComputerClicks.get(mPlayerClick)==button){
            mPlayerClick++;
            if (mComputerClicks.size()==mPlayerClick){
                doComputerTurn();
            }
        }else {
            doGameOver();
        }
    }

    private void doComputerTurn(){
        setState(GameState.ComputerTurn);
        setButtonsClickable(false);

        int clicks=mComputerClicks.size()+1;
        mComputerClicks.clear();

        mLevelTextView.setText("Nivo " + clicks);

        mStatusTextView.setText(R.string.get_ready);
        mStatusTextView.setTextColor(getResources().getColor(R.color.red));

        new ComputerTurnTask().execute(clicks);
    }

    private void doGameOver(){
        mStatusTextView.setText(R.string.game_over);

        setButtonsClickable(false);

        setState(GameState.GameOver);
        mStatusTextView.setTextColor(getResources().getColor(R.color.light_gray));

        saveHighScore(mComputerClicks.size());

        mComputerClicks.clear();

        mStartButton.setEnabled(true);
    }

    private void saveHighScore(final int score){
        if (!SavedData.isHighScore(score)){
            return;
        }

        final EditText input=new EditText(this);
        input.setText(SavedData.getLastPlayer());

        new android.app.AlertDialog.Builder(this)
                .setTitle("Unesite ime")
                .setView(input)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = input.getText().toString();
                        SavedData.saveScore(name, score);
                    }
                }).show();
    }

    private class ComputerTurnTask extends AsyncTask<Integer,Integer,Void> {

        Random mRandom=new Random();

        @Override
        protected Void doInBackground(Integer... clicks) {
            delay(1500);

            int computerClicks=clicks[0];

            int clickDelay = getClickDelay(computerClicks);
            int pressDuration = getPressDuration(computerClicks);

            for (int i = 0; i < computerClicks; ++i) {
                delay(clickDelay);
                int buttonIndex = mRandom.nextInt(mButtons.length);
                mComputerClicks.add(buttonIndex);
                publishProgress(buttonIndex);
                delay(pressDuration);
                publishProgress(buttonIndex);
            }
            return null;
        }

        protected void onProgressUpdate(Integer... buttonIndexes) {
            int buttonIndex = buttonIndexes[0];

            Button button = mButtons[buttonIndex];
            button.setPressed(!button.isPressed());
            button.invalidate();

        }

        protected void onPostExecute(Void unused) {
            mPlayerClick = 0;

            setState(GameState.PlayerTurn);

            setButtonsClickable(true);

            mStatusTextView.setText(R.string.player_turn);
            mStatusTextView.setTextColor(getResources().getColor(R.color.green));
        }

        private int getClickDelay(int level) {
            int delay = 400;

            if (level <= 4) {
                delay = 600;
            }

            return delay;
        }

        private int getPressDuration(int level) {
            int duration = 300;

            if (level <= 7) {
                duration = 500;
            }

            return duration;
        }

        private void delay(int millis) {
            try
            {
                Thread.sleep(millis);
            }
            catch (InterruptedException ex)
            {
            }
        }
    }
}
