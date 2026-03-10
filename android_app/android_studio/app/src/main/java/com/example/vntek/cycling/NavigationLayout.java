package com.example.vntek.cycling;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapMarkerItem2;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class NavigationLayout extends Fragment {

    View v;

    public EditText editTextPoi;

    Button buttonSearch;
    Button buttonReset;

    LinearLayout linearlayoutTmap;

    //T맵 관련
    TMapView tMapView;
    TMapCircle tMapCircle;


    //Google Fused Location API 관련
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    Location lastLocation;
    Location previousLocation;

    LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {

            super.onLocationResult(locationResult);

            if (locationResult.getLastLocation() != null) {

                lastLocation = locationResult.getLastLocation();

                Log.i("TAG", "getLastLocation() in SpeedLayout: Location is provided by " + lastLocation.getProvider()+".");
                Log.i("TAG", "getLastLocation() in SpeedLayout: " + lastLocation);
                Log.i("TAG", "getLastLocation() in SpeedLayout: Error range of distance in location result is up to " + lastLocation.getAccuracy() + " ｍ radially.");

                if(android.os.Build.VERSION.SDK_INT >= 26) {

                    Log.i("TAG", "getLastLocation() in SpeedLayout: Error range of speed in location result is up to " + (3.6 * lastLocation.getSpeedAccuracyMetersPerSecond()) + " ㎞ / ｈ radially.");
                    Log.i("TAG", "getLastLocation() in SpeedLayout: Error range of bearing in location result is up to " + lastLocation.getBearingAccuracyDegrees() + " degree(s).");

                }

                float distance=0;
                double speed=0;
                float bearing=0;

                if(previousLocation != null){

                    distance=previousLocation.distanceTo(lastLocation);
                    double duration=(lastLocation.getTime() - previousLocation.getTime())/1000;

                    speed=3.6*distance/duration;

                    bearing=previousLocation.bearingTo(lastLocation);

                    if(bearing < 0){

                        bearing += 360;

                    }

                }

                tMapView.setLocationPoint(lastLocation.getLongitude(),lastLocation.getLatitude());
                tMapCircle.setCenterPoint(new TMapPoint(lastLocation.getLatitude(),lastLocation.getLongitude()));
                tMapCircle.setRadius(lastLocation.getAccuracy());

                previousLocation=lastLocation;

            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.navigation_layout, container, false);

        getParameter();

        return v;
    }

    void getParameter() throws SecurityException{

        editTextPoi=v.findViewById(R.id.navigation_layout_edittext_poi);
        buttonSearch=v.findViewById(R.id.navigation_layout_button_search);
        buttonReset=v.findViewById(R.id.navigation_layout_button_resetTmapView);

        linearlayoutTmap=v.findViewById(R.id.navigation_layout_linearlayout_tmap);
        setTmap();

        fusedLocationProviderClient=new FusedLocationProviderClient(getContext());

        locationRequest = new LocationRequest().setInterval(1000).setFastestInterval(1000).setSmallestDisplacement((float)0.5).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        previousLocation=null;

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        InitializeUI();

    }

    public void setTmap(){

        tMapView=new TMapView(getContext());
        tMapView.setSKTMapApiKey( "36346d7e-03e8-4af6-b0a7-227289fcbedf" );
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setHttpsMode(true);

        tMapView.setMapType(TMapView.MAPTYPE_HYBRID);
        tMapView.setTrackingMode(true);
        tMapView.setCompassMode(true);
        tMapView.setIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.cursor_current_position));
        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(18);

        tMapCircle=new TMapCircle();
        tMapCircle.setCenterPoint(new TMapPoint(tMapView.getLatitude(),tMapView.getLongitude()));
        tMapCircle.setRadius(20);
        tMapCircle.setCircleWidth(5);
        tMapCircle.setLineColor(Color.BLUE);
        tMapCircle.setAreaColor(Color.GRAY);
        tMapCircle.setAreaAlpha(100);

        tMapView.addTMapCircle("Circle_LastLocation", tMapCircle);

        linearlayoutTmap.addView(tMapView);
    }

    void InitializeUI(){

        buttonReset.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view){

                if(lastLocation != null){

                    tMapView.setCenterPoint(lastLocation.getLongitude(),lastLocation.getLatitude());

                }

                tMapView.setZoomLevel(18);

                tMapView.setTrackingMode(true);
                tMapView.setCompassMode(true);

            }

        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                fusedLocationProviderClient.removeLocationUpdates(locationCallback);

                ((MainActivity)getActivity()).mainBundle.putString("poi_request", editTextPoi.getText().toString());

                getFragmentManager().beginTransaction().replace(R.id.main_frame, new PoiResultLayout()).addToBackStack(null).commit();

            }

        });

    }

    public void onDestroyView(){

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        super.onDestroyView();

    }

}
