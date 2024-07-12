package entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import enums.CuotaEstado;

public class CuotaPrestamo extends PersistentObject {
	private Prestamo prestamo;
	private BigDecimal importeCuota;
	private int cuota;
	private CuotaEstado estado;
	private LocalDate fechaVencimiento;
	public Prestamo getPrestamo() {
		return prestamo;
	}
	public void setPrestamo(Prestamo prestamo) {
		this.prestamo = prestamo;
	}
	public BigDecimal getImporteCuota() {
		return importeCuota;
	}
	public void setImporteCuota(BigDecimal importeCuota) {
		this.importeCuota = importeCuota;
	}
	public int getCuota() {
		return cuota;
	}
	public void setCuota(int cuota) {
		this.cuota = cuota;
	}
	public CuotaEstado getEstado() {
		return estado;
	}
	public void setEstado(CuotaEstado estado) {
		this.estado = estado;
	}	
	
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
	
	
}
