package au.com.accountsflow;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.widget.Toast;

/**
 * Helper class for static functions
 * @author melissa.suryana
 *
 */
public class AfHelper {
	
	/**
	 * Helper function to show popup message
	 * @param Context context
	 * @param CharSequence text
	 */
	public static void showToast(Context context, CharSequence text) {
    	int duration = Toast.LENGTH_SHORT;

    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();	
	}
	
	/**
	 * Helper function to show alert box
	 * @param Context context
	 * @param CharSequence text
	 */
	public static void showAlert(Context context, CharSequence text) {
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
								
		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(text)
		       .setTitle(R.string.error_title)
		       .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       
                   }
		       });

		// 3. Get the AlertDialog from create()
		//AlertDialog dialog = builder.create();
		builder.show();
	}
	
	/**
	 * Helper function to return base URL for HTTP request
	 * 
	 * @param res Resources
	 * @return String base URL
	 */
	public static String getBaseUrl(Resources res) {
        if (res.getBoolean(R.bool.use_live)) {
        	return res.getString(R.string.live_url);
        }
        else {
        	return res.getString(R.string.demo_url);
        }
	}
	
	/**
	 * Helper function to send HTTP POST request 
	 * @param r Resources
	 * @param c Context
	 * @param path String
	 * @param requestArgs List<NameValuePair>
	 * @param generalError String
	 * @return HttpResponse
	 * @throws Exception 
	 */
	public static HttpResponse sendPostRequest(Resources r, Context c, String path, List<NameValuePair> requestArgs, String generalError) throws Exception {
		// Create a new HttpClient and Post Header
        HttpClient httpclient = SingletonHttpClient.getInstance();   
        
        String url = AfHelper.getBaseUrl(r);
        url += path;
        HttpPost httppost = new HttpPost(url);        
        
        try {
        	// Fix for android version 2.2
        	HttpParams params = new BasicHttpParams();  

        	params.setBooleanParameter("http.protocol.expect-continue", false);

        	httppost.setParams(params);
        	
            // Add user name and password  
            httppost.setEntity(new UrlEncodedFormEntity(requestArgs));
 
            // Execute HTTP Post Request	            
            HttpResponse response = httpclient.execute(httppost);            
            
            // Only parse the response if status is OK
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            	return response;
            }
            // Else show reason on popup message
            else {	
            	throw new Exception("HTTP response status is not OK. Status code: " + response.getStatusLine().getStatusCode() );
            	//AfHelper.showAlert(c, generalError);
            } 
            
        } catch (ClientProtocolException e) { 
        	
        	throw new Exception(e.getMessage());
        	//AfHelper.showAlert(c, generalError);   
        	
        } catch (IOException e) {     
        	throw new Exception(e.getMessage());
        	//AfHelper.showAlert(c, generalError);
        	
        }
	}
	
	/**
	 * Helper function to send HTTP POST request 
	 * @param r Resources
	 * @param c Context
	 * @param path String
	 * @param requestArgs List<NameValuePair>
	 * @param generalError String
	 * @return HttpResponse
	 * @throws Exception 
	 */
	public static HttpResponse sendGetRequest(Resources r, Context c, String path, HttpParams params, String generalError) throws Exception {
		// Create a new HttpClient and Post Header
        HttpClient httpclient = SingletonHttpClient.getInstance();   
        
        String url = AfHelper.getBaseUrl(r);
        url += path;
        HttpGet httpGet = new HttpGet();        
               
        try {          
        	httpGet.setURI(new URI(url));
        	httpGet.setParams(params);
        	
            // Execute HTTP get Request	            
            HttpResponse response = httpclient.execute(httpGet);
            
            // Only parse the response if status is OK
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            	return response;
            }            
            // Else show reason on popup message
            else {	
            	throw new Exception("HTTP response status is not OK");
            	//AfHelper.showAlert(c, generalError);
            }  
            
        } catch (URISyntaxException e) {
        	throw new Exception(e.getMessage());
        	//AfHelper.showAlert(c, generalError);
        	
        } catch (ClientProtocolException e) { 
        	
        	throw new Exception(e.getMessage());
        	//AfHelper.showAlert(c, generalError);   
        	
        } catch (IOException e) {     
        	throw new Exception(e.getMessage());
        	//AfHelper.showAlert(c, generalError);
        	
        }
	}
}
