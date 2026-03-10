package com.example.vntek.cycling;


import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.parsers.ParserConfigurationException;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class PoiGuideLayout extends Fragment {

    //View
    View v;

    //UI 컴포넌트
    public TextView textviewSpeed;
    public TextView textviewLatitude;
    public TextView textviewLongitude;
    public TextView textviewAltitude;
    public TextView textviewBearing;
    public TextView textviewWeather;
    public TextView textviewTemperature;

    Button buttonSendLocation;
    boolean buttonStart_isPressed;

    Button buttonReset;

    LinearLayout linearlayoutTmap;

    //T맵 관련
    TMapView tMapView;
    TMapCircle tMapCircle;
    TMapPoint tMapPointPoi;
    TMapPoint tMapPointStart;
    TMapMarkerItem tMapMarker;
    TMapPolyLine tMapPolyLine;

    //서버 전송 관련
    String timestamp;
    double latitudeToSend;
    double longitudeToSend;

    //백그라운드 작업
    HandlerThread thread_sendLocationOnBackground;
    Handler handler_sendLocationOnBackground;

    HandlerThread thread_getWeatherInfoOnBackground;
    Handler handler_getWeatherInfoOnBackground;

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

                tMapPointStart=new TMapPoint(lastLocation.getLatitude(), lastLocation.getLongitude());

                ExecutorService thread_findPathData = Executors.newFixedThreadPool(1);

                Future<TMapPolyLine> result_findPathData = thread_findPathData.submit(new Callable<TMapPolyLine>() {

                    @Override
                    public TMapPolyLine call() {

                        TMapPolyLine result=null;

                        try{

                            result=new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointPoi, tMapPointStart);

                        }catch(IOException | ParserConfigurationException | SAXException e){

                            e.printStackTrace();

                        }

                        return result;

                    }

                });

                try{

                    tMapPolyLine=result_findPathData.get();

                }catch(ExecutionException | InterruptedException e){

                    e.printStackTrace();

                }

                thread_findPathData.shutdown();

                tMapPolyLine.setLineColor(Color.BLUE);
                tMapPolyLine.setLineWidth(2);
                tMapView.addTMapPolyLine("GuideLine", tMapPolyLine);

                Log.i("TAG", "getLastLocation() in PoiGuideLayout: Location is provided by " + lastLocation.getProvider()+".");
                Log.i("TAG", "getLastLocation() in PoiGuideLayout: " + lastLocation);
                Log.i("TAG", "getLastLocation() in PoiGuideLayout: Error range of distance in location result is up to " + lastLocation.getAccuracy() + " ｍ radially.");

                if(android.os.Build.VERSION.SDK_INT >= 26) {

                    Log.i("TAG", "getLastLocation() in PoiGuideLayout: Error range of speed in location result is up to " + (3.6 * lastLocation.getSpeedAccuracyMetersPerSecond()) + " ㎞ / ｈ radially.");
                    Log.i("TAG", "getLastLocation() in PoiGuideLayout: Error range of bearing in location result is up to " + lastLocation.getBearingAccuracyDegrees() + " degree(s).");

                }

                textviewLatitude.setText("위도: " + new DecimalFormat("#0.0000000").format(lastLocation.getLatitude()));
                textviewLongitude.setText("경도: " + new DecimalFormat("#0.0000000").format(lastLocation.getLongitude()));
                textviewAltitude.setText("고도: " + new DecimalFormat("#0.0").format(lastLocation.getAltitude()) + " ｍ");

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

                if(lastLocation.hasSpeed() == true){

                    if(previousLocation != null){

                        if(distance != 0){

                            textviewSpeed.setText(new DecimalFormat("#0.0000000").format(lastLocation.getSpeed() * 3.6) + " ㎞ / ｈ");

                        }
                        else{

                            textviewSpeed.setText("0.0000000 ㎞ / ｈ");

                        }

                    }
                    else{

                        textviewSpeed.setText(new DecimalFormat("#0.0000000").format(lastLocation.getSpeed() * 3.6) + " ㎞ / ｈ");

                    }

                }
                else{

                    if(previousLocation != null){

                        if(distance != 0){

                            textviewSpeed.setText(new DecimalFormat("#0.0000000").format(speed) + " ㎞ / ｈ");

                        }
                        else{

                            textviewSpeed.setText("0.0000000 ㎞ / ｈ");

                        }

                    }
                    else{

                        textviewSpeed.setText("0.0000000 ㎞ / ｈ");

                    }

                }

                if(lastLocation.hasBearing() == true){

                    textviewBearing.setText("방향: " + new DecimalFormat("#0.0000000").format(lastLocation.getBearing()));

                }
                else{

                    if(previousLocation != null){

                        textviewBearing.setText("방향: " + new DecimalFormat("#0.0000000").format(bearing));

                    }
                    else{

                        textviewBearing.setText("방향: ");

                    }

                }

                tMapView.setLocationPoint(lastLocation.getLongitude(),lastLocation.getLatitude());
                tMapCircle.setCenterPoint(new TMapPoint(lastLocation.getLatitude(),lastLocation.getLongitude()));
                tMapCircle.setRadius(lastLocation.getAccuracy());

                latitudeToSend=lastLocation.getLatitude();
                longitudeToSend=lastLocation.getLongitude();

                previousLocation=lastLocation;

            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.poi_guide_layout, container, false);

        getParameter();

        return v;

    }

    protected void getParameter() throws SecurityException{

        v.setKeepScreenOn(true);

        textviewSpeed=v.findViewById(R.id.poi_guide_layout_textview_speed);
        textviewLatitude=v.findViewById(R.id.poi_guide_layout_textview_latitude);
        textviewLongitude=v.findViewById(R.id.poi_guide_layout_textview_longitude);
        textviewAltitude=v.findViewById(R.id.poi_guide_layout_textview_altitude);
        textviewBearing=v.findViewById(R.id.poi_guide_layout_textview_bearing);

        textviewWeather=v.findViewById(R.id.poi_guide_layout_textview_weather);
        textviewTemperature=v.findViewById(R.id.poi_guide_layout_textview_temperature);

        linearlayoutTmap=v.findViewById(R.id.poi_guide_layout_linearlayout_tmap);
        setTmap();

        buttonReset=v.findViewById(R.id.poi_guide_layout_button_resetTmapView);

        buttonSendLocation=v.findViewById(R.id.poi_guide_layout_button_sendLocation);
        buttonStart_isPressed=false;
        if(((MainActivity)getActivity()).mainBundle.getString("login_session") != null)
        {

            buttonSendLocation.setVisibility(VISIBLE);

        }
        else{

            buttonSendLocation.setVisibility(INVISIBLE);

        }

        fusedLocationProviderClient=new FusedLocationProviderClient(getContext());

        locationRequest = new LocationRequest().setInterval(1000).setFastestInterval(1000).setSmallestDisplacement((float)0.5).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        previousLocation=null;

        initializeUI();

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

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

        tMapPointPoi=new TMapPoint(((MainActivity)getActivity()).mainBundle.getDouble("poi_latitude"),((MainActivity)getActivity()).mainBundle.getDouble("poi_longitude"));

        tMapMarker=new TMapMarkerItem();
        tMapMarker.setTMapPoint(tMapPointPoi);
        tMapView.addMarkerItem("destination", tMapMarker);

        linearlayoutTmap.addView(tMapView);

    }

    public void initialize_getWeatherOnBackground(){

        thread_getWeatherInfoOnBackground=new HandlerThread(" thread_getWeatherInfoOnBackground");
        thread_getWeatherInfoOnBackground.start();

        handler_getWeatherInfoOnBackground=new Handler(Looper.getMainLooper());
        handler_getWeatherInfoOnBackground.postDelayed(new Runnable() {

            @Override
            public void run() {

                if(lastLocation != null){

                    RequestQueue postQueue = Volley.newRequestQueue(getActivity());

                    String address=null;

                    ExecutorService thread_convertGpsToAddress = Executors.newFixedThreadPool(1);

                    Future<String> result_convertGpsToAddress = thread_convertGpsToAddress.submit(new Callable<String>() {

                        @Override
                        public String call() {

                            String result=null;

                            try{

                                result = new TMapData().convertGpsToAddress(lastLocation.getLatitude(), lastLocation.getLongitude());

                            }catch(IOException | ParserConfigurationException | SAXException e){

                                e.printStackTrace();

                            }

                            return result;

                        }

                    });

                    try{

                        address=result_convertGpsToAddress.get();

                    }catch(ExecutionException | InterruptedException e){

                        e.printStackTrace();

                    }

                    thread_convertGpsToAddress.shutdown();

                    String[] splitAddress=address.split(" ");

                    String city=null;

                    if(splitAddress[0].endsWith("특별시")){

                        city=splitAddress[0].substring(0 , splitAddress[0].length()-3);

                    }
                    else if(splitAddress[0].endsWith("광역시")){

                        city=splitAddress[0].substring(0 , splitAddress[0].length()-3);

                    }
                    else if(splitAddress[0].endsWith("특별자치시")){

                        city=splitAddress[0].substring(0 , splitAddress[0].length()-5);

                    }
                    else if(splitAddress[0].endsWith("시")){

                        city=splitAddress[0].substring(0 , splitAddress[0].length()-1);

                    }
                    else if(splitAddress[0].endsWith("군")){

                        city=splitAddress[0].substring(0 , splitAddress[0].length()-1);

                    }
                    else if(splitAddress[0].endsWith("도")){

                        city=splitAddress[0].substring(0 , splitAddress[0].length()-1);

                    }
                    else if(splitAddress[0].endsWith("특별자치도")){

                        city=splitAddress[0].substring(0 , splitAddress[0].length()-5);

                    }

                    String county=splitAddress[1];
                    String village=splitAddress[2];

                    String params="?appKey=36346d7e-03e8-4af6-b0a7-227289fcbedf&version=2&city=" + city + "&county=" + county + "&village=" + village;

                    Log.i("TAG", "Params: " + params);

                    StringRequest postRequest = new StringRequest(Request.Method.GET, "https://apis.openapi.sk.com/weather/current/minutely" + params,
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {

                                    String weather=null;
                                    String temperature=null;

                                    JSONObject json=null;

                                    try{

                                        json=new JSONObject(response);

                                        weather=json.getJSONObject("weather").getJSONArray("minutely").getJSONObject(0).getJSONObject("sky").getString("name");
                                        temperature=json.getJSONObject("weather").getJSONArray("minutely").getJSONObject(0).getJSONObject("temperature").getString("tc");



                                    }catch(JSONException e){

                                        e.printStackTrace();

                                    }

                                    Log.i("TAG", "Weather: " + weather + ", Temperature: " + temperature);

                                    //Runnable 익명클래스를 위한 상수
                                    final String _weather=weather;
                                    final String _temperature=temperature;

                                    //외부 스레드에서 UI 제어가 불가능하므로 UI 스레드를 호출한다.
                                    getActivity().runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {

                                            textviewWeather.setText("날씨: " + _weather);
                                            textviewTemperature.setText("기온: " + _temperature);

                                        }

                                    });

                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            textviewWeather.setText("날씨: [error " + error.networkResponse.statusCode + "]");
                            textviewTemperature.setText("기온: [error " + error.networkResponse.statusCode + "]");

                        }

                    });

                    postQueue.add(postRequest);

                }

                handler_getWeatherInfoOnBackground.postDelayed(this, 3600000);

            }

        }, 2500);

    }

    public void initialize_sendLocationOnBackground(){

        thread_sendLocationOnBackground=new HandlerThread("thread_sendLocationOnBackground");
        thread_sendLocationOnBackground.start();

        handler_sendLocationOnBackground=new Handler(thread_sendLocationOnBackground.getLooper());
        handler_sendLocationOnBackground.postDelayed(new Runnable() {

            @Override
            public void run() {

                if(lastLocation != null) {

                    RequestQueue postQueue = Volley.newRequestQueue(getActivity());

                    StringRequest postRequest = new StringRequest(Request.Method.POST, "https://android-api.cyclingsupporter.cf/SendLocation",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }

                    }) {

                        @Override
                        protected Map<String, String> getParams() {

                            Map<String, String> params = new HashMap<>();

                            params.put("session", ((MainActivity) getActivity()).mainBundle.getString("login_session"));
                            params.put("name", timestamp);

                            params.put("lat", Double.toString(latitudeToSend));
                            params.put("long", Double.toString(longitudeToSend));

                            return params;

                        }

                    };

                    postQueue.add(postRequest);

                    handler_sendLocationOnBackground.postDelayed(this, 5000);
                }
            }

        }, 5000);

    }

    public void destroy_getWeatherOnBackground(){

        if(handler_getWeatherInfoOnBackground != null){

            handler_getWeatherInfoOnBackground.removeCallbacksAndMessages(null);

        }

        if(thread_getWeatherInfoOnBackground != null){

            thread_getWeatherInfoOnBackground.quit();

        }

    }

    public void destroy_sendLocationOnBackground(){

        if(handler_sendLocationOnBackground != null){

            handler_sendLocationOnBackground.removeCallbacksAndMessages(null);

        }

        if(thread_sendLocationOnBackground != null){

            thread_sendLocationOnBackground.quit();

        }

    }

    public void initializeUI(){

        initialize_getWeatherOnBackground();

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

        buttonSendLocation.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(buttonStart_isPressed == false){

                    TimeZone tz=TimeZone.getTimeZone("Asia/Seoul");
                    DateFormat df=new SimpleDateFormat("yyyyMMddHHmmssSSS");
                    df.setTimeZone(tz);
                    timestamp=df.format(new Date());

                    initialize_sendLocationOnBackground();

                    buttonSendLocation.setText("중지");

                    buttonStart_isPressed = true;

                }
                else{

                    destroy_sendLocationOnBackground();

                    buttonSendLocation.setText("주행기록 전송");

                    buttonStart_isPressed = false;

                }

            }

        });

    }

    @Override
    public void onDestroyView() {

        destroy_sendLocationOnBackground();

        destroy_getWeatherOnBackground();

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

        v.setKeepScreenOn(false);

        super.onDestroyView();

    }

}
