package com.example.lottery.Controller;

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

@RestController("/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    @ApiMessage("create success")
    public ResponseEntity<User> createUser(@RequestParam User user, String email)throws IdInvaldException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.createUser(user, email));
    }

    @GetMapping("/search-by-email")
    public ResponseEntity<?> searchUserByEmail(@RequestParam("email") String email) {
        try {
            Optional<User> userOptional = userService.searchUserByEmail(email);
            if (userOptional.isPresent()) {
                return ResponseEntity.ok(userOptional.get());
            } else {
                return ResponseEntity.status(404).body("User not found");
            }
        } catch (IdInvaldException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    @GetMapping("search-by-id")
    public ResponseEntity<User> searchUserById(@RequestParam long id) throws IdInvaldException {
        Optional usercheck = this.userService.searchUser(id);
        if (!usercheck.isPresent()) {
            throw new IdInvaldException("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.searchUser(id).get());
    }
    @PutMapping("update")
    @ApiMessage("update success")
    public ResponseEntity<User> updateUser(@RequestParam @RequestBody @Valid User user) throws IdInvaldException {
        Optional<User> usercheck = this.userService.searchUser(user.getId());
        if (!usercheck.isPresent()) {
            throw new IdInvaldException("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.updateUser(user, usercheck.get()));
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
    @GetMapping("get-all-user")
    @ApiMessage("get-all-success")
    public ResponseEntity<ResultPaginationDTO> getAllUser(@Filter Specification spec, Pageable pageable) throws IdInvaldException {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUsers(spec, pageable));
    }

}
