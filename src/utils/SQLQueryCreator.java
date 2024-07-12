package utils;

import java.util.List;
import java.util.Map;

import utils.sql.SQLInsertBuilder;
import utils.sql.SQLSelectBuilder;

public abstract class SQLQueryCreator {

	public static String createClientSQLQuery(List<SearchCriteria> whereClauses) {
		SQLSelectBuilder selectBuilder = new SQLSelectBuilder();
		String query = selectBuilder.select("cliente.id", "cliente.dni", "cliente.cuil", "cliente.nombre", "cliente.apellido", "cliente.sexo", "cliente.id_pais",
				"cliente.fecha_nacimiento", "cliente.direccion", "cliente.id_localidad", "cliente.id_provincia", "cliente.correo_electronico", "cliente.telefono", "cliente.deleted", 
				"usuario.id", "usuario.deleted", "usuario.usuario", "usuario.contrasena", "tipo_usuario.id", "tipo_usuario.nombre", "tipo_usuario.deleted", "pais.id", "pais.nombre", "pais.deleted", "localidad.id", "localidad.nombre", "localidad.deleted", "provincia.id", "provincia.nombre", "provincia.deleted")
				.where(whereClauses)
				.innerJoin("usuario on usuario.id = cliente.id_usuario")
				.innerJoin("tipo_usuario on tipo_usuario.id = usuario.id_tipo_usuario")
				.innerJoin("localidad on localidad.id = cliente.id_localidad")
				.innerJoin("provincia on provincia.id = cliente.id_provincia")
				.innerJoin("pais on pais.id = cliente.id_pais")
				.from("cliente")
				.build();
		return query;
	}

}
