package model.dto;

import java.io.Serializable;

public class UserDTO implements Serializable
{
    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private Role role;

    // Constructors
    public UserDTO(){}

    public UserDTO(int id, String username, String email, String passwordHash, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // Getters and Setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getPasswordHash() {return passwordHash;}
    public void setPasswordHash(String passwordHash) {this.passwordHash = passwordHash;}
    public Role getRole() {return role;}
    public void setRole(Role role) {this.role = role;}


    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UserDTO userDTO = (UserDTO) o;
        return id == userDTO.id &&
                username.equals(userDTO.username) &&
                email.equals(userDTO.email) &&
                passwordHash.equals(userDTO.passwordHash) &&
                role ==  userDTO.role;
    }

    @Override
    public String toString()
    {
        return "User { id: " + id
                + "username: " + username +
                " email: " + email +
                " passwordHash: " + passwordHash +
                " role: " + role + " }";
    }


    public enum Role {
        Customer,
        Admin
    }
}
