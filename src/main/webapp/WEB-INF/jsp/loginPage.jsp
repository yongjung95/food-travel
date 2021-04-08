<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="include/header.jsp"%>


<section id="banner" data-video="images/banner">
    <div class="inner">
        <h1>Food Travel</h1>
        <p>여러 사람들과 맛집 정보를 공유하는 음식 여행 지금 시작 해보세요!<br /></p>
        <p>회원가입없이 소셜로그인 이용하세요.</p>
        <!--   built by <a href="https://templated.co/">Templated</a> and released under the <a href="https://templated.co/license">Creative Commons</a>.-->
        <!-- <a href="#one" class="button special scrolly">Get Started</a>-->
        <div class="social_login_title">
            <span class="divider-with-text"></span>
        </div>
        <div class="social_login">
            <a class="nb" href="/oauth2/authorization/naver">Naver</a>
            <a class="kk" href="/oauth2/authorization/kakao">Kakao</a>
            <a class="gg" href="/oauth2/authorization/google">Google</a>
            <a class="fb" href="/oauth2/authorization/facebook">Facebook</a>
        </div>
    </div>

</section>
<style>
    .social_login a {float:left; display:block; width:49%; height:48px; line-height:48px; font-size:16px; border-radius: 4px; font-weight:bold; color:#fff; margin-bottom:8px; transition: all 0.2s; -webkit-transition: all 0.2s; -moz-transition: all 0.2s; -o-transition: all 0.2s}
    .kk {background:#FFBF00; margin-right:0}
    .nb {background:#1ec800; margin-right: 2%}
    .gg {background:#FE2E2E; margin-right: 2%}
    .fb {background:#2E64FE; margin-right:0}
    .kk:hover {background: #FFBF00}
    .nb:hover {background: #1bb100}
    .gg:hover {background: #FE2E2E}
    .fb:hover {background: #2E64FE}
</style>

<%@include file="include/footer.jsp"%>