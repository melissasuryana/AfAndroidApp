package au.com.accountsflow.object;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import au.com.accountsflow.R;

public class Product implements Parcelable, Comparable<Product>{
	private int supplierId;
	private String code;
	private String category;
	private String description;
	private String unitSize;
	private String unitType;
	private String packType;
	private int unitPerCase;
	private Price price;
	private ArrayList<Special> specials;
	private ArrayList<Discount> qda;
	private Discount promotion;
	
	public Product(String code, String category,
			String description, String unitSize, String unitType,
			String packType, int unitPerCase) {
		super();
		this.code = code;
		this.category = category;
		this.description = description;
		this.unitSize = unitSize;
		this.unitType = unitType;
		this.packType = packType;
		this.unitPerCase = unitPerCase;
		this.specials = new ArrayList<Special>();
		this.qda = new ArrayList<Discount>();
		this.promotion = null;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnitSize() {
		return unitSize;
	}

	public void setUnitSize(String unitSize) {
		this.unitSize = unitSize;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getPackType() {
		return packType;
	}

	public void setPackType(String packType) {
		this.packType = packType;
	}

	public int getUnitPerCase() {
		return unitPerCase;
	}

	public void setUnitPerCase(int unitPerCase) {
		this.unitPerCase = unitPerCase;
	}
		
	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}
	
	public ArrayList<Special> getSpecials() {
		return this.specials;
	}

	public void setSpecials(ArrayList<Special> special) {
		this.specials = special;
	}
	
	public void addSpecial(Special special) {
		if (this.specials == null) {
			this.specials = new ArrayList<Special>();
		}
		this.specials.add(special);
	}

	public Discount getPromotion() {
		return promotion;
	}

	public void setPromotion(Discount promotion) {
		this.promotion = promotion;
	}	

	public ArrayList<Discount> getQda() {
		return qda;
	}

	public void setQda(ArrayList<Discount> qda) {
		this.qda = qda;
	}
	
	public void addQda(Discount qda) {
		if (this.qda == null) {
			this.qda = new ArrayList<Discount>();
		}
		this.qda.add(qda);
	}

	public boolean hasSpecials() {
		return this.getSpecials() != null && ! this.getSpecials().isEmpty();
	}
	
	public boolean hasQda() {
		return this.getQda() != null && ! this.getQda().isEmpty();
	}
	
	public boolean hasPromotion() {
		return this.getPromotion() != null && this.getPromotion().isPromo();
	}
		
	public boolean hasDeals() {
		return this.hasPromotion() || this.hasQda() || this.hasSpecials();
	}
	@Override
	public int compareTo(Product arg0) {
		return this.getDescription().compareTo(arg0.getDescription());
	}
	
	// Parcelling part
	public Product(Parcel in){
		this.setSupplierId(in.readInt());
		this.setCode(in.readString());
		this.setCategory(in.readString());
		this.setDescription(in.readString());
		this.setUnitSize(in.readString());
		this.setUnitType(in.readString());
		this.setPackType(in.readString());
		this.setUnitPerCase(in.readInt());
		this.setPrice((Price) in.readParcelable(Price.class.getClassLoader()));
		this.specials = new ArrayList<Special>();
		this.qda = new ArrayList<Discount>();
		in.readTypedList(specials, Special.CREATOR);
		in.readTypedList(qda, Discount.CREATOR);
		this.setPromotion((Discount) in.readParcelable(Discount.class.getClassLoader()));
	}

	@Override
	public int describeContents(){
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.getSupplierId());
		dest.writeString(this.getCode());
		dest.writeString(this.getCategory());
		dest.writeString(this.getDescription());
		dest.writeString(this.getUnitSize());
		dest.writeString(this.getUnitType());
		dest.writeString(this.getPackType());
		dest.writeInt(this.getUnitPerCase());
		dest.writeParcelable(this.getPrice(), flags);
		dest.writeTypedList(this.getSpecials());
		dest.writeTypedList(this.getQda());
		dest.writeParcelable(this.getPromotion(), flags);
	}
	
	public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
		public Product createFromParcel(Parcel in) {
			return new Product(in); 
		}

		public Product[] newArray(int size) {
			return new Product[size];
		}
	};
	
}
