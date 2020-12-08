package com.mbrickspan.mbpan.controller;

import com.mbrickspan.mbpan.entity.Filedb;
import com.mbrickspan.mbpan.entity.Users;
import com.mbrickspan.mbpan.service.FileService;
import com.mbrickspan.mbpan.service.MailService;
import com.mbrickspan.mbpan.service.UsersService;
import com.mbrickspan.mbpan.util.MD5Util;
import com.sun.org.apache.xpath.internal.operations.Mod;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author: Guessed 魏嘉辰
 * @Date: 2020/10/15
 */
@Controller
@RequestMapping("/user/")
public class UsersController {

    @Autowired
    private UsersService usersServiceImpl;
    @Autowired
    private FileService fileServiceImpl;
    @Getter@Setter
    private List<Filedb> filedbList= new ArrayList<>();

   public static String cookie_uname="";

   private String forgotuname="";
    private String forgotemail="";
    private String checkCode="";

    //登陆
    @RequestMapping("login")
    public String login(String uname, String upwd, HttpServletResponse res) throws IOException {

//        Users users = usersServiceImpl.login(uname, upwd);
        Users users = usersServiceImpl.login(uname, MD5Util.md5(upwd));

        if (users == null) {
            System.out.println("User Login failed");
            return "index";
        } else {
            System.out.println("User Login successed");
            //设置并添加cookie
            Cookie usercookie=new Cookie("uname",uname);
            cookie_uname=uname;
            res.addCookie(usercookie);
            return "forward:/user/selectfile";
        }

    }

    //查找当前用户下的文件
    @RequestMapping("selectfile")
    public String selectfile(Model model){
        filedbList=fileServiceImpl.selectfilebyuname(UsersController.cookie_uname);
        model.addAttribute("filedbList",filedbList);
        return "home";
    }
    //模糊搜索文件
    @RequestMapping("selectfilefuzzily")
    public String selectfilefuzzily(String filename, Model model){
        System.out.println(filename);
        filedbList=fileServiceImpl.selectfilefuzzily(filename);
        model.addAttribute("filedbList",filedbList);
        return "home";
    }
    //查找回收站中的文件
    @RequestMapping("selectbin")
    public String selectbin(Model model){
        filedbList=fileServiceImpl.selectbinbyuname(UsersController.cookie_uname);
        model.addAttribute("filedbList",filedbList);
        return "bin";
    }

    //注册
    @RequestMapping(value = "register",method = RequestMethod.POST)
    public String register(Users users,HttpServletResponse response) throws IOException {
            //检查是否已经注册
            Users existuser=usersServiceImpl.selectByuname(users.getUname());
            if(existuser!=null){
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.print("<script>alert('该用户已存在!');</script>");
            }else{
                    //md5加密
                    users.setUpwd(MD5Util.md5(users.getUpwd()));
                    usersServiceImpl.insertSelective(users);

                    //创建用户文件夹
                    String path = "C:/mbpanupload/"+users.getUname()+"/";
                    File dirfile = new File(path);
                    if (!dirfile.exists()) {//目录不存在，创建目录
                        dirfile.mkdirs();
                    }

                    response.setContentType("text/html;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.print("<script>alert('注册成功，请登陆!');</script>");
            }

        return "index";
    }

    //删除文件
    @RequestMapping("del")
    public String del(int fid){
       fileServiceImpl.del(fid);
        return "forward:/user/selectfile";
    }

    //恢复文件
    @RequestMapping("recover")
    public String recover(int fid){
        fileServiceImpl.recover(fid);
        return "forward:/user/selectbin";
    }

    //查看用户资料
    @RequestMapping("userinfo")
    public String userinfo(Model model){
      Users user= usersServiceImpl.selectByuname(cookie_uname);
      model.addAttribute("user",user);
        return "userinfo";
    }

    //更改用户资料
    @RequestMapping("changeinfo")
    public String changeinfo(Users user) throws IOException {
        usersServiceImpl.updateByunameSelective(user);
        return "forward:/user/userinfo";
    }
    //忘记密码
    @RequestMapping("forgotpwd")
    public String forgotpwd(){
        return "forgotpwd";
    }
    //找回密码1
    @RequestMapping("submituname")
    public String submituname(String uname,HttpServletResponse response,Model model) throws IOException {
        Users user=usersServiceImpl.selectByuname(uname);
        if (user!=null){
            model.addAttribute("useremail",user.getUemail());
            forgotuname=user.getUname();
            forgotemail=user.getUemail();
            return "checkcode";
        }else{
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.print("<script>alert('该用户不存在!');</script>");
        }
        return "forgotpwd";
    }

    //找回密码2
    //获取验证码
    @RequestMapping("getCheckcode")
    public String getCheckcode(){
        checkCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String message = "您的验证码为："+checkCode;
        try {
            MailService mailService=new MailService();
            mailService.sendSimpleMail(forgotemail, "搬砖云盘验证码", message);
        }catch (Exception e){
            System.out.println(e);
        }
        return "checkcode";
    }

    //找回密码3-核对验证码
    @RequestMapping("checkingcode")
    public String changepwd(String code,HttpServletResponse response) throws IOException {
        if(code.equals(checkCode)){
            return "changepwd";
        }else{
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.print("<script>alert('验证码错误!');</script>");
        }
        return "checkcode";
    }

    //找回密码4-修改密码
    @RequestMapping("changingpwd")
    public String changingpwd(String upwd,HttpServletResponse response) throws IOException {
        usersServiceImpl.changingpwd(forgotuname,MD5Util.md5(upwd));
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print("<script>alert('修改密码成功!');</script>");
        return "index";
    }

}
