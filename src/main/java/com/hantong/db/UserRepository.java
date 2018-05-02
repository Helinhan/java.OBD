package com.hantong.db;

import org.springframework.data.jpa.repository.JpaRepository;

//public interface UserRepository extends MongoRepository<User, String> {
public interface UserRepository extends JpaRepository<User, String> {
    //public User findByFirstName(String name);
    public User findByName(String name);
}
