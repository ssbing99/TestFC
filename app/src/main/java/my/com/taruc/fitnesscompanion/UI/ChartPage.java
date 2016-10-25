package my.com.taruc.fitnesscompanion.UI;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.webkit.WebView;

import butterknife.BindView;
import my.com.taruc.fitnesscompanion.R;

public class ChartPage extends ActionBarActivity {

    @BindView(R.id.webViewChart)
    WebView webViewChart;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_page);
    }
}
