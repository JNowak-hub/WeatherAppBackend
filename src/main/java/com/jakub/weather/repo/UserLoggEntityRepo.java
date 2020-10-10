package com.jakub.weather.repo;

import com.jakub.weather.model.user.UserLoggEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoggEntityRepo extends JpaRepository<UserLoggEntity, Long> {

}
