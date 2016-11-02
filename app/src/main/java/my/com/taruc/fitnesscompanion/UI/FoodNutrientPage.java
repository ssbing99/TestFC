package my.com.taruc.fitnesscompanion.UI;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.net.ConnectivityManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.R;

public class FoodNutrientPage extends ActionBarActivity {

    @BindView(R.id.webViewFood)
    WebView webViewFood;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_nutrient_page);
        ButterKnife.bind(this);

        textViewTitle.setText("Food Nutrient");

        webViewFood.getSettings().setJavaScriptEnabled(true);
        webViewFood.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:(function(){" +
                        //"document.getElementsByTagName('div')[0].style.display='block'; "+
                        "document.getElementsByTagName('div')[0].style.display='none'; "+
                        "document.getElementsByTagName('div')[6].style.display='none'; "+
                        "})()");
            }
        });
        webViewFood.loadUrl("http://www.tarucfit.pe.hu/Web/FoodCategory.php");

    }

    public void BackAction(View view) {
        this.finish();
    }
}
