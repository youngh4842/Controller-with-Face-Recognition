package com.example.frdc_2;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

public class CctvActivity extends AppCompatActivity {

    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctv);

        webview = (WebView)findViewById(R.id.webview);
        webview.setWebViewClient(new MyWebClient());

        WebSettings set = webview.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);
        set.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //webview.setPadding(0,0,0,0);
        //webview.getSettings().setBuiltInZoomControls(false);
        //webview.getSettings().setJavaScriptEnabled(true);
        //webview.getSettings().setLoadWithOverviewMode(true);
        //webview.getSettings().setUseWideViewPort(true);

        String url ="http://sormdi11.dothome.co.kr/stream3.php";
        webview.loadUrl(url);
    }
}

class MyWebClient extends WebViewClient{
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url); //웹뷰가 url을 받도록 함
        return true;
    }
}
