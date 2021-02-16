package au.com.accountsflow.arrayadapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import au.com.accountsflow.R;
import au.com.accountsflow.fragment.CoreExpandableListFragment;
import au.com.accountsflow.object.Product;
import au.com.accountsflow.object.ProductGroup;

/**
 * Expandable product array adapter
 * @author melissa.suryana
 *
 */
public class ProductAdapter extends ExpandableProductAdapter{

	public ProductAdapter(Context context, CoreExpandableListFragment fragment, int textViewResourceId, ArrayList<ProductGroup> items) {
		super(context, fragment, textViewResourceId, items);
	}
		
	@Override
	public View  getChildView(int groupPosition, int childPosition, boolean isLastChild, 
			View view, ViewGroup parent){
		View v = view;
		Activity activity = (Activity) fragment.getActivity();
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.product_listrow, null);
		}
		Product product = (Product) this.getChild(groupPosition, childPosition);                     
		if (product != null) {
			// get view elements
			ImageView starView = (ImageView) v.findViewById(R.id.list_star);
			TextView productDesc = (TextView) v.findViewById(R.id.product_desc);
			TextView productPrice = (TextView) v.findViewById(R.id.product_price);
			TextView productPromo = (TextView) v.findViewById(R.id.product_promo);
			TextView productCode = (TextView) v.findViewById(R.id.product_code);
			TextView productSize = (TextView) v.findViewById(R.id.product_size);
			TextView productQuantity = (TextView) v.findViewById(R.id.product_quantity);
			LinearLayout priceSplitContainer = (LinearLayout) v.findViewById(R.id.product_price_split_container);
			TextView productPriceSplit = (TextView) v.findViewById(R.id.product_price_split);

			// display star image if product is on deal
			if (starView != null &&  product.hasDeals()) {
				starView.setVisibility(View.VISIBLE);                            
			}
			else {
				starView.setVisibility(View.GONE);  
			}
			
			// set description
			if (productDesc != null && product.getDescription() != null) {
				productDesc.setText(product.getDescription());                            
			}	
				
			// set product price
			if (productPrice != null && product.getPrice() != null) {
				String wholesaleString = product.getPrice().getWholesaleTaxString();
				if (wholesaleString != null) {
					String price = activity.getString(R.string.currency_symbol);
					price += wholesaleString;
					productPrice.setText(price);  
				}
				
				if (product.hasPromotion()) {
	    			// set strike-through style
					productPrice.setPaintFlags(productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
	    			
	    			String promoString = product.getPromotion().getWholesaleTaxString();
	        		if (promoString != null) {
	        	        String price = activity.getString(R.string.currency_symbol);
	        			price += promoString;	    				
	        			productPromo.setText(price);        	
	        		}
	    		}
				else {
					productPrice.setPaintFlags( productPrice.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
					productPromo.setText("");
				}
			}
			
			// display split price if any
			if (priceSplitContainer != null) {				
				if (productPriceSplit != null && product.getPrice() != null) {
					String wholesaleSplitString = product.getPrice().getWholesaleSplitTaxString();
					if (wholesaleSplitString != null) {
						String price = activity.getString(R.string.currency_symbol);
						price += wholesaleSplitString;
						priceSplitContainer.setVisibility(View.VISIBLE);
						productPriceSplit.setText(price);						
					}
				}
				priceSplitContainer.setVisibility(View.GONE);
			}
			
			if (productCode != null && product.getCode() != null) {
				productCode.setText(product.getCode());                            
			}	
			
			if (productSize != null && product.getUnitSize() != null) {
				productSize.setText(product.getUnitSize());                            
			}
			
			if (productQuantity != null) {
				productQuantity.setText(String.valueOf(product.getUnitPerCase()));                            
			}
		}
		return v;
	}	
}
