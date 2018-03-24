package com.shadowsocks.web.user;

import com.google.common.collect.Lists;
import com.shadowsocks.config.GlobalConfig;
import com.shadowsocks.dto.entity.EmailConfig;
import com.shadowsocks.dto.entity.EmailObject;
import com.shadowsocks.dto.entity.User;
import com.shadowsocks.dto.enums.ActiveStatusEnum;
import com.shadowsocks.dto.enums.ResultEnum;
import com.shadowsocks.dto.request.LoginDto;
import com.shadowsocks.dto.request.RegisterDto;
import com.shadowsocks.service.EmailService;
import com.shadowsocks.utils.*;
import com.shadowsocks.web.BaseController;
import com.shadowsocks.dto.ResponseMessageDto;
import com.shadowsocks.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@RestController
@Slf4j
public class UserApiController extends BaseController implements UserApi {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private UserService userService;
	private HttpServletRequest request;
	private HttpServletResponse response;
    private HttpSession session;
    private GlobalConfig globalConfig;
    private EmailService emailService;

    public UserApiController(UserService userService, HttpServletRequest request, GlobalConfig globalConfig,
                             HttpServletResponse response, HttpSession session, EmailService emailService) {
        this.userService = userService;
        this.request = request;
        this.response = response;
        this.session = session;
        this.globalConfig = globalConfig;
        this.emailService = emailService;
    }

	@Override
	public void getCaptcha() {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		String verifyCode = CaptchaUtils.generateVerifyCode(4);
		session.setMaxInactiveInterval(60);
		session.removeAttribute(SessionKeyUtils.getKeyForCaptcha());
		session.setAttribute(SessionKeyUtils.getKeyForCaptcha(), verifyCode.toLowerCase());

		int width = 100;
		int height = 40;
		try {
			CaptchaUtils.outputImage(width, height, response.getOutputStream(), verifyCode);
		}catch (Exception e) {
			log.error("{}", "生成验证码失败");
			log.error("{}", e);
		}
	}


    @Override
    public boolean isUsernameTaken(@PathVariable("username:.+") String username) {
        return userService.isUsernameTaken(username.toLowerCase());
    }

    @Override
    public boolean isEmailTaken(@PathVariable("email") String email) {
        return userService.isEmailTaken(email);
    }

    private User buildUserFromRegisterDto(RegisterDto registerDto) {
        String time = LocalDateTime.now().format(formatter);
        String activeCode = RandomStringUtils.generateRandomString();
        return User.builder()
                .username(registerDto.getUsername().toLowerCase())
                .email(registerDto.getEmail())
                .password(registerDto.getPassword())
                .inviter(registerDto.getInviter())
                .registerIp(getCurrentIpAddress(request))
                .registerTime(time)
                .loginTimes(0)
                .lastLoginTime("")
                .lastLoginIp("")
                .activeStatus(ActiveStatusEnum.NON_ACTIVE.name())
                .activeCode(activeCode)
                .build();
    }

    @Override
    public ResponseMessageDto register(@RequestBody RegisterDto registerDto) {
        String captchaInSession = (String) session.getAttribute(SessionKeyUtils.getKeyForCaptcha());
        if(!captchaInSession.equalsIgnoreCase(registerDto.getCaptcha())) {
            return ResponseMessageDto.builder().result(ResultEnum.FAIL).message("验证码不正确").build();
        }

        User user = buildUserFromRegisterDto(registerDto);
        boolean result = userService.register(user);

        if(result) {
            //TODO 为邀请人充值
            String pattern = "%s/shadowsocks/user/active/%s";
            String url = String.format(pattern, globalConfig.getUrl(), user.getActiveCode());
            List<EmailConfig> emailConfigList = emailService.findEmailConfigs();

            String contentPattern = HtmlUtils.getActiveHtmlPattern();
            String content = MessageFormat.format(contentPattern, url, url);
            EmailObject emailObject = EmailObject.builder().toList(Lists.newArrayList(registerDto.getEmail())).subject("OceanHere 激活邮件").content(content).build();
            EmailUtils.sendEmailAsyc(emailConfigList, emailObject);
            return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("注册成功, 请检查邮箱并激活账号").build();
        }
        return ResponseMessageDto.builder().result(ResultEnum.FAIL).message("注册失败").build();
    }

    @Override
    public ResponseMessageDto active(@PathVariable("activeCode") String activeCode) {
        boolean result = userService.active(activeCode);
        if(result) {
            return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("激活成功").build();
        }
        return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("激活失败").build();
    }

    @Override
    public ResponseMessageDto login(@RequestBody LoginDto loginDto) {
        loginDto.setIp(getCurrentIpAddress(request));
        Optional<User> userOptional = userService.login(loginDto);
        userOptional.ifPresent(user -> {
            session.setMaxInactiveInterval(3600);
            session.setAttribute(SessionKeyUtils.getKeyForUser(), user);
        });
        if(userOptional.isPresent()) {
            return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("登录成功").build();
        }
        return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("登录失败").build();
    }

    @Override
    public ResponseMessageDto logout() {
        session.removeAttribute(SessionKeyUtils.getKeyForUser());
        return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("退出登录成功").build();
    }
}
