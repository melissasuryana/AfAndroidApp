package au.com.accountsflow.arrayadapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import au.com.accountsflow.R;
import au.com.accountsflow.fragment.CoreExpandableListFragment;
import au.com.accountsflow.object.Product;
import au.com.accountsflow.object.ProductGroup;

/**
 * Expandable list adapter for Product objects
 * @author melissa.suryana
 *
 */
public abstract class ExpandableProductAdapter extends BaseExpandableListAdapter implements Filterable, AfArrayAdapter<ProductGroup> {

	private Context context;
	private ArrayList<ProductGroup> groups;
	protected CoreExpandableListFragment fragment;
	protected ArrayList<ProductGroup> fGroups;
	protected ProductGroupFilter filter;
	
	public ExpandableProductAdapter(Context context, CoreExpandableListFragment fragment, int textViewResourceId, ArrayList<ProductGroup> groups) {
		super();
		this.context = context;
		this.groups = groups;
		this.fGroups = groups;
		this.fragment = fragment;
	}
		
	public ArrayList<ProductGroup> getItems()
	{
		return fGroups;
	}
	
	/**
     * Returns the original position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     *
     * @return The original position of the specified item.
     */
    public int getOriginalPosition(ProductGroup item) {
        return groups.indexOf(item);
    }
	    
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		ArrayList<Product> chList = fGroups.get(groupPosition).getProducts();
		return chList.get(childPosition);
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public abstract View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
			ViewGroup parent);

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		ArrayList<Product> chList = fGroups.get(groupPosition).getProducts();

		return chList.size();

	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return fGroups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return fGroups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		ProductGroup group = (ProductGroup) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.product_group_row, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.product_group_text);
		tv.setText(group.getName());
		// TODO Auto-generated method stub
		return view;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}
	
	/**
	 * Implementing the Filterable interface.
	 */
	public Filter getFilter() {
		if (filter == null) {
			filter = new ProductGroupFilter();
		}
		return filter;
	}
	
	/**
	 * Custom Filter implementation for the items adapter.
	 *
	 */
	private class ProductGroupFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {        	 
			FilterResults results = new FilterResults();
			String prefix = constraint.toString().toLowerCase();

			// if prefix is null or empty, display original array
			if (prefix == null || prefix.length() == 0)
			{	
				final ArrayList<ProductGroup> list = new ArrayList<ProductGroup>(groups);
				results.values = list;
				int count = 0;
				for (int i = 0; i < list.size(); i++) {
					count += list.get(i).getProducts().size();
				}
				results.count = count;
			}
			else
			{
				final ArrayList<ProductGroup> listCopy = new ArrayList<ProductGroup>(groups);
				final HashMap<String,ProductGroup> hashMap = new HashMap<String,ProductGroup>();

				int count = 0;
				for (int i=0; i < listCopy.size(); i++)
				{
					final ProductGroup pg = listCopy.get(i);
					
					for (int j = 0; j < pg.getProducts().size(); j++) {
						final Product p = pg.getProducts().get(j);
						final String description = p.getDescription().toLowerCase();
						final String code = p.getCode().toLowerCase();
						
						// find product contains prefix on the description or code
						if (description.contains(prefix) || code.contains(prefix))
						{
							ProductGroup group = hashMap.get(p.getCategory());
					    	
					    	if (! hashMap.containsKey(p.getCategory())) {
					    		group = new ProductGroup();
					    		group.setName(p.getCategory());
					    		group.setProducts(new ArrayList<Product>());
					    		hashMap.put(p.getCategory(), group);
					    	}
					    	
					    	ArrayList<Product> products = group.getProducts();
					    	products.add(p);
					    	count++;
						}
					}
				}					
				
				results.values = new ArrayList<ProductGroup>(hashMap.values());
				results.count = count;
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			//int position = fragment.getExpandableListView().getCheckedItemPosition();
			//Product selectedProduct = (Product) fragment.getExpandableListView().getItemAtPosition(position);
			
			ExpandableProductAdapter.this.fGroups= (ArrayList<ProductGroup>) results.values;
				
			if (fGroups != null) {
				if (results.count > 0) {
					ExpandableProductAdapter.this.notifyDataSetChanged();
	            } else {
	            	ExpandableProductAdapter.this.notifyDataSetInvalidated();
	            }			
			}
			
			/*if (selectedProduct != null) {	        	
	        	position = BaseProductsAdapter.this.getPosition(selectedProduct);
	        	fragment.getListView().setItemChecked(position, true);
        	}*/
			
			ExpandableListView listView = fragment.getExpandableListView();
			ExpandableProductAdapter adapter = (ExpandableProductAdapter) listView.getExpandableListAdapter();
			if (adapter != null) {
				for (int i = 0; i < adapter.getGroupCount(); i++) {
					listView.expandGroup(i);
				}
			}
		}

	}

}
