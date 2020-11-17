package app.mordred.diffgenerator.view;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.AppCompatTextView;

import androidx.annotation.Nullable;
import app.mordred.diffgenerator.DiffGenerator;
import app.mordred.diffgenerator.util.DiffToHtmlParameters;

import static app.mordred.diffgenerator.impl.java.wrapper.JavaDiffUtils2HtmlWrapper.CHANGE_DEL_TAG;
import static app.mordred.diffgenerator.impl.java.wrapper.JavaDiffUtils2HtmlWrapper.CHANGE_INS_TAG;
import static app.mordred.diffgenerator.impl.java.wrapper.JavaDiffUtils2HtmlWrapper.DELETION_CLOSE_TAG;
import static app.mordred.diffgenerator.impl.java.wrapper.JavaDiffUtils2HtmlWrapper.DELETION_OPEN_TAG;
import static app.mordred.diffgenerator.impl.java.wrapper.JavaDiffUtils2HtmlWrapper.DEL_TAG;
import static app.mordred.diffgenerator.impl.java.wrapper.JavaDiffUtils2HtmlWrapper.INSERTION_CLOSE_TAG;
import static app.mordred.diffgenerator.impl.java.wrapper.JavaDiffUtils2HtmlWrapper.INSERTION_OPEN_TAG;
import static app.mordred.diffgenerator.impl.java.wrapper.JavaDiffUtils2HtmlWrapper.INS_TAG;
import static app.mordred.diffgenerator.util.Constants.CHANGE_DELETION_COLOR;
import static app.mordred.diffgenerator.util.Constants.CHANGE_INSERTION_COLOR;
import static app.mordred.diffgenerator.util.Constants.DELETION_COLOR;
import static app.mordred.diffgenerator.util.Constants.INSERTION_COLOR;

public class TextDiffView extends AppCompatTextView {

    public TextDiffView(Context context) {
        super(context);
        init();
    }

    public TextDiffView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextDiffView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setTypeface(Typeface.SANS_SERIF);
        // TODO set size style gravity etc.
    }

    public void generateAndDisplayDiff(DiffToHtmlParameters parameters) {
        List<String> rawDiffs = DiffGenerator.generateRawDiff(parameters);

        List<Spannable> spansList = new ArrayList<Spannable>();

        for(String rawdiff: rawDiffs) {
            if (rawdiff.startsWith(CHANGE_DEL_TAG)) {
                String cleanRawDiff = rawdiff.replaceAll(CHANGE_DEL_TAG, "");

                Spannable span = new SpannableString(
                        cleanRawDiff.replaceAll(DELETION_OPEN_TAG, "")
                                .replaceAll(DELETION_CLOSE_TAG, ""));
                span.setSpan(new BackgroundColorSpan(DELETION_COLOR),0, span.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                while(cleanRawDiff.contains(DELETION_OPEN_TAG)
                        || cleanRawDiff.contains(DELETION_CLOSE_TAG)) {
                    int chgDelTagOffsetStart = cleanRawDiff.indexOf(DELETION_OPEN_TAG);
                    int chgDelTagOffsetEnd = cleanRawDiff.replace(DELETION_OPEN_TAG, "")
                            .indexOf(DELETION_CLOSE_TAG);
                    span.setSpan(new BackgroundColorSpan(CHANGE_DELETION_COLOR),chgDelTagOffsetStart, chgDelTagOffsetEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    cleanRawDiff = cleanRawDiff.replace(DELETION_OPEN_TAG, "").replace(DELETION_CLOSE_TAG, "");
                }
                spansList.add(span);
            } else if (rawdiff.startsWith(CHANGE_INS_TAG)) {
                String cleanRawDiff = rawdiff.replaceAll(CHANGE_INS_TAG, "");
                Spannable span = new SpannableString(
                        cleanRawDiff.replaceAll(INSERTION_OPEN_TAG, "")
                                .replaceAll(INSERTION_CLOSE_TAG, ""));
                span.setSpan(new BackgroundColorSpan(INSERTION_COLOR),0, span.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                while(cleanRawDiff.contains(INSERTION_OPEN_TAG)
                        || cleanRawDiff.contains(INSERTION_CLOSE_TAG)) {
                    int chgInsTagOffsetStart = cleanRawDiff.indexOf(INSERTION_OPEN_TAG);
                    int chgInsTagOffsetEnd = cleanRawDiff.replace(INSERTION_OPEN_TAG, "")
                            .indexOf(INSERTION_CLOSE_TAG);
                    span.setSpan(new BackgroundColorSpan(CHANGE_INSERTION_COLOR),chgInsTagOffsetStart, chgInsTagOffsetEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    cleanRawDiff = cleanRawDiff.replace(INSERTION_OPEN_TAG, "").replace(INSERTION_CLOSE_TAG, "");
                }
                spansList.add(span);
            } else if (rawdiff.startsWith(DEL_TAG)) {
                String cleanRawDiff = rawdiff.replaceAll(DEL_TAG, "");

                Spannable span = new SpannableString(cleanRawDiff);
                span.setSpan(new BackgroundColorSpan(DELETION_COLOR),0, span.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                spansList.add(span);
            } else if (rawdiff.startsWith(INS_TAG)) {
                String cleanRawDiff = rawdiff.replaceAll(INS_TAG, "");

                Spannable span = new SpannableString(cleanRawDiff);
                span.setSpan(new BackgroundColorSpan(INSERTION_COLOR),0, span.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                spansList.add(span);
            } else {
                // TODO handle equal lines
            }
        }
        Spannable[] allSpans = spansList.toArray(new Spannable[0]);

        this.setText(TextUtils.concat(allSpans));
    }
}
