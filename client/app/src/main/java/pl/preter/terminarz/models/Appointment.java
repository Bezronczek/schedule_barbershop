package pl.preter.terminarz.models;

public class Appointment {

	private Integer _id, _client_id;
	private String _hour, _client_name, _client_surname, _phone, _email, _create_date, _date;
	private Service _service;
	private boolean _isConfirmed;
	
	
	@Override
	public String toString() {
		return "Appointment [_id=" + _id + ", _client_id=" + _client_id
				+ ", _hour=" + _hour + ", _client_name=" + _client_name
				+ ", _client_surname=" + _client_surname + ", _phone=" + _phone
				+ ", _email=" + _email + ", _create_date=" + _create_date
				+ ", _service=" + _service + "]";
	}
	
	public Appointment(int id, String hour, String date, String client_name, String client_surname, 
			String phone, String email, Service service, String created, boolean confirmed){
		this._id = id;
		this._client_name = client_name;
		this._client_surname = client_surname;
		this._hour = hour;
		this._phone = phone;
		this._email = email;
		this._service = service;
		this._create_date = created;
		this._isConfirmed = confirmed;
		this._date = date;
	}
	
	public Appointment(int id, String hour, String date, String client_name, String client_surname, 
			String phone, Service service, String created, boolean confirmed){
		this._id = id;
		this._client_name = client_name;
		this._client_surname = client_surname;
		this._hour = hour;
		this._phone = phone;
		this._service = service;
		this._create_date = created;
		this._isConfirmed = confirmed;
		this._date = date;
	}
	
	public Appointment(int id, String hour, String date, String client_name, String client_surname, 
			int client_id, String phone, String email, Service service, String created, boolean confirmed){
		this._id = id;
		this._client_name = client_name;
		this._client_surname = client_surname;
		this._hour = hour;
		this._phone = phone;
		this._email = email;
		this._service = service;
		this._client_id = client_id;
		this._create_date = created;
		this._isConfirmed = confirmed;
		this._date = date;
	}
	
	
	public boolean isEmailSet(){
        return this._email != null;
	}
	
	public Integer getId(){
		return this._id;
	}
	
	public String getHour(){
		return this._hour;
	}
	
	public String getClientName(){
		return this._client_name;
	}
	
	public String getClientSurname() {
		return this._client_surname;
	}
	
	public String getClientFullName(){
		return this._client_name + " " + this._client_surname;
	}
	
	public String getPhone(){
		return this._phone;
	}
	
	public String getEmail(){
		return this._email;
	}
	
	public Service getService(){
		return this._service;
	}

	public String getCreateDate() {
		return this._create_date;
	}
	
	public Integer getClientId(){
		return this._client_id;
	}

	public void setCreateDate(String _create_date) {
		this._create_date = _create_date;
	}
	
	public boolean isConfirmed(){
		return this._isConfirmed;
	}
}
