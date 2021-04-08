<%@ page import="com.food.foodtravel.domain.post.PostDto" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../include/header.jsp"%>
<%
    List<PostDto> postDtoList = (List) request.getAttribute("postDtoList");

%>
<section id="banner" data-video="images/banner">
    <div class="inner">
        <h1>Food Travel</h1>
        <h2 style="color: #FFF;">Post List</h2>
        <%
            if (loginUserName != null){
        %>
        <p>환영합니다! <%=loginUserName%> 님!!<br /></p>
        <a href="/logout" class="button special scrolly">Logout</a>
        <a href="/postAdd" class="button alt" style="margin-top: 5%; background-color: #5aedac;">글 등록하기</a>
        <%
            }else {
        %>
        <p>여러 사람들과 맛집 정보를 공유하는 음식 여행 지금 시작 해보세요! <br />
        <a href="/loginPage" class="button special scrolly">Get Started</a>
        <% } %>
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
        <div class="row uniform">
            <div class="9u 12u$(small)">
                <input type="text" name="query" id="searchKeyword" value="" placeholder="검색" style="font-family: test_name">
            </div>
            <div class="3u$ 12u$(small)">
                <input type="button" onclick="search()" value="검색" class="fit" style="font-family: test_name">
            </div>
        </div>

        <div class="select-wrapper" style=" float: right;width: 30%; margin-bottom: 5%; margin-top: 5%">
            <select name="demo-category" id="demo-category" style="font-family: test_name" onchange="chageLangSelect()">
                <option value="createdDateAsc">정렬</option>
                <option value="createdDate">최신순</option>
                <option value="hit">조회순</option>
            </select>
        </div>

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
    var selectValue = "";
    var searchKeyword = "";
    var page = 1;
    var totalPage = 0;
    var pagingLast = false;
    var pageOrber = "desc";

    function detail(postId){
        location.href="/post/detail/"+postId;
    }

    function chageLangSelect(){
        var langSelect = document.getElementById("demo-category");

        // select element에서 선택된 option의 value가 저장된다.
        selectValue = langSelect.options[langSelect.selectedIndex].value;

        $("div[id='postList']").empty();

        pagingLast = false;
        page = 1;
        $(window).scrollTop(350);

        if ( selectValue == 'createdDateAsc'){
            console.log(selectValue);
            selectValue = 'createdDate';
            pageOrber = 'asc';

            $.ajax({
                url:"/post/searchList?sort="+selectValue+","+pageOrber,
                type: "GET",
                dataType: "json",
                data : {
                    page : 0,
                    searchKeyword : searchKeyword
                },
                success: function(data){
                    var postDtoList = data.postDtoList;

                    addList(postDtoList);

                },
                error: function (request, status, error){

                }
            });

        }else{
            pageOrber = "desc";
            $.ajax({
                url:"/post/searchList?sort="+selectValue+","+pageOrber,
                type: "GET",
                dataType: "json",
                data : {
                    //page : 0,
                    searchKeyword : searchKeyword
                },
                success: function(data){
                    var postDtoList = data.postDtoList;

                    addList(postDtoList);

                },
                error: function (request, status, error){

                }
            });
        }
    }

    function search(){
        selectValue = "createdDate";
        pageOrber = "asc";
        document.getElementById("demo-category").value = "createdDateAsc";

        searchKeyword = document.getElementById("searchKeyword").value;
        $("div[id='postList']").empty();

        pagingLast = false;
        page = 1;

        $(window).scrollTop(350);

        $.ajax({
            url:"/post/searchList?sort="+selectValue+","+pageOrber,
            type: "GET",
            dataType: "json",
            data : {
                searchKeyword : searchKeyword
            },
            success: function(data){
                var postDtoList = data.postDtoList;

                addList(postDtoList);

            },
            error: function (request, status, error){

            }
        });

    }

    $(window).scroll(function () {   //스크롤이 최하단 으로 내려가면 리스트를 조회하고 page를 증가시킨다.

        if ($(window).scrollTop() + $('#banner').height() == $(window).height()) {
            console.log('여기');
            if (!pagingLast) {
                if (selectValue == 'hit') {
                    $.ajax({
                        url: "/post/paging?sort=" + selectValue + ",createdDate," + pageOrber,
                        type: "GET",
                        dataType: "json",
                        data: {
                            page: page,
                            searchKeyword: searchKeyword
                        },
                        success: function (data) {
                            var postDtoList = data.postDtoList;
                            var isLast = data.isLast;

                            if (!isLast) {
                                //page++;
                            } else {
                                pagingLast = true;
                            }

                            addList(postDtoList);
                            page++;

                        },
                        error: function (request, status, error) {

                        }
                    });
                } else {
                    $.ajax({
                        url: "/post/paging?sort=" + selectValue + "," + pageOrber,
                        type: "GET",
                        dataType: "json",
                        data: {
                            page: page,
                            searchKeyword: searchKeyword
                        },
                        success: function (data) {
                            var postDtoList = data.postDtoList;
                            var isLast = data.isLast;

                            if (!isLast) {
                                //page++;
                            } else {
                                pagingLast = true;
                            }

                            addList(postDtoList);
                            page++;

                        },
                        error: function (request, status, error) {

                        }
                    });
                }
            }
        }
    });

    function addList(postDtoList){
        for (var i = 0; i < postDtoList.length; i++) {
            var image = "/images/pic01.jpg";

            if (postDtoList[i].imageDtoList.length > 0) {
                image = postDtoList[i].imageDtoList[0].imagePath;
            }

            var tag = "<div onclick='detail(" + postDtoList[i].postId + ")' style='cursor: pointer'>"
                + "<div class='box'>"
                + "<div class='image fit'>"
                + "<img src='" + image + "' alt='' width='506' height='150.83' style='object-fit: scale-down'>"
                + "</div>"
                + "<div class='content'>"
                + "<header class='align-center'>"
                + "<h2>" + postDtoList[i].title + "</h2>"
                + "<p>" + postDtoList[i].area + "</p>"
                + "</header>"
                + "<hr>"
                + "<p style='overflow:hidden; text-overflow: ellipsis; white-space: nowrap;'>" + postDtoList[i].content + "</p>"
                + "</div>"
                + "</div>"
                + "</div>";

            $("div[id='postList']").append(tag);

        }
    }

</script>
<%@include file="../include/footer.jsp"%>