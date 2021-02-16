package au.com.accountsflow.arrayadapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import au.com.accountsflow.AfApplication;
import au.com.accountsflow.R;
import au.com.accountsflow.object.SupplierMenuItem;

/**
 * Supplier menu items array adapter
 * @author melissa.suryana
 *
 */
public class SupplierMenuAdapter extends ArrayAdapter<SupplierMenuItem> implements AfArrayAdapter<SupplierMenuItem> {	
	
	private ArrayList<SupplierMenuItem> items;
	private Fragment fragment;
	
    public SupplierMenuAdapter(
    		Context context, Fragment fragment, int textViewResourceId, ArrayList<SupplierMenuItem> items) {
		super(context, textViewResourceId, items);
		this.items = new ArrayList<SupplierMenuItem>(items);
		this.fragment = fragment;
	}

    public boolean areAllItemsEnabled() {
        return false;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = super.getView(position, convertView, parent);
        TextView text = (TextView) listView.findViewById(R.id.list_text);
        SupplierMenuItem item = this.getItem(position);
        AfApplication application = (AfApplication) this.fragment.getActivity().getApplication();
        
    	// product list
        if (item.getCode() == "product_list" ) {
        	listView.setEnabled(true);	
        }
        else if (item.getCode() == "quick_order" ) {
        	listView.setEnabled(true);	
        }
        else if ( item.getCode() == "current_order" ) {
        	listView.setEnabled(true);	
        }
        else if (item.getCode() == "order_history" ) {
        	listView.setEnabled(true);	
        }
        else if (item.getCode() == "promotions" ) {
        	if (application.getListPromotions() != null && application.getListPromotions().size() > 0) {
        		listView.setEnabled(true);
        	}
        	else {
        		listView.setEnabled(false);
        	}
        }
        else if (item.getCode() == "specials" ) {
        	if (application.getListSpecials() != null && application.getListSpecials().size() > 0) {
        		listView.setEnabled(true);
        	}
        	else {
        		listView.setEnabled(false);
        	}	
        }
        else if (item.getCode() == "qda_search" ) {
        	if (application.getListQda() != null && application.getListQda().size() > 0) {
        		listView.setEnabled(true);
        	}
        	else {
        		listView.setEnabled(false);
        	}	
        }
        else if (item.getCode() == "focus_brands" ) {
        	if (application.getFocusBrands() != null && application.getFocusBrands().size() > 0) {
        		listView.setEnabled(true);
        	}
        	else {
        		listView.setEnabled(false);
        	}	
        }
        else if (item.getCode() == "ordered_before" ) {
        	listView.setEnabled(true);	
        }
        else {	        
        	listView.setEnabled(false);	 
        }
        
        if (!listView.isEnabled()) {
        	String name = item.getName();
        	text.setText("No " + name);
        }
        return listView;
    }

    public boolean isEnabled(int position) {
    	SupplierMenuItem item = this.getItem(position);
    	AfApplication application = (AfApplication) this.fragment.getActivity().getApplication();
    	
    	// product list
        if (item.getCode() == "product_list") {    	
        	return true;
        }
        else if (item.getCode() == "quick_order") {    	
        	return true;
        }
        else if ( item.getCode() == "current_order" ) {        	
        	return true;
        }
        else if (item.getCode() == "order_history") {    	
        	return true;
        }
        else if (item.getCode() == "promotions") {    	
        	if (application.getListPromotions() != null && application.getListPromotions().size() > 0) {
        		return true;
        	}
        	else {
        		return false;
        	}
        }
        else if (item.getCode() == "specials") {    	
        	if (application.getListSpecials() != null && application.getListSpecials().size() > 0) {
        		return true;
        	}
        	else {
        		return false;
        	}
        }
        else if (item.getCode() == "qda_search") {    	
        	if (application.getListQda() != null && application.getListQda().size() > 0) {
        		return true;
        	}
        	else {
        		return false;
        	}
        }
        else if (item.getCode() == "focus_brands") {    	
        	if (application.getFocusBrands() != null && application.getFocusBrands().size() > 0) {
        		return true;
        	}
        	else {
        		return false;
        	}
        }
        else if (item.getCode() == "ordered_before") {    	
        	return true;
        }
        
        return false;
    	//return true;
    }
	
	public ArrayList<SupplierMenuItem> getItems()
	{
		return items;
	}
	
	/**
     * Returns the original position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     *
     * @return The original position of the specified item.
     */
    public int getOriginalPosition(SupplierMenuItem item) {
        return items.indexOf(item);
    }
    
    /**
     * Returns the position of the specified item in the filtered array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item in filtered array
     */
    @Override
    public int getPosition(SupplierMenuItem item) {
        return items.indexOf(item);
    }
    
}
