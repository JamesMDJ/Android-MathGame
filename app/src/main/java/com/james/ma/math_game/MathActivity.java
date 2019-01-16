package com.james.ma.math_game;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.james.ma.math_game.com.james.ma.math_game.setting.Setting;

import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MathActivity extends AppCompatActivity {
    Button btnA,btnB,btnC,btnD;
    int questonA,questonB,Answer;
    TextView QuestionText,questionQtyText;
    LinearLayout questionLayout;
    int qtyQuestion,restQuestion;
    LinkedList<String> allAns;
    int i=1;
    String diff,timeType;
    Boolean add,sub,mul,div;
    SharedPreferences settingpfe;
    String mathType[]={"add","sub","mul","div"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);
        QuestionText=findViewById(R.id.textQuestion);
        questionQtyText=findViewById(R.id.textQuestionQty);
        questionLayout=findViewById(R.id.questionLayout);
        allAns=new LinkedList<String>();
        btnA=findViewById(R.id.buttonA);
        btnB=findViewById(R.id.buttonB);
        btnC=findViewById(R.id.buttonC);
        btnD=findViewById(R.id.buttonD);

        qtyQuestion=15; //題目數量
        restQuestion=qtyQuestion; //剩餘題目數量
        settingpfe=getSharedPreferences("setting", MODE_PRIVATE);
        loadSetting();
        createQuestion();

        Log.d("answer", Answer+"");

    }

    private void loadSetting() {
        diff= settingpfe.getString("DIFF", Setting.DIFF.EASY.toString());
        timeType=settingpfe.getString("TIME", Setting.TIME.TIME_SIXTY.toString());
        add=settingpfe.getBoolean(Setting.OPT.ADD.toString(),true);
        sub=settingpfe.getBoolean(Setting.OPT.SUB.toString(),false);
        mul=settingpfe.getBoolean(Setting.OPT.MUL.toString(),false);
        div=settingpfe.getBoolean(Setting.OPT.DIV.toString(),false);
    }

    public int random(int bound){
       return new Random().nextInt(bound) ;
   }

    public int randomErrorAnswer(int answer){
        if(new Random().nextInt(1)==0) {
            return answer + random(9)+1;
        }else{
            return answer - random(9)+1;
        }

    }
    public void  checkAnsClicked(View v){
        //動態畫勾勾叉叉
        btnA.setClickable(false);
        btnB.setClickable(false);
        btnC.setClickable(false);
        btnD.setClickable(false);
        switch(v.getId()){
            case R.id.buttonA:
                if(btnA.getText().equals(String.valueOf(Answer))){
                    btnA.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_correct));
                    //QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.correct));
                }
                else{
                    btnA.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_error));
                    for(int j=0;j<allAns.size();j++){
                        if(allAns.get(j).equalsIgnoreCase(String.valueOf(Answer)))
                        {
                            changeCorrectButton(j);
                        }
                    }
                   // QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.error));
                }
                break;
            case R.id.buttonB:
                if(btnB.getText().equals(String.valueOf(Answer))){
                    btnB.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_correct));
                    //QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.correct));
                }
                else{
                    btnB.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_error));
                    //QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.error));
                    for(int j=0;j<allAns.size();j++){
                        if(allAns.get(j).equalsIgnoreCase(String.valueOf(Answer)))
                        {
                            changeCorrectButton(j);
                        }
                    }
                }
                break;
            case R.id.buttonC:
                if(btnC.getText().equals(String.valueOf(Answer))){
                    btnC.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_correct));
                    //QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.correct));
                }
                else{
                    btnC.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_error));
                   // QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.error));
                    for(int j=0;j<allAns.size();j++){
                        if(allAns.get(j).equalsIgnoreCase(String.valueOf(Answer)))
                        {
                            changeCorrectButton(j);
                        }
                    }
                }
                break;
            case R.id.buttonD:
                if(btnD.getText().equals(String.valueOf(Answer))){
                    btnD.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_correct));
                    //QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.correct));
                }
                else{
                    btnD.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_error));
                   // QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.error));
                    for(int j=0;j<allAns.size();j++){
                        if(allAns.get(j).equalsIgnoreCase(String.valueOf(Answer)))
                        {
                            changeCorrectButton(j);
                        }
                    }
                }
                break;
        }

        if(restQuestion>0)
        {
            Timer timer = new Timer();

            TimerTask nextQuestion = new TimerTask() {
                @Override
                public void run() {
                    createQuestion();
                }
            };
            timer.schedule(nextQuestion, 850);
        }
    }

    public void createQuestion(){
        allAns.clear();
        restQuestion--;
        QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.question));
        questionQtyText.setBackgroundColor(Color.parseColor("#977c00"));
        questionQtyText.setText((qtyQuestion-restQuestion)+"/"+qtyQuestion);
        runOnUiThread(new Runnable() {
            public void run() {
                btnA.setClickable(true);
                btnB.setClickable(true);
                btnC.setClickable(true);
                btnD.setClickable(true);

            }
        });

        btnA.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_blue));
        btnB.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_blue));
        btnC.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_blue));
        btnD.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_blue));
        Answer=0;
        questonA=random(99);
        questonB=random(99);

        switch (mathType[i]) {
            case "add":
                Answer= questonA+questonB;
                break;
            case "sub":
                Answer= questonA-questonB;
                break;
            case "mul":
                Answer= questonA*questonB;
                break;
            case "div":
                Answer= (questonA/questonB);
                break;
        }

        while(allAns.size()<4){
            int randomAns=randomErrorAnswer(Answer);
            if(!allAns.contains(String.valueOf(randomAns))){
                allAns.add(String.valueOf(randomAns));
            }
        }

        btnA.setText(allAns.get(0));
        btnB.setText(allAns.get(1));
        btnC.setText(allAns.get(2));
        btnD.setText(allAns.get(3));

        int ans=new Random().nextInt(3);
        switch (ans) {
            case 0:
                btnA.setText(String.valueOf(Answer));
                allAns.set(0,String.valueOf(Answer));
                break;
            case 1:
                btnB.setText(String.valueOf(Answer));
                allAns.set(1,String.valueOf(Answer));
                break;
            case 2:
                btnC.setText(String.valueOf(Answer));
                allAns.set(2,String.valueOf(Answer));
                break;
            case 3:
                btnD.setText(String.valueOf(Answer));
                allAns.set(3,String.valueOf(Answer));
                break;
        }

        QuestionText.setText(questonA+parserMathType(mathType[i])+questonB+"=?");
    }
    public String parserMathType(String mathType)
    {
        switch (mathType) {
            case "add":
                return "+";
            case "sub":
                return "-";
            case "mul":
                return "X";
            case "div":
                return "÷ ";
            default:
                return "error parser";
        }
    }

    public void changeCorrectButton(int j)
    {
        switch (j) {
            case 0:
                btnA.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_correct));
                break;
            case 1:
                btnB.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_correct));
                break;
            case 2:
                btnC.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_correct));
                break;
            case 3:
                btnD.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_correct));
                break;
        }
    }
}
