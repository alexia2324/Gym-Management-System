package gym.filter;

import gym.domain.Session;
import java.time.LocalDateTime;

public class SessionDateFilter implements AbstractFilter<Session> {
    private final LocalDateTime afterDate;

    public SessionDateFilter(LocalDateTime afterDate) {
        this.afterDate = afterDate;
    }

    @Override
    public boolean test(Session session) {
        return session.getDateTime().isAfter(afterDate);
    }
}
