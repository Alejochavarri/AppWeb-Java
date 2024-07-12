package dao;

import java.sql.SQLException;

import entities.Provincia;

public interface ProvinciaDAO extends GenericDAO<Provincia> {

	public Boolean correspondePais() throws SQLException;
}
