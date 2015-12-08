package pl.preter.terminarz.adapters;

import java.util.ArrayList;

import pl.preter.terminarz.R;
import pl.preter.terminarz.models.Employee;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class AppointmentsListAdapter extends BaseExpandableListAdapter{
	
	private Context _context;
	private ArrayList<Employee> _data;
    private String hour, name, service, phone;
	
	public AppointmentsListAdapter(Context context, ArrayList<Employee> data) {
		this._context = context;
		this._data = data;

	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return this._data.get(groupPosition).getAppointment(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
        try {
            hour = this._data.get(groupPosition).getAppointment(childPosition).getHour().substring(0, 5);
            name = this._data.get(groupPosition).getAppointment(childPosition).getClientFullName();
            service = this._data.get(groupPosition).getAppointment(childPosition).getService().getName();
            phone = "tel. " + this._data.get(groupPosition).getAppointment(childPosition).getPhone();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
		
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.group_item, null);
		}
		
		TextView tvItemHour = (TextView) convertView.findViewById(R.id.tvItemHour);
		TextView tvItemName = (TextView) convertView.findViewById(R.id.tvItemClientName);
		TextView tvServiceName = (TextView) convertView.findViewById(R.id.tvItemServiceName);
		TextView tvPhoneNumber = (TextView) convertView.findViewById(R.id.tvItemPhone);
		
		tvItemHour.setText(hour);
		tvItemName.setText(name);
		tvServiceName.setText(service);
		tvPhoneNumber.setText(phone);
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition){
        if(this._data.isEmpty()){
            return 0;
        } else {
            return this._data.get(groupPosition).getAppointments().size();
        }
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._data.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._data.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		
		String groupTitle = this._data.get(groupPosition).getName();
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.group_header, null);
		}
		
		TextView tvGroupHeader = (TextView) convertView.findViewById(R.id.tvGroupHeader);
		TextView tvGroupExtra = (TextView) convertView.findViewById(R.id.tvGroupExtra);
		
		if(this._data.get(groupPosition).getAppointments().isEmpty()){
			tvGroupExtra.setText("Brak wizyt na dziś");
		} else {
			if(isExpanded){
				tvGroupExtra.setText("Dotknij, aby ukryć");
			} else {
				tvGroupExtra.setText("Dotknij, aby pokazać dzisiejsze wizyty");
			}
		}
		
		tvGroupHeader.setText(groupTitle);
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
