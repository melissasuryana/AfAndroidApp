package au.com.accountsflow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import au.com.accountsflow.fragment.OrderHistoryItemsFragment;
import au.com.accountsflow.fragment.SavedOrdersFragment;

/**
 * Activity for listing order history items
 * @author melissa.suryana
 *
 */
public class OrderHistoryItemsActivity extends CoreAbstractActivity {
	
	public boolean displayHomeAsUp() {
		return true;
	}
	
	public String getActivityTitle() {
		Bundle data = getIntent().getExtras();
        Bundle supplierArgs = data.getBundle("supplierArgs");
        if (supplierArgs != null) {
        	return supplierArgs.getString("orderName");
        }
		return getString(R.string.title_order_history);
	}
	
	public boolean isSearchVisible() {
		return false;
	}
	
	public boolean isRefreshVisible() {
		return false;
	}
	
	public Class<? extends Fragment> getSupplierTabClass() {
		return OrderHistoryItemsFragment.class;
	}
	
	public Class<? extends Fragment> getOrderTabClass() {
		return SavedOrdersFragment.class;
	}
}
