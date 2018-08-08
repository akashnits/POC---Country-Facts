package com.example.akash.proofofconcept.view;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.akash.proofofconcept.R;
import com.example.akash.proofofconcept.databinding.ItemCountryBinding;
import com.example.akash.proofofconcept.model.CountryFact;
import com.example.akash.proofofconcept.viewmodel.CountryItemViewModel;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private List<CountryFact> countryFactList;

    @NonNull
    @Override
    public CountryAdapter.CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCountryBinding itemCountryBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_country, parent, false);
        return new CountryViewHolder(itemCountryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryAdapter.CountryViewHolder holder, int position) {
        holder.bindCountryInfo(countryFactList.get(position));
    }

    @Override
    public int getItemCount() {
        return countryFactList.size();
    }

    public void setCountryFactList(List<CountryFact> countryFactList) {
        this.countryFactList = countryFactList;
        notifyDataSetChanged();
    }

    class CountryViewHolder extends RecyclerView.ViewHolder{
        ItemCountryBinding mItemCountryBinding;

        public CountryViewHolder(ItemCountryBinding itemCountryBinding) {
            super(itemCountryBinding.getRoot());
            mItemCountryBinding= itemCountryBinding;
        }

        public void bindCountryInfo(CountryFact countryFact){
            updateUi(countryFact);
            if(mItemCountryBinding.getItemViewModel() == null){
                mItemCountryBinding.setItemViewModel(new CountryItemViewModel(countryFact));
            }else {
                mItemCountryBinding.getItemViewModel().setCountryItem(countryFact);
            }
        }

        private void updateUi(CountryFact countryFact){
            if(countryFact.getDescription() != null || countryFact.getTitle() != null
                    || countryFact.getImageHref() != null){
                mItemCountryBinding.cardView.setVisibility(View.VISIBLE);
                mItemCountryBinding.itemCountryInfo.setVisibility(View.VISIBLE);
            }else {
                mItemCountryBinding.cardView.setVisibility(View.GONE);
                mItemCountryBinding.itemCountryInfo.setVisibility(View.GONE);
            }

            if(!TextUtils.isEmpty(countryFact.getDescription()))
                mItemCountryBinding.labelDescription.setVisibility(View.VISIBLE);
            else
                mItemCountryBinding.labelDescription.setVisibility(View.GONE);

            if(!TextUtils.isEmpty(countryFact.getTitle()))
                mItemCountryBinding.labelTitle.setVisibility(View.VISIBLE);
            else
                mItemCountryBinding.labelTitle.setVisibility(View.GONE);
        }
    }
}
