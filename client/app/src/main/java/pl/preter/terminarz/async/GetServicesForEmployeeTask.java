package pl.preter.terminarz.async;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.preter.terminarz.ApplicationActivity;
import pl.preter.terminarz.JSONParser;
import pl.preter.terminarz.models.Employee;
import android.os.AsyncTask;
import android.util.Log;

public class GetServicesForEmployeeTask extends
		AsyncTask<Employee, Void, Employee> {
	
	public String message;
	public boolean success = false;
	
	private JSONParser jsonParser;
	private Employee _employee;
	
	@Override
	protected void onPreExecute() {
		jsonParser = new JSONParser();
		super.onPreExecute();
	}

	@Override
	protected Employee doInBackground(Employee... arg0) {
		_employee = arg0[0];
        //_employee.getAllServices().clear();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("employeeID", _employee.getId().toString()));
		JSONObject json = jsonParser.makeHttpRequest(ApplicationActivity.URL_GET_SERVICES_FOR_EMPLOYEE, "POST", params);
		Log.v(this.getClass().getSimpleName(), json.toString());

		try {
			message = json.getString(ApplicationActivity.TAG_MESSAGE);
			success = json.getBoolean(ApplicationActivity.TAG_SUCCESS);
			if(success){
				JSONArray services = json.getJSONArray(ApplicationActivity.TAG_SERVICES_ARRAY);

				for(int i = 0; i < services.length(); i++){
					JSONObject object = services.getJSONObject(i);
					//Service srv = new Service(object.getInt(ApplicationActivity.TAG_CATEGORY_ID), object.getString(ApplicationActivity.TAG_CATEGORY_NAME));
					_employee.addService(ApplicationActivity.servicesList.get(object.getInt(ApplicationActivity.TAG_CATEGORY_ID)));
				}
			} else {
				// TODO add handler for empty response
                   Log.e("Request fail: ", this.toString());
                   Log.e("Request fail: ", Thread.currentThread().getName());
                   Log.e("Request fail: ", message);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeException e) {
            e.printStackTrace();
        }
		
		return this._employee;
	}

}
