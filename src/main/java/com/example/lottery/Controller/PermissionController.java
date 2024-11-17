package com.example.lottery.Controller;

import com.example.lottery.DAO.ResultPaginationDTO;
import com.example.lottery.Entity.Permission;
import com.example.lottery.Service.PermissionService;
import com.example.lottery.Util.annotation.ApiMessage;
import com.example.lottery.Util.error.IdInvaldException;
import com.turkraft.springfilter.boot.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {

    @Autowired
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("tạo oke")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) throws IdInvaldException {
        Permission permissionSave = this.permissionService.createPermission(permission);
        return ResponseEntity.status(HttpStatus.OK).body(permissionSave);

    }

    @PutMapping("permissions")
    @ApiMessage("update oke")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission) throws IdInvaldException {
        Optional<Permission> permissionCheck = this.permissionService.findById(permission.getId());
        if (permissionCheck == null) {
            throw new IdInvaldException("không có permission này em oi");
        }
        Permission permissionUpdate = this.permissionService.handleUpdatePermission(permission, permissionCheck.get());
        return ResponseEntity.status(HttpStatus.OK).body(permissionUpdate);
    }
    @GetMapping("permissions/{id}")
    @ApiMessage("oke")
    public ResponseEntity<Optional<Permission>> getPermissions(@PathVariable int id) throws IdInvaldException{
        Optional<Permission> permissionCheck = this.permissionService.findById(id);
        if (!permissionCheck.isPresent()){
            throw new IdInvaldException("không có id này em oi");
        }
        return ResponseEntity.status(HttpStatus.OK).body(permissionCheck);
    }
    @GetMapping("permissions")
    public ResponseEntity<ResultPaginationDTO> getAllPermission(@Filter Specification<Permission> spec, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.getAllPermission(spec, pageable));
    }
    @DeleteMapping("permissions/{id}")
    @ApiMessage("xoa")
    public ResponseEntity<String> deleteById(@PathVariable int id) throws IdInvaldException {
      this.permissionService.handleDeletePer(id);
      return ResponseEntity.status(HttpStatus.OK).body("xoa oke");
    }
}
