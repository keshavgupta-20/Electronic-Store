package com.yash.Electronic.store.service.impl;

import com.yash.Electronic.store.entites.Role;
import com.yash.Electronic.store.entites.User;
import com.yash.Electronic.store.exception.ResourceNotFoundException;
import com.yash.Electronic.store.helpers.Helper;
import com.yash.Electronic.store.service.UserServices;
import com.yash.Electronic.store.dtos.PageableResponse;
import com.yash.Electronic.store.dtos.UserDto;
import com.yash.Electronic.store.repository.RoleRepo;
import com.yash.Electronic.store.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceiimp implements UserServices {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String imagePath;

    @Autowired
    private RoleRepo roleRepo;
    Logger logger = LoggerFactory.getLogger(UserServiceiimp.class);

    @Override
    public UserDto create_User(UserDto userDto) {

        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        User user =  mapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = new Role();
        role.setRoleId(UUID.randomUUID().toString());
        role.setName("ROLE_USER");
        Role normalROle = roleRepo.findByName("ROLE_USER").orElse(role);
        user.setRoles(List.of(normalROle));
        User savedUser = userRepo.save(user);
        UserDto newDto = entitytoDto(savedUser);
        return newDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not fonud"));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setImageName(userDto.getImageName());
        user.setPassword(userDto.getPassword());

        //assign normal role to user

        //default
        User updatedUser = userRepo.save(user);
        UserDto  updatedDto = entitytoDto(updatedUser);
        return updatedDto;


    }

    @Override
    public void deleteUser(String userId) throws IOException {
        User user  = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Id not found"));
       String fullpath = imagePath+user.getImageName();
       Path path = Paths.get(fullpath);
       try{
           Files.delete(path);
       }
       catch (IOException ex){
           logger.info("User image not found in folder");
           ex.printStackTrace();
       }
       catch (Exception ex){
           ex.printStackTrace();
       }
        user.getRoles().clear();
        userRepo.save(user);
        userRepo.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getallUser(int pageNumber, int  PageSize, String sortBy, String sortDir){
       Sort sort =  (sortDir.equalsIgnoreCase("desc"))? (Sort.by(sortBy).descending()):(Sort.by(sortBy));
        Pageable pageable = PageRequest.of(pageNumber, PageSize, sort);
       Page<User> page  = userRepo.findAll(pageable);
       PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);

        return response;
    }

    @Override
    public UserDto getUser(String UserId) {
        User user = userRepo.findById(UserId).orElseThrow(() -> new ResourceNotFoundException("No such element found"));
        UserDto userDto =  entitytoDto(user);
        return userDto;
    }
    @Override
    public UserDto getUserbyEmail(String email) {
        User user  =userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserDto userDto = entitytoDto(user);
        return userDto;
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
       List<User> user = userRepo.findByNameContaining(keyword);
        List<UserDto> userDtos = user.stream().map(user1 -> entitytoDto(user1)).collect(Collectors.toList());
        return userDtos;
    }

    private User dtotoEntity(UserDto userDto){
//        User user = User.builder()
//                                .UserId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .imageName(userDto.getImageName())
//                .gender(userDto.getGender()).build();
        return mapper.map(userDto, User.class);
    }
    private UserDto entitytoDto(User savedUser){
//       UserDto userDto =  UserDto.builder()
//                .UserId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .gender(savedUser.getGender())
//                .about(savedUser.getAbout())
//                .password(savedUser.getPassword())
//                .email(savedUser.getEmail())
//                .imageName(savedUser.getImageName()).build();
        return mapper.map(savedUser, UserDto.class);
    }

    public List<UserDto> getAllAdmin() {
        List<User> userList = userRepo.findAllAdmins();

        // Correct mapping
        List<UserDto> userDtos = userList.stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        return userDtos;
    }

    public void  createAdmin(UserDto userDto){
        User user = mapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role normalRole = roleRepo.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new ResourceNotFoundException("ROLE_USER not found"));
        user.setRoles(List.of(normalRole));
        User savedUser = userRepo.save(user);
    }
}
