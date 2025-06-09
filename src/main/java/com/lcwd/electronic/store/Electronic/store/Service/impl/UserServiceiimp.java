package com.lcwd.electronic.store.Electronic.store.Service.impl;

import com.lcwd.electronic.store.Electronic.store.Entites.User;
import com.lcwd.electronic.store.Electronic.store.Exception.ResourceNotFoundException;
import com.lcwd.electronic.store.Electronic.store.Helpers.helper;
import com.lcwd.electronic.store.Electronic.store.Service.UserServices;
import com.lcwd.electronic.store.Electronic.store.dtos.PegeableResponse;
import com.lcwd.electronic.store.Electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.Electronic.store.repositoreis.UserRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceiimp implements UserServices {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String imagePath;

    Logger logger = LoggerFactory.getLogger(UserServiceiimp.class);

    @Override
    public UserDto create_User(UserDto userdto) {

        String userId = UUID.randomUUID().toString();
        userdto.setUserId(userId);
        User user =  dtotoEntity(userdto);
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
        userRepo.delete(user);
    }

    @Override
    public PegeableResponse<UserDto> getallUser(int pageNumber, int  PageSize, String sortBy, String sortDir){
       Sort sort =  (sortDir.equalsIgnoreCase("desc"))? (Sort.by(sortBy).descending()):(Sort.by(sortBy));
        Pageable pageable = PageRequest.of(pageNumber, PageSize, sort);
       Page<User> page  = userRepo.findAll(pageable);
       PegeableResponse <UserDto> response = helper.getPageableResponse(page, UserDto.class);

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
}
