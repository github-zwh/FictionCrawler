package com.zwh.FictionCrawler.Service.Impl;

import com.zwh.FictionCrawler.Dao.chapterDao;
import com.zwh.FictionCrawler.Pojo.Chapter;
import com.zwh.FictionCrawler.Service.chapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ChapterServiceImpl implements chapterService {
    @Autowired
    private chapterDao chapterDao;
    @Override
    @Transactional
    public void save(Chapter chapter) {
        this.chapterDao.save(chapter);
    }
    @Override
    public List<Chapter> findAll(Chapter chapter) {
        Example<Chapter> example = Example.of(chapter);
        List<Chapter> all = this.chapterDao.findAll(example);
        return all;
    }
}
