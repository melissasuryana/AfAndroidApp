package au.com.accountsflow.object;

import java.util.ArrayList;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Special implements Parcelable{
	private String code;
	private Date dateStart;
	private Date dateEnd;
	private int cases;
	private String description;
	private ArrayList<Bonus> bonuses;
	
	public Special() {
		bonuses = new ArrayList<Bonus>();
	}
		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public int getCases() {
		return cases;
	}
	public void setCases(int cases) {
		this.cases = cases;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setBonuses(ArrayList<Bonus> bonuses) {
		this.bonuses = bonuses;
	}
	
	public ArrayList<Bonus> getBonuses() {
		return this.bonuses;
	}
	
	// Parcelling part
	public Special(Parcel in){
		this.setCode(in.readString());
		this.setDateStart(new Date(in.readLong()));
		this.setDateEnd(new Date(in.readLong()));
		this.setCases(in.readInt());
		this.setDescription(in.readString());
		bonuses = new ArrayList<Bonus>();
		in.readTypedList(bonuses, Bonus.CREATOR);
	}

	@Override
	public int describeContents(){
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.getCode());
		dest.writeLong(this.getDateStart().getTime());
		dest.writeLong(this.getDateEnd().getTime());
		dest.writeInt(this.getCases());
		dest.writeString(this.getDescription());
		dest.writeTypedList(this.getBonuses());
	}
	
	public static final Parcelable.Creator<Special> CREATOR = new Parcelable.Creator<Special>() {
		public Special createFromParcel(Parcel in) {
			return new Special(in); 
		}

		public Special[] newArray(int size) {
			return new Special[size];
		}
	};
}
