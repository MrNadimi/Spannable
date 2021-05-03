package com.mrnadimi.spannable;


import android.graphics.Color;

/**
 * Created by Bahram on 12/30/2017.
 */

public class SpanObject {

    private SpanClick spanClick;
    private boolean underLine;
    private boolean bold;
    private int color;
    private int start , end;
    private SpanType spanType;



    public static SpanObject get(int start , int end){
        return new SpanObject(start , end , null , SpanType.CUSTOM);
    }

    public static SpanObject get(int start , int end ,SpanClick spanClick ){
        return new SpanObject(start , end , spanClick , SpanType.CUSTOM );
    }

    protected static SpanObject get(int start , int end , SpanType spanType , SpanClick spanClick ){
        return new SpanObject(start , end , spanClick , spanType );
    }



    private SpanObject( int start , int end , SpanClick spanClick , SpanType spanType) {
        this.start= start;
        this.end = end;
        this.spanClick = spanClick;
        this.spanType = spanType;
        init();
    }

    private void init(){
        this.underLine = false;
        this.bold = false;
        this.color = Color.BLACK;//default
    }

    public SpanClick getSpanClick() {
        return spanClick;
    }

    public boolean isUnderLine() {
        return underLine;
    }

    public void setUnderLine(boolean underLine) {
        this.underLine = underLine;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    /**
     *
     * @param spanClick kari ke kalameye customSpans shode pas az click bar ruye an anjam midahad
     */
    public void setSpanClick(SpanClick spanClick) {
        this.spanClick = spanClick;
    }

    public int getStart() {
        return start;
    }


    public int getEnd() {
        return end;
    }

    public SpanType getSpanType() {
        return spanType;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}

