package io.jmix.windturbines.security;

import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.securityflowui.role.annotation.MenuPolicy;
import io.jmix.securityflowui.role.annotation.ViewPolicy;
import io.jmix.windturbines.entity.*;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import io.jmix.windturbines.entity.inspection.InspectionRecommendation;

@ResourceRole(name = "Technican", code = TechnicanRole.CODE)
public interface TechnicanRole extends UiMinimalRole {
    String CODE = "technican";

    @MenuPolicy(menuIds = {"MaintenanceTask.list", "Turbine.list"})
    @ViewPolicy(viewIds = {"MaintenanceTask.list", "Turbine.list", "MainView", "InspectionRecommendation.detail", "InspectionFinding.detail", "Inspection.detail", "Turbine.detail"})
    void screens();

    @EntityAttributePolicy(entityClass = Inspection.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Inspection.class, actions = {EntityPolicyAction.READ, EntityPolicyAction.UPDATE, EntityPolicyAction.CREATE})
    void inspection();

    @EntityAttributePolicy(entityClass = InspectionFinding.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = InspectionFinding.class, actions = EntityPolicyAction.ALL)
    void inspectionFinding();

    @EntityAttributePolicy(entityClass = InspectionRecommendation.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = InspectionRecommendation.class, actions = EntityPolicyAction.ALL)
    void inspectionRecommendation();

    @EntityAttributePolicy(entityClass = MaintenanceTask.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = MaintenanceTask.class, actions = {EntityPolicyAction.READ, EntityPolicyAction.UPDATE, EntityPolicyAction.CREATE})
    void maintenanceTask();

    @EntityAttributePolicy(entityClass = Manufacturer.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Manufacturer.class, actions = EntityPolicyAction.READ)
    void manufacturer();

    @EntityAttributePolicy(entityClass = Operator.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Operator.class, actions = EntityPolicyAction.READ)
    void operator();

    @EntityAttributePolicy(entityClass = Turbine.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = Turbine.class, actions = EntityPolicyAction.READ)
    void turbine();

    @EntityAttributePolicy(entityClass = User.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = User.class, actions = EntityPolicyAction.READ)
    void user();
}
