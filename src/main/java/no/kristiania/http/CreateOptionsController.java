package no.kristiania.http;

import no.kristiania.question.Option;
import no.kristiania.question.OptionDao;
import java.sql.SQLException;
import java.util.Map;

public class CreateOptionsController implements HttpController {
    private final OptionDao optionDao;

    public CreateOptionsController(OptionDao optionDao) {
        this.optionDao = optionDao;
    }

    @Override
    public HttpMessage handle(HttpMessage request) throws SQLException {
        Map<String, String> parameters = HttpMessage.parseRequestParameters(request.messageBody);
        Option option = new Option();
        option.setOption(parameters.get("option"));
        option.setQuestionId(Long.parseLong(parameters.get("questions")));
        optionDao.save(option);
        return new HttpMessage("HTTP/1.1 200 OK", "It is done");
    }
}
