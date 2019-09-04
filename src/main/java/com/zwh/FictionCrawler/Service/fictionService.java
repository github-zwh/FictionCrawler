package com.zwh.FictionCrawler.Service;

import com.zwh.FictionCrawler.Pojo.Fiction;

import java.util.List;

public interface fictionService {
    /**保存小说
     * @param fiction
     */
    public void save(Fiction fiction);
    /**查询小说
     * @param fiction
     * @return
     */
    public List<Fiction> findAll(Fiction fiction);
}
