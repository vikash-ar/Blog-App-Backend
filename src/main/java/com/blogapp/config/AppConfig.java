package com.blogapp.config;

import com.blogapp.model.dao.Role;
import com.blogapp.model.dao.UserDao;
import com.blogapp.repository.RoleRepository;
import com.blogapp.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Configuration
@Slf4j
public class AppConfig implements CommandLineRunner {
    @Bean
    public ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        try {
            Role normal = new Role(1, "ROLE_NORMAL");
            Role admin = new Role(2, "ROLE_ADMIN");
            List<Role> roles = List.of(normal, admin);
            roleRepository.saveAll(roles);

            roles.forEach(role -> log.info(role.getId() +" " + role.getName()));

            UserDao user = new UserDao();
            user.setId(1);
            user.setEmail("chander@mail.com");
            user.setName("chander");
            user.setPassword(passwordEncoder.encode("chander"));
            user.setRoles(Set.of(admin));

            Optional<UserDao> userDb = userRepository.findByEmail("chander@mail.com");
            if(!userDb.isPresent()) user = userRepository.save(user);
            log.info("----Default Admin user : {}----", user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
