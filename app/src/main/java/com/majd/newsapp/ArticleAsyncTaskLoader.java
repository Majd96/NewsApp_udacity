package com.majd.newsapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by majd on 3/22/18.
 */

public class ArticleAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Article>> {

    public ArticleAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Article> loadInBackground() {
        try {
            String jsonString = FetchArticles.getResponseFromHttpUrl();
            return FetchArticles.getJsonFromString(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
