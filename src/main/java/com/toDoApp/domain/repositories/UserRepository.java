package com.toDoApp.domain.repositories;


import com.toDoApp.domain.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {

}
