package entities;

import java.util.Date;

import enums.Sexo;

public class Cliente extends PersistentObject{
	
	private Usuario usuario;
	private int dni;
	private Long cuil;
	private String nombre;
    private String apellido;
    private Sexo sexo;
    private Pais nacionalidad;
    private Date fechaNacimiento;
    private String direccion;
    private Localidad localidad;
    private Provincia provincia;
    private String correoElectronico;
    private String telefono;
    
	public Cliente(int dni, Long cuil, String nombre, String apellido, Sexo sexo, Pais nacionalidad,
			Date fechaNacimiento, String direccion, Localidad localidad, Provincia provincia, String correoElectronico,
			String telefono) {
		super();
		this.dni = dni;
		this.cuil = cuil;
		this.nombre = nombre;
		this.apellido = apellido;
		this.sexo = sexo;
		this.nacionalidad = nacionalidad;
		this.fechaNacimiento = fechaNacimiento;
		this.direccion = direccion;
		this.localidad = localidad;
		this.provincia = provincia;
		this.correoElectronico = correoElectronico;
		this.telefono = telefono;
	}

	public Cliente() {
		// TODO Auto-generated constructor stub
	}

	public Cliente(int dni, Long cuil, String nombre, String apellido, Sexo sexo, Pais nacionalidad,
			Date fechaNacimiento, String direccion, Localidad localidad, Provincia provincia, String correoElectronico,
			String telefono, Usuario usuario) {
		super();
		this.dni = dni;
		this.cuil = cuil;
		this.nombre = nombre;
		this.apellido = apellido;
		this.sexo = sexo;
		this.nacionalidad = nacionalidad;
		this.fechaNacimiento = fechaNacimiento;
		this.direccion = direccion;
		this.localidad = localidad;
		this.provincia = provincia;
		this.correoElectronico = correoElectronico;
		this.telefono = telefono;
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public int getDni() {
		return dni;
	}
	public void setDni(int dni) {
		this.dni = dni;
	}
	public Long getCuil() {
		return cuil;
	}
	public void setCuil(Long cuil) {
		this.cuil = cuil;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public Sexo getSexo() {
		return sexo;
	}
	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}
	public Pais getNacionalidad() {
		return nacionalidad;
	}
	public void setNacionalidad(Pais nacionalidad) {
		this.nacionalidad = nacionalidad;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public Localidad getLocalidad() {
		return localidad;
	}
	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}
	public Provincia getProvincia() {
		return provincia;
	}
	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

}
