package com.example.kk.studentdorm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kk.util.NetUtil;
import com.example.kk.util.SslUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kk on 2017/11/7.
 */

public class LogIn extends Activity implements View.OnClickListener{
    private static final int LOGIN = 1;
    private int errcode=1;
    private HttpURLConnection connnect;
    private Button button_fanhui;
    private Button button_denglu;

    private Handler mHandler = new Handler(){         //开辟子线程
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case LOGIN:
                    String responseStr=(String)msg.obj;
                    try {                                     //JSON解析
                        JSONObject obj = new JSONObject(responseStr);
                        errcode = obj.getInt("errcode");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //判断用户名密码是否正确
                    if (errcode==0) {
                        SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
                        editor.putInt("logInFlag", 0);
                        editor.commit();

                        Intent intent = new Intent(LogIn.this, StudentMessege.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LogIn.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate( Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        // 获取存储的数据
      /*  SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);

        int logInFlag = sp.getInt("logInFlag", 1);
        if(logInFlag==0){
            //如果用户已经登录，直接跳转到MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            //关闭当前界面
            finish();
        }      */

        button_fanhui=(Button)findViewById(R.id.fanhui);       //返回按钮
        button_fanhui.setOnClickListener(this);

        button_denglu=(Button)findViewById(R.id.denglu);       //登录按钮
        button_denglu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if(view.getId()==R.id.fanhui) {                         //返回事件，回到主界面
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        if(view.getId()==R.id.denglu) {                           //登陆事件
            EditText xiexuehao=(EditText)findViewById(R.id.xiexuehao);
            EditText xiemima=(EditText)findViewById(R.id.xiemima);
            final String xuehao=xiexuehao.getText().toString();
            final String mima=xiemima.getText().toString();

            // 存储学号密码
            SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putString("usercode", xuehao);
            editor.putString("password", mima);
            editor.commit();

            //检验网络
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                queryLogin(xuehao, mima);       //如果网络正常，获取网络数据
            } else {
                Toast.makeText(LogIn.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }

        }

    }




    public void queryLogin(String username, String password) {          //登录验证
        final  String address = "https://api.mysspku.com/index.php/V1/MobileCourse/Login?"+"username=" + username
                + "&password=" + password;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url =new URL(address);
                    if("https".equalsIgnoreCase(url.getProtocol())){
                        SslUtils.ignoreSsl();
                    }

                    connnect=(HttpURLConnection)url.openConnection();
                    connnect.setRequestMethod("GET");
                    InputStream in = connnect.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while((str=reader.readLine()) != null){
                        response.append(str);
                    }
                    String responseStr=response.toString();

                    //将结果传给主线程
                    Message msg = new Message();
                    msg.what=LOGIN;
                    msg.obj=responseStr;
                    mHandler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



}
