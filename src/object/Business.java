package au.com.accountsflow.object;

import java.util.Comparator;

import android.graphics.Bitmap;

public class Business implements Comparable<Business> {
	public enum TYPE {
		RETAILER, SUPPLIER, BUYING_GROUP, WHOLESALER
	}
	
	private int id;
	private String name;
	private String logoUrl;
	private Business.TYPE type;
	private Bitmap logoBmp;
	private SubBusiness subBusiness;
	
	public Business (int id, String name, String logoUrl, Business.TYPE type) {
		this.setId(id);
		this.setName(name);
		this.setLogoUrl(logoUrl);
		this.setType(type);
	}
	
	public void setId (int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public Business.TYPE getType() {
		return type;
	}

	public void setType(Business.TYPE type) {
		this.type = type;
	}
	
	public Bitmap getLogoBmp() {
		return logoBmp;
	}

	public void setLogoBmp(Bitmap logoBmp) {
		this.logoBmp = logoBmp;
	}
	
	public SubBusiness getSubBusiness() {
		return subBusiness;
	}

	public void setSubBusiness(SubBusiness subBusiness) {
		this.subBusiness = subBusiness;
	}

	public String toString() {
		return this.getName();
	}
	
	@Override
	public int compareTo(Business arg0) {
		return this.getName().compareTo(arg0.getName());
	}
}
