package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import exceptions.AuthException;
import exceptions.CustomSecurityException;

/**
 * Servlet Filter implementation class HttpIntercept
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    /**
     * Default constructor. 
     */
    public AuthFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String contextPath = httpRequest.getContextPath();
        String loginPage = contextPath + "/Inicio.jsp";
        String loginServlet = contextPath + "/LoginServlet";
        String rootPath = contextPath + "/";
        String logoutServlet = contextPath + "/LogoutServlet";


        String requestURI = httpRequest.getRequestURI();

        boolean loggedIn = (session != null && session.getAttribute("usuario") != null);
        boolean loginRequest = requestURI.equals(loginPage) || requestURI.equals(loginServlet) || requestURI.equals(rootPath) || request.equals(logoutServlet) ;

        if (loggedIn || loginRequest) {
            chain.doFilter(request, response);
        } else {
            throw new AuthException("Usuario no autenticado. Acceso denegado.");
        }

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
