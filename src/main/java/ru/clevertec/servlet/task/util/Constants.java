package ru.clevertec.servlet.task.util;

import java.util.List;

public class Constants {

    public static final String CACHE_TYPE_LRU = "LRU";
    public static final String CACHE_TYPE_LFU = "LFU";
    public static final String PROPERTIES_FILE_NAME = "application.properties";
    public static final String DATABASE_URL = "spring.jdbc.connection.url";
    public static final String DATABASE_USERNAME = "spring.jdbc.connection.username";
    public static final String DATABASE_PASSWORD = "spring.jdbc.connection.password";
    public static final String LIQUIBASE_CHANGE_LOG = "classpath:db/changelog/db.changelog-master.xml";
    public static final String PATH_PDF_CLIENT = "D:/Downloads/pdf/reports/info_about_client";
    public static final String DATE_TIME_PATTERN_FOR_FILE_NAME = "dd-MM-yyyy-HH-mm-ss";
    public static final String DATE_TIME_PATTERN_FOR_PDF = "dd.MM.yyyy HH:mm:ss";
    public static final String DATE_PATTERN_FOR_PDF = "dd.MM.yyyy";
    public static final String DATE_TIME_PATTERN_FOR_PAGINATION = "dd.MM.yyyy HH:mm:ss";
    public static final String URL_CLIENTS = "/service/api/v1/clients/";
    public static final String PATH_TO_BACKGROUND = "/usr/local/tomcat/webapps/service/WEB-INF/classes/img/Clevertec_Template.jpg";
    public static final String PATH_TO_TIMES_ROMAN_FONT = "font/times-ro.ttf";
    public static final String TITLE_INFO_CLIENT = "Info about client: ";
    public static final String TITLE_INFO_CLIENTS = "List of clients";
    public static final String INFO_CLIENT_AGE = "Age: ";
    public static final String INFO_CLIENT_EMAIL = "Email: ";
    public static final String INFO_CLIENT_TELEPHONE = "Telephone: ";
    public static final String INFO_CLIENT_REGISTRATION_DATE = "Registration date: ";
    public static final List<String> FIELDS_CLIENT_DTO = List.of("#", "First name", "Last name", "Email", "Telephone", "Date of birth", "Registration date");
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final  String UUID_TOKEN = "uuid_token";

}
