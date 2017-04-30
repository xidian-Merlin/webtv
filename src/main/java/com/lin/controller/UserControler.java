package com.lin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageHelper;
import com.lin.dao.UserDao;
import com.lin.domain.AnchorModel;
import com.lin.domain.UserExample;
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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
     -	 * 通过注解@ResponseBody返回JSON数据
     -	 *
     -	 * @return
     -	 */
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
			user.setLive(false);
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
    public Map<String, Object>  logout() {

        System.out.println("登出功能执行");
        //将取消角色绑定
      //  Subject currentUser = SecurityUtils.getSubject();
        //String result = "logout";
       // currentUser.logout();

        //将数据库中该用户设置为不在线




        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map.put("success", true);
        map.put("message", "Successfully  loginout.");
        map1.put("response",map);
        return map1;
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
	public void getAddress(HttpServletRequest request,HttpServletResponse response) throws IOException {

		Subject currentUser = SecurityUtils.getSubject();
		String name = request.getParameter("username") ;
		String password = CipherUtil.generatePassword(request.getParameter("password"));
		//清空认证缓存
	//	shiroDbRealm.clearCachedAuthorizationInfo((String) currentUser.getPrincipal().toString());

        User anchor = userService.findUserByLoginName(name);

		//if(currentUser.hasRole("anchor"))

        if (password.equals(anchor.getPassword()))

		{   //一次授权之后会有缓存，下次不会再进行判断
			System.out.print("有该角色");
			//将数据库中的该角色的live设置为1，表示主播开始直播了
            anchor.setLive(true);

            userService.updateUserBySelect(anchor);








		}
		else {
			System.out.print("没哦");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);//返回404状态；
			response.getWriter().write("messaege: 没有该角色");

		}


	}


    /**
     * 关闭直播
     * @param request
     * @return
     */

	@RequestMapping("/closetime.do")
	public @ResponseBody Map<String, Object> closeShowing(HttpServletRequest request) {


		System.out.println("关闭直播");

		String name = request.getParameter("username") ;
		String password = CipherUtil.generatePassword(request.getParameter("password"));
		//清空认证缓存
		//	shiroDbRealm.clearCachedAuthorizationInfo((String) currentUser.getPrincipal().toString());

		User anchor = userService.findUserByLoginName(name);

		//将数据库中的该角色的live设置为1，表示主播关闭了直播；
		anchor.setLive(false);

		userService.updateUserBySelect(anchor);

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		map.put("success", true);
		map.put("message", "Successfully  close living");
		map1.put("response",map);

		return map1;
	}





    /**
     * 分页请求在线的主播
     * @param request
     * @return
     */

	@RequestMapping("/getAnchor.do")
	public  @ResponseBody Map<String, Object> getAnchor (HttpServletRequest request) {

		Map<String, Object> map1 = new HashMap<String, Object>();
		List<AnchorModel> anchors = new ArrayList<AnchorModel>();
 		//使用了pageHelper插件来分业
		String pageValue = request.getParameter("page");
		int pageNum = 0;

 		try{
			pageNum = Integer.valueOf(pageValue).intValue();
		} catch (NumberFormatException e){
			e.printStackTrace();
		}


        //分页查询
		List<User> list = userService.getAllByLimit(pageNum,20);

		if(list==null){
			return null;
		}


		int count = 1;


		for(User user : list) {

		AnchorModel anchor = new AnchorModel();
        anchor.setId(user.getId());
        anchor.setUsername(user.getUsername());
        anchor.setSex(user.getSex());
        anchor.setAddress(user.getAddress());

     //   Map<String, Object> map = new HashMap<String, Object>();

       // String anchorTag = "anchor"+count;

       // map.put(anchorTag, anchor);

        anchors.add(anchor);

        count ++;

		}
		map1.put("anchors", anchors);
		return map1;

	}


}
