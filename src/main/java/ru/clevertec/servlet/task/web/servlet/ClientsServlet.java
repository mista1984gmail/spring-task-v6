package ru.clevertec.servlet.task.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.servlet.task.config.spring.AppContext;
import ru.clevertec.servlet.task.entity.dto.ClientDto;
import ru.clevertec.servlet.task.exception.ClientValidateException;
import ru.clevertec.servlet.task.service.ClientService;
import ru.clevertec.servlet.task.util.Util;
import ru.clevertec.servlet.task.validator.ClientValidator;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/v1/clients")
public class ClientsServlet extends HttpServlet {

	private final static String BAD_PARAMETER = "Bad parameter: sizepage or page.";

	private final ClientService clientService = AppContext.getInstance().context.getBean(ClientService.class);
	public final ObjectMapper objectMapper = AppContext.getInstance().context.getBean(ObjectMapper.class);
	private final ClientValidator clientValidator = AppContext.getInstance().context.getBean(ClientValidator.class);

	/**
	 * Отображает всех Клиентов c пагинацией и сортировкой.
	 * Сортировать можно по имени (sorting = "first_name" - по умолчению),
	 * по дате рождения (sorting = "birthday") и по дате регистрации
	 * (sorting = "registration_date").
	 * Параметр page - номер страницы, параметр pagesize -
	 * количество Клиентов на одной странице.
	 * Если параметры pagesize или page заданы не верно
	 * выводится соответствующее сообщение.
	 *
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String request;
		int status;
		int pagesize = Integer.parseInt(req.getParameter("pagesize"));
		int page = Integer.parseInt(req.getParameter("page"));
		String sorting = req.getParameter("sorting");
		if (pagesize >= 0 & page > 0) {
			try {
				List<ClientDto> clients = clientService.getClientsWithPagination(pagesize, page, sorting);
				request = clients.stream()
								 .map(client -> {
									 try {
										 return objectMapper.writeValueAsString(client);
									 } catch (JsonProcessingException e) {
										 throw new RuntimeException(e);
									 }
								 })
								 .collect(Collectors.joining("\n"));
				status = 200;
			} catch (Exception e) {
				request = e.getMessage();
				status = 404;
			}
		} else {
			status = 400;
			request = BAD_PARAMETER;
		}
		answerForServer(resp, status, request);
	}

	/**
	 * Принимает строку в формате Json, трансформирует ее в объект ClientDto и
	 * валидирует его на корректность данных, если валидация не проходит -
	 * выкидывает исключение ClientValidateException с ошибкой о том, какие данные
	 * не правильные
	 *
	 * Передает провалидированный объект на сервис, если валидация прошла успешно,
	 * для сохранения Клиента
	 *
	 * @throws ClientValidateException если Клиент не прошел валидацию
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String request;
		int status;
		try {
			String json = Util.readInputStream(req.getInputStream());
			ClientDto clientToCreate = objectMapper.readValue(json, ClientDto.class);
			String errorMessages = clientValidator.validateProduct(clientToCreate);
			if (!errorMessages.isEmpty()) {
				throw new ClientValidateException(errorMessages);
			}
			clientService.create(clientToCreate);
			request = objectMapper.writeValueAsString(clientToCreate);
			status = 201;
		} catch (ClientValidateException e) {
			request = e.getMessage();
			status = 400;
		} catch (Exception e) {
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
