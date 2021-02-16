package au.com.accountsflow.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import au.com.accountsflow.AfApplication;
import au.com.accountsflow.CoreAbstractActivity;
import au.com.accountsflow.FocusBrandActivity;
import au.com.accountsflow.ProductsListActivity;
import au.com.accountsflow.R;
import au.com.accountsflow.arrayadapter.ProductAdapter;
import au.com.accountsflow.object.Product;
import au.com.accountsflow.object.ProductGroup;

public class ProductsListFragment extends ExpandableProductsFragment{	
		    
	protected void setGroupListAdapter() {     	
		ProductAdapter adapter = new ProductAdapter(getActivity(), ProductsListFragment.this, R.layout.product_listrow, listGroups);				
		setListAdapter(adapter);
    }
	    	    
	public String getEmptyListMessage() {
		return getString(R.string.empty_products);
	}
		
	protected ArrayList<ProductGroup> getListGroups() {
    	AfApplication application = (AfApplication) this.getActivity().getApplication();
    	HashMap<String, Product> products = application.getListProducts();
    	
    	ArrayList<ProductGroup> productGroupArray = this.convertHashmapToArrayList(products);
		return productGroupArray;
    }
			
	protected void launchActivity(Product selectedProduct) {		
		CoreAbstractActivity activity = (CoreAbstractActivity) this.getActivity();		
		Intent i = new Intent(activity, ProductsListActivity.class);  
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
