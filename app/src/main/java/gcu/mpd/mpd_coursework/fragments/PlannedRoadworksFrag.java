package gcu.mpd.mpd_coursework.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

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
import java.util.List;

import gcu.mpd.mpd_coursework.MainActivity;
import gcu.mpd.mpd_coursework.R;
import gcu.mpd.mpd_coursework.models.CurrentRoadwork;
import gcu.mpd.mpd_coursework.models.PlannedRoadwork;
//Name: Abel Toth
//Student No:S1828152
public class PlannedRoadworksFrag extends androidx.fragment.app.ListFragment {
    MainActivity activity;

    private ArrayList<PlannedRoadwork> plannedRoadworks;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMMM-dd");


    public PlannedRoadworksFrag() {
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


        new getPlannedRoadworks().execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        activity.onItemSelectedPR(position, plannedRoadworks);
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
    public class getPlannedRoadworks extends AsyncTask<Integer, Void, List<PlannedRoadwork>>
    {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            plannedRoadworks = new ArrayList<>();
            progressDialog.setMessage("Loading Planned Roadworks feed... please wait!");
            progressDialog.show();
        }

        @Override
        protected List<PlannedRoadwork> doInBackground(Integer... integers) {

            try
            {
                URL url = new URL("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx");

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
                            PlannedRoadwork plannedRoadwork = new PlannedRoadwork();
                            while (!(eventType == XmlPullParser.END_TAG && xpp.getName().toLowerCase().equals("item"))) {
                                if (eventType == XmlPullParser.START_TAG) {

                                    //String name = xpp.getName();
                                    if (xpp.getName().equalsIgnoreCase("title")) {
                                        plannedRoadwork.setTitle(xpp.nextText());
                                    } else if (xpp.getName().equalsIgnoreCase("description")) {
                                        String desc = xpp.nextText();
                                        String[] descArray = desc.split("<br />+");
                                        String type = "";
                                        if(descArray.length > 1) {

                                            String[] startArray = descArray[0].split("\\s+");
                                            String[] endArray = descArray[1].split("\\s+");
                                            String startDateString = startArray[5] +"-"+ startArray[4] +"-" + startArray[3];
                                            String endDateString = endArray[5] +"-" +endArray[4] + "-"+ endArray[3];
                                            Date startDate = formatter.parse(startDateString);
                                            Date endDate = formatter.parse(endDateString);

                                            plannedRoadwork.setStartDate(startDate);
                                            plannedRoadwork.setEndDate(endDate);

                                        }else
                                        {
                                            type = descArray[0];
                                        }
                                        if(descArray.length >2 )
                                        {
                                            type = descArray[2];

                                        }
                                        plannedRoadwork.setType(type);

                                        // currentRoadwork.setDescription(xpp.getText());
                                    } else if (xpp.getName().equalsIgnoreCase("link")) {
                                        plannedRoadwork.setLink(xpp.nextText());
                                    } else if (xpp.getName().equalsIgnoreCase("georss:point")) {
                                        plannedRoadwork.setGeorrs(xpp.nextText());
                                    } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                                        plannedRoadwork.setPubDate(xpp.nextText());
                                    }
                                    //System.out.println(currentIncident.getTitle());
                                }
                                eventType = xpp.next();
                            }
                            plannedRoadworks.add(plannedRoadwork);
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

            return plannedRoadworks;
        }

        @Override
        protected void onPostExecute(List<PlannedRoadwork> s) {
            super.onPostExecute(s);
            PlannedRoadwork index = new PlannedRoadwork();
            ArrayList<String> titleList = new ArrayList<>();
            for (int i = 0; i <s.size(); i++)
            {
                index = s.get(i);
                String ti = index.getTitle();
                titleList.add(ti);
            }
            if (getActivity()!= null)
            {
                setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, titleList));
            }
            progressDialog.dismiss();
            progressDialog.dismiss();
        }
    }
}
