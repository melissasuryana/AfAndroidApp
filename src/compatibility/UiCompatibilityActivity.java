package au.com.accountsflow.compatibility;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public abstract class UiCompatibilityActivity extends SherlockFragmentActivity {
	TabHelper mTabHelper;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabHelper = TabHelper.createInstance(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mTabHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTabHelper.onRestoreInstanceState(savedInstanceState);
    }


    /**{@inheritDoc}*/
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
            
    /**
     * Returns the {@link TabHelper} for this activity.
     */
    protected TabHelper getTabHelper() {
        mTabHelper.setUp();
        return mTabHelper;
    }
}
