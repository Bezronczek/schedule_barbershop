package pl.preter.terminarz;

import java.util.ArrayList;
import java.util.Collections;

import pl.preter.terminarz.adapters.EmployeeSpinnerAdapter;
import pl.preter.terminarz.adapters.ServicesSpinnerAdapter;
import pl.preter.terminarz.async.GetEmployeesByDateTask;
import pl.preter.terminarz.async.GetFreeHoursForEmployeeTask;
import pl.preter.terminarz.async.GetServicesForEmployeeTask;
import pl.preter.terminarz.models.Appointment;
import pl.preter.terminarz.models.Employee;
import pl.preter.terminarz.models.PHPRequestData;
import pl.preter.terminarz.models.Service;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AppointmentDetailsActivity extends Activity implements ActionBar.OnNavigationListener{

	private Integer childPosition, groupPosition, appointmentID;
	private Appointment appointment;
    private Employee _employee;
	private ApplicationActivity application;
	private TextView tvName, tvSurname, tvService, tvEmployeeName, tvPhone, tvEmail, tvHour, 
						tvDate, tvCreationDate, tvConfirmed, tvDebug;
	private EditText etName, etSurname, etPhone, etEmail;
	private Spinner sDate, sService, sTime, sEmployeeName, sIsConfirmed;
	private ActionBar actionBar;
	private MenuInflater menuInflater;
	private boolean editMode = false;
    private boolean initSpinners = true;
	private String selectedDate;
	private int datePos, serviceID;
    private PHPRequestData requestData;
    private ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (ApplicationActivity) getApplication();
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("passedData");
		childPosition = bundle.getInt("childPosition");
		groupPosition = bundle.getInt("groupPosition");
		appointmentID = bundle.getInt("appointmentID");
		selectedDate = bundle.getString("selectedDate");
		serviceID = bundle.getInt("serviceID");
		appointment = application.employeesList.get(groupPosition).getAppointment(childPosition);
		actionBar = getActionBar();
        assert actionBar != null;
		actionBar.setDisplayHomeAsUpEnabled(true);
        ApplicationActivity.currentDate = selectedDate;
        _employee = ApplicationActivity.employeesList.get(groupPosition);
        _employee.getHoursList().clear();
        _employee.getAllServices().clear();
        _employee.getAppointments().clear();

        pDialog = new ProgressDialog(this);
        pDialog.setIcon(android.R.drawable.ic_menu_info_details);
        pDialog.setTitle("Pobieranie danych");
        pDialog.setMessage("Prosze czekac");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

		switchToViewMode();
		
	}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.action_edit :
                switchToEditView();
                return true;

            case R.id.action_discard:
                switchToViewMode();
                return true;

            default :
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater = getMenuInflater();
        if(editMode){
            menuInflater.inflate(R.menu.appointment_detail_action_bar_edit_mode, menu);
        } else {
            menuInflater.inflate(R.menu.appointment_detail_action_bar, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        // TODO Auto-generated method stub
        return false;
    }

	protected void switchToViewMode() {
		setContentView(R.layout.activity_appointment_details);
		tvName = (TextView) findViewById(R.id.tvAppointmentNameValue);
		tvSurname = (TextView) findViewById(R.id.tvAppointmentSurnameValue);
		tvService = (TextView) findViewById(R.id.tvAppointmentServiceValue);
		tvEmployeeName = (TextView) findViewById(R.id.tvAppointmentEmployeeValue);
		tvPhone = (TextView) findViewById(R.id.tvAppointmentPhoneValue);
		tvEmail = (TextView) findViewById(R.id.tvAppointmentEmailValue);
		tvHour = (TextView) findViewById(R.id.tvAppointmentHourValue);
		tvDate = (TextView) findViewById(R.id.tvAppointmentDateValue);
		tvCreationDate = (TextView) findViewById(R.id.tvAppointmentRegisteredValue);
		tvConfirmed = (TextView) findViewById(R.id.tvAppointmentConfirmedValue);
		
		
		tvName.setText(appointment.getClientName());
		tvSurname.setText(appointment.getClientSurname());
		tvService.setText(appointment.getService().getName());
		tvEmployeeName.setText(application.employeesList.get(groupPosition).getName());
		tvPhone.setText(appointment.getPhone());
		tvEmail.setText(appointment.getEmail());
		tvHour.setText(appointment.getHour().substring(0, 5));
		tvDate.setText(selectedDate);
		tvCreationDate.setText(appointment.getCreateDate());
		if(appointment.isConfirmed()){
			tvConfirmed.setText("Tak");
		} else {
			tvConfirmed.setText("Nie");
		}
		editMode = false;
        initSpinners = true;
		
		invalidateOptionsMenu();
	}

	private void switchToEditView(){
		editMode = true;
		invalidateOptionsMenu();
		setContentView(R.layout.activity_appointment_edit);
		sEmployeeName = (Spinner) findViewById(R.id.sAppointmentEmployeeValueEdit);
		sDate = (Spinner) findViewById(R.id.sAppointmentDateValueEdit);
		sTime = (Spinner) findViewById(R.id.sAppointmentHourValueEdit);
		sService = (Spinner) findViewById(R.id.sAppointmentServiceValueEdit);
		etName = (EditText) findViewById(R.id.etAppointmentNameValueEdit);
		etSurname = (EditText) findViewById(R.id.etAppointmentSurnameValueEdit);
		etEmail = (EditText) findViewById(R.id.etAppointmentEmailValueEdit);
		etPhone = (EditText) findViewById(R.id.etAppointmentPhoneValueEdit);
        tvConfirmed = (TextView) findViewById(R.id.tvAppointmentConfirmedValueEdit);
        tvCreationDate = (TextView) findViewById(R.id.tvAppointmentRegisteredValueEdit);
        tvDebug = (TextView) findViewById(R.id.tvDebug);

		etName.setText(appointment.getClientName());
		etSurname.setText(appointment.getClientSurname());
		etPhone.setText(appointment.getPhone());
		etEmail.setText(appointment.getEmail());
        tvCreationDate.setText(appointment.getCreateDate());

        if(appointment.isConfirmed()){
            tvConfirmed.setText("Tak");
        } else {
            tvConfirmed.setText("Nie");
        }
        prepareSpinnerData();
        spinnerListeners();

	}

    private void prepareSpinnerData(){
        //TODO prepare adapters and display data
        sDate.setAdapter(new ArrayAdapter<String>(this, R.layout.navigation_item, ApplicationActivity.alDates));
        if(initSpinners) sDate.setSelection(ApplicationActivity.alDates.lastIndexOf(ApplicationActivity.currentDate));
        //new LoadFreeHoursForEmployeeByDate().execute(ApplicationActivity.employeesList.get(groupPosition));

    }
    protected void spinnerListeners() {
        sDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pDialog.show();
                ApplicationActivity.employeesList.clear();
                ApplicationActivity.currentDate = parent.getSelectedItem().toString();
                new LoadEmployeesByDateTask().execute(ApplicationActivity.currentDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sEmployeeName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //initSpinners = true;
                if(!(pDialog.isShowing())){
                    pDialog.show();
                }
                new LoadFreeHoursForEmployeeByDate().execute(ApplicationActivity.employeesList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private boolean checkFieldsData(){

        if(etName == null || etSurname == null || etPhone == null || etEmail == null || sDate == null
                || sEmployeeName == null || sTime == null || sService == null) {
            return false;
        } else if(etName.getText().length() == 0 || etSurname.getText().length() == 0 || etPhone.getText().length() == 0 ||
            etEmail.getText().length() == 0 || sDate.getAdapter().getCount() == 0 || sEmployeeName.getCount() == 0 ||
            sTime.getAdapter().getCount() == 0 || sService.getAdapter().getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void updateAppointment(View view){

        requestData = new PHPRequestData();
        if(checkFieldsData()){
            requestData.setName(etName);
            requestData.setSurname(etSurname.getText().toString());
            requestData.setPhone(etPhone.getText().toString());
            requestData.setEmail(etEmail.getText().toString());
            requestData.setDate(sDate.getSelectedItem().toString());
            requestData.setEmployee((Employee) sEmployeeName.getSelectedItem());
            requestData.setHour(sTime.getSelectedItem().toString());
            requestData.setService((Service) sService.getSelectedItem());

            tvDebug.setText(requestData.get_params().toString());

        } else {
            tvDebug.setText("An Error occurred - see Logcat for more details");
        }

    }

    protected class LoadEmployeesByDateTask extends GetEmployeesByDateTask {
        @Override
        protected void onPostExecute(ArrayList<Employee> employees) {
            sEmployeeName.setAdapter(new EmployeeSpinnerAdapter(AppointmentDetailsActivity.this, employees));
            if(initSpinners) sEmployeeName.setSelection(groupPosition);
        }
    }

    protected class LoadFreeHoursForEmployeeByDate extends GetFreeHoursForEmployeeTask {

        @Override
        protected void onPostExecute(Employee emp) {
            boolean emptyHours = emp.getHoursList().isEmpty() && !(emp.getId().equals(_employee.getId()));
            if(emptyHours){
                ApplicationActivity.employeesList.remove(emp);
                sEmployeeName.setAdapter(new EmployeeSpinnerAdapter(AppointmentDetailsActivity.this, ApplicationActivity.employeesList));
                this.success = false;
            } else {
                boolean b = !(emp.getHoursList().contains(appointment.getHour().substring(0,5))) && emp.getId().equals(_employee.getId());
                if(b) {
                    emp.addHour(appointment.getHour().substring(0, 5));
                    Collections.sort(emp.getHoursList());
                }
            }
            sTime.setAdapter(new ArrayAdapter<String> (AppointmentDetailsActivity.this, R.layout.navigation_item, emp.getHoursList()));
               if(emp.getHoursList().contains(appointment.getHour().substring(0, 5))) {
                   sTime.setSelection(emp.getHoursList().lastIndexOf(appointment.getHour().substring(0, 5)));
               }
            new LoadServicesForEmployee().execute(emp);
        }
    }

    protected class LoadServicesForEmployee extends GetServicesForEmployeeTask {

        @Override
        protected void onPostExecute(Employee employee) {
            if (success) {
                ServicesSpinnerAdapter adapter = new ServicesSpinnerAdapter(AppointmentDetailsActivity.this, employee);
                sService.setAdapter(adapter);
                if(initSpinners) sService.setSelection(employee.getAllServices().lastIndexOf(ApplicationActivity.servicesList.get(serviceID)));
                initSpinners = false;
                pDialog.dismiss();
            } else {
                Log.e(this.getClass().getSimpleName(), message);
            }

        }
    }
}
