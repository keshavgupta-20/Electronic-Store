package com.yash.Electronic.store.controller;

import com.yash.Electronic.store.entites.User;
import com.yash.Electronic.store.service.FileService;
import com.yash.Electronic.store.service.UserServices;
import com.yash.Electronic.store.dtos.ImageResponse;
import com.yash.Electronic.store.dtos.PageableResponse;
import com.yash.Electronic.store.dtos.UserDto;
import com.yash.Electronic.store.repository.UserRepo;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/electrohub/user")
public class UserController {
    @Autowired
    private UserServices userServices;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    //create

    @PostMapping("/do-register")
    public String createUser(@Valid @ModelAttribute("registerUser")UserDto userDto, BindingResult bindingResult,
                             @RequestParam("repassword") String repassword,Model model){

        if (bindingResult.hasErrors()){
            return "register";
        }
        if (!userDto.getPassword().equals(repassword)) {
            model.addAttribute("passwordMismatch", "Passwords do not match");
            return "register";
        }
       User user =  userRepo.findByEmail(userDto.getEmail()).orElse(null);
        if (user != null){
            model.addAttribute("email", "Email already exist");
            return "register";
        }
        userServices.create_User(userDto);
        return "redirect:/electrohub/";
    }


    //getalluser
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> Alluser(@RequestParam(value = "pageNumber", defaultValue =  "1", required = false)int pageNumber,
                                                             @RequestParam(value = "PageSize", defaultValue = "4", required = false)int PageSize,
                                                             @RequestParam(value = "sortBy", defaultValue = "name", required = false)String sortBy,
                                                             @RequestParam(value = "sortDir", defaultValue = "asc", required = false)String sortDir)

    {
        PageableResponse<UserDto> userDtos = userServices.getallUser(pageNumber-1, PageSize,sortBy,sortDir);
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    //upload user image
    @PostMapping("/image/{userId}")
    public String uploadUserFile(
        @RequestParam("userImage") MultipartFile image, @PathVariable String userId) throws IOException {
        String imageName = fileService.uploadFile(image, imageUploadPath);
        UserDto user = userServices.getUser(userId);
        user.setImageName(imageName);
        UserDto userDto = userServices.updateUser(user, userId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).message("Image is inserted properly").success(true).httpStatus(HttpStatus.CREATED).build();
        return "redirect:/electrohub/";

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
