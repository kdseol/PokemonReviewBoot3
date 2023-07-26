package com.pokemonreview.api.controllers;

import com.pokemonreview.api.dto.UserDto;
import com.pokemonreview.api.dto.PageResponseDto;
import com.pokemonreview.api.exceptions.ResourceNotFoundException;
import com.pokemonreview.api.models.UserEntity;
import com.pokemonreview.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public PageResponseDto getAllUsers(
						@RequestParam(value = "pageNo", defaultValue = "0", required = false) 
            int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "2", required = false) 
            int pageSize) {
        System.out.println(">>> pageNo = " + pageNo);
        System.out.println("<<<< pageSize = " + pageSize);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id")
                .descending());
        Page<UserEntity> userEntityPage = userRepository.findAll(pageable);
        List<UserEntity> listOfUser = userEntityPage.getContent();
        List<UserDto> userDtoList =
                listOfUser.stream()
                    .map(entity -> UserDto.builder()
                    .id(entity.getId())
                    .username(entity.getUsername())
                    .firstName(entity.getFirstName())
                    .lastName(entity.getLastName())
                    .role(entity.getRoles().get(0).getName())
                    .build())
                    .collect(Collectors.toList());

        PageResponseDto userResponse = new PageResponseDto();
        userResponse.setContent(userDtoList);
        userResponse.setPageNo(userEntityPage.getNumber());
        userResponse.setPageSize(userEntityPage.getSize());
        userResponse.setTotalElements(userEntityPage.getTotalElements());
        userResponse.setTotalPages(userEntityPage.getTotalPages());
        userResponse.setLast(userEntityPage.isLast());

        return userResponse;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public UserDto getUser(@PathVariable("id") int userId) {
        UserEntity existUser = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserDto existUserDto = mapToDto(existUser);
        return existUserDto;
    }

    private UserDto mapToDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setUsername(userEntity.getUsername());
				userDto.setFirstName(userEntity.getFirstName());
        userDto.setLastName(userEntity.getLastName());
        userDto.setPassword(userEntity.getPassword());
        userDto.setRole(userEntity.getRoles().get(0).getName());
        return userDto;
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable("id") int userId,
            @RequestBody UserDto userDto) {
        UserEntity existUser = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        existUser.setFirstName(userDto.getFirstName());
        existUser.setLastName(userDto.getLastName());
        existUser.setPassword(passwordEncoder.encode((userDto.getPassword())));

        UserEntity updatedUser = userRepository.save(existUser);
        UserDto existUserDto = mapToDto(updatedUser);
        return ResponseEntity.ok(existUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int userId) {
        UserEntity userEntity = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(userEntity);
        return new ResponseEntity<>("User delete", HttpStatus.OK);
    }
}