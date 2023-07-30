package com.itshanhe.qiyunblogapi.service.impl;

import com.itshanhe.qiyunblogapi.entity.BlogArticle;
import com.itshanhe.qiyunblogapi.mapper.BlogArticleMapper;
import com.itshanhe.qiyunblogapi.service.BlogArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogArticleServiceImpl implements BlogArticleService {

    @Autowired
    private BlogArticleMapper blogArticleMapper;

    @Override
    public int insertArticle(BlogArticle blogArticle) {
        return blogArticleMapper.insertArticle(blogArticle);
    }

    @Override
    public int updateArticleLikeNum(Integer id, Integer likeNum) {
        return blogArticleMapper.updateArticleLikeNum(id,likeNum);
    }


    @Override
    public int updateArticleIntroduction(Integer id, String introduction) {
        return blogArticleMapper.updateArticleIntroduction(id,introduction);
    }
}
