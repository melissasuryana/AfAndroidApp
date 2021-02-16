package au.com.accountsflow.object;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Basket implements Parcelable
{
	private String id;
	private int supplierId;
	private int retailerId;
	private int wholesalerId;
	private String name;
	private String notes;
	private ArrayList<BasketItem> items;
	
	public Basket() {
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	public int getRetailerId() {
		return retailerId;
	}
	public void setRetailerId(int retailerId) {
		this.retailerId = retailerId;
	}
	public int getWholesalerId() {
		return wholesalerId;
	}
	public void setWholesalerId(int wholesalerId) {
		this.wholesalerId = wholesalerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public ArrayList<BasketItem> getItems() {
		return items;
	}
	public void setItems(ArrayList<BasketItem> items) {
		this.items = items;
	}
	
	// Parcelling part
	public Basket(Parcel in){
		this.setId(in.readString());
		this.setSupplierId(in.readInt());
		this.setRetailerId(in.readInt());
		this.setWholesalerId(in.readInt());
		this.setName(in.readString());
		this.setNotes(in.readString());		
		this.items = new ArrayList<BasketItem>();
		in.readTypedList(items, BasketItem.CREATOR);
	}

	@Override
	public int describeContents(){
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.getId());
		dest.writeInt(this.getSupplierId());
		dest.writeInt(this.getRetailerId());
		dest.writeInt(this.getWholesalerId());
		dest.writeString(this.getName());
		dest.writeString(this.getNotes());
		dest.writeTypedList(this.getItems());
	}
	
	public static final Parcelable.Creator<Basket> CREATOR = new Parcelable.Creator<Basket>() {
		public Basket createFromParcel(Parcel in) {
			return new Basket(in); 
		}

		public Basket[] newArray(int size) {
			return new Basket[size];
		}
	};
}
