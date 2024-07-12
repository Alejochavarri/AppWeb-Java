package entities;


public class Link extends PersistentObject {

	
	private String nombreRecurso;
	private TipoUsuario tipoUsuario;
	private String redirige;
	public Link(String nombreRecurso, TipoUsuario tipoUsuario) {
		super();
		this.nombreRecurso = nombreRecurso;
		this.tipoUsuario = tipoUsuario;
	}
	public Link() {
		// TODO Auto-generated constructor stub
	}
	public String getNombreRecurso() {
		return nombreRecurso;
	}
	public void setNombreRecurso(String nombreRecurso) {
		this.nombreRecurso = nombreRecurso;
	}
	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}
	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	public String getRedirige() {
		return redirige;
	}
	public void setRedirige(String redirige) {
		this.redirige = redirige;
	}
	public Boolean canAccessToResource(Usuario usuario) {
		TipoUsuario tipoUsuarioFromUsuario = usuario.getTipoUsuario();
		if(this.tipoUsuario.getId() == tipoUsuarioFromUsuario.getId()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
