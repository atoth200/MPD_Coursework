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
import gcu.mpd.mpd_coursework.models.CurrentIncident;
import gcu.mpd.mpd_coursework.models.CurrentRoadwork;

//Name: Abel Toth
//Student No:S1828152
public class CurrentIncidentsFrag extends androidx.fragment.app.ListFragment {
    MainActivity activity;

    private ArrayList<CurrentIncident> currentIncidents;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMMM-dd");


    public CurrentIncidentsFrag() {
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

        new getCurrentIncidents().execute();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        activity.onItemSelectedCI(position, currentIncidents);
    }


    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    public class getCurrentIncidents extends AsyncTask<Integer, Void, List<CurrentIncident>> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            currentIncidents = new ArrayList<>();
            progressDialog.setMessage("Loading Current incidents feed... please wait!");
            progressDialog.show();
        }

        @Override
        protected List<CurrentIncident> doInBackground(Integer... integers) {

            try {
                //Current Incidents URL
                URL url = new URL("https://trafficscotland.org/rss/feeds/currentincidents.aspx");

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                factory.setNamespaceAware(false);

                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(getInputStream(url), "ISO-8859-2");


                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            CurrentIncident currentIncident = new CurrentIncident();
                            while (!(eventType == XmlPullParser.END_TAG && xpp.getName().toLowerCase().equals("item"))) {
                                if (eventType == XmlPullParser.START_TAG) {

                                    //String name = xpp.getName();
                                    if (xpp.getName().equalsIgnoreCase("title")) {
                                        currentIncident.setTitle(xpp.nextText());
                                    } else if (xpp.getName().equalsIgnoreCase("description")) {
                                        currentIncident.setDescription(xpp.nextText());
                                    } else if (xpp.getName().equalsIgnoreCase("link")) {
                                        currentIncident.setLink(xpp.nextText());
                                    } else if (xpp.getName().equalsIgnoreCase("georss:point")) {
                                        currentIncident.setGeorrs(xpp.nextText());
                                    } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                                        currentIncident.setPubDate(xpp.nextText());
                                    }
                                    //System.out.println(currentIncident.getTitle());
                                }
                                eventType = xpp.next();
                            }
                            currentIncidents.add(currentIncident);
                        }
                    }
                    eventType = xpp.next();
                }
            } catch (MalformedURLException e) {
                exception = e;
            } catch (XmlPullParserException e) {
                exception = e;
            } catch (IOException e) {
                exception = e;
            }

            return currentIncidents;
        }

        @Override
        protected void onPostExecute(List<CurrentIncident> s) {
            super.onPostExecute(s);
            CurrentIncident index = new CurrentIncident();
            ArrayList<String> titleList = new ArrayList<>();
            for (int i = 0; i < s.size(); i++) {
                index = s.get(i);
                String ti = index.getTitle();
                titleList.add(ti);
            }
            ArrayList<String> empty = new ArrayList<>();
            empty.add("Current Incidents is empty");
            if(titleList.isEmpty())
            {
                setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,empty ));
            }
            else
            {
                setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, titleList));
            }


            progressDialog.dismiss();
        }
    }
}

