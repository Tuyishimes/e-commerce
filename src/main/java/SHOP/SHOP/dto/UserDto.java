package SHOP.SHOP.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String role;

    public UserDto(String name, String email, String role) {
    }
}
