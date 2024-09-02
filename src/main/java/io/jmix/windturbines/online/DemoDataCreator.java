package io.jmix.windturbines.online;

import com.vaadin.flow.component.notification.Notification;
import io.jmix.core.session.SessionData;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.backgroundtask.BackgroundTask;
import io.jmix.flowui.backgroundtask.BackgroundTaskHandler;
import io.jmix.flowui.backgroundtask.BackgroundWorker;
import io.jmix.flowui.backgroundtask.TaskLifeCycle;
import io.jmix.windturbines.test_data.TestDataCreation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Generates demo data in a background task.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class DemoDataCreator {

    @Autowired
    private TestDataCreation testDataCreation;
    @Autowired
    private Notifications notifications;
    @Autowired
    private SessionData sessionData;
    @Autowired
    private BackgroundWorker backgroundWorker;

    public void createDemoData(Runnable finishedHandler) {
        Object demoDataCreated = sessionData.getAttribute("demo-data-created");
        if (!Boolean.TRUE.equals(demoDataCreated)) {
            notifications.create("Generating demo data...")
                    .withPosition(Notification.Position.BOTTOM_END)
                    .show();

            BackgroundTaskHandler<Void> handler = backgroundWorker.handle(new GenerateDemoDataTask(finishedHandler));
            handler.execute();

            sessionData.setAttribute("demo-data-created", true);
        }
    }

    private class GenerateDemoDataTask extends BackgroundTask<Integer, Void> {

        private final Runnable finishedHandler;

        protected GenerateDemoDataTask(Runnable finishedHandler) {
            super(30);
            this.finishedHandler = finishedHandler;
        }

        @Override
        public Void run(TaskLifeCycle<Integer> taskLifeCycle) throws Exception {
            testDataCreation.createData();
            return null;
        }

        @Override
        public void done(Void result) {
            notifications.create("Demo data created 👍")
                    .withType(Notifications.Type.SUCCESS)
                    .withPosition(Notification.Position.BOTTOM_END)
                    .show();
            finishedHandler.run();
        }
    }

}
