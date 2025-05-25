package com.example.biblequotewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link ConfigurationActivity}
 */
public class BibleQuoteWidgetProvider extends AppWidgetProvider {

    private static final String PREFS_NAME = "com.example.biblequotewidget.WidgetPrefs";
    private static final String PREF_PREFIX_KEY = "widget_";
    private static final String PREF_THEME_KEY = "theme_";
    private static final String PREF_APPEARANCE_KEY = "appearance_";
    
    public static final String ACTION_UPDATE_QUOTE = "com.example.biblequotewidget.ACTION_UPDATE_QUOTE";
    public static final String ACTION_SAVE_QUOTE = "com.example.biblequotewidget.ACTION_SAVE_QUOTE";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        
        if (ACTION_UPDATE_QUOTE.equals(intent.getAction())) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                updateAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId);
            }
        } else if (ACTION_SAVE_QUOTE.equals(intent.getAction())) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                // Toggle save status of current quote
                toggleSaveQuote(context, appWidgetId);
                updateAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId);
            }
        }
    }

    private void toggleSaveQuote(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        String quoteKey = PREF_PREFIX_KEY + appWidgetId + "_current_quote";
        String referenceKey = PREF_PREFIX_KEY + appWidgetId + "_current_reference";
        String themeKey = PREF_PREFIX_KEY + appWidgetId + "_current_theme";
        String savedKey = PREF_PREFIX_KEY + appWidgetId + "_is_saved";
        
        boolean isSaved = prefs.getBoolean(savedKey, false);
        
        if (isSaved) {
            // Remove from saved quotes
            editor.putBoolean(savedKey, false);
        } else {
            // Add to saved quotes
            String quote = prefs.getString(quoteKey, "");
            String reference = prefs.getString(referenceKey, "");
            String theme = prefs.getString(themeKey, "");
            
            if (!quote.isEmpty() && !reference.isEmpty()) {
                // Save the current quote
                editor.putBoolean(savedKey, true);
                
                // Also add to saved quotes list
                int savedCount = prefs.getInt("saved_quotes_count", 0);
                editor.putString("saved_quote_" + savedCount, quote);
                editor.putString("saved_reference_" + savedCount, reference);
                editor.putString("saved_theme_" + savedCount, theme);
                editor.putInt("saved_quotes_count", savedCount + 1);
            }
        }
        
        editor.apply();
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        for (int appWidgetId : appWidgetIds) {
            prefs.remove(PREF_THEME_KEY + appWidgetId);
            prefs.remove(PREF_APPEARANCE_KEY + appWidgetId);
        }
        prefs.apply();
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        QuoteUpdateService.scheduleUpdates(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        QuoteUpdateService.cancelUpdates(context);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        try {
            // Get preferences
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String theme = prefs.getString(PREF_THEME_KEY + appWidgetId, "wisdom");
            String appearance = prefs.getString(PREF_APPEARANCE_KEY + appWidgetId, "light");
            
            // Create RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            
            // Set appearance based on user preference - using direct color references
            if ("dark".equals(appearance)) {
                views.setInt(R.id.widget_layout, "setBackgroundResource", R.color.black);
                views.setTextColor(R.id.quote_text, context.getResources().getColor(R.color.white));
                views.setTextColor(R.id.reference_text, context.getResources().getColor(R.color.lightGray));
                views.setTextColor(R.id.theme_text, context.getResources().getColor(R.color.lightGray));
            } else {
                views.setInt(R.id.widget_layout, "setBackgroundResource", R.color.white);
                views.setTextColor(R.id.quote_text, context.getResources().getColor(R.color.black));
                views.setTextColor(R.id.reference_text, context.getResources().getColor(R.color.darkGray));
                views.setTextColor(R.id.theme_text, context.getResources().getColor(R.color.darkGray));
            }
            
            // Check if we have a saved quote
            String quoteText = prefs.getString(PREF_PREFIX_KEY + appWidgetId + "_current_quote", "");
            String referenceText = prefs.getString(PREF_PREFIX_KEY + appWidgetId + "_current_reference", "");
            String themeText = prefs.getString(PREF_PREFIX_KEY + appWidgetId + "_current_theme", "");
            boolean isSaved = prefs.getBoolean(PREF_PREFIX_KEY + appWidgetId + "_is_saved", false);
            
            if (!quoteText.isEmpty() && !referenceText.isEmpty()) {
                // We have a cached quote, use it
                views.setTextViewText(R.id.quote_text, quoteText);
                views.setTextViewText(R.id.reference_text, referenceText);
                views.setTextViewText(R.id.theme_text, themeText.toUpperCase());
                views.setImageViewResource(R.id.save_icon, 
                        isSaved ? R.drawable.ic_saved : R.drawable.ic_not_saved);
            } else {
                // No cached quote, fetch a new one
                fetchNewQuote(context, appWidgetManager, appWidgetId, theme, views);
            }
            
            try {
                // Set up click intent for configuration
                Intent configIntent = new Intent(context, ConfigurationActivity.class);
                configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                PendingIntent configPendingIntent = PendingIntent.getActivity(context, appWidgetId, 
                        configIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                views.setOnClickPendingIntent(R.id.quote_text, configPendingIntent);
                
                // Set up long-press intent for saving
                Intent saveIntent = new Intent(context, BibleQuoteWidgetProvider.class);
                saveIntent.setAction(ACTION_SAVE_QUOTE);
                saveIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                PendingIntent savePendingIntent = PendingIntent.getBroadcast(context, appWidgetId, 
                        saveIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                views.setOnClickPendingIntent(R.id.save_icon, savePendingIntent);
            } catch (Exception e) {
                // If setting up intents fails, at least show the widget content
                views.setTextViewText(R.id.quote_text, "Tap to configure widget");
            }
            
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        } catch (Exception e) {
            // Fallback for any unexpected errors
            try {
                RemoteViews errorViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                errorViews.setTextViewText(R.id.quote_text, "Widget error: " + e.getMessage());
                errorViews.setTextViewText(R.id.reference_text, "Please reconfigure widget");
                errorViews.setTextViewText(R.id.theme_text, "ERROR");
                
                // Set up click intent for configuration as recovery option
                Intent configIntent = new Intent(context, ConfigurationActivity.class);
                configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                PendingIntent configPendingIntent = PendingIntent.getActivity(context, appWidgetId, 
                        configIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                errorViews.setOnClickPendingIntent(R.id.widget_layout, configPendingIntent);
                
                appWidgetManager.updateAppWidget(appWidgetId, errorViews);
            } catch (Exception finalError) {
                // At this point we can't do much more
            }
        }
    }

    private static void fetchNewQuote(Context context, AppWidgetManager appWidgetManager, 
                                     int appWidgetId, String theme, RemoteViews views) {
        try {
            // Set default values while loading
            views.setTextViewText(R.id.quote_text, "Loading quote...");
            views.setTextViewText(R.id.reference_text, "");
            views.setTextViewText(R.id.theme_text, theme.toUpperCase());
            
            // Update widget with loading state
            appWidgetManager.updateAppWidget(appWidgetId, views);
            
            // Get a random quote from the local database
            LocalQuoteDatabase.BibleQuote localQuote = LocalQuoteDatabase.getRandomQuote(theme);
            
            // Update widget with the local quote
            views.setTextViewText(R.id.quote_text, localQuote.getText());
            views.setTextViewText(R.id.reference_text, localQuote.getReference());
            views.setTextViewText(R.id.theme_text, localQuote.getTheme().toUpperCase());
            views.setImageViewResource(R.id.save_icon, R.drawable.ic_not_saved);
            
            // Save the quote to preferences
            SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
            prefs.putString(PREF_PREFIX_KEY + appWidgetId + "_current_quote", localQuote.getText());
            prefs.putString(PREF_PREFIX_KEY + appWidgetId + "_current_reference", localQuote.getReference());
            prefs.putString(PREF_PREFIX_KEY + appWidgetId + "_current_theme", localQuote.getTheme());
            prefs.putBoolean(PREF_PREFIX_KEY + appWidgetId + "_is_saved", false);
            prefs.apply();
            
            // Update widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            
            // Optionally try to fetch from API in background for next update
            tryFetchFromApiInBackground(context, appWidgetId, theme);
            
        } catch (Exception e) {
            // Handle any unexpected errors
            try {
                RemoteViews errorViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                errorViews.setTextViewText(R.id.quote_text, "Widget error: " + e.getMessage());
                errorViews.setTextViewText(R.id.reference_text, "Please reconfigure");
                errorViews.setTextViewText(R.id.theme_text, "ERROR");
                
                // Set up click intent for configuration as recovery option
                Intent configIntent = new Intent(context, ConfigurationActivity.class);
                configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                PendingIntent configPendingIntent = PendingIntent.getActivity(context, appWidgetId, 
                        configIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                errorViews.setOnClickPendingIntent(R.id.widget_layout, configPendingIntent);
                
                appWidgetManager.updateAppWidget(appWidgetId, errorViews);
            } catch (Exception finalError) {
                // At this point we can't do much more
            }
        }
    }
    
    /**
     * Try to fetch a quote from the API in the background for future updates
     * This doesn't affect the current widget display
     */
    private static void tryFetchFromApiInBackground(Context context, int appWidgetId, String theme) {
        // Run on a background thread
        new Thread(() -> {
            try {
                // Create quote manager and fetch quote
                BibleQuoteManager quoteManager = new BibleQuoteManager(context);
                
                quoteManager.getRandomQuoteByTheme(theme, new BibleQuoteManager.QuoteCallback() {
                    @Override
                    public void onQuoteReceived(BibleQuoteManager.BibleQuote quote) {
                        try {
                            // Save the quote to preferences for next update
                            SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
                            prefs.putString(PREF_PREFIX_KEY + appWidgetId + "_next_quote", quote.getText());
                            prefs.putString(PREF_PREFIX_KEY + appWidgetId + "_next_reference", quote.getReference());
                            prefs.putString(PREF_PREFIX_KEY + appWidgetId + "_next_theme", quote.getTheme());
                            prefs.apply();
                        } catch (Exception e) {
                            // Ignore errors, we'll use local quotes
                        }
                    }

                    @Override
                    public void onError(String error) {
                        // Ignore errors, we'll use local quotes
                    }
                });
            } catch (Exception e) {
                // Ignore errors, we'll use local quotes
            }
        }).start();
    }
    
    private static void handleFetchError(Context context, AppWidgetManager appWidgetManager, 
                                        int appWidgetId, RemoteViews views, String errorMessage) {
        // Provide a fallback quote
        String fallbackQuote = "For God so loved the world that he gave his one and only Son, that whoever believes in him shall not perish but have eternal life.";
        String fallbackReference = "John 3:16";
        
        views.setTextViewText(R.id.quote_text, fallbackQuote);
        views.setTextViewText(R.id.reference_text, fallbackReference);
        views.setTextViewText(R.id.theme_text, "OFFLINE");
        
        // Save the fallback quote
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + "_current_quote", fallbackQuote);
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + "_current_reference", fallbackReference);
        prefs.putString(PREF_PREFIX_KEY + appWidgetId + "_current_theme", "offline");
        prefs.putBoolean(PREF_PREFIX_KEY + appWidgetId + "_is_saved", false);
        prefs.apply();
        
        // Update widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
