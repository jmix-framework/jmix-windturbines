package io.jmix.windturbines.test_support.jmix;

import io.jmix.flowui.view.DialogWindow;
import io.jmix.flowui.view.View;

import java.util.ArrayList;
import java.util.List;

public class DialogWindowsTracking {
    private final List<DialogWindow<?>> dialogWindows = new ArrayList<>();

    public <V extends View<?>> void add(DialogWindow<V> dialogWindow) {
        dialogWindows.add(dialogWindow);
    }

    public void clear() {
        this.dialogWindows.clear();
    }

    public <T> T dialogWindowOfType(Class<T> viewClass) {
        return dialogWindows.stream()
                .filter(it -> viewClass.isAssignableFrom(it.getView().getClass()))
                .map(DialogWindow::getView)
                .map(viewClass::cast)
                .reduce((first, second) -> second)
                .orElse(null);
    }
}
