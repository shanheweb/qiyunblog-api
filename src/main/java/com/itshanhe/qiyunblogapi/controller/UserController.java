package com.itshanhe.qiyunblogapi.controller;

import com.itshanhe.qiyunblogapi.entity.BlogUser;
import com.itshanhe.qiyunblogapi.entity.Result;
import com.itshanhe.qiyunblogapi.param.BlogLoginParam;
import com.itshanhe.qiyunblogapi.param.BlogRegisterParam;
import com.itshanhe.qiyunblogapi.service.BlogUserService;
import com.itshanhe.qiyunblogapi.service.MailService;
import com.itshanhe.qiyunblogapi.util.DomainUtil;
import com.itshanhe.qiyunblogapi.util.MD5Util;
import com.itshanhe.qiyunblogapi.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户管理
 */
@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {
    
    @Autowired
    private BlogUserService blogUserService;
    @Autowired
    private MailService mailService;
    @Autowired
    private DomainUtil domainUtil;
//    默认报错为null
    private String paramError = null;
    
    /**
     * 用户注册
     * @param blogRegisterParam 注册信息
     * @param result 数据校验错误信息（自动）
     * @return 注册结果
     */
    @PostMapping("register")
    public Result register(@RequestBody @Valid BlogRegisterParam blogRegisterParam, BindingResult result) {
//        if (blogRegisterParam == null) {
//            return Result.success("请提交参数");
//        }
//            log.info("名字{}",blogRegisterParam);
//        数据校验错误信息
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                this.paramError = (error.getCode()+ "-" + error.getDefaultMessage());
            }
        }
        if (this.paramError != null) {
            return Result.error(this.paramError);
        }
        

//            获取返回值
        int resultUser = blogUserService.userRegister(blogRegisterParam.getUserUsername(), MD5Util.Md5Code(blogRegisterParam.getUserPassword()), blogRegisterParam.getUserNickName(),blogRegisterParam.getUserEmail());
        if (resultUser == -1) {
            return Result.success("用户名已经被注册了");
        } else if (resultUser == 0) {
            return Result.success("未知错误，请联系系统管理员");
        }
        
//        获取ID
        String getUserId = blogUserService.userGetId(blogRegisterParam.getUserUsername());
//        MD5加密ID
        String md5Id = MD5Util.Md5Code(getUserId);
        
        //        发送邮箱验证码
//        需要发送的QQ邮箱
        String to = blogRegisterParam.getUserEmail();
//        发送主题
        String subject = "七云博客邮箱验证";
//        发送的HTML内容 并且设置过期时间为10分钟
        String text = "<h1>七云博客邮箱验证</h1><p>欢迎注册七云博客,请点击以下链接进行注册.</p><p>"+domainUtil.getDomain()+"user/emailVerify/"+ TimeUtil.getSetCurrentTime(10) +"/name="+blogRegisterParam.getUserUsername()+"&md5="+md5Id+"</p>";
        log.info("邮件发送前{},tex:{}",to,text);
        if (mailService.sendHtmlMail(to,subject,text) == -1) {
            log.info("邮箱有问题");
//            删除用户
            blogUserService.userDeleteName(blogRegisterParam.getUserUsername());
            return Result.error("邮箱发送失败,请重新注册填写注册");
        }
        
        
        return Result.success("注册成功！请在邮箱里点击验证才能完成注册.");
    }
    
    @PostMapping("")
    public Result login(@RequestBody @Valid BlogLoginParam blogLoginParam, BindingResult result) {
        //        数据校验错误信息
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                this.paramError = (error.getCode()+ "-" + error.getDefaultMessage());
            }
        }
        if (this.paramError != null) {
            return Result.error(this.paramError);
        }
        return Result.success();
    }
    
    @GetMapping("/emailVerify/{longTime}/name={name}&md5={md5}")
    public Result emailVerify(@PathVariable Long longTime,@PathVariable String name,@PathVariable String md5) {
//        获取现在时间然后判断过期时间,是否过期了
        if (TimeUtil.getNowCurrentTime()>longTime) {
            return Result.error("该链接已经过期了");
        }
//        先给空值防止报错
        String id = null;
        id = blogUserService.userGetId(name);
//        验证一下是否注册了
        if (id == null) {
            return Result.error("你没进行注册");
        }
//        MD5加密ID
        String md5id = MD5Util.Md5Code(id);
//        校验MD5 ID
        if (!md5.equals(md5id)) {
            return Result.error("MD5校验码错误，非法用户");
        }
//        查询是否是锁定状态 如果已经没锁定就返回注册完成
        if (blogUserService.userLocked(id) == 0) {
            return Result.error("你已经注册完了,请不要再次注册!");
        }
//        更改锁定状态 更改为解锁
        blogUserService.setLocked(id,0);
        return Result.success("注册成功!");
    }
}
