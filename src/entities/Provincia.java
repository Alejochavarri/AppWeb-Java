package entities;

import java.sql.SQLException;

import dao.impl.ProvinciaDAOImpl;
import negocio.ProvinciaNegocio;
import negocio.impl.ProvinciaNegocioImpl;

public class Provincia extends PersistentObject {
	private String nombre;
	private Pais pais;
	
	public Provincia(int id) {
		super(id);
	}
	public Provincia() {
		// TODO Auto-generated constructor stub
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String name) {
		this.nombre = name;
	}
	public Pais getPais() {
		return pais;
	}
	public void setPais(Pais pais) {
		this.pais = pais;
	}
	public Boolean isAssociatedCountry(Provincia provincia, Pais pais) throws SQLException {
		try {
			ProvinciaNegocio negocio = new ProvinciaNegocioImpl(new ProvinciaDAOImpl());
			Boolean result = negocio.correspondePais(provincia.getId(), pais.getId());
			return result;
		}catch(SQLException e) {
			throw e;
		}
	}
}
