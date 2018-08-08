package com.example.akash.proofofconcept.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.akash.proofofconcept.R;
import com.example.akash.proofofconcept.databinding.FragmentMainBinding;
import com.example.akash.proofofconcept.viewmodel.CountryViewModel;

import java.util.Observable;
import java.util.Observer;


public class MainFragment extends Fragment implements Observer {

    private Context mContext;
    private FragmentMainBinding fragmentMainBinding;
    private CountryViewModel countryViewModel;
    private CountryAdapter countryAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        countryViewModel= new CountryViewModel(mContext.getApplicationContext());
        countryAdapter= new CountryAdapter();
        setUpObserver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentMainBinding= DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        fragmentMainBinding.setMainViewModel(countryViewModel);
        return fragmentMainBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int[] mid= getCenterPoint();
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)
                view.findViewById(R.id.label_status).getLayoutParams();
        layoutParams.setMargins(0, mid[0], 0, 0);
        fragmentMainBinding.labelStatus.setLayoutParams(layoutParams);

        fragmentMainBinding.swipeRefreshLayout.setProgressViewOffset(true, mid[0], mid[0]+1);
        LinearLayoutManager lm= new LinearLayoutManager(mContext);
        fragmentMainBinding.listCountry.setLayoutManager(lm);
        fragmentMainBinding.listCountry.setAdapter(countryAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext= context;
    }

    private void setUpObserver(){
        countryViewModel.addObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countryViewModel.reset();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof CountryViewModel){
            countryAdapter.setCountryFactList(((CountryViewModel) o).getCountryFactList());
        }
    }

    private int[] getCenterPoint(){
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return new int[]{metrics.heightPixels/2, metrics.widthPixels/2};
    }
}
