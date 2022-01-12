package no.kristiania.http;

import no.kristiania.question.Question;
import no.kristiania.question.QuestionDao;
import java.sql.SQLException;
import java.util.Map;

public class EditQuestionFormController implements HttpController {
    private final QuestionDao questionDao;

    public EditQuestionFormController(QuestionDao questionDao) {
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

        messageBody += "<label>Question title: <input type='text' name='title' value='" + question.getTitle() + "'/></label>";
        messageBody += "<label>Label for 1: <input type='text' name='low_label' value='" + question.getLowLabel() + "'/></label>";
        messageBody += "<label>Label for 5: <input type='text' name='high_label' value='" + question.getHighLabel() + "'/></label>";

        return new HttpMessage("HTTP/1.1 200 OK", messageBody);
    }
}
