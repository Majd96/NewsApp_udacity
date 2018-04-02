package com.majd.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Article>>
        , SwipeRefreshLayout.OnRefreshListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private ListView listView;
    private ArticleAdapter articleAdapter;
    private final static String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 56;
    private TextView noResultTextView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.getDefaultSharedPreferences(this).
                registerOnSharedPreferenceChangeListener(this);

        listView = findViewById(R.id.listView);
        articleAdapter = new ArticleAdapter(this);
        listView.setAdapter(articleAdapter);
        noResultTextView = findViewById(R.id.no_result_textview);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        FetchArticles.QUERIED_TOBIC = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getResources().getString(R.string.pref1_key), getResources().getString(R.string.pref1_default_value));

        FetchArticles.QUERIED_ORDER = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getResources().getString(R.string.pref2_key), getResources().getString(R.string.pref2_default_value));
        if (isNetworkAvailable())
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        else
            Toast.makeText(this, getResources().getString(R.string.internet_message), Toast.LENGTH_LONG).show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Article article = articleAdapter.getItem(position);

                Uri dataUri = Uri.parse(article.getUrl());
                Log.d(TAG, dataUri.toString());

                if (dataUri != null) {
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, dataUri);
                    startActivity(websiteIntent);
                }
            }
        });

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    @Override
    public Loader<ArrayList<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleAsyncTaskLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Article>> loader, ArrayList<Article> data) {
        swipeRefreshLayout.setRefreshing(false);
        articleAdapter.clear();
        try {
            if (data != null && !data.isEmpty()) {
                articleAdapter.addAll(data);
                noResultTextView.setVisibility(View.GONE);
            } else if (data.isEmpty()) {
                noResultTextView.setVisibility(View.VISIBLE);
                noResultTextView.setText(getResources().getString(R.string.no_result_massesge));

            }

        } catch (NullPointerException e) {
            Log.d(TAG, e.getMessage());
        }


    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Article>> loader) {
        articleAdapter.clear();

    }

    @Override
    public void onRefresh() {
        if (isNetworkAvailable()) {
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        } else {
            Toast.makeText(this, getResources().getString(R.string.internet_message), Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).
                unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getResources().getString(R.string.pref1_key)))
            FetchArticles.QUERIED_TOBIC = sharedPreferences.getString(key, getResources().getString(R.string.pref1_default_value));
        if (key.equals(getResources().getString(R.string.pref2_key)))
            FetchArticles.QUERIED_ORDER = sharedPreferences.getString(key, getResources().getString(R.string.pref2_default_value));

    }
}
