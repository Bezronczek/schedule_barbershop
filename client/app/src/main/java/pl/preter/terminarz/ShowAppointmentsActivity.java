package pl.preter.terminarz;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.HashMap;

import pl.preter.terminarz.adapters.AppointmentsListAdapter;
import pl.preter.terminarz.async.GetAllAppointmentsByDateTask;
import pl.preter.terminarz.async.GetAvailableDatesTask;
import pl.preter.terminarz.async.GetEmployeesByDateTask;
import pl.preter.terminarz.async.GetServicesTask;
import pl.preter.terminarz.models.Appointment;
import pl.preter.terminarz.models.Employee;
import pl.preter.terminarz.models.Service;

import static android.util.Log.*;


public class ShowAppointmentsActivity extends Activity implements ActionBar.OnNavigationListener {
	
	ProgressDialog pDialog;
	String selectedDate;
	TextView tvDebug;
	ActionBar actionBar;
	AppointmentsListAdapter listAdapter;
	ExpandableListView listView;
	ApplicationActivity application;
	int datePos;
	
	@SuppressLint("UseSparseArrays")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUI();
    }

    private void initializeUI() {
        setContentView(R.layout.activity_show_appointments);
        listView = (ExpandableListView) findViewById(R.id.lvAppointmentsExpandable);
        tvDebug = (TextView) findViewById(R.id.tvDebug);
        application = (ApplicationActivity) getApplication();
        actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    }

    @Override
	protected void onResume(){
		//FIXME add handler for empty ApplicationData or wipe references
		super.onResume();
        d("Resuming Activity: ", this.toString());
        showProgressDialog();
        new LoadServices().execute();
	}



	private void showProgressDialog() {
		pDialog = new ProgressDialog(ShowAppointmentsActivity.this);
		pDialog.setIcon(android.R.drawable.ic_menu_info_details);
		pDialog.setTitle("Pobieranie danych");
		pDialog.setMessage("Proszę czekać...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
	}

	
		
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		datePos = itemPosition;
		selectedDate = application.alDates.get(itemPosition).toString();
		new LoadEmployeesByDate().execute(selectedDate);
		pDialog.show();
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.appointments_action_bar, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId()){
		case R.id.action_register:
			startRegisterActivity();
			return true;
		case R.id.action_refresh:
			showProgressDialog();
			new LoadEmployeesByDate().execute(selectedDate);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}

	private void startRegisterActivity() {
		Intent intent = new Intent(getApplicationContext(),
				RegisterVisitActivity.class);
		startActivity(intent);
	}
	
	class LoadAvailableDates extends GetAvailableDatesTask {

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			
			application.alDates = result;
			
			actionBar.setListNavigationCallbacks(
	                new ArrayAdapter<String>(
	                        actionBar.getThemedContext(),
	                        android.R.layout.simple_list_item_1,
	                        android.R.id.text1,
	                        application.alDates), ShowAppointmentsActivity.this);
		}
		
	}
	
	class LoadServices extends GetServicesTask {

		@Override
		protected void onPostExecute(HashMap<Integer, Service> result) {
			super.onPostExecute(result);
			application.servicesList = result;
			new LoadAvailableDates().execute(ShowAppointmentsActivity.this);
			
		}
		
	}
	
	class LoadEmployeesByDate extends GetEmployeesByDateTask {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			application.employeesList.clear();
		}

		@Override
		protected void onPostExecute(ArrayList<Employee> result) {
			super.onPostExecute(result);
			application.employeesList = result;
			if (this.success) {
				LoadAppointmentsByDate task = new LoadAppointmentsByDate();
				task.executeOnExecutor(SERIAL_EXECUTOR, this.selectedDate);
				
			} else {
				tvDebug.setText(message);
			}
		}
		
	}
	
	class LoadAppointmentsByDate extends GetAllAppointmentsByDateTask {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Multimap<Integer, Appointment> result) {
			super.onPostExecute(result);
            for(Employee emp : application.employeesList){
                emp.setAppointments(result);
            }
            if(application.employeesList.isEmpty()){
                setContentView(R.layout.placeholder_no_employees_list);
            } else {
                // replace with setContentView(R.layout.activity_show_appointments); if initializeUI() will not work here
                initializeUI();
                listAdapter = new AppointmentsListAdapter(ShowAppointmentsActivity.this, application.employeesList);
                listView.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();
                if(!ApplicationActivity.debug) {
                    listView.expandGroup(0);
                }
                listView.setOnChildClickListener(new OnChildClickListener() {

                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) {
                        Appointment apt = application.employeesList.get(groupPosition).getAppointment(childPosition);
                        Intent intent = new Intent(ShowAppointmentsActivity.this, AppointmentDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("appointmentID", apt.getId());
                        bundle.putInt("groupPosition", groupPosition);
                        bundle.putInt("childPosition", childPosition);
                        bundle.putString("selectedDate", selectedDate);
                        bundle.putString("selectedHour", apt.getHour());
                        bundle.putInt("serviceID", apt.getService().getID());
                        intent.putExtra("passedData", bundle);
                        startActivity(intent);
                        return false;
                    }
                });
            }
			pDialog.dismiss();
		}
		
	}
	
}
