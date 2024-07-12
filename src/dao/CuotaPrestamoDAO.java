package dao;

import java.sql.SQLException;
import java.util.List;

import entities.CuotaPrestamo;
import enums.CuotaEstado;

public interface CuotaPrestamoDAO extends GenericDAO<CuotaPrestamo>{

	public Boolean cambiarEstado(CuotaEstado nuevoEstado) throws SQLException;
	public List<CuotaPrestamo> list() throws SQLException;
	public CuotaPrestamo findCuotaPrestamo() throws SQLException;
	
}
