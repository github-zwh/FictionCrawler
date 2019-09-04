package com.zwh.FictionCrawler.Service;

import com.zwh.FictionCrawler.Pojo.Chapter;

import java.util.List;

public interface chapterService {
    /**保存章节
     * @param chapter
     */
    public void save(Chapter  chapter);
    /**查询章节
     * @param chapter
     * @return
     */
    public List<Chapter> findAll(Chapter chapter);
}
