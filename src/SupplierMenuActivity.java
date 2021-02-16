package au.com.accountsflow;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import au.com.accountsflow.fragment.AddProductDialog;
import au.com.accountsflow.fragment.CoreExpandableListFragment;
import au.com.accountsflow.fragment.CoreListFragment;
import au.com.accountsflow.fragment.CurrentOrderFragment;
import au.com.accountsflow.fragment.SavedOrderItemsFragment;
import au.com.accountsflow.fragment.SavedOrdersFragment;
import au.com.accountsflow.fragment.SupplierMenuFragment;
import au.com.accountsflow.object.OrderItem;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;

/**
 * Activity to list available menus within supplier context
 * @author melissa.suryana
 *
 */
public class SupplierMenuActivity extends CoreAbstractActivity {

	public boolean displayHomeAsUp() {
		return true;
	}

	public String getActivityTitle() {
		Bundle data = getIntent().getExtras();
		Bundle supplierArgs = data.getBundle("supplierArgs");
		if (supplierArgs != null) {
			return supplierArgs.getString("businessName");
		}
		return "Menu";
	}

	public boolean isSearchVisible() {
		if (this.isTwoPanelLayout()) {
			return true;
		}
		return false;
	}

	public boolean isRefreshVisible() {		
		return true;
	}

	public Class<? extends Fragment> getSupplierTabClass() {
		return SupplierMenuFragment.class;
	}

	public Class<? extends Fragment> getOrderTabClass() {
		return SavedOrdersFragment.class;
	}

	@Override
	protected void _configureSearchView(Menu menu) {    	
		if (this.isTwoPanelLayout()) {
			//Create the search view
			SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			MenuSearchView searchView = new MenuSearchView(getSupportActionBar().getThemedContext());
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

			final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
				@Override
				public boolean onQueryTextChange(String newText) {
					Fragment leftFragment = SupplierMenuActivity.this.getLeftFragment();
					if (leftFragment != null) {
						if (leftFragment instanceof SavedOrdersFragment) {	            	
							((SavedOrdersFragment) leftFragment).filterListAdapter(newText);
							return true;
						}
					}

					Fragment rightFragment = SupplierMenuActivity.this.getRightFragment();
					if (rightFragment != null) {
						if (rightFragment instanceof CoreListFragment) {
							CoreListFragment listFragment = (CoreListFragment) rightFragment;	            	
							listFragment.filterListAdapter(newText);
						}
						else if (rightFragment instanceof CoreExpandableListFragment) {
							CoreExpandableListFragment listFragment = (CoreExpandableListFragment) rightFragment;	            	
							listFragment.filterListAdapter(newText);
						}
					}
					return true;
				}
				@Override
				public boolean onQueryTextSubmit(String query) {
					return true;
				}
			};
			searchView.setOnQueryTextListener(queryTextListener);            
			searchItem.setActionView(searchView);
		}
		else {
			super._configureSearchView(menu);
		}
	}

	public MenuItem getAddProductButton() {
		return addProductButton;
	}

	/**
	 * Base action bar-aware implementation for
	 * {@link Activity#onCreateOptionsMenu(android.view.Menu)}.
	 *
	 * Note: marking menu items as invisible/visible is not currently supported.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		boolean r = super.onCreateOptionsMenu(menu);

		if (this.isTwoPanelLayout()) {
			Fragment rightFragment = this.getRightFragment();

			if (rightFragment instanceof CurrentOrderFragment) {
				this.addProductButton.setVisible(true);
				refreshItem.setVisible(false);
				searchItem.setVisible(false);
			}
			else {
				this.addProductButton.setVisible(false);
				refreshItem.setVisible(true);
				searchItem.setVisible(true);
			}
		}

		Fragment fragment = this.getRightFragment();
		if (fragment instanceof CoreListFragment) {
			CoreListFragment listFragment = (CoreListFragment) fragment;
			if (listFragment.getListView().getCount() <= 0) {
				refreshItem.setEnabled(false);
				searchItem.setEnabled(false);
			}
			else {
				refreshItem.setEnabled(true);
				searchItem.setEnabled(true);
			}
		}
		else if (fragment instanceof CoreExpandableListFragment) {
			CoreExpandableListFragment listFragment = (CoreExpandableListFragment) fragment;
			if (listFragment.getExpandableListView().getCount() <= 0) {
				refreshItem.setEnabled(false);
				searchItem.setEnabled(false);
			}
			else {
				refreshItem.setEnabled(true);
				searchItem.setEnabled(true);
			}
		}
		else {
			refreshItem.setEnabled(false);
			searchItem.setEnabled(false);
		}

		return r;
	}

	@Override
	public void onRefreshClicked(MenuItem item) {
		super.onRefreshClicked(item);
	}

	public void onAddProductClicked(MenuItem item) {
		AddProductDialog dialog = new AddProductDialog();
		dialog.show(getSupportFragmentManager(), "add_product");
	}
	
	public void createReminderDialog() {
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(R.string.save_basket_reminder)
		.setTitle(R.string.alert_title);		

		// Add the buttons
		builder.setPositiveButton(R.string.save_basket_yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Bundle args = new Bundle();
				if (SupplierMenuActivity.this.isTwoPanelLayout()) {
					args.putBoolean("runProgressOrder", true);
				}
				Intent i = new Intent(SupplierMenuActivity.this, CurrentOrderActivity.class);
				i.putExtra("supplierArgs", args);
				startActivity(i);				
			}
		});
		builder.setNegativeButton(R.string.save_basket_no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				AfApplication application = (AfApplication) SupplierMenuActivity.this.getApplication();
				application.setCurrentOrder(new HashMap<String, OrderItem>());
				SupplierMenuActivity.this.finish();
			}
		});

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public void onBackPressed () {
		AfApplication application = (AfApplication) this.getApplication();
		HashMap<String, OrderItem> orders = application.getCurrentOrder();
		if (orders != null && !orders.isEmpty()) {
			createReminderDialog();
		}
		else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				AfApplication application = (AfApplication) this.getApplication();
				HashMap<String, OrderItem> orders = application.getCurrentOrder();
				if (orders != null && !orders.isEmpty()) {
					createReminderDialog();
					return false;
				}
				else {
					return super.onOptionsItemSelected(item);
				}
		}
		return super.onOptionsItemSelected(item);
	}

	public class MenuSearchView extends AfSearchView {

		public MenuSearchView(Context context) {
			super(context);
		}

		@Override
		public void onActionViewCollapsed() {
			super.onActionViewCollapsed();

			Fragment fragment = SupplierMenuActivity.this.getRightFragment();
			if (fragment != null) {
				if (fragment instanceof CoreListFragment) {
					CoreListFragment listFragment = (CoreListFragment) fragment;	            	
					listFragment.filterListAdapter("");
				}
				else if (fragment instanceof CoreExpandableListFragment) {
					CoreExpandableListFragment listFragment = (CoreExpandableListFragment) fragment;	            	
					listFragment.filterListAdapter("");
				}
			}
		}
	}
}
