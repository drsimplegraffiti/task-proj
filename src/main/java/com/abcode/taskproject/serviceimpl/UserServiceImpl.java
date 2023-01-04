package com.abcode.taskproject.serviceimpl;

import com.abcode.taskproject.entity.Users;
import com.abcode.taskproject.exception.APIException;
import com.abcode.taskproject.payload.UserDto;
import com.abcode.taskproject.repository.UserRepository;
import com.abcode.taskproject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // import the password encoder
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        // check if the user already exists
        if (userRepository.existsByEmail(userDto.getEmail())) {
            log.error("User already exists" + " " + userDto.getEmail());
            throw new APIException("User already exists");
        }


        // encode the password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // because we cannot parse in the userDto to the repository
        // we need to perform a type conversion of the userDto to the Users entity
        Users user = userDtoToEntity(userDto); // here we are converting the userDto to the Users entity

        Users savedUser  = userRepository.save(user);
        return entityToUserDto(savedUser); // here we are converting the Users entity to the userDto and returning it to the controller layer
    }



    // convert the userDto to the Users entity in order to save it in the database
    private Users userDtoToEntity(UserDto userDto){
        Users users = new Users();
        users.setName(userDto.getName());
        users.setEmail(userDto.getEmail());
        users.setPassword(userDto.getPassword());
        return users;
    }

    // convert the Users entity to the userDto in order to return it to the user
    private UserDto entityToUserDto(Users savedUser){
        UserDto userDto = new UserDto();
        userDto.setId(savedUser.getId());
        userDto.setName(savedUser.getName());
        userDto.setEmail(savedUser.getEmail());
        userDto.setPassword(savedUser.getPassword());
        return userDto;
    }
}
