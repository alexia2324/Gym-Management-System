package gym.filter;

import gym.domain.Client;

public class ClientNameFilter implements AbstractFilter<Client> {
    private final String nameSubstring;

    public ClientNameFilter(String nameSubstring) {
        this.nameSubstring = nameSubstring.toLowerCase();
    }

    @Override
    public boolean test(Client client) {
        return client.getName().toLowerCase().contains(nameSubstring);
    }
}
