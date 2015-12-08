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
import pl.preter.terminarz.models.Appointment;
import android.os.AsyncTask;
import android.util.Log;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class GetAllAppointmentsByDateTask extends AsyncTask<String, Void, Multimap<Integer, Appointment>> {
	
	public String message;
	public boolean success;
	
	private JSONParser jsonParser = new JSONParser();
	private Multimap<Integer, Appointment> _appointments;
	
	
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		_appointments = ArrayListMultimap.create();
	}

	@Override
	protected Multimap<Integer, Appointment> doInBackground(String... param) {
		List<NameValuePair> post_params = new ArrayList<NameValuePair>();
		String date = param[0];
		post_params.add(new BasicNameValuePair("date", date));
        Log.d(Thread.currentThread().getName(), "Starting: " + this.toString());
		JSONObject json = jsonParser.makeHttpRequest(ApplicationActivity.URL_GET_APPOINTMENTS, "POST", post_params);
		Log.v("Appointments response", json.toString());
		if(json != null){
			try {
				message = json.getString(ApplicationActivity.TAG_MESSAGE);
				success = json.getBoolean(ApplicationActivity.TAG_SUCCESS);
				if(success){
                    Log.d(Thread.currentThread().getName(), message);
					JSONArray result = json.getJSONArray(ApplicationActivity.TAG_RESULT);
					String godzina, telefon, email, imie, nazwisko, data_utworzenia;
					Integer id, userID, zabiegID, pracownikID;
					boolean confirmed = false;
					
					for(int i = 0; i < result.length(); i++){
						JSONObject obj = result.getJSONObject(i);
						godzina = obj.getString(ApplicationActivity.TAG_HOUR_FIELD);
						telefon = obj.getString(ApplicationActivity.TAG_PHONE);
						email = obj.getString(ApplicationActivity.TAG_EMAIL);
						imie = obj.getString(ApplicationActivity.TAG_NAME);
						nazwisko = obj.getString(ApplicationActivity.TAG_SURNAME);
						pracownikID = obj.getInt(ApplicationActivity.TAG_EMPLOYEE_ID);
						data_utworzenia = obj.getString(ApplicationActivity.TAG_CRATE_DATE);
						id = Integer.parseInt(obj.getString(ApplicationActivity.TAG_ID));
						userID = Integer.parseInt(obj.getString(ApplicationActivity.TAG_USER_ID));
						zabiegID = Integer.parseInt(obj.getString(ApplicationActivity.TAG_SERVICE_ID));
						
						if(obj.getInt(ApplicationActivity.TAG_IS_CONFIRMED) == 1){
							confirmed = true;
						}
						
						Appointment apt = null;
						
						if(email == null && userID == null){
							apt = new Appointment(id, godzina, date, imie, nazwisko, telefon, ApplicationActivity.servicesList.get(zabiegID), data_utworzenia, confirmed);
							_appointments.put(pracownikID, apt);
						} else if(email != null && userID == null){
							apt = new Appointment(id, godzina, date, imie, nazwisko, telefon, email, ApplicationActivity.servicesList.get(zabiegID), data_utworzenia, confirmed);
							_appointments.put(pracownikID, apt);
						} else if(email != null && userID != null){
							apt = new Appointment(id, godzina, date, imie, nazwisko, userID, telefon, email, ApplicationActivity.servicesList.get(zabiegID), data_utworzenia, confirmed);
							_appointments.put(pracownikID, apt);
						}
						
						Log.v("Appointment", apt.toString());
                        Log.d(this.getClass().getSimpleName(), "done: " + Thread.currentThread().getName());
					}
					
				} else {
					Log.e("Appointments - success fail: ", message);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RuntimeException e) {
                e.printStackTrace();
            }
			
		}
		
		return _appointments;
	}

}
