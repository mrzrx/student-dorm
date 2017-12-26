package com.example.kk.studentdorm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kk.util.SslUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kk on 2017/12/25.
 */

public class Select extends Activity implements View.OnClickListener{
    private static final int CHUANGWEI =1;
    private static final int SUCCESS =2;
    private HttpURLConnection connect;
    private int errcode1=1,errcode2=1;
    private String data;
    private String gender;
    private ListView mList;
    private ImageView backBtn;
    private Button jixuBtn;



    private Handler mHandler =new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case CHUANGWEI:
                    String responseStr=(String)msg.obj;
                    try {                                   //JSON解析
                        JSONObject obj = new JSONObject(responseStr);
                        errcode1 = obj.getInt("errcode");
                        data = obj.getString("data");
                        JSONObject obj1 = new JSONObject(data);

                        String[] data_chuanwei=new String[5];
                        data_chuanwei[0]="5号楼剩余床位数："+obj1.getString("5");
                        data_chuanwei[1]="13号楼剩余床位数："+obj1.getString("13");
                        data_chuanwei[2]="14号楼剩余床位数："+obj1.getString("14");
                        data_chuanwei[3]="8号楼剩余床位数："+obj1.getString("8");
                        data_chuanwei[4]="9号楼剩余床位数："+obj1.getString("9");

                        mList=(ListView)findViewById(R.id.list);
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(Select.this,android.R.layout.simple_list_item_1,data_chuanwei);
                        mList.setAdapter(adapter);

                        mList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l){
                                    if (i== 0) {
                                        SharedPreferences.Editor editor = getSharedPreferences("config", MODE_PRIVATE).edit();
                                        editor.putInt("Dormitory", 5);
                                        editor.commit();
                                        //initview();
                                    }

                                    if (i==1) {
                                        SharedPreferences.Editor editor = getSharedPreferences("config", MODE_PRIVATE).edit();
                                        editor.putInt("Dormitory", 8);
                                        editor.commit();
                                        //initview();
                                    }

                                    if(i==2) {
                                        SharedPreferences.Editor editor = getSharedPreferences("config", MODE_PRIVATE).edit();
                                        editor.putInt("Dormitory", 9);
                                        editor.commit();
                                       // initview();
                                    }

                                    if(i==3) {
                                        SharedPreferences.Editor editor = getSharedPreferences("config", MODE_PRIVATE).edit();
                                        editor.putInt("Dormitory", 13);
                                        editor.commit();
                                       // initview();
                                    }

                                    if(i==4) {
                                        SharedPreferences.Editor editor = getSharedPreferences("config", MODE_PRIVATE).edit();
                                        editor.putInt("Dormitory", 14);
                                        editor.commit();
                                       // initview();
                                    }

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                case SUCCESS:
                    String responseStr1=(String)msg.obj;
                    try {                                                    //JSON解析
                        JSONObject obj = new JSONObject(responseStr1);
                        errcode2 = obj.getInt("errcode");
                        if(errcode2==0){
                            //Toast.makeText(Dormitorys.this, "选宿舍成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Select.this, Success.class);
                            startActivity(intent);
                            finish();
                        }else{
                            //Toast.makeText(Dormitorys.this, "选宿舍失败，请重新选宿舍", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    default:
                        break;
            }
        }
    };



    /*private Handler mHandler1 =new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case SUCCESS:
                    String responseStr=(String)msg.obj;
                    try {                                                    //JSON解析
                        JSONObject obj = new JSONObject(responseStr);
                        errcode2 = obj.getInt("errcode");
                        if(errcode2==0){
                            //Toast.makeText(Dormitorys.this, "选宿舍成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Select.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            //Toast.makeText(Dormitorys.this, "选宿舍失败，请重新选宿舍", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };
*/



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);

        backBtn=(ImageView)findViewById(R.id.back);
        backBtn.setOnClickListener(this);

        jixuBtn=(Button)findViewById(R.id.queding);
        jixuBtn.setOnClickListener(this);

        //获取登陆人性别
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        String xingbie=sharedPreferences.getString("xingBie","");
        if(xingbie.equals("男")) {
            gender ="";
        }else if(xingbie.equals("女")){
            gender="2";
        }

        final String ip1 = "https://api.mysspku.com/index.php/V1/MobileCourse/getRoom?gender="+gender;

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
                            msg.what=CHUANGWEI;
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

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.back){
            Intent intent = new Intent(Select.this, RenShu.class);
            startActivity(intent);
            finish();
        }

        if(v.getId()==R.id.queding){
            Map<String, Object> map = new HashMap<String, Object>();
            //获取办理人数
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            int Num = sharedPreferences.getInt("Num", 0);
            int total = Num + 1;
            int i = Num;

            map.put("num", total);
            //获取学号校验码
            String usercode = sharedPreferences.getString("usercode", "");
            map.put("stuid", usercode);
            if (i > 0) {
                String xueHao1 = sharedPreferences.getString("xueHao1", "");
                map.put("stu1id", xueHao1);
                String jiaoYan1 = sharedPreferences.getString("jiaoYan1", "");
                map.put("v1code", jiaoYan1);
                i--;
            }
            if (i > 0) {
                String xueHao2 = sharedPreferences.getString("xueHao2", "");
                map.put("stu2id", xueHao2);
                String jiaoYan2 = sharedPreferences.getString("jiaoYan2", "");
                map.put("v2code", jiaoYan2);
                i--;
            }
            if (i > 0) {
                String xueHao3 = sharedPreferences.getString("xueHao3", "");
                map.put("stu3id", xueHao3);
                String jiaoYan3 = sharedPreferences.getString("jiaoYan3", "");
                map.put("v3code", jiaoYan3);
                i--;
            }

            int Dormitory = sharedPreferences.getInt("Dormitory", 0);
            map.put("buildingNo", Dormitory);

            //连接网络
            StringBuffer sbRequest = new StringBuffer();
            if (map != null && map.size() > 0) {
                for (String key : map.keySet()) {
                    sbRequest.append(key + "=" + map.get(key) + "&");
                }
            }
            final String request = sbRequest.substring(0, sbRequest.length() - 1);
            final String ip2 = "https://api.mysspku.com/index.php/V1/MobileCourse/SelectRoom";
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL(ip2);
                                if ("https".equalsIgnoreCase(url.getProtocol())) {
                                    SslUtils.ignoreSsl();
                                }
                                connect = (HttpURLConnection) url.openConnection();
                                connect.setRequestMethod("POST");
                                //通过正文发送数据
                                OutputStream os = connect.getOutputStream();
                                os.write(request.getBytes());
                                os.flush();

                                InputStream in = connect.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                StringBuilder response = new StringBuilder();
                                String str;
                                while ((str = reader.readLine()) != null) {
                                    response.append(str);
                                }
                                String responseStr = response.toString();

                                //将结果传给主线程
                                Message msg = new Message();
                                msg.what = SUCCESS;
                                msg.obj = responseStr;
                                mHandler.sendMessage(msg);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
            ).start();

        }

    }






}
