package com.lcwd.electronic.store.Electronic.store.Controller;

import com.lcwd.electronic.store.Electronic.store.Service.FileService;
import com.lcwd.electronic.store.Electronic.store.Service.UserServices;
import com.lcwd.electronic.store.Electronic.store.dtos.ApiResposeClass;
import com.lcwd.electronic.store.Electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.Electronic.store.dtos.PegeableResponse;
import com.lcwd.electronic.store.Electronic.store.dtos.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServices userServices;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    //create

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid  @RequestBody UserDto userDto){
        UserDto userDto1 = userServices.create_User(userDto);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{userId}")
    public  ResponseEntity<UserDto> updateUser(@Valid @PathVariable("userId") String userId, @RequestBody UserDto userDto){
        UserDto userDto1 = userServices.updateUser(userDto, userId);
        return new ResponseEntity<>(userDto1, HttpStatus.OK);

    }
    //delete
    @DeleteMapping("/{userId}")
    public  ResponseEntity<String> deleteUser(@PathVariable("userId") String userId) throws IOException {
          userServices.deleteUser(userId);
         return new ResponseEntity<>("Deleted Sucessfully", HttpStatus.OK);
    }
    //getuserbyid
    @GetMapping("/{userId}")
    public  ResponseEntity<UserDto> UserById(@PathVariable("userId") String userId){
        UserDto userDto = userServices.getUser(userId);
//        ApiResposeClass message = ApiResposeClass.builder().message("Succesffuly deleted").success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
    //getuserbyemail
    @GetMapping("/emailId/{email}")
    public  ResponseEntity<UserDto> UserByEmailId(@PathVariable("email") String email){
        UserDto userDto = userServices.getUserbyEmail(email);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
    //getalluser
    @GetMapping
    public ResponseEntity<PegeableResponse<UserDto>> Alluser(@RequestParam(value = "pageNumber", defaultValue =  "1", required = false)int pageNumber,
                                                             @RequestParam(value = "PageSize", defaultValue = "4", required = false)int PageSize,
                                                             @RequestParam(value = "sortBy", defaultValue = "name", required = false)String sortBy,
                                                             @RequestParam(value = "sortDir", defaultValue = "asc", required = false)String sortDir)

    {
        PegeableResponse<UserDto> userDtos = userServices.getallUser(pageNumber-1, PageSize,sortBy,sortDir);
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }
    //get user by keyword
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords){
        return new ResponseEntity<>(userServices.searchUser(keywords), HttpStatus.OK);
    }
    //upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserFile(
        @RequestParam("userImage") MultipartFile image, @PathVariable String userId) throws IOException {
        String imageName = fileService.uploadFile(image, imageUploadPath);
        UserDto user = userServices.getUser(userId);
        user.setImageName(imageName);
        UserDto userDto = userServices.updateUser(user, userId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).message("Image is inserted properly").success(true).httpStatus(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);

    }
    //serve user image
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto userDto =userServices.getUser(userId);
        logger.info(userDto.getImageName());
        InputStream inputStream = fileService.getResource(imageUploadPath, userDto.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());
    }
}
