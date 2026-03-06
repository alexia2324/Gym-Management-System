package gym.validator;

public interface Validator<T> {
    void validate(T entity);
}
