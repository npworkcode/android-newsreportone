package com.newsapp.npmain.newsappone;

import android.content.Context;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class URLStringBuilder
{
    private String URLFromDate;
    private String URLToDate;
    private String URLOrderBy;
    private String URLPageSize;
    private String URLAPIKey;
    private String URLSearchTerm;
    private String URLapi;
    private String URLpages;
    private String URLcontributor;

    public URLStringBuilder withAPIURL(Context context)
    {
        this.URLapi = context.getString(R.string.guardian_api_url);
        return this;
    }
    public URLStringBuilder withFromDate(Context context, Date fromDate)
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        this.URLFromDate =  context.getString(R.string.from_date) + dateFormatter.format(fromDate);
        return this;
    }

    public URLStringBuilder withToDate(Context context, Date toDate)
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.URLToDate =  context.getString(R.string.to_date) + dateFormatter.format(toDate);
        return this;
    }

    public URLStringBuilder withOrderBy(Context context, String orderBy)
    {
        this.URLOrderBy = context.getString(R.string.order_by) + orderBy;
        return this;
    }

    public URLStringBuilder withPageSize(Context context, int pageSize)
    {
        this.URLPageSize = context.getString(R.string.page_size) + Integer.toString(pageSize);
        return this;
    }
    public URLStringBuilder withSearchTerm(Context context)
    {
        this.URLSearchTerm = context.getString(R.string.query) + context.getString(R.string.query_term);
        return this;
    }

    public URLStringBuilder withAPIKey(Context context)
    {
        this.URLAPIKey = context.getString(R.string.api_key);
        return this;
    }

    public URLStringBuilder withContributor(Context context)
    {
        this.URLcontributor = context.getString(R.string.show_tag_contributor);
        return this;
    }

    public URLStringBuilder withPages(Context context, int numPages)
    {
        this.URLpages = context.getString(R.string.pages, numPages);
        return this;
    }

    public String build()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(URLapi)
                .append(URLFromDate)
                .append(URLToDate)
                .append(URLOrderBy)
                .append(URLPageSize)
                .append(URLSearchTerm)
                .append(URLcontributor)
                .append(URLpages)
                .append(URLAPIKey);
        return builder.toString();
    }


}
