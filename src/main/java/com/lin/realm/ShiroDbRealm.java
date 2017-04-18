package com.lin.realm;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lin.domain.User;
import com.lin.service.UserService;
import com.lin.utils.CipherUtil;

public class ShiroDbRealm extends AuthorizingRealm {
	private static Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);
	//凭证
	private static final String ALGORITHM = "MD5";
	
	@Autowired
	private UserService userService; //接口而不是实现

	public ShiroDbRealm() {
		super();
	}
	
	/**
	 * 验证登陆
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {

		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		System.out.println(token.getUsername());

		String username = (String)token.getPrincipal();  //得到用户名
		String password = new String((char[])token.getCredentials()); //得到密码

		User user = userService.findUserByLoginName(username);

		if(!user.getUsername().equals(username)) {
			throw new UnknownAccountException(); //如果用户名错误
		}
		if(!user.getPassword().equals(password)) {
			throw new IncorrectCredentialsException(); //如果密码错误
		}
		//如果身份认证验证成功，返回一个AuthenticationInfo实现；
		return new SimpleAuthenticationInfo(username, password, getName());
	}

	/**
	 *  查验授权信息时，如果没有授权缓存，就回调此函数，对用户进行角色赋予及授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		/*这里应该根据userName使用role和permission 的serive层来做判断，并将对应 的权限加进来，下面简化了这一步*/
		Set<String> roleNames = new HashSet<String>();
	    Set<String> permissions = new HashSet<String>();
	  //  roleNames.add("admin");//添加角色。对应到index.jsp
	    roleNames.add("anchor");//添加角色，主播
	  //  permissions.add("create");//添加权限,对应到index.jsp
	   // permissions.add("login.do?main");
	   /// permissions.add("login.do?logout");
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
	  //  info.setStringPermissions(permissions);
		return info;
	}


	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}


	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

//	@PostConstruct
//	public void initCredentialsMatcher() {//MD5鍔犲瘑
//		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(ALGORITHM);
//		setCredentialsMatcher(matcher);
//	}
}
