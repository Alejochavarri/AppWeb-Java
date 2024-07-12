package entities;

import java.sql.SQLException;

import dao.impl.LocalidadDAOImpl;
import negocio.LocalidadNegocio;
import negocio.impl.LocalidadNegocioImpl;
import utils.sql.EqualsCriteria;
import utils.sql.SQLSelectBuilder;

public class Localidad extends PersistentObject {

	private String nombre;
	private Provincia provincia;
	
	public Localidad(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	public Localidad() {
		// TODO Auto-generated constructor stub
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Provincia getProvincia() {
		return provincia;
	}
	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}
	
	public Boolean isAssociatedProvince(Provincia provincia, Localidad localidad) throws SQLException {
		try {
		
			LocalidadNegocio negocio = new LocalidadNegocioImpl(new LocalidadDAOImpl());
			Boolean result = negocio.correspondeProvincia(provincia.getId(), localidad.getId());
			return result;
		}catch(SQLException e) {
			throw e;
		}

	}
	
}
