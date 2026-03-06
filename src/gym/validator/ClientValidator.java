package gym.validator;

import gym.domain.Client;
import gym.exceptions.ValidationException;

public class ClientValidator implements Validator<Client> {
    @Override
    public void validate(Client c) {
        if (c.getName() == null || c.getName().isBlank())
            throw new ValidationException("Name cannot be empty!");
        if (!c.getEmail().contains("@"))
            throw new ValidationException("Invalid email!");
        if (!c.getPhone().matches("\\d{10}"))
            throw new ValidationException("Phone must have 10 digits!");
    }
}
