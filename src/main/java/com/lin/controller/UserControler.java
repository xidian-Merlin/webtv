package com.lin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.tools.internal.ws.processor.model.Response;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserControler {
	private static Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);
	@Autowired
	private UserService userService;

	@Autowired
	private ShiroDbRealm shiroDbRealm;

	
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
	public  @ResponseBody Map<String, Object> login(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		//String result = "login.do";
		// 取得用户名
		String username = request.getParameter("username");
		//取得 密码，并用MD5加密
		String password = CipherUtil.generatePassword(request.getParameter("password"));
		//String password = request.getParameter("password");
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);

		//从shiro的session中取出user
		Subject subject = SecurityUtils.getSubject();
		try {
			System.out.println("----------------------------");
			if (!subject.isAuthenticated()){//该用户如果没有认证
				token.setRememberMe(true);  //记住我
				subject.login(token);//验证角色和权限
			}
			//用户名验证成功
			User user = userService.findUserByLoginName(username);
			//登录成功，将用户保存进shiro的 session
			subject.getSession().setAttribute("user", user);

			map.put("success", true);
			map.put("message", "login Successfully");
			map1.put("response",map);

			//System.out.println("result: " + result);
			//result = "index";//验证成功
		} catch (Exception e) {
			logger.error(e.getMessage());
			map.put("success", false);
			map.put("message", "login failed");
			map1.put("response",map);
			//result = "login.do";//验证失败
		}

		return map1;
	}

	/**
	 * 注册功能
	 * @param request
	 * @return jsonmessage
	 */

	@RequestMapping("/regist.do")
	public @ResponseBody Map<String, Object> getResponseOfRegist(HttpServletRequest request) {
		System.out.println("注册功能执行");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		//获取用户名与密码
		String username = request.getParameter("username");

		String password = request.getParameter("password");
		password = CipherUtil.generatePassword(request.getParameter("password"));
		User user = new User();
		user.setPassword(password);
		user.setUsername(username);

		//List< User> users= new ArrayList<User>();

		//查询数据库， 判断用户名是否已注册
		User user1 = null;
		user1 = userService.findUserByLoginName(username);
		if(null != user1) {

			//返回注册结果为用户已经存在


			map.put("success", false);
			map.put("message", "the user has exist");
			map1.put("response",map);
		}else{

			//返回注册结果，注册成功，将用户名与密码存入数据库
			userService.saveUser(user);

			map.put("success", true);
			map.put("message", "Successfully resisting");
			map1.put("response",map);
		}
		//返回注册结果
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
     *
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

	/**
	 * 获取播放地址
	 * @param request
	 * @return
	 */
	@RequestMapping("/showingtime.do") //鉴权，是否能够进行直播
	//@RequiresRoles("anchor")
	public void getAddress(HttpServletRequest request,HttpServletResponse response) {

//		Subject currentUser = SecurityUtils.getSubject();
//		String name = request.getParameter("username") ;
//		String password = request.getParameter("password");
//		shiroDbRealm.clearCachedAuthorizationInfo((String) currentUser.getPrincipal().toString());
//
//
//
//		if(currentUser.hasRole("anchor")){   //一次授权之后会有缓存，下次不会再进行判断
//			System.out.print("有该角色");
//
//
//
//
//		}
//		else {
//			System.out.print("没哦");
//			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//返回404状态；
//
//		}
//
//
	}
}
