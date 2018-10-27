package com.newsapp.npmain.newsappone;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewsAppActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsReport>>
{
    private static final int PAGE_SIZE = 16;
    private static final int NUM_PAGES = 1;
    private static final int NEWSREPORT_LOADER_ID = 1;
    private TextView emptyTextView;
    private ProgressBar newsReportProgressBar;
    private ListView newsReportsListView;
    private NewsReportAdapter newsReportAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_app);
        final Context context = getApplicationContext();
        newsReportsListView = findViewById(R.id.list);
        emptyTextView = findViewById(R.id.empty_view);
        newsReportProgressBar = findViewById(R.id.loading_spinner);
        if (isConnected())
        {
            newsReportAdapter = new NewsReportAdapter(this, new ArrayList<NewsReport>());
            newsReportsListView.setAdapter(newsReportAdapter);
            newsReportsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
                {
                    NewsReport currentNewsReport = newsReportAdapter.getItem(position);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(currentNewsReport.getWebUri())));
                }
            });
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWSREPORT_LOADER_ID, null, this);
        }
        else
        {
            newsReportProgressBar.setVisibility(View.GONE);
            emptyTextView.setText(context.getString(R.string.no_network));
        }

    }

    private boolean isConnected()
    {
        boolean isNetworkUp = true;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE))
        {
            isNetworkUp = false;
        }
        return isNetworkUp;
    }

    @Override
    public Loader<List<NewsReport>> onCreateLoader(int id, Bundle args)
    {
        final Context context = getApplicationContext();
        URLStringBuilder builder = new URLStringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date beginDate = calendar.getTime();
        String url = builder.withAPIURL(context)
                .withFromDate(context,beginDate)
                .withToDate(context, new Date())
                .withOrderBy(context, context.getString(R.string.order_by_newest))
                .withPageSize(context, PAGE_SIZE)
                .withSearchTerm(context)
                .withContributor(context)
                .withPages(context,NUM_PAGES)
                .withAPIKey(context)
                .build();
        return new NewsReportLoader(context, url);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsReport>> loader, List<NewsReport> data)
    {
        final Context context = getApplicationContext();
        newsReportProgressBar.setVisibility(View.GONE);
        newsReportAdapter.clear();
        if ( data != null && !data.isEmpty())
        {
            newsReportAdapter.addAll(data);
        }
        else if (!isConnected())
        {
            emptyTextView.setText(context.getString(R.string.no_network));
        }
        else
        {
            emptyTextView.setText(context.getString(R.string.no_articles));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsReport>> loader)
    {
        newsReportAdapter.clear();
    }
}
