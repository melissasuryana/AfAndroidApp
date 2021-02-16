package au.com.accountsflow;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.ClassUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import au.com.accountsflow.compatibility.CompatTab;
import au.com.accountsflow.compatibility.CompatTabListener;
import au.com.accountsflow.compatibility.TabHelper;
import au.com.accountsflow.compatibility.UiCompatibilityActivity;
import au.com.accountsflow.fragment.BaseProductsFragment;
import au.com.accountsflow.fragment.CoreExpandableListFragment;
import au.com.accountsflow.fragment.CoreFragmentInterface;
import au.com.accountsflow.fragment.CoreListFragment;
import au.com.accountsflow.fragment.CurrentOrderFragment;
import au.com.accountsflow.fragment.ExpandableProductsFragment;
import au.com.accountsflow.fragment.OrderedBeforeFragment;
import au.com.accountsflow.fragment.ProductDetailFragment;
import au.com.accountsflow.fragment.SavedOrderItemsFragment;
import au.com.accountsflow.fragment.SavedOrdersFragment;
import au.com.accountsflow.fragment.SupplierMenuFragment;
import au.com.accountsflow.object.Basket;
import au.com.accountsflow.object.BasketItem;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;

/**
 * Base activity class for Accounts Flow
 * @author melissa.suryana
 *
 */
public abstract class CoreAbstractActivity extends UiCompatibilityActivity {

	CompatTab supplierTab;
	CompatTab orderTab;
	CompatTab selectedTab;
	MenuItem searchItem;
	MenuItem refreshItem;
	MenuItem submitAllItem;
	MenuItem addOrderButton;
	MenuItem addProductButton;
	SubmitOrderTask mSubmitOrder;
	ProgressDialog progressDialog;
	ArrayList<Basket> unsentOrders;

	Class<? extends Activity> homeActivity = SupplierListActivity.class;

	/**
	 * Toggle for the back arrow next to home button
	 * @return boolean
	 */
	public abstract boolean displayHomeAsUp();

	/**
	 * Get the title for this activity
	 * @return String
	 */
	public abstract String getActivityTitle();

	/**
	 * Is search button visible in this activity?
	 * @return boolean
	 */
	public abstract boolean isSearchVisible();

	/**
	 * Is refresh button visible in this activity?
	 * @return boolean
	 */
	public abstract boolean isRefreshVisible();

	/**
	 * Get fragment class for 'Suppliers' tab
	 * @return Class
	 */
	public abstract Class<? extends Fragment> getSupplierTabClass();

	/**
	 * Get arguments to pass to 'Suppliers' tab fragment
	 * @return Bundle
	 */
	public Bundle getSupplierTabArgs() {
		Bundle data = getIntent().getExtras();
		Bundle supplierArgs = data.getBundle("supplierArgs");
		if (supplierArgs == null) {
			return new Bundle();
		}
		return supplierArgs;
	}

	/**
	 * Get fragment class for 'Saved Orders' tab
	 * @return Class
	 */
	public abstract Class<? extends Fragment> getOrderTabClass();

	/**
	 * Get arguments to pass to 'Saved Orders' tab fragment
	 * @return Bundle
	 */
	public Bundle getOrderTabArgs() {
		Bundle data = getIntent().getExtras();
		Bundle orderArgs = data.getBundle("orderArgs");
		if (orderArgs == null) {
			return new Bundle();
		}
		return orderArgs;
	}

	/**
	 * Show or hide up button
	 * @param shown boolean
	 */
	protected void toggleUpButton(boolean shown) {    	
		ActionBar actionBar = this.getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(shown);
	}

	/**
	 * Initial setup for this activity
	 */
	protected void setup() {

		toggleUpButton(this.displayHomeAsUp());

		// Set title        
		this.setTitle(this.getActivityTitle());
	}

	/**
	 * Is this using two panel layout?
	 * @return boolean true if both left and right fragment are exist and visible
	 */
	public boolean isTwoPanelLayout() {
		View rightFragment = this.findViewById(R.id.right_fragment);
		return (rightFragment != null && rightFragment.getVisibility() == View.VISIBLE); 
	}

	/**
	 * Get left fragment instance
	 * @return Fragment
	 */
	public Fragment getLeftFragment() {
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		return fragmentManager.findFragmentById(R.id.left_fragment);
	}

	/**
	 * Get right fragment instance
	 * @return Fragment
	 */
	public Fragment getRightFragment() {
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		Fragment rightFragment = fragmentManager.findFragmentById(R.id.right_fragment);

		if (rightFragment != null) {
			View fragmentView = rightFragment.getView();
			if (fragmentView != null && fragmentView.getVisibility() == View.VISIBLE) {		
				return rightFragment;
			}
		}

		return null;		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boolean loggedOut = getIntent().getBooleanExtra("loggedOut", false);
		// launch login activity on logout
		if (loggedOut) {
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			return;
		}

		setContentView(R.layout.activity_core);

		/*Bundle data = getIntent().getExtras();
        //UserIdentity identity = data.getParcelable("identity");

        // Set title
        if (getResources().getBoolean(R.bool.use_live)) {
        	this.setTitle(getString(R.string.app_name));
        }
        else {
        	this.setTitle(getString(R.string.app_name_demo));
        }
        if (identity.getType() == UserIdentity.TYPE.RETAILER) {
        	this.setTitle(getString(R.string.title_activity_suppliers));
        }
        else if(identity.getType() == UserIdentity.TYPE.SUPPLIER) {
        	this.setTitle(getString(R.string.title_activity_retailers));
        }*/
		
		// Check if supplier fragment implements CoreFragmentInterface
		Class supplierClass = this.getSupplierTabClass();
		List<Class<?>> supplierInterfaces = ClassUtils.getAllInterfaces(supplierClass);
		boolean implementsCoreInterface = false;
		for (int i = 0; i < supplierInterfaces.size(); i++) {
			Class c = supplierInterfaces.get(i);
			if (c.getSimpleName().equals("CoreFragmentInterface")) {
				implementsCoreInterface = true;
			}
		}
		if (! implementsCoreInterface) {
			throw new RuntimeException("Supplier fragment must implements CoreFragmentInterface");
		}
		
		// Check if order fragment implements CoreFragmentInterface
		Class orderClass = this.getOrderTabClass();
		List<Class<?>> orderInterfaces = ClassUtils.getAllInterfaces(orderClass);
		implementsCoreInterface = false;
		for (int i = 0; i < orderInterfaces.size(); i++) {
			Class c = orderInterfaces.get(i);
			if (c.getSimpleName().equals("CoreFragmentInterface")) {
				implementsCoreInterface = true;
			}
		}
		if (! implementsCoreInterface) {
			throw new RuntimeException("Order fragment must implements CoreFragmentInterface");
		}
		// create tabs
		onCreateTabs(this.getSupplierTabClass(), this.getSupplierTabArgs(),
				this.getOrderTabClass(), this.getOrderTabArgs());

		setup();
	}
	
	/**
	 * Set selected tab
	 * @param tab CompatTab
	 */
	public void setSelectedTab(CompatTab tab) {
		this.selectedTab = tab;
	}

	/**
	 * Get selected tab
	 * @return CompatTab
	 */
	public CompatTab getSelectedTab() {
		return selectedTab;
	}

	/**
	 * Create tabse
	 * @param supplierClass Class
	 * @param supplierArgs Bundle
	 * @param orderClass Class
	 * @param orderArgs Bundle
	 */
	protected void onCreateTabs(Class<? extends Fragment> supplierClass, Bundle supplierArgs, 
			Class<? extends Fragment> orderClass, Bundle orderArgs) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		TabHelper tabHelper = getTabHelper();

		supplierTab = tabHelper.newTab("supplierTab")
				.setText(R.string.tab_suppliers)
				.setIcon(R.drawable.ic_suppliers_white)
				.setTabListener(new InstantiatingTabListener(this, supplierClass, supplierArgs));
		tabHelper.addTab(supplierTab);
		this.setSelectedTab(supplierTab);

		orderTab = tabHelper.newTab("orderTab")
				.setText(R.string.tab_orders)
				.setIcon(R.drawable.ic_orders_white)
				.setTabListener(new InstantiatingTabListener(this, orderClass, orderArgs));
		tabHelper.addTab(orderTab);
	}

	/**
	 * Get 'Supplier' tab instance
	 * @return CompatTab
	 */
	public CompatTab getSupplierTab(){
		return supplierTab;
	}

	/**
	 * Get 'Order' tab instance
	 * @return CompatTab
	 */
	public CompatTab getOrderTab(){
		return orderTab;
	}
	
	/**
	 * Implementation of {@link CompatTabListener} to handle tab change events. This implementation
	 * instantiates the specified fragment class with no arguments when its tab is selected.
	 */
	public static class InstantiatingTabListener implements CompatTabListener {

		private final CoreAbstractActivity mActivity;
		private final Class mClass;
		private final Bundle mArgs;

		/**
		 * Constructor used each time a new tab is created.
		 *
		 * @param activity The host Activity, used to instantiate the fragment
		 * @param cls      The class representing the fragment to instantiate
		 */
		public InstantiatingTabListener(CoreAbstractActivity activity, Class<? extends Fragment> cls, Bundle args) {
			mActivity = activity;
			mClass = cls;
			mArgs = args;
		}

		/* The following are each of the ActionBar.TabListener callbacks */
		@Override
		public void onTabSelected(CompatTab tab, FragmentTransaction ft) {
			mActivity.cancelAllFragmentRequests();
			mActivity.setSelectedTab(tab);

			// Check if the fragment is already initialized
			Fragment fragment = tab.getFragment();

			if (fragment == null) {
				// If not, instantiate and add it to the activity
				fragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
				tab.setFragment(fragment);
				ft.replace(R.id.left_fragment, fragment, tab.getTag());
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(fragment);
			}
			
			// empty right fragment if exist
			Fragment rightFragment = mActivity.getSupportFragmentManager().findFragmentById(R.id.right_fragment);
			if (rightFragment != null && rightFragment.isVisible()) {
				ft.remove(rightFragment);
			}

			// show or hide submit all item button
			if (mActivity.submitAllItem != null) {
				if (fragment instanceof SavedOrdersFragment) {
					mActivity.submitAllItem.setVisible(true);
				}
				else {
					mActivity.submitAllItem.setVisible(false);
				}
			}
			
			// show or hide add order button
			if (mActivity.addOrderButton != null) {
				if (fragment instanceof ProductDetailFragment) {
					mActivity.addOrderButton.setVisible(true);
				}
				else if (mActivity.isTwoPanelLayout() &&  (fragment instanceof BaseProductsFragment ||
					fragment instanceof ExpandableProductsFragment || fragment instanceof OrderedBeforeFragment)) {
					mActivity.addOrderButton.setVisible(true);
				}
				else {
					mActivity.addOrderButton.setVisible(false);
				}
			}
			
			// show or hide submit all product button
			if (mActivity.addProductButton != null) {
				if (fragment instanceof CurrentOrderFragment) {
					mActivity.addProductButton.setVisible(true);
				}
				else if (fragment instanceof SupplierMenuFragment && rightFragment instanceof CurrentOrderFragment) {
					mActivity.addProductButton.setVisible(true);
				}
				else {
					mActivity.addProductButton.setVisible(false);
				}
			}
			
			// show or hide refresh item button
			if (mActivity.refreshItem != null) {
				mActivity.refreshItem.setVisible(mActivity.isRefreshVisible());
	    	}
	    	
			// show or hide search item button
	    	if (mActivity.searchItem != null) {
	    		mActivity.searchItem.setVisible(mActivity.isSearchVisible());
	    		
	    		AfSearchView searchView = (AfSearchView) mActivity.searchItem.getActionView();
		    	
		    	if (fragment instanceof SavedOrdersFragment) {
		    		searchView.setQueryHint(mActivity.getString(R.string.search_saved_order));
				}
				else {
					searchView.setQueryHint(searchView.getOriginalQueryHint());
				}
	    	}					
		}

		@Override
		public void onTabUnselected(CompatTab tab, FragmentTransaction ft) {
			Fragment fragment = tab.getFragment();
			if (fragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(fragment);
			}
		}

		@Override
		public void onTabReselected(CompatTab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Do nothing.
		}
	} 

	/*
	 * Setup search view config
	 */
	protected void _configureSearchView(Menu menu) {	

		//Create the search view
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		AfSearchView searchView = new AfSearchView(getSupportActionBar().getThemedContext());
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

		// filter on text change
		final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String newText) {
				Fragment fragment = getSelectedTab().getFragment();
				if (fragment instanceof CoreListFragment) {
					CoreListFragment listFragment = (CoreListFragment) getSelectedTab().getFragment();
					listFragment.filterListAdapter(newText);
				}
				else if (fragment instanceof CoreExpandableListFragment) {
					CoreExpandableListFragment listFragment = (CoreExpandableListFragment) getSelectedTab().getFragment();
					listFragment.filterListAdapter(newText);
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

	/**
	 * Base action bar-aware implementation for
	 * {@link Activity#onCreateOptionsMenu(android.view.Menu)}.
	 *
	 * Note: marking menu items as invisible/visible is not currently supported.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_core, menu);

		// get menu items
		refreshItem = (MenuItem) menu.findItem(R.id.menu_refresh);      	
		searchItem = (MenuItem) menu.findItem(R.id.menu_search);
		submitAllItem = (MenuItem) menu.findItem(R.id.menu_submit_all);
		addOrderButton = (MenuItem) menu.findItem(R.id.menu_add_order);
		addProductButton = (MenuItem) menu.findItem(R.id.menu_add_product);

		// set menu items visibility
		refreshItem.setVisible(this.isRefreshVisible());     	
		searchItem.setVisible(this.isSearchVisible());  
		submitAllItem.setVisible(false);
		addOrderButton.setVisible(false);
		addProductButton.setVisible(false);
		
		Fragment fragment = this.getLeftFragment();
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

		_configureSearchView(menu);

		// Calling super after populating the menu is necessary here to ensure that the
		// action bar helpers have a chance to handle this event.
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Get refresh menu item
	 * @return MenuItem
	 */
	public MenuItem getRefreshMenu() {
		return refreshItem;
	}

	/**
	 * Get search menu item
	 * @return MenuItem
	 */
	public MenuItem getSearchMenu() {
		return searchItem;
	}

	/**
	 * Get submit all menu
	 * @return MenuItem
	 */
	public MenuItem getSubmitAllMenu() {
		return submitAllItem;
	}
	
	/**
	 * Cancel http requests run in this fragment
	 */
	public void cancelAllFragmentRequests() {
		CoreFragmentInterface leftFragment = (CoreFragmentInterface) this.getLeftFragment();
		if (leftFragment != null) {
			leftFragment.cancelAllRequests();
		}
		if (this.isTwoPanelLayout()) {
			CoreFragmentInterface rightFragment = (CoreFragmentInterface) this.getRightFragment();
			if (rightFragment != null) {
				rightFragment.cancelAllRequests();
			}
		}
	}

	/**
	 * Logout menu item clicked 
	 * @param MenuItem item
	 */
	public void onLogoutClicked(MenuItem item) {
		cancelAllFragmentRequests();
		UserLogoutTask logoutTask = new UserLogoutTask();
		logoutTask.execute((Void) null);
	}

	/**
	 * Search menu item clicked
	 * @param MenuItem item
	 */
	public void onSearchClicked(MenuItem item) {
		onSearchRequested();
	}

	/**
	 * Refresh menu item clicked
	 * @param MenuItem item
	 */
	public void onRefreshClicked(MenuItem item) {		 
		Fragment fragment = getSelectedTab().getFragment();		
		
		if (fragment instanceof CoreListFragment) {
			CoreListFragment listFragment = (CoreListFragment) getSelectedTab().getFragment();
			listFragment.refreshList();
		}
		else if (fragment instanceof CoreExpandableListFragment) {
			CoreExpandableListFragment listFragment = (CoreExpandableListFragment) getSelectedTab().getFragment();
			listFragment.refreshList();
		}
	}
	
	/**
	 * Add order menu item clicked
	 * @param MenuItem item
	 */
	public void onAddOrderClicked(MenuItem item) {
		// Needs to be implemented by child fragment if needed
	}
	
	/**
	 * Add product menu item clicked
	 * @param MenuItem item
	 */
	public void onAddProductClicked(MenuItem item) {
		// Needs to be implemented by child fragment if needed
	}

	/**
	 * Progress order button clicked
	 * @param view
	 */
	public void progressOrder(View view) {		
		if (this.getLeftFragment() instanceof CurrentOrderFragment) {
			((CurrentOrderFragment) this.getLeftFragment()).progressOrder(view);
		}
		else if (this.getLeftFragment() instanceof SavedOrderItemsFragment) {
			((SavedOrderItemsFragment) this.getLeftFragment()).progressOrder(view);
		}
		else if (this.getRightFragment() instanceof CurrentOrderFragment) {
			((CurrentOrderFragment) this.getRightFragment()).progressOrder(view);
		}
		else if (this.getRightFragment() instanceof SavedOrderItemsFragment) {
			((SavedOrderItemsFragment) getRightFragment()).progressOrder(view);
		}
	}
	
	/**
	 * Create submit all progress dialog 
	 */
	public void createProgressDialog() {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setTitle(R.string.submit_all_title);
		dialog.setMax(100);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog = dialog;
		dialog.show();
	}
	
	/**
	 * Create confirmation dialog when submit all clicked
	 */
	public void createConfirmDialog() {
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(R.string.submit_all_dialog_message)
		.setTitle(R.string.confirmation_title);

		// Add the buttons
		builder.setPositiveButton(R.string.submit_all_dialog_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				ArrayList<Basket> baskets = CoreAbstractActivity.this.getSavedOrders();
				
				if (baskets.size() <= 0) {
					AfHelper.showAlert(
						CoreAbstractActivity.this.getApplicationContext(), 
						CoreAbstractActivity.this.getString(R.string.submit_all_no_basket)
					);
					return;
				}
				mSubmitOrder = new SubmitOrderTask();
				mSubmitOrder.execute(false);
				createProgressDialog();
			}
		});
		builder.setNegativeButton(R.string.submit_all_dialog_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	/**
	 * Create retry dialog when submit all request failed
	 */
	public void createRetryDialog() {
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// 2. Chain together various setter methods to set the dialog characteristics
		String message = this.getString(R.string.submit_all_fail);
		if (this.unsentOrders != null) {
			message += "\n";
			for (int i = 0; i < unsentOrders.size(); i++) {
				message += "\n - " + unsentOrders.get(i).getName();
			}
		}
		message += "\n\n" + getString(R.string.submit_all_retry);
		
		builder.setMessage(message)
		.setTitle(R.string.submit_all_retry_title);
		
		// Add the buttons
		builder.setPositiveButton(R.string.submit_all_retry_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				ArrayList<Basket> baskets = CoreAbstractActivity.this.getUnsentOrders();
				
				if (baskets.size() <= 0) {
					AfHelper.showAlert(
						CoreAbstractActivity.this.getApplicationContext(), 
						CoreAbstractActivity.this.getString(R.string.submit_all_no_basket)
					);
					return;
				}
				mSubmitOrder = new SubmitOrderTask();
				mSubmitOrder.execute(true);
				createProgressDialog();
			}
		});
		builder.setNegativeButton(R.string.submit_all_retry_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * Submit all click event
	 * @param MenuItem item
	 */
	public void onSubmitAllClicked(MenuItem item) {
		createConfirmDialog();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {   
		case android.R.id.home:
			/*FragmentManager fragmentManager = getSupportFragmentManager();  

                if (fragmentManager.getBackStackEntryCount() > 0) {
                	fragmentManager.popBackStack();
            	}*/
			this.finish();
			break;
		case R.id.menu_logout:
			onLogoutClicked(item);
			break;
		case R.id.menu_refresh:
			onRefreshClicked(item);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLogoutTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {

			HttpParams httpParams = new BasicHttpParams();
			String generalError = getString(R.string.error_logout);


			boolean success = false;
			try { 
				HttpResponse response = AfHelper.sendGetRequest(
						getResources(), getBaseContext(), getString(R.string.logout_api), 
						httpParams, generalError
						);
				InputStream inputStream = response.getEntity().getContent();
				AfXmlParser xmlParser = new AfXmlParser();           	

				success = xmlParser.parseLogout(inputStream);

			} catch (Exception e) {        	
				return false;
			}

			return success;
		}

		@Override
		protected void onPostExecute(final Boolean success) {

			if (success) {
				Intent intent = getIntent();
				intent.removeExtra("identity");

				Intent parentActivityIntent = new Intent(CoreAbstractActivity.this, homeActivity);
				parentActivityIntent.putExtra("loggedOut", true);
				parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(parentActivityIntent);
				finish();
			}
			else {
				AfHelper.showAlert(CoreAbstractActivity.this, getString(R.string.error_logout));
			}
		}	
	}

	/**
	 * Custom search view class
	 * @author melissa.suryana
	 *
	 */
	public class AfSearchView extends SearchView {
		private CharSequence originalQueryHint;
		
		public AfSearchView(Context context) {
			super(context);
			originalQueryHint = null;
		}

		@Override
		public void onActionViewCollapsed() {
			super.onActionViewCollapsed();

			if (getSelectedTab().getFragment() instanceof CoreListFragment) {
				CoreListFragment fragment = (CoreListFragment) getSelectedTab().getFragment();			
				fragment.filterListAdapter("");
			}
			else if (getSelectedTab().getFragment() instanceof CoreExpandableListFragment) {
				CoreExpandableListFragment fragment = (CoreExpandableListFragment) getSelectedTab().getFragment();			
				fragment.filterListAdapter("");
			}
		}
		
		@Override
		public void setQueryHint(CharSequence hint) {
			if (originalQueryHint == null) {
				originalQueryHint = this.getQueryHint();
			}
			super.setQueryHint(hint);
		}
		
		public CharSequence getOriginalQueryHint() {
			return originalQueryHint;
		}
	}
	
	/**
	 * Get list of saved orders
	 * @return ArrayList<Basket>
	 */
	protected ArrayList<Basket> getSavedOrders() {
		SavedOrdersFragment fragment = null;
		ArrayList<Basket> baskets = null;
		
		if (CoreAbstractActivity.this.getLeftFragment() instanceof SavedOrdersFragment) {
			fragment = (SavedOrdersFragment) CoreAbstractActivity.this.getLeftFragment();
		}

		if (CoreAbstractActivity.this.getRightFragment() instanceof SavedOrdersFragment) {
			fragment = (SavedOrdersFragment) CoreAbstractActivity.this.getRightFragment();
		}

		if (fragment != null) {
			baskets = fragment.getSavedOrders();
		}
		
		return baskets;
	}
	
	/**
	 * Get list of unsent orders after submit all request
	 * @return
	 */
	protected ArrayList<Basket> getUnsentOrders() {
		return unsentOrders;
	}
	
	/**
	 * Get arguments for submit order request 
	 * @param basket Basket
	 * @return List<NameValuePair>
	 */
	protected List<NameValuePair> getRequestArgs(Basket basket) {
    	AfApplication application = (AfApplication) this.getApplication();
    	List<NameValuePair> requestArgs = new ArrayList<NameValuePair>(5);
    	String uuid = UUID.randomUUID().toString();
        requestArgs.add(new BasicNameValuePair("Id", uuid));
        requestArgs.add(new BasicNameValuePair("SupplierId", String.valueOf(basket.getSupplierId())));
        int wholesalerId = basket.getWholesalerId();
        if (wholesalerId > 0) {
        	requestArgs.add(new BasicNameValuePair("WholesalerId", String.valueOf( wholesalerId )));
        }
        requestArgs.add(new BasicNameValuePair("Name", basket.getName()));
        requestArgs.add(new BasicNameValuePair("Notes", basket.getNotes()));
        
        ArrayList<BasketItem> basketItems = basket.getItems();
        
        for(int i = 0; i < basketItems.size(); i++) {
	        BasketItem item = basketItems.get(i);
                        	
        	String code = "Items[" + i + "].Code";
        	String quantity = "Items[" + i + "].Quantity";
        	
        	requestArgs.add(new BasicNameValuePair(code, item.getCode()));
            requestArgs.add(new BasicNameValuePair(quantity, String.valueOf(item.getQuantity())));
            i++;
        }
        
        return requestArgs;
    }
	
	/**
	 * Submit order async task
	 */
	public class SubmitOrderTask extends AsyncTask<Boolean, Integer, ArrayList<Basket>> {
		@Override
		protected ArrayList<Basket> doInBackground(Boolean... params) {
			boolean sendUnsentOrder = params[0];
			ArrayList<Basket> baskets = CoreAbstractActivity.this.getSavedOrders();
			
			if (sendUnsentOrder) {
				baskets = CoreAbstractActivity.this.getUnsentOrders();
			}
			
			String generalError = getString(R.string.error_submit_order);
			ArrayList<Basket> unsentBaskets = new ArrayList<Basket>();
			
			for (int i = 0; i < baskets.size(); i++) {
				Basket basket = baskets.get(i);
				
				List<NameValuePair> requestArgs = CoreAbstractActivity.this.getRequestArgs(basket);
				
		        try {	
		        	HttpResponse response = AfHelper.sendPostRequest(getResources(), CoreAbstractActivity.this.getBaseContext(), 
		        			getString(R.string.retailer_basket_send_api), requestArgs, generalError);
					
	            	InputStream inputStream = response.getEntity().getContent();
	            	AfXmlParser xmlParser = new AfXmlParser();
	            	
		            int orderNumber = xmlParser.parseSubmitOrder(inputStream);
		            
		            if (orderNumber < 0) {
		            	unsentBaskets.add(basket);
		            }			            
		        } catch (Exception e) {		        	
		        	unsentBaskets.add(basket);		        	
		        }
		        
		        publishProgress(i);
			} 
			
			return unsentBaskets;
		}

		protected void onProgressUpdate(Integer... progress) {
			ArrayList<Basket> baskets = CoreAbstractActivity.this.getSavedOrders();
			int count = baskets.size();
			
	         int percent = (int) ((progress[0] / (float) count ) * 100);
	         if (progressDialog != null) {
	        	 progressDialog.setProgress(percent);
	         }
	         
	     }
		
		@Override
		protected void onPostExecute(final ArrayList<Basket> unsentBaskets) {
			mSubmitOrder = null;			
			progressDialog.dismiss();
			
			CoreAbstractActivity activity =  CoreAbstractActivity.this;
			
			if (unsentBaskets.size() <= 0) {
				AfHelper.showToast(activity, getString(R.string.submit_order_successful));
			}
			else {
				CoreAbstractActivity.this.unsentOrders = unsentBaskets;
				createRetryDialog();
			}			
		}

		@Override
		protected void onCancelled() {
			mSubmitOrder = null;				
			progressDialog.dismiss();
		}		
	}
}
