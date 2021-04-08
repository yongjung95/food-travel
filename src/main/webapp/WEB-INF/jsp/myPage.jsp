<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="include/header.jsp"%>
<section id="banner" data-video="images/banner">
    <div class="inner">
        <h1>Food Travel</h1>
        <h2 style="color: #FFF;">MyPage</h2>
        <%
            if (loginUserName != null){
        %>
        <p>환영합니다! <%=loginUserName%> 님!!<br /></p>
        <a href="/logout" class="button special scrolly">Logout</a>
        <%
            }
        %>
    </div>
</section>

<section id="three" class="wrapper style2">
    <div class="inner">
        <div style="display:flex; margin: 0 auto;">
            <h2>닉네임</h2>
            <div style="display:flex; margin: 0 auto;" class="6u 12u$" >
                <input type="text" name="nickname" id="nickname" value="<%=sessionUser.getNickname()%>" placeholder="nickname" style="font-family: test_name" onkeyup="myPageNickNameCheck()" />
                <a id="nickNameCheck" class="button special icon fa-search" style="margin-left: 4%; background-color: #777777;" >중복 확인</a>
            </div>
        </div>

        <br/>
        <hr>
        <br/>

        <div style="display:flex;">
            <h2>거주지</h2>
            <div style="display:flex; margin: 0 auto;" class="6u 12u$" >
                <input type="text" id="sample5_address" placeholder="주소" value="<%=sessionUser.getArea()%>" style="font-family: test_name" readonly>
                <a type="button" onclick="sample5_execDaumPostcode()" class="button special icon fa-search" style="margin-left: 4%; ">주소 검색</a><br>

            </div>
        </div>

        <br/>

        <div style="display: flex">
            <div id="map" style="width:100%;height:300px;margin-top:10px;"></div>
        </div>

        <div class="grid-style">
            <header>
                <p>제일 먼저 게시글을 등록해보세요!!</p>
            </header>
        </div>

        <br/>
        <hr>
        <br/>

        <div class="row uniform">
            <!-- Break -->
            <div class="12u$">
                <input type="button" id = "myPageSave" value="저장 하기" style="background: #777777; float: right; font-family: test_name; margin-left: 2%;" onclick="myPageSave()">
                <input type="button" value="탈퇴 하기" style="background: #ec5757; float: right; font-family: test_name;" onclick="withdraw()">
            </div>
        </div>
    </div>
</section>

<script>

    var isChangeValue = false;

    var nickname = "<%=sessionUser.getNickname()%>";

    var xpoint = <%=sessionUser.getXpoint()%>;
    var ypoint = <%=sessionUser.getYpoint()%>;
    var area = "<%=sessionUser.getArea()%>";


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

    //주소-좌표 변환 객체를 생성
    var geocoder = new daum.maps.services.Geocoder();
    function sample5_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                var addr = data.address; // 최종 주소 변수

                // 주소 정보를 해당 필드에 넣는다.
                document.getElementById("sample5_address").value = addr;
                // 주소로 상세 정보를 검색
                geocoder.addressSearch(data.address, function(results, status) {
                    // 정상적으로 검색이 완료됐으면
                    if (status === daum.maps.services.Status.OK) {

                        var result = results[0]; //첫번째 결과의 값을 활용

                        // 해당 주소에 대한 좌표를 받아서
                        var coords = new daum.maps.LatLng(result.y, result.x);

                        changeXpoint = result.y;
                        changeYpoint = result.x;
                        changeArea = addr;

                        // 지도를 보여준다.
                        mapContainer.style.display = "block";
                        map.relayout();
                        // 지도 중심을 변경한다.
                        map.setCenter(coords);
                        // 마커를 결과값으로 받은 위치로 옮긴다.
                        marker.setPosition(coords)

                        if(changeArea === area){
                            isChangeValue = false;
                        }else{
                            isChangeValue = true;
                        }

                        changeValueCheck();
                    }
                });
            }
        }).open();
    }

    var type = "<%=sessionUser.getType()%>";
    function withdraw (){
        if ( confirm("정말 탈퇴하시겠습니까..?") ){
            $.ajax({
                url: "/signUp/delete",
                type: "DELETE",
                dataType: "json",
                data: {
                    type : type
                },
                success: function (data) {
                    var result = data.result;
                    if ( result === "000") {/**/
                        alert("탈퇴가 완료되었습니다.");
                        location.replace("/logout");
                    }
                    else if ( result === "010" ){
                        alert("회원 정보가 일치하지 않습니다.")
                    }

                },
                error: function (request, status, error) {

                }
            });
        }
    }

    var nickNameCheck = document.getElementById('nickNameCheck');
    var isNickNameCheck = false;

    var nickNameCheckFun = function (){
        var nickName = $("#nickname").val();

        if (nickName.length <= 0 ){
            alert('닉네임을 입력해주세요.');
        }else{
            $.ajax({
                url: "/signUp/nickNameCheck.do",
                type: "GET",
                cache: false,
                dataType: "json",
                data: {
                    nickname : nickname
                },
                success: function(data){
                    var result = data.result;

                    if ( result == "000"){
                        alert('사용가능한 닉네임입니다.');
                        isNickNameCheck=true;
                        $("#nickNameCheck").css('background','#777777');
                    }else{
                        alert('사용불가한 닉네임입니다.');
                        isNickNameCheck=false;
                    }

                    changeValueCheck();
                },
                error: function (request, status, error){
                }
            });
        }
    }



    nickNameCheck.addEventListener('click',nickNameCheckFun);

    function myPageNickNameCheck(){
        if ( nickname === $("#nickname").val() ){
            $("#nickNameCheck").css('background','#777777');
            nickNameCheck.removeEventListener('click', nickNameCheckFun);
            isNickNameCheck = false;
        }else{
            $("#nickNameCheck").css('background','#71b2f0');
            nickNameCheck.addEventListener('click',nickNameCheckFun);
            isNickNameCheck = false;
        }

        changeValueCheck();
    }

    function myPageSave(){
        $.ajax({
            url: "/user/update",
            type: "PUT",
            cache: false,
            dataType: "json",
            data: {
                nickname : $("#nickname").val(),
                xpoint : xpoint ,
                ypoint : ypoint ,
                area : area
            },
            success: function(data){
                var result = data.result;

                if ( result == "000"){
                    alert('수정되었습니다.');
                }else{
                    alert('에러발생입니다.');
                }
            },
            error: function (request, status, error){
            }
        });
    }

    function changeValueCheck(){

        if ( isChangeValue || isNickNameCheck){
            $("#myPageSave").css('background','#71b2f0');
        }else{
            $("#myPageSave").css('background','#777777');
        }
    }

</script>
<%@include file="include/footer.jsp"%>