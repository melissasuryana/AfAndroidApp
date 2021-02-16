package au.com.accountsflow;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;
import au.com.accountsflow.object.BasketItem;
import au.com.accountsflow.object.Business;
import au.com.accountsflow.object.Favorite;
import au.com.accountsflow.object.OrderHistoryItem;
import au.com.accountsflow.object.OrderItem;
import au.com.accountsflow.object.Product;

/**
 * Core application class for Accounts Flow. 
 * Contains list of objects that needs to be shared among different fragments and activities.
 * @author melissa.suryana
 *
 */
public class AfApplication extends Application {
	
	private String loginEmail;
	private Business currentBusiness;
	private HashMap<String, OrderItem> currentOrder;
	private HashMap<String, OrderItem> savedOrderItems;
	private HashMap<String, Product> listProducts;
	private HashMap<String, Product> listFocusBrands;
	private HashMap<String, Product> listSpecials;
	private HashMap<String, Product> listQda;
	private HashMap<String, Product> listPromotions;
	private HashMap<Integer, Favorite> listFavorites;
	private ArrayList<OrderHistoryItem> orderHistoryItems;

	@Override
    public void onCreate() {
        resetData();
        super.onCreate();
    }
	
	/**
	 * Get current order items
	 * @return HashMap<String, OrderItem>
	 */
	public HashMap<String, OrderItem> getCurrentOrder() {
		return currentOrder;
	}

	/**
	 * Set current order items
	 * @param HashMap<String, OrderItem> currentOrder
	 */
	public void setCurrentOrder(HashMap<String, OrderItem> currentOrder) {
		this.currentOrder = currentOrder;
	}
	
	/**
	 * Set order item into current order list
	 * @param OrderItem item
	 */
	public void setOrderItem(OrderItem item) {
		this.currentOrder.put(item.getItem().getCode(), item);
	}
	
	/**
	 * Remove order item from the list
	 * @param String code
	 */
	public void removeOrderItem(String code) {
		this.currentOrder.remove(code);
	}
			
	/**
	 * Get order item from the list
	 * @param String code
	 * @return OrderItem
	 */
	public OrderItem getOrderItem(String code) {
		return currentOrder.get(code);
	}
	
	/**
	 * Get saved order items
	 * @return HashMap<String, OrderItem>
	 */
	public HashMap<String, OrderItem> getSavedOrderItems() {
		return savedOrderItems;
	}

	/**
	 * Set saved order items
	 * @param HashMap<String, OrderItem> savedOrderItems
	 */
	public void setSavedOrderItems(HashMap<String, OrderItem> savedOrderItems) {
		this.savedOrderItems = savedOrderItems;
	}
	
	/**
	 * Set saved order item into the list
	 * @param OrderItem item
	 */
	public void setSavedOrderItem(OrderItem item) {
		this.savedOrderItems.put(item.getItem().getCode(), item);
	}
	
	/**
	 * Remove an item from saved order list
	 * @param String code
	 */
	public void removeSavedOrderItem(String code) {
		this.savedOrderItems.remove(code);
	}
		
	/**
	 * Get saved order item from the list
	 * @param String code
	 * @return OrderItem
	 */
	public OrderItem getSavedOrderItem(String code) {
		return savedOrderItems.get(code);
	}

	/**
	 * Get product list
	 * @return HashMap<String, Product>
	 */
	public HashMap<String, Product> getListProducts() {
		return listProducts;
	}

	/**
	 * Set product list
	 * @param HashMap<String, Product> listProducts
	 */
	public void setListProducts(HashMap<String, Product> listProducts) {
		this.listProducts = listProducts;
	}
	
	/**
	 * Get focus brands list
	 * @return HashMap<String, Product>
	 */
	public HashMap<String, Product> getFocusBrands() {
		return listFocusBrands;
	}

	/**
	 * Set focus brands list
	 * @param HashMap<String, Product> focusBrands
	 */
	public void setFocusBrands(HashMap<String, Product> focusBrands) {
		this.listFocusBrands = focusBrands;
	}
	
	/**
	 * Get favorite list for all suppliers
	 * @return HashMap<Integer, Favorite>
	 */
	public HashMap<Integer, Favorite> getListFavorites() {
		return listFavorites;
	}
	
	/**
	 * Get favorite list
	 * @param int supplierId
	 * @return Favorite
	 */
	public Favorite getFavorites(int supplierId) {
		if (listFavorites != null) {
			return listFavorites.get(supplierId);
		}
		return null;
	}

	/**
	 * Set favorite list
	 * @param HashMap<Integer, Favorite> listFavorites
	 */
	public void setListFavorites(HashMap<Integer, Favorite> listFavorites) {
		this.listFavorites = listFavorites;
	}

	/**
	 * Reset all lists to be empty
	 */
	public void resetData() {
		this.currentBusiness = null;
		this.currentOrder = new HashMap<String, OrderItem>();
		this.savedOrderItems = null;
        this.listProducts = new HashMap<String, Product>();
        this.listFocusBrands = new HashMap<String, Product>();
        this.listSpecials = new HashMap<String, Product>();
        this.listQda = new HashMap<String, Product>();
        this.listPromotions = new HashMap<String, Product>();
        this.orderHistoryItems = new ArrayList<OrderHistoryItem>();
        this.listFavorites = new HashMap<Integer, Favorite>();
	}

	/**
	 * Get currently selected business
	 * @return Business
	 */
	public Business getCurrentBusiness() {
		return currentBusiness;
	}

	/**
	 * Set currently selected business
	 * @param Business currentBusiness
	 */
	public void setCurrentBusiness(Business currentBusiness) {
		this.currentBusiness = currentBusiness;
	}

	/**
	 * Get order history items
	 * @return ArrayList<OrderHistoryItem>
	 */
	public ArrayList<OrderHistoryItem> getOrderHistoryItems() {
		return orderHistoryItems;
	}

	/**
	 * Set order history items
	 * @param ArrayList<OrderHistoryItem> orderHistoryItems
	 */
	public void setOrderHistoryItems(ArrayList<OrderHistoryItem> orderHistoryItems) {
		this.orderHistoryItems = orderHistoryItems;
	}

	/**
	 * Get list of specials
	 * @return HashMap<String, Product>
	 */
	public HashMap<String, Product> getListSpecials() {
		return listSpecials;
	}

	/**
	 * Set list of specials
	 * @param HashMap<String, Product> listSpecials
	 */
	public void setListSpecials(HashMap<String, Product> listSpecials) {
		this.listSpecials = listSpecials;
	}

	/**
	 * Get list of QDA
	 * @return HashMap<String, Product>
	 */
	public HashMap<String, Product> getListQda() {
		return listQda;
	}

	/**
	 * Set list of QDA
	 * @param HashMap<String, Product> listQda
	 */
	public void setListQda(HashMap<String, Product> listQda) {
		this.listQda = listQda;
	}

	/**
	 * Get list of promotions
	 * @return HashMap<String, Product>
	 */
	public HashMap<String, Product> getListPromotions() {
		return listPromotions;
	}

	/**
	 * Set list of promotions
	 * @param HashMap<String, Product> listPromotions
	 */
	public void setListPromotions(HashMap<String, Product> listPromotions) {
		this.listPromotions = listPromotions;
	}

	/**
	 * Get email address used for log in
	 * @return String 
	 */
	public String getLoginEmail() {
		return loginEmail;
	}

	/**
	 * Set email address used for log in
	 * @param loginEmail
	 */
	public void setLoginEmail(String loginEmail) {
		this.loginEmail = loginEmail;
	}
	
	
}
