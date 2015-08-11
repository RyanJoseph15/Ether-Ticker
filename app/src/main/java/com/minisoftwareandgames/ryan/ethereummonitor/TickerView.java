package com.minisoftwareandgames.ryan.ethereummonitor;

import android.widget.TextView;

/**
 * Created by ryan on 8/11/15.
 */
public class TickerView {

    private TextView low;
    private TextView high;
    private TextView price;
    private TextView exchange;

    public TickerView(){}
    public static TickerView newInstance(TextView low, TextView high, TextView price, TextView exchange) {
        TickerView tickerView = new TickerView();
        tickerView.low = low;
        tickerView.high = high;
        tickerView.price = price;
        tickerView.exchange = exchange;
        return tickerView;
    }

    /* getters and setters */
    public TextView getLow() {return this.low;}
    public TextView getHigh() {return this.high;}
    public TextView getPrice() {return this.price;}
    public TextView getExchange() {return this.exchange;}

}
