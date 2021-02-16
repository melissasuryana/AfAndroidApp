package au.com.accountsflow;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import au.com.accountsflow.fragment.OrderDialogFragment;
import au.com.accountsflow.fragment.SavedOrdersFragment;
import au.com.accountsflow.fragment.ProductDetailFragment;
import au.com.accountsflow.object.Product;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * Product detailed information screen activity
 * @author melissa.suryana
 *
 */
public class ProductDetailActivity extends CoreAbstractActivity {

	public boolean displayHomeAsUp() {
		return true;
	}
	
	public String getActivityTitle() {
		return getString(R.string.title_product_detail);
	}
	
	public boolean isSearchVisible() {
		return false;
	}
	
	public boolean isRefreshVisible() {
		return false;
	}	
	
	public Class<? extends Fragment> getSupplierTabClass() {
		return ProductDetailFragment.class;
	}
	
	public Class<? extends Fragment> getOrderTabClass() {
		return SavedOrdersFragment.class;
	}
	
	public Product getSelectedProduct() {
		Bundle args = this.getSupplierTabArgs();
		
		Product product = (Product) args.getParcelable("product");
		
		return product;
	}
	
	public void onAddOrderClicked(MenuItem item) {
		Product product = this.getSelectedProduct();
		OrderDialogFragment orderDialog = OrderDialogFragment.newInstance(product);
		orderDialog.show(getSupportFragmentManager(), "add_order");
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
    	if (! this.isTwoPanelLayout()) {
    		this.addOrderButton.setVisible(true);
    	}
        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return result;
    }
}
