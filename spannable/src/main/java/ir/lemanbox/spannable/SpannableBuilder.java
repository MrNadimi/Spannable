package ir.lemanbox.spannable;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.lemanbox.SpanType;

public class SpannableBuilder {

    private final String TAG = getClass().getName();

    private String text;
    private ArrayList<SpanObject> spans;
    /*
     * Span hai ke be surate automatic peyda mishavand hich onClick
     * baraye anha set nashode . banabarin dar inja tavasote in object
     * anha ra dar birun mitavan listen kard
     *
     * Agar in meghdar null bashad custom span surat nemigirad
     */
    private SpanClick listener;


    public static SpannableBuilder get(){
        return new SpannableBuilder();
    }

    private SpannableBuilder() {
        spans =new ArrayList<>();
    }

    public SpannableBuilder text(CharSequence text){
        if (text == null){
            this.text = "";
            return this;
        }
        this.text = text.toString();
        return this;
    }

    /*
     * kole kalame ra customSpans mikonad
     */

    public SpannableBuilder customSpans(ArrayList<SpanObject> spans) {
        if (spans == null)
            return this;
        this.spans.addAll(spans);
        return this;
    }

    /*
     * Yek kalame ra custom span mikonad
     */
    public SpannableBuilder customSpans(SpanObject span) {
        if (span == null)
            return this;
        this.spans.add(span);
        return this;
    }

    public SpannableBuilder setAutoDetectListener(SpanClick listener) {
        this.listener = listener;
        return this;
    }

    public void into(TextView textView){

        if (text == null){
            return;
        }

        /*
         * Peyda kardan custom span
         *
         * Vaghti listener null ast custom span niaz nist chon callbacki vojud nadarad
         */
        if (listener != null)
            detectLinks();

        CharSequence charSequence = text;
        StringBuilder st = new StringBuilder(text);
        //start kalmate full
        if (spans != null) {
            for (SpanObject span: spans) {
                String subject = st.substring(span.getStart() , span.getEnd());
                if (!text.contains(subject)) {
                    continue;
                }

                Spannable _span = getSpan(subject, span);
                charSequence = TextUtils.concat(charSequence.subSequence(0, span.getStart() ), _span
                        , charSequence.subSequence(  span.getEnd() , text.length()));
            }
            //end
        }
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(TextUtils.concat(charSequence), TextView.BufferType.SPANNABLE);
    }

    /**
     * "mention" , "hashtag" , "email" , "phone"
     * ra detect mikonad
     */
    private void detectLinks(){

        Matcher usernameMatcher = Pattern.compile(getUsernamePattern()).matcher(text);
        while (usernameMatcher.find()) {
            SpanObject span = SpanObject.get(usernameMatcher.start(), usernameMatcher.end(), SpanType.MENTION , listener );
            span.setColor(Color.BLUE);
            spans.add(span);
        }

        Matcher emailMatcher = Pattern.compile(getEmailPattern()).matcher(text);
        while (emailMatcher.find()) {
            SpanObject span = SpanObject.get(emailMatcher.start(), emailMatcher.end(),SpanType.EMAIL , listener );
            span.setColor(Color.BLUE);
            spans.add(span);
        }

        Matcher hashtagMatcher = Pattern.compile(getHashtagPattern()).matcher(text);
        while (hashtagMatcher.find()) {
            SpanObject span = SpanObject.get(hashtagMatcher.start(), hashtagMatcher.end(),SpanType.HASHTAG , listener );
            span.setColor(Color.BLUE);
            spans.add(span);
        }

        Matcher phoneNumberMatcher = Pattern.compile(getPhoneNumberPattern()).matcher(text);
        while (phoneNumberMatcher.find()) {
            SpanObject span = SpanObject.get(phoneNumberMatcher.start(), phoneNumberMatcher.end(),SpanType.PHONE , listener );
            span.setColor(Color.BLUE);
            spans.add(span);
        }


    }

    private String getHashtagPattern(){
        return "#([A-Za-z0-9_]+)";
    }

    private String getUsernamePattern(){
        return "@([A-Za-z0-9._]+)";
    }

    /**
     *
     * @return @see : https://stackoverflow.com/questions/42104546/java-regular-expressions-to-validate-phone-numbers
     */
    private String getPhoneNumberPattern(){
        return "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
    }

    private String getEmailPattern(){
        return "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";
    }

    public static SpanObject findWord(String word , String text){
        Matcher matcher = Pattern.compile(word).matcher(text);
        if (matcher.find()){
            return SpanObject.get(matcher.start() , matcher.end());
        }
        return null;
    }


    /**
     *
     * @param s : String or Character
     * @return agar baraye mention va hashtag daraye characterhaye sahih bashad
     */
    private boolean isAcceptableString(String s){
        if (s == null || s.length() > 30 ){
            return false;
        }
        Pattern p = Pattern.compile("^[a-zA-Z0-9._]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }


    //get customSpans string
    private Spannable getSpan(final CharSequence subject , final SpanObject spanObject){
        Spannable ordSpannable = new SpannableString(subject);
        ClickableSpan myClickableSpan = new ClickableSpan() {

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(spanObject.isUnderLine());
                ds.setFakeBoldText(spanObject.isBold());
                ds.setColor(spanObject.getColor());
            }

            @Override
            public void onClick( View widget) {
                if (spanObject.getSpanClick() != null)
                    spanObject.getSpanClick().run(widget  ,  spanObject.getSpanType() , subject );
            }


        };
        ordSpannable.setSpan(myClickableSpan,  0 , subject.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ordSpannable;
    }
}

