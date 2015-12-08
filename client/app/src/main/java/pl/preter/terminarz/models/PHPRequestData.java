package pl.preter.terminarz.models;

import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mort on 2014-07-12.
 */
public class PHPRequestData {

    private List<NameValuePair> _params;
    private String _url;

    public PHPRequestData(List<NameValuePair> params, String url) {
        this._params = params;
        this._url = url;
    }

    public PHPRequestData(){
        _params = new ArrayList<NameValuePair>();
    }

    @Override
    public String toString() {
        return "PHPRequestData{" +
                "_params=" + _params +
                ", _url='" + _url + '\'' +
                '}';
    }

    public List<NameValuePair> get_params() {
        return _params;
    }

    public String get_url() {
        return _url;
    }

    public void set_params(List<NameValuePair> _params) {
        this._params = _params;
    }

    public void set_url(String _url) {
        this._url = _url;
    }


    public void setName(String name) {
        _params.add(new BasicNameValuePair("name", name));
    }

    public PHPRequestData setName(EditText name) {
        _params.add(new BasicNameValuePair("name", name.getText().toString()));
        return this;
    }

    public void setSurname(String surname) {
        _params.add(new BasicNameValuePair("surname", surname));
    }

    public void setHour(String hour) {
        _params.add(new BasicNameValuePair("hour", hour));
    }

    public void setService(Service srv) {
        _params.add(new BasicNameValuePair("serviceID", srv.getID().toString()));
    }

    public void setEmployee(Employee emp) {
        _params.add(new BasicNameValuePair("employeeID", emp.getId().toString()));
    }

    public void setDate(String date) {
        _params.add(new BasicNameValuePair("date", date));
    }

    public void setEmail(String mail) {
        _params.add(new BasicNameValuePair("email", mail));
    }

    public void setPhone(String phone) {
        _params.add(new BasicNameValuePair("phone", phone));
    }
}
