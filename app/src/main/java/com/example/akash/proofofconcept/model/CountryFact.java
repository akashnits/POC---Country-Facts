package com.example.akash.proofofconcept.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class CountryFact {

	@SerializedName("imageHref")
	private String imageHref;

	@SerializedName("description")
	private String description;

	@SerializedName("title")
	public String title;

	public void setImageHref(String imageHref){
		this.imageHref = imageHref;
	}

	public String getImageHref(){
		return imageHref;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	@Override
 	public String toString(){
		return 
			"CountryFact{" +
			"imageHref = '" + imageHref + '\'' + 
			",description = '" + description + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}