package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.PrestamoDAO;
import entities.Cliente;
import entities.Cuenta;
import entities.Prestamo;
import enums.PrestamoEstado;
import utils.ResultSetMapper;

public class PrestamoDAOImpl extends GenericDAOImpl<Prestamo> implements PrestamoDAO {

	public PrestamoDAOImpl(String query) {
		super(query);
	}

	public List<Prestamo> getPrestamosPorCliente() throws SQLException {
		List<Prestamo> prestamos = new ArrayList<>();
		try (Connection conn = getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {
			while (rs.next()) {
				Prestamo prestamo = mapPrestamo(rs);
				prestamos.add(prestamo);
			}
			return prestamos;
		} catch (SQLException e) {
			throw e;
		}

	}
	
	public List<Prestamo> getSoloPrestamosPorCliente() throws SQLException {
		List<Prestamo> prestamos = new ArrayList<>();
		try (Connection conn = getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {
			while (rs.next()) {
				Prestamo prestamo = ResultSetMapper.prestamoFromResultSet(rs);
				prestamos.add(prestamo);
			}
			return prestamos;
		} catch (SQLException e) {
			throw e;
		}

	}


	@Override
	public List<Prestamo> list() throws SQLException {
		List<Prestamo> prestamos = new ArrayList<>();
		try (Connection conn = getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {
			while (rs.next()) {
				Prestamo prestamo = mapPrestamo(rs);
				prestamos.add(prestamo);
			}
			return prestamos;
		}catch(SQLException e) {
			throw e;
		}
	}
	

	@Override
	public Boolean cambiarEstado(PrestamoEstado nuevoEstado) throws SQLException {
		try(Connection conn = getConnection();
				PreparedStatement st = conn.prepareStatement(query)){
			st.setString(1, nuevoEstado.toString());
			return st.executeUpdate() > 0;
		}catch(SQLException e) {
			throw e;
		}
	}
	
	private Prestamo mapPrestamo(ResultSet rs) throws SQLException {
		Prestamo prestamo = ResultSetMapper.prestamoFromResultSet(rs);
		return prestamo;
	}

	@Override
	public Prestamo getPrestamoPorId(int id_prestamo) throws SQLException {
		try (Connection conn = getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {
			while (rs.next()) {
				Prestamo prestamo = ResultSetMapper.prestamoFromResultSet(rs);
				return prestamo;
			}
		} catch (SQLException e) {
			throw e;
		}
		return null;
	}


}
