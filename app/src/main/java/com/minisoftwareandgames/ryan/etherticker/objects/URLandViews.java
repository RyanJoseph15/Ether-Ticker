package com.minisoftwareandgames.ryan.etherticker.objects;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.widget.RemoteViews;

/**
 * Created by ryan on 8/14/15.
 */
public class URLandViews {
	/* this class was created to pass in the views that need to be updated with the
	 * information that is returned in the async task */
	public String URL;
	public AppWidgetManager manager;
	public int widgetId;
	public RemoteViews views;
	public Context context;
	public URLandViews(String URL, AppWidgetManager manager, int widgetId, RemoteViews views, Context context) {
		this.URL = URL;
		this.manager = manager;
		this.widgetId = widgetId;
		this.views = views;
		this.context = context;
	}
}
