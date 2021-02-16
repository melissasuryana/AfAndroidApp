package au.com.accountsflow.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import au.com.accountsflow.CoreAbstractActivity;
import au.com.accountsflow.R;
import au.com.accountsflow.arrayadapter.AfArrayAdapter;
import au.com.accountsflow.arrayadapter.ExpandableProductAdapter;

import com.actionbarsherlock.view.MenuItem;

/**
 * Base expandable list fragment to be used in this app
 * @author melissa.suryana
 *
 */
public abstract class CoreExpandableListFragment extends ExpandableListFragment implements CoreFragmentInterface {	
	
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
		ExpandableListView listView = getExpandableListView();
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setSelector(R.drawable.selected_list_item);
		listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				onChildItemClick(parent, v, groupPosition, childPosition, id);
				return true;
			}
		});
		
		toggleActionItems(false);
	}
	
	/**
	 * Event when child item is clicked
	 * @param parent
	 * @param v
	 * @param groupPosition
	 * @param childPosition
	 * @param id
	 */
	public void onChildItemClick (ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// Close opened SearchView
		CoreAbstractActivity activity = (CoreAbstractActivity) this.getActivity();   	
		MenuItem search = (MenuItem) activity.getSearchMenu();
		if (search != null && (!this.isTwoPanelLayout() || this.isRightFragment())) {
			//search.collapseActionView();
        }	
		getExpandableListView().setSelectedChild(groupPosition, childPosition, true);
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
	public void setListAdapter(ExpandableListAdapter adapter) {		
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
		ExpandableProductAdapter adapter = (ExpandableProductAdapter)this.getExpandableListAdapter();
		if (adapter != null) {			
			adapter.getFilter().filter(constraint);
		}
	}

	public ArrayList getListItems()
	{
		AfArrayAdapter adapter = (AfArrayAdapter)this.getExpandableListAdapter();
		if (adapter != null) {			
			return new ArrayList(adapter.getItems());
		}
		return new ArrayList();
	}
	
	public void cancelAllRequests() {
		
	}
}

