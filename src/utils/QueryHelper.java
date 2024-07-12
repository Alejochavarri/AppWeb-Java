package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import enums.Filtros;

public abstract class QueryHelper {

	/**
	 * Devuelve la query para insertar datos en una tabla
	 * 
	 * @param table
	 *            tabla a insertar
	 * @param cantidadCampos
	 *            cantidad de campos que tiene esa tabla
	 * @return INSERT INTO table VALUES (?,?,?,?) por ej
	 */
	public static String createInsertInto(String table, List<String> properties) {
		StringBuilder query = new StringBuilder("INSERT INTO " + table + " (");

		for (int i = 0; i < properties.size(); i++) {
			query.append(properties.get(i));
			if (i < properties.size() - 1) {
				query.append(", ");
			}
		}

		query.append(") VALUES (");

		for (int i = 0; i < properties.size(); i++) {
			query.append("?");
			if (i < properties.size() - 1) {
				query.append(", ");
			}
		}

		query.append(")");
		return query.toString();
	};

	/**
	 * Devuelve la query para hacer un update a la tabla
	 * 
	 * @param table
	 *            tabla a updatear
	 * @param properties
	 *            campos a updatear de forma key, value por ej. nombre, ? para
	 *            setear los parametros en el PreparedStatement
	 * @param idEntidad
	 *            entidad a hacer el WHERE
	 * @return UPDATE table SET propertyName = propertyValue, ... WHERE id =
	 *         idEntidad
	 */
	public static String createUpdateQuery(String table, HashMap<String, String> properties, int idEntidad) {
		String setClause = properties.entrySet().stream().map(entry -> {
			String key = entry.getKey();
			String value = entry.getValue();
			return key + " = " + value;
		}).collect(Collectors.joining(", "));
		return "UPDATE " + table + " SET " + setClause + "WHERE id " + String.valueOf(idEntidad);
	}

	/**
	 * Borrado logico de registro
	 * 
	 * @param table
	 *            tabla donde hacer el borrado
	 * @param id
	 *            de la entidad
	 * @return UPDATE table SET deleted = true;
	 */
	public static String createDeleteQuery(String table) {
		return "UPDATE " + table + " SET deleted = true WHERE id = ?";
	}

	/**
	 * Devuelve el list con paginado
	 * 
	 * @param table
	 *            tabla donde se quieren obtener datos
	 * @param conditions
	 *            para el where @see createUpdateQuery
	 * @param page
	 *            pagina
	 * @param pageSize
	 *            tama�o de pagina
	 * @return SELECT * FROM table LIMIT pageSize OFFSET offset
	 */
	public static String createSelectQueryWithPagination(String table, HashMap<String, Object> conditions, int page,
			int pageSize) {
		String whereClause = conditions.entrySet().stream().map(entry -> {
			String key = entry.getKey();
			Object value = entry.getValue();
			String formattedValue = (value instanceof String) ? "'" + value + "'" : value.toString();
			return key + " = " + formattedValue;
		}).collect(Collectors.joining(" AND "));

		int offset = (page - 1) * pageSize;

		String query = "SELECT * FROM " + table;
		if (!whereClause.isEmpty()) {
			query += " WHERE " + whereClause;
		}
		query += " LIMIT " + pageSize + " OFFSET " + offset;

		return query;
	}

	/**
	 * Devuelve un select con like
	 * 
	 * @param table
	 *            tabla a donde se quiere hacer el FROM
	 * @param filters
	 *            filtros
	 * @return SELECT * FROM table WHERE usuario LIKE %ad%
	 */
	public static String createSelectQuery(String table, HashMap<String, String> filters) {
		String whereClause = null;
		if(filters != null) {
			whereClause = filters.entrySet().stream().map(entry -> {
				String key = entry.getKey();
				Object value = entry.getValue();
				
				return key + " LIKE '%" + value + "%'";
				
			}).collect(Collectors.joining(" AND "));
		}

		String query = "SELECT * FROM " + table;
		if (whereClause != null && !whereClause.isEmpty()) {
			query += " WHERE " + whereClause;
		}

		return query.contains(" WHERE ") ? query + " AND deleted = false" : query + " WHERE deleted = false";
	}

	public static String findUser(String table, HashMap<String, String> filters) {

		String whereClause = filters.entrySet().stream().map(entry -> {
			String key = entry.getKey();
			Object value = entry.getValue();
			return key + " = '" + value + "'";
		}).collect(Collectors.joining(" AND "));

		String query = "SELECT * FROM " + table;
		if (!whereClause.isEmpty()) {
			query += " WHERE " + whereClause;
		}

		return query;
	}

	/**
	 * Devuelve un SELECT con JOINS
	 * @param mainTable tabla main a hacer el from
	 * @param mainTableColumns columnas de la tabla main
	 * @param joinTables tablas a joinear
	 * @param joinTableColumns columnas de las tablas a joinear (deben tener el mismo orden que joinTables)
	 * es decir, si en el primer lugar se encuentra A, primero deben ir todos los campos de A
	 * @param joinConditions condiciones para el join
	 * @return SELECT columns FROM mainTable JOIN joinTables ON joinConditions
	 */
	public static String createSelectQueryWithJoin(String mainTable, List<String> mainTableColumns,
			List<String> joinTables, List<List<String>> joinTableColumns, List<String> joinConditions) {
		
		if (joinTables.size() != joinTableColumns.size() || joinTables.size() != joinConditions.size()) {
			throw new IllegalArgumentException(
					"Las listas de tablas de uni�n, columnas y condiciones deben tener el mismo tama�o.");
		}

		StringBuilder query = new StringBuilder("SELECT ");

		StringJoiner selectColumns = new StringJoiner(", ");
		for (String column : mainTableColumns) {
			selectColumns.add(mainTable + "." + column);
		}

		for (int i = 0; i < joinTables.size(); i++) {
			String joinTable = joinTables.get(i);
			List<String> columns = joinTableColumns.get(i);
			for (String column : columns) {
				selectColumns.add(joinTable + "." + column);
			}
		}

		query.append(selectColumns.toString());

		query.append(" FROM ").append(mainTable);

		for (int i = 0; i < joinTables.size(); i++) {
			String joinTable = joinTables.get(i);
			String joinCondition = joinConditions.get(i);
			query.append(" JOIN ").append(joinTable).append(" ON ").append(joinCondition);
		}

		return query.toString();
	}
	
	public static String createSelectQueryWithBetweens(String mainTable, HashMap<String, List<String>> filters) {
        String whereClause = filters.entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey();
                    List<String> value = entry.getValue();

                    if (value.size() == 2) { 
                        return key + " BETWEEN " + value.get(0) + " AND " + value.get(1);
                    } else {
                        throw new IllegalArgumentException("Cada filtro debe contener exactamente 2 elementos: valor inicial y valor final");
                    }
                })
                .collect(Collectors.joining(" AND "));

        if (!whereClause.isEmpty()) {
            return "SELECT * FROM " + mainTable + " WHERE " + whereClause;
        }
        return "SELECT * FROM " + mainTable;
    }
	
	public static String genericFinder(String table, HashMap<String, String> filters) {

		String whereClause = filters.entrySet().stream().map(entry -> {
			String key = entry.getKey();
			Object value = entry.getValue();
			return key + " = '" + value + "'";
		}).collect(Collectors.joining(" AND "));

		String query = "SELECT * FROM " + table;
		if (!whereClause.isEmpty()) {
			query += " WHERE " + whereClause;
		}

		return query;
	}
	
    public static String findMovimientosCuentas(String table, HashMap<String, String> filters) {
        String whereClause = filters.entrySet().stream().map(entry -> {
            String key = entry.getKey();
            String value = entry.getValue();
            return key + " = '" + value + "'";
        }).collect(Collectors.joining(" AND "));

        String query = "SELECT movimiento.*, " +
	               "cuenta_origen.cbu as cbu_origen, cuenta_origen.saldo as saldo_origen, tcuenta.nombre as tipocuenta, cuenta_origen.fecha_creacion as fecha_creacion_origen, cuenta_origen.deleted as deleted_origen, " +
	               "cuenta_destino.cbu as cbu_destino, cuenta_destino.saldo as saldo_destino, cuenta_destino.fecha_creacion as fecha_creacion_destino, cuenta_destino.deleted as deleted_destino, tcuenta.id as id_cuenta_origen, tcuenta.deleted as deleted_cuenta_origen " +
                "FROM movimiento " +
                "INNER JOIN cuenta AS cuenta_origen ON movimiento.idcuenta_origen = cuenta_origen.id " +
                "INNER JOIN tipo_cuenta AS tcuenta ON cuenta_origen.id_tipo_cuenta = tcuenta.id " +
                "LEFT JOIN cuenta AS cuenta_destino ON movimiento.idcuenta_destino = cuenta_destino.id ";

        if (!whereClause.isEmpty()) {
            query += "WHERE " + whereClause;
        }

        return query;
    }

		public static String findCuotasPrestamos(String table, HashMap<String, String> filters) {
			String whereClause = filters.entrySet().stream().map(entry -> {
					String key = entry.getKey();
					String value = entry.getValue();
					return key + " = '" + value + "'";
			}).collect(Collectors.joining(" AND "));

			String query = "SELECT prestamo.*, cuota.id AS id_cuota, cuota.importe_cuota, cuota.cuota, cuota.estado AS cuota_estado, cuota.deleted AS cuota_deleted FROM prestamo " +
							"INNER JOIN cuota_prestamo AS cuota ON cuota.id_prestamo = prestamo.id ";

			if (!whereClause.isEmpty()) {
					query += "WHERE " + whereClause;
			}

			return query;
	}

}
