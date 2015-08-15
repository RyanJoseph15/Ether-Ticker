package com.minisoftwareandgames.ryan.etherticker.objects;

/**
 * Created by ryan on 8/14/15.
 */
public class Widget {

	public int id;
	public String exchange;
	public String currency;

	public Widget(int id, String exchange, String currency) {
		this.id = id;
		this.exchange = exchange;
		this.currency = currency;
	}

}
