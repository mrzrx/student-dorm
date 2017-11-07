package com.example.kk.studentdorm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by kk on 2017/11/7.
 */

public class LogIn extends Activity implements View.OnClickListener{
    private Button button_fanhui;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        button_fanhui=(Button)findViewById(R.id.fanhui);
        button_fanhui.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){

        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);

    }
}
