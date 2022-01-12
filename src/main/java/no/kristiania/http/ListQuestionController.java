package no.kristiania.http;

import no.kristiania.question.*;

import java.sql.SQLException;

public class ListQuestionController implements HttpController {
    private final QuestionDao questionDao;
    private OptionDao optionDao;

    public ListQuestionController(QuestionDao questionDao, OptionDao optionDao) {
        this.questionDao = questionDao;
        this.optionDao = optionDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        String messageBody = "";

        for (Question question : questionDao.listAll()) {
            messageBody += "<div><strong>Question: </strong><span>" +
                    question.getTitle() +
                    "</span>";
            for (Option options: optionDao.listAllOptionForQuestion(question)){
                messageBody += "<div><strong> Answer: </strong><span>" +
                options.getOption() +
                        "</span></div>";

            }
        }

        return new HttpMessage("HTTP/1.1 200 OK", messageBody);
    }
}
