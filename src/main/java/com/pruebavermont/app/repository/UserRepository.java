package com.pruebavermont.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pruebavermont.app.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
