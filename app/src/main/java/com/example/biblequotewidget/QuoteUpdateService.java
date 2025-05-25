package com.example.biblequotewidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
import java.util.Random;

public class QuoteUpdateService extends Service {
    private static final String TAG = "QuoteUpdateService";
    private static final long INTERVAL_DAY = AlarmManager.INTERVAL_DAY;
    private static final long INTERVAL_HOUR = AlarmManager.INTERVAL_HOUR;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");
        
        // Update all widgets
        updateAllWidgets();
        
        // Schedule next update with some randomization
        scheduleNextUpdate();
        
        return START_NOT_STICKY;
    }

    private void updateAllWidgets() {
        Intent updateIntent = new Intent(this, BibleQuoteWidgetProvider.class);
        updateIntent.setAction(BibleQuoteWidgetProvider.ACTION_UPDATE_QUOTE);
        sendBroadcast(updateIntent);
    }
    
    private void scheduleNextUpdate() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, QuoteUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        
        // Add some randomization (between 4-8 hours)
        Random random = new Random();
        int hoursToAdd = random.nextInt(5) + 4; // 4 to 8 hours
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, hoursToAdd);
        
        // Schedule the next update
        if (alarmManager != null) {
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
            Log.d(TAG, "Next update scheduled in " + hoursToAdd + " hours");
        }
    }

    public static void scheduleUpdates(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, QuoteUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Start immediately for first update
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 10); // Start after 10 seconds

        // Schedule the alarm
        if (alarmManager != null) {
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
            Log.d(TAG, "Updates scheduled to start immediately");
        }
    }

    public static void cancelUpdates(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, QuoteUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Log.d(TAG, "Updates canceled");
        }
    }
}
