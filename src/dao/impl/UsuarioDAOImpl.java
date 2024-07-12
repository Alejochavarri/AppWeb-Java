package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.UsuarioDAO;
import entities.Usuario;
import utils.ResultSetMapper;

public class UsuarioDAOImpl extends GenericDAOImpl<Usuario> implements UsuarioDAO {

	public UsuarioDAOImpl(String query) {
		super(query);
	}

	@Override
	public List<Usuario> list() throws SQLException {
		List<Usuario> usuarios = new ArrayList<>();
		try(Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
			while (rs.next()) {
				Usuario usuario = ResultSetMapper.usuarioFromResultSet(rs);
				usuarios.add(usuario);
			}
			return usuarios;
		}catch (SQLException e) {
			throw e;
		}
	}
	
	@Override
	public Usuario findUser() throws SQLException {
		try(Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)) {
			Usuario usuario = new Usuario();
			if(rs.next()) {
				usuario = ResultSetMapper.usuarioFromResultSet(rs);
				return usuario;
			} else {
				return null;
			}
		}catch (SQLException e) {
			throw e;
		}
	}	
	
	@Override
	public Usuario getByUsername() throws SQLException {
		try(Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)){
			Usuario usuario = new Usuario();
			while(rs.next()) {
				usuario = ResultSetMapper.usuarioFromResultSet(rs);
			}
			return usuario;
		}
	}

	@Override
	public Boolean updateUsuario(Usuario usuario) throws SQLException {
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)){
			ps.setObject(1, usuario.getTipoUsuario().getId());
			ps.setObject(2, usuario.getContrasena());
			return ps.executeUpdate() > 0;
		}catch(SQLException e) {
			throw e;
		}
	}
	
	
}
