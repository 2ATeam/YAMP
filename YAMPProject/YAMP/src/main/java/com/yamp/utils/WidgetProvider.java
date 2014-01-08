package com.yamp.utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.yamp.R;
import com.yamp.core.AudioManager;
import com.yamp.core.PlayerMainActivity;
import com.yamp.core.YAMPApplication;

/**
 * Created by AdYa on 09.01.14.
 */
public class WidgetProvider extends AppWidgetProvider {

    private static String PLAY_CLICK = "PLAY_CLICK";
    private static String NEXT_CLICK = "NEXT_CLICK";
    private static String PREV_CLICK = "PREV_CLICK";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(PLAY_CLICK)){
            playHandler();
        }
        else if (intent.getAction().equals(NEXT_CLICK)){
            nextHandler();
        }
        else if (intent.getAction().equals(PREV_CLICK)){
            prevHandler();
        }
        else
            super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch PlayerActivity
            Intent launchIntent = new Intent(context, PlayerMainActivity.class);
            PendingIntent launchPendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);

            // Play
            Intent playIntent = new Intent(context, WidgetProvider.class);
            playIntent.setAction(PLAY_CLICK);
            PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 0, playIntent, 0);

            // NEXT
            Intent nextIntent = new Intent(context, WidgetProvider.class);
            nextIntent.setAction(NEXT_CLICK);
            PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 0, nextIntent, 0);

            // PREV
            Intent prevIntent = new Intent(context, WidgetProvider.class);
            prevIntent.setAction(PREV_CLICK);
            PendingIntent prevPendingIntent = PendingIntent.getBroadcast(context, 0, prevIntent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.wrlWidget, launchPendingIntent);
            views.setOnClickPendingIntent(R.id.wbPlay, playPendingIntent);
            views.setOnClickPendingIntent(R.id.wbNext, nextPendingIntent);
            views.setOnClickPendingIntent(R.id.wbPrev, prevPendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        if (YAMPApplication.isPlayerReady()){

        }
    }


    private void prevHandler() {
        AudioManager.getInstance().prev();
    }

    private void nextHandler() {
        AudioManager.getInstance().next();
    }

    private void playHandler() {
        if (AudioManager.getInstance().isPlaying()){
            AudioManager.getInstance().pause();
        } else {
            AudioManager.getInstance().play();
        }
    }
}
