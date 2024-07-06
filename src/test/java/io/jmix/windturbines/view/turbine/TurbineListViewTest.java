package io.jmix.windturbines.view.turbine;

import com.vaadin.flow.component.virtuallist.VirtualList;
import io.jmix.core.DataManager;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.data.items.ContainerDataProvider;
import io.jmix.flowui.testassist.FlowuiTestAssistConfiguration;
import io.jmix.flowui.testassist.UiTest;
import io.jmix.flowui.testassist.UiTestUtils;
import io.jmix.flowui.view.View;
import io.jmix.windturbines.JmixWindturbinesApplication;
import io.jmix.windturbines.entity.Manufacturer;
import io.jmix.windturbines.entity.Operator;
import io.jmix.windturbines.entity.Turbine;
import io.jmix.windturbines.entity.TurbineStatus;
import io.jmix.windturbines.test_data.EntityTestData;
import io.jmix.windturbines.test_data.entity.ManufacturerData;
import io.jmix.windturbines.test_data.entity.OperatorData;
import io.jmix.windturbines.test_data.entity.TurbineData;
import io.jmix.windturbines.test_support.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@UiTest
@SpringBootTest(classes = {JmixWindturbinesApplication.class, FlowuiTestAssistConfiguration.class})
public class TurbineListViewTest {

    @Autowired
    DataManager dataManager;
    @Autowired
    DatabaseCleanup databaseCleanup;
    @Autowired
    EntityTestData entityTestData;
    @Autowired
    ViewNavigators viewNavigators;

    private Turbine vestasV150;
    private Turbine vestasV160;
    private Turbine siemensSG5;


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

        vestasV160 = entityTestData.saveWithDefaults(new TurbineData(vestas, skywind), it -> {
            it.setTurbineId("#738902");
            it.setModel("V160-4.2 MW");
            it.setStatus(TurbineStatus.OPERATING);
            it.setLocation("Santa Barbara, California");
        });

        siemensSG5 = entityTestData.saveWithDefaults(new TurbineData(siemens, windtech), it -> {
            it.setTurbineId("#892920");
            it.setModel("SG 5.8-155");
            it.setStatus(TurbineStatus.MAINTENANCE);
            it.setLocation("Palm Springs, California");
        });
    }

    @Test
    void when_openListView_then_allTurbinesAreDisplayed() {

        // given
        TurbineListView listView = navigateTo(TurbineListView.class);

        // expect
        assertThat(turbinesVirtualListData(listView))
                .containsExactlyInAnyOrder(vestasV150, vestasV160, siemensSG5);
    }


    @Nested
    class SearchFunctionality {

        @Test
        void when_searchByTurbineId_then_oneOneMatch() {

            // given
            TurbineListView listView = navigateTo(TurbineListView.class);

            assertThat(turbinesVirtualListData(listView))
                    .contains(vestasV150, vestasV160, siemensSG5);

            // when
            searchFor(listView, vestasV150.getTurbineId());

            // then
            assertThat(turbinesVirtualListData(listView))
                    .containsExactly(vestasV150);
        }

        @Test
        void when_searchByManufacturer_then_allTurbinesWithMatchingManufacturerAreFound() {

            // given
            TurbineListView listView = navigateTo(TurbineListView.class);

            assertThat(turbinesVirtualListData(listView))
                    .contains(vestasV150, vestasV160, siemensSG5);

            // when
            searchFor(listView, "Vestas");

            // then
            assertThat(turbinesVirtualListData(listView))
                    .containsExactlyInAnyOrder(vestasV150, vestasV160);
        }
        @Test
        void when_searchByManufacturerWithPartialName_then_allTurbinesWithMatchingManufacturerAreFound() {

            // given
            TurbineListView listView = navigateTo(TurbineListView.class);

            assertThat(turbinesVirtualListData(listView))
                    .contains(vestasV150, vestasV160, siemensSG5);

            // when
            searchFor(listView, "Vest");

            // then
            assertThat(turbinesVirtualListData(listView))
                    .containsExactlyInAnyOrder(vestasV150, vestasV160);
        }

        @Test
        void when_searchByOperator_then_allTurbinesWithMatchingOperatorAreFound() {

            // given
            TurbineListView listView = navigateTo(TurbineListView.class);

            assertThat(turbinesVirtualListData(listView))
                    .contains(vestasV150, vestasV160, siemensSG5);

            // when
            searchFor(listView, siemensSG5.getOperator().getName());

            // then
            assertThat(turbinesVirtualListData(listView))
                    .containsExactlyInAnyOrder(siemensSG5);
        }

        @Test
        void when_searchByStatus_then_allTurbinesWithMatchingStatusAreFound() {

            // given
            TurbineListView listView = navigateTo(TurbineListView.class);

            // and
            assertThat(turbinesVirtualListData(listView))
                    .contains(vestasV150, vestasV160, siemensSG5);

            // and
            assertThat(vestasV150.getStatus()).isEqualTo(TurbineStatus.OPERATING);
            assertThat(vestasV160.getStatus()).isEqualTo(TurbineStatus.OPERATING);
            assertThat(siemensSG5.getStatus()).isEqualTo(TurbineStatus.MAINTENANCE);

            // when
            searchFor(listView, "Maintenance");

            // then
            assertThat(turbinesVirtualListData(listView))
                    .containsExactlyInAnyOrder(siemensSG5);
        }
        @Test
        void when_searchByLocation_then_allTurbinesWithMatchingLocationsAreFound() {

            // given
            TurbineListView listView = navigateTo(TurbineListView.class);

            // and
            assertThat(turbinesVirtualListData(listView))
                    .contains(vestasV150, vestasV160, siemensSG5);

            // and
            assertThat(vestasV150.getLocation()).contains("Santa Barbara");
            assertThat(vestasV160.getLocation()).contains("Santa Barbara");
            assertThat(siemensSG5.getLocation()).doesNotContain("Santa Barbara");

            // when
            searchFor(listView, "Santa Barbara");

            // then
            assertThat(turbinesVirtualListData(listView))
                    .containsExactlyInAnyOrder(vestasV150, vestasV160);
        }

        private void searchFor(TurbineListView listView, String searchValue) {
            TypedTextField<String> searchField = UiTestUtils.getComponent(listView, "searchField");
            searchField.setValue(searchValue);
        }
    }

    private <V extends View<?>> V navigateTo(Class<V> viewClass) {
        viewNavigators.view(UiTestUtils.getCurrentView(), viewClass).navigate();
        return UiTestUtils.getCurrentView();
    }

    private static List<Turbine> turbinesVirtualListData(TurbineListView listView) {
        VirtualList<Turbine> turbinesVirtualList = turbinesVirtualList(listView);
        return ((ContainerDataProvider<Turbine>) turbinesVirtualList.getDataProvider()).getContainer().getItems();
    }

    private static VirtualList<Turbine> turbinesVirtualList(TurbineListView listView) {
        return UiTestUtils.getComponent(listView, "turbinesVirtualList");
    }
}
