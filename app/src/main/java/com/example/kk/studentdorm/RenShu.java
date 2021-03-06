package com.example.kk.studentdorm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


/**
 * Created by kk on 2017/12/25.
 */

public class RenShu extends Activity implements View.OnClickListener{
    private ImageView renshuBack;
    private ListView mList;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renshu);

        renshuBack=(ImageView)findViewById(R.id.renshu_back);    //返回按钮
        renshuBack.setOnClickListener(this);

        String[] data_renshu=new String[4];         //办理人数列表
        data_renshu[0]="单人办理";
        data_renshu[1]="两人办理";
        data_renshu[2]="三人办理";
        data_renshu[3]="四人办理";

        mList=(ListView)findViewById(R.id.renshu_list);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(RenShu.this,android.R.layout.simple_list_item_1,data_renshu);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l){        //为列表添加单击事件，跳转到相应界面
                switch (i) {
                    case 0:
                        Intent intent = new Intent(RenShu.this, Select.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 1:
                        Intent intent1 = new Intent(RenShu.this, LiangRen.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case 2:
                        Intent intent2 = new Intent(RenShu.this, SanRen.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case 3:
                        Intent intent3 = new Intent(RenShu.this, SiRen.class);
                        startActivity(intent3);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.renshu_back){                 //返回到信息界面
            Intent intent = new Intent(RenShu.this, StudentMessege.class);
            startActivity(intent);
            finish();
        }
    }







}

