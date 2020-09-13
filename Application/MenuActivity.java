package com.example.frdc_2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuActivity extends AppCompatActivity {
    private Button bt_cctv, bt_open, bt_openlist, bt_unknown, bt_emergency, bt_setting, bt_logout;
    String in_time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //main에서 전달해준 id값을 받음
        Intent intent_id = getIntent();
        final String o_id = intent_id.getStringExtra("m_id");

        bt_cctv = (Button) findViewById(R.id.bt_cctv);
        bt_cctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CctvActivity.class);
                startActivity(intent);
            }
        });

        bt_open = (Button) findViewById(R.id.bt_open);
        bt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //라즈베리파이 아아피 192.168.0.127
                MyClientTask myClientTask = new MyClientTask("172.29.118.157", 8080, o_id);
                myClientTask.execute();
                System.out.println(o_id);
            }
        });

        bt_openlist = (Button) findViewById(R.id.bt_openlist);
        bt_openlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, OpenlistActivity.class);
                startActivity(intent);
            }
        });

        bt_unknown = (Button) findViewById(R.id.bt_unknown);
        bt_unknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, UnknownlistActivity.class);
                startActivity(intent);
            }
        });

        bt_emergency = (Button) findViewById(R.id.bt_emergency);
        bt_emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bt_setting = (Button) findViewById(R.id.bt_setting);
        bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        bt_logout = (Button) findViewById(R.id.bt_logout);
        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);

                //SharedPreferences에 저장된 값들을 Main에서 만든 이름으로 불러옴
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                //editor.clear()로 auto에 들어있는 모든 값(로그인 정보)들을 삭제
                SharedPreferences.Editor editor = auto.edit();
                editor.clear();
                editor.commit();

                Toast.makeText(MenuActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        String response = "";
        String myMessage = "";

        //constructor
        MyClientTask(String addr, int port, String message) {
            dstAddress = addr;
            dstPort = port;
            myMessage = message;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Socket socket = null;
            myMessage = myMessage.toString();

            try {
                socket = new Socket(dstAddress, dstPort);

                //송신
                OutputStream out = socket.getOutputStream();
                out.write(myMessage.getBytes());

                //수신
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                int bytesRead;
                InputStream inputStream = socket.getInputStream();

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                }

                response = "서버의 응답: " + response;
                System.out.println(response);
                in_time = response;
                System.out.println(in_time);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            recieveText.setText(response);
            super.onPostExecute(result);
        }
    }
}