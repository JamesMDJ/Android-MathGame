package com.james.ma.math_game;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.james.ma.math_game.com.james.ma.math_game.setting.Setting;

public class SettingActivity extends AppCompatActivity {
    RadioGroup radio_Operations1,radio_Operations2;
    RadioGroup radio_Difficulty;
    RadioGroup radio_Timer;
    RadioButton mRbAdd,mRbSub,mRbMul,mRbDiv,mRbMix;
    RadioButton mRbDifficulty_easy,mRbDifficulty_medium,mRbDifficulty_hard;
    RadioButton mRbNoTime,mRbTime_60;
    String diff,timeType;
    Boolean add,sub,mul,div,mix;
    SharedPreferences settingpfe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingpfe = getSharedPreferences("setting", MODE_PRIVATE);
        // Operations
        mRbAdd=findViewById(R.id.radioBtnAdd);
        mRbSub=findViewById(R.id.radioBtnSub);
        mRbMul=findViewById(R.id.radioBtnMultiply);
        mRbDiv=findViewById(R.id.radioBtnDiv);
        mRbMix=findViewById(R.id.radioBtnMix);
        mRbDifficulty_easy=findViewById(R.id.radioBtnEasy);
        mRbDifficulty_medium=findViewById(R.id.radioBtnMedium);
        mRbDifficulty_hard=findViewById(R.id.radioBtnHard);
        mRbNoTime=findViewById(R.id.radioBtnNoTime);
        mRbTime_60=findViewById(R.id.radioBtnCountdownSixty);

        radio_Difficulty=findViewById(R.id.RedioGroupDifficulty);
        radio_Timer=findViewById(R.id.RedioGroupTime);
        radioBtnListen();
        loadSetting();

    }

    private void loadSetting() {
        diff= settingpfe.getString("DIFF", Setting.DIFF.EASY.toString());
        timeType=settingpfe.getString("TIME", Setting.TIME.TIME_SIXTY.toString());
        add=settingpfe.getBoolean(Setting.OPT.ADD.toString(),true);
        sub=settingpfe.getBoolean(Setting.OPT.SUB.toString(),false);
        mul=settingpfe.getBoolean(Setting.OPT.MUL.toString(),false);
        div=settingpfe.getBoolean(Setting.OPT.DIV.toString(),false);
        mix=settingpfe.getBoolean(Setting.OPT.MIX.toString(),false);
        if(add){
            mRbAdd.setChecked(true);
        }
        if(sub){
            mRbSub.setChecked(true);
        }
        if(mul){
            mRbMul.setChecked(true);
        }
        if(div){
            mRbDiv.setChecked(true);
        }
        if(mix){
            mRbMix.setChecked(true);
        }
        switch(diff)
        {
            case "EASY":
                mRbDifficulty_easy.setChecked(true);
                break;
            case "MID":
                mRbDifficulty_medium.setChecked(true);
                break;
            case "HARD":
                mRbDifficulty_hard.setChecked(true);
                break;
        }
        switch(timeType)
        {
            case "NO_TIME":
                mRbNoTime.setChecked(true);
                break;
            case "TIME_SIXTY":
                mRbTime_60.setChecked(true);
                break;
        }
    }

    private void radioBtnListen() {
        View.OnClickListener add_radio_listener = new View.OnClickListener(){
            public void onClick(View v) {
                mRbSub.setChecked(false);
                mRbMul.setChecked(false);
                mRbDiv.setChecked(false);
                mRbMix.setChecked(false);
            }
        };
        View.OnClickListener sub_radio_listener = new View.OnClickListener(){
            public void onClick(View v) {
                mRbAdd.setChecked(false);
                mRbMul.setChecked(false);
                mRbDiv.setChecked(false);
                mRbMix.setChecked(false);
            }
        };
        View.OnClickListener mul_radio_listener = new View.OnClickListener(){
            public void onClick(View v) {
                mRbAdd.setChecked(false);
                mRbSub.setChecked(false);
                mRbDiv.setChecked(false);
                mRbMix.setChecked(false);

            }
        };
        View.OnClickListener div_radio_listener = new View.OnClickListener(){
            public void onClick(View v) {
                mRbAdd.setChecked(false);
                mRbSub.setChecked(false);
                mRbMul.setChecked(false);
                mRbMix.setChecked(false);

            }
        };
        View.OnClickListener mix_radio_listener = new View.OnClickListener(){
            public void onClick(View v) {
                mRbAdd.setChecked(false);
                mRbSub.setChecked(false);
                mRbMul.setChecked(false);
                mRbDiv.setChecked(false);

            }
        };
        mRbAdd.setOnClickListener(add_radio_listener);
        mRbSub.setOnClickListener(sub_radio_listener);
        mRbMul.setOnClickListener(mul_radio_listener);
        mRbDiv.setOnClickListener(div_radio_listener);
        mRbMix.setOnClickListener(mix_radio_listener);
    }


    public void saveSetting(View v){
        if(mRbAdd.isChecked())
        {
            settingpfe.edit().putBoolean(Setting.OPT.ADD.toString(), true).apply();
        }else{
            settingpfe.edit().putBoolean(Setting.OPT.ADD.toString(), false).apply();
        }
        if(mRbSub.isChecked())
        {
            settingpfe.edit().putBoolean(Setting.OPT.SUB.toString(), true).apply();
        }else{
            settingpfe.edit().putBoolean(Setting.OPT.SUB.toString(), false).apply();
        }
        if(mRbMul.isChecked())
        {
            settingpfe.edit().putBoolean(Setting.OPT.MUL.toString(), true).apply();
        }else{
            settingpfe.edit().putBoolean(Setting.OPT.MUL.toString(), false).apply();
        }
        if(mRbDiv.isChecked())
        {
            settingpfe.edit().putBoolean(Setting.OPT.DIV.toString(), true).apply();
        }else{
            settingpfe.edit().putBoolean(Setting.OPT.DIV.toString(), false).apply();
        }
        if(mRbMix.isChecked())
        {
            settingpfe.edit().putBoolean(Setting.OPT.MIX.toString(), true).apply();
        }else{
            settingpfe.edit().putBoolean(Setting.OPT.MIX.toString(), false).apply();
        }

        switch(radio_Difficulty.getCheckedRadioButtonId()){
            //存檔
            case R.id.radioBtnEasy:
                settingpfe.edit().putString("DIFF", Setting.DIFF.EASY.toString()).apply();
                break;
            case R.id.radioBtnMedium:
                settingpfe.edit().putString("DIFF",Setting.DIFF.MID.toString()).apply();
                break;
            case R.id.radioBtnHard:
                settingpfe.edit().putString("DIFF", Setting.DIFF.HARD.toString()).apply();
                break;
        }
        switch(radio_Timer.getCheckedRadioButtonId()){
            //存檔
            case R.id.radioBtnNoTime:
                settingpfe.edit().putString("TIME", Setting.TIME.NO_TIME.toString()).apply();
                break;
            case R.id.radioBtnCountdownSixty:
                settingpfe.edit().putString("TIME",Setting.TIME.TIME_SIXTY.toString()).apply();
                break;
        }
//        String diff= settingpfe.getString("DIFF", Setting.DIFF.EASY.toString());
//        String timeType=settingpfe.getString("TIME", Setting.TIME.TIME_SIXTY.toString());
//        Boolean  add=settingpfe.getBoolean(Setting.OPT.ADD.toString(),true);
//        Boolean  sub=settingpfe.getBoolean(Setting.OPT.SUB.toString(),false);
//        Boolean  mul=settingpfe.getBoolean(Setting.OPT.MUL.toString(),false);
//        Boolean  div=settingpfe.getBoolean(Setting.OPT.DIV.toString(),false);
        AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this);
        dialog.setTitle("Save");
        dialog.setMessage("Successful Storage");
//        dialog.setMessage("Successful Storage"+" diff="+diff+" timeType="+timeType+" add="+add+" sub="+sub+" mul="+mul+" div="+div);
        dialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });
        dialog.show();
    }

    public void cancelSetting(View v){
        Intent intent = new Intent(SettingActivity.this, StartActivity.class);
        startActivity(intent);
    }


}
