package gym.undo;

import gym.domain.Identifiable;
import gym.repository.Repository;

public class ActionUpdate<ID, T extends Identifiable<ID>> implements IAction {

    private Repository<ID, T> repo;
    private T oldElem; // obiectul înainte de update
    private T newElem; // obiectul după update

    public ActionUpdate(Repository<ID, T> repo, T oldElem, T newElem) {
        this.repo = repo;
        this.oldElem = oldElem;
        this.newElem = newElem;
    }

    @Override
    public void executeUndo() {
        repo.update(oldElem); // readuce starea veche
    }

    @Override
    public void executeRedo() {
        repo.update(newElem); // reaplica update-ul
    }
}
