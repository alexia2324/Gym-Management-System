package gym.undo;

import gym.domain.Identifiable;
import gym.repository.Repository;

public class ActionRemove<ID, T extends Identifiable<ID>> implements IAction {

    private Repository<ID, T> repo;
    private T removedElem;

    public ActionRemove(Repository<ID, T> repo, T removedElem) {
        this.repo = repo;
        this.removedElem = removedElem;
    }

    @Override
    public void executeUndo() {
        repo.add(removedElem);
    }

    @Override
    public void executeRedo() {
        repo.remove(((gym.domain.Identifiable<ID>) removedElem).getId());
    }
}
