package gcu.mpd.mpd_coursework;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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

import gcu.mpd.mpd_coursework.fragments.CurrentIncidentsFrag;
import gcu.mpd.mpd_coursework.fragments.CurrentRoadworksFrag;
import gcu.mpd.mpd_coursework.fragments.DetailFragment;
import gcu.mpd.mpd_coursework.models.CurrentIncident;
import gcu.mpd.mpd_coursework.models.CurrentRoadwork;
import gcu.mpd.mpd_coursework.models.PlannedRoadwork;
//Name: Abel Toth
//Student No:S1828152
@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    private ArrayList<CurrentIncident> currentIncidents;
    private ArrayList<CurrentRoadwork> currentRoadworks;
    private ArrayList<PlannedRoadwork> plannedRoadworks;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMMM-dd");


    public TextView tvTitle;
    public TextView tvDetails;
    public TextView tvStart;
    public TextView tvEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTitle = findViewById(R.id.tvTitle);
        tvDetails = findViewById(R.id.tvDetails);
        tvStart = findViewById(R.id.tvStart);
        tvEnd = findViewById(R.id.tvEnd);

        //portrait mode
        if (findViewById(R.id.layout_portrait) !=null)
        {
            FragmentManager manager = this.getSupportFragmentManager();
            manager.beginTransaction()
                    .hide(manager.findFragmentById(R.id.detailFragment))
                    //.show(manager.findFragmentById((R.id.fragment_container)))
                    .commit();
            FrameLayout layout = (FrameLayout)findViewById(R.id.fragment_container);
            layout.setVisibility(View.VISIBLE);

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Info");
            alertDialog.setMessage("To view both list and details switch to landscape!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        //landscape mode
       if (findViewById(R.id.layout_landscape)!= null)
        {
            FragmentManager manager = this.getSupportFragmentManager();
            manager.beginTransaction()
                    .show(manager.findFragmentById(R.id.detailFragment))
                    //.show(manager.findFragmentById(R.id.listFragment))
                    .commit();
            FrameLayout layout = (FrameLayout)findViewById(R.id.fragment_container);
            layout.setVisibility(View.VISIBLE);
        }


        //Navbar
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new gcu.mpd.mpd_coursework.fragments.CurrentIncidentsFrag()).commit();

    }


    //bottom nav
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFrag = null;
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_current_incident:
                            selectedFrag = new gcu.mpd.mpd_coursework.fragments.CurrentIncidentsFrag();
                            break;
                        case R.id.navigation_current_roadworks:
                            selectedFrag = new gcu.mpd.mpd_coursework.fragments.CurrentRoadworksFrag();
                            break;
                        case R.id.navigation_planned_roadwork:
                            selectedFrag = new gcu.mpd.mpd_coursework.fragments.PlannedRoadworksFrag();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFrag).commit();
                    FrameLayout layout = (FrameLayout)findViewById(R.id.fragment_container);
                    layout.setVisibility(View.VISIBLE);
                    tvTitle.setText(" ");
                    tvDetails.setText(" ");
                    tvStart.setText(" ");
                    tvEnd.setText(" ");
                    return true;
                }
            };

    public void onItemSelectedCR(int index, ArrayList<CurrentRoadwork> array) {
        CurrentRoadwork r ;

        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> delayInfo = new ArrayList<>();
        ArrayList<String> start = new ArrayList<>();
        ArrayList<String> end = new ArrayList<>();


        for (int i = 0; i <array.size(); i++)
        {
            r = array.get(i);
            String t = r.getTitle();
            title.add(t);
            String d = r.getDelayInfo();
            delayInfo.add(d);
            String s = r.getStartDate().toString();
            start.add(s);
            String e = r.getEndDate().toString();
            end.add(e);
        }

        tvTitle.setText(title.get(index));
        tvDetails.setText(delayInfo.get(index));
        tvStart.setText("Start Date: "+start.get(index));
        tvEnd.setText("End Date: "+end.get(index));

        //portrait mode
        if (findViewById(R.id.layout_portrait) !=null)
        {
            FragmentManager manager = this.getSupportFragmentManager();
            manager.beginTransaction()
                    //.hide(manager.findFragmentById(R.id.listFragment))
                    .show(manager.findFragmentById((R.id.detailFragment)))
                    .addToBackStack(null)
                    .commit();
            FrameLayout layout = (FrameLayout)findViewById(R.id.fragment_container);
            layout.setVisibility(View.INVISIBLE);
        }
    }
    public void onItemSelectedCI(int index, ArrayList<CurrentIncident> array) {
        CurrentIncident incident ;
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> type = new ArrayList<>();;

        if (array.isEmpty()){
            tvTitle.setText("No current incidents to display");
        }else
        {
            for (int i = 0; i <array.size(); i++)
            {
                incident = array.get(i);
                String t = incident.getTitle();
                title.add(t);
                String d = incident.getDescription();
                type.add(d);

            }
            tvTitle.setText(title.get(index));
            tvDetails.setText(type.get(index));
        }



        //portrait mode
        if (findViewById(R.id.layout_portrait) !=null)
        {
            FragmentManager manager = this.getSupportFragmentManager();
            manager.beginTransaction()
                    //.hide(manager.findFragmentById(R.id.listFragment))
                    .show(manager.findFragmentById((R.id.detailFragment)))
                    .addToBackStack(null)
                    .commit();
            FrameLayout layout = (FrameLayout)findViewById(R.id.fragment_container);
            layout.setVisibility(View.INVISIBLE);
        }
    }
    public void onItemSelectedPR(int index, ArrayList<PlannedRoadwork> array) {
        PlannedRoadwork p ;
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> type = new ArrayList<>();
        ArrayList<String> start = new ArrayList<>();
        ArrayList<String> end = new ArrayList<>();


        for (int i = 0; i <array.size(); i++)
        {
            p = array.get(i);
            String t = p.getTitle();
            title.add(t);
            String d = p.getType();
            type.add(d);
            String s = p.getStartDate().toString();
            start.add(s);
            String e = p.getEndDate().toString();
            end.add(e);
        }
        tvTitle.setText(title.get(index));
        tvDetails.setText(type.get(index));
        tvStart.setText("Start Date: "+start.get(index));
        tvEnd.setText("End Date: "+end.get(index));

        //portrait mode
        if (findViewById(R.id.layout_portrait) !=null)
        {
            FragmentManager manager = this.getSupportFragmentManager();
            manager.beginTransaction()
                    //.hide(manager.findFragmentById(R.id.listFragment))
                    .show(manager.findFragmentById((R.id.detailFragment)))
                    .addToBackStack(null)
                    .commit();
            FrameLayout layout = (FrameLayout)findViewById(R.id.fragment_container);
            layout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FrameLayout layout = (FrameLayout)findViewById(R.id.fragment_container);
        layout.setVisibility(View.VISIBLE);
    }


}