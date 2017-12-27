package com.example.kk.studentdorm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by kk on 2017/12/25.
 */

public class Success extends Activity implements View.OnClickListener{
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);

        back = (ImageView) findViewById(R.id.back);         //返回按钮
        back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {             //返回到人数界面
            Intent intent = new Intent(Success.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
