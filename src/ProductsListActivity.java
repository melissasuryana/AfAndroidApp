package au.com.accountsflow;

import android.support.v4.app.Fragment;
import au.com.accountsflow.fragment.SavedOrdersFragment;
import au.com.accountsflow.fragment.ProductsListFragment;

/**
 * Product list screen activity
 * @author melissa.suryana
 *
 */
public class ProductsListActivity extends BaseProductsActivity {
		
	public String getActivityTitle() {
		return getString(R.string.title_product_list);
	}
			
	public Class<? extends Fragment> getSupplierTabClass() {
		return ProductsListFragment.class;
	}
	
	public Class<? extends Fragment> getOrderTabClass() {
		return SavedOrdersFragment.class;
	}
}
