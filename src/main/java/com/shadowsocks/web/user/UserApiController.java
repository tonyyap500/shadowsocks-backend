package com.shadowsocks.web.user;

import com.shadowsocks.dto.UserRegisterDto;
import com.shadowsocks.web.BaseController;
import com.shadowsocks.dto.ResponseMessageDto;
import com.shadowsocks.service.UserService;
import com.shadowsocks.utils.CaptchaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import static com.shadowsocks.utils.SessionKeyUtils.getKeyForCaptcha;

@RestController
@Slf4j
public class UserApiController extends BaseController implements UserApi {


	private UserService userService;
	private HttpServletRequest request;
	private HttpServletResponse response;
    private HttpSession session;

    public UserApiController(UserService userService, HttpServletRequest request,
                             HttpServletResponse response, HttpSession session) {
        this.userService = userService;
        this.request = request;
        this.response = response;
        this.session = session;
    }

	@Override
	public void getCaptcha() {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		String verifyCode = CaptchaUtils.generateVerifyCode(4);
		session.removeAttribute(getKeyForCaptcha());
		session.setAttribute(getKeyForCaptcha(), verifyCode.toLowerCase());

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
    public boolean isUsernameTaken(String username) {
        String ip = getCurrentIpAddress(request);
        log.info("ip : {}", ip);
        return false;
    }

    @Override
    public boolean isEmailTaken(String email) {
        return false;
    }

    @Override
    public ResponseMessageDto register(UserRegisterDto userRegisterDtoRegisterDto) {
        return null;
    }

    @Override
    public ResponseMessageDto active(String username) {
        return null;
    }

    @Override
    public ResponseMessageDto login(UserRegisterDto userRegisterDtoRegisterDto) {
        return null;
    }

    @Override
    public ResponseMessageDto logout(HttpSession session) {
        return null;
    }


}
