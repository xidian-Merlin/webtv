package com.lin.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageHelper;
import com.lin.dao.UserDao;
import com.lin.domain.AnchorModel;
import com.lin.domain.UserExample;
import com.sun.tools.internal.ws.processor.model.Response;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lin.domain.User;
import com.lin.realm.ShiroDbRealm;
import com.lin.service.UserService;
import com.lin.utils.CipherUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;

@Controller
public class IconController {

    @Autowired
    private UserService userService;



    @RequestMapping("/setIcon.do")
    public @ResponseBody Map<String, Object> setIcon(MultipartFile file,HttpServletRequest request)throws IOException{
        System.out.print("设置头像");

        String path = request.getSession().getServletContext().getRealPath("/icon");
        String fileName = file.getOriginalFilename();
        File dir = new File(path,fileName);
        if(!dir.exists()){

            dir.mkdir();
        }

        //MultipartFile自带的解析方法

       // FileUtils.writeByteArrayToFile(dir,file.getBytes());
       file.transferTo(dir);

        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map.put("success", true);
        map.put("message", "Successfully returning the data.");
        map1.put("response",map);

        return map1;
    }


}
