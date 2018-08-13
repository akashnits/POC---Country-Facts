package com.example.akash.proofofconcept.viewmodel;


import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.example.akash.proofofconcept.R;
import com.example.akash.proofofconcept.model.Country;
import com.example.akash.proofofconcept.model.CountryFact;
import com.example.akash.proofofconcept.networkUtils.ApiClient;
import com.example.akash.proofofconcept.networkUtils.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CountryViewModel extends Observable {
    public ObservableInt countryRecyclerView;
    public ObservableInt countryLabel;
    public ObservableField<String> countryMessage;
    public ObservableField<String> toolbarTitle;
    public ObservableBoolean isRefreshing;


    private List<CountryFact> countryFactList;
    private Context context;
    private CompositeDisposable compositeDisposable= new CompositeDisposable();

    public CountryViewModel(Context context) {
        this.context = context;
        countryRecyclerView= new ObservableInt(View.GONE);
        countryLabel= new ObservableInt(View.GONE);
        countryMessage= new ObservableField<>(context.getString(R.string.pull_down_to_refresh));
        toolbarTitle= new ObservableField<>(context.getString(R.string.app_name));
        countryFactList = new ArrayList<>();
        isRefreshing = new ObservableBoolean();
        fetchCountryInfo();
    }

    public List<CountryFact> getCountryFactList() {
        return countryFactList;
    }

    //this is called on swiping down the refresh layout
    public void onRefresh(){
        isRefreshing.set(true);
        if(isNetworkAvailable()) {
            updateViews();
            fetchCountryInfo();
        }else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
            isRefreshing.set(false);
        }
    }

    public void updateViews(){
        countryRecyclerView.set(View.GONE);
        countryLabel.set(View.GONE);
    }

    //making network call to fetch json using RxJava and retrofit
    private void fetchCountryInfo(){
        ApiService apiService= ApiClient.getInstance().getClient().create(ApiService.class);
        compositeDisposable.add(apiService
                .getCountry()
                .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Country, List<CountryFact>>() {
                    @Override
                    public List<CountryFact> apply(Country country) throws Exception {
                        toolbarTitle.set(country.getTitle());
                        return country.getRows();
                    }
                })
                .subscribeWith(new DisposableSingleObserver<List<CountryFact>>(){
                    @Override
                    public void onSuccess(List<CountryFact> countryFacts) {

                        updateData(countryFacts);
                        countryRecyclerView.set(View.VISIBLE);
                        countryLabel.set(View.GONE);
                        isRefreshing.set(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        countryMessage.set(context.getString(R.string.error_loading_facts));
                        countryLabel.set(View.VISIBLE);
                        countryRecyclerView.set(View.GONE);
                        isRefreshing.set(false);
                    }
                }));

    }

    private void updateData(List<CountryFact> countryFacts){
        countryFactList.clear();
        countryFactList.addAll(countryFacts);
        setChanged();
        notifyObservers();
    }

    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
            = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
