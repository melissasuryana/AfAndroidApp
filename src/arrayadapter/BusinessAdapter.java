package au.com.accountsflow.arrayadapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import au.com.accountsflow.R;
import au.com.accountsflow.fragment.CoreListFragment;
import au.com.accountsflow.object.Business;

/**
 * Array adapter for Business objects
 * @author melissa.suryana
 *
 */
public class BusinessAdapter extends ArrayAdapter<Business> implements Filterable, AfArrayAdapter<Business>{
	
	private CoreListFragment fragment;
	private ArrayList<Business> items;
	private ArrayList<Business> fItems;
	private BusinessFilter filter;

	public BusinessAdapter(Context context, CoreListFragment fragment, int textViewResourceId, ArrayList<Business> items) {
		super(context, textViewResourceId, items);
		this.items = new ArrayList<Business>(items);
		this.fItems = new ArrayList<Business>(items);
		this.fragment = fragment;
	}
	
	public ArrayList<Business> getItems()
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
    public int getOriginalPosition(Business item) {
        return items.indexOf(item);
    }
    
    /**
     * Returns the position of the specified item in the filtered array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item in filtered array
     */
    @Override
    public int getPosition(Business item) {
        return fItems.indexOf(item);
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			Activity activity = (Activity) this.getContext();
			LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.supplier_listrow, null);
		}
		Business business = fItems.get(position);                
		if (business != null) {
			ImageView icon = (ImageView) v.findViewById(R.id.list_icon);
			TextView listText = (TextView) v.findViewById(R.id.list_text);

			if (listText != null && business.getName() != null) {
				listText.setText(business.getName());                            
			}
			// set logo image
			if (icon != null) {
				if (business.getLogoBmp() != null) {												
					icon.setImageBitmap(business.getLogoBmp());
				}
				else {
					int w = 100, h = 70;

					Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
					Bitmap bmp = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap
					icon.setImageBitmap(bmp);
				}

			}

		}
		
		return v;
	}

	/**
	 * Implementing the Filterable interface.
	 */
	public Filter getFilter() {
		if (filter == null) {
			filter = new BusinessFilter();
		}
		return filter;
	}
	
	/**
	 * Custom Filter implementation for the items adapter.
	 *
	 */
	private class BusinessFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {        	 
			FilterResults results = new FilterResults();
			String prefix = constraint.toString().toLowerCase();

			if (prefix == null || prefix.length() == 0)
			{	
				ArrayList<Business> list = new ArrayList<Business>(items);
				results.values = list;
				results.count = items.size();
			}
			else
			{
				final ArrayList<Business> itemsCopy = new ArrayList<Business>(items);
				final ArrayList<Business> list = new ArrayList<Business>();

				for (int i=0; i < itemsCopy.size(); i++)
				{
					final Business b = itemsCopy.get(i);
					final String value = b.getName().toLowerCase();

					if (value.contains(prefix))
					{
						list.add(b);
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
			Business selectedBusiness = (Business) fragment.getListView().getItemAtPosition(position);
			
			BusinessAdapter.this.fItems = (ArrayList<Business>) results.values;
			
			clear();
			int count = fItems.size();
			for (int i=0; i<count; i++)
			{
				Business b = fItems.get(i);
				add(b);
			}
			
			if (count > 0) {
				BusinessAdapter.this.notifyDataSetChanged();
            } else {
            	BusinessAdapter.this.notifyDataSetInvalidated();
            }
			
			if (selectedBusiness != null) {	        	
	        	position = BusinessAdapter.this.getPosition(selectedBusiness);
	        	fragment.getListView().setItemChecked(position, true);
        	}
		}

	}
}
