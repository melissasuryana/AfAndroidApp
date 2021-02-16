package au.com.accountsflow.fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ListView;
import au.com.accountsflow.AfApplication;
import au.com.accountsflow.AfHelper;
import au.com.accountsflow.AfXmlParser;
import au.com.accountsflow.CoreAbstractActivity;
import au.com.accountsflow.CurrentOrderActivity;
import au.com.accountsflow.FocusBrandActivity;
import au.com.accountsflow.OrderHistoryActivity;
import au.com.accountsflow.OrderedBeforeActivity;
import au.com.accountsflow.ProductsListActivity;
import au.com.accountsflow.PromotionsActivity;
import au.com.accountsflow.QdaActivity;
import au.com.accountsflow.R;
import au.com.accountsflow.SpecialsActivity;
import au.com.accountsflow.SupplierMenuActivity;
import au.com.accountsflow.arrayadapter.SupplierMenuAdapter;
import au.com.accountsflow.object.Discount;
import au.com.accountsflow.object.Price;
import au.com.accountsflow.object.Product;
import au.com.accountsflow.object.Special;
import au.com.accountsflow.object.SupplierMenuItem;

import com.actionbarsherlock.view.MenuItem;

public class SupplierMenuFragment extends CoreListFragment{
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private ListProductTask mProductTask = null;
	private ListPricesTask mPricesTask = null;
	private ListSpecialTask mSpecialsTask = null;
	private ListDiscountsTask mDiscountsTask = null;
	private HashMap<String, Product> listProducts = null;
	
	ArrayList<String> menus;
	
	/**
     * Create a new instance of SupplierMenuFragment
     */
    public static SupplierMenuFragment newInstance(String businessName, int businessId, int priceListId) {
    	SupplierMenuFragment f = new SupplierMenuFragment();

        // Supply string input as an argument.
        Bundle args = new Bundle();
        args.putString("businessName", businessName);
        args.putInt("businessId", businessId);
        args.putInt("priceListId", priceListId);
        f.setArguments(args);

        return f;
    }
        
    public String getEmptyListMessage() {
		return "";
	}
    
    protected void setSupplierMenuAdapter() {
    	setListAdapter(createMenuAdapter(this.getActivity(), R.layout.suppliers_menu));
    	
    	Bundle args = this.getArguments();
		if (args != null && args.containsKey("selectedPosition")) {
			int selectedPosition = this.getArguments().getInt("selectedPosition");
			
			this.setSelection(selectedPosition);
			
			updateRightFragment(selectedPosition);
		}
    }
    
    @Override
    public void onViewStateRestored (Bundle savedInstanceState){
		super.onViewStateRestored (savedInstanceState);
				        
		if (this.listProducts == null || this.listProducts.isEmpty()) {
			AfApplication application = (AfApplication) this.getActivity().getApplication();
			HashMap<String, Product> listProducts = application.getListProducts();
			if (listProducts == null || listProducts.isEmpty()) {
				mProductTask = new ListProductTask();
				mProductTask.execute((Void) null);
				toggleActionItems(false);
			}
			else {
				this.listProducts = listProducts;			
				setSupplierMenuAdapter();
			}
		}
		else {
			setSupplierMenuAdapter();
		}
	}
    	
	public void updateRightFragment(int leftPosition) {
		ListView l = this.getListView();
		l.setItemChecked(leftPosition, true);
		SupplierMenuItem menu = (SupplierMenuItem) l.getItemAtPosition(leftPosition);
		
		CoreAbstractActivity activity = (CoreAbstractActivity) this.getActivity();
		
		// Do something with the data
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		
		SupplierMenuActivity supplierMenuActivity = (SupplierMenuActivity) activity;
		MenuItem addProductButton = supplierMenuActivity.getAddProductButton();
		MenuItem refreshButton = activity.getRefreshMenu();
		MenuItem searchButton = activity.getSearchMenu();
		
		if (searchButton != null) {
			searchButton.setVisible(true);
		}
		if (refreshButton != null) {
			refreshButton.setVisible(true);
		}
		if (addProductButton != null) {
			addProductButton.setVisible(false);
		}
		
		if (menu.getCode() == "product_list") {			
			Fragment productList = new ProductsListFragment();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
	        
	        // Replace whatever is in the fragment_container view with this fragment,
	        // and add the transaction to the back stack
	        
	        transaction.replace(R.id.right_fragment, productList, "productListTag");
	        transaction.addToBackStack(null);
	
	        // Commit the transaction
	        transaction.commit();
		}
		else if(menu.getCode() == "current_order") {
			if (searchButton != null) {
				searchButton.setVisible(false);
			}
			if (refreshButton != null) {
				refreshButton.setVisible(false);
			}
			if (addProductButton != null) {
				addProductButton.setVisible(true);
			}
			Fragment currentOrder = new CurrentOrderFragment();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
	        
	        // Replace whatever is in the fragment_container view with this fragment,
	        // and add the transaction to the back stack
	        
	        transaction.replace(R.id.right_fragment, currentOrder, "currentOrderTag");
	        transaction.addToBackStack(null);
	
	        // Commit the transaction
	        transaction.commit();
		}
		else if(menu.getCode() == "order_history") {
			Fragment orderHistory = new OrderHistoryFragment();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
	        
	        // Replace whatever is in the fragment_container view with this fragment,
	        // and add the transaction to the back stack
	        
	        transaction.replace(R.id.right_fragment, orderHistory, "orderHistoryTag");
	        transaction.addToBackStack(null);
	
	        // Commit the transaction
	        transaction.commit();
		}
		else if(menu.getCode() == "promotions") {
			Fragment promotions = new PromotionsFragment();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
	        
	        // Replace whatever is in the fragment_container view with this fragment,
	        // and add the transaction to the back stack
	        
	        transaction.replace(R.id.right_fragment, promotions, "promotionsTag");
	        transaction.addToBackStack(null);
	
	        // Commit the transaction
	        transaction.commit();
		}
		else if(menu.getCode() == "specials") {
			Fragment specials = new SpecialsFragment();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
	        
	        // Replace whatever is in the fragment_container view with this fragment,
	        // and add the transaction to the back stack
	        
	        transaction.replace(R.id.right_fragment, specials, "specialsTag");
	        transaction.addToBackStack(null);
	
	        // Commit the transaction
	        transaction.commit();
		}
		else if(menu.getCode() == "qda_search") {
			Fragment qda = new QdaFragment();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
	        
	        // Replace whatever is in the fragment_container view with this fragment,
	        // and add the transaction to the back stack
	        
	        transaction.replace(R.id.right_fragment, qda, "qdaTag");
	        transaction.addToBackStack(null);
	
	        // Commit the transaction
	        transaction.commit();
		}
		else if(menu.getCode() == "focus_brands") {
			Fragment qda = new FocusBrandFragment();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
	        
	        // Replace whatever is in the fragment_container view with this fragment,
	        // and add the transaction to the back stack
	        
	        transaction.replace(R.id.right_fragment, qda, "focusBrandTag");
	        transaction.addToBackStack(null);
	
	        // Commit the transaction
	        transaction.commit();
		}
		else if(menu.getCode() == "ordered_before") {
			Fragment fragment = new OrderedBeforeFragment();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
	        
	        // Replace whatever is in the fragment_container view with this fragment,
	        // and add the transaction to the back stack
	        
	        transaction.replace(R.id.right_fragment, fragment, "orderedBeforeTag");
	        transaction.addToBackStack(null);
	
	        // Commit the transaction
	        transaction.commit();
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		SupplierMenuItem menu = (SupplierMenuItem) l.getItemAtPosition(position);
		CoreAbstractActivity activity = (CoreAbstractActivity) this.getActivity();
		
		int businessId = this.getArguments().getInt("businessId");
		int priceListId = this.getArguments().getInt("priceListId");
		
		if (menu.getCode() == "quick_order") {
			QuickOrderDialog orderDialog = new QuickOrderDialog();
			orderDialog.show(this.getActivity().getSupportFragmentManager(), "quick_order");
			return;
		}
		
		// if two-panel layout is enabled
		if (this.isTwoPanelLayout()) {	
			if (this.isLeftFragment()) {
				updateRightFragment(position);
			}	
			else {
				Intent i = new Intent(activity, SupplierMenuActivity.class);  
				Bundle args = new Bundle();	 		
		        args.putInt("selectedPosition", position);
		        args.putString("businessName", this.getArguments().getString("businessName"));
		        args.putInt("businessId", this.getArguments().getInt("businessId"));
		        args.putInt("priceListId", this.getArguments().getInt("priceListId"));
				i.putExtra("supplierArgs", args);
			    startActivity(i);
			}
		}
		else {
			if (menu.getCode() == "product_list") {
				Intent i = new Intent(activity, ProductsListActivity.class);  
				Bundle args = new Bundle();	 		
		        args.putInt("supplierId", businessId);
		        args.putInt("priceListId", priceListId);
				i.putExtra("supplierArgs", args);
			    startActivity(i);
			}
			else if (menu.getCode() == "current_order") {
				Intent i = new Intent(activity, CurrentOrderActivity.class);  
				Bundle args = new Bundle();	
				i.putExtra("supplierArgs", args);
			    startActivity(i);
			}
			else if (menu.getCode() == "order_history") {
				Intent i = new Intent(activity, OrderHistoryActivity.class);  
				Bundle args = new Bundle();	
				i.putExtra("supplierArgs", args);
			    startActivity(i);
			}
			else if (menu.getCode() == "promotions") {
				Intent i = new Intent(activity, PromotionsActivity.class);  
				Bundle args = new Bundle();	
				i.putExtra("supplierArgs", args);
			    startActivity(i);
			}
			else if (menu.getCode() == "specials") {
				Intent i = new Intent(activity, SpecialsActivity.class);  
				Bundle args = new Bundle();	
				i.putExtra("supplierArgs", args);
			    startActivity(i);
			}
			else if (menu.getCode() == "qda_search") {
				Intent i = new Intent(activity, QdaActivity.class);  
				Bundle args = new Bundle();	
				i.putExtra("supplierArgs", args);
			    startActivity(i);
			}
			else if (menu.getCode() == "focus_brands") {
				Intent i = new Intent(activity, FocusBrandActivity.class);  
				Bundle args = new Bundle();	
				i.putExtra("supplierArgs", args);
			    startActivity(i);
			}
			else if (menu.getCode() == "ordered_before") {
				Intent i = new Intent(activity, OrderedBeforeActivity.class);  
				Bundle args = new Bundle();	
				i.putExtra("supplierArgs", args);
			    startActivity(i);
			}
		}
	}
	
	public SupplierMenuAdapter createMenuAdapter(Context context, int textViewResId) {
        ArrayList<SupplierMenuItem> items = new ArrayList<SupplierMenuItem>();
        
        SupplierMenuItem quickOrder = new SupplierMenuItem("quick_order", getString(R.string.quick_order_title));
        items.add(quickOrder);
        
        SupplierMenuItem promotions = new SupplierMenuItem("promotions", getString(R.string.title_product_promotions));
        items.add(promotions);
        
        SupplierMenuItem specials = new SupplierMenuItem("specials", getString(R.string.title_product_specials));
        items.add(specials);
        
        SupplierMenuItem focusBrands = new SupplierMenuItem("focus_brands", getString(R.string.title_focus_brand));
        items.add(focusBrands);
        
        SupplierMenuItem qdaSearch = new SupplierMenuItem("qda_search", getString(R.string.title_product_qda));
        items.add(qdaSearch);
        
        SupplierMenuItem orderedBefore = new SupplierMenuItem("ordered_before", getString(R.string.title_ordered_before));
        items.add(orderedBefore);
        
        SupplierMenuItem productList = new SupplierMenuItem("product_list", getString(R.string.title_product_list));
        items.add(productList);
        
        SupplierMenuItem currentOrder = new SupplierMenuItem("current_order", getString(R.string.current_order_title));
        items.add(currentOrder);
        
        SupplierMenuItem orderHistory = new SupplierMenuItem("order_history", getString(R.string.title_order_history));
        items.add(orderHistory);
        
        return new SupplierMenuAdapter(context, SupplierMenuFragment.this, textViewResId, items);
    }
	
	/**
	 * Represents an asynchronous task used to fetch list of relevant businesses
	 */
	public class ListProductTask extends AsyncTask<Void, Void, HashMap<String, Product> > {
		@Override
		protected HashMap<String, Product> doInBackground(Void... params) {
			AfApplication application = (AfApplication) SupplierMenuFragment.this.getActivity().getApplication();
			int supplierId = application.getCurrentBusiness().getId();
			String generalError = getString(R.string.error_list_products);			           
			HttpParams httpParams = new BasicHttpParams();

			try {	
				String url = getString(R.string.retailer_products_api) + "/" + supplierId;
				HttpResponse response = AfHelper.sendGetRequest(getResources(), getActivity(), url, httpParams, generalError);

				InputStream inputStream = response.getEntity().getContent();
				AfXmlParser xmlParser = new AfXmlParser();

				return xmlParser.parseProducts(inputStream);

			} catch (Exception e) {

				return null;

			}
		}

		@Override
		protected void onPostExecute(final HashMap<String, Product> response) {
			mProductTask = null;
			if (response == null) {
				String generalError = getString(R.string.error_list_products);
				AfHelper.showAlert(getActivity(), generalError);
				
				return;
			}
			else {
				listProducts = response;
				mPricesTask = new ListPricesTask();
				mPricesTask.execute((Void) null);
				toggleActionItems(false);
			}
		}

		@Override
		protected void onCancelled() {
			mProductTask = null;			
		}

	}
	
	/**
	 * Represents an asynchronous task used to fetch list of relevant businesses
	 */
	public class ListPricesTask extends AsyncTask<Void, Void, ArrayList<Price> > {
		@Override
		protected ArrayList<Price> doInBackground(Void... params) {
			AfApplication application = (AfApplication) SupplierMenuFragment.this.getActivity().getApplication();
			int priceListId = application.getCurrentBusiness().getSubBusiness().getPriceListId();
			String generalError = getString(R.string.error_list_prices);			           
			HttpParams httpParams = new BasicHttpParams();

			try {	
				String url = getString(R.string.retailer_prices_api) + "/" + priceListId;
				HttpResponse response = AfHelper.sendGetRequest(getResources(), getActivity(), url, httpParams, generalError);

				InputStream inputStream = response.getEntity().getContent();
				AfXmlParser xmlParser = new AfXmlParser();

				return xmlParser.parsePrices(inputStream);

			} catch (Exception e) {

				return null;

			}
		}

		@Override
		protected void onPostExecute(final ArrayList<Price> response) {
			mPricesTask = null;
			if (response == null) {
				String generalError = getString(R.string.error_list_prices);
				AfHelper.showAlert(getActivity(), generalError);
			}
			else {
				HashMap<String, Product> focusBrands = new HashMap<String, Product>();
				for (int i = 0; i < response.size(); i++) {
					Price p = response.get(i);
					Product product = listProducts.get(p.getCode());
					product.setPrice(p);
					if (p.getIsFocusBrand()) {
						focusBrands.put(p.getCode(), product);
					}
				}
				AfApplication application = (AfApplication) SupplierMenuFragment.this.getActivity().getApplication();
				application.setListProducts(listProducts);
				application.setFocusBrands(focusBrands);
				mSpecialsTask = new ListSpecialTask();
				mSpecialsTask.execute((Void) null);
				toggleActionItems(false);
			}
		}

		@Override
		protected void onCancelled() {
			mPricesTask = null;			
		}

	}
	
	/**
	 * Represents an asynchronous task used to fetch list of relevant businesses
	 */
	public class ListSpecialTask extends AsyncTask<Void, Void, ArrayList<Special> > {
		@Override
		protected ArrayList<Special> doInBackground(Void... params) {
			AfApplication application = (AfApplication) SupplierMenuFragment.this.getActivity().getApplication();
			int priceListId = application.getCurrentBusiness().getSubBusiness().getPriceListId();
			String generalError = getString(R.string.error_list_specials);			           
			HttpParams httpParams = new BasicHttpParams();

			try {	
				String url = getString(R.string.retailer_promotions_api) + "/" + priceListId;
				HttpResponse response = AfHelper.sendGetRequest(getResources(), getActivity(), url, httpParams, generalError);

				InputStream inputStream = response.getEntity().getContent();
				AfXmlParser xmlParser = new AfXmlParser();

				return xmlParser.parseSpecials(inputStream);

			} catch (Exception e) {

				return null;

			}
		}

		@Override
		protected void onPostExecute(final ArrayList<Special> response) {
			mSpecialsTask = null;
			if (response == null) {
				String generalError = getString(R.string.error_list_specials);
				AfHelper.showAlert(getActivity(), generalError);
			}
			else {
				HashMap<String, Product> specials = new HashMap<String, Product>();
				
				for (int i = 0; i < response.size(); i++) {
					Special s = response.get(i);
					Product p = listProducts.get(s.getCode());
					p.addSpecial(s);
					specials.put(s.getCode(), p);
				}
				AfApplication application = (AfApplication) SupplierMenuFragment.this.getActivity().getApplication();
				application.setListProducts(listProducts);
				application.setListSpecials(specials);
				
				mDiscountsTask = new ListDiscountsTask();
				mDiscountsTask.execute((Void) null);
				toggleActionItems(false);
			}
		}

		@Override
		protected void onCancelled() {
			mSpecialsTask = null;			
		}

	}
	
	/**
	 * Represents an asynchronous task used to fetch list of relevant businesses
	 */
	public class ListDiscountsTask extends AsyncTask<Void, Void, ArrayList<Discount> > {
		@Override
		protected ArrayList<Discount> doInBackground(Void... params) {
			AfApplication application = (AfApplication) SupplierMenuFragment.this.getActivity().getApplication();
			int priceListId = application.getCurrentBusiness().getSubBusiness().getPriceListId();
			String generalError = getString(R.string.error_list_discounts);			           
			HttpParams httpParams = new BasicHttpParams();

			try {	
				String url = getString(R.string.retailer_discounts_api) + "/" + priceListId;
				HttpResponse response = AfHelper.sendGetRequest(getResources(), getActivity(), url, httpParams, generalError);

				InputStream inputStream = response.getEntity().getContent();
				AfXmlParser xmlParser = new AfXmlParser();

				return xmlParser.parseDiscounts(inputStream);

			} catch (Exception e) {

				return null;

			}
		}

		@Override
		protected void onPostExecute(final ArrayList<Discount> response) {
			mDiscountsTask = null;
			if (response == null) {
				String generalError = getString(R.string.error_list_discounts);
				AfHelper.showAlert(getActivity(), generalError);
			}
			else {
				HashMap<String, Product> promo = new HashMap<String, Product>();
				HashMap<String, Product> qda = new HashMap<String, Product>();
				
				for (int i = 0; i < response.size(); i++) {					
					Discount d = response.get(i);
					Product p = listProducts.get(d.getCode());
					d.setTaxString(p.getPrice().getTaxRateString());					
					
					if (d.isPromo()) {
						p.setPromotion(d);
						promo.put(d.getCode(), p);
					}
					else if (d.isQda()) {
						p.addQda(d);
						qda.put(d.getCode(), p);
					}
				}
				AfApplication application = (AfApplication) SupplierMenuFragment.this.getActivity().getApplication();
				application.setListProducts(listProducts);
				application.setListPromotions(promo);
				application.setListQda(qda);
				setSupplierMenuAdapter();
			}
		}

		@Override
		protected void onCancelled() {
			mDiscountsTask = null;			
		}

	}
	
	/**
	 * Function to refresh list of the suppliers
	 */
	public void refreshList()
	{		
		super.refreshList();
		mProductTask = new ListProductTask();
		mProductTask.execute((Void) null);
	}
	
	public void cancelAllRequests(){
		if (mProductTask != null ) {
			mProductTask.cancel(true);
		}		
		if (mPricesTask != null ) {
			mPricesTask.cancel(true);
		}
		if (mSpecialsTask != null ) {
			mSpecialsTask.cancel(true);
		}
		if (mDiscountsTask != null ) {
			mDiscountsTask.cancel(true);
		}
	}
}
