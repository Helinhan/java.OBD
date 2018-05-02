package com.hantong.db;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    public Long id;

    @Column(name="name")
    public String name;

    @Column(name="password")
    public String password;

    @Column(name="role")
    public String role;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public void addRole(String role) {
        if (null == role || role.length() == 0) {
            return;
        }

        if (this.role == null) {
            this.role = role;
        } else {
            this.role += role;
        }

        this.role += ",";
    }

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%s, name='%s', password='%s']",
                id, name, password);
    }
}
