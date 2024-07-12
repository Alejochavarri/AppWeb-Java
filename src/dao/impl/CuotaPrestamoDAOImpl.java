package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.CuotaPrestamoDAO;
import entities.CuotaPrestamo;
import enums.CuotaEstado;
import enums.PrestamoEstado;
import utils.ResultSetMapper;

public class CuotaPrestamoDAOImpl extends GenericDAOImpl<CuotaPrestamo> implements CuotaPrestamoDAO {
	public CuotaPrestamoDAOImpl(String query) {
		super(query);
	}
	

	@Override
	public List<CuotaPrestamo> list() throws SQLException {
		List<CuotaPrestamo> cuotas = new ArrayList<CuotaPrestamo>();
		try(Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)){
			while(rs.next()) {
				CuotaPrestamo cuota = ResultSetMapper.cuotaPrestamoFromResultSet(rs);
				cuotas.add(cuota);
			}
			
			return cuotas;
		}
	}
	
	@Override
	public Boolean cambiarEstado(CuotaEstado nuevoEstado) throws SQLException {
		try(Connection conn = getConnection();
				PreparedStatement st = conn.prepareStatement(query)){
			st.setString(1, nuevoEstado.toString());
			return st.executeUpdate() > 0;
		}catch(SQLException e) {
			throw e;
		}
	}

	@Override
	public CuotaPrestamo findCuotaPrestamo() throws SQLException {
		try (Connection conn = getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {
			while (rs.next()) {
				CuotaPrestamo cuota = ResultSetMapper.cuotaFromResultSet(rs);
				return cuota;
			}
		} catch (SQLException e) {
			throw e;
		}
		return null;
	}
}
