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


public class GetEmployeesByDateTask extends AsyncTask<String, Void, ArrayList<Employee>> {
	
	public String message;
	public boolean success;
	public String selectedDate;
	
	private ArrayList<Employee> _employeesList = ApplicationActivity.employeesList;
	private JSONParser jsonParser = new JSONParser();
	
	

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}



	@Override
	protected ArrayList<Employee> doInBackground(String... arg0) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		selectedDate = arg0[0];
		Log.v("date value", arg0[0]);
		params.add(new BasicNameValuePair("date", selectedDate));
		Log.d(Thread.currentThread().getName(), "starting: " + this.toString());
		JSONObject json = jsonParser.makeHttpRequest(
				ApplicationActivity.URL_GET_EMPLOYEES_BY_DATE, "POST", params);
		Log.v("JSON response", json.toString());

		try {
			if (json != null) {
				message = json.getString(ApplicationActivity.TAG_MESSAGE);
				success = json.getBoolean(ApplicationActivity.TAG_SUCCESS);
				if (success) {

					JSONArray employees = json.getJSONArray(ApplicationActivity.TAG_EMPLOYEES_ARRAY);
					Log.v("employees array: ", employees.toString());

					for (int i = 0; i < employees.length(); i++) {
						JSONObject object = employees.getJSONObject(i);
						String name = object.getString(ApplicationActivity.TAG_NAME) + " "
								+ object.getString(ApplicationActivity.TAG_SURNAME);
						Employee emp = new Employee(name,
								object.getInt(ApplicationActivity.TAG_EMPLOYEE_ID));
						_employeesList.add(emp);
					}
					Log.d(this.getClass().getSimpleName(), "done: " + Thread.currentThread().getName());
				} else {
					// TODO add handler for null data
                    Log.e("Request fail: ", this.toString());
                    Log.e("Request fail: ", Thread.currentThread().getName());
                    Log.e("Request fail: ", message);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
            e.printStackTrace();
        }

		return _employeesList;
	}

}
