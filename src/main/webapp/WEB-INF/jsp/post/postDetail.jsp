<%@ page import="com.food.foodtravel.domain.post.PostDto" %>
<%@ page import="com.food.foodtravel.domain.image.ImageDto" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../include/header.jsp"%>
<%
    PostDto postDto = (PostDto) request.getAttribute("postDto");

    Boolean favoriteCheck = (boolean) request.getAttribute("favoriteCheck");

%>
<section id="banner" data-video="images/banner">
    <div class="inner">
        <h1>Food Travel</h1>
        <h2 style="color: #FFF;">Post Detail</h2>
    </div>
</section>

<section id="one" class="wrapper style2">
    <div class="inner">
        <div class="row uniform">
            <h4>제목</h4>
            <div class="12u$" style="color: black">
                <input readonly type="text" name="title" id="title" value="<%=postDto.getTitle()%>" placeholder="제목"
                       STYLE="font-family: test_name">
            </div>

            <h4>내용</h4>
            <div class="12u$" style="color: black">
                    <textarea readonly name="content" id="content" placeholder="내용을 입력해주세요!" rows="6"
                              STYLE="font-family: test_name"><%=postDto.getContent()%></textarea>
            </div>
            <%
                for (ImageDto imageDto : postDto.getImageDtoList()) {
            %>
            <h4>사진</h4>
            <span class="image fit"><img src="<%=imageDto.getImagePath()%>" alt=""></span>
            <%
                }
            %>
            <h4>위치</h4>

            <div class="12u$">
                <div id="map" style="width:100%;height:300px;margin-top:10px;"></div>
                <input type="text" readonly name="area" id="area" style="margin-top: 2%; font-family: test_name"
                       value="<%=postDto.getArea()%>">
                <input type="hidden" name="local" id="local"/>
                <input type="hidden" name="xpoint" id="xpoint"/>
                <input type="hidden" name="ypoint" id="ypoint"/>
            </div>

            <!-- Break -->
            <div class="12u$">
                <%
                    if (sessionUser.getUserId().equals(postDto.getUserDto().getUserId())){
                %>
                <input type="button" value="삭제하기" style="margin-left: 2%; background: #ed5a5a; float: right; font-family: test_name"
                       onclick="postDelete(<%=postDto.getPostId()%>)">
                <input type="button" value="수정하기" style="margin-left: 2%; background: #5AA6ED; float: right; font-family: test_name"
                       onclick="postUpdate(<%=postDto.getPostId()%>)">
                <%
                    }
                %>
                <a href="/postList" class="button" style="float: right;margin-left: 2%;">목록으로</a>
                <input type="button" value="길찾기" style="background: #935aed; float: right; font-family: test_name" onclick="findMap()">
                <a id="favorites" class="button alt icon fa-check" style="padding-right: 1.1rem;" onclick="Favorite(<%=postDto.getPostId()%>)">즐겨찾기</a>
            </div>
        </div>
    </div>
</section>

<script>

    var favoriteCheck = <%=favoriteCheck%>

    if ( favoriteCheck ){
        document.getElementById('favorites').style.backgroundColor = '#d6fbdc';
    }

    var xpoint = <%=postDto.getXpoint()%>;
    var ypoint = <%=postDto.getYpoint()%>;
    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new kakao.maps.LatLng(xpoint, ypoint), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨
        };

    var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

    // 마커가 표시될 위치입니다
    var markerPosition  = new kakao.maps.LatLng(xpoint, ypoint);

    // 마커를 생성합니다
    var marker = new kakao.maps.Marker({
        position: markerPosition
    });

    // 마커가 지도 위에 표시되도록 설정합니다
    marker.setMap(map);

    function postUpdate(id){
        location.href="/post/update/"+id;
    }

    function postDelete(id){
        if ( confirm("정말 삭제하시겠습니까??") ){
            $.ajax({
                url:"/post/delete.do",
                type:"DELETE",
                data:{
                    id:id
                },
                success: function(data){
                    location.replace("/postList")
                },
                error: function(request, status, error){

                }
            });
        }
    }

    function Favorite(id){
        $.ajax({
            url: "/post/Favorite.do",
            type: "PUT",
            data: {
                id: id
            },
            success: function (data) {
                var result = data.result;
                if ( result === "000" ){
                    alert('즐겨찾기에 추가되었습니다.');
                    document.getElementById('favorites').style.backgroundColor = '#d6fbdc';
                } else if ( result === "200" ){
                    alert('즐겨찾기에 제거되었습니다.');
                    document.getElementById('favorites').style.backgroundColor = 'transparent';
                }
            },
            error: function (request, status, error) {

            }
        });
    }

    function findMap(){
        window.open('https://map.kakao.com/?sName=<%=sessionUser.getArea()%>&eName=<%=postDto.getArea()%>');
    }
</script>

<%@include file="../include/footer.jsp"%>