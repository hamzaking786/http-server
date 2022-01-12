package no.kristiania.http;

import no.kristiania.question.Question;
import no.kristiania.question.QuestionDao;

import java.sql.SQLException;

public class QuestionOptionsController implements HttpController {
    private final QuestionDao questionDao;

    public QuestionOptionsController(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String messageBody = "";

        for (Question question : questionDao.listAll()) {
            messageBody += "<option value=" + (question.getId()) + ">" + question.getTitle() + "</option>";
        }


        return new HttpMessage("HTTP/1.1 200 OK", messageBody);
    }
}
