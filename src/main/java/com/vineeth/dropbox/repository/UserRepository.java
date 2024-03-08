package com.vineeth.dropbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vineeth.dropbox.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

