package entities;

public class Pais extends PersistentObject {
	private String nombre;

	public Pais(int id) {
		super(id);
	}


	public Pais() {
		// TODO Auto-generated constructor stub
	}


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
