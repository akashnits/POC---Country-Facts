package com.example.akash.proofofconcept.model;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Country{

	@SerializedName("title")
	private String title;

	@SerializedName("rows")
	private List<CountryFact> rows;

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setRows(List<CountryFact> rows){
		this.rows = rows;
	}

	public List<CountryFact> getRows(){
		return rows;
	}

	@Override
 	public String toString(){
		return 
			"Country{" + 
			"title = '" + title + '\'' + 
			",rows = '" + rows + '\'' + 
			"}";
		}
}