package pl.preter.terminarz;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import junit.framework.Assert;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.preter.terminarz.adapters.EmployeeSpinnerAdapter;
import pl.preter.terminarz.adapters.ServicesSpinnerAdapter;
import pl.preter.terminarz.async.GetAvailableDatesTask;
import pl.preter.terminarz.async.GetEmployeesByDateTask;
import pl.preter.terminarz.async.GetFreeHoursForEmployeeTask;
import pl.preter.terminarz.async.GetServicesForEmployeeTask;
import pl.preter.terminarz.async.PostDataToPHPScript;
import pl.preter.terminarz.models.Employee;
import pl.preter.terminarz.models.PHPRequestData;

public class RegisterVisitActivity extends Activity {

    private EditText etName, etSurname, etPhoneNumber, etEmail;
    private Spinner sDate, sTime, sService, sEmployeeName;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();
    private ActionBar actionBar;
    private ApplicationActivity application;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (ApplicationActivity) getApplication();
        switchToViewMode();
        new LoadAvailableDates().execute();

    }

    protected void switchToViewMode() {
        setContentView(R.layout.activity_register_visit);
        etName = (EditText) findViewById(R.id.etName);
        etSurname = (EditText) findViewById(R.id.etSurname);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etEmail = (EditText) findViewById(R.id.etEmail);
        sDate = (Spinner) findViewById(R.id.sDate);
        sTime = (Spinner) findViewById(R.id.sHour);
        sService = (Spinner) findViewById(R.id.sService);
        sEmployeeName = (Spinner) findViewById(R.id.sWorker);
        actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        spinnerListeners();

    }

    protected void spinnerListeners() {
        sTime.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO implement checking if chosen time is not taken yet and reserve it

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        sDate.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                ApplicationActivity.employeesList.clear();
                ApplicationActivity.currentDate = arg0.getSelectedItem().toString();
                new LoadEmployeesByDateTask().execute(ApplicationActivity.currentDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        sEmployeeName.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {

                new LoadFreeHoursForEmployeeByDate().execute(ApplicationActivity.employeesList.get(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    // TODO rewrite this method to use custom adapters
    private void populateSpinnerWithData(Spinner spinner, ArrayList<String> data) {

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, data);
        spinner.setAdapter(spinnerAdapter);

    }

    // TODO rewrite this method with new data model
    @SuppressWarnings("unchecked")
    public void postRegisterData(View view) {
        Integer empId = ApplicationActivity.employeesList.get(sEmployeeName.getSelectedItemPosition()).getId();
        Integer srvId = ApplicationActivity.employeesList.get(sEmployeeName.getSelectedItemPosition()).getAllServices().get(sService.getSelectedItemPosition()).getID();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", etName.getText().toString()));
        params.add(new BasicNameValuePair("surname", etSurname.getText().toString()));
        params.add(new BasicNameValuePair("phoneNumber", etPhoneNumber.getText().toString()));
        params.add(new BasicNameValuePair("email", etEmail.getText().toString()));

        if (sDate.getSelectedItem() != null) {
            params.add(new BasicNameValuePair("date", sDate.getSelectedItem()
                    .toString()));
        }
        if (sTime.getSelectedItem() != null) {
            params.add(new BasicNameValuePair("time", sTime.getSelectedItem()
                    .toString() + ":00"));
        }

        if (sService.getSelectedItem() != null) {
            params.add(new BasicNameValuePair("service", srvId.toString()));
        }
        if (sEmployeeName.getSelectedItem() != null) {
            params.add(new BasicNameValuePair("worker", empId.toString()));
        }

        PHPRequestData requestData = new PHPRequestData(params, ApplicationActivity.URL_POST_REGISTER_DATA);

        new PostRegisterDataToPHP().execute(params);

        new PostDataToPHPScript().execute(requestData);

    }

    public void prepareRegisterData(View view) {

    }


    protected class PostRegisterDataToPHP extends AsyncTask<List<NameValuePair>, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterVisitActivity.this);
            pDialog.setMessage("Wysylanie danych na serwer");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(List<NameValuePair>... arg0) {
            Log.d("post request!", arg0[0].toString());
            JSONObject json = jsonParser.makeHttpRequest(ApplicationActivity.URL_POST_REGISTER_DATA, "POST", arg0[0]);
            Log.d("register", json.toString());
            return json.toString();
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            ApplicationActivity.alDates.clear();
            etEmail.setText("");
            etName.setText("");
            etPhoneNumber.setText("");
            etSurname.setText("");
            new LoadAvailableDates().execute();
            Toast.makeText(RegisterVisitActivity.this, "Sukces, odswierzam dane", Toast.LENGTH_LONG).show();
        }
    }

    protected class LoadAvailableDates extends GetAvailableDatesTask {
        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            ApplicationActivity.alDates = result;
            if (success) {
                populateSpinnerWithData(sDate, application.alDates);
            }
        }
    }

    protected class LoadEmployeesByDateTask extends GetEmployeesByDateTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterVisitActivity.this);
            pDialog.setIcon(android.R.drawable.ic_menu_info_details);
            pDialog.setTitle("Pobieranie danych");
            pDialog.setMessage("Prosze czekac...");
            // FIXME remove comment block in release version
            // pDialog.setIndeterminate(false);
            // pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Employee> employees) {
            super.onPostExecute(employees);
            ApplicationActivity.employeesList = employees;
            if(success) {
                EmployeeSpinnerAdapter adapter = new EmployeeSpinnerAdapter(RegisterVisitActivity.this, employees);
                sEmployeeName.setAdapter(adapter);
            }
            pDialog.dismiss();
        }
    }

    protected class LoadFreeHoursForEmployeeByDate extends GetFreeHoursForEmployeeTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterVisitActivity.this);
            pDialog.setIcon(android.R.drawable.ic_menu_info_details);
            pDialog.setTitle("Pobieranie danych");
            pDialog.setMessage("Proszę czekac...");
            // FIXME remove comment block in release version
            // pDialog.setIndeterminate(false);
            // pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Employee emp) {
            super.onPostExecute(emp);
            pDialog.dismiss();
            if (emp.getHoursList().isEmpty()) {
                ApplicationActivity.employeesList.remove(emp);
                EmployeeSpinnerAdapter adapter = new EmployeeSpinnerAdapter(RegisterVisitActivity.this, ApplicationActivity.employeesList);
                sEmployeeName.setAdapter(adapter);
                this.success = false;
            } else {

                if (!(emp.getAllServices().isEmpty())) {
                    emp.getAllServices().clear();
                }
                new LoadServicesForEmployee().execute(emp);

                if (this.success) populateSpinnerWithData(sTime, emp.getHoursList());
            }
        }
    }

    protected class LoadServicesForEmployee extends GetServicesForEmployeeTask {

        @Override
        protected void onPostExecute(Employee employee) {
            super.onPostExecute(employee);
            if (success) {
                ServicesSpinnerAdapter adapter = new ServicesSpinnerAdapter(RegisterVisitActivity.this, employee);
                sService.setAdapter(adapter);
            } else {
                Log.e(this.toString(), message);
            }
        }
    }




}
