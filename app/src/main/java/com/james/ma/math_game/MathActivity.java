package com.james.ma.math_game;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.james.ma.math_game.com.james.ma.math_game.setting.Setting;

import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MathActivity extends AppCompatActivity {
    Button btnA,btnB,btnC,btnD,btn_quit,btn_resume;
    int questonA,questonB,Answer;
    TextView QuestionText,questionQtyText,timeText,scoreText;
    LinearLayout questionLayout;
    int qtyQuestion,restQuestion,countTime,score;
    LinkedList<String> allAns;
    int i=0,questionType=0,answerType=Setting.ANSWER.answer.ordinal();
    String diff,timeType;
    Boolean add,sub,mul,div,mix;
    SharedPreferences settingpfe;
    public Long startTime,postTime;
    private Handler handler = new Handler();
    //View viewPauseDialog;

    //Create an interstitial ad object
    private InterstitialAd interstitialAd;

    // achievements and scores we're pending to push to the cloud
    // (waiting for the user to sign in, for instance)
    String mathType[]={"add","sub","mul","div","mix"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        // Create the InterstitialAd and set the adUnitId.
        interstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        interstitialAd.setAdUnitId(getString(R.string.ad_unit_id));

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startGame();
            }
        });

        if (!interstitialAd.isLoading() && !interstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
        }

        QuestionText=findViewById(R.id.textQuestion);
        questionQtyText=findViewById(R.id.textQuestionQty);
        questionLayout=findViewById(R.id.questionLayout);
        timeText=findViewById(R.id.textTime);
        scoreText=findViewById(R.id.textGetScore);
        allAns=new LinkedList<String>();
        btnA=findViewById(R.id.buttonA);
        btnB=findViewById(R.id.buttonB);
        btnC=findViewById(R.id.buttonC);
        btnD=findViewById(R.id.buttonD);
        score=0;
        qtyQuestion=15; //題目數量
        restQuestion=qtyQuestion; //剩餘題目數量
        settingpfe=getSharedPreferences("setting", MODE_PRIVATE);
        loadSetting();
        createQuestion(diff);

        Log.d("answer", Answer+"");

        //取得目前時間
        startTime = System.currentTimeMillis();
        handler.postDelayed(updateTimer, 200);

    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            //Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            startGame();
        }
    }

    private void startGame() {
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
        if (!interstitialAd.isLoading() && !interstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
        }

        Intent intent = new Intent(MathActivity.this, MathActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        handler.removeCallbacks(updateTimer);
        super.onDestroy();

    }

    private void loadSetting() {
        diff= settingpfe.getString("DIFF", Setting.DIFF.EASY.toString());
        timeType=settingpfe.getString("TIME", Setting.TIME.TIME_SIXTY.toString());
        if(timeType.equalsIgnoreCase(Setting.TIME.TIME_SIXTY.toString())){
            countTime=60;
        }
        if(timeType.equalsIgnoreCase(Setting.TIME.NO_TIME.toString())){
            countTime=0;
        }
        add=settingpfe.getBoolean(Setting.OPT.ADD.toString(),true);
        sub=settingpfe.getBoolean(Setting.OPT.SUB.toString(),false);
        mul=settingpfe.getBoolean(Setting.OPT.MUL.toString(),false);
        div=settingpfe.getBoolean(Setting.OPT.DIV.toString(),false);
        mix=settingpfe.getBoolean(Setting.OPT.MIX.toString(),false);
        if(add){
            questionType=0;
        }
        else if(sub) {
            questionType=1;
        }else if(mul){
            questionType=2;
        }else if(div){
            questionType=3;
        }else if(mix){
            questionType=4;
        }
    }


    private Runnable updateTimer = new Runnable() {
        public void run() {
            Long spentTime = System.currentTimeMillis() - startTime;
            //計算目前已過分鐘數
            Long minius = (spentTime/1000)/60;
            //計算目前已過秒數
            postTime = (spentTime/1000) % 60;
            Log.d("posttime:",postTime+"");
            Log.d("spentTime:",spentTime+"");
            scoreText.setText(score+"");
            handler.postDelayed(this, 1000);
            if((spentTime/1000)>59){
                timeText.setText("0");
            }else{
                timeText.setText(countTime-postTime+"");
            }

        }
    };

    public int random(int bound){
       return new Random().nextInt(bound) ;
   }

    public int randomErrorAnswer(int answer){
        if(new Random().nextInt(2)==0) {
            if(new Random().nextInt(2)==0){
                return answer + random(9)+10;
            }else{
                return answer + random(9)-10;
            }
        }else{
            if(new Random().nextInt(2)==0){
                return answer - random(9)+10;
            }else{
                return answer - random(9)-10;
            }
        }

    }
    public void  checkAnsClicked(View v){
        //動態畫勾勾叉叉
        btnA.setClickable(false);
        btnB.setClickable(false);
        btnC.setClickable(false);
        btnD.setClickable(false);
        int tmpAnswer = 0;
        if(answerType==Setting.ANSWER.answer.ordinal()){
            tmpAnswer=Answer;
        }else if(answerType==Setting.ANSWER.questionA.ordinal())
        {
            tmpAnswer=questonA;
        }else if(answerType==Setting.ANSWER.questionB.ordinal())
        {
            tmpAnswer=questonB;
        }
        switch(v.getId()){
            case R.id.buttonA:
                if(btnA.getText().equals(String.valueOf(tmpAnswer))){
                    btnA.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_correct));
                    //QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.correct));
                    calScore(Integer.valueOf(timeText.getText().toString()));

                }
                else{
                    btnA.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_error));
                    for(int j=0;j<allAns.size();j++){
                        if(allAns.get(j).equalsIgnoreCase(String.valueOf(tmpAnswer)))
                        {
                            changeCorrectButton(j);
                        }
                    }
                   // QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.error));
                }
                break;
            case R.id.buttonB:
                if(btnB.getText().equals(String.valueOf(tmpAnswer))){
                    btnB.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_correct));
                    //QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.correct));
                    calScore(Integer.valueOf(timeText.getText().toString()));
                }
                else{
                    btnB.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_error));
                    //QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.error));
                    for(int j=0;j<allAns.size();j++){
                        if(allAns.get(j).equalsIgnoreCase(String.valueOf(tmpAnswer)))
                        {
                            changeCorrectButton(j);
                        }
                    }
                }
                break;
            case R.id.buttonC:
                if(btnC.getText().equals(String.valueOf(tmpAnswer))){
                    btnC.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_correct));
                    //QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.correct));
                    calScore(Integer.valueOf(timeText.getText().toString()));
                }
                else{
                    btnC.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_error));
                   // QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.error));
                    for(int j=0;j<allAns.size();j++){
                        if(allAns.get(j).equalsIgnoreCase(String.valueOf(tmpAnswer)))
                        {
                            changeCorrectButton(j);
                        }
                    }
                }
                break;
            case R.id.buttonD:
                if(btnD.getText().equals(String.valueOf(tmpAnswer))){
                    btnD.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_correct));
                    //QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.correct));
                    calScore(Integer.valueOf(timeText.getText().toString()));
                }
                else{
                    btnD.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_error));
                   // QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.error));
                    for(int j=0;j<allAns.size();j++){
                        if(allAns.get(j).equalsIgnoreCase(String.valueOf(tmpAnswer)))
                        {
                            changeCorrectButton(j);
                        }
                    }
                }
                break;
        }

        if(restQuestion>0)
        {
            startTime = System.currentTimeMillis();
            Timer timer = new Timer();
            TimerTask nextQuestion = new TimerTask() {
                @Override
                public void run() {
                    createQuestion(diff);
                }
            };
            timer.schedule(nextQuestion, 1000);
        }
        if(restQuestion==0){
            //game over
            handler.removeCallbacks(updateTimer);
            AlertDialog.Builder builderGameover=new AlertDialog.Builder(this);
            builderGameover.setTitle("Game Over");
            builderGameover.setMessage(getResources().getString(R.string.score)+score);
            builderGameover.setPositiveButton(getResources().getString(R.string.again), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    showInterstitial();
                }
            });
            builderGameover.setNegativeButton(getResources().getString(R.string.quit), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MathActivity.this, StartActivity.class);
                    startActivity(intent);
                }
            });
            final AlertDialog alert=builderGameover.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
            if(GoogleSignIn.getLastSignedInAccount(this)!=null) {
                submitScore();
            }

        }
    }

    public void submitScore(){
        if(diff.equalsIgnoreCase(Setting.DIFF.EASY.toString())) {
            switch(questionType){
                case 0:
                    Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .submitScore(getString(R.string.leaderboard_level_easyadd),score );
                    break;
                case 1:
                    Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .submitScore(getString(R.string.leaderboard_level_easysub),score );
                    break;
                case 2:
                    Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .submitScore(getString(R.string.leaderboard_level_easymultiplication),score );
                    break;
                case 3:
                    Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .submitScore(getString(R.string.leaderboard_level_easydivision),score );
                    break;
                case 4:
                    Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .submitScore(getString(R.string.leaderboard_level_easymix),score );
                    break;
            }
        }
        if(diff.equalsIgnoreCase(Setting.DIFF.MID.toString())) {
            switch(questionType){
                case 0:
                    Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .submitScore(getString(R.string.leaderboard_level__mediumadd),score );
                    break;
                case 1:
                    Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .submitScore(getString(R.string.leaderboard_level__mediumsub),score );
                    break;
                case 2:
                    Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .submitScore(getString(R.string.leaderboard_level__mediummultiplication),score );
                    break;
                case 3:
                    Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .submitScore(getString(R.string.leaderboard_level__mediumdivision),score );
                    break;
                case 4:
                    Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .submitScore(getString(R.string.leaderboard_level__mediummix),score );
                    break;
            }
        }
        if(diff.equalsIgnoreCase(Setting.DIFF.HARD.toString())) {
            switch(questionType){
                case 0:
                    Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .submitScore(getString(R.string.leaderboard_level_hardadd),score );
                    break;
                case 1:
                    Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .submitScore(getString(R.string.leaderboard_level_hardsub),score );
                    break;
                case 2:
                    Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .submitScore(getString(R.string.leaderboard_level_hardmultiplication),score );
                    break;
                case 3:
                    Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .submitScore(getString(R.string.leaderboard_level_harddivision),score );
                    break;
                case 4:
                    Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .submitScore(getString(R.string.leaderboard_level_hardmix),score );
                    break;
            }
        }
    }


    public void calScore(int time){
        score+=time*10;
    }

    public void createQuestion(final String diff){

        allAns.clear();
        i=questionType;
        restQuestion--;
        runOnUiThread(new Runnable() {
            public void run() {
        QuestionText.setBackgroundDrawable(getResources().getDrawable(R.drawable.question));
        questionQtyText.setBackgroundColor(Color.parseColor("#977c00"));
        questionQtyText.setText((qtyQuestion-restQuestion)+"/"+qtyQuestion);

                btnA.setClickable(true);
                btnB.setClickable(true);
                btnC.setClickable(true);
                btnD.setClickable(true);


        btnA.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_blue));
        btnB.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_blue));
        btnC.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_blue));
        btnD.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_rectangle_light_blue));
        Answer=0;
        if(i==4){
            i=random(3);
        }
        //難度配置
        if(diff.equalsIgnoreCase(Setting.DIFF.EASY.toString())){
            //個位數 加減乘除
            questonA=random(9)+1;
            questonB=random(9)+2;
            while(i==3){
                questonA=random(99)+1;
                questonB=random(9)+2;
                if(questonA%questonB==0){
                    break;
                }else{
                    questonA=random(9)+1;
                    questonB=random(9)+1;
                }
            }
        }else if(diff.equalsIgnoreCase(Setting.DIFF.MID.toString())){
            //十進制 加減 十進制跟個位數 乘除
            questonA=random(99);
            questonB=random(99);
            if(i==2){
                questonB=random(9)+2;
            }
            while(i==3){
                questonA=random(99)+1;
                questonB=random(9)+2;
                if(questonA%questonB==0){
                    break;
                }else{
                    questonA=random(9)+1;
                    questonB=random(9)+1;
                }
            }
        } else if (diff.equalsIgnoreCase(Setting.DIFF.HARD.toString())) {
            questonA=random(99);
            questonB=random(99);
            if(i==2){
                questonB=random(9)+2;
            }
            while(i==3){
                questonA=random(99)+1;
                questonB=random(9)+2;
                if(questonA%questonB==0){
                    break;
                }else{
                    questonA=random(9)+1;
                    questonB=random(9)+1;
                }
            }
        }

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

        answerType= Setting.ANSWER.answer.ordinal();
        if (diff.equalsIgnoreCase(Setting.DIFF.HARD.toString())){
            int radomQuestionType=random(3);
            if(radomQuestionType==0){
                answerType= Setting.ANSWER.questionA.ordinal();
                QuestionText.setText("?" + parserMathType(mathType[i]) + questonB + "="+Answer);
            }else if(radomQuestionType==1){
                answerType= Setting.ANSWER.questionB.ordinal();
                QuestionText.setText(questonA + parserMathType(mathType[i]) + "?" + "="+Answer);
            }else{
                QuestionText.setText(questonA + parserMathType(mathType[i]) + questonB + "=?");
            }

        }else {
            QuestionText.setText(questonA + parserMathType(mathType[i]) + questonB + "=?");
        }

        while(allAns.size()<4){
            int randomAns=0;
            if(answerType==Setting.ANSWER.answer.ordinal()){
                randomAns=randomErrorAnswer(Answer);
            }else if(answerType==Setting.ANSWER.questionA.ordinal())
            {
                randomAns=randomErrorAnswer(questonA);
            }else if(answerType==Setting.ANSWER.questionB.ordinal())
            {
                randomAns=randomErrorAnswer(questonB);
            }

            if(!allAns.contains(String.valueOf(randomAns))){
                allAns.add(String.valueOf(randomAns));
            }
        }
        //btn猜題答案
                if(allAns.size()==4) {
                    btnA.setText(allAns.get(0));
                    btnB.setText(allAns.get(1));
                    btnC.setText(allAns.get(2));
                    btnD.setText(allAns.get(3));
                }
        int ans=new Random().nextInt(3);
        int answerTmp=0;
        Log.d("answerType:",answerType+"");
        if(answerType==Setting.ANSWER.answer.ordinal()){
            answerTmp=Answer;
        }else if(answerType==Setting.ANSWER.questionA.ordinal())
        {
            answerTmp=questonA;
        }else if(answerType==Setting.ANSWER.questionB.ordinal())
        {
            answerTmp=questonB;
        }
        switch (ans) {
            case 0:
                btnA.setText(String.valueOf(answerTmp));
                allAns.set(0,String.valueOf(answerTmp));
                break;
            case 1:
                btnB.setText(String.valueOf(answerTmp));
                allAns.set(1,String.valueOf(answerTmp));
                break;
            case 2:
                btnC.setText(String.valueOf(answerTmp));
                allAns.set(2,String.valueOf(answerTmp));
                break;
            case 3:
                btnD.setText(String.valueOf(answerTmp));
                allAns.set(3,String.valueOf(answerTmp));
                break;
        }
            }
        });

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

    //onBackPressed
    @Override
    public void onBackPressed() {
        final Long tmpTime=postTime;
        timeText.setText(countTime-tmpTime+"");
        handler.removeCallbacks(updateTimer);

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        LayoutInflater factory=LayoutInflater.from(MathActivity.this);
        View viewPauseDialog=factory.inflate(R.layout.dialog_pause,null);
        builder.setView(viewPauseDialog);
        final AlertDialog dialog = builder.show();
        dialog.setCanceledOnTouchOutside(false);
        btn_resume= viewPauseDialog.findViewById(R.id.btn_resume);
        btn_quit= viewPauseDialog.findViewById(R.id.btn_quit);
        btn_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.postDelayed(updateTimer, 200);
                startTime = System.currentTimeMillis()-(tmpTime*1000);
                dialog.dismiss();
                Log.d("btn_resume ","onClick");
            }
        });
        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Log.d("btn_quit ","onClick");
            }
        });
    }


}
