package au.com.accountsflow.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import au.com.accountsflow.R;
import au.com.accountsflow.object.Discount;
import au.com.accountsflow.object.Price;
import au.com.accountsflow.object.Product;
import au.com.accountsflow.object.Special;

public class ProductDetailFragment extends CoreFragment{
	/**
     * Create a new instance of SearchResultsFragment
     */
    public static ProductDetailFragment newInstance(Product p) {
    	ProductDetailFragment f = new ProductDetailFragment();

        // Supply string input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("product", p);
        f.setArguments(args);

        return f;
    }
    
    public Product getProduct() {
    	return getArguments().getParcelable("product");
    }
        
    @Override
	public void onResume() {
    	super.onResume();
    	    	
    	Product p = this.getProduct();
        
    	if (p.getDescription() != null) {
	        TextView productDesc = (TextView) this.getView().findViewById(R.id.product_desc_text);
	        productDesc.setText(p.getDescription());
    	}
        
    	if (p.getCategory() != null) {
	        TextView productCategory = (TextView) this.getView().findViewById(R.id.product_category_text);
	        productCategory.setText(p.getCategory());
    	}
        
    	if (p.getCode() != null) {
	        TextView productCode = (TextView) this.getView().findViewById(R.id.product_code_text);
	        productCode.setText(p.getCode());
    	}
        
    	if (p.getUnitSize() != null) {
	        TextView unitSize = (TextView) this.getView().findViewById(R.id.unit_size_text);
	        unitSize.setText(p.getUnitSize());
    	}
        
    	if (p.getUnitType() != null) {
	        TextView unitType = (TextView) this.getView().findViewById(R.id.unit_type_text);
	        unitType.setText(p.getUnitType());
    	}
        
    	if (p.getPackType() != null) {
    		TextView packType = (TextView) this.getView().findViewById(R.id.pack_type_text);
    		packType.setText(p.getPackType());
    	}
        
    	if (p.getUnitPerCase() >= 0) {
	        TextView unitPerCase = (TextView) this.getView().findViewById(R.id.unit_per_case_text);
	        unitPerCase.setText(String.valueOf(p.getUnitPerCase()));
    	}
        
    	if (p.getPrice() != null) {
    		String wholesaleString = p.getPrice().getWholesaleTaxString();
    		TextView priceView = (TextView) this.getView().findViewById(R.id.product_price_text);
    		if (wholesaleString != null) {    			
    	        String price = getString(R.string.currency_symbol);
    			price += wholesaleString;
    	        priceView.setText(price);        	
    		}
    		
    		if (p.hasPromotion()) {
    			// set strike-through style
    			priceView.setPaintFlags(priceView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    			
    			String promoString = p.getPromotion().getWholesaleTaxString();
        		if (promoString != null) {
        			TextView promoView = (TextView) this.getView().findViewById(R.id.product_promo_text);
        	        String price = "Promo: " + getString(R.string.currency_symbol);
        			price += promoString;
        			
        			Date dateStart = p.getPromotion().getDateStart();    				
    				Date dateEnd = p.getPromotion().getDateEnd();
    				
    				price += datesToString(dateStart, dateEnd);
    				
        			promoView.setText(price);        	
        		}
    		}
    		
    		if (p.hasSpecials()) {
    			LinearLayout l = (LinearLayout) this.getView().findViewById(R.id.specials_layout);
    			l.setVisibility(View.VISIBLE);    			
    			TextView text = (TextView) this.getView().findViewById(R.id.product_specials_text);
    			
    			ArrayList<Special> specials = p.getSpecials();
				String specialString = "";
				
				for (int i = 0; i < specials.size(); i++) {
					if (i > 0) {
						specialString += "\n";
					}
					Special s = specials.get(i);
					Date dateStart = s.getDateStart();    				
    				Date dateEnd = s.getDateEnd();
    				
					specialString += s.getDescription() + " " + datesToString(dateStart, dateEnd);
				}
				
    			text.setText(specialString);
    			
    		}
    		
    		if (p.hasQda()) {
    			LinearLayout l = (LinearLayout) this.getView().findViewById(R.id.qda_layout);
    			l.setVisibility(View.VISIBLE);
    			
    			TextView text = (TextView) this.getView().findViewById(R.id.product_qda_text);
    			
    			ArrayList<Discount> qda = p.getQda();
				String qdaString = "";
				
				for (int i = 0; i < qda.size(); i++) {
					Discount d = qda.get(i);
					if (i > 0) {
						qdaString += "\n";
					}
					
					Date dateStart = d.getDateStart();    				
    				Date dateEnd = d.getDateEnd();
    				
					qdaString += getString(R.string.currency_symbol) +
						     	 d.getWholesaleTaxString() + " per case. Minimum order " + 
						     	 d.getCases() + " cases " + datesToString(dateStart, dateEnd) ;
				}
				
    			text.setText(qdaString);
    			
    		}
    		
    		String wholesaleSplitString = p.getPrice().getWholesaleSplitTaxString();
    		if (wholesaleSplitString != null) {
    			TextView priceSplitView = (TextView) this.getView().findViewById(R.id.product_split_price_text);
    	        String splitPrice = getString(R.string.currency_symbol);
    	        splitPrice += wholesaleSplitString;
    	        priceSplitView.setText(splitPrice);        	
    		}
    		
    		String freightString = p.getPrice().getFreightString();
    		if (freightString != null) {
    			TextView freightView = (TextView) this.getView().findViewById(R.id.product_freight_text);
    	        String freightPrice = getString(R.string.currency_symbol);
    	        freightPrice += freightString;
    	        freightView.setText(freightPrice);        	
    		}
    		
    		if (p.getPrice().getTaxRate() >= 0 && p.getPrice().getTaxRateDp() >= 0) {    		
	    		TextView taxRateView = (TextView) this.getView().findViewById(R.id.product_tax_text);
	            int taxRateBase = p.getPrice().getTaxRate();
	            int taxRateDp = p.getPrice().getTaxRateDp();
	            String taxRateString = Price.convertToPercent(taxRateBase, taxRateDp);
	            
	            taxRateView.setText(String.valueOf(taxRateString));
    		}
    		
            TextView isSplittableView = (TextView) this.getView().findViewById(R.id.is_splittable_text);
            boolean isSplittable = p.getPrice().getIsSplittable();
            if (isSplittable) {
            	isSplittableView.setText("Yes");
            }
            else {
            	isSplittableView.setText("No");
            }
    	}
    }
    
    protected String datesToString (Date dateStart, Date dateEnd) {    	
		String dateStartString = new SimpleDateFormat("dd MMM yyyy", 
										Locale.getDefault()).format(dateStart);		
		
		String dateEndString = new SimpleDateFormat("dd MMM yyyy", 
										Locale.getDefault()).format(dateEnd);
		
		return " (" + dateStartString + " - " + dateEndString + ") ";
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        
        return inflater.inflate(R.layout.product_detail, null);
    }
}
