package dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import dao.GenericDAO;
import entities.Movimiento;
import entities.PersistentObject;

public class GenericDAOImpl<PERSISTENTE extends PersistentObject> implements GenericDAO<PERSISTENTE> {
	
	private String host = "jdbc:mysql://localhost:3306/bdbancos?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
	private String user = "root";
	private String password = "root";
	protected String query;
	
	public GenericDAOImpl(String query) {
		super();
		this.query = query;
	}
	
	public GenericDAOImpl() {
		super();
	}

	protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(host, user, password);
    }
	
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found. Include it in your library path.", e);
        }
    }

	@Override
	public boolean create(PERSISTENTE persistente, Object[] properties) throws SQLException {
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)){
			for(int i = 0; i < properties.length; i++) {
				ps.setObject(i + 1, properties[i]);
			}
			return ps.executeUpdate() > 0;
		}catch(SQLException e) {
			throw e;
		}
	}

	@Override
	public boolean update(PERSISTENTE nuevoPersistente, int idPersistente, Object[] properties) throws SQLException {
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)){
			for(int i = 0; i < properties.length; i++) {
				ps.setObject(i + 1, properties[i]);
			}
			return ps.executeUpdate() > 0;
		}catch(SQLException e) {
			throw e;
		}
	}

	@Override
	public boolean delete(int idPersistente) throws SQLException {
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)){
			ps.setInt(1, idPersistente);
			return ps.executeUpdate() > 0;
		}catch(SQLException e) {
			throw e;
		}
	}

	@Override
	public List<PERSISTENTE> list() throws SQLException {
		return null;
	    
	}

}
