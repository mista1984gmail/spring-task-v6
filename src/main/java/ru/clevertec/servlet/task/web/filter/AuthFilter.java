package ru.clevertec.servlet.task.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.servlet.task.config.spring.AppContext;
import ru.clevertec.servlet.task.security.service.JwtService;
import ru.clevertec.servlet.task.util.Constants;

import java.io.IOException;
import java.util.Enumeration;

@WebFilter(urlPatterns = {
		"/api/v1/clients/*", "/api/v1/clients", "/api/v1/clients_xml"
})
public class AuthFilter implements Filter {

	private final static String NO_ACCESS = "No access, wrong UUID token.";

	public final JwtService jwtService = AppContext.getInstance().context.getBean(JwtService.class);

	/**
	 * Предоставляет доступ к ресурсам только Пользователям прошедшим аутентификацию.
	 * Проводит проверку token, предоставленную через
	 * Header uuid_token - он должен совпадать с token Пользователя из базы данных.
	 * Если token не правильный - выводится сообщение об этом
	 * "No access, wrong UUID token."
	 *
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		Enumeration<String> headerNames = httpRequest.getHeaderNames();
		String tokenJWT = null;
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				String key = (String) headerNames.nextElement();
				String value = httpRequest.getHeader(key);
				if (key.equals(Constants.UUID_TOKEN)) {
					tokenJWT = value;
				}
			}
		}
		if (jwtService.validateToken(tokenJWT)) {
			chain.doFilter(request, response);
		} else {
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			httpServletResponse.getOutputStream()
							   .println(NO_ACCESS);
			httpServletResponse.setStatus(403);
		}
	}

	@Override
	public void destroy() {
		jwtService.deleteTokens();
	}

}
