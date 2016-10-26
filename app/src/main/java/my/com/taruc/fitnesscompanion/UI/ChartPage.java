package my.com.taruc.fitnesscompanion.UI;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.R;

public class ChartPage extends ActionBarActivity {

    private WebView webViewChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_page);

        webViewChart = (WebView) findViewById(R.id.webViewChart);

        webViewChart.setWebViewClient(new MyWebViewClient());

        String url = "http://tarucfit.pe.hu/ServerRequest/ViewChart.html";
        webViewChart.getSettings().setJavaScriptEnabled(true);
        webViewChart.getSettings().setUseWideViewPort(true);
        webViewChart.getSettings().setLoadWithOverviewMode(true);
        webViewChart.loadUrl(url);
    }

    private class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;
        }
    }



}
