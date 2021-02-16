package au.com.accountsflow;

import android.support.v4.app.Fragment;
import android.view.View;
import au.com.accountsflow.fragment.SavedOrderItemsFragment;
import au.com.accountsflow.fragment.SavedOrdersFragment;
import au.com.accountsflow.fragment.SupplierListFragment;

/**
 * Activity to list suppliers
 * @author melissa.suryana
 *
 */
public class SupplierListActivity extends CoreAbstractActivity {
	
	public boolean displayHomeAsUp() {
		return false;
	}
	
	public String getActivityTitle() {
		// Set title
        if (getResources().getBoolean(R.bool.use_live)) {
        	return getString(R.string.app_name);
        }
        else {
        	return getString(R.string.app_name_demo);
        }
	}
	
	public boolean isSearchVisible() {
		return true;
	}
	
	public boolean isRefreshVisible() {
		return true;
	}
	
	public Class<? extends Fragment> getSupplierTabClass() {
		return SupplierListFragment.class;
	}
	
	public Class<? extends Fragment> getOrderTabClass() {
		return SavedOrdersFragment.class;
	}	
}
