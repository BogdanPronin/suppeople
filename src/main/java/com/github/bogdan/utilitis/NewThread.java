package com.github.bogdan.utilitis;

import com.github.bogdan.databaseConfiguration.DatabaseConfiguration;
import com.github.bogdan.model.Post;
import com.github.bogdan.model.Status;
import com.github.bogdan.model.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static com.github.bogdan.service.CtxService.updated;
import static com.github.bogdan.service.PostApplicationService.getPostApplications;
import static com.github.bogdan.service.PostService.addPostQt;

public class NewThread extends Thread{
    Dao<User,Integer> userDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,User.class);
    Dao<Post,Integer> postDao = DaoManager.createDao(DatabaseConfiguration.connectionSource,Post.class);

    public NewThread() throws SQLException {
    }
    Logger logger = LoggerFactory.getLogger(NewThread.class);

    public void run(){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String currentTime = LocalDate.now().format(formatter);
            while (true) {
                for (Post p : postDao.queryForAll()) {
                    LocalDate l = LocalDate.parse(p.getDateOfCreate(), formatter);
                    if (p.getStatus() == Status.PROCESSING) {

                        if (ChronoUnit.DAYS.between(l, LocalDate.now()) >= 15 && !getPostApplications(p.getId()).isEmpty()) {
                            p.setStatus(Status.COMPLETED);
                            addPostQt(p);
                        }
                    }
                    postDao.update(p);

                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
