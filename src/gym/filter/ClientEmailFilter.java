package gym.filter;

import gym.domain.Client;

public class ClientEmailFilter implements AbstractFilter<Client> {
    private final String domain;

    public ClientEmailFilter(String domain) {
        this.domain = domain.toLowerCase();
    }

    @Override
    public boolean test(Client client) {
        return client.getEmail().toLowerCase().endsWith(domain);
    }
}
