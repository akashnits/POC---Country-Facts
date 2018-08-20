package com.example.akash.proofofconcept.viewmodel;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.akash.proofofconcept.model.Country;
import com.example.akash.proofofconcept.model.CountryFact;
import com.example.akash.proofofconcept.networkUtils.ApiClient;
import com.example.akash.proofofconcept.networkUtils.ApiService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CountryViewModel extends AndroidViewModel {
    private Single<List<CountryFact>> countryFactList;

    public CountryViewModel(@NonNull Application application) {
        super(application);
        this.countryFactList= Single.create(new SingleOnSubscribe<List<CountryFact>>() {
            @Override
            public void subscribe(SingleEmitter<List<CountryFact>> emitter) throws Exception {
                emitter.onSuccess(new ArrayList<CountryFact>());
            }
        });
    }

    public Single<List<CountryFact>> getCountryFactList() {
        return countryFactList;
    }

    //fetch observable from server
    public void fetchCountryInfo(){
        ApiService apiService= ApiClient.getInstance().getClient().create(ApiService.class);
        countryFactList= apiService.getCountry()
                .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                .flatMap(new Function<Country, SingleSource<List<CountryFact>>>() {
                    @Override
                    public SingleSource<List<CountryFact>> apply(Country country) throws Exception {
                        return getCountryFactObservable(country);
                    }
                });
    }

    private Single<List<CountryFact>> getCountryFactObservable(final Country country){
        countryFactList= Single.create(new SingleOnSubscribe<List<CountryFact>>() {
            @Override
            public void subscribe(SingleEmitter<List<CountryFact>> emitter) throws Exception {
                if(!emitter.isDisposed())
                    emitter.onSuccess(country.getRows());
            }
        });
        return countryFactList ;
    }
}
