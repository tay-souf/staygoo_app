package com.phptravelsnative.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.phptravelsnative.Activities.Invoice;
import com.phptravelsnative.Activities.MainLayout;
import com.phptravelsnative.Activities.WebViewInvoice;
import com.phptravelsnative.Models.ExpediaExtra;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.Models.Model_Tour;
import com.phptravelsnative.Models.car_model;
import com.phptravelsnative.Network.Post.LoginRequest;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Views.SingleTonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Login_Fragment extends Fragment {
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    SharedPreferences sharedPreferences;
    public  final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences.Editor editor;
    boolean _login=false;
    Hotel_data hotel_data;
    ExpediaExtra expediaExtra;
    String room_number,room_id;
    Model_Tour tour;
    car_model car;

    String type;
    // TODO: Rename and change types of parameters


    public Login_Fragment() {
    }

    public static Login_Fragment newInstance(String type, boolean _login, Hotel_data hotel_data, Model_Tour tour, car_model car, String room_id, String room_number) {
        Login_Fragment fragment = new Login_Fragment();
        Bundle args = new Bundle();
        args.putBoolean("check",_login);
        args.putString("check_type",type);

        if(type.equals("hotel")) {
            args.putParcelable("hotel", hotel_data);
            args.putString("room_id", room_id);
            args.putString("room_number", room_number);
        }else if(type.equals("tour"))
        {
            args.putParcelable("tour", tour);

        }else if(type.equals("car"))
        {
            args.putParcelable("car",car);
        }

        fragment.setArguments(args);
        return fragment;
    }
    public static Login_Fragment newInstance(String type, boolean _login, ExpediaExtra expediaExtra, Hotel_data hotel_data) {
        Login_Fragment fragment = new Login_Fragment();
        Bundle args = new Bundle();
        args.putBoolean("check",_login);
        args.putString("check_type",type);
        args.putParcelable("hotel_data",hotel_data);
        args.putParcelable("expedia",expediaExtra);


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        _login=getArguments().getBoolean("check");
        type=getArguments().getString("check_type");

        if(!_login)
        if(type.equals("hotel")) {
                hotel_data=getArguments().getParcelable("hotel");
                room_id=getArguments().getString("room_id");
                room_number=getArguments().getString("room_number");
        }else if(type.equals("car"))
        {
            car=getArguments().getParcelable("car");
        }else if(type.equals("tour"))
        {
            tour=getArguments().getParcelable("tour");
        }else if(type.equals("expedia"))
        {
            expediaExtra=getArguments().getParcelable("expedia");
            hotel_data=getArguments().getParcelable("hotel_data");
        }


        View inflated=inflater.inflate(R.layout.activity_login, container, false);
        sharedPreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        ButterKnife.bind(this, inflated);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();


            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),MainLayout.class);
                intent.putExtra("CheckLayout","register");
                startActivityForResult(intent, 2);
            }
        });

        return inflated;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(_login)
        {
            getActivity().finish();
        }

    }

    public void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.authenticating));
        progressDialog.show();

        String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        Response.Listener<String> listener =new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    progressDialog.dismiss();
                    JSONObject main_json = new JSONObject(response);
                    if(main_json.getBoolean("response"))
                    {
                        JSONObject userInfo_object=main_json.getJSONObject("userInfo");
                        editor.putBoolean("Check_Login",true);
                        editor.putString("email",userInfo_object.getString("email"));
                        editor.putString("name",userInfo_object.getString("firstName")+" "+userInfo_object.getString("lastName"));
                        editor.putString("password",password);
                        editor.putString("id",userInfo_object.getString("id"));
                        editor.commit();
                        onLoginSuccess();
                    }
                    else
                    {
                        JSONObject error_object=main_json.getJSONObject("error");
                        Toast.makeText(getContext(),error_object.getString("msg"),Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        LoginRequest requestRegister=new LoginRequest(email,password,listener);

        RequestQueue requestQueue = SingleTonRequest.getmInctance(getContext()).getRequestQueue();
        requestQueue.add(requestRegister);


        // TODO: Implement your own authentication logic here.

    }

    public void onLoginSuccess() {
        if(_login) {
            _loginButton.setEnabled(true);
            Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }else
        {
            Intent intent=new Intent(getContext(), WebViewInvoice.class);
            Bundle bundle=new Bundle();
            if(type.equals("hotel"))
            {
                bundle.putParcelable("hotel_object",hotel_data);
                intent.putExtra("check_type","hotel");
                intent.putExtra("room_id",room_id);
                intent.putExtra("numbers_rooms",room_number);
                intent.putExtras(bundle);
            }else if(type.equals("tour"))
            {
                bundle.putParcelable("tour_object",tour);
                intent.putExtra("check_type","tour");

                intent.putExtras(bundle);
            }else if(type.equals("car"))
            {
                bundle.putParcelable("car_object",car);
                intent.putExtra("check_type","car");
                intent.putExtras(bundle);
            }else  if(type.equals("expedia"))
            {
                intent=new Intent(getContext(), Invoice.class);
                bundle.putParcelable("hotel_object",hotel_data);
                bundle.putParcelable("expedia",expediaExtra);
                intent.putExtra("check_type","expedia");
                intent.putExtras(bundle);
            }
            startActivity(intent);
            getActivity().finish();
        }
    }

    public void onLoginFailed() {
        Toast.makeText(getContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
