package com.example.kk.studentdorm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by kk on 2017/12/25.
 */

public class SanRen extends Activity implements View.OnClickListener{
    private ImageView back;
    private Button jixuBtn;
    private EditText xueHao1,xueHao2,jiaoYan1,jiaoYan2;



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sanren);

        back=(ImageView)findViewById(R.id.back);          //返回按钮
        back.setOnClickListener(this);

        jixuBtn=(Button) findViewById(R.id.jixu);           //继续按钮
        jixuBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.back){                 //返回到信息界面
            Intent intent = new Intent(SanRen.this, StudentMessege.class);
            startActivity(intent);
            finish();
        }

        if(v.getId()==R.id.jixu){                //跳转到选择界面
            xueHao1=(EditText)findViewById(R.id.xiexuehao1);
            xueHao2=(EditText)findViewById(R.id.xiexuehao2);
            jiaoYan1=(EditText)findViewById(R.id.xiejiaoyanma1);
            jiaoYan2=(EditText)findViewById(R.id.xiejiaoyanma2);

            // 存储同住人信息
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putString("xueHao1",xueHao1.getText().toString());
            editor.putString("jiaoYan1",jiaoYan1.getText().toString());
            editor.putString("xueHao2",xueHao2.getText().toString());
            editor.putString("jiaoYan2",jiaoYan2.getText().toString());
            editor.putInt("Num",3);
            editor.commit();

            Intent intent = new Intent(SanRen.this, Select.class);
            startActivity(intent);
        }

    }
}
