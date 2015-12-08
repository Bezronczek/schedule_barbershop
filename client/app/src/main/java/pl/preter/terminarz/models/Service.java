package pl.preter.terminarz.models;

public class Service {
	private Integer _id;
	private String _name;
	
	public Service(){}

    @Override
    public String toString() {
        return "Service{" +
                "_id=" + _id +
                ", _name='" + _name + '\'' +
                '}';
    }

    public Service(int id, String name){
		this._id = id;
		this._name = name;
	}
	
	public void setId(int id){
		this._id = id;
	}
	
	public Integer getID(){
		return this._id;
	}
	
	public void setName(String name){
		this._name = name;
	}
	
	public String getName(){
		return this._name;
	}
}
