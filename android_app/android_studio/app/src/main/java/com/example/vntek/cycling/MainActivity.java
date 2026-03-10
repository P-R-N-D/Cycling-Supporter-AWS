package com.example.vntek.cycling;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import android.preference.PreferenceManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    public DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    public TextView navName;
    public TextView navEmail;

    public MenuItem navRecord;
    public MenuItem navLogin;
    public MenuItem navLogout;

    public AlertDialog.Builder finishDialog;

    private FragmentManager fragmentManager;
    public Bundle mainBundle;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //R은 리소스 아래 layout에 activitymain이 있다.

        requestPermission();

        getParameter();

        fragmentManager.beginTransaction().add(R.id.main_frame, new SpeedLayout()).commit();

        autoLogin();

    }

    protected void getParameter(){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        sharedPreferencesEditor = sharedPreferences.edit();

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);

        navName=navigationView.getHeaderView(0).findViewById(R.id.nav_name);
        navEmail=navigationView.getHeaderView(0).findViewById(R.id.nav_email);

        navRecord=navigationView.getMenu().findItem(R.id.nav_record_layout);
        navLogin=navigationView.getMenu().findItem(R.id.nav_login_layout);
        navLogout=navigationView.getMenu().findItem(R.id.nav_logout_layout);

        finishDialog = new AlertDialog.Builder(this);
        finishDialog.setTitle("Cycling-Supporter");
        finishDialog.setMessage("앱을 종료하시겠어요?");
        finishDialog.setPositiveButton("예",

                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int button) {

                        finish();

                    }

                });
        finishDialog.setNegativeButton("아니요", null);

        fragmentManager=getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {

                        fragmentManager.beginTransaction().detach(fragmentManager.findFragmentById(R.id.main_frame)).attach(fragmentManager.findFragmentById(R.id.main_frame)).commit();

                    }
                });

        mainBundle = new Bundle();
        mainBundle.putString("session", null);

        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        Log.v("TAG", "navigationView.setNavigationItemSelectedListener(this)");

    }

    @Override
    public void onBackPressed() {

        Log.v("TAG", "Back stack entry count: " + fragmentManager.getBackStackEntryCount());

        if (drawer.isDrawerOpen(GravityCompat.START) == true) {

            drawer.closeDrawer(GravityCompat.START);

        }
        else {

            if(fragmentManager.getBackStackEntryCount() == 0){

                finishDialog.show();

            }
            else{

                fragmentManager.popBackStack();

            }

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Log.v("TAG", "Running on: onNavigationItemSelected(MenuItem item)");

        int id = item.getItemId();

        Log.v("TAG", "Drawer: selected item is: " + id);

        switch(id){

            case R.id.nav_navigation_layout:

                fragmentManager.beginTransaction().replace(R.id.main_frame, new NavigationLayout()).addToBackStack(null).commit();
                break;

            case R.id.nav_speed_layout:

                fragmentManager.beginTransaction().replace(R.id.main_frame, new SpeedLayout()).addToBackStack(null).commit();
                break;

            case R.id.nav_record_layout:

                fragmentManager.beginTransaction().replace(R.id.main_frame, new RecordLayout()).addToBackStack(null).commit();
                break;

            case R.id.nav_ranking_layout:

                fragmentManager.beginTransaction().replace(R.id.main_frame, new RankingLayout()).addToBackStack(null).commit();
                break;

            case R.id.nav_friend_layout:

                fragmentManager.beginTransaction().replace(R.id.main_frame, new FriendLayout()).addToBackStack(null).commit();
                break;

            case R.id.nav_club_layout:

                fragmentManager.beginTransaction().replace(R.id.main_frame, new ClubLayout()).addToBackStack(null).commit();
                break;

            case R.id.nav_login_layout:

                checkConnectivityForLogin();

                break;

            case R.id.nav_logout_layout:

                mainBundle.putString("login_session", null);

                sharedPreferencesEditor.putString("auto_login_id", null);
                sharedPreferencesEditor.putString("auto_login_pw", null);
                sharedPreferencesEditor.putBoolean("auto_login_isEnabled", false);

                sharedPreferencesEditor.commit();

                navName.setText(R.string.nav_header_title);
                navEmail.setText(R.string.nav_header_subtitle);
                navRecord.setVisible(FALSE);
                navLogin.setVisible(TRUE);
                navLogout.setVisible(FALSE);

                fragmentManager.beginTransaction().detach(fragmentManager.findFragmentById(R.id.main_frame)).attach(fragmentManager.findFragmentById(R.id.main_frame)).commit();

                Log.v("TAG", "Logout in MainActivity");

                break;

            default:

                Log.e("TAG", "Drawer: Invalid drawer item id");

                break;

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void checkConnectivityForLogin(){

        RequestQueue postQueue = Volley.newRequestQueue(this.getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, "https://android-api.cyclingsupporter.cf/HttpsConnectionStatus",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("TAG", "checkConnectivityForLogin() in MainActivity: [Response] " + response);

                        fragmentManager.beginTransaction().replace(R.id.main_frame, new LoginLayout()).addToBackStack(null).commit();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NetworkError) {

                    Log.i("TAG", "checkConnectivityForLogin() in MainActivity: cannot connect to the URL https://android-api.cyclingsupporter.cf Network Error: " + error);

                    Toast.makeText(getApplicationContext(), "인터넷에 연결할 수 없어요.", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {

                    Toast.makeText(getApplicationContext(), "서버에서 응답이 없어요.", Toast.LENGTH_SHORT).show();

                } else if (error instanceof AuthFailureError) {


                } else if (error instanceof ParseError) {


                } else if (error instanceof TimeoutError) {

                    Toast.makeText(getApplicationContext(), "서버에서 응답이 늦어지고 있어요.", Toast.LENGTH_SHORT).show();

                }

            }

        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params=new HashMap<>();

                return params;

            }

        };

        postQueue.add(postRequest);

    }

    public void autoLogin(){

        if(sharedPreferences.getBoolean("auto_login_isEnabled", false) == true){

            RequestQueue postQueue = Volley.newRequestQueue(this.getApplicationContext());

            StringRequest postRequest = new StringRequest(Request.Method.POST, "https://android-api.cyclingsupporter.cf/AndroidLogin",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(response != null && response.matches("Failed") == false )
                            {

                                mainBundle.putString("login_session",response);

                                Log.v("TAG", "void AutoLogin() in MainActivity: Success. Your Session is " + response);

                                updateNavHeader();

                            }
                            else if(response != null && response.matches("Failed") == true )
                            {

                                mainBundle.putString("login_session",null);

                                navName.setText(R.string.nav_header_title);
                                navEmail.setText(R.string.nav_header_subtitle);
                                navRecord.setVisible(FALSE);
                                navLogin.setVisible(TRUE);
                                navLogout.setVisible(FALSE);

                                fragmentManager.beginTransaction().detach(fragmentManager.findFragmentById(R.id.main_frame)).attach(fragmentManager.findFragmentById(R.id.main_frame)).commit();

                                Log.e("TAG", "void AutoLogin() in MainActivity: Failed");
                                Log.e("TAG", "void AutoLogin() in MainActivity: SharedPreferences for auto login are NOT correct.");

                            }
                            else
                            {

                                Log.e("TAG", "void AutoLogin() in MainActivity: Failed");
                                Log.e("TAG", "void AutoLogin() in MainActivity: HTTP Error");

                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("TAG", "void AutoLogin() in MainActivity: Failed");
                    Log.e("TAG", "void AutoLogin() in MainActivity: HTTP Error");

                }

            }){

                @Override
                protected Map<String, String> getParams(){

                    Map<String, String> params=new HashMap<>();

                    params.put("id", sharedPreferences.getString("auto_login_id", null));
                    params.put("pw", sharedPreferences.getString("auto_login_pw",null));

                    return params;

                }

            };

            postQueue.add(postRequest);

        }

    }

    public void updateNavHeader(){

        RequestQueue postQueue = Volley.newRequestQueue(this.getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, "https://android-api.cyclingsupporter.cf/SessionToUserInfo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject json;

                        try{

                            json=new JSONObject(response);

                            Log.i("TAG", "updateNavHeader(): name is " + json.optString("name"));
                            Log.i("TAG", "updateNavHeader(): email is " + json.optString("email"));

                            navName.setText(json.optString("name"));
                            navEmail.setText(json.optString("email"));
                            navRecord.setVisible(TRUE);
                            navLogin.setVisible(FALSE);
                            navLogout.setVisible(TRUE);

                            fragmentManager.beginTransaction().detach(fragmentManager.findFragmentById(R.id.main_frame)).attach(fragmentManager.findFragmentById(R.id.main_frame)).commit();

                        }
                        catch(JSONException e){

                            e.printStackTrace();
                            Log.e("TAG", "void updateNavHeader() in MainActivity: Failed");
                            Log.e("TAG", "void updateNavHeader() in MainActivity: JSON Exception");


                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("TAG", "void updateNavHeader() in MainActivity: Failed. " + error);

            }

        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params=new HashMap<>();

                Log.i("TAG", "updateNavHeader() session: "+ mainBundle.getString("login_session"));

                params.put("session", mainBundle.getString("login_session"));

                return params;

            }

        };

        postQueue.add(postRequest);

    }

    protected void requestPermission(){

        if(android.os.Build.VERSION.SDK_INT >= 23){

            Log.v("TAG", "void requestPermission() in MainActivity: Your Android OS version is 6.0, or later.");

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                Log.i("TAG", "void requestPermission() in MainActivity: All necessary permissions are granted");

            }
            else{

                String[] permissions = new String[]
                        {

                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.BLUETOOTH,
                                Manifest.permission.BLUETOOTH_ADMIN,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION

                        };

                Log.v("TAG", "void requestPermission() in MainActivity: Requesting necesary permissions");
                ActivityCompat.requestPermissions(this, permissions, 0);

            }


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case 0: {

                if(grantResults.length == permissions.length) {

                    Log.i("TAG", "void onRequestPermissionsResult() in MainActivity: All necesary permissions are granted");

                }
                else{

                    finish();

                }

                break;

            }

        }

    }

}