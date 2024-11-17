package com.example.lottery.Service;


import com.example.lottery.DAO.ResPermissionDTO;
import com.example.lottery.DAO.ResultPaginationDTO;
import com.example.lottery.Entity.Permission;
import com.example.lottery.Entity.Role;
import com.example.lottery.Responsetory.PermissionReponsetory;
import com.example.lottery.Responsetory.RoleReponsetory;
import com.example.lottery.Util.error.IdInvaldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    @Autowired
    private final PermissionReponsetory permissionReponsetory;
    private final RoleReponsetory roleReponsetory;

    public PermissionService(PermissionReponsetory permissionReponsetory, RoleReponsetory roleReponsetory) {
        this.permissionReponsetory = permissionReponsetory;
        this.roleReponsetory = roleReponsetory;
    }

    public Optional<Permission> findById(int id) {
        return this.permissionReponsetory.findById(id);
    }

    //    public List<Permission> findAll() {
//        return this.permissionReponsetory.findAll();
//    }
    boolean isExistsByModuleAndAndApiPathAndAndMethodCheck(Permission permission) {
        return permissionReponsetory.existsByModuleAndApiPathAndMethod(
                permission.getModule(),
                permission.getApiPath(),
                permission.getMethod()
        );

    }

    boolean isExistsByModuleCheck(String module) {
        return permissionReponsetory.existsByModule(module);
    }

    boolean isExistsbyApiPath(String apiPath) {
        return permissionReponsetory.existsByApiPath(apiPath);

    }

    boolean isExistsByMethod(String method) {
        return permissionReponsetory.existsByMethod(method);
    }

    //    boolean existsby
    public Permission createPermission(Permission permission) throws IdInvaldException {
//        boolean permissionCheck = this.permissionReponsetory.existsByNoduleAndAndApiPathAndAndMethod(permission.getModule(), permission.getApiPath(), permission.getMethod());
        boolean permissionCheckAll = this.isExistsByModuleAndAndApiPathAndAndMethodCheck(permission);
        boolean permissionCheckModule = this.isExistsByModuleCheck(permission.getModule());
        boolean permissionCheckPath = this.isExistsbyApiPath(permission.getApiPath());
        boolean permissionCheckMethod = this.isExistsByMethod(permission.getMethod());
        if (permissionCheckAll == true) {
            throw new IdInvaldException("3 cái thằng này tồn tại rồi em ơi");
        }
        if (permissionCheckModule == true) {
            throw new IdInvaldException("cái thằng module này tồn tại rồi em oi");
        }
        if (permissionCheckPath == true) {
            throw new IdInvaldException("Cái thằng path này tồn tại rồi em ơi");
        }
        if (permissionCheckMethod == true) {
            throw new IdInvaldException("cái thằng method này tồn tại rồi em ơi");
        }


        return this.permissionReponsetory.save(permission);


//        return this.permissionReponsetory.save(permission);

    }

    public Permission handleUpdatePermission(Permission permisFe, Permission permisBe) {
        Optional<Permission> optionalPermission = this.permissionReponsetory.findById(permisFe.getId());
        if (!optionalPermission.isPresent()) {
            // Xử lý trường hợp không tìm thấy Permission, ví dụ: ném ra một exception
            throw new IllegalArgumentException("Permission not found");
        }

        // Cập nhật các trường nếu giá trị trong permisFe không phải null
        if (permisFe.getName() != null) {
            permisBe.setName(permisFe.getName());
        }
        if (permisFe.getApiPath() != null) {
            permisBe.setApiPath(permisFe.getApiPath());
        }
        if (permisFe.getMethod() != null) {
            permisBe.setMethod(permisFe.getMethod());
        }
        if (permisFe.getModule() != null) {
            permisBe.setModule(permisFe.getModule());
        }

        Permission per = this.permissionReponsetory.save(permisBe);

        if (permisFe.getRoles() != null) {

            List<Integer> reqRoleIds = permisFe.getRoles()
                    .stream()
                    .map(Role::getId)
                    .collect(Collectors.toList());

            List<Role> dbRoles = this.roleReponsetory.findByIdIn(reqRoleIds);

            permisBe.setRoles(dbRoles);
        }

        return per;
    }

    public ResultPaginationDTO getAllPermission(Specification<Permission> spec, Pageable pageable){
        Page<Permission> pagePermissions = this.permissionReponsetory.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pagePermissions.getTotalPages());
        mt.setTotal(pagePermissions.getTotalPages());

        rs.setMeta(mt);

        List<ResPermissionDTO> listPer = pagePermissions.getContent()
                .stream().map(item -> new ResPermissionDTO(
                        item.getId(),
                        item.getName(),
                        item.getApiPath(),
                        item.getMethod(),
                        item.getModule(),

                        item.getRoles().stream()
                                .map(role -> new ResPermissionDTO.RolePermission(role.getId(),
                                        role.getName(), role.getDescription()))
                                .collect(Collectors.toList())
                )).collect(Collectors.toList());

        rs.setResult(listPer);
        return rs;
    }

    public void handleDeletePer(int id) throws IdInvaldException {
        Optional<Permission> permissionCheck = this.permissionReponsetory.findById(id);
        if (!permissionCheck.isPresent()){
            throw new IdInvaldException("khong co thang nay dau");
        }
      Permission permission = permissionCheck.get();
        if (permission.getRoles() !=null){
            for (Role role : permission.getRoles()){
                role.setPermissions(null);
                this.roleReponsetory.save(role);
            }
        }
        this.permissionReponsetory.deleteById(id);

//        return permission;
    }


}
