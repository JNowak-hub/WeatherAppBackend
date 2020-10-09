package com.jakub.weather.repo;

import com.jakub.weather.model.weather.user.UserApiCallHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserApiCallHistoryRepo extends JpaRepository<UserApiCallHistoryEntity, Long> {
}
