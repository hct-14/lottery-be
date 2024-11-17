package com.example.lottery.Responsetory;

import com.example.lottery.Entity.Permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PermissionReponsetory extends JpaRepository<Permission, Integer>, JpaSpecificationExecutor<Permission> {

//    boolean existsByNoduleAndAndApiPathAndAndMethod(Permission permission);

    boolean existsByModuleAndApiPathAndMethod(String module, String apiPath, String method);
    boolean existsByModule(String module);
    boolean existsByApiPath(String apiPath);
    boolean existsByMethod(String method);
    List<Permission> findByIdIn(List<Integer> id);

}
