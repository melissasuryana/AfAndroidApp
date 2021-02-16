package au.com.accountsflow.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import au.com.accountsflow.BaseProductsActivity;
import au.com.accountsflow.CoreAbstractActivity;
import au.com.accountsflow.ProductDetailActivity;
import au.com.accountsflow.R;
import au.com.accountsflow.arrayadapter.ExpandableProductAdapter;
import au.com.accountsflow.object.Product;
import au.com.accountsflow.object.ProductGroup;

import com.actionbarsherlock.view.MenuItem;

/**
 * Base fragment class for expandable list of products
 * @author melissa.suryana
 *
 */
public abstract class ExpandableProductsFragment extends CoreExpandableListFragment{	
    
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	protected ArrayList<ProductGroup> listGroups = null;
	
	protected abstract void setGroupListAdapter();
    
    protected abstract ArrayList<ProductGroup> getListGroups();
    
    protected abstract void launchActivity(Product selectedProduct);
    
    @Override
    protected void toggleActionItems(boolean enabled) {
		CoreAbstractActivity activity = (CoreAbstractActivity) this.getActivity();
		MenuItem refresh = (MenuItem) activity.getRefreshMenu();
        MenuItem search = (MenuItem) activity.getSearchMenu();
        
        if (refresh != null) {
        	refresh.setEnabled(enabled);
        }
    	if (search != null) {
    		search.setEnabled(enabled);
        }
    	
    	if (this.isLeftFragment()) {
    		BaseProductsActivity leftActivity = (BaseProductsActivity) this.getActivity();
    		MenuItem addOrder = (MenuItem) leftActivity.getAddOrderButton();
    		
    		if (addOrder != null) {
    			addOrder.setEnabled(enabled);
    		}
    	}
	}    
    
    public ArrayList<ProductGroup> convertHashmapToArrayList(HashMap<String, Product> products) {
    	HashMap<String, ProductGroup> productGroups = new HashMap<String, ProductGroup>();
    	Iterator<String> iter = products.keySet().iterator();
    	while(iter.hasNext()) {
	    	String key = iter.next();
	    	Product product = products.get(key);
	    	String category = product.getCategory();
	    	ProductGroup group = productGroups.get(category);
	    	
	    	if (! productGroups.containsKey(product.getCategory())) {
	    		group = new ProductGroup();
	    		group.setName(product.getCategory());
	    		group.setProducts(new ArrayList<Product>());
	    		productGroups.put(category, group);
	    	}
	    	
	    	ArrayList<Product> listProducts = group.getProducts();
	    	listProducts.add(product);
    	}
    	
    	ArrayList<ProductGroup> productGroupArray = new ArrayList<ProductGroup>(productGroups.values());
		Collections.sort(productGroupArray);
		
		return productGroupArray;
    }
    
	@Override
	public void onViewStateRestored (Bundle savedInstanceState){
		super.onViewStateRestored (savedInstanceState);
		
		if (this.listGroups == null || this.listGroups.isEmpty()) {
			ArrayList<ProductGroup> listGroups = this.getListGroups();
						
			this.listGroups = listGroups;			
			this.setGroupListAdapter();
		}
		else {
			toggleActionItems(true);
		}
		ExpandableListView listView = getExpandableListView();
		ExpandableProductAdapter adapter = (ExpandableProductAdapter) listView.getExpandableListAdapter();
		if (adapter != null) {
			for (int i = 0; i < adapter.getGroupCount(); i++) {
				listView.expandGroup(i);
			}
		}
		
		if (this.listGroups != null && !this.listGroups.isEmpty()) {
			Bundle args = this.getArguments();
			if (args != null && args.containsKey("childPosition") && args.containsKey("groupPosition")) {
				int groupPosition = args.getInt("groupPosition");
				int childPosition = args.getInt("childPosition");			
								
				updateRightFragment(groupPosition, childPosition);
			}
		}
		
	}	    
		
	public void updateRightFragment(int groupPosition, int childPosition) {
		ExpandableListView l = this.getExpandableListView();
		
		int index = l.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
		if (index >= 0) {
			l.setItemChecked(index, true);
		}
		
		CoreAbstractActivity activity = (CoreAbstractActivity) this.getActivity();		
		
		ExpandableListAdapter adapter = l.getExpandableListAdapter();
		Product selectedProduct = (Product) adapter.getChild(groupPosition, childPosition);
		
		// Do something with the data
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		Fragment productDetail = ProductDetailFragment.newInstance(selectedProduct);
		FragmentTransaction transaction = fragmentManager.beginTransaction();
        
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        
        transaction.add(R.id.right_fragment, productDetail, "productDetailTag");
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
	}
	
	public void onChildItemClick (ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		super.onChildItemClick(parent, v, groupPosition, childPosition, id);
		ExpandableListAdapter adapter = parent.getExpandableListAdapter();
		Product selectedProduct = (Product) adapter.getChild(groupPosition, childPosition);
		
		CoreAbstractActivity activity = (CoreAbstractActivity) this.getActivity();
		
		// if two-panel layout is enabled
		if (this.isTwoPanelLayout()) {			
	        
	        if (this.isLeftFragment()) {
	        	updateRightFragment(groupPosition, childPosition);
			}	
			else {
				launchActivity(selectedProduct);
			}
	        
		}
		else {
			Intent i = new Intent(activity, ProductDetailActivity.class);  
			Bundle args = new Bundle();
	        args.putParcelable("product", selectedProduct);
	        i.putExtra("supplierArgs", args);
		    startActivity(i);
		    //activity.finish(); 
		}
	}
	
	/**
	 * Function to refresh list of the suppliers
	 */
	/*public void refreshList()
	{		
		super.refreshList();
		mProductTask = new ListProductTask();
		mProductTask.execute((Void) null);
	}*/
}

