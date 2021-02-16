package au.com.accountsflow;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Singleton class to create HTTP client instance 
 * @author melissa.suryana
 *
 */
public class SingletonHttpClient {
	private static HttpClient mHttpClient = null;	
	
	public static HttpClient getInstance(){
		if( mHttpClient == null ){
			mHttpClient = new DefaultHttpClient();
		}
		return mHttpClient;
	}
}
