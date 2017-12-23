package com.example.kk.studentdorm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kk.util.SslUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kk on 2017/11/22.
 */

public class StudentMessege extends Activity implements ViewStub.OnClickListener{
    private static final int RESEARCH =1;
    private int errcode=1;
    private HttpURLConnection connect;
    private String data;

    private Button chaxunBtn;
    private Button fanhuiBtn;

    private TextView xueHao,xingMing,xingBie,xiaoQu,nianJi,jiaoYanMa,louHao,suSheHao;

    private Handler mHandler =new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case RESEARCH:
                    String responseStr=(String)msg.obj;
                    try {                                    //JSON解析
                        JSONObject obj = new JSONObject(responseStr);
                        errcode = obj.getInt("errcode");
                        data = obj.getString("data");
                        JSONObject obj1 = new JSONObject(data);
                        xueHao=(TextView)findViewById(R.id.xuehao);
                        xingMing=(TextView)findViewById(R.id.xingming);
                        xingBie=(TextView)findViewById(R.id.xingbie);
                        xiaoQu=(TextView)findViewById(R.id.xiaoqu);
                        nianJi=(TextView)findViewById(R.id.nianji);
                        jiaoYanMa=(TextView)findViewById(R.id.jiaoyanma);
                        louHao=(TextView)findViewById(R.id.louhao);
                        suSheHao=(TextView)findViewById(R.id.sushehao);

                        xueHao.setText("学号："+obj1.getString("studentid"));
                        xingMing.setText("姓名："+obj1.getString("name"));
                        xingBie.setText("性别："+obj1.getString("gender"));
                        xiaoQu.setText("校区："+obj1.getString("location"));
                        nianJi.setText("年级："+obj1.getString("grade"));
                        jiaoYanMa.setText("验证码："+obj1.getString("vcode"));
                        int i = Integer.parseInt(obj1.getString("studentid"));
                        if(i%2==0){
                            louHao.setText("已选楼号："+obj1.getString("building"));
                            suSheHao.setText("已选宿舍号："+obj1.getString("room"));
                        }else{
                            louHao.setText("已选楼号：未选");
                            suSheHao.setText("已选宿舍号：未选");
                        }

                        // 存储解析结果
                        SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
                        editor.putString("xueHao", obj1.getString("studentid"));
                        editor.putString("xingMing", obj1.getString("name"));
                        editor.putString("xingBie", obj1.getString("gender"));
                        editor.putString("xiaoQu", obj1.getString("location"));
                        editor.putString("nianJi", obj1.getString("grade"));
                        editor.putString("jiaoYanMa", obj1.getString("vcode"));
                        editor.putString("louHao", obj1.getString("building"));
                        editor.putString("suSheHao", obj1.getString("room"));
                        editor.commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.if_ok);

        chaxunBtn=(Button) findViewById(R.id.chaxun);      //查询按钮
        chaxunBtn.setOnClickListener(this);

        fanhuiBtn=(Button) findViewById(R.id.fanhui);                 //返回按钮
        fanhuiBtn.setOnClickListener(this);

        queryMessege();       //查询个人信息
    }



    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.chaxun){
            Intent intent = new Intent(StudentMessege.this, ChuangWei.class);
            startActivity(intent);
            finish();
        }


        if(v.getId()== R.id.fanhui){
            // 清除存储信息
           /* SharedPreferences.Editor editor = getSharedPreferences("config",MODE_PRIVATE).edit();
            editor.putInt("logInFlag", 1);
            editor.putString("usercode", "");
            editor.putString("password",  "");
            editor.putString("xingMing","");
            editor.putString("xingBie", "");
            editor.putString("yanZhengma", "");
            editor.putString("xiaoQu", "");
            editor.putString("nianJi","");
            editor.commit();     */

            Intent intent = new Intent(StudentMessege.this, LogIn.class);
            startActivity(intent);
            finish();
        }


    }



    private void queryMessege(){                       //查询个人信息
        //读取学号
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        String usercode=sharedPreferences.getString("usercode","");

        final String ip1 ="https://api.mysspku.com/index.php/V1/MobileCourse/getDetail?stuid="+usercode;

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url=new URL(ip1);
                            if("https".equalsIgnoreCase(url.getProtocol())){
                                SslUtils.ignoreSsl();
                            }
                            connect=(HttpURLConnection)url.openConnection();
                            connect.setRequestMethod("GET");
                            InputStream in = connect.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder response = new StringBuilder();
                            String str;
                            while((str=reader.readLine()) != null){
                                response.append(str);
                            }
                            String responseStr=response.toString();

                            //将结果传给主线程
                            Message msg = new Message();
                            msg.what=RESEARCH;
                            msg.obj=responseStr;
                            mHandler.sendMessage(msg);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).start();
    }

}

