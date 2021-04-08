<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../include/header.jsp"%>

<section id="banner" data-video="images/banner">
    <div class="inner">
        <h1>Food Travel</h1>
        <p>여러 사람들과 맛집 정보를 공유하는 음식 여행 지금 시작 해보세요!<br /></p>
        <p>거주지 정보를 입력해주세요.</p>

        <div style="display: flex">
            <input type="text" id="sample5_address" placeholder="주소" style="font-family: test_name" readonly>
            <a type="button" onclick="sample5_execDaumPostcode()" class="button special icon fa-search" style="margin-left: 4%; ">주소 검색</a><br>
        </div>

        <div id="map" style="width:100%;height:300px;margin-top:10px;display:none"></div>

        <br>
        <p style="font-size: 15px; margin-top: 4%; margin-bottom: 2%">거주지 정보는 맛집과의 길찾기를 위한 용도입니다.</p>
        <p style="font-size: 15px;">꼭! 거주지가 아니여도 괜찮습니다.</p>
        <a type="button" onclick="finish_signup()" class="button special" style="margin-top: 4%;">가입하기</a><br>
    </div>

</section>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=<%=key%>&libraries=services"></script>
<script>
    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new daum.maps.LatLng(37.537187, 127.005476), // 지도의 중심좌표
            level: 5 // 지도의 확대 레벨
        };

    //지도를 미리 생성
    var map = new daum.maps.Map(mapContainer, mapOption);
    //주소-좌표 변환 객체를 생성
    var geocoder = new daum.maps.services.Geocoder();
    //마커를 미리 생성
    var marker = new daum.maps.Marker({
        position: new daum.maps.LatLng(37.537187, 127.005476),
        map: map
    });

    var xpoint = "";
    var ypoint = "";
    var area = "";

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

                        xpoint = result.y;
                        ypoint = result.x;
                        area = addr;

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

    function finish_signup(){
        if ( area.length > 0 ){

            $.ajax({
                url: "/signUp/settings.do",
                type: "POST",
                cache: false,
                dataType: "json",
                data: {
                    xpoint : xpoint
                    , ypoint : ypoint
                    , area : area
                },
                success: function(data){
                    var result = data.result;

                    if ( result == "000"){
                        location.href = "/";
                    }else{
                        alert('가입에 실패하였습니다.');
                    }
                },
                error: function (request, status, error){
                }
            });

        }else {
            alert('거주지 정보를 입력해주세요.');
        }
    }

</script>
<%@include file="../include/footer.jsp"%>