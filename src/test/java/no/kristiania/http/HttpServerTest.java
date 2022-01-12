package no.kristiania.http;

import no.kristiania.question.*;
import no.kristiania.question.QuestionDao;
import org.junit.jupiter.api.Test;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {

    private final HttpServer server = new HttpServer(0);

    HttpServerTest() throws IOException {
    }

    @Test
    void shouldReturn404ForUnknownRequestTarget() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/non-existing");
        assertEquals(404, client.getStatusCode());
    }

    @Test
    void shouldRespondWithRequestTargetIn404() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/non-existing");
        assertEquals("File not found: /non-existing", client.getMessageBody());
    }


    @Test
    void shouldServeFiles() throws IOException {
        String fileContent = "A file created at " + LocalTime.now();
        Files.write(Paths.get("target/test-classes/example-file.txt"), fileContent.getBytes());

        HttpClient client = new HttpClient("localhost", server.getPort(), "/example-file.txt");
        assertEquals(fileContent, client.getMessageBody());
        assertEquals("text/plain", client.getHeader("Content-Type"));
    }

    @Test
    void shouldUseFileExtensionForContentType() throws IOException {
        String fileContent = "<p>Hello</p>";
        Files.write(Paths.get("target/test-classes/example-file.html"), fileContent.getBytes());

        HttpClient client = new HttpClient("localhost", server.getPort(), "/example-file.html");
        assertEquals("text/html", client.getHeader("Content-Type"));
    }

    @Test
    void shouldAddAndReturnOptionFromServer() throws IOException, SQLException {
        DataSource dataSource = TestData.testDataSource();
        QuestionDao questionDao = new QuestionDao(dataSource);
        OptionDao optionDao = new OptionDao(dataSource);

        server.addController("/api/newQuestion", new CreateQuestionController(questionDao));

        HttpPostClient postClient = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/newQuestion",
                "low_label=Per&high_label=Test&title=Title"
        );
        assertEquals(200, postClient.getStatusCode());
        assertThat(questionDao.listAll())
                .anySatisfy(q -> {
                    System.out.println(q.getId());
                    assertThat(q.getLowLabel()).isEqualTo("Per");
                    assertThat(q.getHighLabel()).isEqualTo("Test");
                    assertThat(q.getTitle()).isEqualTo("Title");
                });

        HttpPostClient postClient2 = new HttpPostClient(
                "localhost",
                server.getPort(),
                "/api/addOption",
                "questions=1&option=low_label"
        );
        System.out.println(postClient2.getStatusCode());
        //assertEquals(200, postClient2.getStatusCode());

    }
}