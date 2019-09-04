package com.zwh.FictionCrawler.Dao;

import com.zwh.FictionCrawler.Pojo.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.LinkedList;

public interface chapterDao extends JpaRepository<Chapter, Integer>, JpaSpecificationExecutor {

}
