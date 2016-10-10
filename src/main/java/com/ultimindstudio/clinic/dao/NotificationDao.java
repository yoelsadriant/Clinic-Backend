package com.ultimindstudio.clinic.dao;

import com.ultimindstudio.clinic.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by Yoel on 10/6/2016.
 */
public interface NotificationDao extends JpaRepository<Notification, Long> {

    List<Notification> findByDate(String datetime);
}
