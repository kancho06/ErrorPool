package com.sparta.errorpool.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.errorpool.article.Article;
import com.sparta.errorpool.article.Category;
import com.sparta.errorpool.article.Skill;
import com.sparta.errorpool.article.dto.ArticleCreateRequestDto;
import com.sparta.errorpool.security.UserDetailsImpl;
import com.sparta.errorpool.security.WebSecurityConfig;
import com.sparta.errorpool.user.User;
import com.sparta.errorpool.user.UserRoleEnum;
import com.sparta.errorpool.user.UserService;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = CommentController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class))
class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;
    @MockBean
    UserService userService;

    User testUser;
    Article mockArticle;

    @BeforeEach
    public void setup() {
        testUser = new User(
                "testuser@test.com",
                "password",
                "tester",
                UserRoleEnum.USER,
                Skill.NONE
        );

        mockArticle = new Article(
                new ArticleCreateRequestDto(
                        "test title",
                        null,
                        "test content",
                        2,
                        2
                ), testUser);
        mockArticle.setId(1L);
    }

    @Test
    @WithUserDetails
    @DisplayName("?????? ??????-input ??????-??????")
    void addComment_CheckInputNormal() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .articleId(1L)
                .commentId(1L)
                .username(testUser.getEmail())
                .content("??????1")
                .build();
        String commentInfo = objectMapper.writeValueAsString(commentDto);
        given(commentService.addComment(any(), any(), any()))
                .willReturn(new Comment(testUser, mockArticle, "??????1"));
        given(userService.userFromUserDetails(any()))
                .willReturn(testUser);

        mvc.perform(post("/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentInfo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successYn").value(true))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data.content").value("??????1"))
                .andExpect(jsonPath("$.data.article.id").value(1L));
    }

    @Test
    @WithUserDetails
    @DisplayName("?????? ??????-input ??????-????????????(Request????????? ???????????? ????????????)")
    void addComment_CheckInput_RequestNullError() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .commentId(1L)
                .username(testUser.getEmail())
                .content("??????1")
                .build();
        String commentInfo = objectMapper.writeValueAsString(commentDto);
        given(commentService.addComment(any(), any(), any()))
                .willThrow(IllegalArgumentException.class);
        given(userService.userFromUserDetails(any()))
                .willReturn(testUser);

        mvc.perform(post("/comments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentInfo))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successYn").value(false))
                .andExpect(jsonPath("$.statusCode").value(400));

        verify(commentService).addComment(eq(null), any(CommentDto.class), any(User.class));
    }

    @Test
    @DisplayName("?????? ??????-input ??????-????????????(CommentDto??? commentId ????????????)")
    void addComment_CheckInput_CommentDto_commentIdNullError() {

    }

    @Test
    @DisplayName("?????? ??????-input ??????-????????????(CommentDto??? userName ????????????)")
    void addComment_CheckInput_CommentDto_userNameNullError() {

    }

    @Test
    @DisplayName("?????? ??????-input ??????-????????????(CommentDto??? articleId ????????????)")
    void addComment_CheckInput_CommentDto_articleIdNullError() {

    }

    @Test
    @DisplayName("?????? ??????-input ??????-????????????(CommentDto??? content ????????????)")
    void addComment_CheckInput_CommentDto_contentNullError() {

    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void addCommentNormal() {


    }

    @Test
    @DisplayName("?????? ?????? ??????")
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
//// Mock ????????? ?????? ??????
//        String username = "?????????";
//        String password = "hope!@#";
//        String email = "hope@sparta.com";
//        UserRoleEnum role = UserRoleEnum.USER;
//        User testUser = new User(username, password, email, role);
//        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
//        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
//    }
//
//    @Test
//    @DisplayName("????????? view")
//    void test1() throws Exception {
//// when - then
//        mvc.perform(get("/user/login"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("login"))
//                .andDo(print());
//    }
//
//    @Test
//    @DisplayName("?????? ?????? ?????? ??????")
//    void test2() throws Exception {
//// given
//        MultiValueMap<String, String> signupRequestForm = new LinkedMultiValueMap<>();
//        signupRequestForm.add("username", "?????????");
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
//    @DisplayName("?????? ???????????? ??????")
//    void test3() throws Exception {
//// given
//        this.mockUserSetup();
//        String title = "Apple <b>?????????</b> 2?????? ???????????? ?????? (MV7N2KH/A)";
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