package entities;

import java.math.BigDecimal;
import java.util.Date;

public class Cuenta extends PersistentObject {
	private TipoCuenta tipoCuenta;
    private Date fechaCreacion;
    private Long cbu;
    private BigDecimal saldo;
    private Cliente cliente;
    
    public Cuenta(TipoCuenta tipoCuenta, int dni, Date fechaCreacion, Long cbu, BigDecimal saldo, Cliente cliente) {
		super();
		this.tipoCuenta = tipoCuenta;
		//this.dni = dni;
		this.fechaCreacion = fechaCreacion;
		this.cbu = cbu;
		this.saldo = saldo;
		this.cliente = cliente;
	}
    public Cuenta() {}
    
    public Cuenta(int cuentaId) {
    	super(cuentaId);
    }
	public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
    

    /*public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }*/

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getCbu() {
        return cbu;
    }

    public void setCbu(Long cbu) {
        this.cbu = cbu;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}
