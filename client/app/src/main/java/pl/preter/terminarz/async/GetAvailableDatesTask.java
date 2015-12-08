package pl.preter.terminarz.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.preter.terminarz.ApplicationActivity;
import pl.preter.terminarz.JSONParser;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

/**
 * @author Artur Strz&#x119;pka
 * Extends this class to fetch or refresh avaliable dates.
 * Result should be stored in @see ApplicationActivity#alDates
 * No parameters required.
 */

public class GetAvailableDatesTask extends AsyncTask<Context, Void, ArrayList<String>> {
	
	public String message;
	public boolean success;
	
	private JSONParser jsonParser = new JSONParser();
	private ArrayList<String> _dates;
	
	

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		_dates = new ArrayList<String>();
	}



	@Override
	protected ArrayList<String> doInBackground(Context... params) {
		Log.d(Thread.currentThread().getName(), "starting");
		JSONObject json = jsonParser.getJSONFromUrl(ApplicationActivity.URL_GET_DATES);

		if (json != null) {
			try {
                Log.v("daty", json.toString());
				message = json.getString(ApplicationActivity.TAG_MESSAGE);
				success = json.getBoolean(ApplicationActivity.TAG_SUCCESS);
				if (success) {
					JSONArray dates = json.getJSONArray(ApplicationActivity.TAG_DATES_ARRAY);
					Log.v("dates array: ", dates.toString());

					for (int i = 0; i < dates.length(); i++) {
						JSONObject object = dates.getJSONObject(i);
						String date = object.getString(ApplicationActivity.TAG_DATE_FIELD);
						_dates.add(date);
					}
                    Log.d(Thread.currentThread().getName(), "done");
				} else {
					//TODO Handler for success = false
                    Log.e(Thread.currentThread().getName(), message);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (RuntimeException e) {
                e.printStackTrace();
                AlertDialog.Builder adb = new AlertDialog.Builder(params[0]);
                AlertDialog ad = adb.create();
                ad.setMessage("Connection Error");
                ad.show();

            }

		}

		return _dates;
	}
}


