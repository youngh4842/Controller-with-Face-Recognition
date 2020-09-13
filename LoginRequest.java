package com.example.frdc_2;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest{

    //서버 url 설정(php파일 연동)
    final static  private String URL="http://sormdi11.dothome.co.kr/login2.php";
    private Map<String,String>map;

    public LoginRequest(String m_id, String m_pw, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);

        map=new HashMap<>();
        map.put("m_id", m_id);
        map.put("m_pw", m_pw);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}