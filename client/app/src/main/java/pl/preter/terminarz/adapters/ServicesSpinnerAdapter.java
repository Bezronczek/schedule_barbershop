package pl.preter.terminarz.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl.preter.terminarz.R;
import pl.preter.terminarz.models.Employee;
import pl.preter.terminarz.models.Service;

/**
 * Created by Mort on 2014-06-26.
 */
public class ServicesSpinnerAdapter extends BaseAdapter {

    private ArrayList<Service> _data;
    private Context _context;
    private TextView _tvLabel;

    public ServicesSpinnerAdapter(Context context, Employee emp){
        _data = emp.getAllServices();
        _context = context;
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int position) {
        return _data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.services_spinner_view, null);
        }

        _tvLabel = (TextView) view.findViewById(R.id.tvServiceSpinnerLabel);
        _tvLabel.setText(_data.get(position).getName());

        return view;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.services_spinner_view, null);
        }

        _tvLabel = (TextView) view.findViewById(R.id.tvServiceSpinnerLabel);
        _tvLabel.setText(_data.get(position).getName());

        return view;
    }
}
