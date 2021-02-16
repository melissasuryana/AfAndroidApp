package au.com.accountsflow.arrayadapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import au.com.accountsflow.R;
import au.com.accountsflow.fragment.CoreExpandableListFragment;
import au.com.accountsflow.object.Discount;
import au.com.accountsflow.object.Product;
import au.com.accountsflow.object.ProductGroup;

/**
 * Array adapter for QDA
 * @author melissa.suryana
 *
 */
public class QdaAdapter extends ExpandableProductAdapter{	
	
	public QdaAdapter(Context context, CoreExpandableListFragment fragment, int textViewResourceId, ArrayList<ProductGroup> items) {
		super(context, fragment, textViewResourceId, items);
	}
		
	@Override
	public View  getChildView(int groupPosition, int childPosition, boolean isLastChild, 
			View view, ViewGroup parent){
		View v = view;
		Activity activity = (Activity) fragment.getActivity();
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.qda_row, null);
		}
		Product product = (Product) this.getChild(groupPosition, childPosition);           
		if (product != null) {
			ImageView starView = (ImageView) v.findViewById(R.id.list_star);
			TextView productDesc = (TextView) v.findViewById(R.id.product_desc);
			TextView productSize = (TextView) v.findViewById(R.id.product_size);
			TextView productQda = (TextView) v.findViewById(R.id.product_qda);
			TextView productCode = (TextView) v.findViewById(R.id.product_code);

			if (starView != null &&  product.hasDeals()) {
				starView.setVisibility(View.VISIBLE);                            
			}
			else {
				starView.setVisibility(View.GONE);  
			}
			
			if (productDesc != null && product.getDescription() != null) {
				productDesc.setText(product.getDescription());                            
			}					 
						
			if (productQda != null &&	product.hasQda()) {
				ArrayList<Discount> qda = product.getQda();
				String qdaString = "";
				
				for (int i = 0; i < qda.size(); i++) {
					Discount d = qda.get(i);
					if (i > 0) {
						qdaString += "\n";
					}
					
					qdaString += d.getCases() + " @ " + activity.getString(R.string.currency_symbol) +
						     	 d.getWholesaleTaxString() + "ea";
				}
				
				
				
				productQda.setText(qdaString);
			}
			
			if (productCode != null && product.getCode() != null) {
				productCode.setText(product.getCode());                            
			}	
			
			if (productSize != null && product.getUnitSize() != null) {
				productSize.setText(product.getUnitSize());                            
			}

		}
		return v;
	}	
}
