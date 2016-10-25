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

    @BindView(R.id.webViewChart)
    WebView webViewChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_page);
        ButterKnife.bind(this);

        webViewChart.loadUrl("http://tarucfit.pe.hu/ServerRequest/ViewChart.html");
        webViewChart.setWebViewClient(new WebViewChartClient());
        WebSettings webSettings = webViewChart.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private class WebViewChartClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("http://tarucfit.pe.hu/ServerRequest/ViewChart.html")) {
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }


}
