package com.example.akash.proofofconcept.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;

import com.example.akash.proofofconcept.R;
import com.example.akash.proofofconcept.model.CountryFact;
import com.example.akash.proofofconcept.viewmodel.CountryViewModel;

import java.util.List;

//Uses MVVM design pattern: Fragment acts as an observer for the corresponding ViewModel (i.e. CountryViewModel)

public class MainFragment extends Fragment {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.label_status) TextView labelStatus;
  @BindView(R.id.list_country) RecyclerView listCountry;
  @BindView(R.id.linearLayout) ConstraintLayout linearLayout;
  @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
  Unbinder unbinder;

  private Context mContext;
  private CountryViewModel countryViewModel;
  private CountryAdapter countryAdapter;
  private CompositeDisposable disposable= new CompositeDisposable();

  public MainFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initViewModel();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_main, container, false);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    toolbar.setTitle("Canada");
    initViews(view);
    populateUI();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    mContext = context;
  }


  @Override
  public void onDestroy() {
    super.onDestroy();
    disposable.dispose();
  }

  private void initViewModel(){
    countryViewModel = ViewModelProviders.of(this).get(CountryViewModel.class);
  }

  private void subscribeToDataStream(){
    disposable.add(
    countryViewModel.getCountryFactList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableSingleObserver<List<CountryFact>>(){
              @Override
              public void onSuccess(List<CountryFact> countryFacts) {
                countryAdapter.updateData(countryFacts);
                swipeRefreshLayout.setRefreshing(false);
              }

              @Override
              public void onError(Throwable e) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
              }
            }));
  }

  //polishing UI - getting the mid position on screen to set up progress bar for SwipeRefresh progress bar
  private int[] getCenterPoint() {
    WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    DisplayMetrics metrics = new DisplayMetrics();
    display.getMetrics(metrics);
    return new int[] { metrics.heightPixels / 2, metrics.widthPixels / 2 };
  }

  private void initViews(View view){
    int[] mid = getCenterPoint();
    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)
            view.findViewById(R.id.label_status).getLayoutParams();
    layoutParams.setMargins(0, mid[0], 0, 0);
    labelStatus.setLayoutParams(layoutParams);

    swipeRefreshLayout.setProgressViewOffset(true, mid[0], mid[0] + 1);
    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        callAndSubscribe();
      }
    });
    countryAdapter = new CountryAdapter();
    LinearLayoutManager lm = new LinearLayoutManager(mContext);
    listCountry.setLayoutManager(lm);
    listCountry.setAdapter(countryAdapter);
  }

  private void callAndSubscribe(){
    countryViewModel.fetchCountryInfo();
    subscribeToDataStream();
  }

  private void populateUI(){
    disposable.add(
            countryViewModel.getCountryFactList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<CountryFact>>() {
                      @Override
                      public void onSuccess(List<CountryFact> countryFactList) {
                        if(countryFactList.size() ==0 )
                          callAndSubscribe();
                        else
                          subscribeToDataStream();
                      }

                      @Override
                      public void onError(Throwable e) {

                      }
                    }));
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

}
