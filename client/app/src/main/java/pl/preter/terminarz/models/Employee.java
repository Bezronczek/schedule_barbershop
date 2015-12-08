package pl.preter.terminarz.models;

import java.util.ArrayList;

import pl.preter.terminarz.ApplicationActivity;

import com.google.common.collect.Multimap;

public class Employee {
    private String _name;
    private Integer _id;
    private ArrayList<String> _hours = new ArrayList<String>();
    private ArrayList<Service> _services = new ArrayList<Service>();
    private ArrayList<Appointment> _appointments = new ArrayList<Appointment>();

    public Employee(String name, int id) {
        this._name = name;
        this._id = id;
    }


    @Override
    public String toString() {
        return "Employee{" +
                "_name='" + _name + '\'' +
                ", _id=" + _id +
                ", _hours=" + _hours +
                ", _services=" + _services +
                ", _appointments=" + _appointments +
                '}';
    }

    public Integer getId() {
        return this._id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    // not sure if this fields will ever be used. keeping them for a while
    public void addHour(String hr) {
        this._hours.add(hr);
    }

    public ArrayList<String> getHoursList() {
        return this._hours;
    }

    public void addService(Service srv) {
        this._services.add(srv);
    }

    public ArrayList<Service> getAllServices() {
        return this._services;
    }

    public void setAllServices(ArrayList<Service> list) {
        this._services = list;
    }

    public ArrayList<Appointment> getAppointments() {
        return this._appointments;
    }

    public void setAppointments(Multimap<Integer, Appointment> appointments) {
        this._appointments = new ArrayList<Appointment>(appointments.get(this._id));
    }


    public Appointment getAppointment(int pos) {
        return this._appointments.get(pos);
    }

}
