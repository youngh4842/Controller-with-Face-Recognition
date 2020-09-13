package com.example.frdc_2;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    //서버 url 설정(php파일 연동)
    final static  private String URL="http://sormdi11.dothome.co.kr/register2.php";
    private Map<String,String>map;

    public RegisterRequest(String m_id, String m_pw, String m_name, String m_ph, String m_e, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);//위 url에 post방식으로 값을 전송

        map=new HashMap<>();
        map.put("m_id", m_id);
        map.put("m_pw", m_pw);
        map.put("m_name", m_name);
        map.put("m_ph", m_ph);
        map.put("m_e", m_e);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
