package com.example.android.delta_11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String HIGH_SCORE = "highScore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    int[] options = new int[3];
    TextView[] textViewArray = new TextView[3];
    boolean flag = false, secondaryFlag = false;
    int streak = 1, highScore = 1;

    private void genNewSet(int ques){

        int right = getRandomNumberInRange(0, 2);
        int index = 0, op = 0, j = 0;

        for(index = 0; index < 3; ++index){
            op = getRandomNumberInRange(2, ques);

            for (j = 0; j < index; ++j){
                while (op == options[j]){
                    op = getRandomNumberInRange(2, ques);
                }
            }

            if(index == right){
                while (ques % op != 0){
                    op = getRandomNumberInRange(2, ques);
                }
            }

            else{
                while (ques % op == 0){
                    op = getRandomNumberInRange(2, ques);
                }
            }

            options[index] = op;
        }

        generateOptions(right);

    }

    private void generateOptions(int right){

        int index = 0;

        textViewArray[0] = findViewById(R.id.option_1_tv);
        textViewArray[1] = findViewById(R.id.option_2_tv);
        textViewArray[2] = findViewById(R.id.option_3_tv);

        final View parent = findViewById(R.id.pv_normal);
        final View optionsbg = findViewById(R.id.normal_quiz_layout);


        for(index = 0; index < 3; ++index){

            textViewArray[index].setText(Integer.toString(options[index]));

        }

        for(index = 0; index < 3; ++index){

            if (index == right){
                final int finalIndex = index;
                textViewArray[index].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textViewArray[finalIndex].setBackgroundColor(Color.GREEN);
                        parent.setBackgroundColor(Color.GREEN);
                        optionsbg.setBackgroundColor(Color.GREEN);
                        Toast.makeText(MainActivity.this, "Right Answer!", Toast.LENGTH_SHORT).show();
                        if (!secondaryFlag){
                            if (flag){
                                ++streak;
                                if (streak >= highScore){
                                    highScore = streak;
                                }
                            }
                            flag = true;
                        }
                        secondaryFlag = true;
                    }
                });
            }

            else {
                final int finalIndex2 = right;
                textViewArray[index].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textViewArray[finalIndex2].setBackgroundColor(Color.GREEN);
                        parent.setBackgroundColor(Color.RED);
                        optionsbg.setBackgroundColor(Color.RED);
                        Toast.makeText(MainActivity.this, "Incorrect! :( Check out the correct answer in green...", Toast.LENGTH_SHORT).show();
                        flag = false;
                        streak = 1;
                    }
                });
            }
        }

    }

    private static int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }


    public void goButton(View view){

        int ques = 0;
        final View parent = findViewById(R.id.pv_normal);
        final View optionsbg = findViewById(R.id.normal_quiz_layout);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int highScoreOld = sharedPreferences.getInt(HIGH_SCORE, 1);
        editor.putInt(HIGH_SCORE, highScore);
        editor.apply();

        EditText editText=findViewById(R.id.number_normal);
        String temp=editText.getText().toString();
        if (!"".equals(temp)){
            ques = Integer.parseInt(temp);
        }

        if (ques<5)
        {
            Context context = getApplicationContext();
            CharSequence text = "Please enter an integer greater than or equal to 5";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        else
            genNewSet(ques);

        for (int index = 0; index < 3; ++index){
            textViewArray[index].setBackgroundColor(Color.parseColor("#3C514C4C"));
        }

        TextView bestStreak = findViewById(R.id.streak_text_view);
        TextView currentStreak = findViewById(R.id.current_streak_text_view);



        if (highScore > highScoreOld){
            editor.putInt(HIGH_SCORE, highScore);
            editor.commit();
        }

        String bestStreakString = "Best Streak: " + sharedPreferences.getInt(HIGH_SCORE, 0);
        String currentStreakString = "Current Streak: " + streak;
        bestStreak.setText(bestStreakString);
        currentStreak.setText(currentStreakString);
        parent.setBackgroundColor(Color.WHITE);
        optionsbg.setBackgroundColor(Color.WHITE);
        secondaryFlag = false;
    }

}
