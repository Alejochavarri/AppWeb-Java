package negocio;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import entities.CuotaPrestamo;
import entities.Prestamo;
import enums.CuotaEstado;
import enums.PrestamoEstado;

public interface PrestamoNegocio extends GenericNegocio<Prestamo> {

	List<Prestamo> getPrestamoPorCliente(int id) throws SQLException;
	List<Prestamo> getSoloPrestamoPorCliente(int id) throws SQLException;
	Prestamo getPrestamoPorId(int id_prestamo) throws SQLException;
	List<Prestamo> getPrestamosFilter(LocalDate desde, LocalDate hasta, BigDecimal importeDesde,
			BigDecimal importeHasta, Integer plazoPagoDesde, Integer plazoPagoHasta, Integer cuentaId) throws SQLException;
	Boolean cambiarEstadoPrestamo(Integer idPrestamo, PrestamoEstado estado) throws SQLException;
	public List<CuotaPrestamo> getCuotaPrestamoByPrestamoId(int id_prestamo) throws SQLException;
	public Boolean updatePagoCuotaPrestamo(int idCuota, CuotaEstado estado) throws SQLException;
	public CuotaPrestamo findCuotaPrestamoById(int idCuota) throws SQLException;
}
