package au.com.accountsflow.arrayadapter;

import java.util.ArrayList;
import android.widget.ArrayAdapter;

/**
 * Base interface for other array adapters
 * @author melissa.suryana
 *
 * @param <T>
 */
public interface AfArrayAdapter<T> {
	public ArrayList<T> getItems();
	public int getOriginalPosition(T item);
}
