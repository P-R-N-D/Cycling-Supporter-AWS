package com.example.vntek.cycling;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

public class CreateAccountLayout extends Fragment {

    View v;

    TextView JoinNotify=null;
    EditText JoinName=null;
    EditText JoinEmail=null;
    EditText JoinID=null;
    EditText JoinPW=null;
    EditText JoinConfirmPW=null;
    Button JoinFinish=null;
    Button JoinRefresh=null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.create_account_layout, container, false);

        getParameter();

        BtnFinishOnClick();
        BtnRefreshOnClick();

        return v;
    }

    void getParameter(){

        JoinNotify=v.findViewById(R.id.JoinNotify);
        JoinName=v.findViewById(R.id.JoinName);
        JoinEmail=v.findViewById(R.id.JoinEmail);
        JoinID=v.findViewById(R.id.JoinID);
        JoinPW=v.findViewById(R.id.JoinPW);
        JoinConfirmPW=v.findViewById(R.id.JoinConfirmPW);
        JoinFinish=v.findViewById(R.id.JoinFinish);
        JoinRefresh=v.findViewById(R.id.JoinRefresh);

    }

    void BtnFinishOnClick(){

        JoinFinish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(JoinName.getText().toString()) == true
                        ||TextUtils.isEmpty(JoinEmail.getText().toString()) == true
                        ||TextUtils.isEmpty(JoinID.getText().toString()) == true
                        ||TextUtils.isEmpty(JoinPW.getText().toString()) == true
                        || TextUtils.isEmpty(JoinConfirmPW.getText().toString()) == true)
                {

                    JoinNotify.setText("입력하지 않은 정보가 있어요.");

                }
                else
                {

                    if(JoinPW.getText().toString().matches(JoinConfirmPW.getText().toString()) == true)
                    {

                        JoinRegister();

                    }
                    else
                    {

                        JoinNotify.setText("비밀번호와 비밀번호 확인의 정보가 서로 달라요.");

                    }

                }

            }

        });

    }

    void BtnRefreshOnClick(){

        JoinRefresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                JoinNotify.setText("회원가입에 필요한 정보를 입력해주세요.");
                JoinName.setText("");
                JoinEmail.setText("");
                JoinID.setText("");
                JoinPW.setText("");
                JoinConfirmPW.setText("");

            }

        });

    }

    protected void JoinRegister(){

        RequestQueue postQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest postRequest = new StringRequest(Request.Method.POST, "https://android-api.cyclingsupporter.cf/AndroidRegister",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response == null)
                        {

                            JoinNotify.setText("서버로부터 응답을 받지 못했어요.");

                        }

                        switch(response)
                        {

                            case "OK":

                                getFragmentManager().popBackStack();

                                break;

                            case "Already Registered":

                                JoinNotify.setText("입력하신 아이디로 이미 회원가입을 하셨어요.");
                                break;

                            case "Failed":

                                JoinNotify.setText("지금은 회원가입을 할 수 없어요.");
                                break;

                            default:

                                JoinNotify.setText("서버로부터 적절한 응답을 받지 못했어요.");
                                break;

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("TAG", "CreateAccountLayout: HTTP Error");
                JoinNotify.setText("서버로부터 응답을 받지 못했어요.");

            }

        }){

            @Override
            protected Map<String, String> getParams(){

                Map<String, String> params=new HashMap<>();

                params.put("name", JoinName.getText().toString());
                params.put("email", JoinEmail.getText().toString());
                params.put("id", JoinID.getText().toString());
                params.put("pw", JoinPW.getText().toString());

                return params;

            }

        };

        postQueue.add(postRequest);

    }

    public void onDestroyView(){

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        super.onDestroyView();

    }

}
