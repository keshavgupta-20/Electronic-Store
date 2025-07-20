package com.yash.Electronic.store.service;

import com.yash.Electronic.store.dtos.PageableResponse;
import com.yash.Electronic.store.dtos.UserDto;
import org.springframework.stereotype.Component;

//import java.lang.classfile.constantpool.PackageEntry;
import java.io.IOException;
import java.util.List;
@Component
public interface UserServices {
    // create
    UserDto create_User(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto, String userId);
    //delete

    void deleteUser(String userId) throws IOException;
    //get all user

    PageableResponse<UserDto> getallUser(int pageNumber, int PageSize, String sortBy, String sortDir);
    // get single user by id
    UserDto getUser(String UserId);
    // get single user by email
    UserDto getUserbyEmail(String  email);

    //search user
    List<UserDto> searchUser(String keyword);
    List<UserDto> getAllAdmin();
    void  createAdmin(UserDto userDto);

    // other user specific feature

}
