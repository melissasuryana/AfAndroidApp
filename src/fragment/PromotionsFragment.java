package au.com.accountsflow.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import au.com.accountsflow.AfApplication;
import au.com.accountsflow.CoreAbstractActivity;
import au.com.accountsflow.PromotionsActivity;
import au.com.accountsflow.R;
import au.com.accountsflow.arrayadapter.PromotionAdapter;
import au.com.accountsflow.object.OrderItem;
import au.com.accountsflow.object.Product;
import au.com.accountsflow.object.ProductGroup;

public class PromotionsFragment extends ExpandableProductsFragment{		
        
    protected void setGroupListAdapter() {  
		PromotionAdapter adapter = new PromotionAdapter(getActivity(), PromotionsFragment.this, R.layout.promotion_row, listGroups);				
		setListAdapter(adapter);	
    }
    
    protected ArrayList<ProductGroup> getListGroups() {
    	AfApplication application = (AfApplication) this.getActivity().getApplication();
    	HashMap<String, Product> promotions = application.getListPromotions();
    	
    	ArrayList<ProductGroup> productGroupArray = this.convertHashmapToArrayList(promotions);
		return productGroupArray;
    }
    	    
	public String getEmptyListMessage() {
		return getString(R.string.empty_promotions);
	}
	
	protected void launchActivity(Product selectedProduct) {		
		CoreAbstractActivity activity = (CoreAbstractActivity) this.getActivity();		
		Intent i = new Intent(activity, PromotionsActivity.class);  
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
