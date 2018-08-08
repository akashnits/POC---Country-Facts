package com.example.akash.proofofconcept.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableInt;
import android.opengl.Visibility;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.akash.proofofconcept.model.CountryFact;

public class CountryItemViewModel extends BaseObservable {

    private CountryFact countryFact;

    public CountryItemViewModel(CountryFact countryFact) {
        this.countryFact = countryFact;
    }

    public String getHeader(){
        return countryFact.getTitle();
    }

    public String getBody(){
        return countryFact.getDescription();
    }

    public String getPicture(){
        return countryFact.getImageHref();
    }

    //using glide for fetching and caching images
    @BindingAdapter("imageUrl") public static void setImageUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    public void setCountryItem(CountryFact countryFact) {
        this.countryFact = countryFact;
        notifyChange();
    }
}
