package au.com.accountsflow;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.view.View;
import au.com.accountsflow.fragment.ExpandableListFragment;
import au.com.accountsflow.fragment.OrderDialogFragment;
import au.com.accountsflow.fragment.ProductDetailFragment;
import au.com.accountsflow.fragment.SavedOrderItemsFragment;
import au.com.accountsflow.object.Product;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public abstract class BaseProductsActivity extends CoreAbstractActivity {
	
	public boolean displayHomeAsUp() {
		return true;
	}	
	
	public boolean isSearchVisible() {
		return true;
	}
	
	public boolean isRefreshVisible() {
		return false;
	}
			
	public void onAddOrderClicked(MenuItem item) {
		ProductDetailFragment fragment = (ProductDetailFragment)this.getRightFragment();
		Product product = (Product) fragment.getProduct();
		
		if (product != null) {
			OrderDialogFragment orderDialog = OrderDialogFragment.newInstance(product);
			orderDialog.show(getSupportFragmentManager(), "add_order");
		}
	}
	
	public MenuItem getAddOrderButton() {
		return this.addOrderButton;
	}
	
	/**
     * Base action bar-aware implementation for
     * {@link Activity#onCreateOptionsMenu(android.view.Menu)}.
     *
     * Note: marking menu items as invisible/visible is not currently supported.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean result = super.onCreateOptionsMenu(menu);
    	if (this.isTwoPanelLayout()) {
	     	this.addOrderButton.setVisible(true);
	     	
	     	if (this.getLeftFragment() instanceof ListFragment) {
	     		ListFragment fragment = (ListFragment)this.getLeftFragment();
	     		if (fragment.getListView().getCount() <= 0) {
		     		this.addOrderButton.setEnabled(false);
		     	}
		     	else {
		     		this.addOrderButton.setEnabled(true);
		     	}
	     	}
	     	else if (this.getLeftFragment() instanceof ExpandableListFragment) {
	     		ExpandableListFragment fragment = (ExpandableListFragment)this.getLeftFragment();
	     		if (fragment.getExpandableListView().getCount() <= 0) {
		     		this.addOrderButton.setEnabled(false);
		     	}
		     	else {
		     		this.addOrderButton.setEnabled(true);
		     	}
	     	}
    	}
        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return result;
    }
    
    public void progressOrder(View view) {
		SavedOrderItemsFragment fragment = (SavedOrderItemsFragment) this.getRightFragment();
		fragment.progressOrder(view);
	}
}
