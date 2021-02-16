package au.com.accountsflow.fragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import au.com.accountsflow.AfApplication;
import au.com.accountsflow.AfHelper;
import au.com.accountsflow.AfXmlParser;
import au.com.accountsflow.CoreAbstractActivity;
import au.com.accountsflow.R;
import au.com.accountsflow.SupplierMenuActivity;
import au.com.accountsflow.arrayadapter.BusinessAdapter;
import au.com.accountsflow.arrayadapter.ExpandableProductAdapter;
import au.com.accountsflow.fragment.SupplierMenuFragment.ListProductTask;
import au.com.accountsflow.object.Business;
import au.com.accountsflow.object.Product;
import au.com.accountsflow.object.ProductGroup;
import au.com.accountsflow.object.SubBusiness;

import com.actionbarsherlock.view.MenuItem;

public class SupplierListFragment extends CoreListFragment {	
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private ListBusinessesTask mBusTask = null;
	private ListSubBusinesses mSubBus = null;
	private FetchBusinessLogo mLogoTask = null;
	private ArrayList<SubBusiness> listSubBusinesses = null;
	private boolean finishLoading = false;
	private HashMap<Integer, Business> listBusinesses = null;
	
	private RelativeLayout progressBar = null;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		progressBar = (RelativeLayout) this.getActivity().findViewById(R.id.loading_image_bar);
		progressBar.setVisibility(View.GONE);

		//mBusTask = new ListBusinessesTask();
		//mBusTask.execute((Void) null);
	}	
	
	@Override
	public void onViewStateRestored (Bundle savedInstanceState){
		super.onViewStateRestored (savedInstanceState);
		
		if (! this.finishLoading) {
			mBusTask = new ListBusinessesTask();
			mBusTask.execute((Void) null);
		}
		else {			
			toggleActionItems(true);
		}		
	}
		
	public String getEmptyListMessage() {
		return getString(R.string.empty_suppliers);
	}
	
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
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		CoreAbstractActivity activity = (CoreAbstractActivity) this.getActivity();
		AfApplication application = (AfApplication) activity.getApplication();
		application.resetData();
		
		Business selectedBusiness = (Business) l.getItemAtPosition(position);
		application.setCurrentBusiness(selectedBusiness);
		SubBusiness subBusiness = selectedBusiness.getSubBusiness();
		
		// if two-panel layout is enabled
		if (this.isTwoPanelLayout()) {			
			// Do something with the data
			FragmentManager fragmentManager = activity.getSupportFragmentManager();
			Fragment supplierMenu = SupplierMenuFragment.newInstance(selectedBusiness.getName(), selectedBusiness.getId(), subBusiness.getPriceListId());
	        FragmentTransaction transaction = fragmentManager.beginTransaction();
	        
	        // Replace whatever is in the fragment_container view with this fragment,
	        // and add the transaction to the back stack
	        
	        transaction.replace(R.id.right_fragment, supplierMenu, "supplierMenuTag");
	        transaction.addToBackStack(null);

	        // Commit the transaction
	        transaction.commit();
		}
		else {
			if (mLogoTask != null) {
				mLogoTask.cancel(true);
			}
			Intent i = new Intent(activity, SupplierMenuActivity.class);  
			Bundle args = new Bundle();
	        args.putString("businessName", selectedBusiness.getName());
	        args.putInt("businessId", selectedBusiness.getId());
	        args.putInt("priceListId", subBusiness.getPriceListId());
			i.putExtra("supplierArgs", args);
		    startActivity(i);
		    //activity.finish(); 
		}
	}
	
	/**
	 * Function to refresh list of the suppliers
	 */
	public void refreshList()
	{		
		super.refreshList();
		mBusTask = new ListBusinessesTask();
		mBusTask.execute((Void) null);
	}
		
	/**
	 * Represents an asynchronous task used to fetch list of relevant businesses
	 */
	public class ListBusinessesTask extends AsyncTask<Void, Void, HashMap<Integer, Business> > {
		@Override
		protected HashMap<Integer, Business> doInBackground(Void... params) {
			String generalError = getString(R.string.error_list_businesses);			           
			HttpParams httpParams = new BasicHttpParams();

			try {	
				HttpResponse response = AfHelper.sendGetRequest(getResources(), getActivity(), getString(R.string.retailer_businesses_api), httpParams, generalError);

				InputStream inputStream = response.getEntity().getContent();
				AfXmlParser xmlParser = new AfXmlParser();

				return xmlParser.parseBusinesses(inputStream);

			} catch (Exception e) {

				return null;

			}
		}

		@Override
		protected void onPostExecute(final HashMap<Integer, Business> response) {
			mBusTask = null;
			if (response == null) {
				String generalError = getString(R.string.error_list_businesses);
				AfHelper.showAlert(getActivity(), generalError);

				ArrayList<Business> businesses = new ArrayList<Business>();
				BusinessAdapter adapter = new BusinessAdapter(getActivity(), SupplierListFragment.this, R.layout.supplier_listrow, businesses);				
				setListAdapter(adapter);
				
				return;
			}
			listBusinesses = response;

			toggleActionItems(false);
			mSubBus = new ListSubBusinesses();
			mSubBus.execute();
		}

		@Override
		protected void onCancelled() {
			toggleActionItems(true);
			mBusTask = null;			
		}

	}	

	/**
	 * Represents an asynchronous task used to fetch list of relevant businesses
	 */
	public class ListSubBusinesses extends AsyncTask<Void, Void, ArrayList<SubBusiness> > {
		@Override
		protected ArrayList<SubBusiness> doInBackground(Void... params) {
			String generalError = getString(R.string.error_list_businesses);			           
			HttpParams httpParams = new BasicHttpParams();

			try {	
				HttpResponse response = AfHelper.sendGetRequest(getResources(), getActivity().getBaseContext() , getString(R.string.retailer_suppliers_api), httpParams, generalError);

				InputStream inputStream = response.getEntity().getContent();
				AfXmlParser xmlParser = new AfXmlParser();

				return xmlParser.parseSubBusinesses(inputStream);

			} catch (Exception e) {

				return null;

			}
		}

		@Override
		protected void onPostExecute(final ArrayList<SubBusiness> response) {
			mSubBus = null;
			if (response == null) {
				String generalError = getString(R.string.error_list_businesses);
				AfHelper.showAlert(getActivity(), generalError);

				ArrayList<Business> businesses = new ArrayList<Business>();
				BusinessAdapter adapter = new BusinessAdapter(getActivity(), SupplierListFragment.this, R.layout.supplier_listrow, businesses);
				setListAdapter(adapter);
				return;
			}
			listSubBusinesses = response;

			ArrayList<Business> businessList = new ArrayList<Business>();
			Business[] businesses = new Business[response.size()];
			for (int i = 0; i < response.size(); i++) {
				SubBusiness sb = response.get(i);
				Business b = listBusinesses.get(sb.getBusinessId());
				b.setSubBusiness(sb);
				businesses[i] = b;
				businessList.add(b);
			}
			//BusinessAdapter adapter = new BusinessAdapter(getActivity(), SupplierListFragment.this, R.layout.supplier_listrow, businessList);
			//setListAdapter(adapter);
			
			mLogoTask = new FetchBusinessLogo();
			//progressBar.setVisibility(View.VISIBLE);
			toggleActionItems(false);
			mLogoTask.execute(businesses);
		}

		@Override
		protected void onCancelled() {
			toggleActionItems(true);
			mSubBus = null;			
		}

	}	

	/**
	 * Represents an asynchronous task used to fetch logo images of the businesses
	 */
	public class FetchBusinessLogo extends AsyncTask<Business, Void, Business[]> {
		@Override
		protected Business[] doInBackground(Business... businesses) {			
			try {

				int count = businesses.length;
				for (int i = 0; i < count; i++) {
					Business business = businesses[i];
					String url = AfHelper.getBaseUrl(getResources());
					if (business.getLogoUrl() != null) {
						url += business.getLogoUrl();

						Bitmap bmp = BitmapFactory.decodeStream(new java.net.URL(url).openStream());
						business.setLogoBmp(bmp);
					}
				} 

			} catch (Exception e) {
				//throw new RuntimeException(e);
				return null;
			}
			return businesses;
		}

		@Override
		protected void onPostExecute(final Business[] response) {
			progressBar.setVisibility(View.GONE);
			mLogoTask = null;
			if (response == null) {
				String generalError = getString(R.string.error_fetch_logos);
				AfHelper.showAlert(getActivity(), generalError);
				return;
			}
			ArrayList<Business> businessList = new ArrayList<Business>(Arrays.asList(response));
			BusinessAdapter adapter = new BusinessAdapter(getActivity(), SupplierListFragment.this, R.layout.supplier_listrow, businessList);
			setListAdapter(adapter);
			if (SupplierListFragment.this.isVisible()) {
				int selectedPosition = SupplierListFragment.this.getListView().getSelectedItemPosition();
	
				if (selectedPosition >= 0) {
					SupplierListFragment.this.setSelection(selectedPosition);
				}
			}
			finishLoading = true;
		}

		@Override
		protected void onCancelled() {
			progressBar.setVisibility(View.GONE);
			toggleActionItems(true);
			mLogoTask = null;			
		}

	}	
	
	public void cancelAllRequests(){
		if (mBusTask != null ) {
			mBusTask.cancel(true);
		}	
		if (mSubBus != null ) {
			mSubBus.cancel(true);
		}	
		if (mLogoTask != null ) {
			mLogoTask.cancel(true);
		}	
	}
}
