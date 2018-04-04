package com.shadowsocks.web.user;

import com.google.common.collect.Lists;
import com.shadowsocks.config.GlobalConfig;
import com.shadowsocks.dto.entity.Balance;
import com.shadowsocks.dto.entity.EmailConfig;
import com.shadowsocks.dto.entity.EmailObject;
import com.shadowsocks.dto.entity.User;
import com.shadowsocks.dto.enums.ActiveStatusEnum;
import com.shadowsocks.dto.enums.ResultEnum;
import com.shadowsocks.dto.request.LoginDto;
import com.shadowsocks.dto.request.RegisterDto;
import com.shadowsocks.dto.response.UserCenter;
import com.shadowsocks.service.BalanceService;
import com.shadowsocks.service.EmailService;
import com.shadowsocks.utils.*;
import com.shadowsocks.web.BaseController;
import com.shadowsocks.dto.ResponseMessageDto;
import com.shadowsocks.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
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
import java.util.Objects;
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
    private BalanceService balanceService;

    public UserApiController(UserService userService, HttpServletRequest request, GlobalConfig globalConfig,
                             HttpServletResponse response, HttpSession session, EmailService emailService,
                             BalanceService balanceService) {
        this.userService = userService;
        this.request = request;
        this.response = response;
        this.session = session;
        this.globalConfig = globalConfig;
        this.emailService = emailService;
        this.balanceService = balanceService;
    }

	@Override
	public void getCaptcha() {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		String ipAddress = getCurrentIpAddress(request);
		String userAgent = request.getHeader("User-Agent");
        String verifyCode = CaptchaUtils.generateVerifyCode(4);
        log.info("生成验证码 {} ，来源IP {}, User-Agent {}", verifyCode, ipAddress, userAgent);
		session.setMaxInactiveInterval(300);
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
    public boolean isUsernameTaken(@PathVariable("username") String username) {
        boolean result = userService.isUsernameTaken(username.toLowerCase());
        if(result) {
            log.info("用户名 {} 已经被占用", username);
        }
        return result;
    }

    @Override
    public boolean isEmailTaken(@PathVariable("email") String email) {
        boolean result = userService.isEmailTaken(email);
        if(result) {
            log.info("邮箱 {} 已被占用", email);
        }
        return result;
    }

    private User buildUserFromRegisterDto(RegisterDto registerDto) {
        String time = LocalDateTime.now().format(formatter);
        String activeCode = RandomStringUtils.generateRandomStringWithMD5();
        User user = User.builder()
                .username(registerDto.getUsername().toLowerCase())
                .email(registerDto.getEmail())
                .password(registerDto.getPassword())
                .registerIp(getCurrentIpAddress(request))
                .registerTime(time)
                .loginTimes(0)
                .lastLoginTime("")
                .lastLoginIp("")
                .activeStatus(ActiveStatusEnum.NON_ACTIVE.name())
                .activeCode(activeCode)
                .build();
        if(Objects.nonNull(registerDto.getInviter())) {
            user.setInviter(registerDto.getInviter());
        }
        return user;
    }

    private void sendActiveEmail(String email, String activeCode) {
        String pattern = "%s/shadowsocks/user/active/%s";
        String activeURL = String.format(pattern, globalConfig.getUrl(), activeCode);
        log.info("生成激活邮件，邮箱 {}, 激活地址 {}", email, activeURL);
        List<EmailConfig> emailConfigList = emailService.findEmailConfigs();

        String contentPattern = HtmlUtils.getActiveHtmlPattern();
        String content = MessageFormat.format(contentPattern, activeURL);
        EmailObject emailObject = EmailObject.builder().toList(Lists.newArrayList(email)).subject("OceanHere 激活邮件").content(content).build();
        EmailUtils.sendEmailAsyc(emailConfigList, emailObject);
    }

    @Override
    public ResponseMessageDto register(@RequestBody RegisterDto registerDto) {
        String captchaInSession = (String) session.getAttribute(SessionKeyUtils.getKeyForCaptcha());
        if(StringUtils.isEmpty(captchaInSession)) {
            log.warn("验证码未生成");
            return ResponseMessageDto.builder().result(ResultEnum.FAIL).message("验证码未生成").build();
        }

        if(!captchaInSession.equalsIgnoreCase(registerDto.getCaptcha())) {
            log.warn("验证码输入错误");
            return ResponseMessageDto.builder().result(ResultEnum.FAIL).message("验证码不正确").build();
        }

        User user = buildUserFromRegisterDto(registerDto);
        boolean result = userService.register(user);

        if(result) {
            log.info("注册成功， 用户名 {}, 邮箱 {}, 来源IP {}", registerDto.getUsername(), registerDto.getEmail(), getCurrentIpAddress(request));
            sendActiveEmail(registerDto.getEmail(), user.getActiveCode());
            return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("注册成功, 请检查邮箱并激活账号").build();
        }
        return ResponseMessageDto.builder().result(ResultEnum.FAIL).message("注册失败").build();
    }

    @Override
    public ResponseMessageDto resendActiveEmail(String email) {
        Optional<String> activeCodeOptional = userService.findActiveCodeByEmail(email);
        if(!activeCodeOptional.isPresent()) {
            log.error("邮箱 {} 不存在", email);
            return ResponseMessageDto.builder().result(ResultEnum.FAIL).message("邮箱不存在").build();
        }
        log.info("重新发送激活邮件， 邮箱 {}", email);
        activeCodeOptional.ifPresent(activeCode -> sendActiveEmail(email, activeCode));
        return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("邮件发送成功").build();
    }

    @Override
    public ResponseMessageDto active(@PathVariable("activeCode") String activeCode) {
        boolean activeResult = userService.active(activeCode);
        if(activeResult) {
            log.info("激活成功， 激活码 {}", activeCode);
            Optional<User> userOptional = userService.findUserByActiveCode(activeCode);
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                boolean createBalanceItemResult = balanceService.createItem(user.getId());
                if(createBalanceItemResult) {
                    log.info("创建余额数据成功， 用户id {}", user.getId());
                    int inviter = user.getInviter();
                    if(inviter != 0) {
                        boolean addBalanceResult = balanceService.addBalanceByUserId(inviter, 3.0);
                        if(addBalanceResult) log.info("为邀请者 [userId={}] 增加余额成功", inviter);
                    }
                    return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("激活成功").build();
                }
            }
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
            log.info("用户 {} 登录成功, 来源 IP {}", loginDto.getUsername(), loginDto.getIp());
            return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("登录成功").build();
        }
        log.error("用户 {} 登录失败， 密码 {}, 来源 IP {}", loginDto.getUsername(), loginDto.getPassword(), loginDto.getIp());
        return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("登录失败").build();
    }

    @Override
    public ResponseMessageDto logout() {
        session.removeAttribute(SessionKeyUtils.getKeyForUser());
        return ResponseMessageDto.builder().result(ResultEnum.SUCCESS).message("退出登录成功").build();
    }

    @Override
    public int inviteCode() {
        User user = (User) session.getAttribute(SessionKeyUtils.getKeyForUser());
        return user.getId();
    }

    @Override
    public UserCenter userCenter() {
        User user = (User) session.getAttribute(SessionKeyUtils.getKeyForUser());
        Optional<Balance> balanceOptional = balanceService.findBalanceByUserId(user.getId());
        UserCenter userCenter = UserCenter.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .loginTimes(user.getLoginTimes())
                .lastLoginTime(user.getLastLoginTime())
                .lastLoginIp(user.getLastLoginIp())
                .build();
        balanceOptional.ifPresent(balance -> userCenter.setBalance(balance.getCurrentBalance()));
        return userCenter;
    }
}
