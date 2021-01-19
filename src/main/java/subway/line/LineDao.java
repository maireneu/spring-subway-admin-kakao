package subway.line;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Line line) {
        String SQL = "INSERT INTO line(name,color) VALUES (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL, new String[]{"id"});
            preparedStatement.setString(1, line.getName());
            preparedStatement.setString(2, line.getColor());
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public boolean hasLineName(String name) {
        String SQL = "SELECT count(*) FROM line WHERE name = ?";
        int count = jdbcTemplate.queryForObject(SQL, Integer.class, name);
        return count != 0;
    }


    public List<Line> getLines() {
        String SQL = "SELECT * FROM line";
        return jdbcTemplate.query(
                SQL,
                (resultSet, rowNum) -> new Line(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("color")
                )
        );
    }

    public Line getLine(long id) {
        String SQL = "SELECT * FROM line WHERE id = ?";
        return jdbcTemplate.queryForObject(
                SQL,
                (resultSet, rowNum) -> new Line(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("color")
                ),
                id
        );
    }

    public void editLineById(long id, String name, String color) {
        String SQL = "UPDATE line SET name = ?, color = ? WHERE id = ?";
        jdbcTemplate.update(SQL, name, color, id);
    }


    public void deleteLineById(long id) {
        String SQL = "DELETE FROM line where id = ?";
        jdbcTemplate.update(SQL, id);
    }
}
