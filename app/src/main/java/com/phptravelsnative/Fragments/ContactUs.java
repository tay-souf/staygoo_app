package com.phptravelsnative.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phptravelsnative.Network.Post.contact_request;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.Extra.Constant;
import com.phptravelsnative.utality.Views.SingleTonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ContactUs extends Fragment {

    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.txt_address) TextView addressText;
    @BindView(R.id.input_subject) EditText _subText;
    @BindView(R.id.input_message)EditText _messageText;
    @BindView(R.id.btn_signup)
    Button _signupButton;

    ProgressDialog progressDialog;

    String sendTo;

    public ContactUs() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_contact_us, container, false);
        ButterKnife.bind(this,v);
        progressDialog= new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        getContacts();
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    postReport();
                }
            }
        });


        return v;
    }

    private void postReport() {
        Response.Listener<String> listener =new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    progressDialog.dismiss();

                    JSONObject main_json = new JSONObject(response);
                    JSONObject json_reponse=main_json.getJSONObject("response");
                    if(json_reponse.getBoolean("sent"))
                    {
                        Toast.makeText(getContext(),"Request Send",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getContext(),"Something Error",Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        progressDialog.show();
        contact_request requestRegister=new contact_request(_emailText.getText().toString()
                ,_nameText.getText().toString(),_subText.getText().toString(),_messageText.getText().toString()
                ,sendTo,listener);

        RequestQueue requestQueue = SingleTonRequest.getmInctance(getContext()).getRequestQueue();
        requestQueue.add(requestRegister);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
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

        return valid;
    }

    public void getContacts()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constant.domain_name+"contact/info?appKey="+Constant.key+"&lang="+Constant.default_lang,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {


                        JSONObject parentObject = null;
                        try {
                            parentObject = new JSONObject(response.toString());
                            JSONObject main_object= parentObject.getJSONObject("response");
                            addressText.setText(main_object.getString("contact_address"));
                            sendTo=main_object.getString("contact_email");
                            progressDialog.dismiss();

                        }
                        catch(JSONException e){

                            Log.d("abcwwwwd",e.getMessage());
                        }

                    }
                }, null);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().finish();
    }
}
