package io.jmix.windturbines.test_data.entity;

import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.security.role.assignment.RoleAssignmentRoleType;
import io.jmix.securitydata.entity.RoleAssignmentEntity;
import io.jmix.windturbines.entity.User;
import io.jmix.windturbines.security.TechnicanRole;
import io.jmix.windturbines.test_data.TestDataProvider;

import static io.jmix.windturbines.test_data.RandomValues.withRandomSuffix;

public class TechnicanRoleData implements TestDataProvider<RoleAssignmentEntity> {

    private final User technican;

    public TechnicanRoleData(User technican) {
        this.technican = technican;
    }

    @Override
    public Class<RoleAssignmentEntity> getEntityClass() {
        return RoleAssignmentEntity.class;
    }

    @Override
    public void accept(RoleAssignmentEntity roleAssignmentEntity) {
        roleAssignmentEntity.setRoleType(RoleAssignmentRoleType.RESOURCE);
        roleAssignmentEntity.setRoleCode(TechnicanRole.CODE);
        roleAssignmentEntity.setUsername(technican.getUsername());
    }
}
