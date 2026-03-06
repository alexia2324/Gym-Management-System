package gym.undo;

import gym.domain.Identifiable;
import gym.undo.IAction;

import gym.repository.Repository;

public class ActionAdd<ID, T extends Identifiable<ID>> implements IAction {

    private Repository<ID, T> repo;
    private T addedElem;

    public ActionAdd(Repository<ID, T> repo, T addedElem) {
        this.repo = repo;
        this.addedElem = addedElem;
    }

    @Override
    public void executeUndo() {
        repo.remove(((gym.domain.Identifiable<ID>) addedElem).getId());
    }

    @Override
    public void executeRedo() {
        repo.add(addedElem);
    }
}
