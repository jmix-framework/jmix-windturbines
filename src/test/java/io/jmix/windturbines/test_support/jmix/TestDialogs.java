package io.jmix.windturbines.test_support.jmix;

import com.vaadin.flow.component.dialog.Dialog;
import io.jmix.core.Messages;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.UiViewProperties;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.backgroundtask.BackgroundWorker;
import io.jmix.flowui.impl.DialogsImpl;
import io.jmix.flowui.kit.action.Action;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestDialogs extends DialogsImpl {

    private final List<TestOptionDialog> optionDialogs = new ArrayList<>();

    public TestDialogs(ApplicationContext applicationContext, Messages messages, UiViewProperties flowUiViewProperties, DialogWindows dialogWindows, UiComponents uiComponents, BackgroundWorker backgroundWorker) {
        super(applicationContext, messages, flowUiViewProperties, dialogWindows, uiComponents, backgroundWorker);
    }

    public List<TestOptionDialog> getOptionDialogs() {
        return optionDialogs;
    }

    public TestOptionDialog openedOptionDialog() {
        return optionDialogs.get(0);
    }

    @Override
    public TestOptionDialogBuilder createOptionDialog() {
        return new TestOptionDialogBuilder();
    }

    public void clear() {
        optionDialogs.clear();
    }

    class TestOptionDialogBuilder extends OptionDialogBuilderImpl {

        @Override
        public Dialog open() {
            optionDialogs.add(new TestOptionDialog(dialog, Arrays.stream(actions).toList()));
            return super.open();
        }
    }


    public static class TestOptionDialog {

        private final Dialog dialog;
        private final List<Action> actions;


        public TestOptionDialog(Dialog dialog, List<Action> actions) {
            this.dialog = dialog;
            this.actions = actions;
        }

        public void closeWith(DialogAction.Type actionType) {

            Action foundAction = actions.stream()
                    .filter(action -> action.getId().equals(actionType.getId()))
                    .findFirst()
                    .orElseThrow();

            foundAction.actionPerform(null);

        }
    }
}
