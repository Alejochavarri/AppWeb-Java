package entities;

public class PersistentObject {

	private int id;
	private boolean deleted = false;
	
	public PersistentObject() {
		// TODO Auto-generated constructor stub
	}
	
	public PersistentObject(int id) {
		super();
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
