package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.MovimientoDAO;
import entities.Movimiento;
import utils.ResultSetMapper;

public class MovimientoDAOImpl extends GenericDAOImpl<Movimiento> implements MovimientoDAO {
	public MovimientoDAOImpl(String createSelectQuery) {
		super(createSelectQuery);
	}

	@Override
	public List<Movimiento> list() throws SQLException {
		List<Movimiento> movimientos = new ArrayList<Movimiento>();
		try(Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(query)){
			while(rs.next()) {
				Movimiento movimiento = ResultSetMapper.movimientoFromResultSet(rs);
				movimientos.add(movimiento);
			}
			
			return movimientos;
		}
	}

	@Override
	public List<Movimiento> getMovimientoConCuentas() throws SQLException {
		List<Movimiento> movimientos = new ArrayList<>();
	    try (Connection connection = getConnection();
	            Statement statement = connection.createStatement();
	            ResultSet resultSet = statement.executeQuery(query)) {
	           
	           while (resultSet.next()) {
	               Movimiento movimiento = ResultSetMapper.movimientoCuentasFromResultSet(resultSet);
	               movimientos.add(movimiento);
	           }
	       }
	       
	       return movimientos;
	}
	
	@Override
	public boolean guardarMovimiento(Movimiento movimiento, String query) throws SQLException {
		try (Connection conn = getConnection(); PreparedStatement st = conn.prepareStatement(query)) {
            st.setObject(1, movimiento.getFecha());
            st.setObject(2, movimiento.getCuentaOrigen().getId());
            st.setObject(3, movimiento.getImporte());
            st.setObject(4, movimiento.getTipoMovimiento().toString());
            st.setObject(5, movimiento.getCuentaDestino());
            st.setObject(6, movimiento.getDetalle());
            return st.executeUpdate() > 0;
        }catch(SQLException e) {
			throw e;
		}
	}
	
	public List<Movimiento> getMovimientosConFiltros(String query, List<Object> parameters) throws SQLException {
        List<Movimiento> movimientos = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            for (int i = 0; i < parameters.size(); i++) {
            	st.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet resultSet = st.executeQuery()) {
                while (resultSet.next()) {
                    Movimiento movimiento = ResultSetMapper.movimientoCuentasFromResultSet(resultSet);
                    movimientos.add(movimiento);
                }
            }
        }
        return movimientos;
    }	
}
