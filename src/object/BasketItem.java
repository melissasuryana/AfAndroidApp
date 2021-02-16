package au.com.accountsflow.object;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class BasketItem implements Parcelable
{
	private String code;
	private int quantity;
	private int quantityBonus;
	private String caseDiscountString;
	private int caseDiscount;
	private int caseDiscountDp;
	private int order;
	
	public BasketItem() {
		
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getQuantityBonus() {
		return quantityBonus;
	}
	public void setQuantityBonus(int quantityBonus) {
		this.quantityBonus = quantityBonus;
	}	
	public String getCaseDiscountString() {
		return caseDiscountString;
	}
	public void setCaseDiscountString(String caseDiscountString) {
		this.caseDiscountString = caseDiscountString;
		this.caseDiscount = -1;
		this.caseDiscountDp = -1;
	}
	public int getCaseDiscount() {
		if (caseDiscount < 0) {
			caseDiscount = Price.getBaseValue(this.getCaseDiscountString());
		}
		return caseDiscount;
	}	
	public int getCaseDiscountDp() {
		if (caseDiscountDp < 0) {
			caseDiscountDp = Price.getDecimalPoints(this.getCaseDiscountString());
		}
		return caseDiscountDp;
	}	
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
	// Parcelling part
	public BasketItem(Parcel in){		
		this.setCode(in.readString());
		this.setQuantity(in.readInt());
		this.setQuantityBonus(in.readInt());
		this.setCaseDiscountString(in.readString());
		this.setOrder(in.readInt());
	}

	@Override
	public int describeContents(){
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.getCode());
		dest.writeInt(this.getQuantity());
		dest.writeInt(this.getQuantityBonus());
		dest.writeString(this.getCaseDiscountString());
		dest.writeInt(this.getOrder());
	}
	
	public static final Parcelable.Creator<BasketItem> CREATOR = new Parcelable.Creator<BasketItem>() {
		public BasketItem createFromParcel(Parcel in) {
			return new BasketItem(in); 
		}

		public BasketItem[] newArray(int size) {
			return new BasketItem[size];
		}
	};
}
