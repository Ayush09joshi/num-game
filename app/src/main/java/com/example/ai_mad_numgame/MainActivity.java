package com.example.ai_mad_numgame;
/*
   App will show your last performance at the start of the activity. New Tournament will start from
   all performance set to -1 again. And your new performance will be visible, when you return back to game
 */
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;  //make changes at appropriate places to include this dependency

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    int matchCounter=0,gamecounter=0;
    int []performance={-1,-1,-1,-1,-1,-1}; //score of a game is updated in this array
    int []score={-1,-1,-1}; //score of each match is updated in this array. A total of three matches in a game
    String operators[]={"+","-","*","/"};
    Random random=new Random(); //You will generate random algebra questions
    int correctButton =0; //which button will have the correct answer (tag of that button)
    TextView textView2;
    Button button1,button2,button3,button4;
    public void load(View view){
        Button buttonClicked=(Button)view;
        if(buttonClicked.getTag().toString().equals(correctButton+"")){
            score[matchCounter++]=1;
        }else{
            score[matchCounter++]=0;
        }
        newMatch();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        button4=findViewById(R.id.button4);
        textView2=findViewById(R.id.textView2);
        newMatch();
        sharedPreferences=this.getSharedPreferences("com.example.aiapp_2022", Context.MODE_PRIVATE);
        int[][]dataFrame=dataPrep(); //dataPrep function returns a two-dimenssional array
        double slope=LR.getSlope(dataFrame); //LR class, which provides slope on invoking getSlope
        new AlertDialog.Builder(this)
               // .setIcon() //your custom icon
                .setTitle("Performance")

                .setMessage(getInterpretation(dataFrame,slope))
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newMatch();
                    }
                }).show();
    }

    public void newMatch() {  //A game is composed of three matches
        correctButton= random.nextInt(4);
        int operand1 = random.nextInt(10);
        int operand2 = random.nextInt(10);
        int n=random.nextInt(4);
        int correctAnswer=-100;
        //check is operand2 is not zero; otherwise in case of division-divide by zero error will come
        String operator = operators[random.nextInt(4)];
        textView2.setText(operand1 + operator + operand2);

      // Your code here, to display correct and incorrect options on the buttons

        if(operator.equals("+"))
            correctAnswer=(operand1+operand2);
        else if(operator.equals("-"))
            correctAnswer=(operand1-operand2);
        else if(operator.equals("*"))
            correctAnswer=(operand1*operand2);
        else if(operator.equals("/"))
            correctAnswer=(operand1/operand2);

        if(correctButton==0) {
            button1.setText("" + correctAnswer);
            button2.setText("" + random.nextInt(10));
            button3.setText("" + random.nextInt(10));
            button4.setText("" + random.nextInt(10));
        }
        else if(correctButton==1&& !(operand2==0 && n==3)) {
            button2.setText(""+correctAnswer);
            button1.setText(""+random.nextInt(10));
            button3.setText(""+random.nextInt(10));
            button4.setText(""+random.nextInt(10));
        }
        else if(correctButton==2&& !(operand2==0 && n==3)) {
            button3.setText("" + correctAnswer);
            button1.setText(""+random.nextInt(10));
            button2.setText(""+random.nextInt(10));
            button4.setText(""+random.nextInt(10));
        }
        else if(correctButton==3&& !(operand2==0 && n==3)) {
            button4.setText("" + correctAnswer);
            button1.setText(""+random.nextInt(10));
            button2.setText(""+random.nextInt(10));
            button3.setText(""+random.nextInt(10));
        }

        if(matchCounter==3){    // if three matches are completed update the performance in sharedPreferences
            matchCounter=0;
            int sc=sumOfScore();
            performance[gamecounter++]=sc;
            Toast.makeText(this,""+"Score: "+sc, Toast.LENGTH_SHORT).show();
            if(gamecounter<5)
                new AlertDialog.Builder(this)
                        .setTitle("WAIT")
                        .setMessage("Want to Play More")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                newMatch();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                System.exit(0);
                            }
                        }).show();
            sharedPreferences.edit().putString("data",new Gson().toJson(performance)).apply();

        }
        
    }

    public int sumOfScore(){
        //Computing the sum of score array, which has the 1 or in each index,depending on correct or incorrect answers
        int sum=0;
        for(int i=0;i<3;i++)
        {
            sum+=score[i];
        }
        return sum;
    }

    public int[][] dataPrep() {
        int[] data = new Gson().fromJson((sharedPreferences.getString("data", null)), performance.getClass());
        Log.i("data", Arrays.toString(data)); //this is how you display arrays in Logcat, for debugging
        int dataFrame[][] = new int[6][2]; //creating a dataframe of two columns and six rows for regresson purpose
        if(data==null)
            return null;
        for (int i = 0; i < data.length; i++) {
            dataFrame[i][0] = i + 1;
            dataFrame[i][1] = data[i];
        }
        return dataFrame;
    }

    public String getInterpretation(int [][]dataFrame,double slope){
       //provide interpretation based on your slope analysis
        // Your code here
        return "Your Interpretation";
    }
}