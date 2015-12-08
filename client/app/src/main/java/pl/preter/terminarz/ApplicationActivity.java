package pl.preter.terminarz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import pl.preter.terminarz.async.GetAvailableDatesTask;
import pl.preter.terminarz.models.Employee;
import pl.preter.terminarz.models.Service;

import android.annotation.SuppressLint;
import android.app.Application;

import com.android.volley.RequestQueue;

public final class ApplicationActivity extends Application {

	//main host of php server
	private final static String URL_MAIN_HOST = "http://test.mortis.edl.pl/";
	//script files
	public final static String URL_GET_DATES = URL_MAIN_HOST + "getDates.php";
	public final static String URL_GET_EMPLOYEES_BY_DATE = URL_MAIN_HOST + "getEmployeesByDate.php";
	public final static String URL_GET_APPOINTMENTS = URL_MAIN_HOST + "getAllAppointmentsByDate.php";
//	protected final static String URL_GET_SERVICES_FOR_EMPLOYEE = URL_MAIN_HOST + "getServicesForEmployee.php"; //not sure if needed
	public final static String URL_GET_ALL_SERVICES = URL_MAIN_HOST + "getAllServices.php";
	public final static String URL_GET_HOURS_FOR_EMPLOYEE = URL_MAIN_HOST + "getHoursForEmployeeByDate.php";
	public final static String URL_GET_SERVICES_FOR_EMPLOYEE = URL_MAIN_HOST + "getServicesForEmployee.php";
    public final static String URL_POST_REGISTER_DATA = URL_MAIN_HOST + "registerVisitFromAndroidApp.php";
	public final static String URL_UPDATE_VISIT = "PLACEHOLDER_URL_UPDATE_VISIT";

	// JSON response tags
    public final static String TAG_MESSAGE = "message"; //String
    public final static String TAG_SUCCESS = "success"; //Boolean
    public final static String TAG_RESULT = "result";
    public final static String TAG_IS_CONFIRMED = "potwierdzona"; //Boolean

    //ID TAGS
	public final static String TAG_ID = "ID";
	public final static String TAG_USER_ID = "userID";
	public final static String TAG_SERVICE_ID = "zabiegID";
    public final static String TAG_EMPLOYEE_ID = "pracownikID";
    public final static String TAG_CATEGORY_ID = "kategoriaID";

    //TEXT FIELD TAGS
    public final static String TAG_PHONE = "telefon";
	public final static String TAG_EMAIL = "email";
	public final static String TAG_CRATE_DATE = "data_utworzenia";
    public final static String TAG_SURNAME = "nazwisko";
    public final static String TAG_NAME = "imie";
    public final static String TAG_DATE_FIELD = "data";
    public final static String TAG_HOUR_FIELD = "godzina";
    public final static String TAG_CATEGORY_NAME = "nazwa";

    //ARRAYS TAGS
	public final static String TAG_EMPLOYEES_ARRAY = "employees";
	public final static String TAG_DATES_ARRAY = "dates";
	public final static String TAG_HOURS_ARRAY = "hours";
    public final static String TAG_SERVICES_ARRAY = "services";

    public final static String TAG_THIS_SIMPLE = ApplicationActivity.class.getSimpleName();

	public static final boolean debug = true;
	
	public static HashMap<Integer, Service> servicesList;
	public static ArrayList<Employee> employeesList;
	public static ArrayList<String> alDates;
    public static String currentDate;

    private RequestQueue mRequestQueue;

	public void refreshAvaliableDates() throws InterruptedException, ExecutionException {
		GetAvailableDatesTask task = new GetAvailableDatesTask();
		alDates = task.get();
	}
		
	@SuppressLint("UseSparseArrays")
	@Override
	public void onCreate(){
		servicesList = new HashMap<Integer, Service>();
		employeesList = new ArrayList<Employee>();
		alDates = new ArrayList<String>();
        //currentDate = "DB Error";

	}

    //TODO implement volley library as a standard network request handler; JSONParser.class is a bit... old
}
