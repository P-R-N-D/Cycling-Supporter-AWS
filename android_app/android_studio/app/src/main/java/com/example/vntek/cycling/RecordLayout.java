package com.example.vntek.cycling;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RecordLayout extends Fragment {

    View v;

    WebView webView;
    WebSettings webSettings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.record_layout, container, false);

        getParameter();

        RequestQueue postQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, "https://android-api.cyclingsupporter.cf/SessionToUserInfo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject json;

                        try{

                            json=new JSONObject(response);

                            String postParam="show_path_input_userid=" + URLEncoder.encode(json.optString("id"), "UTF-8");

                            webView.postUrl("https://android-api.cyclingsupporter.cf/ShowPathInput",postParam.getBytes());

                        }
                        catch(JSONException e){

                            e.printStackTrace();
                            Log.e("TAG", "void updateNavHeader() in MainActivity: Failed");
                            Log.e("TAG", "void updateNavHeader() in MainActivity: JSON Exception");


                        }
                        catch(UnsupportedEncodingException e)
                        {

                            e.printStackTrace();
                            Log.e("TAG", "void updateNavHeader() in MainActivity: Failed");
                            Log.e("TAG", "void updateNavHeader() in MainActivity: Unsupported HTTP Encoding Exception");

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("TAG", "void updateNavHeader() in MainActivity: Failed");
                Log.e("TAG", "void updateNavHeader() in MainActivity: HTTP Error");

            }

        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params=new HashMap<>();

                params.put("session", ((MainActivity)getActivity()).mainBundle.getString("login_session"));

                return params;

            }

        };

        postQueue.add(postRequest);

        return v;
    }

    void getParameter(){

        webView=v.findViewById(R.id.record_layout_webview);
        webSettings=webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

    }

}
