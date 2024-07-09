package io.jmix.windturbines.view.inspection;

import com.vaadin.flow.component.virtuallist.VirtualList;
import io.jmix.core.DataManager;
import io.jmix.core.security.SystemAuthenticator;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.data.items.ContainerDataProvider;
import io.jmix.flowui.testassist.UiTestUtils;
import io.jmix.flowui.view.View;
import io.jmix.windturbines.entity.*;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.test_data.EntityTestData;
import io.jmix.windturbines.test_data.entity.*;
import io.jmix.windturbines.test_support.DatabaseCleanup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import io.jmix.flowui.testassist.FlowuiTestAssistConfiguration;
import io.jmix.flowui.testassist.UiTest;
import io.jmix.windturbines.JmixWindturbinesApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@UiTest
@SpringBootTest(classes = {JmixWindturbinesApplication.class, FlowuiTestAssistConfiguration.class})
class InspectionListViewTest {

    @Autowired
    DataManager dataManager;
    @Autowired
    DatabaseCleanup databaseCleanup;
    @Autowired
    EntityTestData entityTestData;
    @Autowired
    ViewNavigators viewNavigators;
    @Autowired
    SystemAuthenticator systemAuthenticator;

    private Turbine vestasV150;
    private Turbine vestasV160;
    private Turbine siemensSG5;
    private User mike;
    private User tom;
    private Inspection mikeVestasV160TomorrowInspection;
    private Inspection mikeVestasV150TodayInspection;
    private Inspection tomSiemensSG5NextWeekInspection;


    @BeforeEach
    void setUp() {
        databaseCleanup.removeAllEntities();

        Manufacturer vestas = entityTestData.saveWithDefaults(new ManufacturerData("Vestas"));
        Manufacturer siemens = entityTestData.saveWithDefaults(new ManufacturerData("Siemens"));

        Operator skywind = entityTestData.saveWithDefaults(new OperatorData(), it -> {
            it.setName("SkyWind Enterprises");
            it.setContactPerson("John Doe");
            it.setAddress("123 Windy Lane");
        });
        Operator windtech = entityTestData.saveWithDefaults(new OperatorData(), it -> {
            it.setName("WindTech Innovations Inc.");
            it.setContactPerson("Jane Smith");
            it.setAddress("456 Breeze Blvd");
        });

        vestasV150 = entityTestData.saveWithDefaults(new TurbineData(vestas, skywind), it -> {
            it.setTurbineId("#738901");
            it.setModel("V150-4.2 MW");
            it.setStatus(TurbineStatus.OPERATING);
            it.setLocation("Santa Barbara, California");
        });

        mike = entityTestData.saveWithDefaults(new TechnicanData());
        entityTestData.saveWithDefaults(new TechnicanRoleData(mike));

        tom = entityTestData.saveWithDefaults(new TechnicanData());
        entityTestData.saveWithDefaults(new TechnicanRoleData(tom));

        mikeVestasV150TodayInspection = entityTestData.saveWithDefaults(new InspectionData(vestasV150, mike), it -> {
            it.setTaskStatus(TaskStatus.SCHEDULED);
            it.setInspectionDate(LocalDate.now());
        });

        vestasV160 = entityTestData.saveWithDefaults(new TurbineData(vestas, skywind), it -> {
            it.setTurbineId("#738902");
            it.setModel("V160-4.2 MW");
            it.setStatus(TurbineStatus.OPERATING);
            it.setLocation("Santa Barbara, California");
        });

        mikeVestasV160TomorrowInspection = entityTestData.saveWithDefaults(new InspectionData(vestasV160, mike), it -> {
            it.setTaskStatus(TaskStatus.SCHEDULED);
            it.setInspectionDate(LocalDate.now().plusDays(1));
        });

        siemensSG5 = entityTestData.saveWithDefaults(new TurbineData(siemens, windtech), it -> {
            it.setTurbineId("#892920");
            it.setModel("SG 5.8-155");
            it.setStatus(TurbineStatus.MAINTENANCE);
            it.setLocation("Palm Springs, California");
        });


        tomSiemensSG5NextWeekInspection = entityTestData.saveWithDefaults(new InspectionData(siemensSG5, tom), it -> {
            it.setTaskStatus(TaskStatus.SCHEDULED);
            it.setInspectionDate(LocalDate.now().plusDays(7));
        });

    }

    @Nested
    class MyInspections {

        @BeforeEach
        void setUp() {
            systemAuthenticator.begin(mike.getUsername());
        }

        @Test
        void when_openListView_then_myInspectionsAreDisplayed() {

            // given
            InspectionListView listView = navigateTo(InspectionListView.class);

            // expect
            assertThat(myInspectionsVirtualListData(listView))
                    .containsExactlyInAnyOrder(mikeVestasV150TodayInspection, mikeVestasV160TomorrowInspection)
                    .doesNotContain(tomSiemensSG5NextWeekInspection);
        }

        @AfterEach
        void tearDown() {
            systemAuthenticator.end();
        }
    }

    @Nested
    class AllInspections {

        @BeforeEach
        void setUp() {
            systemAuthenticator.begin(mike.getUsername());
        }

        @Test
        void when_openListView_then_allInspectionsAreDisplayed() {

            // given
            InspectionListView listView = navigateTo(InspectionListView.class);

            // expect
            assertThat(allInspectionsVirtualListData(listView))
                    .containsExactlyInAnyOrder(
                            mikeVestasV150TodayInspection,
                            mikeVestasV160TomorrowInspection,
                            tomSiemensSG5NextWeekInspection
                    );
        }

        @AfterEach
        void tearDown() {
            systemAuthenticator.end();
        }
    }

    private <V extends View<?>> V navigateTo(Class<V> viewClass) {
        viewNavigators.view(UiTestUtils.getCurrentView(), viewClass).navigate();
        return UiTestUtils.getCurrentView();
    }

    private static List<Inspection> myInspectionsVirtualListData(InspectionListView listView) {
        VirtualList<Inspection> virtualList = myInspectionsVirtualList(listView);
        return ((ContainerDataProvider<Inspection>) virtualList.getDataProvider()).getContainer().getItems();
    }

    private static VirtualList<Inspection> myInspectionsVirtualList(InspectionListView listView) {
        return UiTestUtils.getComponent(listView, "myInspectionsVirtualList");
    }
    private static List<Inspection> allInspectionsVirtualListData(InspectionListView listView) {
        VirtualList<Inspection> virtualList = allInspectionsVirtualList(listView);
        return ((ContainerDataProvider<Inspection>) virtualList.getDataProvider()).getContainer().getItems();
    }

    private static VirtualList<Inspection> allInspectionsVirtualList(InspectionListView listView) {
        return UiTestUtils.getComponent(listView, "allInspectionsVirtualList");
    }
}
