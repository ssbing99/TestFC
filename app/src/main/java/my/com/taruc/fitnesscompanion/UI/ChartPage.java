package my.com.taruc.fitnesscompanion.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.UserLocalStore;

public class ChartPage extends ActionBarActivity {

    private ProgressDialog pDialog;
    UserLocalStore userLocalStore;

    @BindView(R.id.webViewChart)
    WebView webViewChart;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_page);
        ButterKnife.bind(this);

        pDialog = new ProgressDialog(this);

        textViewTitle.setText("Chart");

        userLocalStore = new UserLocalStore(this);
        int id = userLocalStore.returnUserID();

        if (!pDialog.isShowing()) {
            pDialog.setMessage("Loading Data...");
            pDialog.show();
        }

        webViewChart.setWebViewClient(new MyWebViewClient());

        //String url = "http://tarucfit.pe.hu/Web/ViewChart.html";
        String url = "http://tarucfit.pe.hu/ServerRequest/FetchGoalRecordChart.php?id="+id;
        webViewChart.getSettings().setJavaScriptEnabled(true);
        webViewChart.getSettings().setUseWideViewPort(true);
        webViewChart.getSettings().setLoadWithOverviewMode(true);
        webViewChart.loadUrl(url);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }
        },3000);

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
