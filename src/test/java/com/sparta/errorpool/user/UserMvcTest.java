//package com.sparta.errorpool.user;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nimbusds.oauth2.sdk.http.HTTPResponse;
//import com.sparta.errorpool.article.Skill;
//import com.sparta.errorpool.defaultResponse.DefaultResponse;
//import com.sparta.errorpool.defaultResponse.ResponseMessage;
//import com.sparta.errorpool.defaultResponse.StatusCode;
//import com.sparta.errorpool.defaultResponse.SuccessYn;
//import com.sparta.errorpool.globalController.GlobalController;
//import com.sparta.errorpool.security.UserDetailsImpl;
//import com.sparta.errorpool.security.WebSecurityConfig;
//import com.sparta.errorpool.user.dto.LoginResDto;
//import com.sparta.errorpool.user.dto.UserRequestDto;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultMatcher;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.security.Principal;
//
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.willReturn;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(
//        controllers = {UserController.class, GlobalController.class, },
//        excludeFilters = {
//                @ComponentScan.Filter(
//                        type = FilterType.ASSIGNABLE_TYPE,
//                        classes = WebSecurityConfig.class
//                )
//        }
//)
//
//
//class UserMvcTest {
//
//    private MockMvc mvc;
//
//    private Principal mockPrincipal;
//
//    @Autowired
//    private WebApplicationContext context;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    UserService userService;
//
//    @MockBean
//    KakaoUserService kakaoUserService;
//
//    @BeforeEach
//    public void setup() {
//        mvc = MockMvcBuilders.webAppContextSetup(context)
//                .apply(springSecurity(new MockSpringSecurityFilter()))
//                .build();
//    }
//    private void mockUserSetup() {
//        String email = "wotjd5792@gmail.com";
//        String username = "이재성";
//        String password = "dlfksjd";
//        Skill skill = null;
//        Long socialId = null;
//        UserRoleEnum role = UserRoleEnum.USER;
//        User testUser = new User(email, username, password, role, socialId, skill);
//
//
//
//        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
//        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
//    }
//
//    LoginResDto loginResDto;
//
//    @Test
//    @DisplayName("로그인")
//    void userTest1() throws Exception {
//        this.mockUserSetup();
//
//        String email = "wotjd5792@gmail.com";
//        String password = "dlfksjd";
//
//
//        UserRequestDto userRequestDto = new UserRequestDto();
//        userRequestDto.setEmail(email);
//        userRequestDto.setPassword(password);
//        String userInfo = objectMapper.writeValueAsString(userRequestDto);
//
//        given(userService.createToken(userRequestDto))
//                .willReturn(String.valueOf(loginResDto));
//
//        mvc.perform(post("/user/login")
//                        .content(userInfo)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        //위에 유저만들때 유저디테일로 인증 시킨것을 여기서 확인
//                        .principal(mockPrincipal)
//                        .with(csrf())
//
//                )
////                .andExpect((ResultMatcher) willReturn(loginResDto))
//                .andExpect(status().isOk())
//                .andDo(print());
//
//
//    }
//
//    @Test
//    @DisplayName("회원 가입 요청 처리")
//    void userTest2() throws Exception{
//        MultiValueMap<String, String> signupRequestForm = new LinkedMultiValueMap<>();
//        signupRequestForm.add("email","wotjd5792@gmail.com");
//        signupRequestForm.add("username","이재성");
//        signupRequestForm.add("password","dlfksjd");
//
//
//
//        mvc.perform(post("/user/signup")
//                        .params(signupRequestForm)
//
//                )
//                .andExpect((ResultMatcher) willReturn(DefaultResponse.res(SuccessYn.OK, StatusCode.OK , ResponseMessage.CREATED_USER,null ), HttpStatus.OK))
//                .andExpect(status().is3xxRedirection())
//                .andDo(print());
//    }
//}