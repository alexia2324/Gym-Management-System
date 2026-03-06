package gym.repository;

import gym.domain.Client;

public class ClientTextFileRepository extends TextFileRepository<Integer, Client> {

    public ClientTextFileRepository(String fileName) {
        super(fileName);
    }

    @Override
    protected Client readEntity(String line) {
        String[] parts = line.split(",");

        int id = Integer.parseInt(parts[0].trim());
        String name = parts[1].trim();
        String email = parts[2].trim();
        String phone = parts[3].trim();

        return new Client(id, name, email, phone);
    }

    @Override
    protected String writeEntity(Client c) {
        return c.getId() + "," + c.getName() + "," + c.getEmail() + "," + c.getPhone();
    }
}
