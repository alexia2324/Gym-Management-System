package gym.undo;

import java.util.Stack;

public class UndoService {

    private Stack<IAction> undoStack = new Stack<>();
    private Stack<IAction> redoStack = new Stack<>();

    public void recordAction(IAction action) {
        undoStack.push(action);
        redoStack.clear();
    }

    public void undo() {
        if (undoStack.isEmpty())
            throw new RuntimeException("Nothing to undo");

        IAction action = undoStack.pop();
        action.executeUndo();
        redoStack.push(action);
    }

    public void redo() {
        if (redoStack.isEmpty())
            throw new RuntimeException("Nothing to redo");

        IAction action = redoStack.pop();
        action.executeRedo();
        undoStack.push(action);
    }
}
