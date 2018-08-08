package com.example.akash.proofofconcept.viewmodel;


import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
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

import io.reactivex.SingleSource;
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
    public ObservableBoolean isLoading = new ObservableBoolean();


    private List<CountryFact> countryFactList;
    private Context context;
    private CompositeDisposable compositeDisposable= new CompositeDisposable();

    public CountryViewModel(Context context) {
        this.context = context;
        countryRecyclerView= new ObservableInt(View.GONE);
        countryLabel= new ObservableInt(View.VISIBLE);
        countryMessage= new ObservableField<>(context.getString(R.string.pull_down_to_refresh));
        toolbarTitle= new ObservableField<>(context.getString(R.string.app_name));
        countryFactList = new ArrayList<>();
    }

    public List<CountryFact> getCountryFactList() {
        return countryFactList;
    }

    public void onRefresh(){
        isLoading.set(true);
        updateViews();
        fetchCountryInfo();
    }

    private void updateViews(){
        countryRecyclerView.set(View.GONE);
        countryLabel.set(View.GONE);
    }

    private void fetchCountryInfo(){
        ApiService apiService= ApiClient.getClient().create(ApiService.class);
        compositeDisposable.add(apiService
                .getCountry()
                .subscribeOn(Schedulers.io())
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
                        isLoading.set(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        countryMessage.set(context.getString(R.string.error_loading_facts));
                        countryLabel.set(View.VISIBLE);
                        countryRecyclerView.set(View.GONE);
                        isLoading.set(false);
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
}
