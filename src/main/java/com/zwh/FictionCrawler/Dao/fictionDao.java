package com.zwh.FictionCrawler.Dao;

import com.zwh.FictionCrawler.Pojo.Fiction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface fictionDao  extends JpaRepository<Fiction, Integer>, JpaSpecificationExecutor {

}
