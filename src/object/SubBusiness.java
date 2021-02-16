package au.com.accountsflow.object;

import java.net.URL;

public class SubBusiness {
	private int id;
	private int businessId;
	private int wholesalerId;
	private int priceListId;
	private String accountCode;
	
	public SubBusiness(int businessId, int wholesalerId, int priceListId, String accountCode) {
		// this.setId(id);
		this.setBusinessId(businessId);
		this.setWholesalerId(wholesalerId);
		this.setPriceListId(priceListId);
		this.setAccountCode(accountCode);
	}
	
	
	public void setId(int id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public int getBusinessId() {
		return businessId;
	}


	public void setBusinessId(int businessId) {
		this.businessId = businessId;
	}


	public int getWholesalerId() {
		return wholesalerId;
	}

	public void setWholesalerId(int wholesalerId) {
		this.wholesalerId = wholesalerId;
	}

	public int getPriceListId() {
		return priceListId;
	}

	public void setPriceListId(int priceListId) {
		this.priceListId = priceListId;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
}
