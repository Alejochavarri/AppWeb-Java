package entities;

public class TipoCuenta extends PersistentObject {
	
	private String nombre;

	
	public TipoCuenta() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TipoCuenta(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}	

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}
