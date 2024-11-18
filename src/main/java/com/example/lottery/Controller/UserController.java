package com.example.lottery.Controller;

import com.example.lottery.DAO.ResCreateUserDTO;
import com.example.lottery.DAO.ResultPaginationDTO;
import com.example.lottery.Entity.User;
import com.example.lottery.Service.UserService;
import com.example.lottery.Util.annotation.ApiMessage;
import com.example.lottery.Util.error.IdInvaldException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/create")
    @ApiMessage("create success")
    public ResponseEntity<ResCreateUserDTO> createUser(@RequestBody User user)throws IdInvaldException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.converToResUserDTO(this.userService.createUser(user)));
    }
    @GetMapping("/search-by-email")
    public ResponseEntity<ResCreateUserDTO> searchUserByEmail(@RequestParam String email) throws IdInvaldException {
       User userEmail = this.userService.searchUserByEmail(email);

       return ResponseEntity.status(HttpStatus.OK).body(this.userService.converToResUserDTO(userEmail));
    }
    @GetMapping("search-by-id/{id}")
    public ResponseEntity<ResCreateUserDTO> searchUserById(@PathVariable long id) throws IdInvaldException {
        Optional usercheck = this.userService.searchUser(id);
        if (!usercheck.isPresent()) {
            throw new IdInvaldException("User not found");
        }
        User userSearch = this.userService.searchUser(id).get();
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.converToResUserDTO(userSearch));
    }
    @PutMapping("update")
    @ApiMessage("update success")
    public ResponseEntity<ResCreateUserDTO> updateUser(@RequestBody @Valid User user) throws IdInvaldException {
        Optional<User> usercheck = this.userService.searchUser(user.getId());
        if (!usercheck.isPresent()) {
            throw new IdInvaldException("User not found");
        }
        User updateUser = this.userService.updateUser(user, usercheck.get());
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.converToResUserDTO(updateUser));
    }
    @DeleteMapping("delete/{id}")
    @ApiMessage("delete success")
    public ResponseEntity<String> deleteUser(@PathVariable long id) throws IdInvaldException {
        Optional<User> usercheck = this.userService.searchUser(id);
        if (!usercheck.isPresent()) {
            throw new IdInvaldException("User not found");
        }
        this.userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete success");
    }
    @GetMapping("get-all")
    @ApiMessage("get-all-success")
    public ResponseEntity<ResultPaginationDTO> getAllUser(@Filter Specification spec, Pageable pageable) throws IdInvaldException {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUsers(spec, pageable));
    }

}
