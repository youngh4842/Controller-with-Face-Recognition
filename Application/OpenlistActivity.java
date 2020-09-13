package com.example.frdc_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class OpenlistActivity extends AppCompatActivity {

    private WebView web_openlist;
    private WebSettings websettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openlist);

        web_openlist = (WebView)findViewById(R.id.web_openlist);
        web_openlist.setWebViewClient(new WebViewClient());

        websettings = web_openlist.getSettings();
        websettings.setJavaScriptEnabled(true);
        //웹 디스플레이 조정
        //websettings.setLoadWithOverviewMode(true);
        //websettings.setUseWideViewPort(true);
        //웹 줌 허용?
        websettings.setBuiltInZoomControls(true);
        websettings.setSupportZoom(true);

        web_openlist.loadUrl("http://sormdi11.dothome.co.kr/openlist.php");
    }
}
