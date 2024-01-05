package ru.clevertec.servlet.task.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.servlet.task.config.spring.AppContext;
import ru.clevertec.servlet.task.entity.dto.ClientDto;
import ru.clevertec.servlet.task.exception.ClientValidateException;
import ru.clevertec.servlet.task.pdfcreator.PDFWriter;
import ru.clevertec.servlet.task.service.ClientService;
import ru.clevertec.servlet.task.util.Constants;
import ru.clevertec.servlet.task.util.Util;
import ru.clevertec.servlet.task.validator.ClientValidator;

import java.io.IOException;

@WebServlet("/api/v1/clients/*")
public class ClientServlet extends HttpServlet {

	private final ClientService clientService = AppContext.getInstance().context.getBean(ClientService.class);
	public final ObjectMapper objectMapper = AppContext.getInstance().context.getBean(ObjectMapper.class);
	private final PDFWriter pdfWriter = AppContext.getInstance().context.getBean(PDFWriter.class);
	private final ClientValidator clientValidator = AppContext.getInstance().context.getBean(ClientValidator.class);

	/**
	 * Принимает id Клиента для отображения
	 * Отображает Клиента в формате Json
	 * Сохраняет Клиента в pdf
	 *
	 * @throws Exception если Пользователь не найден
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long id = getIdFromURL(req);
		ClientDto client;
		String request;
		String fileName = "";
		int status;
		try {
			client = clientService.getById(id);
			request = objectMapper.writeValueAsString(client);
			pdfWriter.writeClientToPDF(client, Constants.PATH_PDF_CLIENT, fileName);
			status = 200;
		} catch (Exception e) {
			request = e.getMessage();
			status = 404;
		}
		answerForServer(resp, status, request);
	}

	/**
	 * Принимает id Клиента для обновления и строку в
	 * формате json, трансформирует ее в объект ClientDto и
	 * валидирует его на корректность данных, если валидация не проходит -
	 * выкидывает исключение ClientValidateException с ошибкой о том, какие данные
	 * не правильные
	 *
	 * Передает провалидированный объект на сервис, если валидация прошла успешно,
	 * где происходит его обновление
	 * Если Клиента не существует с таким id - выкидывается исключение ClientNotFoundException
	 *
	 * @throws ClientValidateException если Клиент не прошел валидацию
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long id = getIdFromURL(req);
		String request;
		int status;
		try {
			String json = Util.readInputStream(req.getInputStream());
			ClientDto clientToUpdate = objectMapper.readValue(json, ClientDto.class);
			String errorMessages = clientValidator.validateProduct(clientToUpdate);
			if (!errorMessages.isEmpty()) {
				throw new ClientValidateException(errorMessages);
			}
			clientService.update(id, clientToUpdate);
			request = objectMapper.writeValueAsString(clientToUpdate);
			status = 200;
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
	 * Принимает id Клиента для удаления
	 * и передает ее на сервис
	 *
	 * @throws Exception если Клиент не найден
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Long id = getIdFromURL(req);
		String request;
		int status;
		try {
			clientService.delete(id);
			request = String.format("Client with id %d deleted.", id);
			status = 200;
		} catch (Exception e) {
			request = e.getMessage();
			status = 404;
		}
		answerForServer(resp, status, request);
	}

	/**
	 * Возвращает id из url запроса
	 *
	 * @return id
	 */
	private static Long getIdFromURL(HttpServletRequest req) {
		String url = req.getRequestURI();
		return Long.parseLong(url.substring(Constants.URL_CLIENTS.length()));
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
