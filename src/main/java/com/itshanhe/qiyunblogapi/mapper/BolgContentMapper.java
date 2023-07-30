package com.itshanhe.qiyunblogapi.mapper;

import com.itshanhe.qiyunblogapi.entity.BlogArticleContent;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

public interface BolgContentMapper {
    /**
     * 添加文章
     * @param blogArticleContent 文章属性
     * @return 1 成功 0 失败
     */

    int insertBlog(BlogArticleContent blogArticleContent);

    /**
     * 删除文章
     * @param id 文章id
     * @return 1 成功 0 失败
     */
    int deleteBlog(Integer id);

    /**
     * 修改文章属性
     * @param blogArticleContent 文章属性
     * @return 1 成功 0 失败
     */
    int updateBlog(BlogArticleContent blogArticleContent);

    /**
     * 根据文章id查询文章属性
     * @param id 文章id
     * @return 文章属性
     */
    BlogArticleContent selectBlogArticleByArticleBlogId(Integer id);

    /**
     * 根据用户id查询所有文章
     * @param id 用户id
     * @return 文章集合
     */
    List<BlogArticleContent> selectAllBlogArticleByUserId(Integer id);

    /**
     * 隐藏文章
     * @param id 文章id
     * @return 1 成功 0 失败
     */
    int HideArticle(Integer id);



}
