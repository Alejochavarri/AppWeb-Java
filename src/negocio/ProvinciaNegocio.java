package negocio;

import java.sql.SQLException;

import entities.Provincia;

public interface ProvinciaNegocio extends GenericNegocio<Provincia> {

	public Boolean correspondePais(int idProvincia, int idLocalidad) throws SQLException;
}
