package com.phptravelsnative.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.phptravelsnative.Activities.WebViewInvoice;
import com.phptravelsnative.Models.Hotel_data;
import com.phptravelsnative.Models.Model_Tour;
import com.phptravelsnative.Models.car_model;
import com.phptravelsnative.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Guest extends Fragment {

    @BindView(R.id.first_name)EditText ed_first;
    @BindView(R.id.second_name)EditText ed_second;
    @BindView(R.id.input_email)EditText ed_mail;
    @BindView(R.id.input_address)EditText ed_address;
    @BindView(R.id.input_phone)EditText ed_phone;
    @BindView(R.id.btn_proceed)Button bt_proceed;

    String room_number,room_id;
    Hotel_data hotel_data;
    Hotel_data guest=new Hotel_data();
    car_model car;
    Model_Tour tour;
    String type;


    public Guest() {
    }

    // TODO: Rename and change types and number of parameters
    public static Guest newInstance(String s, Model_Tour tour, car_model car, Hotel_data hotel_data, String room_id, String room_number) {
        Guest fragment = new Guest();
        Bundle args = new Bundle();
        args.putString("type",s);
        if(s.equals("hotel")) {
            args.putParcelable("hotel", hotel_data);
            args.putString("room_id", room_id);
            args.putString("room_number", room_number);
        }else if(s.equals("tour"))
        {
            args.putParcelable("tour", tour);

        }else if(s.equals("car"))
        {
            args.putParcelable("car",car);
        }

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        type=getArguments().getString("type");
        if(type.equals("hotel")) {
            room_id = getArguments().getString("room_id");
            room_number = getArguments().getString("room_number");
            hotel_data = getArguments().getParcelable("hotel");
        }else if(type.equals("car"))
        {
            car=getArguments().getParcelable("car");
        }else if(type.equals("tour"))
        {
            tour=getArguments().getParcelable("tour");
        }

        View view =inflater.inflate(R.layout.fragment_guest, container, false);

        ButterKnife.bind(this,view);




        bt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    guest.setAdult(ed_first.getText().toString());
                    guest.setChild(ed_second.getText().toString());
                    guest.setLocation(ed_mail.getText().toString());
                    guest.setFrom(ed_address.getText().toString());
                    guest.setTo(ed_phone.getText().toString());
                    Intent intent=new Intent(getContext(), WebViewInvoice.class);
                    Bundle bundle=new Bundle();
                    bundle.putBoolean("Check_Guest",true);
                    bundle.putParcelable("guest",guest);
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
                    }

                    startActivity(intent);
                }

                }
        });

        return view;
    }

    public boolean validate() {
        boolean valid = true;

        String name = ed_first.getText().toString();
        String email = ed_mail.getText().toString();
        String second = ed_second.getText().toString();
        String address= ed_address.getText().toString();
        String phone_number=ed_phone.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            ed_first.setError("at least 3 characters");
            valid = false;
        } else {
            ed_first.setError(null);
        }
        if (address.isEmpty() || address.length() < 3) {
            ed_address.setError("at least 3 characters");
            valid = false;
        } else {
            ed_address.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ed_mail.setError("enter a valid email address");
            valid = false;
        } else {
            ed_mail.setError(null);
        }

        if (second.isEmpty() || second.length() < 4 || second.length() > 10) {
            ed_second.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
           ed_second .setError(null);
        }

        return valid;
    }


}
