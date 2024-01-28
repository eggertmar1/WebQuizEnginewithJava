package engine.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

public class RegisterInput {
    @Pattern(regexp = ".+@.+\\..+", message = "Invalid email format")
    private String email;

    @Length(min = 5, message = "Password must be at least 5 characters long")
    private String password;

    public RegisterInput() {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
