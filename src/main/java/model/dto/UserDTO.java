package model.dto;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

public class UserDTO implements Serializable
{
    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String phone;
    private Role role;

    public UserDTO(){}

    public UserDTO(String name, String surname, LocalDate birthDate, String username, String email, String passwordHash, String phone, Role role) {
        this.id = 0;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.phone = phone;
        this.role = role;
        System.out.println(this.toString());
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getPasswordHash() {return passwordHash;}
    public void setPasswordHash(String passwordHash) {this.passwordHash = passwordHash;}
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public String getSurname(){return surname;}
    public void setSurname(String surname){this.surname = surname;}
    public LocalDate getBirthDate(){return birthDate;}
    public void setBirthDate(LocalDate birthDate){this.birthDate = birthDate;}
    public String getPhone(){return phone;}
    public void setPhone(String phone){this.phone = phone;}
    public Role getRole(){return role;}
    public void setRole(Role role){this.role = role;}


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof UserDTO userDTO)) return false;
        return id == userDTO.id &&
                username.equals(userDTO.username) &&
                email.equals(userDTO.email) &&
                passwordHash.equals(userDTO.passwordHash) &&
                name.equals(userDTO.name) &&
                surname.equals(userDTO.surname) &&
                birthDate.equals(userDTO.birthDate) &&
                Objects.equals(phone, userDTO.phone) &&
                role ==  userDTO.role;
    }

    @Override
    public String toString() {
        return "User { id: " + id +
                ", username: " + username +
                ", email: " + email +
                ", passwordHash: " + passwordHash +
                ", name: " + name +
                ", surname: " + surname +
                ", birthDate: " + birthDate +
                ", phone: " + phone +
                ", role: " + role + " }";
    }

    public enum Role {
        Customer,
        Admin
    }
}
