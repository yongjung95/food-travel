<%@ page import="com.food.foodtravel.domain.post.PostDto" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="include/header.jsp"%>
<%
    List<PostDto> postDtoList = (List) request.getAttribute("postDtoList");

%>

<section id="banner" data-video="images/banner" >
    <div class="inner">
        <h1>Food Travel</h1>
        <%
            if (loginUserName != null){
        %>
        <p>환영합니다! <%=loginUserName%> 님!!<br /></p>
        <a href="/logout" class="button special scrolly">Logout</a>
        <%
            }else {
        %>

        <p>여러 사람들과 맛집 정보를 공유하는 음식 여행 지금 시작 해보세요!</p>
        <br />
        <br />
        <a href="/loginPage" class="button special scrolly">Get Started</a>
        <%
            }
        %>
    </div>
</section>

<section id="three" class="wrapper style2">
    <div class="inner">
        <%
            if ( postDtoList == null ){
        %>
        <div class="grid-style">
            <header>
                <h2>작성된 글이 없습니다!</h2>
                <p>제일 먼저 게시글을 등록해보세요!!</p>
            </header>
        </div>
        <%
        } else {
        %>
        <div class="grid-style" id="postList">
            <%
                for ( PostDto postDto : postDtoList ){
            %>
            <div onclick="detail(<%=postDto.getPostId()%>)" style="cursor: pointer">
                <div class="box">
                    <div class="image fit">
                        <img src="<%=postDto.getImageDtoList().size() > 0 ? postDto.getImageDtoList().get(0).getImagePath() : "/images/pic01.jpg"%>" alt="" width="506" height="150.83" style="object-fit: scale-down">
                    </div>
                    <div class="content">
                        <header class="align-center">
                            <h2><%=postDto.getTitle()%></h2>
                            <p><%=postDto.getArea()%></p>
                        </header>
                        <hr>
                        <p style="overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;"> <%=postDto.getContent()%></p>
                    </div>
                </div>
            </div>
            <%
                }
            %>
        </div>
        <%
            }
        %>
    </div>
</section>
<script>

    function detail(postId){
        location.href="/post/detail/"+postId;
    }

</script>
<%@include file="include/footer.jsp"%>