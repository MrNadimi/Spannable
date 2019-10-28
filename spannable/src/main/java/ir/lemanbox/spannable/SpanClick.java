package ir.lemanbox.spannable;

import android.view.View;

import ir.lemanbox.SpanType;

public interface SpanClick {

    public void run(View widget, SpanType spanType, CharSequence subject);
}
