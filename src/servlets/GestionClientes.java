package servlets;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.ClienteDAOImpl;
import dao.impl.LocalidadDAOImpl;
import dao.impl.PaisDAOImpl;
import dao.impl.ProvinciaDAOImpl;
import dao.impl.TipoUsuarioDAOImpl;
import entities.Cliente;
import entities.Localidad;
import entities.Pais;
import entities.Provincia;
import entities.TipoUsuario;
import entities.Usuario;
import enums.Sexo;
import exceptions.ValidationException;
import negocio.ClienteNegocio;
import negocio.LocalidadNegocio;
import negocio.PaisNegocio;
import negocio.ProvinciaNegocio;
import negocio.TipoUsuarioNegocio;
import negocio.impl.ClienteNegocioImpl;
import negocio.impl.LocalidadNegocioImpl;
import negocio.impl.PaisNegocioImpl;
import negocio.impl.ProvinciaNegocioImpl;
import negocio.impl.TipoUsuarioNegocioImpl;
import utils.SQLQueryCreator;
import utils.SearchCriteria;
import utils.sql.EqualsCriteria;
import utils.sql.LikeCriteria;
import utils.sql.QueryHelper;
import utils.sql.SQLSelectBuilder;

/**
 * Servlet implementation class GestionClientes
 */
@WebServlet("/GestionClientes")
public class GestionClientes extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GestionClientes() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        RequestDispatcher rd = request.getRequestDispatcher("GestionClientes.jsp");
        try {
            if (request.getParameter("load") != null) {
                loadClientes(request, response, rd);

            }

            if (request.getParameter("eliminar") != null) {
                Integer id = new Integer(request.getParameter("id"));
                String query = QueryHelper.createDeleteQuery("cliente");
                ClienteNegocio clienteNegocio = new ClienteNegocioImpl(new ClienteDAOImpl(query));
                Boolean result = clienteNegocio.delete(id);
                request.setAttribute("deleteResult", result);
                loadClientes(request, response, rd);
            }
            if (request.getParameter("buscarCriteria") != null) {
                String nombreCriteria = request.getParameter("nombreBuscar");
                String apellidoCriteria = request.getParameter("apellidoBuscar");
                String correoElectronicoBuscar = request.getParameter("correoElectronicoBuscar");
                Integer paisBuscarId = null;

                if (request.getParameter("paisBuscar") != null && !request.getParameter("paisBuscar").isEmpty()) {
                    paisBuscarId = Integer.parseInt(request.getParameter("paisBuscar"));
                }
                List<SearchCriteria> searchCriterias = new ArrayList<SearchCriteria>();
                if (nombreCriteria != null && !nombreCriteria.isEmpty()) {
                    LikeCriteria nombreLike = new LikeCriteria("cliente.nombre", nombreCriteria);
                    searchCriterias.add(nombreLike);
                }
                if (apellidoCriteria != null && !apellidoCriteria.isEmpty()) {
                    LikeCriteria apellidoLike = new LikeCriteria("cliente.apellido", apellidoCriteria);
                    searchCriterias.add(apellidoLike);
                }
                if (correoElectronicoBuscar != null && !correoElectronicoBuscar.isEmpty()) {
                    LikeCriteria correoElectronicoLike = new LikeCriteria("cliente.correo_electronico", correoElectronicoBuscar);
                    searchCriterias.add(correoElectronicoLike);
                }
                if (paisBuscarId != null) {
                    EqualsCriteria paisEqual = new EqualsCriteria("cliente.id_pais", paisBuscarId, Boolean.FALSE);
                    searchCriterias.add(paisEqual);
                }
                String query = SQLQueryCreator.createClientSQLQuery(searchCriterias);
                System.out.println(query);
                ClienteNegocio negocio = new ClienteNegocioImpl(new ClienteDAOImpl(query));
                List<Cliente> clientesFiltrados = negocio.list();
                request.setAttribute("clientes", clientesFiltrados);

            }

        } catch (SQLException e) {
            request.setAttribute("error", Boolean.TRUE);
        } catch (Exception e) {
            request.setAttribute("error", Boolean.TRUE);

        } finally {
            try {
                loadPaises(request);
                loadProvincias(request);
                loadLocalidades(request);
                loadTipoUsuarios(request);
            } catch (SQLException e) {
                request.setAttribute("error", Boolean.TRUE);
                e.printStackTrace();
            }

            rd.forward(request, response);
        }
    }

    private void loadTipoUsuarios(HttpServletRequest request) throws SQLException {
        SQLSelectBuilder selectBuilder = new SQLSelectBuilder();
        String query = selectBuilder.select("tipo_usuario.id", "tipo_usuario.nombre", "tipo_usuario.deleted")
                .from("tipo_usuario")
                .build();
        TipoUsuarioNegocio tipoUsuarioNegocio = new TipoUsuarioNegocioImpl(new TipoUsuarioDAOImpl(query));
        List<TipoUsuario> tipos = tipoUsuarioNegocio.list();
        request.setAttribute("tipoUsuarios", tipos);
    }

    private void loadLocalidades(HttpServletRequest request) throws SQLException {
        SQLSelectBuilder selectBuilderLocalidades = new SQLSelectBuilder();
        String localidadesQuery = selectBuilderLocalidades
                .select("pais.id", "pais.nombre", "pais.deleted",
                        "provincia.id", "provincia.nombre",
                        "provincia.deleted",
                        "localidad.id", "localidad.nombre", "localidad.deleted")
                .innerJoin("provincia ON provincia.id = localidad.id_provincia")
                .innerJoin("pais ON pais.id = provincia.id_pais")
                .from("localidad")
                .build();
        LocalidadNegocio localidadNegocio = new LocalidadNegocioImpl(new LocalidadDAOImpl(localidadesQuery));
        List<Localidad> localidades = localidadNegocio.list();
        request.setAttribute("localidades", localidades);
    }

    private void loadProvincias(HttpServletRequest request) throws SQLException {
        SQLSelectBuilder selectBuilderProvincias = new SQLSelectBuilder();
        String provinciaQuery = selectBuilderProvincias.select("provincia.id", "provincia.nombre", "provincia.deleted", "pais.id", "pais.nombre", "pais.deleted")
                .innerJoin("pais ON pais.id = provincia.id_pais")
                .from("provincia")
                .build();
        ProvinciaNegocio provinciaNegocio = new ProvinciaNegocioImpl(new ProvinciaDAOImpl(provinciaQuery));
        List<Provincia> provincias = provinciaNegocio.list();
        request.setAttribute("provincias", provincias);
    }

    private void loadPaises(HttpServletRequest request) throws SQLException {
        SQLSelectBuilder selectBuilderPaises = new SQLSelectBuilder();
        String query = selectBuilderPaises.select("pais.id", "pais.nombre", "pais.deleted")
                .from("pais")
                .build();
        PaisNegocio paisNegocio = new PaisNegocioImpl(new PaisDAOImpl(query));
        List<Pais> paises = paisNegocio.list();
        request.setAttribute("paises", paises);
    }

    private void loadClientes(HttpServletRequest request, HttpServletResponse response, RequestDispatcher rd)
            throws SQLException, ServletException, IOException {
        String query = SQLQueryCreator.createClientSQLQuery(Arrays.asList(new EqualsCriteria("cliente.deleted", Boolean.FALSE, Boolean.FALSE)));
        ClienteNegocio clienteNegocio = new ClienteNegocioImpl(new ClienteDAOImpl(query));
        List<Cliente> clientes = clienteNegocio.list();
        request.setAttribute("clientes", clientes);
    }


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rd = request.getRequestDispatcher("GestionClientes.jsp");
        try {
            if (request.getParameter("save") != null) {
                Integer dni = Integer.valueOf(request.getParameter("dni"));
                Long cuil = Long.valueOf(request.getParameter("cuil"));
                String nombre = request.getParameter("nombre");
                String apellido = request.getParameter("apellido");
                Sexo sexo = Sexo.valueOf(request.getParameter("sexo"));
                Date fechaNacimiento = Date.valueOf(request.getParameter("fechaNacimiento"));
                String direccion = request.getParameter("direccion");
                Pais nacionalidad = new Pais(Integer.parseInt(request.getParameter("nacionalidad")));
                Localidad localidad = new Localidad(Integer.parseInt(request.getParameter("localidad")));
                Provincia provincia = new Provincia(Integer.parseInt(request.getParameter("provincia")));
                String email = request.getParameter("correo");
                String telefono = request.getParameter("telefono");
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                entities.TipoUsuario tipoUsuario = new entities.TipoUsuario(Integer.parseInt(request.getParameter("tipousuario")));

                Cliente newClient = new Cliente(dni, cuil, nombre, apellido, sexo, nacionalidad, fechaNacimiento, direccion,
                        localidad, provincia, email, telefono);
                Usuario newUsuario = new Usuario(username, password, tipoUsuario);
                ClienteNegocio clienteNegocio = new ClienteNegocioImpl(new ClienteDAOImpl());

                // Si el usuario envio una localidad que no pertenece a la provincia
                // O una provincia no perteneciente a su pais, debe arrojar un error porque
                // lleva a una inconsistencia de datos
                if (!checkLocationConfig(newClient)) {
                    request.setAttribute("operacion", "NOT OK");
                    request.setAttribute("validationErrorMessage", "La localidad no pertenece a la provincia o la provincia no pertenece al país.");
                    rd.forward(request, response);
                    return;
                }

                // Intentar crear el cliente y capturar la excepción de validación
                try {
                    Boolean result = clienteNegocio.createClient(newUsuario, newClient);
                    if (result) {
                        request.setAttribute("operacion", "OK");
                    } else {
                        request.setAttribute("operacion", "NOT OK");
                    }
                } catch (ValidationException ve) {
                    request.setAttribute("operacion", "NOT OK");
                    request.setAttribute("validationErrorMessage", ve.getMessage());
                }
            }
            if (request.getParameter("modificar") != null) {
                Cliente cliente = getClientFromRequest(request);
                ClienteNegocio negocio = new ClienteNegocioImpl(new ClienteDAOImpl(null));
                int id = Integer.parseInt(request.getParameter("id"));
                Boolean result = negocio.updateClient(cliente, id);
                if (result) {
                    request.setAttribute("modificarResult", Boolean.TRUE);
                } else {
                    request.setAttribute("modificarResult", Boolean.FALSE);
                }
            }

        } catch (SQLException e) {
            request.setAttribute("operacion", "NOT OK");
            e.printStackTrace();
        } catch (Exception e) {
            request.setAttribute("operacion", "NOT OK");
            e.printStackTrace();
        } finally {
            try {
                loadClientes(request, response, rd);
                loadPaises(request);
                loadProvincias(request);
                loadLocalidades(request);
                loadTipoUsuarios(request);
            } catch (SQLException e) {
                request.setAttribute("operacion", "NOT OK");
                e.printStackTrace();
            }
            rd.forward(request, response);
        }
    }
    private Cliente getClientFromRequest(HttpServletRequest request) {
        Integer dni = Integer.valueOf(request.getParameter("dni"));
        Long cuil = Long.valueOf(request.getParameter("cuil"));
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        Sexo sexo = Sexo.valueOf(request.getParameter("sexo"));
        Pais nacionalidad = new Pais(Integer.parseInt(request.getParameter("nacionalidad")));
        Date fechaNacimiento = Date.valueOf(request.getParameter("fechaNacimiento"));
        String direccion = request.getParameter("direccion");
        Localidad localidad = new Localidad(Integer.parseInt(request.getParameter("localidad")));
        Provincia provincia = new Provincia(Integer.parseInt(request.getParameter("provincia")));
        String email = request.getParameter("correo");
        String telefono = request.getParameter("telefono");
        String password = request.getParameter("password");
        String usuario = request.getParameter("usuarioModificar");
        TipoUsuario tipoUsuario = new TipoUsuario(Integer.parseInt(request.getParameter("tipousuarioModificar")));
        Integer usuarioId = Integer.parseInt(request.getParameter("usuarioIdModificar"));
        Usuario user = new Usuario(usuarioId, usuario, password, tipoUsuario);
        return new Cliente(dni, cuil, nombre, apellido, sexo, nacionalidad, fechaNacimiento, direccion,
                localidad, provincia, email, telefono, user);
    }

    private Boolean checkLocationConfig(Cliente cliente) throws SQLException {
        try {
            return
                    cliente.getLocalidad().isAssociatedProvince(cliente.getProvincia(), cliente.getLocalidad()) &&
                            cliente.getProvincia().isAssociatedCountry(cliente.getProvincia(), cliente.getNacionalidad());
        } catch (SQLException e) {
            throw e;
        }

    }
}
