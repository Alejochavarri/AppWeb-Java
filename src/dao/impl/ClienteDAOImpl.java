package dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.ClienteDAO;
import entities.Cliente;
import entities.Usuario;
import exceptions.ValidationException;
import utils.ResultSetMapper;

public class ClienteDAOImpl extends GenericDAOImpl<Cliente> implements ClienteDAO {

	
	public ClienteDAOImpl() {
		super();
	}

	public ClienteDAOImpl(String query) {
		super(query);
	}

	@Override
	public Cliente getClientByUser() throws SQLException {
		try(Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)){
			Cliente cliente = new Cliente();
			while(rs.next()) {
				cliente = ResultSetMapper.clientFromResultSet(rs);

			}
			
			return cliente;
		}
	}

	@Override
	public List<Cliente> list() throws SQLException {
		List<Cliente> clientes = new ArrayList<>();
		try(Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)){
			while(rs.next()) {
				Cliente cliente = ResultSetMapper.clientFromResultSet(rs);
				clientes.add(cliente);
			}
			
			return clientes;
		}catch(SQLException e) {
			throw e;
		}
	}

	@Override
	public Boolean updateClientAndUser(Cliente cliente) throws SQLException {
		try(Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setObject(1, cliente.getLocalidad().getId());
			ps.setObject(2, cliente.getCorreoElectronico());
			ps.setObject(3, cliente.getFechaNacimiento());
			ps.setObject(4, cliente.getDireccion());
			ps.setObject(5, cliente.getNombre());
			ps.setObject(6, cliente.isDeleted());
			ps.setObject(7, cliente.getApellido());
			ps.setObject(8, cliente.getCuil());
			ps.setObject(9, cliente.getSexo().toString());
			ps.setObject(10, cliente.getTelefono());
			ps.setObject(11, cliente.getNacionalidad().getId());
			ps.setObject(12, cliente.getProvincia().getId());
			ps.setObject(13, cliente.getDni());
			
			return ps.executeUpdate() > 0;
		}catch(SQLException e) {
			throw e;
		}
	}

	private boolean isDniDuplicate(int dni) throws SQLException {
        String query = "SELECT COUNT(*) FROM cliente WHERE dni = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return false;
    }

    private boolean isCuilDuplicate(long cuil) throws SQLException {
        String query = "SELECT COUNT(*) FROM cliente WHERE cuil = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, cuil);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return false;
    }

    private boolean isCorreoElectronicoDuplicate(String correoElectronico) throws SQLException {
        String query = "SELECT COUNT(*) FROM cliente WHERE correo_electronico = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, correoElectronico);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return false;
    }

    private boolean isTelefonoDuplicate(String telefono) throws SQLException {
        String query = "SELECT COUNT(*) FROM cliente WHERE telefono = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, telefono);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return false;
    }

    @Override
    public Boolean createClient(Cliente cliente, Usuario usuario) throws SQLException, ValidationException {
        if (isDniDuplicate(cliente.getDni())) {
            throw new ValidationException("DNI ya registrado.");
        }
        if (isCuilDuplicate(cliente.getCuil())) {
            throw new ValidationException("CUIL ya registrado.");
        }
        if (isCorreoElectronicoDuplicate(cliente.getCorreoElectronico())) {
            throw new ValidationException("Correo electrónico ya registrado.");
        }
        if (isTelefonoDuplicate(cliente.getTelefono())) {
            throw new ValidationException("Teléfono ya registrado.");
        }

        String query = "{CALL sp_crear_usuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = getConnection(); CallableStatement cs = conn.prepareCall(query)) {
            cs.setString(1, usuario.getUsuario());
            cs.setString(2, usuario.getContrasena());
            cs.setInt(3, usuario.getTipoUsuario().getId());
            cs.setInt(4, cliente.getDni());
            cs.setLong(5, cliente.getCuil());
            cs.setString(6, cliente.getNombre());
            cs.setString(7, cliente.getApellido());
            cs.setString(8, cliente.getSexo().toString());
            cs.setInt(9, cliente.getNacionalidad().getId());
            cs.setDate(10, java.sql.Date.valueOf(cliente.getFechaNacimiento().toString()));
            cs.setString(11, cliente.getDireccion());
            cs.setInt(12, cliente.getLocalidad().getId());
            cs.setInt(13, cliente.getProvincia().getId());
            cs.setString(14, cliente.getCorreoElectronico());
            cs.setString(15, cliente.getTelefono());
            cs.registerOutParameter(16, java.sql.Types.INTEGER);
            cs.execute();
            int status = cs.getInt(16);
            return status == 1;
        } catch (SQLException e) {
            throw e;
        }
    }
	@Override
	public Boolean updateClient(Cliente cliente, int id) throws SQLException {
	    String query = "{CALL sp_modificar_usuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

	    try (Connection conn = getConnection(); CallableStatement cs = conn.prepareCall(query)) {
	    	 cs.setInt(1, cliente.getUsuario().getId());
	         cs.setString(2, cliente.getUsuario().getUsuario());
	         cs.setString(3, cliente.getUsuario().getContrasena());
	         cs.setInt(4, cliente.getUsuario().getTipoUsuario().getId());
	         cs.setInt(5, id);
	         cs.setInt(6, cliente.getDni());
	         cs.setLong(7, cliente.getCuil());
	         cs.setString(8, cliente.getNombre());
	         cs.setString(9, cliente.getApellido());
	         cs.setString(10, cliente.getSexo().toString());
	         cs.setInt(11, cliente.getNacionalidad().getId());
	         cs.setDate(12, java.sql.Date.valueOf(cliente.getFechaNacimiento().toString()));
	         cs.setString(13, cliente.getDireccion());
	         cs.setInt(14, cliente.getLocalidad().getId());
	         cs.setInt(15, cliente.getProvincia().getId());
	         cs.setString(16, cliente.getCorreoElectronico());
	         cs.setString(17, cliente.getTelefono());
	         cs.registerOutParameter(18, java.sql.Types.INTEGER);

	         cs.execute();
	         int status = cs.getInt(18);
	         return status == 1;

	    } catch (SQLException e) {
	        throw e;
	    }
	}


	
	

}
