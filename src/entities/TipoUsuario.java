package entities;

public class TipoUsuario extends PersistentObject {


	private String nombre;
	
	public TipoUsuario() {
		// TODO Auto-generated constructor stub
	}
	
	public TipoUsuario(int id) {
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
