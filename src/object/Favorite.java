package au.com.accountsflow.object;

import java.util.ArrayList;

public class Favorite {
	private int supplierId;
	private int retailerId;
	private ArrayList<String> productCodes;
	
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
	public ArrayList<String> getProductCodes() {
		return productCodes;
	}
	public void setProductCodes(ArrayList<String> productCodes) {
		this.productCodes = productCodes;
	}
	
	
}
