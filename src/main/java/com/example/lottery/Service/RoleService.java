package com.example.lottery.Service;

import com.example.lottery.DAO.ResRoleDTO;
import com.example.lottery.DAO.ResultPaginationDTO;
import com.example.lottery.Entity.Permission;
import com.example.lottery.Entity.Role;
import com.example.lottery.Entity.User;
import com.example.lottery.Responsetory.PermissionReponsetory;
import com.example.lottery.Responsetory.RoleReponsetory;
import com.example.lottery.Responsetory.UserReponsetory;
import com.example.lottery.Util.error.IdInvaldException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleReponsetory roleReponsetory;
    private final PermissionReponsetory permissionReponsetory;
    private final UserReponsetory userRepository;


    public RoleService(RoleReponsetory roleReponsetory, PermissionReponsetory permissionReponsetory, UserReponsetory userRepository) {
        this.roleReponsetory = roleReponsetory;
        this.permissionReponsetory = permissionReponsetory;
        this.userRepository = userRepository;
    }

    boolean existsByNameRole(String name){
        return this.roleReponsetory.existsByName(name);
    }
    public  Optional<Role> findById(int id){

        return this.roleReponsetory.findById(id);

    }
    public Role createRole(Role role) throws IdInvaldException {
        boolean existByNameCheck = this.existsByNameRole(role.getName());
        if (existByNameCheck==true){
            throw new IdInvaldException("role này đã tồn tại rồi em");
        }
        return this.roleReponsetory.save(role);
    }

    public Role updateRoles(Role roleFe, Role roleBe)throws IdInvaldException{
//        Optional<Role> optionalRole = this.roleReponsetory.findById(roleFe.getId());
//        if (!optionalRole.isPresent()){
//            throw new IdInvaldException("không có role này mà cập nhập");
//        }
        if (roleFe.getPermissions() != null){
            List<Integer> reqPer = roleFe.getPermissions()
                    .stream()
                    .map(Permission::getId)
                    .collect(Collectors.toList());

            List<Permission> roleBe1 = this.permissionReponsetory.findByIdIn(reqPer);
            roleBe.setPermissions(roleBe1);
        }
        if (roleFe.getName() != null){
            roleBe.setName(roleFe.getName());
        }

        Role role = this.roleReponsetory.save(roleBe);
        return role;

    }
    public void handleDeleteById(int id) throws IdInvaldException{
        Optional<Role> roleOptional = this.roleReponsetory.findById(id);
        if (!roleOptional.isPresent()){
            throw new IdInvaldException("khong co thang role nay");
        }
        Role role = roleOptional.get();
        if (role.getPermissions()!=null){
            for (Permission permission : role.getPermissions()){
                permission.setRoles(null);
                this.permissionReponsetory.save(permission);
            }
        }
        if (role.getUsers() !=null){
            for (User user : role.getUsers()){
                user.setRole(null);
                this.userRepository.save(user);
            }
        }
        this.roleReponsetory.deleteById(id);
    }
    public ResultPaginationDTO getAllJob(Specification<Role> spec, Pageable pageable){
        Page<Role>  rolePage = this.roleReponsetory.findAll(spec, pageable);
//        Page<Permission> pagePermissions = this.permissionReponsetory.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(rolePage.getTotalPages());
        mt.setTotal(rolePage.getTotalPages());
        rs.setMeta(mt);

        List<ResRoleDTO> listRole= rolePage.getContent()
                .stream().map(item -> new ResRoleDTO (
                        item.getId(),
                        item.getName(),
                        item.getDescription(),

                        item.getPermissions().stream()
                                .map(permission -> new ResRoleDTO.PermistionRole(permission.getId(),
                                        permission.getName()))
                                .collect(Collectors.toList())
                )).collect(Collectors.toList());


        rs.setResult(listRole);
        return rs;
    }
}
