package pl.preter.terminarz.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl.preter.terminarz.R;
import pl.preter.terminarz.models.Employee;

/**
 * Created by Mort on 2014-06-18.
 */
public class EmployeeSpinnerAdapter extends BaseAdapter {

    private ArrayList<Employee> _data;
    private Context _context;
    private TextView _tvLabel;

    public EmployeeSpinnerAdapter(Context context, ArrayList<Employee> employees)
    {
        _data = employees;
        _context = context;
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int position)    {
        return _data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.services_spinner_view, null);
        }

        _tvLabel = (TextView) convertView.findViewById(R.id.tvServiceSpinnerLabel);
        _tvLabel.setText(_data.get(position).getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.services_spinner_view, null);
        }

        _tvLabel = (TextView) convertView.findViewById(R.id.tvServiceSpinnerLabel);
        _tvLabel.setText(_data.get(position).getName());

        return convertView;
    }
}
