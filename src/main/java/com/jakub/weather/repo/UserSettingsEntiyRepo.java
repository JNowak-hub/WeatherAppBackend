package com.jakub.weather.repo;

import com.jakub.weather.model.weather.user.UserSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSettingsEntiyRepo extends JpaRepository<UserSettingsEntity, Long> {
}
