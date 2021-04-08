<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.ResourceBundle"%>
<%@ page import="com.food.foodtravel.domain.user.UserDto" %>
<%
    UserDto sessionUser = (UserDto) request.getAttribute("sessionUser");
    String loginUserName = null;
    if (sessionUser != null ) {
        loginUserName = sessionUser.getNickname();
    }

    ResourceBundle resourceBundle = ResourceBundle.getBundle("application");

    String key = resourceBundle.getString("kakaomap.api");
%>
<html>

<head>
    <title>맛있는 여행</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="/assets/css/main.css">
</head>

<style>
    @font-face {
        font-family: 'test_name';
        font-style: normal;
        font-weight: 800;
        src: url('/font/MaplestoryLight.ttf') format('truetype');
    }
</style>

<body style="font-family: test_name">

<!-- Header -->
<header id="header" class="alt">
    <div class="logo"><a href="/">Transitive <span>by TEMPLATED</span></a></div>
    <a href="#menu" class="toggle"><span>Menu</span></a>
</header>

<!-- Nav -->
<nav id="menu">
    <ul class="links">
        <li>
            <% if (loginUserName != null) {%>
            <p style="color: white">환영합니다! <%=loginUserName%> 님!!<br /></p>
            <a href="/logout" class="button special scrolly">Logout</a>
            <%} else{%>
            <a href="/loginPage" class="button special scrolly">Login</a>
            <%}%>
        </li>
        <li><a href="/">Home</a></li>
        <% if (loginUserName != null) {%>
        <li><a href="/myPage">MyPage</a></li>
        <li><a href="/myFavoriteList">Favorite</a></li>
        <li><a href="/postList">Post</a></li>
        <%}%>

    </ul>
</nav>

<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=<%=key%>&libraries=services"></script>