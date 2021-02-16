package au.com.accountsflow;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import au.com.accountsflow.fragment.OrderDialogFragment;
import au.com.accountsflow.fragment.OrderHistoryDetailFragment;
import au.com.accountsflow.fragment.SavedOrdersFragment;
import au.com.accountsflow.object.Product;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * Order history item detail activity
 * @author melissa.suryana
 *
 */
public class OrderHistoryDetailActivity extends CoreAbstractActivity {

	public boolean displayHomeAsUp() {
		return true;
	}
	
	public String getActivityTitle() {
		return getString(R.string.title_order_history_details);
	}
	
	public boolean isSearchVisible() {
		return false;
	}
	
	public boolean isRefreshVisible() {
		return false;
	}	
	
	public Class<? extends Fragment> getSupplierTabClass() {
		return OrderHistoryDetailFragment.class;
	}
	
	public Class<? extends Fragment> getOrderTabClass() {
		return SavedOrdersFragment.class;
	}
	
}
