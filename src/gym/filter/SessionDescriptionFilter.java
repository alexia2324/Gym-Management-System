package gym.filter;

import gym.domain.Session;

public class SessionDescriptionFilter implements AbstractFilter<Session> {
    private final String keyword;

    public SessionDescriptionFilter(String keyword) {
        this.keyword = keyword.toLowerCase();
    }

    @Override
    public boolean test(Session session) {
        return session.getDescription().toLowerCase().contains(keyword);
    }
}
