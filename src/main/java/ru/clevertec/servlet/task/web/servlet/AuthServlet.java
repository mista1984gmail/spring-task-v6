package ru.clevertec.servlet.task.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.servlet.task.config.spring.AppContext;
import ru.clevertec.servlet.task.exception.UserValidateException;
import ru.clevertec.servlet.task.security.entity.dto.UserCredentialDto;
import ru.clevertec.servlet.task.security.service.AuthService;
import ru.clevertec.servlet.task.util.Util;
import ru.clevertec.servlet.task.validator.UserValidator;

import java.io.IOException;

@WebServlet("/api/v1/authentication")
public class AuthServlet extends HttpServlet {

	private final static String USER_SAVED = "User saved";
	private final static String USER_NOT_SAVED = "User not saved";

	private final AuthService authService = AppContext.getInstance().context.getBean(AuthService.class);
	private final UserValidator userValidator = AppContext.getInstance().context.getBean(UserValidator.class);
	private final ObjectMapper objectMapper = AppContext.getInstance().context.getBean(ObjectMapper.class);

	/**
	 * Проводит аутентификацию Пользователя,
	 * если аутентификация прошла успешно - возращает token
	 * для доступа к другим ресурсам.
	 *
	 * @throws UserValidateException если Пользователь не прошел валидацию
	 * @throws Exception если Пользователь не найден
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String request;
		int status;
		try {
			String json = Util.readInputStream(req.getInputStream());
			UserCredentialDto userCredential = objectMapper.readValue(json, UserCredentialDto.class);
			String errorMessages = userValidator.validateUser(userCredential);
			if (!errorMessages.isEmpty()) {
				throw new UserValidateException(errorMessages);
			}
			request = authService.authorization(userCredential);
			status = 200;
		}
		catch (UserValidateException e) {
			request = e.getMessage();
			status = 400;
		}
		catch (Exception e) {
			request = e.getMessage();
			status = 404;
		}
		answerForServer(resp, status, request);
	}

	/**
	 * Принимает строку в формате Json, трансформирует ее в объект UserCredentialDto и
	 * валидирует его на корректность данных, если валидация не проходит -
	 * выкидывает исключение UserValidateException с ошибкой о том, какие данные
	 * не правильные
	 *
	 * Передает провалидированный объект на сервис, если валидация прошла успешно,
	 * где происходит его сохранение
	 *
	 * @throws UserValidateException если Пользователь не прошел валидацию
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String request;
		int status;
		try {
			String json = Util.readInputStream(req.getInputStream());
			UserCredentialDto userCredential = objectMapper.readValue(json, UserCredentialDto.class);
			String errorMessages = userValidator.validateUser(userCredential);
			if (!errorMessages.isEmpty()) {
				throw new UserValidateException(errorMessages);
			}
			if(authService.saveUser(userCredential)){
				request = USER_SAVED;
				status = 201;
			}else {
				request = USER_NOT_SAVED;
				status = 400;

			}
		}
		catch (UserValidateException e) {
			request = e.getMessage();
			status = 400;
		}
		catch (Exception e) {
			request = e.getMessage();
			status = 404;
		}
		answerForServer(resp, status, request);
	}

	/**
	 * Формирует и отправляет ответ на сервер
	 *
	 */
	private static void answerForServer(HttpServletResponse resp, int status, String request) throws IOException {
		resp.setStatus(status);
		resp.setHeader("Content-Type", "application/json");
		resp.getOutputStream().println(request);
	}

}
