package au.com.accountsflow.arrayadapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import au.com.accountsflow.R;
import au.com.accountsflow.fragment.CoreListFragment;
import au.com.accountsflow.object.Product;

/**
 * Base array adapter for product based lists
 * @author melissa.suryana
 *
 */
public abstract class BaseProductsAdapter extends ArrayAdapter<Product> implements Filterable, AfArrayAdapter<Product>{
	
	protected CoreListFragment fragment;
	protected ArrayList<Product> items;
	protected ArrayList<Product> fItems;
	protected ProductFilter filter;

	public BaseProductsAdapter(Context context, CoreListFragment fragment, int textViewResourceId, ArrayList<Product> items) {
		super(context, textViewResourceId, items);
		this.items = new ArrayList<Product>(items);
		this.fItems = new ArrayList<Product>(items);
		this.fragment = fragment;
	}
	
	public ArrayList<Product> getItems()
	{
		return items;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);

	/**
	 * Implementing the Filterable interface.
	 */
	public Filter getFilter() {
		if (filter == null) {
			filter = new ProductFilter();
		}
		return filter;
	}
	
	/**
     * Returns the original position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     * @return The original position of the specified item.
     */
    public int getOriginalPosition(Product item) {
        return items.indexOf(item);
    }
	
    /**
     * Returns the position of the specified item in the filtered array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item in filtered array
     */
    @Override
    public int getPosition(Product item) {
        return fItems.indexOf(item);
    }
    
	/**
	 * Custom Filter implementation for the items adapter.
	 *
	 */
	private class ProductFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {        	 
			FilterResults results = new FilterResults();
			String prefix = constraint.toString().toLowerCase();

			// if prefix is empty or null, return original items
			if (prefix == null || prefix.length() == 0)
			{	
				final ArrayList<Product> list = new ArrayList<Product>(items);
				results.values = list;
				results.count = items.size();
			}
			else
			{
				final ArrayList<Product> itemsCopy = new ArrayList<Product>(items);
				final ArrayList<Product> list = new ArrayList<Product>();

				// filter if description contains the prefix
				for (int i=0; i < itemsCopy.size(); i++)
				{
					final Product p = itemsCopy.get(i);
					final String value = p.getDescription().toLowerCase();

					if (value.contains(prefix))
					{
						list.add(p);
					}
				}					
				
				results.values = list;
				results.count = list.size();
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			int position = fragment.getListView().getCheckedItemPosition();
			Product selectedProduct = (Product) fragment.getListView().getItemAtPosition(position);
			
			BaseProductsAdapter.this.fItems = (ArrayList<Product>) results.values;
			
			// clear the array adapter
			clear();
			int count = fItems.size();
			
			// add the filtered item to the adapter
			for (int i=0; i<count; i++)
			{
				Product b = fItems.get(i);
				add(b);
			}
			
			// notify dataset changed
			if (count > 0) {
				BaseProductsAdapter.this.notifyDataSetChanged();
            } else {
            	BaseProductsAdapter.this.notifyDataSetInvalidated();
            }			

			// reselect selected item
			if (selectedProduct != null) {	        	
	        	position = BaseProductsAdapter.this.getPosition(selectedProduct);
	        	fragment.getListView().setItemChecked(position, true);
        	}
		}

	}
}
