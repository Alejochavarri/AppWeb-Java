package utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import entities.Cliente;
import entities.Cuenta;
import entities.CuotaPrestamo;
import entities.Link;
import entities.Localidad;
import entities.Movimiento;
import entities.Pais;
import entities.Prestamo;
import entities.Provincia;
import entities.TipoCuenta;
import entities.TipoUsuario;
import entities.Usuario;
import enums.CuotaEstado;
import enums.PrestamoEstado;
import enums.Sexo;
import enums.TipoMovimiento;

public abstract class ResultSetMapper {
	public static Cliente clientFromResultSet(ResultSet rs) throws SQLException {
		try {
			Cliente cliente = new Cliente();
			cliente.setId(rs.getInt("cliente.id"));
			cliente.setDni(rs.getInt("cliente.dni"));
			cliente.setDeleted(rs.getBoolean("cliente.deleted"));
			cliente.setApellido(rs.getString("cliente.apellido"));
			cliente.setCorreoElectronico(rs.getString("cliente.correo_electronico"));
			cliente.setNombre(rs.getString("cliente.nombre"));
			cliente.setCuil(rs.getLong("cliente.cuil"));
			cliente.setDireccion(rs.getString("cliente.direccion"));
			cliente.setFechaNacimiento(rs.getDate("cliente.fecha_nacimiento"));
			cliente.setSexo(Sexo.valueOf(rs.getString("cliente.sexo")));
			cliente.setTelefono(rs.getString("cliente.telefono"));
			Localidad localidad = ResultSetMapper.localidadFromResultSet(rs);
			Provincia provincia = ResultSetMapper.provinciaFromResultSet(rs);
			Pais pais = ResultSetMapper.paisFromResultSet(rs);
			cliente.setLocalidad(localidad);
			cliente.setProvincia(provincia);
			cliente.setNacionalidad(pais);
			cliente.setDireccion(rs.getString("cliente.direccion"));
			Usuario usuario = ResultSetMapper.usuarioFromResultSet(rs);
			cliente.setUsuario(usuario);
			return cliente;
			
		}catch(SQLException e) {
			throw e;
		}
	}
	public static Prestamo prestamoFromResultSet(ResultSet rs) throws SQLException {
		try {
			Prestamo prestamo = new Prestamo();
			prestamo.setCuotas(rs.getInt("cuotas"));
			prestamo.setDeleted(rs.getBoolean("deleted"));
			prestamo.setFechaDeContratacion(rs.getDate("fecha_contratacion").toLocalDate());
			prestamo.setId(rs.getInt("id"));
			prestamo.setImportePedido(rs.getBigDecimal("importe_pedido"));
			prestamo.setImporteTotal(rs.getBigDecimal("importe_con_intereses"));
			prestamo.setMontoPorMes(rs.getBigDecimal("monto_por_mes"));
			prestamo.setPlazoEnMeses(rs.getInt("plazo_pago_mes"));
			prestamo.setEstado(PrestamoEstado.valueOf(rs.getString("estado")));
			return prestamo;
		}catch(SQLException e) {
			throw e;
		}
	}
	
	public static Cuenta cuentaOrigenFromResultSet(ResultSet rs) throws SQLException{
		try {
			Cuenta cuenta  = new Cuenta();
			if (rs.getObject("idcuenta_destino") != null) {
				cuenta.setId(rs.getInt("idcuenta_origen"));
//				cuenta.setTipoCuenta(TipoCuenta.valueOf(rs.getString("tipocuenta_origen")));
				cuenta.setCbu(rs.getLong("cbu_origen"));
				cuenta.setFechaCreacion(rs.getDate("fecha_creacion_origen"));
				cuenta.setDeleted(rs.getBoolean("deleted_origen"));
				cuenta.setSaldo(rs.getBigDecimal("saldo_origen"));
	        }
			return cuenta;
		}catch(SQLException e) {
			throw e;
		}
	}
	
	public static Cuenta cuentaFromResultSet(ResultSet rs) throws SQLException {
	    try {
	        Cuenta cuenta = new Cuenta();
	        cuenta.setId(rs.getInt("cuenta.id"));
	        TipoCuenta tipoCuenta = ResultSetMapper.tipoCuentaFromResultSet(rs);
	        cuenta.setTipoCuenta(tipoCuenta);
	        // cuenta.setCliente()(rs.getInt("cuenta.id_cliente"));
	        cuenta.setFechaCreacion(rs.getDate("cuenta.fecha_creacion"));
	        cuenta.setCbu(rs.getLong("cuenta.cbu"));
	        cuenta.setSaldo(rs.getBigDecimal("cuenta.saldo"));
	        cuenta.setDeleted(rs.getBoolean("cuenta.deleted"));	        
	        return cuenta;
	    } catch (SQLException e) {
	        throw e;
	    }
	}

	
	
	public static Pais paisFromResultSet(ResultSet rs) throws SQLException{
		try {
			Pais pais = new Pais();
			pais.setId(rs.getInt("pais.id"));
			pais.setNombre(rs.getString("pais.nombre"));
			pais.setDeleted(rs.getBoolean("pais.deleted"));
			return pais;
		}catch(SQLException e) {
			throw e;
		}
	}
	
	public static Provincia provinciaFromResultSet(ResultSet rs) throws SQLException{
		try {
			Provincia provincia = new Provincia();
			provincia.setId(rs.getInt("provincia.id"));
			provincia.setNombre(rs.getString("provincia.nombre"));
			provincia.setDeleted(rs.getBoolean("provincia.deleted"));
			return provincia;
		}catch(SQLException e) {
			throw e;
		}
	}
	
	public static Localidad localidadFromResultSet(ResultSet rs) throws SQLException{
		try {
			Localidad localidad = new Localidad();
			localidad.setId(rs.getInt("localidad.id"));
			localidad.setNombre(rs.getString("localidad.nombre"));
			localidad.setDeleted(rs.getBoolean("localidad.deleted"));
			return localidad;

		}catch(SQLException e) {
			throw e;
		}
	}
	
	public static Movimiento movimientoFromResultSet(ResultSet rs) throws SQLException{
		try {
			Movimiento movimiento  = new Movimiento();
			movimiento.setId(rs.getInt("id"));
				if (rs.getObject("idcuenta_origen") != null) {
		            Cuenta cuentaOrigen = ResultSetMapper.cuentaOrigenFromResultSet(rs);
		            movimiento.setCuentaOrigen(cuentaOrigen);
		        }
			if (rs.getObject("idcuenta_destino") != null) {
				Cuenta cuentaDestino = ResultSetMapper.cuentaDestinoFromResultSet(rs);		
				movimiento.setCuentaDestino(cuentaDestino);
		    }	
			movimiento.setFecha(rs.getDate("fecha"));
			movimiento.setDetalle(rs.getString("detalle"));
			movimiento.setImporte(rs.getBigDecimal("importe"));
			movimiento.setTipoMovimiento(TipoMovimiento.valueOf(rs.getString("tipo_movimiento")));
			return movimiento;
		}catch(SQLException e) {
			throw e;
		}
	}
	
	public static Cuenta cuentaDestinoFromResultSet(ResultSet rs) throws SQLException{
		try {
			Cuenta cuenta  = new Cuenta();
			if (rs.getObject("idcuenta_origen") != null) {
	            cuenta.setId(rs.getInt("idcuenta_origen"));
	            cuenta.setId(rs.getInt("idcuenta_destino"));
				//cuenta.setTipoCuenta(TipoCuenta.valueOf(rs.getString("tipocuenta_destino")));
				cuenta.setCbu(rs.getLong("cbu_destino"));
				cuenta.setFechaCreacion(rs.getDate("fecha_creacion_destino"));
				cuenta.setDeleted(rs.getBoolean("deleted_destino"));
				cuenta.setSaldo(rs.getBigDecimal("saldo_destino"));
	        }		
			return cuenta;
		}catch(SQLException e) {
			throw e;
		}
	}
	
	public static Movimiento movimientoCuentasFromResultSet(ResultSet rs) throws SQLException {
		try {
			Movimiento movimiento = new Movimiento();
		    movimiento.setId(rs.getInt("id"));
		    
		    TipoCuenta tipo_cuenta = new TipoCuenta();
		    tipo_cuenta = tipoCuentaMovimientosFromResultSet(rs);
		    if(rs.getObject("idcuenta_origen")!=null) {
		    	Cuenta cuentaOrigen = new Cuenta();
			    cuentaOrigen.setId(rs.getInt("idcuenta_origen"));
			    cuentaOrigen.setTipoCuenta(tipo_cuenta);
			    cuentaOrigen.setCbu(rs.getLong("cbu_origen"));
			    cuentaOrigen.setFechaCreacion(rs.getDate("fecha_creacion_origen"));
			    cuentaOrigen.setDeleted(rs.getBoolean("deleted_origen"));
			    cuentaOrigen.setSaldo(rs.getBigDecimal("saldo_origen"));
			    movimiento.setCuentaOrigen(cuentaOrigen);
		    }

		
		    Cuenta cuentaDestino = new Cuenta();
		    cuentaDestino.setId(rs.getInt("idcuenta_destino"));
//		    cuentaDestino.setTipoCuenta(TipoCuenta.valueOf(rs.getString("tipocuenta_destino")));
		    cuentaDestino.setCbu(rs.getLong("cbu_destino"));
		    cuentaDestino.setFechaCreacion(rs.getDate("fecha_creacion_destino"));
		    cuentaDestino.setDeleted(rs.getBoolean("deleted_destino"));
		    cuentaDestino.setSaldo(rs.getBigDecimal("saldo_destino"));
		    movimiento.setCuentaDestino(cuentaDestino);
		    
		    movimiento.setFecha(rs.getDate("fecha"));
		    movimiento.setDetalle(rs.getString("detalle"));
		    movimiento.setImporte(rs.getBigDecimal("importe"));
		    movimiento.setTipoMovimiento(TipoMovimiento.valueOf(rs.getString("tipo_movimiento")));
		    
		    return movimiento;
		} catch(SQLException e) {
			throw e;
		}
	    
	}
	
	public static TipoUsuario tipoUsuarioFromResultSet(ResultSet rs) throws SQLException{
		try {
			TipoUsuario tipoUsuario = new TipoUsuario();
			tipoUsuario.setId(rs.getInt("tipo_usuario.id"));
			tipoUsuario.setNombre(rs.getString("tipo_usuario.nombre"));
			tipoUsuario.setDeleted(rs.getBoolean("tipo_usuario.deleted"));
			return tipoUsuario;
		}catch(SQLException e) {
			throw e;
		}
	}
	
	public static TipoCuenta tipoCuentaFromResultSet(ResultSet rs) throws SQLException{
		try {
			TipoCuenta tipoCuenta = new entities.TipoCuenta();
			tipoCuenta.setId(rs.getInt("tipo_cuenta.id"));
			tipoCuenta.setNombre(rs.getString("tipo_cuenta.nombre"));
			tipoCuenta.setDeleted(rs.getBoolean("tipo_cuenta.deleted"));
			return tipoCuenta;
		}catch(SQLException e) {
			throw e;
		}
	}
	
	public static TipoCuenta tipoCuentaMovimientosFromResultSet(ResultSet rs) throws SQLException{
		try {
			TipoCuenta tipoCuenta = new entities.TipoCuenta();
			tipoCuenta.setId(rs.getInt("id_cuenta_origen"));
			tipoCuenta.setNombre(rs.getString("tipocuenta"));
			tipoCuenta.setDeleted(rs.getBoolean("deleted_cuenta_origen"));
			return tipoCuenta;
		}catch(SQLException e) {
			throw e;
		}
	}
	
	public static Usuario usuarioFromResultSet(ResultSet rs) throws SQLException{
		try {
			Usuario usuario = new Usuario();
			usuario.setId(rs.getInt("usuario.id"));
			usuario.setUsuario(rs.getString("usuario.usuario"));
			usuario.setContrasena(rs.getString("usuario.contrasena"));
			usuario.setTipoUsuario(ResultSetMapper.tipoUsuarioFromResultSet(rs));
			usuario.setDeleted(rs.getBoolean("usuario.deleted"));
			return usuario;
		}catch(SQLException e) {
			throw e;
		}
	}
	
	public static Link linkFromResultSet(ResultSet rs) throws SQLException{
		try {
			Link link = new Link();
			link.setId(rs.getInt("links.id"));
			link.setNombreRecurso(rs.getString("links.nombre_recurso"));
			link.setRedirige(rs.getString("links.redirige"));
			link.setDeleted(rs.getBoolean("links.deleted"));
			link.setTipoUsuario(ResultSetMapper.tipoUsuarioFromResultSet(rs));
			return link;
		}catch(SQLException e) {
			throw e;
		}
	}

	public static CuotaPrestamo cuotaPrestamoFromResultSet(ResultSet rs) throws SQLException{
		try {
			CuotaPrestamo cuota = new CuotaPrestamo();
			cuota.setId(rs.getInt("id_cuota"));
			Prestamo prestamo = prestamoFromResultSet(rs);
			cuota.setPrestamo(prestamo);
			cuota.setImporteCuota(rs.getBigDecimal("importe_cuota"));
			cuota.setCuota(rs.getInt("cuota"));
			cuota.setEstado(CuotaEstado.valueOf(rs.getString("cuota_estado")));
			cuota.setDeleted(rs.getBoolean("cuota_deleted"));
			
			return cuota;

		}catch(SQLException e) {
			throw e;
		}
	}

	public static CuotaPrestamo cuotaFromResultSet(ResultSet rs) throws SQLException{
		try {
			CuotaPrestamo cuota = new CuotaPrestamo();
			cuota.setId(rs.getInt("id"));;
			cuota.setImporteCuota(rs.getBigDecimal("importe_cuota"));
			cuota.setCuota(rs.getInt("cuota"));
			cuota.setEstado(CuotaEstado.valueOf(rs.getString("estado")));
			cuota.setDeleted(rs.getBoolean("deleted"));
			
			return cuota;

		}catch(SQLException e) {
			throw e;
		}
	}
}
