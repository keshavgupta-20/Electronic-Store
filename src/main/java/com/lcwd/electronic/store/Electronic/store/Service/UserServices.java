package com.lcwd.electronic.store.Electronic.store.Service;

import com.lcwd.electronic.store.Electronic.store.dtos.PegeableResponse;
import com.lcwd.electronic.store.Electronic.store.dtos.RegisterUser;
import com.lcwd.electronic.store.Electronic.store.dtos.UserDto;
import org.springframework.stereotype.Component;

//import java.lang.classfile.constantpool.PackageEntry;
import java.io.IOException;
import java.util.List;
@Component
public interface UserServices {
    // create
    UserDto create_User(RegisterUser registerUser);

    //update
    UserDto updateUser(UserDto userDto, String userId);
    //delete

    void deleteUser(String userId) throws IOException;
    //get all user

    PegeableResponse<UserDto> getallUser(int pageNumber, int PageSize, String sortBy, String sortDir);
    // get single user by id
    UserDto getUser(String UserId);
    // get single user by email
    UserDto getUserbyEmail(String  email);

    //search user
    List<UserDto> searchUser(String keyword);

    // other user specific feature

}
