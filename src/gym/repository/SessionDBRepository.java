package gym.repository;

import gym.domain.Session;
import gym.exceptions.RepositoryException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Optional;

public class SessionDBRepository implements Repository<Integer, Session> {
    private final String url;
    private final String username;
    private final String password;

    public SessionDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    private Timestamp toTimestamp(LocalDateTime ldt) {
        return ldt == null ? null : Timestamp.valueOf(ldt);
    }

    private LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }

    @Override
    public void add(Session session) {
        String sql = "INSERT INTO sessions (id, client_id, date_time, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, session.getId());
            ps.setInt(2, session.getClientId());
            // Folosim setString pentru a salva LocalDateTime ca text (ISO 8601),
            // așa cum este gestionat în metoda getAll.
            ps.setString(3, session.getDateTime().toString());
            ps.setString(4, session.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error inserting session: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Session> findById(Integer id) {
        String sql = "SELECT * FROM sessions WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // CORECTIE 1: S-a schimbat "date" în "date_time"
                String dtString = rs.getString("date_time");
                LocalDateTime dt = LocalDateTime.parse(dtString);

                Session s = new Session(
                        rs.getInt("id"),
                        rs.getInt("client_id"),
                        dt,
                        rs.getString("description")
                );
                return Optional.of(s);
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RepositoryException("Error finding session: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Session> update(Session session) {
        Optional<Session> old = findById(session.getId());
        if (old.isEmpty()) return Optional.empty();

        // CORECTIE 2: S-a schimbat "date" în "date_time"
        String sql = "UPDATE sessions SET client_id=?, date_time=?, description=? WHERE id=?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, session.getClientId());
            // Folosim setString pentru a salva LocalDateTime ca text (ISO 8601),
            // similar cu add() și citirea din getAll().
            ps.setString(2, session.getDateTime().toString());
            ps.setString(3, session.getDescription());
            ps.setInt(4, session.getId());

            ps.executeUpdate();
            return old;

        } catch (SQLException e) {
            throw new RepositoryException("Error updating session: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Session> remove(Integer id) {
        Optional<Session> old = findById(id);
        if (old.isEmpty()) return Optional.empty();

        String sql = "DELETE FROM sessions WHERE id=?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return old;

        } catch (SQLException e) {
            throw new RepositoryException("Error deleting session: " + e.getMessage(), e);
        }
    }


    @Override
    public List<Session> getAll() {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM sessions";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // Aceasta este metoda corectă de citire a coloanei "date_time" ca String,
                // care era în concordanță cu metoda add.
                String dtString = rs.getString("date_time");
                LocalDateTime dt = LocalDateTime.parse(dtString);

                sessions.add(new Session(
                        rs.getInt("id"),
                        rs.getInt("client_id"),
                        dt,
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error retrieving sessions: " + e.getMessage(), e);
        }
        return sessions;
    }
    @Override
    public Iterator<Session> iterator() {
        return getAll().iterator();
    }
}