package com.example.vntek.cycling;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginLayout extends Fragment {

    public View v;

    private EditText LoginID=null;
    private EditText LoginPW=null;
    private CheckBox LoginAutoSignIn=null;
    private Button LoginSignIn=null;
    private Button LoginJoin=null;
    private TextView LoginDoLogin=null;

    private SharedPreferences sharedPreferences=null;
    private SharedPreferences.Editor sharedPreferencesEditor=null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.login_layout, container, false);

        getParameter();

        BtnLoginJoinOnClick();
        BtnLoginSignInOnClick();

        return v;
    }

    protected void getParameter(){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        LoginSignIn=v.findViewById(R.id.LoginSignIn);
        LoginID=v.findViewById(R.id.LoginID);
        LoginPW=v.findViewById(R.id.LoginPW);
        LoginAutoSignIn=v.findViewById(R.id.LoginAutoSignIn);
        LoginDoLogin=v.findViewById(R.id.LoginDoLogin);
        LoginJoin=v.findViewById(R.id.LoginJoin);

        sharedPreferencesEditor = sharedPreferences.edit();

    }

    protected void BtnLoginSignInOnClick(){

        LoginSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                RequestQueue postQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

                StringRequest postRequest = new StringRequest(Request.Method.POST, "https://android-api.cyclingsupporter.cf/AndroidLogin",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if(response != null && response.matches("Failed") == false )
                                {

                                    if(LoginAutoSignIn.isChecked() == true )
                                    {

                                        sharedPreferencesEditor.putString("auto_login_id", LoginID.getText().toString());
                                        sharedPreferencesEditor.putString("auto_login_pw", LoginPW.getText().toString());
                                        sharedPreferencesEditor.putBoolean("auto_login_isEnabled", true);

                                        sharedPreferencesEditor.commit();

                                    }
                                    else
                                    {

                                        sharedPreferencesEditor.putString("auto_login_id", null);
                                        sharedPreferencesEditor.putString("auto_login_pw", null);
                                        sharedPreferencesEditor.putBoolean("auto_login_isEnabled", false);

                                        sharedPreferencesEditor.commit();

                                    }

                                    ((MainActivity)getActivity()).mainBundle.putString("login_session",response);

                                    Log.v("TAG", "LoginSignIn.setOnClickListener in LoginLayout: Success. Your Session is " + response );

                                    ((MainActivity)getActivity()).updateNavHeader();

                                    getFragmentManager().popBackStack();

                                }
                                else if(response != null && response.matches("Failed") == true )
                                {

                                    ((MainActivity)getActivity()).mainBundle.putString("session", null);

                                    ((MainActivity)getActivity()).navName.setText(R.string.nav_header_title);
                                    ((MainActivity)getActivity()).navEmail.setText(R.string.nav_header_subtitle);

                                    LoginDoLogin.setText("아이디 또는 비밀번호를 확인해주세요.");

                                    Log.e("TAG", "LoginSignIn.setOnClickListener in LoginLayout: Failed");
                                    Log.e("TAG", "LoginSignIn.setOnClickListener in LoginLayout: Input parameters for login are NOT correct.");

                                }
                                else
                                {

                                    Log.e("TAG", "LoginSignIn.setOnClickListener in LoginLayout: Failed");
                                    Log.e("TAG", "LoginSignIn.setOnClickListener in LoginLayout: HTTP Error");
                                    LoginDoLogin.setText("서버로부터 응답을 받지 못했어요.");

                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("TAG", "LoginSignIn.setOnClickListener in LoginLayout: Failed");
                        Log.e("TAG", "LoginSignIn.setOnClickListener in LoginLayout: HTTP Error");
                        LoginDoLogin.setText("서버로부터 응답을 받지 못했어요.");

                    }

                }){

                    @Override
                    protected Map<String, String> getParams(){

                        Map<String, String> params=new HashMap<>();

                        params.put("id", LoginID.getText().toString());
                        params.put("pw", LoginPW.getText().toString());

                        return params;

                    }

                };

                postQueue.add(postRequest);

            }

        });

    }

    protected void BtnLoginJoinOnClick(){

        LoginJoin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                getFragmentManager().beginTransaction().replace(R.id.main_frame, new CreateAccountLayout()).addToBackStack(null).commit();

            }

        });

    }

    public void onDestroyView(){

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        super.onDestroyView();

    }

}
