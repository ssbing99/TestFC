package my.com.taruc.fitnesscompanion.UI;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.R;

public class ChartPage extends ActionBarActivity {

    @BindView(R.id.webViewChart)
    WebView webViewChart;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_page);
        ButterKnife.bind(this);

        textViewTitle.setText("Chart");

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


    public void BackAction(View view) {
        this.finish();
    }

}
