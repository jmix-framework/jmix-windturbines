package io.jmix.windturbines.view.turbine;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.Messages;
import io.jmix.core.metamodel.datatype.DatatypeFormatter;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.component.virtuallist.JmixVirtualList;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import io.jmix.windturbines.entity.TaskStatus;
import io.jmix.windturbines.entity.Turbine;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.view.inspection.MyInspectionCard;
import io.jmix.windturbines.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Route(value = "turbines/:id", layout = MainView.class)
@ViewController("Turbine.detail")
@ViewDescriptor("turbine-detail-view.xml")
@EditedEntityContainer("turbineDc")
public class TurbineDetailView extends StandardDetailView<Turbine> implements HasDynamicTitle {
    @ViewComponent
    private TypedTextField<String> operatorPhoneField;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private Messages messages;
    @Autowired
    private Fragments fragments;
    @ViewComponent
    private H3 pageTitle;
    @ViewComponent
    private MessageBundle messageBundle;
    @ViewComponent
    private Span statusField;

    @Subscribe
    public void onAttachEvent(final AttachEvent event) {
        operatorPhoneField.setSuffixComponent(callButton(getEditedEntity().getOperator().getPhone()));
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {

        pageTitle.setText(turbineTitle());

        statusField.addClassName("turbine-status");
        statusField.getElement().getThemeList().addAll(List.of("badge", "pill", getEditedEntity().getStatus().getBadgeThemeName()));
        statusField.setWidth("120px");
        statusField.setText(messages.getMessage(getEditedEntity().getStatus()));
    }

    private String turbineTitle() {
        return messageBundle.formatMessage("turbineDetailView.title", getEditedEntity().getTurbineId());
    }

    @Override
    public String getPageTitle() {
        return turbineTitle();
    }

    private Button callButton(String phoneNumber) {
        Button btn = uiComponents.create(Button.class);
        btn.setIcon(VaadinIcon.PHONE.create());
        btn.addClassNames("call-button", LumoUtility.BorderRadius.SMALL, LumoUtility.Background.CONTRAST_5);
        btn.addClickListener(e ->
                // We're using executeJs with window.open instead of UI.getCurrent().getPage().open()
                // because Vaadin internally calls 'this.stopApplication()', which breaks the application
                // when the system dialog asking to call the number is cancelled.
                UI.getCurrent().getPage().executeJs("window.open($0, '_self')", "tel:" + phoneNumber)
        );
        return btn;
    }

    @Supply(to = "inspectionsVirtualList", subject = "renderer")
    private Renderer<Inspection> inspectionsVirtualListRenderer() {
        return new ComponentRenderer<>(inspection -> {
            MyInspectionCard card = fragments.create(this, MyInspectionCard.class);
            card.setInspection(inspection);
            card.setOriginView(this);
            return card;
        });
    }

    @Subscribe("back")
    public void onBack(final ActionPerformedEvent event) {
        closeWithDiscard();
    }

}
