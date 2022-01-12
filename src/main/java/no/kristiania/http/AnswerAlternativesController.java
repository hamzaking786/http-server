package no.kristiania.http;

import no.kristiania.question.Question;
import no.kristiania.question.QuestionDao;
import java.sql.SQLException;
import java.util.Map;

public class AnswerAlternativesController implements HttpController {
    private final QuestionDao questionDao;

    public AnswerAlternativesController(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String messageBody = "";

        String[] requestLine = request.startLine.split(" ");
        String requestTarget = requestLine[1];
        int questionPos = requestTarget.indexOf('?');

        Map<String, String> parameters = HttpMessage.parseRequestParameters(requestTarget.substring(questionPos+1));
        String questionId = parameters.get("questionId");

        Question question = questionDao.retrieve(Long.parseLong(questionId));

        messageBody += "<option name='low_label' value=" +question.getLowLabel()+">" + question.getLowLabel() + "</option>";
        messageBody += "<option name='high_label value="+question.getHighLabel()+"'>" + question.getHighLabel() + "</option>";

        return new HttpMessage("HTTP/1.1 200 OK", messageBody);
    }
}
