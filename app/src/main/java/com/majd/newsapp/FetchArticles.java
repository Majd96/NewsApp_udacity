package com.majd.newsapp;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by majd on 3/21/18.
 */

public class FetchArticles {

    private final static String BASE_URL = "https://content.guardianapis.com/search";
    public static String QUERY_KEY = "q";
    public static String QUERIED_TOBIC;
    public static String QUERY_ORDER_KEY = "order-by";
    public static String QUERIED_ORDER;
    final static String PARAM_key = "api-key";
    final static String API_KEY = "test";
    final static String TAGS_KEY = "show-tags";
    final static String TAGS_VALUE = "contributor";

    //build the url

    public static URL buildUrl() {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_KEY, QUERIED_TOBIC)
                .appendQueryParameter(QUERY_ORDER_KEY, QUERIED_ORDER)
                .appendQueryParameter(TAGS_KEY, TAGS_VALUE)
                .appendQueryParameter(PARAM_key, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;

    }

    //read the JSON
    public static String getResponseFromHttpUrl() throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) buildUrl().openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }

    }

    //convert the string into ArrayList of Article
    public static ArrayList<Article> getJsonFromString(String jsonString) throws JSONException {
        String title, author, date, section, url;
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject jsonResponse = jsonObject.getJSONObject("response");
        JSONArray jsonArray = jsonResponse.getJSONArray("results");
        ArrayList<Article> articles = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject articleData = jsonArray.getJSONObject(i);
            title = articleData.getString("webTitle");

            date = articleData.getString("webPublicationDate");
            section = articleData.getString("sectionName");
            url = articleData.getString("webUrl");
            author = "";
            if (articleData.has("tags")) {

                JSONArray tagsArray = articleData.getJSONArray("tags");

                if (!tagsArray.isNull(0)) {
                    JSONObject currentStoryTags = tagsArray.getJSONObject(0);

                    if (articleData.has("webTitle")) {
                        author = currentStoryTags.getString("webTitle");
                    }
                }

                articles.add(new Article(title, section, author, date, url));


            }


        }
        return articles;
    }
}
