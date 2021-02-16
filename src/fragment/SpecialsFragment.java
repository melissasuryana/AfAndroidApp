package au.com.accountsflow.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import au.com.accountsflow.AfApplication;
import au.com.accountsflow.CoreAbstractActivity;
import au.com.accountsflow.FocusBrandActivity;
import au.com.accountsflow.R;
import au.com.accountsflow.SpecialsActivity;
import au.com.accountsflow.arrayadapter.ProductAdapter;
import au.com.accountsflow.arrayadapter.SpecialAdapter;
import au.com.accountsflow.object.Product;
import au.com.accountsflow.object.ProductGroup;

public class SpecialsFragment extends ExpandableProductsFragment{	
		
	protected void setGroupListAdapter() {     	
		SpecialAdapter adapter = new SpecialAdapter(getActivity(), this, R.layout.product_listrow, listGroups);				
		setListAdapter(adapter);
    }
        
    protected ArrayList<ProductGroup> getListGroups() {
    	AfApplication application = (AfApplication) this.getActivity().getApplication();
    	HashMap<String, Product> specials = application.getListSpecials();
    	
    	ArrayList<ProductGroup> productGroupArray = this.convertHashmapToArrayList(specials);
		return productGroupArray;
    }
    	    
	public String getEmptyListMessage() {
		return getString(R.string.empty_specials);
	}
			
	protected void launchActivity(Product selectedProduct) {		
		CoreAbstractActivity activity = (CoreAbstractActivity) this.getActivity();		
		Intent i = new Intent(activity, SpecialsActivity.class);  
		Bundle args = new Bundle();
		for (int j = 0; j < listGroups.size(); j++) {
    		ArrayList<Product> products = listGroups.get(j).getProducts();
    		int index = products.indexOf(selectedProduct);
    		if (index >= 0) {
    			args.putInt("childPosition", index);
    			args.putInt("groupPosition", j);
    			break;
    		}
    	}
		/*NewPromotionAdapter adapter = (NewPromotionAdapter) this.getExpandableListView().getAdapter();
		int originalPosition = adapter.getOriginalPosition(selectedProduct);
        args.putInt("selectedPosition", originalPosition);*/
		i.putExtra("supplierArgs", args);
	    startActivity(i);
	}	
}
