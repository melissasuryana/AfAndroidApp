package au.com.accountsflow.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import au.com.accountsflow.CoreAbstractActivity;
import au.com.accountsflow.R;
import au.com.accountsflow.arrayadapter.AfArrayAdapter;

import com.actionbarsherlock.view.MenuItem;

/**
 * Base list fragment
 * @author melissa.suryana
 *
 */
public abstract class CoreListFragment extends ListFragment implements CoreFragmentInterface {	
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		toggleActionItems(false);
		this.setRetainInstance(true);
		//this.setHasOptionsMenu(true);
	}
	
	public abstract String getEmptyListMessage();
	
	/**
	 * Is two panel layout enabled?
	 * @return boolean
	 */
	public boolean isTwoPanelLayout() {
		CoreAbstractActivity activity = (CoreAbstractActivity) this.getActivity();
		View rightFragment = activity.findViewById(R.id.right_fragment);
		return (rightFragment != null && rightFragment.getVisibility() == View.VISIBLE);  
	}
	
	/**
	 * Is this current fragment on the left side?
	 * @return boolean
	 */
	public boolean isLeftFragment() {
		return (this.getId() == R.id.left_fragment);
	}
	
	/**
	 * Is this current fragment on the right side?
	 * @return boolean
	 */
	public boolean isRightFragment() {
		return (this.getId() == R.id.right_fragment);
	}
	
	/**
	 * Enable or disable action items
	 * @param enabled boolean
	 */
	protected void toggleActionItems(boolean enabled) {
		CoreAbstractActivity activity = (CoreAbstractActivity) this.getActivity();
		MenuItem refresh = (MenuItem) activity.getRefreshMenu();
        MenuItem search = (MenuItem) activity.getSearchMenu();
        MenuItem submitAll = (MenuItem) activity.getSubmitAllMenu();
        
        if (this.isLeftFragment()) {
	        if (refresh != null) {
	        	refresh.setEnabled(enabled);
	        }
	    	if (search != null) {
	    		search.setEnabled(enabled);
	        }
	    	if (submitAll != null) {
	    		submitAll.setEnabled(enabled);
	        }
        }
	}	
			
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {		
		super.onViewCreated(view, savedInstanceState);
		
		if (this.getEmptyListMessage() != null) {
			this.setEmptyText(this.getEmptyListMessage());
		}
		
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		getListView().setSelector(R.drawable.selected_list_item);
		
		toggleActionItems(false);
	}
		
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Close opened SearchView
		CoreAbstractActivity activity = (CoreAbstractActivity) this.getActivity();   	
		MenuItem search = (MenuItem) activity.getSearchMenu();
		if (search != null && (!this.isTwoPanelLayout() || this.isRightFragment())) {
			//search.collapseActionView();
        }	
		getListView().setItemChecked(position, true);
	}
	
	/**
	 * Function to refresh list of the suppliers
	 */
	public void refreshList()
	{		
		this.setListShown(false);
		toggleActionItems(false);
	}
	
	@Override
	public void setListAdapter(ListAdapter adapter) {		
		//if (this.isVisible()) {
			super.setListAdapter(adapter);
			this.setListShown(true);		
			toggleActionItems(true);
		//}
	}
	
	/**
	 * Function to filter list adapter	
	 * @param constraint
	 */
	public void filterListAdapter(String constraint)
	{
		ArrayAdapter adapter = (ArrayAdapter)this.getListAdapter();
		if (adapter != null) {			
			adapter.getFilter().filter(constraint);
		}
	}

	public ArrayList getListItems()
	{
		AfArrayAdapter adapter = (AfArrayAdapter)this.getListAdapter();
		if (adapter != null) {			
			return new ArrayList(adapter.getItems());
		}
		return new ArrayList();
	}
	
	public void cancelAllRequests() {
		
	}
}

