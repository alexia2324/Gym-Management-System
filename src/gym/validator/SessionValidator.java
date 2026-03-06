package gym.validator;

import gym.domain.Session;
import gym.exceptions.ValidationException;

import java.time.LocalDateTime;

public class SessionValidator implements Validator<Session> {

    @Override
    public void validate(Session s) {
        if (s == null)
            throw new ValidationException("Session cannot be null!");

        if (s.getClientId() == null || s.getClientId() <= 0)
            throw new ValidationException("Invalid client ID!");

        if (s.getDateTime() == null)
            throw new ValidationException("Session date and time cannot be null!");

        if (s.getDateTime().isBefore(LocalDateTime.now()))
            throw new ValidationException("Session date and time cannot be in the past!");

        if (s.getDescription() == null || s.getDescription().isBlank())
            throw new ValidationException("Description cannot be empty!");
    }
}
