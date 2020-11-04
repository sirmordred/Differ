package app.mordred.diffgenerator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DiffView extends WebView {
    public DiffView(@NonNull Context context) {
        super(context);
        init();
    }

    public DiffView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DiffView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DiffView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public DiffView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init();
    }

    protected void init() {
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);

        this.setSaveEnabled(true);

        final WebSettings webSettings = this.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
    }

    //TODO change produced html to fit into webviews screen width height and center it
    public void loadHtml(final String html) {
        loadHtml(html, null);
    }

    public void loadHtml(final String html, final String baseUrl) {
        loadHtml(html, baseUrl, null);
    }

    public void loadHtml(final String html, final String baseUrl, final String historyUrl) {
        loadHtml(html, baseUrl, historyUrl, "utf-8");
    }


    public void loadHtml(final String html, final String baseUrl, final String historyUrl, final String encoding) {
        loadDataWithBaseURL(baseUrl, html, "text/html", encoding, historyUrl);
    }
}
