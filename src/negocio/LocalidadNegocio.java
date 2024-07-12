package negocio;

import java.sql.SQLException;

import entities.Localidad;

public interface LocalidadNegocio extends GenericNegocio<Localidad> {

	public boolean correspondeProvincia(int provinciaId, int idLocalidad) throws SQLException;
}
