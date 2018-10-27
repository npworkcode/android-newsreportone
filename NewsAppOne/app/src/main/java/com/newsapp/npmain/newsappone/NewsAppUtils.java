package com.newsapp.npmain.newsappone;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

final class NewsAppUtils
{
    private static final String LOG_TAG = NewsAppUtils.class.getSimpleName() + "ERROR";
    private static final String HTTP_GET = "GET";
    private static final int TIME_OUT = 15000;
    private static final String FALLBACK_STRING = "Not Available";
    private static final String EMPTY_STRING = "";

    private NewsAppUtils() {
    }

    private static URL createUrl(Context context, String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, context.getString(R.string.newsreport_error_createurl), exception);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(Context context, URL url) throws IOException
    {
        String jsonResponse = EMPTY_STRING;
        if (url == null )
        {
            return jsonResponse;
        }
        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        String method = "makeHttpRequest";

        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod(HTTP_GET);
            urlConnection.setReadTimeout(TIME_OUT /* milliseconds */);
            urlConnection.setConnectTimeout(TIME_OUT /* milliseconds */);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else
            {
                Log.e(LOG_TAG, context.getString(R.string.newsreport_error_httprequest, responseCode));
            }
        } catch( SecurityException se) {
            Log.e(LOG_TAG, context.getString(R.string.newsreport_error_securityexception, method), se);
        }
        catch (IOException e) {

            Log.e(LOG_TAG, context.getString(R.string.newsreport_error_ioexception, method), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    static ArrayList<NewsReport> extractNewsReports(Context context, String url)
    {
        URL urlConnect = createUrl(context,url);
        String jsonResponse = EMPTY_STRING;
        String method = "extractNewsReport";
        try
        {
            jsonResponse = makeHttpRequest(context, urlConnect);
            //jsonResponse = "{\"response\":{\"status\":\"ok\",\"userTier\":\"developer\",\"total\":310,\"startIndex\":1,\"pageSize\":16,\"currentPage\":1,\"pages\":20,\"orderBy\":\"newest\",\"results\":[{\"id\":\"business/live/2018/oct/25/market-sell-off-ftse-europe-asia-bear-market-ecb-mario-draghi-business-live\",\"type\":\"liveblog\",\"sectionId\":\"business\",\"sectionName\":\"Business\",\"webPublicationDate\":\"2018-10-25T20:58:50Z\",\"webTitle\":\"Dow surges by 400 points as Wall Street recovers from rout - as it happened\",\"webUrl\":\"https://www.theguardian.com/business/live/2018/oct/25/market-sell-off-ftse-europe-asia-bear-market-ecb-mario-draghi-business-live\",\"apiUrl\":\"https://content.guardianapis.com/business/live/2018/oct/25/market-sell-off-ftse-europe-asia-bear-market-ecb-mario-draghi-business-live\",\"tags\":[{\"id\":\"profile/graemewearden\",\"type\":\"contributor\",\"webTitle\":\"Graeme Wearden\",\"webUrl\":\"https://www.theguardian.com/profile/graemewearden\",\"apiUrl\":\"https://content.guardianapis.com/profile/graemewearden\",\"references\":[],\"bio\":\"<p>Graeme Wearden tracks the latest world business, economic and financial news in our daily liveblog. Previously he worked as a technology journalist at CNET Networks</p>\",\"bylineImageUrl\":\"https://static.guim.co.uk/sys-images/Guardian/Pix/contributor/2014/5/7/1399476600504/Graeme-Wearden.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2017/10/06/Graeme-Wearden,-L.png\",\"firstName\":\"wearden\",\"lastName\":\"\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"environment/2018/oct/25/shell-starts-rollout-of-ultrafast-electric-car-chargers-in-europe\",\"type\":\"article\",\"sectionId\":\"environment\",\"sectionName\":\"Environment\",\"webPublicationDate\":\"2018-10-25T16:20:30Z\",\"webTitle\":\"Shell starts rollout of ultrafast electric car chargers in Europe\",\"webUrl\":\"https://www.theguardian.com/environment/2018/oct/25/shell-starts-rollout-of-ultrafast-electric-car-chargers-in-europe\",\"apiUrl\":\"https://content.guardianapis.com/environment/2018/oct/25/shell-starts-rollout-of-ultrafast-electric-car-chargers-in-europe\",\"tags\":[{\"id\":\"profile/adam-vaughan\",\"type\":\"contributor\",\"webTitle\":\"Adam Vaughan\",\"webUrl\":\"https://www.theguardian.com/profile/adam-vaughan\",\"apiUrl\":\"https://content.guardianapis.com/profile/adam-vaughan\",\"references\":[],\"bio\":\"<p>Adam Vaughan is the Guardian's energy correspondent. He tweets at&nbsp;<a href=\"https://twitter.com/adamvaughan_uk\">@adamvaughan_uk</a>&nbsp;and you can find him on <a href=\"https://www.facebook.com/adamvaughaneditorjournalist/\">Facebook</a>.</p>\",\"bylineImageUrl\":\"https://static.guim.co.uk/sys-images/Guardian/Pix/contributor/2015/10/15/1444896484961/Adam-Vaughan.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2017/10/06/Adam-Vaughan,-L.png\",\"firstName\":\"vaughan\",\"lastName\":\"adam\",\"twitterHandle\":\"adamvaughan_uk\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"technology/2018/oct/24/tesla-elon-musk-quarter-profits-report\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2018-10-25T00:08:21Z\",\"webTitle\":\"Tesla announces high profits in quarter where Musk was embroiled in scandals\",\"webUrl\":\"https://www.theguardian.com/technology/2018/oct/24/tesla-elon-musk-quarter-profits-report\",\"apiUrl\":\"https://content.guardianapis.com/technology/2018/oct/24/tesla-elon-musk-quarter-profits-report\",\"tags\":[{\"id\":\"profile/dominic-rushe\",\"type\":\"contributor\",\"webTitle\":\"Dominic Rushe\",\"webUrl\":\"https://www.theguardian.com/profile/dominic-rushe\",\"apiUrl\":\"https://content.guardianapis.com/profile/dominic-rushe\",\"references\":[],\"bio\":\"<p>Dominic Rushe is business editor for Guardian US. He was part of the Guardian team that won the 2014 Pulitzer prize for public service journalism.&nbsp;<a href=\"https://pgp.theguardian.com/PublicKeys/Dominic%20Rushe.pub.txt\">Click here</a>&nbsp;for&nbsp;Dominic's public key. Twitter&nbsp;<a href=\"https://twitter.com/dominicru?\">@dominicru</a><br></p>\",\"bylineImageUrl\":\"https://uploads.guim.co.uk/2017/12/28/Dominic-Rushe.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2017/12/28/Dominic_Rushe,_R.png\",\"firstName\":\"Dominic\",\"lastName\":\"Rushe\",\"twitterHandle\":\"dominicru\"},{\"id\":\"profile/julia-carrie-wong\",\"type\":\"contributor\",\"webTitle\":\"Julia Carrie Wong\",\"webUrl\":\"https://www.theguardian.com/profile/julia-carrie-wong\",\"apiUrl\":\"https://content.guardianapis.com/profile/julia-carrie-wong\",\"references\":[],\"bio\":\"<p>Julia Carrie Wong is a technology reporter for Guardian US, based in San Francisco. <a href=\"https://pgp.theguardian.com/PublicKeys/Julia%20Wong.pub.txt\">Click here </a>for Julia 's public key. Twitter&nbsp;<a href=\"https://twitter.com/juliacarriew?lang=en\">@julliacarriew</a></p>\",\"bylineImageUrl\":\"https://static.guim.co.uk/sys-images/Guardian/Pix/pictures/2016/2/17/1455718053374/JuliaCarrieWong.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2017/10/06/Julia-Carrie-Wong,-L.png\",\"firstName\":\"Julia Carrie\",\"lastName\":\"Wong\",\"emailAddress\":\"julia.wong@theguardian.com\",\"twitterHandle\":\"juliacarriew\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"business/live/2018/oct/24/federal-reserve-donald-trump-powell-italy-budget-eurozone-business-live\",\"type\":\"liveblog\",\"sectionId\":\"business\",\"sectionName\":\"Business\",\"webPublicationDate\":\"2018-10-24T22:17:05Z\",\"webTitle\":\"Dow Jones falls 600 points as market volatility continues –as it happened\",\"webUrl\":\"https://www.theguardian.com/business/live/2018/oct/24/federal-reserve-donald-trump-powell-italy-budget-eurozone-business-live\",\"apiUrl\":\"https://content.guardianapis.com/business/live/2018/oct/24/federal-reserve-donald-trump-powell-italy-budget-eurozone-business-live\",\"tags\":[{\"id\":\"profile/graemewearden\",\"type\":\"contributor\",\"webTitle\":\"Graeme Wearden\",\"webUrl\":\"https://www.theguardian.com/profile/graemewearden\",\"apiUrl\":\"https://content.guardianapis.com/profile/graemewearden\",\"references\":[],\"bio\":\"<p>Graeme Wearden tracks the latest world business, economic and financial news in our daily liveblog. Previously he worked as a technology journalist at CNET Networks</p>\",\"bylineImageUrl\":\"https://static.guim.co.uk/sys-images/Guardian/Pix/contributor/2014/5/7/1399476600504/Graeme-Wearden.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2017/10/06/Graeme-Wearden,-L.png\",\"firstName\":\"wearden\",\"lastName\":\"\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"business/grogonomics/2018/oct/25/scott-morrisons-big-plan-of-fair-dinkum-power-is-a-relic-of-the-past\",\"type\":\"article\",\"sectionId\":\"business\",\"sectionName\":\"Business\",\"webPublicationDate\":\"2018-10-24T17:00:15Z\",\"webTitle\":\"Scott Morrison's big plan of 'fair dinkum power' is a relic of the past | Greg Jericho\",\"webUrl\":\"https://www.theguardian.com/business/grogonomics/2018/oct/25/scott-morrisons-big-plan-of-fair-dinkum-power-is-a-relic-of-the-past\",\"apiUrl\":\"https://content.guardianapis.com/business/grogonomics/2018/oct/25/scott-morrisons-big-plan-of-fair-dinkum-power-is-a-relic-of-the-past\",\"tags\":[{\"id\":\"profile/greg-jericho\",\"type\":\"contributor\",\"webTitle\":\"Greg Jericho\",\"webUrl\":\"https://www.theguardian.com/profile/greg-jericho\",\"apiUrl\":\"https://content.guardianapis.com/profile/greg-jericho\",\"references\":[],\"bio\":\"<p>Greg writes on economics for Guardian Australia and is the author of the celebrated Grogs Gamut blog. He is a former public servant and author of the book The Rise of the Fifth Estate: Social Media and Blogging in Australian Politics</p>\",\"bylineImageUrl\":\"https://static.guim.co.uk/sys-images/Guardian/Pix/contributor/2014/7/21/1405973562987/Greg-Jericho.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2017/10/06/Greg-Jericho,-R.png\",\"firstName\":\"jericho\",\"lastName\":\"greg\",\"twitterHandle\":\"GrogsGamut\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"technology/2018/oct/23/dyson-to-build-electric-cars-in-singapore-with-launch-planned-for-2021\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2018-10-23T09:06:01Z\",\"webTitle\":\"Dyson to build electric cars in Singapore – with 2021 launch planned\",\"webUrl\":\"https://www.theguardian.com/technology/2018/oct/23/dyson-to-build-electric-cars-in-singapore-with-launch-planned-for-2021\",\"apiUrl\":\"https://content.guardianapis.com/technology/2018/oct/23/dyson-to-build-electric-cars-in-singapore-with-launch-planned-for-2021\",\"tags\":[{\"id\":\"profile/jasper-jolly\",\"type\":\"contributor\",\"webTitle\":\"Jasper Jolly\",\"webUrl\":\"https://www.theguardian.com/profile/jasper-jolly\",\"apiUrl\":\"https://content.guardianapis.com/profile/jasper-jolly\",\"references\":[],\"bio\":\"<p>Jasper Jolly is a financial reporter for the Guardian</p>\",\"firstName\":\"Jasper\",\"lastName\":\"Jolly\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"business/2018/oct/20/saudi-arabia-business-titans-fight-shy-trillion-dollar-mohammed-bin-salman-jamal-khashoggi\",\"type\":\"article\",\"sectionId\":\"business\",\"sectionName\":\"Business\",\"webPublicationDate\":\"2018-10-20T15:00:09Z\",\"webTitle\":\"Business titans fight shy of Saudi’s trillion-dollar charm offensive\",\"webUrl\":\"https://www.theguardian.com/business/2018/oct/20/saudi-arabia-business-titans-fight-shy-trillion-dollar-mohammed-bin-salman-jamal-khashoggi\",\"apiUrl\":\"https://content.guardianapis.com/business/2018/oct/20/saudi-arabia-business-titans-fight-shy-trillion-dollar-mohammed-bin-salman-jamal-khashoggi\",\"tags\":[{\"id\":\"profile/rob-davies\",\"type\":\"contributor\",\"webTitle\":\"Rob Davies\",\"webUrl\":\"https://www.theguardian.com/profile/rob-davies\",\"apiUrl\":\"https://content.guardianapis.com/profile/rob-davies\",\"references\":[],\"bio\":\"<p>Rob Davies is a reporter on the business desk. He covers industries including gambling, tobacco, alcohol and secondary ticketing</p>\",\"bylineImageUrl\":\"https://uploads.guim.co.uk/2017/02/08/RobDavies.jpg\",\"firstName\":\"davies\",\"lastName\":\"rob\",\"twitterHandle\":\"ByRobDavies\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"environment/2018/oct/20/space-robots-lasers-rise-robot-farmer\",\"type\":\"article\",\"sectionId\":\"environment\",\"sectionName\":\"Environment\",\"webPublicationDate\":\"2018-10-20T07:00:25Z\",\"webTitle\":\"‘We'll have space bots with lasers, killing plants’: the rise of the robot farmer\",\"webUrl\":\"https://www.theguardian.com/environment/2018/oct/20/space-robots-lasers-rise-robot-farmer\",\"apiUrl\":\"https://content.guardianapis.com/environment/2018/oct/20/space-robots-lasers-rise-robot-farmer\",\"tags\":[{\"id\":\"profile/johnharris\",\"type\":\"contributor\",\"webTitle\":\"John Harris\",\"webUrl\":\"https://www.theguardian.com/profile/johnharris\",\"apiUrl\":\"https://content.guardianapis.com/profile/johnharris\",\"references\":[],\"bio\":\"<p>John Harris is a Guardian columnist, who writes on subjects including politics, popular culture and music</p>\",\"bylineImageUrl\":\"https://uploads.guim.co.uk/2018/01/11/John-Harris.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2018/01/11/John_Harris,_L.png\",\"firstName\":\"John\",\"lastName\":\"Harris\",\"twitterHandle\":\"johnharris1969\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"media/2018/oct/17/murdoch-children-may-get-up-to-2bn-each-in-21st-century-fox-sale\",\"type\":\"article\",\"sectionId\":\"media\",\"sectionName\":\"Media\",\"webPublicationDate\":\"2018-10-17T17:06:56Z\",\"webTitle\":\"Murdoch children may get up to $2bn each in 21st Century Fox sale\",\"webUrl\":\"https://www.theguardian.com/media/2018/oct/17/murdoch-children-may-get-up-to-2bn-each-in-21st-century-fox-sale\",\"apiUrl\":\"https://content.guardianapis.com/media/2018/oct/17/murdoch-children-may-get-up-to-2bn-each-in-21st-century-fox-sale\",\"tags\":[{\"id\":\"profile/marksweney\",\"type\":\"contributor\",\"webTitle\":\"Mark Sweney\",\"webUrl\":\"https://www.theguardian.com/profile/marksweney\",\"apiUrl\":\"https://content.guardianapis.com/profile/marksweney\",\"references\":[],\"bio\":\"<p>Mark Sweney is media business correspondent at the Guardian. He joined in March 2006. Previously he worked at Haymarket Publishing for six years, primarily as a news reporter, on Revolution, Campaign and Marketing weekly magazines. He is a New Zealander</p>\",\"bylineImageUrl\":\"https://static.guim.co.uk/sys-images/Guardian/Pix/pictures/2007/10/16/mark_sweney_140x140.jpg\",\"firstName\":\"sweney\",\"lastName\":\"\",\"emailAddress\":\"mark.sweney@theguardian.com\",\"twitterHandle\":\"marksweney\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"commentisfree/2018/oct/17/race-for-space-greed-nasa\",\"type\":\"article\",\"sectionId\":\"commentisfree\",\"sectionName\":\"Opinion\",\"webPublicationDate\":\"2018-10-17T05:00:33Z\",\"webTitle\":\"We once marvelled at Neil Armstrong. Now space is a playground for the rich | John Harris\",\"webUrl\":\"https://www.theguardian.com/commentisfree/2018/oct/17/race-for-space-greed-nasa\",\"apiUrl\":\"https://content.guardianapis.com/commentisfree/2018/oct/17/race-for-space-greed-nasa\",\"tags\":[{\"id\":\"profile/johnharris\",\"type\":\"contributor\",\"webTitle\":\"John Harris\",\"webUrl\":\"https://www.theguardian.com/profile/johnharris\",\"apiUrl\":\"https://content.guardianapis.com/profile/johnharris\",\"references\":[],\"bio\":\"<p>John Harris is a Guardian columnist, who writes on subjects including politics, popular culture and music</p>\",\"bylineImageUrl\":\"https://uploads.guim.co.uk/2018/01/11/John-Harris.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2018/01/11/John_Harris,_L.png\",\"firstName\":\"John\",\"lastName\":\"Harris\",\"twitterHandle\":\"johnharris1969\"}],\"isHosted\":false,\"pillarId\":\"pillar/opinion\",\"pillarName\":\"Opinion\"},{\"id\":\"technology/2018/oct/16/uber-targets-120bn-valuation-2019-flotation-report\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2018-10-16T18:41:32Z\",\"webTitle\":\"Uber targets $120bn valuation for 2019 flotation – report\",\"webUrl\":\"https://www.theguardian.com/technology/2018/oct/16/uber-targets-120bn-valuation-2019-flotation-report\",\"apiUrl\":\"https://content.guardianapis.com/technology/2018/oct/16/uber-targets-120bn-valuation-2019-flotation-report\",\"tags\":[{\"id\":\"profile/rob-davies\",\"type\":\"contributor\",\"webTitle\":\"Rob Davies\",\"webUrl\":\"https://www.theguardian.com/profile/rob-davies\",\"apiUrl\":\"https://content.guardianapis.com/profile/rob-davies\",\"references\":[],\"bio\":\"<p>Rob Davies is a reporter on the business desk. He covers industries including gambling, tobacco, alcohol and secondary ticketing</p>\",\"bylineImageUrl\":\"https://uploads.guim.co.uk/2017/02/08/RobDavies.jpg\",\"firstName\":\"davies\",\"lastName\":\"rob\",\"twitterHandle\":\"ByRobDavies\"},{\"id\":\"profile/rupertneate\",\"type\":\"contributor\",\"webTitle\":\"Rupert Neate\",\"webUrl\":\"https://www.theguardian.com/profile/rupertneate\",\"apiUrl\":\"https://content.guardianapis.com/profile/rupertneate\",\"references\":[],\"bio\":\"<p>Rupert Neate is wealth correspondent covering the super rich and inequality. He was previously US business correspondent in New York.&nbsp;He was shortlisted for reporter of the year at the 2012 British Press Awards and the British Journalism Awards for&nbsp;<a href=\"http://www.pressgazette.co.uk/story.asp?storycode=48784\">his investigation that led to Liam Fox's resignation as defence secretary</a>.<br></p><p><br></p><p><a href=\"https://pgp.theguardian.com/PublicKeys/Rupert%20Neate.pub.txt\">Click here for Rupert's Public Key</a>.</p>\",\"bylineImageUrl\":\"https://uploads.guim.co.uk/2017/10/09/Rupert_Neate.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2017/10/09/Rupert-Neate,-L.png\",\"firstName\":\"neate\",\"lastName\":\"\",\"twitterHandle\":\"RupertNeate\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"us-news/2018/oct/16/jared-kushner-trump-saudi-khashoggi-mbs\",\"type\":\"article\",\"sectionId\":\"us-news\",\"sectionName\":\"US news\",\"webPublicationDate\":\"2018-10-16T11:12:00Z\",\"webTitle\":\"A tale of two houses: how Jared Kushner fuelled the Trump-Saudi love-in\",\"webUrl\":\"https://www.theguardian.com/us-news/2018/oct/16/jared-kushner-trump-saudi-khashoggi-mbs\",\"apiUrl\":\"https://content.guardianapis.com/us-news/2018/oct/16/jared-kushner-trump-saudi-khashoggi-mbs\",\"tags\":[{\"id\":\"profile/julianborger\",\"type\":\"contributor\",\"webTitle\":\"Julian Borger\",\"webUrl\":\"https://www.theguardian.com/profile/julianborger\",\"apiUrl\":\"https://content.guardianapis.com/profile/julianborger\",\"references\":[],\"bio\":\"<p>Julian Borger is the Guardian's world affairs editor. He was previously a correspondent in the US, the Middle East, eastern Europe and the Balkans. His book on the pursuit and capture of the Balkan war criminals, <a href=\"https://bookshop.theguardian.com/catalog/product/view/id/359254/?utm_source=editoriallink&amp;utm_medium=merch&amp;utm_campaign=article\">The Butcher's Trail</a>, is published by Other Press.</p><p>• <a href=\"https://pgp.theguardian.com/PublicKeys/Julian%20Borger.pub.txt\">Julian Borger's public key</a></p>\",\"bylineImageUrl\":\"https://static.guim.co.uk/sys-images/Guardian/Pix/contributor/2016/1/8/1452247825373/Julian-Borger.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2017/10/06/Julian-Borger,-R.png\",\"firstName\":\"borger\",\"lastName\":\"\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"australia-news/2018/oct/16/australia-should-be-exporting-sunshine-not-coal-economist-jeffrey-sachs-tells-qa\",\"type\":\"article\",\"sectionId\":\"australia-news\",\"sectionName\":\"Australia news\",\"webPublicationDate\":\"2018-10-15T20:47:44Z\",\"webTitle\":\"Australia should be 'exporting sunshine, not coal', economist Jeffrey Sachs tells Q&A\",\"webUrl\":\"https://www.theguardian.com/australia-news/2018/oct/16/australia-should-be-exporting-sunshine-not-coal-economist-jeffrey-sachs-tells-qa\",\"apiUrl\":\"https://content.guardianapis.com/australia-news/2018/oct/16/australia-should-be-exporting-sunshine-not-coal-economist-jeffrey-sachs-tells-qa\",\"tags\":[{\"id\":\"profile/calla-wahlquist\",\"type\":\"contributor\",\"webTitle\":\"Calla Wahlquist\",\"webUrl\":\"https://www.theguardian.com/profile/calla-wahlquist\",\"apiUrl\":\"https://content.guardianapis.com/profile/calla-wahlquist\",\"references\":[],\"bio\":\"<p>Calla is a reporter for Guardian Australia. <a href=\"https://pgp.theguardian.com/PublicKeys/Calla%20Wahlquist.pub.txt\">Click here for Calla Wahlquist's public key.</a></p>\",\"bylineImageUrl\":\"https://static.guim.co.uk/sys-images/Guardian/Pix/pictures/2015/4/30/1430379832264/140x140_Calla_Wahlquist.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2017/10/06/Calla-Wahlquist,-L.png\",\"firstName\":\"wahlquist\",\"lastName\":\"calla\",\"twitterHandle\":\"callapilla\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"books/2018/oct/13/booker-shortlisted-authors-novels-inspiration\",\"type\":\"article\",\"sectionId\":\"books\",\"sectionName\":\"Books\",\"webPublicationDate\":\"2018-10-13T08:00:28Z\",\"webTitle\":\"How I write: Man Booker shortlist authors reveal their inspirations\",\"webUrl\":\"https://www.theguardian.com/books/2018/oct/13/booker-shortlisted-authors-novels-inspiration\",\"apiUrl\":\"https://content.guardianapis.com/books/2018/oct/13/booker-shortlisted-authors-novels-inspiration\",\"tags\":[{\"id\":\"profile/rachel-kushner\",\"type\":\"contributor\",\"webTitle\":\"Rachel Kushner\",\"webUrl\":\"https://www.theguardian.com/profile/rachel-kushner\",\"apiUrl\":\"https://content.guardianapis.com/profile/rachel-kushner\",\"references\":[],\"firstName\":\"Rachel\",\"lastName\":\"Kushner\"},{\"id\":\"profile/anna-burns\",\"type\":\"contributor\",\"webTitle\":\"Anna Burns\",\"webUrl\":\"https://www.theguardian.com/profile/anna-burns\",\"apiUrl\":\"https://content.guardianapis.com/profile/anna-burns\",\"references\":[],\"bio\":\"<p>Anna Burns is an author.</p>\",\"firstName\":\"Anna\",\"lastName\":\"Burns\"},{\"id\":\"profile/esi-edugyan\",\"type\":\"contributor\",\"webTitle\":\"Esi Edugyan\",\"webUrl\":\"https://www.theguardian.com/profile/esi-edugyan\",\"apiUrl\":\"https://content.guardianapis.com/profile/esi-edugyan\",\"references\":[],\"firstName\":\"Esi\",\"lastName\":\"Edugyan\"},{\"id\":\"profile/robin-robertson\",\"type\":\"contributor\",\"webTitle\":\"Robin Robertson\",\"webUrl\":\"https://www.theguardian.com/profile/robin-robertson\",\"apiUrl\":\"https://content.guardianapis.com/profile/robin-robertson\",\"references\":[],\"bio\":\"<p>Robin Robertson is a poet and author.</p>\",\"firstName\":\"Robin\",\"lastName\":\"Robertson\"},{\"id\":\"profile/richard-powers\",\"type\":\"contributor\",\"webTitle\":\"Richard Powers\",\"webUrl\":\"https://www.theguardian.com/profile/richard-powers\",\"apiUrl\":\"https://content.guardianapis.com/profile/richard-powers\",\"references\":[],\"bio\":\"<p>Richard Powers is an author.</p>\",\"firstName\":\"Richard\",\"lastName\":\"Powers\"},{\"id\":\"profile/daisy-johnson\",\"type\":\"contributor\",\"webTitle\":\"Daisy Johnson\",\"webUrl\":\"https://www.theguardian.com/profile/daisy-johnson\",\"apiUrl\":\"https://content.guardianapis.com/profile/daisy-johnson\",\"references\":[],\"bio\":\"<p>Daisy Johnson is an author.</p>\",\"firstName\":\"Daisy\",\"lastName\":\"Johnson\"}],\"isHosted\":false,\"pillarId\":\"pillar/arts\",\"pillarName\":\"Arts\"},{\"id\":\"technology/2018/oct/12/elon-musk-teslaquila-tesla-tequila-trademark\",\"type\":\"article\",\"sectionId\":\"technology\",\"sectionName\":\"Technology\",\"webPublicationDate\":\"2018-10-12T17:50:06Z\",\"webTitle\":\"Elon Musk says 'Teslaquila' is 'coming soon' as Tesla files trademark\",\"webUrl\":\"https://www.theguardian.com/technology/2018/oct/12/elon-musk-teslaquila-tesla-tequila-trademark\",\"apiUrl\":\"https://content.guardianapis.com/technology/2018/oct/12/elon-musk-teslaquila-tesla-tequila-trademark\",\"tags\":[{\"id\":\"profile/dominic-rushe\",\"type\":\"contributor\",\"webTitle\":\"Dominic Rushe\",\"webUrl\":\"https://www.theguardian.com/profile/dominic-rushe\",\"apiUrl\":\"https://content.guardianapis.com/profile/dominic-rushe\",\"references\":[],\"bio\":\"<p>Dominic Rushe is business editor for Guardian US. He was part of the Guardian team that won the 2014 Pulitzer prize for public service journalism.&nbsp;<a href=\"https://pgp.theguardian.com/PublicKeys/Dominic%20Rushe.pub.txt\">Click here</a>&nbsp;for&nbsp;Dominic's public key. Twitter&nbsp;<a href=\"https://twitter.com/dominicru?\">@dominicru</a><br></p>\",\"bylineImageUrl\":\"https://uploads.guim.co.uk/2017/12/28/Dominic-Rushe.jpg\",\"bylineLargeImageUrl\":\"https://uploads.guim.co.uk/2017/12/28/Dominic_Rushe,_R.png\",\"firstName\":\"Dominic\",\"lastName\":\"Rushe\",\"twitterHandle\":\"dominicru\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"},{\"id\":\"us-news/2018/oct/12/louisiana-seafood-workers-union-undocumented-abuse\",\"type\":\"article\",\"sectionId\":\"us-news\",\"sectionName\":\"US news\",\"webPublicationDate\":\"2018-10-12T10:00:27Z\",\"webTitle\":\"Louisiana's undocumented seafood workers unite to fight workplace abuses\",\"webUrl\":\"https://www.theguardian.com/us-news/2018/oct/12/louisiana-seafood-workers-union-undocumented-abuse\",\"apiUrl\":\"https://content.guardianapis.com/us-news/2018/oct/12/louisiana-seafood-workers-union-undocumented-abuse\",\"tags\":[{\"id\":\"profile/mike-elk\",\"type\":\"contributor\",\"webTitle\":\"Mike Elk\",\"webUrl\":\"https://www.theguardian.com/profile/mike-elk\",\"apiUrl\":\"https://content.guardianapis.com/profile/mike-elk\",\"references\":[],\"bio\":\"<p><em>Mike Elk is a member of the Washington-Baltimore NewsGuild. He is the co-founder of&nbsp;<a href=\"http://paydayreport.com/\">Payday Report</a>&nbsp;and was previously senior labor reporter at Politico</em></p>\",\"firstName\":\"mike\",\"lastName\":\"elk\",\"twitterHandle\":\"MikeElk\"}],\"isHosted\":false,\"pillarId\":\"pillar/news\",\"pillarName\":\"News\"}]}}";
        }
        catch (IOException e) {
            Log.e(LOG_TAG, context.getString(R.string.newsreport_error_ioexception, method), e);
        }
        if (jsonResponse.equals(""))
        {
            return null;
        }
        else
        {
            ArrayList<NewsReport> newsReports = new ArrayList<>();
            try {
                 final String RESULTS = context.getString(R.string.guardian_api_json_results);
                 final String SECTION_NAME = context.getString(R.string.guardian_api_json_section_name);
                 final String WEB_TITLE = context.getString(R.string.guardian_api_json_web_title);
                 final String WEB_URL = context.getString(R.string.guardian_api_json_web_url);
                 final String TAGS = context.getString(R.string.guardian_api_json_tags);
                 final String FIRST_NAME = context.getString(R.string.guardian_api_json_contributor_firstname);
                 final String LAST_NAME = context.getString(R.string.guardian_api_json_contributor_lastname);
                 final String WEB_PUBLICATION_DATE = context.getString(R.string.guardian_api_json_date_published);
                 final String RESPONSE = context.getString(R.string.guardian_api_json_response);
                JSONObject reader = new JSONObject(jsonResponse);
                JSONObject response = reader.getJSONObject(RESPONSE);
                JSONArray results = response.getJSONArray(RESULTS);

                for (int i = 0; i < results.length(); i++)
                {
                    JSONObject result = results.getJSONObject(i);
                    String sectionName = result.optString(SECTION_NAME,FALLBACK_STRING);
                    String webTitle = result.optString(WEB_TITLE, FALLBACK_STRING);
                    String webURL = result.optString(WEB_URL, FALLBACK_STRING);
                    String webPublicationDate = result.optString(WEB_PUBLICATION_DATE, FALLBACK_STRING);
                    JSONArray tags = result.getJSONArray(TAGS);
                    JSONObject tag = tags.getJSONObject(0);
                    String authorFirstName = tag.optString(FIRST_NAME, FALLBACK_STRING);
                    String authorLastName = tag.optString(LAST_NAME, FALLBACK_STRING);
                    NewsReport newsReport = new NewsReport();
                    newsReport.setAuthorName(authorFirstName, authorLastName,FALLBACK_STRING);
                    newsReport.setSectionName(sectionName);
                    newsReport.setTitle(webTitle);
                    newsReport.setWebUri(webURL);
                    newsReport.setDatePublished(webPublicationDate);
                    newsReports.add(newsReport);
                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, context.getString(R.string.newsreport_error_jsonexception, method), e);
            }
            return newsReports;
        }

    }
}
