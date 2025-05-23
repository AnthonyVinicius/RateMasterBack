package br.com.ifpe.ratemaster.controller;

import br.com.ifpe.ratemaster.entity.UserModel;
import br.com.ifpe.ratemaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public List<UserModel> listAllUsers() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> findUserById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    private UserModel registerUser(@RequestBody UserModel userModel) {
        return service.saveUser(userModel);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserModel> updateUser(@PathVariable Long id, @RequestBody UserModel userModel) {
        return service.findById(id)
                    .map(newUser -> {
                        newUser.setName(userModel.getName());
                        newUser.setEmail(userModel.getEmail());

                        UserModel updatedUser = service.saveUser(newUser);
                        return ResponseEntity.ok(updatedUser);
                    })
                .orElse(ResponseEntity.notFound().build());
    }
}