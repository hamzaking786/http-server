package no.kristiania.question;

import org.junit.jupiter.api.Test;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class QuestionDaoTest {

    @Test
    void mainTest() throws SQLException {
        DataSource dataSource = TestData.testDataSource();
        QuestionDao dao = new QuestionDao(dataSource);
        OptionDao od = new OptionDao(dataSource);

        // Create Question
        Question question1 = new Question();
        question1.setTitle("Question1");
        question1.setId(1);
        question1.setLowLabel("Low");
        question1.setHighLabel("High");
        dao.save(question1);
        Question question1FromDb = dao.retrieve(question1.getId());
        assertThat(question1FromDb)
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(question1);

        assertThat(question1FromDb.getTitle()).isEqualTo("Question1");

        // Give answer/option and verify it
        Option option = new Option();
        option.setId(1);
        option.setOption("low_label");
        od.save(option);
        assertThat(od.retrieve(option.getId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(option);

        // Give another answer/option
        option.setOption("high_label");
        od.save(option);

        // Verify both answers
        List<Option> options = od.listAll();
        assertThat(options.get(0).getOption()).isEqualTo("low_label");
        assertThat(options.get(1).getOption()).isEqualTo("high_label");

        // Add a new question1FromDb
        Question question2 = new Question();
        question2.setTitle("Question2");
        question2.setId(2);
        question2.setLowLabel("Low");
        question2.setHighLabel("High");
        dao.save(question2);

        Question question2FromDb = dao.retrieve(question2.getId());
        assertThat(question2FromDb)
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(question2);

        assertThat(question2FromDb.getTitle()).isEqualTo("Question2");
    }
}