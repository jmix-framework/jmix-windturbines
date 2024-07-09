package io.jmix.windturbines.test_data.entity;

import io.jmix.security.role.assignment.RoleAssignmentRoleType;
import io.jmix.securitydata.entity.RoleAssignmentEntity;
import io.jmix.windturbines.entity.User;
import io.jmix.windturbines.security.TechnicianRole;
import io.jmix.windturbines.test_data.TestDataProvider;

public class TechnicianRoleData implements TestDataProvider<RoleAssignmentEntity> {

    private final User technician;

    public TechnicianRoleData(User technician) {
        this.technician = technician;
    }

    @Override
    public Class<RoleAssignmentEntity> getEntityClass() {
        return RoleAssignmentEntity.class;
    }

    @Override
    public void accept(RoleAssignmentEntity roleAssignmentEntity) {
        roleAssignmentEntity.setRoleType(RoleAssignmentRoleType.RESOURCE);
        roleAssignmentEntity.setRoleCode(TechnicianRole.CODE);
        roleAssignmentEntity.setUsername(technician.getUsername());
    }
}
