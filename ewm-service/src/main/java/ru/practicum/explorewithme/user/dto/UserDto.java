package ru.practicum.explorewithme.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
@JsonInclude(Include.NON_NULL)
public class UserDto {

  private Long id;
  @NotBlank
  private String name;
  @NotBlank
  @Email
  private String email;
}
