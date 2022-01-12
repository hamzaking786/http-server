package no.kristiania.http;

import no.kristiania.question.AnswerDao;
import no.kristiania.question.OptionDao;
import no.kristiania.question.QuestionDao;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PersonnelServer {

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer(8080);
        DataSource dataSource = createDataSource();

        QuestionDao questionDao = new QuestionDao(dataSource);
        AnswerDao answerDao = new AnswerDao(dataSource);
        OptionDao optionDao = new OptionDao(dataSource);

        httpServer.addController("/api/newQuestion", new CreateQuestionController(questionDao));
        httpServer.addController("/api/questions", new ListQuestionController(questionDao, optionDao));
        httpServer.addController("/api/questionOptions", new QuestionOptionsController(questionDao));
        httpServer.addController("/api/addOption", new CreateOptionsController(optionDao));
        httpServer.addController("/api/answerAlternatives", new AnswerAlternativesController(questionDao));
        httpServer.addController("/api/editQuestionForm", new EditQuestionFormController(questionDao));
        httpServer.addController("/api/editQuestion", new EditQuestionController(questionDao));

        logger.info("Started http://localhost:" + httpServer.getPort() + "/index.html");
    }

    private static DataSource createDataSource() throws IOException {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader("pgr203.properties")) {
            properties.load(reader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty("dataSource.url", "jdbc:postgresql://localhost:5432/person_db"));
        dataSource.setUser(properties.getProperty("dataSource.username", "person_dbuser"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }
}
