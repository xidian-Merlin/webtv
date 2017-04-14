package com.lin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserControler {
	private static Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);
	@Autowired
	private UserService userService;

	
	/**
	 * 验证springmvc与batis连接成功
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/{id}/showUser")
	public String showUser(@PathVariable int id, HttpServletRequest request) {
		User user = userService.getUserById(id);
		System.out.println(user.getUsername());
		request.setAttribute("user", user);
		return "showUser";
	}
	
	/**
	 * 初始登陆界面
	 * @param request
	 * @return
	 */
	@RequestMapping("/login.do")
	public String tologin(HttpServletRequest request, HttpServletResponse response, Model model){
		logger.debug("来自IP[" + request.getRemoteHost() + "]的访问");
		return "login";
	}
	
	/**
	 * 验证用户名和密码
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkLogin.do")
	public String login(HttpServletRequest request) {
		String result = "login.do";
		// 取得用户名
		String username = request.getParameter("username");
		//取得 密码，并用MD5加密
		String password = CipherUtil.generatePassword(request.getParameter("password"));
		//String password = request.getParameter("password");
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		
		Subject currentUser = SecurityUtils.getSubject();
		try {
			System.out.println("----------------------------");
			if (!currentUser.isAuthenticated()){//使用shiro来验证
				token.setRememberMe(true);
				currentUser.login(token);//验证角色和权限
			}
			System.out.println("result: " + result);
			result = "index";//验证成功
		} catch (Exception e) {
			logger.error(e.getMessage());
			result = "login。do";//验证失败
		}
		return result;
	}

	@RequestMapping("/regist.do")
	public @ResponseBody Map<String, Object> getResponseOfRegist(HttpServletRequest request) {
		System.out.println("注册功能执行");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		//获取用户名与密码
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = new User();
		user.setPassword(password);
		user.setUsername(username);

		//List< User> users= new ArrayList<User>();

		//查询数据库， 判断用户名是否已注册
		User user1 = userService.findUserByLoginName(username);
		if(null != user1) {

			//返回注册结果为用户已经存在


			map.put("success", false);
			map.put("message", "the user has exist");
			map1.put("response",map);
		}else{

			//返回注册结果，注册成功，将用户名与密码存入数据库
			userService.saveUser(user);

			map.put("success", true);
			map.put("message", "Successfully returning the data.");
			map1.put("response",map);


		}


		//返回注册结果


		map.put("success", true);
		map.put("message", "Successfully returning the data.");
		map1.put("response",map);

		return map1;


	}




	/**
	 * 通过注解@ResponseBody返回JSON数据
	 *
	 * @return
	 */
	@RequestMapping("/requestjson.do")
	public @ResponseBody Map<String, Object> getAjaxDataByResponseBody() {
		System.out.println("通过注解@ResponseBody返回JSON数据");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		map.put("success", true);
		map.put("message", "Successfully returning the data.");
		map1.put("response",map);

		return map1;
	}
    /**
     * 退出
     * @return
     */
    @RequestMapping(value = "/logout")  
    @ResponseBody  
    public String logout() {  
  
        Subject currentUser = SecurityUtils.getSubject();  
        String result = "logout";  
        currentUser.logout();  
        return result;  
    }  
    
    /**
     * 妫?煡
     * @return
     */
    @RequestMapping(value = "/chklogin", method = RequestMethod.POST)  
    @ResponseBody  
    public String chkLogin() {  
        Subject currentUser = SecurityUtils.getSubject();  
        if (!currentUser.isAuthenticated()) {  
            return "false";  
        }  
        return "true";  
    }  
}
