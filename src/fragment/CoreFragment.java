package au.com.accountsflow.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import au.com.accountsflow.CoreAbstractActivity;
import au.com.accountsflow.R;

import com.actionbarsherlock.view.MenuItem;

/**
 * Base fragment class
 * @author melissa.suryana
 *
 */
public abstract class CoreFragment extends Fragment implements CoreFragmentInterface{	
		    
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		toggleActionItems(false);
		this.setRetainInstance(true);
	}
	
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
	
	public void cancelAllRequests() {
		
	}
}

