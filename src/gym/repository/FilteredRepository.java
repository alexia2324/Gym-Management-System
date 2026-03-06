package gym.repository;

import gym.domain.Identifiable;
import gym.filter.AbstractFilter;

import java.util.List;
import java.util.stream.Collectors;

public class FilteredRepository<ID, E extends Identifiable<ID>> extends InMemoryRepository<ID, E> {

    public List<E> filter(AbstractFilter<E> criteria){
        return storage.values().stream()
                .filter(criteria::test)
                .collect(Collectors.toList());
    }
}
