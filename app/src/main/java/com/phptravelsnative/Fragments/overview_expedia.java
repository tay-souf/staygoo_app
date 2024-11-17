package com.phptravelsnative.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.phptravelsnative.Adapters.Amenites_adpater_expedia;
import com.phptravelsnative.Models.Amenities_Model;
import com.phptravelsnative.Models.OverView;
import com.phptravelsnative.R;
import com.phptravelsnative.utality.lib.Parallex.NotifyingScrollView;
import com.phptravelsnative.utality.lib.Parallex.ScrollViewFragment;

import java.util.ArrayList;


public class overview_expedia extends ScrollViewFragment {

    OverView overView;
    private ProgressDialog dialog;
    TextView text_desc;
    ArrayList<Amenities_Model> amenities_list;
    LinearLayout amenities_layout;


    public static Fragment newInstance(int position, OverView ov, ArrayList<Amenities_Model> amenities_list) {
        overview_expedia fragment = new overview_expedia();


        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putParcelable("ov", ov);
        args.putParcelableArrayList("am", amenities_list);
        fragment.setArguments(args);
        return fragment;
    }

    public overview_expedia() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPosition = getArguments().getInt(ARG_POSITION);
        overView = getArguments().getParcelable("ov");
        dialog = new ProgressDialog(getContext());
        dialog = new ProgressDialog(getContext());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");
        amenities_list = getArguments().getParcelableArrayList("am");


        View view = inflater.inflate(R.layout.expedia_scrollview, container, false);
        mScrollView = (NotifyingScrollView) view.findViewById(R.id.scrollview);


        RecyclerView horizontal_recycler_view = (RecyclerView) view.findViewById(R.id.horizontal_recycler_view);

        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);
        Amenites_adpater_expedia amenites_adpater;
        amenites_adpater=new Amenites_adpater_expedia(amenities_list, getContext());


        horizontal_recycler_view.setAdapter(amenites_adpater);



        setScrollViewOnScrollListener();

        TextView text_policy = (TextView) view.findViewById(R.id.policy);
        text_policy.setText(fromHtml(overView.getPolicy()));

         text_desc = (TextView) view.findViewById(R.id.desc);
          amenities_layout=(LinearLayout)view.findViewById(R.id.amenities_layout);

        if(amenites_adpater.getItemCount()<=0)
        {
            amenities_layout.setVisibility(View.GONE);
        }

        text_desc.setText(fromHtml( fromHtml(overView.getDesc()) ));


        return view;
    }

    public  String fromHtml(String source) {
        source = source.replaceFirst("&apos;", "'");
        //Removes any connected item to the last bracket
        return source;

    }

}
