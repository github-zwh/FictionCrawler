package com.zwh.FictionCrawler.Service.Impl;

import com.zwh.FictionCrawler.Pojo.Fiction;
import com.zwh.FictionCrawler.Service.fictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FictionServiceImpl implements fictionService {
    @Autowired
    private com.zwh.FictionCrawler.Dao.fictionDao fictionDao;

    @Override
    @Transactional
    public void save(Fiction fic) {
        this.fictionDao.save(fic);
    }
    @Override
    public List<Fiction> findAll(Fiction fic) {
        //申明查询条件
        Example<Fiction> example = Example.of(fic);
        //根据查询条件进行查询
        List<Fiction> all = this.fictionDao.findAll(example);
        return all;
    }
}
