package ssafy.e105.Seiren.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ssafy.e105.Seiren.domain.user.dto.ProfileImgRequest;
import ssafy.e105.Seiren.domain.user.dto.login.LoginReqDto;
import ssafy.e105.Seiren.domain.user.dto.nickname.NicknameReqDto;
import ssafy.e105.Seiren.domain.user.dto.register.RegisterReqDto;
import ssafy.e105.Seiren.domain.user.service.UserService;
import ssafy.e105.Seiren.global.common.BaseResponse;
import ssafy.e105.Seiren.global.utils.ApiResult;
import ssafy.e105.Seiren.global.utils.ApiUtils;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "회원 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 가입")
    @PostMapping("/signup")
    public BaseResponse signup(
            @RequestBody @Valid RegisterReqDto registerReqDto
    ){
        return BaseResponse.success(userService.signup(registerReqDto));
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ApiResult<?> login(
            @RequestBody @Valid LoginReqDto loginReqDto,
            HttpServletResponse response
            ){
        return ApiUtils.success(userService.login(response, loginReqDto));
    }

    @Operation(summary = "닉네임 중복 체크")
    @GetMapping("/nicknames/chcek")
    public ApiResult nicknameCheck(@RequestParam String nickname){
        return ApiUtils.success(userService.nicknameCheck(nickname));
    }

    @Operation(summary = "닉네임 업데이트")
    @PutMapping("/nicknames")
    public ApiResult nicknameUpdate(HttpServletRequest request, @RequestBody @Valid NicknameReqDto nicknameReqDto){
        return ApiUtils.success(userService.nicknameUpdate(request, nicknameReqDto));
    }

    @Operation(summary = "프로필 사진 업데이트")
    @PutMapping("/profileimg")
    public ApiResult profileImgUpdate(HttpServletRequest request, @RequestBody @Valid ProfileImgRequest profileImgRequest){
        return ApiUtils.success(userService.profileImgUpdate(request, profileImgRequest));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping()
    public ApiResult deleteUser(HttpServletRequest request){
        return ApiUtils.success(userService.deleteUser(request));
    }



}