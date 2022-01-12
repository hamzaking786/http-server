package no.kristiania.question;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OptionDao extends AbstractDao<Option> {
    private final DataSource dataSource;

    public OptionDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Option option) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into options (option, question_id) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                statement.setString(1, option.getOption());
                statement.setLong(2, option.getQuestionId());
                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    option.setId(rs.getLong("id"));
                }
            }
        }
    }


    @Override
    public Option retrieve(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from options where id = ?")) {
                statement.setLong(1, id);

                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();

                    return readFromResultSet(rs);
                }
            }
        }
    }


    public List<Option> listAll() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from options")) {
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<Option> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(readFromResultSet(rs));
                    }
                    return result;
                }
            }
        }
    }

    public List<Option> listAllOptionForQuestion(Question question) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from options where question_id = ? ")) {
                statement.setLong(1, question.getId());
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<Option> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(readFromResultSet(rs));
                    }
                    return result;
                }
            }
        }
    }


    @Override
    protected Option readFromResultSet(ResultSet rs) throws SQLException {
        Option option = new Option();
        option.setId(rs.getLong("id"));
        option.setQuestionId(rs.getLong("question_id"));
        option.setOption(rs.getString("option"));

        return option;
    }
}
