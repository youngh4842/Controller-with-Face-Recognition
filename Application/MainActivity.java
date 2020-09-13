package com.example.frdc_2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private EditText et_id, et_pass;
    private Button bt_login,bt_register;
    private String loginId, loginPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_id=findViewById(R.id.et_id);
        et_pass=findViewById(R.id.et_pass);
        bt_login=findViewById(R.id.bt_login);
        bt_register=findViewById(R.id.bt_register);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginId = auto.getString("inputId", null);
        loginPwd = auto.getString("inputPwd", null);

        if(loginId != null && loginPwd != null) { //SharedPreferences에 저장된 값이 존재하는 경우
            Toast.makeText(MainActivity.this, loginId +"님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            intent.putExtra("m_id", loginId);
            startActivity(intent);
            finish();
        } else if(loginId == null && loginPwd == null) { //SharedPreferences에 값이 존재하지 않는 경우
            bt_register.setOnClickListener(new View.OnClickListener() {//회원가입 버튼을 클릭시 수행
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });

            bt_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String m_id = et_id.getText().toString();
                    final String m_pw = et_pass.getText().toString();

                    Response.Listener<String> responseListener=new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jasonObject=new JSONObject(response);
                                boolean success=jasonObject.getBoolean("success");
                                if (success) {//로그인 성공한 경우
                                    String id = jasonObject.getString("m_id");
                                    String pass = jasonObject.getString("m_pw");

                                    //auto의 loginId와 loginPwd에 값을 저장
                                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor autoLogin = auto.edit();
                                    autoLogin.putString("inputId", m_id);
                                    autoLogin.putString("inputPwd", m_pw);
                                    autoLogin.commit();

                                    Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                                    intent.putExtra("log", "User");
                                    intent.putExtra("m_id", m_id);
                                    startActivity(intent);
                                }
                                else{//로그인 실패한 경우
                                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(m_id, m_pw, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(loginRequest);
                }
            });
        }
    }
}
