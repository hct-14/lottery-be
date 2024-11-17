package com.example.lottery.Service;

import com.example.lottery.DAO.ResCreateUserDTO;
import com.example.lottery.DAO.ResultPaginationDTO;
import com.example.lottery.DAO.UserDTO;
import com.example.lottery.Entity.User;
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
public class UserService {
    private final UserReponsetory userReponsetory;
    public UserService(UserReponsetory userReponsetory) {
        this.userReponsetory = userReponsetory;
    }
    public boolean isEmailExist(String email){
        return this.userReponsetory.existsByEmail(email);
    }
    public User handleRegister(User user) {
        return this.userReponsetory.save(user);

    }
    public User createUser(User user,String email) throws IdInvaldException {
        boolean emailCheck = this.userReponsetory.existsByEmail(email);
        if (emailCheck) {
            throw new IdInvaldException("Email already exists");
        }
        return this.userReponsetory.save(user);
    }
    public void deleteUser(long id) throws IdInvaldException {
        Optional<User> userOptional = this.userReponsetory.findById(id);
        if (!userOptional.isPresent()) {
            throw new IdInvaldException("User not found");
        }
        User existingUser = userOptional.get();
        if (existingUser.getHistory() != null && !existingUser.getHistory().isEmpty()) {
            existingUser.getHistory().clear();
            this.userReponsetory.save(existingUser);
        }

        this.userReponsetory.delete(existingUser);
    }
    public User handleGetUserByUsername(String username) {
        return this.userReponsetory.findByEmail(username);
    }
    public User updateUser(User userFe, User userBe) throws IdInvaldException {
        Optional<User> userOptional = this.userReponsetory.findById(userBe.getId());
        if (!userOptional.isPresent()) {
            throw new IdInvaldException("User not found");
        }
        if (userFe.getName()!=null){
            userBe.setName(userFe.getName());
        }
        if (userFe.getEmail()!=null){
            userBe.setEmail(userFe.getEmail());

        }
        if (userFe.getPassword()!=null){
            userBe.setPassword(userFe.getPassword());
        }
        if (userFe.getAddress()!=null){
            userBe.setAddress(userFe.getAddress());
        }
        if (userFe.getAge() >0){
            userBe.setAge(userFe.getAge());
        }
        return this.userReponsetory.save(userBe);
    }

    public Optional<User> searchUser(long id) throws IdInvaldException {
        Optional<User> userOptional = this.userReponsetory.findById(id);
        if (!userOptional.isPresent()) {
            throw new IdInvaldException("User not found");
        }
        return this.userReponsetory.findById(id);
    }

    public Optional<User> searchUserByEmail(String email) throws IdInvaldException {
        User userOptional = this.userReponsetory.findByEmail(email);
        if (userOptional==null) {
            throw new IdInvaldException("User with email not found");
        }
        return this.userReponsetory.findById(userOptional.getId());
    }


    public ResultPaginationDTO getAllUsers(Specification<User> spec, Pageable pageable) throws IdInvaldException {
        Page<User> pageUser = this.userReponsetory.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        List<UserDTO> listUser = pageUser.getContent().stream()
                .map(item -> new UserDTO(
                        item.getId(),
                        item.getName(),
                        item.getEmail(),
                        item.getAddress(),
                        item.getAge()
                ))
                .collect(Collectors.toList());

        // Đặt danh sách LotteryDTO vào ResultPaginationDTO
        rs.setResult(listUser);

        return rs;
    }
    public void updateUserToken(String token, String email){
        User currentUser = this.handleGetUserByUsername(email);
        if(currentUser != null){
            currentUser.setRefreshToken(token);
            this.userReponsetory.save(currentUser);
        }
    }
    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userReponsetory.findByRefreshTokenAndEmail(token, email);
    }

    public ResCreateUserDTO converToResUserDTO(User user){
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setGender(user.getGender());
        res.setEmail(user.getEmail());
        res.setAge(user.getAge());
        res.setAddress(user.getAddress());
        res.setGender(user.getGender());
        ResCreateUserDTO.Role RoleUser = new ResCreateUserDTO.Role();
        if (user.getRole() != null){
            RoleUser.setId(user.getRole().getId());
            RoleUser.setName(user.getRole().getName());
            res.setRole(RoleUser);
        }

        return  res;
    }
}
