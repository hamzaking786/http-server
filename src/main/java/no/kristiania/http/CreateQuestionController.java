package no.kristiania.http;

import no.kristiania.question.Question;
import no.kristiania.question.QuestionDao;

import java.sql.SQLException;
import java.util.Map;

public class CreateQuestionController implements HttpController {
    private final QuestionDao questionDao;

    public CreateQuestionController(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> parameters = HttpMessage.parseRequestParameters(request.messageBody);
        Question question = new Question();
        question.setTitle(parameters.get("title").replace("+", " "));
        question.setLowLabel(parameters.get("low_label"));
        question.setHighLabel(parameters.get("high_label"));
        questionDao.save(question);
        return new HttpMessage("HTTP/1.1 200 OK", "It is done");
    }
}
