package entities;


public class Usuario extends PersistentObject {
    private String usuario;
    private String contrasena;
    private TipoUsuario tipoUsuario;

    public Usuario() {
	}
    public Usuario(String usuario, String contrasena, TipoUsuario tipoUsuario) {
		super();
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.tipoUsuario = tipoUsuario;
	}

    public Usuario(Integer usuarioId, String usuario, String contrasena, TipoUsuario tipoUsuario) {
    	super(usuarioId);
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.tipoUsuario = tipoUsuario;	
	}
	public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

}
