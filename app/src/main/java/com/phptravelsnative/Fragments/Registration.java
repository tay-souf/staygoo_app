package com.phptravelsnative.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.phptravelsnative.Activities.MainLayout;
import com.phptravelsnative.Network.Post.RequestRegister;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Views.SingleTonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Registration extends Fragment {


    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_phone)EditText phoneText;
    @BindView(R.id.first_name)EditText _firstText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;
    public Registration() {
    }

    // TODO: Rename and change types and number of parameters
    public static Registration newInstance(String param1, String param2) {
        Registration fragment = new Registration();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflated=inflater.inflate(R.layout.activity_sign_up, container, false);


        ButterKnife.bind(this,inflated);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),MainLayout.class);
                intent.putExtra("CheckLayout","login");
                startActivity(intent);
            }
        });

        return inflated;
    }
    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }


        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String first_name= _firstText.getText().toString();
        String phone_number=phoneText.getText().toString();

        Response.Listener<String> listener =new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    progressDialog.dismiss();
                    JSONObject main_json = new JSONObject(response);
                    if(main_json.getBoolean("response"))
                    {
                        onSignupSuccess();
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
        RequestRegister requestRegister=new RequestRegister(email,password,first_name,name,phone_number,listener);

        RequestQueue requestQueue = SingleTonRequest.getmInctance(getContext()).getRequestQueue();
        requestQueue.add(requestRegister);

    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        Intent intent=new Intent(getContext(),MainLayout.class);
        intent.putExtra("CheckLayout","login");
        startActivity(intent);

    }

    public void onSignupFailed() {
        Toast.makeText(getContext(), "Login failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String first_name= _firstText.getText().toString();
        String phone_number=phoneText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }
        if (first_name.isEmpty() || first_name.length() < 3) {
            _firstText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

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

    @Override
    public void onPause() {
        super.onPause();
        getActivity().finish();
    }
}
