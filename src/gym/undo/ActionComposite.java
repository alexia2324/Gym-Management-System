package gym.undo;

import java.util.ArrayList;
import java.util.List;

public class ActionComposite implements IAction {

    private List<IAction> actions = new ArrayList<>();

    public void addAction(IAction action) {
        actions.add(action);
    }

    @Override
    public void executeUndo() {
        for (int i = actions.size() - 1; i >= 0; i--) {
            actions.get(i).executeUndo();
        }
    }

    @Override
    public void executeRedo() {
        for (IAction action : actions) {
            action.executeRedo();
        }
    }
}
