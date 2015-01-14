package org.klem.android.wouf;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class WoufWidget extends AppWidgetProvider {
	private static final String UPDATE_WOUF = "org.klem.android.wouf.UPDATE_WOUF";
	static String woufs[];
	static RemoteViews views;
	@Override
	public void onEnabled(Context context) {

		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		
		views = new RemoteViews(context.getPackageName(),R.layout.main);
		String w[] = { context.getString(R.string.wouf_0),
				context.getString(R.string.wouf_1),
				context.getString(R.string.wouf_2),
				context.getString(R.string.wouf_3),
				context.getString(R.string.wouf_4),
				context.getString(R.string.wouf_5),
				context.getString(R.string.wouf_6),
				context.getString(R.string.wouf_7),
				context.getString(R.string.wouf_8),
				context.getString(R.string.wouf_9),
				context.getString(R.string.wouf_10)};
		woufs = w;
		
		 final int N = appWidgetIds.length;
		// Perform this loop procedure for each App Widget that belongs to this
        // provider
		Log.i("WoufWidget",  "Updating widgets " + Arrays.asList(appWidgetIds));
        for (int i = 0; i < N; i++) {
        	System.out.println("IDentifying widget... ");
            int appWidgetId = appWidgetIds[i];
            
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

	}

	static void updateAppWidget(Context context,AppWidgetManager appWidgetManager, int appWidgetId) {
	
		String wouf = "error";
		
		try
		{
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://127.0.0.1:8888/wouf.html");

		HttpResponse response = hc.execute(post);

		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
		{
			String str = EntityUtils.toString(response.getEntity());
			wouf = str;
		}
		}catch(IOException e){

		}

		
		

		views.setTextViewText(R.id.woufism, wouf);
		Log.i("WoufWidget",  "Loading widgets " +wouf);
		
		 // Prepare intent to launch on widget click
        Intent intent = new Intent(context,WoufWidget.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intent.setAction(UPDATE_WOUF);
        
 
        // Launch intent on widget click
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.woufism, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);

	}
	
	public void updateWouf(Context context) {
		
	}
	
	 @Override
	    public void onReceive(Context context, Intent intent) {
		 Log.i("WoufWidget",  "click received");
	        super.onReceive(context, intent);
	        
	        if(UPDATE_WOUF.equals(intent.getAction())) {
	        	AppWidgetManager instance = AppWidgetManager.getInstance(context);
	        	Random r = new Random();
	    		int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
	    		
	    		int nextWouf = r.nextInt(woufs.length);
	    		
	    		views.setTextViewText(R.id.woufism, woufs[nextWouf]);
	    		Log.i("WoufWidget",  "Updating widgets " +woufs[nextWouf]);
	    		instance.updateAppWidget(appWidgetId, views);
	        }
	       
	       
	    }

	
}
