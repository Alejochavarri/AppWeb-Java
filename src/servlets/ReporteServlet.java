package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.impl.CuentaDAOImpl;
import dao.impl.PrestamoDAOImpl;
import entities.Cuenta;
import entities.Movimiento;
import entities.Prestamo;
import enums.PrestamoEstado;
import enums.TipoMovimiento;
import negocio.CuentaNegocio;
import negocio.PrestamoNegocio;
import negocio.impl.CuentaNegocioImpl;
import negocio.impl.MovimientoNegocioImpl;
import negocio.impl.PrestamoNegocioImpl;
import utils.sql.BetweenCriteria;
import utils.sql.QueryHelper;
import utils.sql.SQLSelectBuilder;

import java.util.Date;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.TreeMap;

/**
 * Servlet implementation class ReporteServlet
 */
@WebServlet("/ReporteServlet")
public class ReporteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReporteServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String reportType = request.getParameter("type");
		if (startDate == null || endDate == null || startDate.isEmpty() || endDate.isEmpty()) {
			request.setAttribute("error", "Las fechas de inicio y fin son obligatorias.");
			RequestDispatcher rd = request.getRequestDispatcher("/Reportes.jsp");
			rd.forward(request, response);
			return;
		}
		List<YearMonth> listaMeses = listarMeses(startDate, endDate);
		for (YearMonth yearMonth : listaMeses) {
			System.out.println(yearMonth);
		}
		List<Integer> listaAnios = listarAnios(startDate, endDate);
		for (Integer anios : listaAnios) {
			System.out.println(anios);
		}
		List<String> fechas = new ArrayList<>();
		fechas.add("'" + startDate + "'");
		fechas.add("'" + endDate + "'");

		getCuentas(request, reportType, startDate, endDate);
		getPrestamos(request, fechas, reportType, startDate, endDate);
		getHistorial(request, fechas, reportType, startDate, endDate);

		RequestDispatcher rd = request.getRequestDispatcher("/Reportes.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void getCuentas(HttpServletRequest request, String reportType, String startDate, String endDate) {
		SQLSelectBuilder selectbuilder = new SQLSelectBuilder();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			String query = selectbuilder
					.select("cuenta.id", "cuenta.id_tipo_cuenta", "cuenta.id_cliente", "cuenta.fecha_creacion",
							"cuenta.cbu", "cuenta.saldo", "cuenta.deleted", "tipo_cuenta.id", "tipo_cuenta.nombre",
							"tipo_cuenta.deleted", "cliente.id")
					.innerJoin("tipo_cuenta ON cuenta.id_tipo_cuenta = tipo_cuenta.id")
					.innerJoin("cliente ON cuenta.id_cliente = cliente.id")
					.where(new BetweenCriteria("cuenta.fecha_creacion", startDate, endDate, true)).from("cuenta")
					.build();
			System.out.println(query);
			CuentaNegocio cuentaNegocio = new CuentaNegocioImpl(new CuentaDAOImpl(query));

			List<Cuenta> cuentas = cuentaNegocio.list();

			if (cuentas != null) {
				Map<?, List<Long>> cuentasPorFecha = null;
				switch (reportType) {
				case "Daily":
					cuentasPorFecha = cuentas.stream().collect(Collectors.groupingBy(Cuenta::getFechaCreacion,
							LinkedHashMap::new, Collectors.collectingAndThen(Collectors.toList(), list -> {
								long totalCuentas = list.size();
								long cajaAhorroCuentas = list.stream()
										.filter(cuenta -> "CAJA_DE_AHORRO".equals(cuenta.getTipoCuenta().getNombre()))
										.count();
								long cuentaCorrienteCuentas = list.stream()
										.filter(cuenta -> "CUENTA CORRIENTE".equals(cuenta.getTipoCuenta().getNombre()))
										.count();
								return Arrays.asList(totalCuentas, cajaAhorroCuentas, cuentaCorrienteCuentas);
							})));
					break;

				case "Monthly":
					List<YearMonth> listaMeses = listarMeses(startDate, endDate);
					cuentasPorFecha = new TreeMap<>();
					for (YearMonth yearMonth : listaMeses) {
						long totalCuentas = 0;
						long cajaAhorroCuentas = 0;
						long cuentaCorrienteCuentas = 0;
						for (Cuenta cuenta : cuentas) {
							Date fechaCreacionDate = cuenta.getFechaCreacion();
							LocalDate yearMonthDate = yearMonth.atDay(1);
							if (mismoMesyAnio(fechaCreacionDate,
									Date.from(yearMonthDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
								totalCuentas++;
								if ("CAJA_DE_AHORRO".equals(cuenta.getTipoCuenta().getNombre())) {
									cajaAhorroCuentas++;
								} else {
									cuentaCorrienteCuentas++;
								}
							}
						}
						((TreeMap<YearMonth, List<Long>>) cuentasPorFecha).put(yearMonth,
								Arrays.asList(totalCuentas, cajaAhorroCuentas, cuentaCorrienteCuentas));
					}
					break;

				case "Yearly":
					List<Integer> listaAnios = listarAnios(startDate, endDate);
					cuentasPorFecha = new TreeMap<>();
					for (Integer anio : listaAnios) {
						long totalCuentas = 0;
						long cajaAhorroCuentas = 0;
						long cuentaCorrienteCuentas = 0;
						for (Cuenta cuenta : cuentas) {
							Date fechaCreacionDate = cuenta.getFechaCreacion();
							if (mismoAnio(fechaCreacionDate, Date
									.from(LocalDate.of(anio, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
								totalCuentas++;
								if ("CAJA_DE_AHORRO".equals(cuenta.getTipoCuenta().getNombre())) {
									cajaAhorroCuentas++;
								} else {
									cuentaCorrienteCuentas++;
								}
							}
						}
						((TreeMap<Integer, List<Long>>) cuentasPorFecha).put(anio,
								Arrays.asList(totalCuentas, cajaAhorroCuentas, cuentaCorrienteCuentas));
					}
					break;
				}
				request.setAttribute("cuentasPorFecha", cuentasPorFecha);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void getPrestamos(HttpServletRequest request, List<String> fechas, String reportType, String startDate,
	        String endDate) {
	    HashMap<String, List<String>> prestamoFilters = new HashMap<>();
	    prestamoFilters.put("fecha_contratacion", fechas);
	    try {
	        System.out.println(QueryHelper.createSelectQueryWithBetweens("prestamo", prestamoFilters));
	        PrestamoNegocio prestamoNegocio = new PrestamoNegocioImpl(
	                new PrestamoDAOImpl(QueryHelper.createSelectQueryWithBetweens("prestamo", prestamoFilters)));

	        List<Prestamo> prestamos = prestamoNegocio.list();

	        if (prestamos != null) {
	            switch (reportType) {
	                case "Daily":
	                    Map<LocalDate, List<Long>> prestamosPorFechaDiario = prestamos.stream().collect(Collectors.groupingBy(
	                        Prestamo::getFechaDeContratacion,
	                        LinkedHashMap::new,
	                        Collectors.collectingAndThen(Collectors.toList(), list -> {
	                            long totalPrestamos = list.size();
	                            long prestamosAprobados = list.stream()
	                                .filter(prestamo -> PrestamoEstado.APROBADO.equals(prestamo.getEstado()))
	                                .count();
	                            long prestamosDesaprobados = list.stream()
	                                .filter(prestamo -> PrestamoEstado.DESAPROBADO.equals(prestamo.getEstado()))
	                                .count();
	                            long prestamosRevision = list.stream()
	                                .filter(prestamo -> PrestamoEstado.BAJO_REVISION.equals(prestamo.getEstado()))
	                                .count();
	                            return Arrays.asList(totalPrestamos, prestamosAprobados, prestamosDesaprobados, prestamosRevision);
	                        })
	                    ));
	                    request.setAttribute("prestamosPorFecha", prestamosPorFechaDiario);
	                    break;

	                case "Monthly":
	                    List<YearMonth> listaMeses = listarMeses(startDate, endDate);
	                    Map<YearMonth, List<Long>> prestamosPorFechaMensual = new TreeMap<>();

	                    for (YearMonth yearMonth : listaMeses) {
	                        long totalPrestamos = 0;
	                        long prestamosAprobados = 0;
	                        long prestamosDesaprobados = 0;
	                        long prestamosRevision = 0;

	                        for (Prestamo prestamo : prestamos) {
	                            LocalDate fechaContratacionDate = prestamo.getFechaDeContratacion();
	                            LocalDate yearMonthDate = yearMonth.atDay(1);

	                            if (mismoMesYAnioLocalDate(
	                                    Date.from(yearMonthDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), fechaContratacionDate)) {
	                                totalPrestamos++;
	                                if (PrestamoEstado.APROBADO.equals(prestamo.getEstado())) {
	                                    prestamosAprobados++;
	                                } else if (PrestamoEstado.DESAPROBADO.equals(prestamo.getEstado())) {
	                                    prestamosDesaprobados++;
	                                } else if (PrestamoEstado.BAJO_REVISION.equals(prestamo.getEstado())) {
	                                    prestamosRevision++;
	                                }
	                            }
	                        }

	                        prestamosPorFechaMensual.put(yearMonth,
	                                Arrays.asList(totalPrestamos, prestamosAprobados, prestamosDesaprobados, prestamosRevision));
	                    }
	                    request.setAttribute("prestamosPorFecha", prestamosPorFechaMensual);
	                    break;

	                case "Yearly":
	                    List<Integer> listaAnios = listarAnios(startDate, endDate);
	                    Map<Integer, List<Long>> prestamosPorFechaAnual = new TreeMap<>();

	                    for (Integer anio : listaAnios) {
	                        long totalPrestamos = 0;
	                        long prestamosAprobados = 0;
	                        long prestamosDesaprobados = 0;
	                        long prestamosRevision = 0;

	                        for (Prestamo prestamo : prestamos) {
	                            LocalDate fechaContratacionDate = prestamo.getFechaDeContratacion();
	                            if (mismoAnioLocalDate(
	                                    Date.from(
	                                            LocalDate.of(anio, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()),
	                                    fechaContratacionDate)) {
	                                totalPrestamos++;
	                                if (PrestamoEstado.APROBADO.equals(prestamo.getEstado())) {
	                                    prestamosAprobados++;
	                                } else if (PrestamoEstado.DESAPROBADO.equals(prestamo.getEstado())) {
	                                    prestamosDesaprobados++;
	                                } else if (PrestamoEstado.BAJO_REVISION.equals(prestamo.getEstado())) {
	                                    prestamosRevision++;
	                                }
	                            }
	                        }
	                        prestamosPorFechaAnual.put(anio,
	                                Arrays.asList(totalPrestamos, prestamosAprobados, prestamosDesaprobados, prestamosRevision));
	                    }
	                    request.setAttribute("prestamosPorFecha", prestamosPorFechaAnual);
	                    break;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	private void getHistorial(HttpServletRequest request, List<String> fechas, String reportType, String startDate,
			String endDate) {
		HashMap<String, List<String>> movimientosFilters = new HashMap<>();
		movimientosFilters.put("fecha", fechas);
		try {
			MovimientoNegocioImpl movimientoNegocio = new MovimientoNegocioImpl(null);
			List<Movimiento> movimientos = movimientoNegocio.getMovimientosFechas(startDate, endDate);	
			if (movimientos != null) {
	            switch (reportType) {
	                case "Daily":
	                    Map<Date, Object> movimientosPorFechaDiario = movimientos.stream().collect(Collectors.groupingBy(
	                        Movimiento::getFecha,
	                        LinkedHashMap::new,
	                        Collectors.collectingAndThen(Collectors.toList(), list -> {
	                            long totalMovimientos = list.size();
	                            long movAltaCuenta = list.stream()
	                                .filter(movimiento -> TipoMovimiento.ALTA_DE_CUENTA.equals(movimiento.getTipoMovimiento()))
	                                .count();
	                            long movAltaPrestamo = list.stream()
	                                .filter(movimiento -> TipoMovimiento.ALTA_DE_PRESTAMO.equals(movimiento.getTipoMovimiento()))
	                                .count();
	                            long movPagoPrestamo = list.stream()
	                                .filter(movimiento -> TipoMovimiento.PAGO_DE_PRESTAMO.equals(movimiento.getTipoMovimiento()))
	                                .count();
	                            long movTransferencia = list.stream()
		                                .filter(movimiento -> TipoMovimiento.TRANSFERENCIA.equals(movimiento.getTipoMovimiento()))
		                                .count();
	                            return Arrays.asList(totalMovimientos, movAltaCuenta, movAltaPrestamo, movPagoPrestamo, movTransferencia);
	                        })
	                    ));
	                    request.setAttribute("historial", movimientosPorFechaDiario);
	                    break;

	                case "Monthly":
	                    List<YearMonth> listaMeses = listarMeses(startDate, endDate);
	                    Map<YearMonth, List<Long>> movimientosPorFechaMensual = new TreeMap<>();

	                    for (YearMonth yearMonth : listaMeses) {
	                        long totalMovimientos = 0;
	                        long movAltaCuenta = 0;
	                        long movAltaPrestamo = 0;
	                        long movPagoPrestamo = 0;
	                        long movTransferencia = 0;
	                        
	                        for (Movimiento movimiento : movimientos) {
	                            Date fecha = movimiento.getFecha();
	                            LocalDate yearMonthDate = yearMonth.atDay(1);

	                            if (mismoMesyAnio(fecha,
										Date.from(yearMonthDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
	                            	totalMovimientos++;
	                                if (TipoMovimiento.ALTA_DE_CUENTA.equals(movimiento.getTipoMovimiento())) {
	                                	movAltaCuenta++;
	                                } else if (TipoMovimiento.ALTA_DE_PRESTAMO.equals(movimiento.getTipoMovimiento())) {
	                                	movAltaPrestamo++;
	                                } else if (TipoMovimiento.PAGO_DE_PRESTAMO.equals(movimiento.getTipoMovimiento())) {
	                                	movPagoPrestamo++;
	                                } else {
	                                	movTransferencia++;
	                                }
	                            }
	                        }

	                        movimientosPorFechaMensual.put(yearMonth,
	                        		Arrays.asList(totalMovimientos, movAltaCuenta, movAltaPrestamo, movPagoPrestamo, movTransferencia));
	                    }
	                    request.setAttribute("historial", movimientosPorFechaMensual);
	                    break;

	                case "Yearly":
	                    List<Integer> listaAnios = listarAnios(startDate, endDate);
	                    Map<Integer, List<Long>> movimientosPorFechaAnual = new TreeMap<>();

	                    for (Integer anio : listaAnios) {
	                    	long totalMovimientos = 0;
	                        long movAltaCuenta = 0;
	                        long movAltaPrestamo = 0;
	                        long movPagoPrestamo = 0;
	                        long movTransferencia = 0;

	                        for (Movimiento movimiento : movimientos) {
	                            Date fecha = movimiento.getFecha();
	                            if (mismoAnio(
	                                    Date.from(
	                                            LocalDate.of(anio, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()),
	                                    fecha)) {
	                            	totalMovimientos++;
	                                if (TipoMovimiento.ALTA_DE_CUENTA.equals(movimiento.getTipoMovimiento())) {
	                                	movAltaCuenta++;
	                                } else if (TipoMovimiento.ALTA_DE_PRESTAMO.equals(movimiento.getTipoMovimiento())) {
	                                	movAltaPrestamo++;
	                                } else if (TipoMovimiento.PAGO_DE_PRESTAMO.equals(movimiento.getTipoMovimiento())) {
	                                	movPagoPrestamo++;
	                                } else {
	                                	movTransferencia++;
	                                }
	                            }
	                        }

	                        movimientosPorFechaAnual.put(anio,
	                        		Arrays.asList(totalMovimientos, movAltaCuenta, movAltaPrestamo, movPagoPrestamo, movTransferencia));
	                    }
	                    request.setAttribute("historial", movimientosPorFechaAnual);
	                    break;
	            }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	

	private List<YearMonth> listarMeses(String startDate, String endDate) {
		List<YearMonth> monthsList = new ArrayList<>();
		

		LocalDate start = LocalDate.parse(startDate, formatter);
		LocalDate end = LocalDate.parse(endDate, formatter);

		YearMonth startMonth = YearMonth.from(start);
		YearMonth endMonth = YearMonth.from(end);

		YearMonth currentMonth = startMonth;
		while (!currentMonth.isAfter(endMonth)) {
			monthsList.add(currentMonth);
			currentMonth = currentMonth.plusMonths(1);
		}
		return monthsList;
	}

	public List<Integer> listarAnios(String startDate, String endDate) {
		List<Integer> yearsList = new ArrayList<>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate start = LocalDate.parse(startDate, formatter);
		LocalDate end = LocalDate.parse(endDate, formatter);

		int startYear = start.getYear();
		int endYear = end.getYear();

		for (int year = startYear; year <= endYear; year++) {
			yearsList.add(year);
		}
		return yearsList;
	}

	public boolean mismoMesyAnio(Date fecha1, Date fecha2) {
		Calendar first = Calendar.getInstance();
		first.setTime(fecha1);
		Calendar second = Calendar.getInstance();
		second.setTime(fecha2);
		return ((first.get(Calendar.MONTH) == second.get(Calendar.MONTH))
				&& (first.get(Calendar.YEAR) == second.get(Calendar.YEAR)));

	}

	public boolean mismoAnio(Date fecha1, Date fecha2) {
		Calendar first = Calendar.getInstance();
		first.setTime(fecha1);
		Calendar second = Calendar.getInstance();
		second.setTime(fecha2);
		return ((first.get(Calendar.YEAR) == second.get(Calendar.YEAR)));

	}

	public boolean mismoMesYAnioLocalDate(Date fecha1, LocalDate fecha2) {
		LocalDate localDate1 = fecha1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate1.getMonthValue() == fecha2.getMonthValue() && localDate1.getYear() == fecha2.getYear();
	}

	public boolean mismoAnioLocalDate(Date fecha1, LocalDate fecha2) {
		LocalDate localDate1 = fecha1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate1.getYear() == fecha2.getYear();
	}
}
