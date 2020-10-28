package com.jakub.weather.repo;

import com.jakub.weather.model.user.UserSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSettingsEntiyRepo extends JpaRepository<UserSettingsEntity, Long> {

//    @Query(value = "DELETE FROM user_settings_entity AS us JOIN user_entity AS ue ON ue.settings_id = us.id where ue.id = ?1 ", nativeQuery = true)
    @Query(value = "DELETE FROM UserSettingsEntity us WHERE us.user.id = :userId")
    void deleteUserSettings(Long userId);

//    @Query(value = "SELECT us.* FROM user_settings_entity AS us JOIN user_entity AS ue ON ue.settings_id = us.id where ue.id = ?1", nativeQuery = true)
    @Query(value = "SELECT us FROM UserSettingsEntity us WHERE us.user.id = :id")
    Optional<UserSettingsEntity> getUserSettingsByUserId(Long id);
}
