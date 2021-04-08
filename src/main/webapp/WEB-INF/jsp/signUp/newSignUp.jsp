<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../include/header.jsp"%>


<section id="banner" data-video="images/banner">
    <div class="inner">
        <h1>Food Travel</h1>
        <p>여러 사람들과 맛집 정보를 공유하는 음식 여행 지금 시작 해보세요!<br /></p>
        <p>추가 정보를 입력해주세요.</p>

        <div>
            <h2 style="color:rgba(255, 255, 255, 0.65)">닉네임</h2>
            <div style="display:flex; margin: 0 auto;" class="6u 12u$(xsmall)" >
                <input type="text" name="nickname" id="nickname" value="" placeholder="nickname" style="font-family: test_name" />
                <a id="nickNameCheck" class="button special icon fa-search" style="margin-left: 4%;">중복 확인</a>
            </div>
        </div>

        <div>
            <h2 style="color:rgba(255, 255, 255, 0.65); margin-top: 4%;">전화번호</h2>

            <div style="display:flex; margin: 0 auto;" class="6u 12u$(xsmall)">
                <input type="text" name="phoneNumber" id="phoneNumber" maxlength="13" placeholder="phoneNumber" style="font-family: test_name" />
                <a id="phoneNumberCheck" class="button special icon fa-search" style="margin-left: 4%;">가입 확인</a>
            </div>
        </div>
        <br/>
        <p style="font-size: 15px; margin-top: 4%; margin-bottom: 2%">전화번호는 중복 가입여부를 확인하기 위한 용도입니다.</p>
        <p style="font-size: 15px;">(개인정보는 암호화 후 보관됩니다.)</p>

        <a id="signUp" class="button special" style="margin-top: 4%;">가입하기</a>
    </div>

</section>
<script type="text/javascript">
    var nickNameCheck = false;
    var phoneNumberCheck = false;

    var autoHypenPhone = function(str){
        str = str.replace(/[^0-9]/g, '');
        var tmp = '';

        if( str.length < 4){

            return str;

        }else if(str.length < 7){
            tmp += str.substr(0, 3);
            tmp += '-';
            tmp += str.substr(3);

            return tmp;

        }else if(str.length < 11){
            tmp += str.substr(0, 3);
            tmp += '-';
            tmp += str.substr(3, 3);
            tmp += '-';
            tmp += str.substr(6);

            return tmp;

        }else{
            tmp += str.substr(0, 3);
            tmp += '-';
            tmp += str.substr(3, 4);
            tmp += '-';
            tmp += str.substr(7);

            return tmp;
        }

        return str;
    }

    var phoneNumber = document.getElementById('phoneNumber');

    phoneNumber.onkeyup = function(){
        phoneNumberCheck = false;
        this.value = autoHypenPhone( this.value ) ;
    }

    $(document).ready(function () {
        $("#nickname").on('keypress',function (){
            nickNameCheck = false;
        });

        $("#nickNameCheck").click(function (){
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
                        nickname : nickName
                    },
                    success: function(data){
                        var result = data.result;

                        if ( result == "000"){
                            alert('사용가능한 닉네임입니다.');
                            nickNameCheck=true;
                        }else{
                            alert('사용불가한 닉네임입니다.');
                            nickNameCheck=false;
                        }
                    },
                    error: function (request, status, error){
                    }
                });
            }

        });

        $("#phoneNumberCheck").click(function (){
            var phoneNumber = $("#phoneNumber").val();

            if (phoneNumber.length <= 0 || phoneNumber.length <= 11 ){
                alert('전화번호를 입력해주세요.');
            }else{
                $.ajax({
                    url: "/signUp/phoneNumberCheck.do",
                    type: "GET",
                    cache: false,
                    dataType: "json",
                    data: {
                        phoneNumber : phoneNumber
                    },
                    success: function(data){
                        var result = data.result;

                        if ( result == "000"){
                            alert('사용가능한 전화번호 입니다.');
                            phoneNumberCheck=true;
                        }else{
                            alert('사용불가한 전화번호 입니다');
                            phoneNumberCheck=false;
                        }
                    },
                    error: function (request, status, error){
                    }
                });
            }

        });

        $("#signUp").click( function (){
            var nickName = $("#nickname").val();
            var phoneNumber = $("#phoneNumber").val();

            if ( !nickNameCheck ){
                alert('닉네임 중복 확인을 해주세요.');
            }else if ( !phoneNumberCheck ){
                alert('전화번호 중복 확인을 해주세요.');
            }else{
                $.ajax({
                    url: "/signUp/signUp.do",
                    type: "POST",
                    cache: false,
                    dataType: "json",
                    data: {
                        nickname : nickName
                        ,phoneNumber : phoneNumber
                    },
                    success: function(data){
                        var result = data.result;

                        if ( result == "000"){
                            location.href = "/signUp/settings";
                        }else{
                            alert('이미 가입된 계정입니다.');
                        }
                    },
                    error: function (request, status, error){
                    }
                });
            }

        });
    });

</script>

<%@include file="../include/footer.jsp"%>