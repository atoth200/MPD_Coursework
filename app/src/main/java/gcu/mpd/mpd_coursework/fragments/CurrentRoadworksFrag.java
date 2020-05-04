package gcu.mpd.mpd_coursework.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gcu.mpd.mpd_coursework.MainActivity;
import gcu.mpd.mpd_coursework.models.CurrentRoadwork;

//Name: Abel Toth
//Student No:S1828152
public class CurrentRoadworksFrag extends androidx.fragment.app.ListFragment  {
    MainActivity activity;

    private ArrayList<CurrentRoadwork> currentRoadworks;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMMM-dd");


    public CurrentRoadworksFrag() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

            new getCurrentRoadworks().execute();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        activity.onItemSelectedCR(position, currentRoadworks);
    }


    public InputStream getInputStream(URL url){
        try
        {
            return url.openConnection().getInputStream();
        }
        catch (IOException e)
        {
            return null;
        }
    }
    public class getCurrentRoadworks extends AsyncTask<Integer, Void, ArrayList<CurrentRoadwork>>
    {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            currentRoadworks = new ArrayList<>();
            progressDialog.setMessage("Loading Current Roadworks feed... please wait!");
            progressDialog.show();
        }

        @Override
        protected ArrayList<CurrentRoadwork> doInBackground(Integer... integers) {

            try
            {
                URL url = new URL("https://trafficscotland.org/rss/feeds/roadworks.aspx");

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                factory.setNamespaceAware(false);

                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(getInputStream(url), "ISO-8859-2");

                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    if(eventType == XmlPullParser.START_TAG)
                    {
                        if(xpp.getName().equalsIgnoreCase("item"))
                        {
                            CurrentRoadwork currentRoadwork = new CurrentRoadwork();
                            while (!(eventType == XmlPullParser.END_TAG && xpp.getName().toLowerCase().equals("item"))) {
                                if (eventType == XmlPullParser.START_TAG) {

                                    //String name = xpp.getName();
                                    if (xpp.getName().equalsIgnoreCase("title")) {
                                        currentRoadwork.setTitle(xpp.nextText());
                                    } else if (xpp.getName().equalsIgnoreCase("description")) {
                                        String desc = xpp.nextText();
                                        String[] descArray = desc.split("<br />+");
                                        String delayInfo = "";
                                        if(descArray.length > 1) {

                                            String[] startArray = descArray[0].split("\\s+");
                                            String[] endArray = descArray[1].split("\\s+");

                                            String startDateString = startArray[5] +"-"+ startArray[4] +"-" + startArray[3];
                                            String endDateString = endArray[5] +"-" +endArray[4] + "-"+ endArray[3];
                                            Date startDate = formatter.parse(startDateString);
                                            Date endDate = formatter.parse(endDateString);

                                            currentRoadwork.setStartDate(startDate);
                                            currentRoadwork.setEndDate(endDate);

                                        }else
                                        {
                                            delayInfo = descArray[0];
                                        }
                                        if(descArray.length >2 )
                                        {
                                            delayInfo = descArray[2];
                                            String[] delayInfoSplit = delayInfo.split("\n+");
                                            if(delayInfoSplit.length >1)
                                            {
                                                delayInfo = "";
                                                for (int i = 0; i < delayInfoSplit.length -1; i+=2)
                                                {
                                                    delayInfo = delayInfo +delayInfoSplit[i]+ " " + delayInfoSplit[i+i];
                                                }
                                                delayInfo = delayInfo.substring(0, delayInfo.length() -10);
                                            }
                                        }
                                        currentRoadwork.setDelayInfo(delayInfo);

                                        // currentRoadwork.setDescription(xpp.getText());
                                    } else if (xpp.getName().equalsIgnoreCase("link")) {
                                        currentRoadwork.setLink(xpp.nextText());
                                    } else if (xpp.getName().equalsIgnoreCase("georss:point")) {
                                        currentRoadwork.setGeorrs(xpp.nextText());
                                    } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                                        currentRoadwork.setPubDate(xpp.nextText());
                                    }
                                    //System.out.println(currentIncident.getTitle());
                                }
                                eventType = xpp.next();
                            }
                            currentRoadworks.add(currentRoadwork);
                        }
                    }
                    eventType = xpp.next();
                }
            }
            catch (MalformedURLException e)
            {
                exception = e;
            }
            catch (XmlPullParserException e)
            {
                exception = e;
            }
            catch (IOException e)
            {
                exception= e;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return currentRoadworks;
        }

        @Override
        protected void onPostExecute(ArrayList<CurrentRoadwork> s) {
            super.onPostExecute(s);

            CurrentRoadwork index = new CurrentRoadwork();
            ArrayList<String> titleList = new ArrayList<>();
            for (int i = 0; i <s.size(); i++)
            {
                index = s.get(i);
                String ti = index.getTitle();
                titleList.add(ti);
            }
            if (getActivity()!= null)
            {
                setListAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, titleList));
            }

            progressDialog.dismiss();
        }
    }
}
