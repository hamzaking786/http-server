package no.kristiania.http;

import no.kristiania.question.Question;
import no.kristiania.question.QuestionDao;
import java.sql.SQLException;
import java.util.Map;

public class QuestionController implements HttpController {
    private final QuestionDao questionDao;

    public QuestionController(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
            Map<String, String> parameters = HttpMessage.parseRequestParameters(request.messageBody);
            String questionId = parameters.get("id");

        String messageBody = "";

        Question question = questionDao.retrieve(Long.parseLong(questionId));
        messageBody += "<div><strong>Question: </strong><span>" +
                    "<a href='question.html?id=" + question.getId() + "'>" + question.getTitle() + "</a>" +
                    "</span></div>";


        return new HttpMessage("HTTP/1.1 200 OK", messageBody);
    }
}
