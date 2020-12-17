package com.diskagua.api;

import com.diskagua.api.models.Role;
import com.diskagua.api.repository.RoleRepository;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiskAguaApplication {

    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(DiskAguaApplication.class, args);
    }

    @PostConstruct
    public void init() {
        this.roleRepository.save(new Role(null, "ADMIN"));
        this.roleRepository.save(new Role(null, "CLIENTE"));
        this.roleRepository.save(new Role(null, "VENDEDOR"));
    }

}
