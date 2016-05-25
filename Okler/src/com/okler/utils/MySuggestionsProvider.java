package com.okler.utils;

import android.content.SearchRecentSuggestionsProvider;

public class MySuggestionsProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.okler.utils.MySuggestionsProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public MySuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
