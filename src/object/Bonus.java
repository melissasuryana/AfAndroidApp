package au.com.accountsflow.object;

import android.os.Parcel;
import android.os.Parcelable;

public class  Bonus implements Parcelable{
	private String code;
	private int cases;
	
	public Bonus(){}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getCases() {
		return cases;
	}
	public void setCases(int cases) {
		this.cases = cases;
	}
	
	// Parcelling part
	public Bonus(Parcel in){
		this.setCode(in.readString());
		this.setCases(in.readInt());
	}

	@Override
	public int describeContents(){
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.getCode());
		dest.writeInt(this.getCases());
	}
	
	public static final Bonus.Creator<Bonus> CREATOR = new Parcelable.Creator<Bonus>() {
		public Bonus createFromParcel(Parcel in) {
			return new Bonus(in); 
		}

		public Bonus[] newArray(int size) {
			return new Bonus[size];
		}
	};

};
