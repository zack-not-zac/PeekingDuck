package peekingduckapp.peekingduck;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends Activity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://github.com/zack-not-zac/PeekingDuck/blob/master/README.md#how-to-use");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}