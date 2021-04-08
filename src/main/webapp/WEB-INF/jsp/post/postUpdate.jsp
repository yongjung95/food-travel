<%@ page import="com.food.foodtravel.domain.post.PostDto" %>
<%@ page import="com.food.foodtravel.domain.image.ImageDto" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../include/header.jsp"%>
<%
    PostDto postDto = (PostDto) request.getAttribute("postDto");
%>
<section id="banner" data-video="images/banner">
    <div class="inner">
        <h1>Food Travel</h1>
        <h2 style="color: #FFF;">Post Update</h2>
    </div>
</section>

<section id="one" class="wrapper style2">
    <div class="inner">
        <form id="tmpSendFrm" method="post" enctype="multipart/form-data">
            <div class="row uniform">
                <div class="12u$" style="color: black">
                    <h4>제목을 입력해주세요!</h4>
                    <input type="hidden" name="postId" value="<%=postDto.getPostId()%>"/>
                    <input type="text" name="title" id="title" value="<%=postDto.getTitle()%>" placeholder="제목" STYLE="font-family: test_name">
                </div>

                <div class="12u$" style="color: black">
                    <h4>내용을 입력해주세요!</h4>
                    <textarea name="content" id="content" placeholder="내용을 입력해주세요!" rows="6"
                              STYLE="font-family: test_name"><%=postDto.getContent()%></textarea>
                </div>

                <ul class="12u$" style="color: black">
                    <h5 style="font-family: test_name">주소를 검색하시면 자동으로 지역이 저장됩니다.<br/>
                        음식점의 정확한 주소를 입력해주세요!</h5>

                    <a onclick="sample5_execDaumPostcode()" class="button" style="float: right; font-family: test_name">주소
                        검색</a>
                </ul>

                <div class="12u$">
                    <div id="map" style="width:100%;height:300px;margin-top:10px;"></div>
                    <input type="text" readonly name="area" id="area" value="<%=postDto.getArea()%>" style="margin-top: 2%; font-family: test_name">
                    <input type="hidden" name="local" id="local" value="<%=postDto.getLocal()%>"/>
                    <input type="hidden" name="xpoint" id="xpoint" value="<%=postDto.getXpoint()%>"/>
                    <input type="hidden" name="ypoint" id="ypoint" value="<%=postDto.getYpoint()%>" />
                </div>

                <div class="12u$">
                    <h4 style="font-family: test_name;">음식 사진이나 음식점 사진을 올려주세요! (최대 5개)</h4>
                    <div id="imageListDiv">
                    </div>
                    <%
                        for (ImageDto imageDto : postDto.getImageDtoList()){
                    %>
                    <div id="<%=imageDto.getId()%>" style="margin-top: 4%;">
                        <a href='#this' onclick='localImageDelete(<%=imageDto.getId()%>)'>삭제</a>
                        <span class="image fit"><img src="<%=imageDto.getImagePath()%>" alt=""></span>
                    </div>
                    <%
                        }
                    %>
                    <a class="button" onclick="file_add()" style="float: right;">사진 추가</a>
                    <br>
                    <div id="file-list">
                    </div>

                </div>

                <!-- Break -->
                <div class="12u$">
                    <input type="button" value="수정 하기" style="background: #5AA6ED; float: right; font-family: test_name" onclick="post_update()">
                </div>

            </div>
        </form>
    </div>
</section>

<script>
    // 카카오 지도
    var xpoint = <%=postDto.getXpoint()%>;
    var ypoint = <%=postDto.getYpoint()%>;

    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new daum.maps.LatLng(xpoint, ypoint), // 지도의 중심좌표
            level: 5 // 지도의 확대 레벨
        };

    //지도를 미리 생성
    var map = new daum.maps.Map(mapContainer, mapOption);
    //주소-좌표 변환 객체를 생성
    var geocoder = new daum.maps.services.Geocoder();
    //마커를 미리 생성
    var marker = new daum.maps.Marker({
        position: new daum.maps.LatLng(xpoint, ypoint),
        map: map
    });

    function sample5_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                var addr = data.address; // 최종 주소 변수

                // 주소 정보를 해당 필드에 넣는다.
                document.getElementById("area").value = addr;
                document.getElementById("area").style.display = "block";

                // 주소로 상세 정보를 검색
                geocoder.addressSearch(data.address, function(results, status) {
                    // 정상적으로 검색이 완료됐으면
                    if ( status === daum.maps.services.Status.OK ) {

                        var result = results[0]; //첫번째 결과의 값을 활용

                        // 해당 주소에 대한 좌표를 받아서
                        var coords = new daum.maps.LatLng(result.y, result.x);

                        xpoint = result.y;
                        ypoint = result.x;
                        var area = addr;

                        document.getElementById("xpoint").value = result.y;
                        document.getElementById("ypoint").value = result.x;
                        document.getElementById("local").value = area.substr(0,2);

                        // 지도를 보여준다.
                        mapContainer.style.display = "block";
                        map.relayout();
                        // 지도 중심을 변경한다.
                        map.setCenter(coords);
                        // 마커를 결과값으로 받은 위치로 옮긴다.
                        marker.setPosition(coords)
                    }
                });
            }
        }).open();
    }
    // 카카오 지도

    var fileCount = <%=postDto.getImageDtoList().size()%>; // 파일 갯수
    var fileImageCheck = true; // 파일 형식을 이미지만 사용했는지.

    function file_add(){
        if ( fileCount < 5 ){
            fileCount ++;
            var str = "<div id='file"+fileCount+"' style='margin-top: 4%'><input type='file' id='file"+ fileCount+ "' name='fileList' class='upload-hidden'>" +
                "<a href='#this' id='file"+fileCount+"' name='file-delete' onclick='newImageDelete()'>삭제</a><span class='image fit' style='margin-top: 4%;'><img id='file"+fileCount+"' src='' style='display:none'></span></div>";

            $("#file-list").append(str);

            $("a[name='file-delete']").on("click", function(e) {
                var id =$(this).attr('id');
                $("div[id='"+id+"']").remove();
            });

            var imgTarget = $('.upload-hidden');
            imgTarget.on('change',function (){
                if( window.FileReader ){ //image 파일만
                    var id =$(this).attr('id');

                    if (!$(this)[0].files[0].type.match('image.*')) {
                        alert("이미지 파일만 업로드 가능합니다.");
                        fileImageCheck = false;

                        return;
                    }else{
                        if( this.files && this.files[0] ) {
                            var reader = new FileReader;
                            reader.onload = function(data) {
                                $("img[id='"+id+"']").attr("src", data.target.result).css("display","block");
                            }
                            reader.readAsDataURL(this.files[0]);
                        }
                    }
                }
            });
        }
    }

    function localImageDelete(postId){
        fileCount--;
        console.log(fileCount);
        var str = "<input type='hidden' name='deleteImage' value='"+postId+"'>";
        $("#imageListDiv").append(str);
        $("div[id='"+postId+"']").remove();
    }

    function newImageDelete(){
        fileCount--;
    }

    function post_update(){

        var title = document.getElementById("title").value;
        var content = document.getElementById("content").value;
        var area = document.getElementById("area").value;

        if ( fileCount === 0 ){
            fileImageCheck = true;
        }

        if ( title.length <= 0 ){
            alert('제목을 입력해주세요.');

            return false;
        }

        if ( content.length <= 0 ){
            alert('내용을 입력해주세요.');

            return false;
        }

        if ( area.length <= 0 ){
            alert('주소를 입력해주세요.');

            return false;
        }

        if ( !fileImageCheck ){
            alert('이미지 파일만 업로드 가능합니다.');

            return false;
        }

        if ( area.length > 0 ){
            var form = $('#tmpSendFrm')[0];

            var formData = new FormData(form);

            $.ajax({
                url: "/post/update.do",
                enctype: "multipart/form-data",
                type: "PUT",
                cache: false,
                dataType: "json",
                /*
                     ajax로 파일 업로드할 때는 contentType 과 processData 를 꼭 써줘야한다.
                 */
                contentType: false,
                processData: false,
                data: formData,
                success: function(data){
                    var result = data.result;

                    if ( result == "000" ){
                        alert("정상적으로 수정되었습니다.");
                        location.replace("/postList");
                    }else{
                        alert('정상적으로 수정되지않았습니다.');
                    }
                },
                error: function (request, status, error){
                }
            });

        }else {
            alert('주소 정보를 입력해주세요.');
        }
    }
</script>
<%@include file="../include/footer.jsp"%>