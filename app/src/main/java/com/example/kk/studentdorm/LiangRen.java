package com.example.kk.studentdorm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by kk on 2017/12/25.
 */

public class LiangRen extends Activity implements View.OnClickListener{
    private ImageView backForward,nextBut2;

    private EditText xueHao1,xueHao2,xueHao3,xueHao4,yanZhengma1,yanZhengma2,yanZhengma3,yanZhengma4;
    private int classMateNumbe=0;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.siren);

        backForward=(ImageView)findViewById(R.id.backforward1);
        backForward.setOnClickListener(this);

        nextBut2=(ImageView)findViewById(R.id.next2);
        nextBut2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backforward1){
            //清空同学储存信息
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putString("xueHao1","");
            editor.putString("xueHao2","");
            editor.putString("xueHao3","");
            editor.putString("xueHao4","");
            editor.putString("yanZhengma1","");
            editor.putString("yanZhengma2","");
            editor.putString("yanZhengma3","");
            editor.putString("yanZhengma4","");
            classMateNumbe=0;
            editor.putInt("classMateNumbe",classMateNumbe);
            editor.commit();

            Intent intent = new Intent(TogetherSlelect.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        if(v.getId()==R.id.next2){
            xueHao1=(EditText)findViewById(R.id.classmxuehao1);
            xueHao2=(EditText)findViewById(R.id.classmxuehao2);
            xueHao3=(EditText)findViewById(R.id.classmxuehao3);
            yanZhengma1=(EditText)findViewById(R.id.classmyanzheng1);
            yanZhengma2=(EditText)findViewById(R.id.classmyanzheng2);
            yanZhengma3=(EditText)findViewById(R.id.classmyanzheng3);

            // 存储同学信息

            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putString("xueHao1",xueHao1.getText().toString());
            Log.d("myapp","同学1的学号"+xueHao1.getText().toString());
            classMateNumbe++;
            editor.putInt("classMateNumbe",classMateNumbe);
            editor.commit();


            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putString("yanZhengma1",yanZhengma1.getText().toString());
            Log.d("myapp","同学1的验证码"+yanZhengma1.getText().toString());
            editor.commit();





            Intent intent = new Intent(TogetherSlelect.this, Dormitorys.class);
            startActivity(intent);
        }

    }
}