package gym.repository;

import gym.domain.Client;
import gym.exceptions.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ClientDBRepository implements Repository<Integer, Client> {
    private final String url;
    private final String username;
    private final String password;

    public ClientDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public void add(Client client) {
        String sql = "INSERT INTO clients (id, name, email, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, client.getId());
            ps.setString(2, client.getName());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getPhone());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error inserting client: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Client> findById(Integer id) {
        String sql = "SELECT * FROM clients WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Client c = new Client(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                return Optional.of(c);
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RepositoryException("Error find client: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Client> update(Client client) {
        Optional<Client> old = findById(client.getId());
        if (old.isEmpty()) return Optional.empty();

        String sql = "UPDATE clients SET name=?, email=?, phone=? WHERE id=?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, client.getName());
            ps.setString(2, client.getEmail());
            ps.setString(3, client.getPhone());
            ps.setInt(4, client.getId());

            ps.executeUpdate();
            return old;

        } catch (SQLException e) {
            throw new RepositoryException("Error updating client: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Client> remove(Integer id) {
        Optional<Client> old = findById(id);
        if (old.isEmpty()) return Optional.empty();

        String sql = "DELETE FROM clients WHERE id=?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return old;

        } catch (SQLException e) {
            throw new RepositoryException("Error deleting client: " + e.getMessage(), e);
        }
    }


    @Override
    public List<Client> getAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error retrieving clients: " + e.getMessage(), e);
        }
        return clients;
    }

    @Override
    public java.util.Iterator<Client> iterator() {
        return getAll().iterator();
    }
}
