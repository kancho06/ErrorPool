package com.sparta.errorpool.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.errorpool.article.Skill;
import com.sparta.errorpool.security.UserDetailsImpl;
import com.sparta.errorpool.security.WebSecurityConfig;
import com.sparta.errorpool.user.User;
import com.sparta.errorpool.user.UserRoleEnum;
import com.sparta.errorpool.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest(
        controllers = CommentController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class))
class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    CommentService commentService;

//    @BeforeEach
//    public void setup() {
//        MockMvcBuilders.webAppContextSetup(context)
//                .apply(springSecurity(new MockSpringSecurityFilter())).build();
//    }


        private void mockUserSetup() {
// Mock 테스트 유져 생성
        String username = "namelim@gmail.com";
        String password = "1111";
        String email = "namelim@gmail.com";
        String socialId = "namelim@gmail.com";
        UserRoleEnum role = UserRoleEnum.USER;
        User testUser = new User(email,password,username,role, Skill.SPRING);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }


    @Test
    @WithMockUser( username="namelim@gmail.com", password = "custom_password", roles = {"USER"})
    @DisplayName("댓글 추가-input 확인-성공")
    void addComment_CheckInputNormal() throws Exception {
        Map<String, Long> input = new HashMap<>();
        input.put("articleId",21L);

//        User user = this.mockUserSetup();
        this.mockUserSetup();
        CommentDto commentDto = CommentDto.builder()
                .articleId(30L)
                .commentId(1L)
                .username("namelim@gmail.com")
                .content("댓글1")
                .build();

        String commentInfo = objectMapper.writeValueAsString(commentDto);
        String inputInfo = objectMapper.writeValueAsString(input);
        mvc.perform(post("/comments")
                        .param("articleId",String.valueOf(30L))
                        .content(commentInfo)
                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 추가-input 확인-오류발생(Request게시글 아이디가 비어있음)")
    void addComment_CheckInput_RequestNullError() {

    }

    @Test
    @DisplayName("댓글 추가-input 확인-오류발생(CommentDto의 commentId 비어있음)")
    void addComment_CheckInput_CommentDto_commentIdNullError() {

    }

    @Test
    @DisplayName("댓글 추가-input 확인-오류발생(CommentDto의 userName 비어있음)")
    void addComment_CheckInput_CommentDto_userNameNullError() {

    }

    @Test
    @DisplayName("댓글 추가-input 확인-오류발생(CommentDto의 articleId 비어있음)")
    void addComment_CheckInput_CommentDto_articleIdNullError() {

    }

    @Test
    @DisplayName("댓글 추가-input 확인-오류발생(CommentDto의 content 비어있음)")
    void addComment_CheckInput_CommentDto_contentNullError() {

    }

    @Test
    @DisplayName("댓글 추가 성공")
    void addCommentNormal() {


    }

    @Test
    @DisplayName("댓글 추가 성공")
    void addCommentError() {

    }

    @Test
    void modifyCommentNormal() {
    }

    @Test
    void modifyCommentError() {
    }

    @Test
    void deleteCommentNormal() {
    }

    @Test
    void deleteCommentError() {
    }


//    private void mockUserSetup() {
//// Mock 테스트 유져 생성
//        String username = "제이홉";
//        String password = "hope!@#";
//        String email = "hope@sparta.com";
//        UserRoleEnum role = UserRoleEnum.USER;
//        User testUser = new User(username, password, email, role);
//        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
//        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
//    }
//
//    @Test
//    @DisplayName("로그인 view")
//    void test1() throws Exception {
//// when - then
//        mvc.perform(get("/user/login"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("login"))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("회원 가입 요청 처리")
//    void test2() throws Exception {
//// given
//        MultiValueMap<String, String> signupRequestForm = new LinkedMultiValueMap<>();
//        signupRequestForm.add("username", "제이홉");
//        signupRequestForm.add("password", "hope!@#");
//        signupRequestForm.add("email", "hope@sparta.com");
//        signupRequestForm.add("admin", "false");
//
//// when - then
//        mvc.perform(post("/user/signup")
//                        .params(signupRequestForm)
//                )
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/user/login"))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("신규 관심상품 등록")
//    void test3() throws Exception {
//// given
//        this.mockUserSetup();
//        String title = "Apple <b>에어팟</b> 2세대 유선충전 모델 (MV7N2KH/A)";
//        String imageUrl = "https://shopping-phinf.pstatic.net/main_1862208/18622086330.20200831140839.jpg";
//        String linkUrl = "https://search.shopping.naver.com/gate.nhn?id=18622086330";
//        int lPrice = 77000;
//        ProductRequestDto requestDto = new ProductRequestDto(
//                title,
//                imageUrl,
//                linkUrl,
//                lPrice
//        );
//
//        String postInfo = objectMapper.writeValueAsString(requestDto);
//
//// when - then
//        mvc.perform(post("/api/products")
//                        .content(postInfo)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .principal(mockPrincipal)
//                )
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
}