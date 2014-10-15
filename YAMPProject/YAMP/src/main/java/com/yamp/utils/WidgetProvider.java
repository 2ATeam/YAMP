package com.yamp.utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;

import com.yamp.R;
import com.yamp.core.AudioManager;
import com.yamp.core.PlayerMainActivity;
import com.yamp.core.YAMPApplication;
import com.yamp.events.PlaybackListener;
import com.yamp.events.SoundControllerBoundedListener;
import com.yamp.events.TrackLoadedListener;
import com.yamp.library.AlbumArtLoader;
import com.yamp.library.AudioFile;
import com.yamp.sound.SoundController;

/**
 * Created by AdYa on 09.01.14.
 */
public class WidgetProvider extends AppWidgetProvider {

    private static String PLAY_CLICK = "YAMP_PLAY_CLICK";
    private static String NEXT_CLICK = "YAMP_NEXT_CLICK";
    private static String PREV_CLICK = "YAMP_PREV_CLICK";

    private static void forceUpdate() {
        Intent intent = new Intent(YAMPApplication.getInstance(),WidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(YAMPApplication.getInstance()).getAppWidgetIds(new ComponentName(YAMPApplication.getInstance(), WidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        YAMPApplication.getInstance().sendBroadcast(intent);
    }


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
    public void onUpdate(Context unusedContext, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Context context = YAMPApplication.getInstance();
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        final int N = appWidgetIds.length;
        Log.i("WIDGET","UPDATED");
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < N; i++) {
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
            views.setOnClickPendingIntent(R.id.wivCover, launchPendingIntent);
            views.setOnClickPendingIntent(R.id.wbPlay, playPendingIntent);
            views.setOnClickPendingIntent(R.id.wbNext, nextPendingIntent);
            views.setOnClickPendingIntent(R.id.wbPrev, prevPendingIntent);

            if (YAMPApplication.isPlayerReady()){
                if (AudioManager.getInstance().isPlaying())
                    views.setImageViewResource(R.id.wbPlay, R.drawable.button_pause);
                else
                    views.setImageViewResource(R.id.wbPlay, R.drawable.button_play);
                views.setImageViewBitmap(R.id.wivCover, AlbumArtLoader.getArtwork(YAMPApplication.getInstance(), AudioManager.getInstance().getCurrent().getAlbumID()));
                views.setTextViewText(R.id.wtvTitle, AudioManager.getInstance().getCurrent().getName());
            }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        views.setImageViewResource(R.id.wivCover, R.drawable.logo);
        views.setTextViewText(R.id.wtvTitle, "Tap logo to run YAMP");

        if (YAMPApplication.isPlayerReady()){
            if (AudioManager.getInstance().isPlaying())
                views.setImageViewResource(R.id.wbPlay, R.drawable.button_pause);
            else
                views.setImageViewResource(R.id.wbPlay, R.drawable.button_play);
            init();
        }
        else{
            views.setImageViewResource(R.id.wbPlay, R.drawable.button_play);

        YAMPApplication.setOnSoundControllerBoundedListener(new SoundControllerBoundedListener() {
            @Override
            public void onSoundControllerBounded(SoundController controller) {
               init();
            }
        });
        }

        AudioManager.getInstance().setTrackLoadedListener(new TrackLoadedListener() {
            @Override
            public void onNewTrackLoaded(AudioFile track) {
                WidgetProvider.forceUpdate();
            }

            @Override
            public void onNextTrackLoaded(AudioFile nextTrack) {

            }

            @Override
            public void onPrevTrackLoaded(AudioFile prevTrack) {

            }
        });
    }

private void init(){
    AudioManager.getInstance().setPlaybackListener(new PlaybackListener() {
        @Override
        public void onPlayingStarted(boolean causedByUser) {
            forceUpdate();
        }

        @Override
        public void onPlayingPaused(int currentProgress) {
            forceUpdate();
        }

        @Override
        public void onPlayingResumed(int currentProgress) {
            forceUpdate();
        }

        @Override
        public void onPlayingCompleted(boolean causedByUser) {
            forceUpdate();
        }
    });
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
        forceUpdate();
    }

}
