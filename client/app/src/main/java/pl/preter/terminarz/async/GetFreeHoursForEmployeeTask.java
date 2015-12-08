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

public class GetFreeHoursForEmployeeTask extends
		AsyncTask<Employee, Void, Employee> {

	private ArrayList<String> _alHours;
	private JSONParser jsonParser;
	public boolean success;
	public String message;
	
	@Override
	protected Employee doInBackground(Employee... employees) {
		
		jsonParser = new JSONParser();
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair("date", ApplicationActivity.currentDate));
		postParams.add(new BasicNameValuePair("employeeID", employees[0].getId().toString()));
		JSONObject json = jsonParser.makeHttpRequest(
				ApplicationActivity.URL_GET_HOURS_FOR_EMPLOYEE, "POST", postParams);
        employees[0].getHoursList().clear();

		if (json != null) {
            try {
                message = json.getString(ApplicationActivity.TAG_MESSAGE);
                success = json.getBoolean(ApplicationActivity.TAG_SUCCESS);
                Log.v("hours json", json.toString());

                if (success) {
                    JSONArray hours = json.getJSONArray(ApplicationActivity.TAG_HOURS_ARRAY);
                    for (int i = 0; i < hours.length(); i++) {
                        JSONObject object = hours.getJSONObject(i);
                        String hour = object.getString(ApplicationActivity.TAG_HOUR_FIELD).substring(0, 5);
                        employees[0].addHour(hour);
                    }

                } else {
                    Log.e(this.getClass().getSimpleName() + " Request fail: ", message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
		return employees[0];
	}

}
