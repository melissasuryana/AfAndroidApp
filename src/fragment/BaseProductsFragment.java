package au.com.accountsflow.fragment;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ListView;
import au.com.accountsflow.BaseProductsActivity;
import au.com.accountsflow.CoreAbstractActivity;
import au.com.accountsflow.ProductDetailActivity;
import au.com.accountsflow.R;
import au.com.accountsflow.object.Product;

import com.actionbarsherlock.view.MenuItem;

/**
 * Fragment class for any product list fragments
 * @author melissa.suryana
 *
 */
public abstract class BaseProductsFragment extends CoreListFragment{	
    	
	protected HashMap<String, Product> listProducts = null;
	
	protected abstract void setProductListAdapter();
    
    protected abstract HashMap<String, Product> getListProducts();
    
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
    
	@Override
	public void onViewStateRestored (Bundle savedInstanceState){
		super.onViewStateRestored (savedInstanceState);
		
		if (this.listProducts == null || this.listProducts.isEmpty()) {
			HashMap<String, Product> listProducts = this.getListProducts();
						
			this.listProducts = listProducts;			
			this.setProductListAdapter();
		}
		
	}	
    	
	/**
	 * Change displayed right fragment
	 * @param leftPosition
	 */
	public void updateRightFragment(int leftPosition) {
		getListView().setItemChecked(leftPosition, true);
		
		CoreAbstractActivity activity = (CoreAbstractActivity) this.getActivity();
		
		ListView l = this.getListView();
		Product selectedProduct = (Product) l.getItemAtPosition(leftPosition);
		
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
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Product selectedProduct = (Product) l.getItemAtPosition(position);
		
		CoreAbstractActivity activity = (CoreAbstractActivity) this.getActivity();
		
		// if two-panel layout is enabled
		if (this.isTwoPanelLayout()) {			
	        
	        if (this.isLeftFragment()) {
	        	updateRightFragment(position);
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

