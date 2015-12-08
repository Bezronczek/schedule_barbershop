package pl.preter.terminarz.async;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.preter.terminarz.ApplicationActivity;
import pl.preter.terminarz.JSONParser;
import pl.preter.terminarz.models.Service;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

@SuppressLint("UseSparseArrays")

public class GetServicesTask extends AsyncTask<Void, Void, HashMap<Integer, Service>>{
	
	public String message;
	public boolean success;
	
	private JSONParser jsonParser = new JSONParser();
	private HashMap<Integer, Service> servicesList;
	
	
	@Override
	protected void onPreExecute() {
		servicesList = new HashMap<Integer, Service>();
		super.onPreExecute();
	}
	

	@Override
	protected HashMap<Integer, Service> doInBackground(Void... params) {
        Log.i(Thread.currentThread().getName(), "Starting network request");
        Log.d(this.toString(), ApplicationActivity.URL_GET_ALL_SERVICES);
		JSONObject json = jsonParser.getJSONFromUrl(ApplicationActivity.URL_GET_ALL_SERVICES);
		
		if(json != null){
			try {
                Log.v("services", json.toString());
				message = json.getString(ApplicationActivity.TAG_MESSAGE);
				success = json.getBoolean(ApplicationActivity.TAG_SUCCESS);
                Log.d(Thread.currentThread().getName(), message);
				
				if(success){
					JSONArray services = json.getJSONArray(ApplicationActivity.TAG_SERVICES_ARRAY);
					
					for(int i = 0; i < services.length(); i++){
						JSONObject object = services.getJSONObject(i);
						Service srv = new Service(object.getInt(ApplicationActivity.TAG_ID), object.getString(ApplicationActivity.TAG_CATEGORY_NAME));
						servicesList.put(srv.getID(), srv);
					}
				} else {
					// TODO add handler for empty response
                    Log.e(Thread.currentThread().getName(), message);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RuntimeException e) {
                e.printStackTrace();
            }
		}
		
		return servicesList;
	}
	
}
