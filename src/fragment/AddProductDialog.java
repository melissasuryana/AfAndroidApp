package au.com.accountsflow.fragment;

import java.util.ArrayList;

import com.actionbarsherlock.view.MenuItem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import au.com.accountsflow.AfApplication;
import au.com.accountsflow.AfHelper;
import au.com.accountsflow.CoreAbstractActivity;
import au.com.accountsflow.CurrentOrderActivity;
import au.com.accountsflow.FocusBrandActivity;
import au.com.accountsflow.OrderHistoryActivity;
import au.com.accountsflow.OrderedBeforeActivity;
import au.com.accountsflow.ProductsListActivity;
import au.com.accountsflow.PromotionsActivity;
import au.com.accountsflow.QdaActivity;
import au.com.accountsflow.R;
import au.com.accountsflow.SpecialsActivity;
import au.com.accountsflow.SupplierMenuActivity;
import au.com.accountsflow.arrayadapter.SupplierMenuAdapter;
import au.com.accountsflow.object.Discount;
import au.com.accountsflow.object.OrderItem;
import au.com.accountsflow.object.Product;
import au.com.accountsflow.object.Special;
import au.com.accountsflow.object.SupplierMenuItem;

/**
 * Dialog to add product to current order list
 * 
 */
public class AddProductDialog extends DialogFragment {

	SupplierMenuAdapter menuAdapter;

	public SupplierMenuAdapter createMenuAdapter(Context context, int textViewResId) {

		Resources resources = context.getResources();
		ArrayList<SupplierMenuItem> items = new ArrayList<SupplierMenuItem>();

		SupplierMenuItem quickOrder = new SupplierMenuItem("quick_order", getString(R.string.quick_order_title));
		items.add(quickOrder);

		SupplierMenuItem promotions = new SupplierMenuItem("promotions", getString(R.string.title_product_promotions));
		items.add(promotions);

		SupplierMenuItem specials = new SupplierMenuItem("specials", getString(R.string.title_product_specials));
		items.add(specials);

		SupplierMenuItem focusBrands = new SupplierMenuItem("focus_brands", getString(R.string.title_focus_brand));
		items.add(focusBrands);

		SupplierMenuItem qdaSearch = new SupplierMenuItem("qda_search", getString(R.string.title_product_qda));
		items.add(qdaSearch);

		SupplierMenuItem orderedBefore = new SupplierMenuItem("ordered_before", getString(R.string.title_ordered_before));
		items.add(orderedBefore);

		SupplierMenuItem productList = new SupplierMenuItem("product_list", getString(R.string.title_product_list));
		items.add(productList);

		return new SupplierMenuAdapter(context, this, textViewResId, items);
	}

		
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		menuAdapter = this.createMenuAdapter(this.getActivity().getApplicationContext(), R.layout.suppliers_menu);
		builder.setTitle(R.string.menu_add_product)
		.setNegativeButton(R.string.order_dialog_cancel, null) 
		.setAdapter(menuAdapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int position) {	            	   
				SupplierMenuItem menu = (SupplierMenuItem) menuAdapter.getItem(position);
				CoreAbstractActivity activity = (CoreAbstractActivity) AddProductDialog.this.getActivity();
								
				if (menu.getCode() == "quick_order") {
					QuickOrderDialog orderDialog = new QuickOrderDialog();
					orderDialog.show(activity.getSupportFragmentManager(), "quick_order");
					return;
				}
				
				Bundle args = new Bundle();
				args.putInt("selectedPosition", 0);
				if (menu.getCode() == "product_list") {
					Intent i = new Intent(activity, ProductsListActivity.class);						
					i.putExtra("supplierArgs", args);						
					startActivity(i);
				}
				else if (menu.getCode() == "current_order") {
					Intent i = new Intent(activity, CurrentOrderActivity.class);
					i.putExtra("supplierArgs", args);
					startActivity(i);
				}				
				else if (menu.getCode() == "promotions") {
					Intent i = new Intent(activity, PromotionsActivity.class);
					i.putExtra("supplierArgs", args);
					startActivity(i);
				}
				else if (menu.getCode() == "specials") {
					Intent i = new Intent(activity, SpecialsActivity.class); 
					i.putExtra("supplierArgs", args);
					startActivity(i);
				}
				else if (menu.getCode() == "qda_search") {
					Intent i = new Intent(activity, QdaActivity.class);
					i.putExtra("supplierArgs", args);
					startActivity(i);
				}
				else if (menu.getCode() == "focus_brands") {
					Intent i = new Intent(activity, FocusBrandActivity.class);  
					i.putExtra("supplierArgs", args);
					startActivity(i);
				}
				else if (menu.getCode() == "ordered_before") {
					Intent i = new Intent(activity, OrderedBeforeActivity.class);
					args.remove("selectedPosition");
					i.putExtra("supplierArgs", args);
					startActivity(i);
				}
			}
		});

		return builder.create();
	}
}
