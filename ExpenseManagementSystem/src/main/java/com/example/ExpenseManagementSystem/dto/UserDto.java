package com.example.ExpenseManagementSystem.dto;

import com.example.ExpenseManagementSystem.entity.User;
import com.example.ExpenseManagementSystem.enums.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserDto extends RepresentationModel<UserDto> {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;

    public static UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}