package au.com.accountsflow;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import au.com.accountsflow.CoreAbstractActivity.InstantiatingTabListener;
import au.com.accountsflow.compatibility.TabHelper;
import au.com.accountsflow.fragment.OrderDialogFragment;
import au.com.accountsflow.fragment.ProductDetailFragment;
import au.com.accountsflow.fragment.SupplierListFragment;
import au.com.accountsflow.object.Product;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * Saved order item detail information activity
 * @author melissa.suryana
 *
 */
public class SavedOrderDetailActivity extends CoreAbstractActivity {

	public boolean displayHomeAsUp() {
		return true;
	}
	
	protected void onCreateTabs(Class<? extends Fragment> supplierClass, Bundle supplierArgs, 
    		Class<? extends Fragment> orderClass, Bundle orderArgs) {
    	ActionBar actionBar = getSupportActionBar();
    	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        TabHelper tabHelper = getTabHelper();
        
        supplierTab = tabHelper.newTab("supplierTab")
                .setText(R.string.tab_suppliers)
                .setIcon(R.drawable.ic_suppliers_white)
                .setTabListener(new InstantiatingTabListener(this, supplierClass, supplierArgs));
        tabHelper.addTab(supplierTab, 0, false);
        
        orderTab = tabHelper.newTab("orderTab")
                .setText(R.string.tab_orders)
                .setIcon(R.drawable.ic_orders_white)
                .setTabListener(new InstantiatingTabListener(this, orderClass, orderArgs));
        tabHelper.addTab(orderTab, 1, true);
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
		return SupplierListFragment.class;
	}
	
	public Class<? extends Fragment> getOrderTabClass() {
		return ProductDetailFragment.class;
	}
	
	public Product getSelectedProduct() {
		Bundle args = this.getSupplierTabArgs();
		
		Product product = (Product) args.getParcelable("product");
		
		return product;
	}			
}
