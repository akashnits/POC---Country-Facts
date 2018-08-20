package com.example.akash.proofofconcept.view;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;
import com.example.akash.proofofconcept.R;
import com.example.akash.proofofconcept.model.CountryFact;
import com.example.akash.proofofconcept.viewmodel.CountryItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {


  private List<CountryFact> countryFactList;

   CountryAdapter() {
    this.countryFactList= new ArrayList<>();
  }

  @NonNull
  @Override
  public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
    return new CountryViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
    holder.bindCountryInfo(countryFactList.get(position));
  }

  @Override
  public int getItemCount() {
    return countryFactList.size();
  }

  class CountryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.label_title) TextView labelTitle;
    @BindView(R.id.label_description) TextView labelDescription;
    @BindView(R.id.label_imageView) ImageView labelImageView;
    @BindView(R.id.item_country_info) ConstraintLayout itemCountryInfo;
    @BindView(R.id.cardView) CardView cardView;

    CountryViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bindCountryInfo(CountryFact countryFact) {
      updateUi(countryFact);
      labelTitle.setText(countryFact.getTitle());
      labelDescription.setText(countryFact.getDescription());

      Glide.with(itemView.getContext()).load(countryFact.getImageHref()).into(labelImageView);
    }

    //updating UI as per data from server
    private void updateUi(CountryFact countryFact) {
      if (countryFact.getDescription() != null || countryFact.getTitle() != null
          || countryFact.getImageHref() != null) {
        cardView.setVisibility(View.VISIBLE);
        itemCountryInfo.setVisibility(View.VISIBLE);
      } else {
        cardView.setVisibility(View.GONE);
        itemCountryInfo.setVisibility(View.GONE);
      }

      if (!TextUtils.isEmpty(countryFact.getDescription())) {
        labelDescription.setVisibility(View.VISIBLE);
      } else {
        labelDescription.setVisibility(View.GONE);
      }

      if (!TextUtils.isEmpty(countryFact.getTitle())) {
        labelTitle.setVisibility(View.VISIBLE);
      } else {
        labelTitle.setVisibility(View.GONE);
      }
    }
  }

  public void updateData(List<CountryFact> factList){
    countryFactList.clear();
    countryFactList.addAll(factList);
    notifyDataSetChanged();
  }
}
