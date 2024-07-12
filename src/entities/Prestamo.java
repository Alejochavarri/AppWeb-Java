package entities;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.List;

import enums.PrestamoEstado;

public class Prestamo extends PersistentObject {
    private Cliente cliente;
    private LocalDate fechaDeContratacion;
    private BigDecimal importePedido;
    private BigDecimal importeTotal;
    private int plazoEnMeses;
    private BigDecimal montoPorMes;
    private int cuotas;
    private BigDecimal interesAnual = new BigDecimal(2); // 2% de interés anual
    private Cuenta cuenta;
    private List<CuotaPrestamo> cuotaPrestamos;
    
    private PrestamoEstado estado;
    
    public Prestamo(Cliente cliente, LocalDate fechaDeContratacion, BigDecimal importePedido, int plazoEnMeses, Cuenta cuenta, PrestamoEstado estado) {
        this.cliente = cliente;
        this.fechaDeContratacion = fechaDeContratacion;
        this.importePedido = importePedido;
        this.plazoEnMeses = plazoEnMeses;
        this.cuotas = plazoEnMeses;
        this.importeTotal = calcularImporteTotal();
        this.montoPorMes = calcularMontoPorMes();
        this.estado = estado;
        this.cuenta = cuenta;
    }

    public Prestamo() {
    	
	}

    private BigDecimal calcularImporteTotal() {
        BigDecimal tasaInteresMensual = interesAnual.divide(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(12), MathContext.DECIMAL128);
        BigDecimal interesTotal = importePedido.multiply(tasaInteresMensual).multiply(BigDecimal.valueOf(plazoEnMeses));
        return importePedido.add(interesTotal);
    }

    private BigDecimal calcularMontoPorMes() {
        return importeTotal.divide(BigDecimal.valueOf(plazoEnMeses), MathContext.DECIMAL128);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFechaDeContratacion() {
        return fechaDeContratacion;
    }

    public void setFechaDeContratacion(LocalDate fechaDeContratacion) {
        this.fechaDeContratacion = fechaDeContratacion;
    }

    public BigDecimal getImportePedido() {
        return importePedido;
    }

    public void setImportePedido(BigDecimal importePedido) {
        this.importePedido = importePedido;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }
    
    public List<CuotaPrestamo> getCuotaPrestamos() {
        return cuotaPrestamos;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }

    public int getPlazoEnMeses() {
        return plazoEnMeses;
    }

    public void setPlazoEnMeses(int plazoEnMeses) {
        this.plazoEnMeses = plazoEnMeses;
    }

    public BigDecimal getMontoPorMes() {
        return montoPorMes;
    }

    public void setMontoPorMes(BigDecimal montoPorMes) {
        this.montoPorMes = montoPorMes;
    }

    public int getCuotas() {
        return cuotas;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    public BigDecimal getTasaInteresAnual() {
        return interesAnual;
    }

    public void setTasaInteresAnual(BigDecimal tasaInteresAnual) {
        this.interesAnual = tasaInteresAnual;
    }

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public PrestamoEstado getEstado() {
		return estado;
	}

	public void setEstado(PrestamoEstado estado) {
		this.estado = estado;
	}
	
	public void setCuotaPrestamos(List<CuotaPrestamo> cuotaPrestamos) {
        this.cuotaPrestamos = cuotaPrestamos;
    }
}
