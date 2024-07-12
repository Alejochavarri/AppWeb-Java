package dao;

import java.sql.SQLException;
import java.util.List;

import entities.Prestamo;
import enums.PrestamoEstado;

public interface PrestamoDAO extends GenericDAO<Prestamo> {

	List<Prestamo> getPrestamosPorCliente() throws SQLException;
	List<Prestamo> getSoloPrestamosPorCliente() throws SQLException;
	Prestamo getPrestamoPorId(int id_prestamo) throws SQLException;
	Boolean cambiarEstado(PrestamoEstado nuevoEstado) throws SQLException;
	public List<Prestamo> list() throws SQLException;
	
}
