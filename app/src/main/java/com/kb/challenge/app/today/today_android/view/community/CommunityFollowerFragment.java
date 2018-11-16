package com.kb.challenge.app.today.today_android.view.community;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kb.challenge.app.today.today_android.R;
import com.kb.challenge.app.today.today_android.model.community.FollowListResponse;
import com.kb.challenge.app.today.today_android.model.community.FollowerData;
import com.kb.challenge.app.today.today_android.network.ApplicationController;
import com.kb.challenge.app.today.today_android.network.NetworkService;
import com.kb.challenge.app.today.today_android.utils.Init;
import com.kb.challenge.app.today.today_android.utils.SharedPreference;
import com.kb.challenge.app.today.today_android.view.community.adapter.CommunityFollowerListAdapter;
import com.kb.challenge.app.today.today_android.view.login.LoginActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shineeseo on 2018. 11. 9..
 */

public class CommunityFollowerFragment extends Fragment implements Init {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "CommunityFollowerFragment";

    private CommunityFollowerFragment.OnFragmentInteractionListener mListener;

    private NetworkService networkService;

    private RecyclerView mRecyclerView;
    @Override
    public void init() {
        networkService = ApplicationController.Companion.getInstance().getNetworkService();
        SharedPreference.Companion.getInstance();
    }

    public CommunityFollowerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommunityFollowerFragment newInstance(/*String param1, String param2*/) {
        CommunityFollowerFragment fragment = new CommunityFollowerFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_community_following, container, false);

        init();
        TextView community_following_txt = (TextView)view.findViewById(R.id.community_following_txt);

        community_following_txt.setText("팔로워");
        TextView community_following_list_txt = (TextView)view.findViewById(R.id.community_following_list_txt);

        community_following_list_txt.setText("팔로워 리스트");

        ImageView community_back_btn = (ImageView)view.findViewById(R.id.community_back_btn);

        community_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.community_following_recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        getFollowerList();

        return view;

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CommunityFollowerFragment.OnFragmentInteractionListener) {
            mListener = (CommunityFollowerFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void getFollowerList() {
        Log.v("getFollowerList process", "getFollowerList process!!!");
        Call<FollowListResponse> requestDetail = networkService.getFollowerList(SharedPreference.Companion.getInstance().getPrefStringData("data"));
        requestDetail.enqueue(new Callback<FollowListResponse>() {
            @Override
            public void onResponse(Call<FollowListResponse> call, Response<FollowListResponse> response) {
                if (response.isSuccessful()) {
                    Log.v("getFollowerList", "getFollowerList process2!!!");
                    Log.v("message", response.body().getMessage().toString());

                    if (response.body().getMessage().toString().equals("success")) {
                        ArrayList<FollowerData> followerDataList = response.body().getData();
                        Log.v("followerDataList", followerDataList.toString());

                        CommunityFollowerListAdapter communityFollowerListAdapter = new CommunityFollowerListAdapter(getActivity(),followerDataList);
                        mRecyclerView.setAdapter(communityFollowerListAdapter);
                    }

                }

            }

            @Override
            public void onFailure(Call<FollowListResponse> call, Throwable t) {
                Log.i("err", t.getMessage());
            }
        });
    }
}
