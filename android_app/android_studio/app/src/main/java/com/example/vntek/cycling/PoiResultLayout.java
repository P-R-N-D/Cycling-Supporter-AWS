package com.example.vntek.cycling;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.parsers.ParserConfigurationException;

public class PoiResultLayout extends Fragment {

    View v;

    TextView textviewLayoutTitle;
    Spinner spinnerList;
    Button buttonStart;

    TMapView tMapView;
    TMapData tMapData;
    TMapPoint tMapPoint;

    public ArrayList<TMapPOIItem> poiList;
    public ArrayList<String> spinnerStringList;

    LinearLayout linearlayoutTmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        v=inflater.inflate(R.layout.poi_result_layout, container, false);

        getParameter();

        return v;

    }

    void getParameter(){

        textviewLayoutTitle=v.findViewById(R.id.poi_result_layout_textview_layouttitle);
        spinnerList=v.findViewById(R.id.poi_result_layout_spinner_list);
        buttonStart=v.findViewById(R.id.poi_result_layout_button_start);

        linearlayoutTmap=v.findViewById(R.id.poi_result_layout_linearlayout_tmap);
        setTmap();

        spinnerStringList=new ArrayList<String>();

        if(poiList == null || poiList.isEmpty() == true){

            textviewLayoutTitle.setText("검색된 장소가 없어요.");
            spinnerStringList.add("검색된 장소가 없어요.");

            spinnerList.setVisibility(View.INVISIBLE);
            buttonStart.setVisibility(View.GONE);

        }
        else{

            tMapView.setCenterPoint(poiList.get(0).getPOIPoint().getLongitude(), poiList.get(0).getPOIPoint().getLatitude());

            tMapPoint=new TMapPoint(poiList.get(0).getPOIPoint().getLongitude(), poiList.get(0).getPOIPoint().getLatitude());

            for(int i=0; i < poiList.size(); i++){

                spinnerStringList.add("[" + (i+1) + "] " + poiList.get(i).getPOIName() + ", " + poiList.get(i).getPOIAddress().replace("null", ""));

            }

            spinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    tMapView.setLocationPoint(poiList.get(position).getPOIPoint().getLongitude(), poiList.get(position).getPOIPoint().getLatitude());
                    tMapView.setCenterPoint(poiList.get(position).getPOIPoint().getLongitude(), poiList.get(position).getPOIPoint().getLatitude());

                    tMapPoint.setLongitude(poiList.get(position).getPOIPoint().getLongitude());
                    tMapPoint.setLatitude(poiList.get(position).getPOIPoint().getLatitude());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {



                }

            });

        }

        spinnerList.setAdapter(new ArrayAdapter<String> (getActivity(), R.layout.poi_result_layout_spinner_list, spinnerStringList));

        spinnerList.setSelection(0);

        buttonStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).mainBundle.putDouble("poi_latitude", tMapPoint.getLatitude());
                ((MainActivity)getActivity()).mainBundle.putDouble("poi_longitude", tMapPoint.getLongitude());

                getFragmentManager().beginTransaction().replace(R.id.main_frame, new PoiGuideLayout()).addToBackStack(null).commit();

            }

        });

    }

    public void setTmap() {

        tMapView=new TMapView(getContext());
        tMapView.setSKTMapApiKey( "36346d7e-03e8-4af6-b0a7-227289fcbedf" );
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setHttpsMode(true);

        tMapView.setMapType(TMapView.MAPTYPE_HYBRID);
        tMapView.setCompassMode(true);
        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(19);

        linearlayoutTmap.addView(tMapView);

        tMapData=new TMapData();

        ExecutorService thread_findAllPoi = Executors.newFixedThreadPool(1);

        Future<ArrayList<TMapPOIItem>> result_findAllPoi = thread_findAllPoi.submit(new Callable<ArrayList<TMapPOIItem>>() {

            @Override
            public ArrayList<TMapPOIItem> call() {

                ArrayList<TMapPOIItem> result=null;

                try{

                    result = tMapData.findAllPOI(((MainActivity) getActivity()).mainBundle.getString("poi_request"));

                }catch(IOException | ParserConfigurationException | SAXException e){

                    e.printStackTrace();

                }

                return result;

            }

        });

        try{

            poiList=result_findAllPoi.get();

        }catch(ExecutionException | InterruptedException e){

            e.printStackTrace();

        }

        thread_findAllPoi.shutdown();

        Log.d("TAG", "Is POI updated? " + Boolean.valueOf(poiList != null));

    }

}
