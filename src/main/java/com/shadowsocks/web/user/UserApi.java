package com.shadowsocks.web.user;

import com.shadowsocks.dto.ResponseMessageDto;
import com.shadowsocks.dto.request.LoginDto;
import com.shadowsocks.dto.request.RegisterDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RequestMapping("user")
public interface UserApi {

	/**
	 * 获取验证码
	 * */
	@ApiOperation(value = "获取验证码", tags = "user")
	@RequestMapping(path = "/captcha", method = RequestMethod.GET)
	void getCaptcha();

	/**
	 * 查看用户名是否被占用
	 * */
	@ApiOperation(value = "查看用户名是否被占用", tags = "user")
	@RequestMapping(path = "/isUsernameTaken/{username:.+}", method = RequestMethod.GET)
	boolean isUsernameTaken(@PathVariable("username") String username);

	/**
	 * 查看邮箱是否被占用
	 * */
	@ApiOperation(value = "查看邮箱是否被占用", tags = "user")
	@RequestMapping(path = "/isEmailTaken/{email:.+}", method = RequestMethod.GET)
	boolean isEmailTaken(@PathVariable("email") String email);

	/**
	 * 注册
	 * */
	@ApiOperation(value = "新用户注册", tags = "user")
	@RequestMapping(path = "/register", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    ResponseMessageDto register(@RequestBody RegisterDto registerDto);

	/**
	 * 重新发送激活邮件
	 * */
	@ApiOperation(value = "重新发送激活邮件", tags = "user")
	@RequestMapping(path = "/resend/{email:.+}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	ResponseMessageDto resendActiveEmail(@PathVariable("email") String email);

	/**
	 * 激活
	 * */
	@ApiOperation(value = "用户激活", tags = "user")
	@RequestMapping(path = "/active/{activeCode}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	ResponseMessageDto active(@PathVariable("activeCode") String activeCode);

	/**
	 * 登录
	 * */
	@ApiOperation(value = "用户登录", tags = "user")
	@RequestMapping(path = "/login", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	ResponseMessageDto login(@RequestBody LoginDto loginDto);

	/**
	 * 退出登录
	 * */
	@ApiOperation(value = "用户注销", tags = "user")
	@RequestMapping(path = "/logout", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	ResponseMessageDto logout();

	/**
	 * 查看邀请码
	 * */
	@ApiOperation(value = "查看邀请码", tags = "user")
	@RequestMapping(path = "/inviteCode", method = RequestMethod.GET)
	int inviteCode();
}
