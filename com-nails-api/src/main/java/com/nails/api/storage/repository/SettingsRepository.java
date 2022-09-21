package com.nails.api.storage.repository;

import com.nails.api.storage.model.Settings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SettingsRepository extends JpaRepository<Settings,Long>, JpaSpecificationExecutor<Settings>{
    public Settings findSettingsByKey(String key);
}
