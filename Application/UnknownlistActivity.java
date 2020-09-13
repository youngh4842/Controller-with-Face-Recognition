package com.example.frdc_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class UnknownlistActivity extends AppCompatActivity {

    private WebView web_unknown;
    private WebSettings websettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unknownlist);

        web_unknown = (WebView)findViewById(R.id.web_unknown);
        web_unknown.setWebViewClient(new WebViewClient());

        websettings = web_unknown.getSettings();
        websettings.setJavaScriptEnabled(true);
        //웹 디스플레이 조정
        websettings.setLoadWithOverviewMode(true);
        websettings.setUseWideViewPort(true);
        //웹 줌 허용?
        websettings.setBuiltInZoomControls(true);
        websettings.setSupportZoom(true);

        web_unknown.loadUrl("http://sormdi11.dothome.co.kr//unkown_pp.php");
    }
}
