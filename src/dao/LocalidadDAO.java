package dao;

import java.sql.SQLException;

import entities.Localidad;

public interface LocalidadDAO extends GenericDAO<Localidad> {

	public Boolean correspondeProvincia() throws SQLException;
}
