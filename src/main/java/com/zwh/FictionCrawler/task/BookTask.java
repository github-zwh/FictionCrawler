package com.zwh.FictionCrawler.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zwh.FictionCrawler.Service.Impl.ChapterServiceImpl;
import com.zwh.FictionCrawler.Pojo.Chapter;
import com.zwh.FictionCrawler.Pojo.Fiction;
import com.zwh.FictionCrawler.Service.Impl.FictionServiceImpl;
import com.zwh.FictionCrawler.util.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;

@Component
public class BookTask {
    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private FictionServiceImpl fictionServiceImpl;
    @Autowired
    private ChapterServiceImpl chapterServiceImpl;
    @Scheduled(cron ="0 0 2 * * ?") //凌晨2点自动更新
    public  void bookTask() throws  Exception{
        //要解析的url的初始地址
        String url ="https://www.biqukan.cc/fenlei";
        //按照分类对的搜索结果进行遍历
        for (int i =1; i <8 ; i++) {
            for (int j = 1; ; j++){
                String Target  = url+i+"/"+j+".html";
                String html = httpUtils.doGetHtml(Target);
                //获取document
                Document doc= Jsoup.parse(html);
                Elements Eles  = doc.select("div.panel-body").select("div.row");
                if (Eles.size()==1)
                {
                    break;
                }else{
                    savefic(Eles,i);
                }
            }
        }
        System.out.println("更新成功！");
    }
    /**
     * 小说简介保存
     * @param Eles  页面数据
     * @param type  小说类型
     * @throws Exception
     */
    private void savefic(Elements Eles,int type) throws  Exception{
        //遍历页面的每个小说
        for (int k = 1; k<Eles.size() ;k++) {
            Element element = Eles.get(k);
            String href = element.select("div.col-md-5 > a").attr("href");
            String name = element.select("div.caption > h4").text();
            String authorName = element.select("div.caption > small").text();
            String introduce = element.select("div.caption > p").text();

            Fiction fiction = new Fiction();
            fiction.setUrl(href);
            //查询是否已经存在
            List<Fiction> all = fictionServiceImpl.findAll(fiction);
            if (all.size()>0&&k!=Eles.size()){
                continue;
            }else if(all.size()>0){
                break;
            }
            fiction.setName(name);
            fiction.setIntroduce(introduce);
            fiction.setType(type);
            fiction.setCreated(new Date());
            fiction.setAuthor(authorName);
            fictionServiceImpl.save(fiction);
            Elements els = getEls(href);
            savechapter(els,fiction.getId(),href);//保存章节
            System.out.println("--------------------->"+k);
        }
    }
    /**
     * 保存所有章节
     * @param Eles
     * @param ficId
     * @param prehref
     * @throws Exception
     */
    private  void  savechapter(Elements Eles,int ficId,String prehref) throws  Exception{
        for (int i = Eles.size()-1; i>=0;i--){
            String url = Eles.get(i).select("a").attr("href");
            String title = Eles.get(i).select("a").attr("title");

            Chapter chapter = new Chapter();
            chapter.setChapter_name(title);
            List<Chapter> all = chapterServiceImpl.findAll(chapter);
            if (all.size()>0)
            {
                break;
            }
            chapter.setCreated(new Date());
            chapter.setFic_id(ficId);
            String target = prehref+url;//下一页
            String text = getText(target);
            chapter.setChapter_text(text);
            chapterServiceImpl.save(chapter);
        }
    }

    /**
     * 获取所有章节的列表
     * @param Target
     * @return
     * @throws Exception
     */
    private  Elements  getEls(String Target) throws  Exception{
        String html = httpUtils.doGetHtml(Target);
        Document doc= Jsoup.parse(html);
        Elements Eles  = doc.select("div#list-chapterAll").select("dl").select("dd");
        return Eles;
    }

    /**
     * 获取一个章节的内容
     * @param Target
     * @return
     * @throws Exception
     */
    private  String  getText(String Target) throws  Exception{
        String Text=" ";
        String target = Target;
        String target1=Target;
        for (int i = 1; i < 10; i++) {
            String temp=getSubText(target1);
            if(temp.endsWith("本章未完，点击下一页继续阅读")){
                String next = target.substring(0, target.lastIndexOf("."));
                int page = i+1;
                target1=next+"_"+page+".html";
                Text=Text+textHandle(temp);
            }else{
                Text=Text+textHandle(temp);
                break;
            }
        }
        return Text;
    }
    /**
     * 获取章节一个分页中的内容
     * @param Target
     * @return
     */
    private String getSubText(String Target){
        String html = httpUtils.doGetHtml(Target);
        Document doc= Jsoup.parse(html);
        String text  = doc.select("div.panel-body").text();
        return text;
    }

    /**
     * 对章节文字进行处理
     * @param text
     * @return
     */
    private String textHandle(String text){
        String substring;
        if (text.isEmpty()){
            return "本章暂未收录！";
        }else{
           if (text.startsWith("笔趣阁")){
               if(text.endsWith("本章未完，点击下一页继续阅读"))
               {
                   substring = text.substring(text.lastIndexOf("最新章节！"), text.length()-14);
               }else{
                   substring = text.substring(text.lastIndexOf("最新章节！"));
               }
               String substring1 = substring.substring(5);
               return substring1 ;
           }else {
               return "本章暂未收录！";
           }
        }
    }
}

