package ru.clevertec.servlet.task.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

@WebServlet("/api/v1/clients_xml")
public class ClientXmlServlet extends HttpServlet {

	private final ClientService clientService = AppContext.getInstance().context.getBean(ClientService.class);
	public final ObjectMapper objectMapper = AppContext.getInstance().context.getBean(ObjectMapper.class);
	private final XmlMapper xmlMapper = AppContext.getInstance().context.getBean(XmlMapper.class);
	private final ClientValidator clientValidator = AppContext.getInstance().context.getBean(ClientValidator.class);

	/**
	 * Принимает строку в формате xml, трансформирует ее в объект ClientDto и
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
			String xml = Util.readInputStream(req.getInputStream());
			ClientDto clientToCreate = xmlMapper.readValue(xml, ClientDto.class);
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
