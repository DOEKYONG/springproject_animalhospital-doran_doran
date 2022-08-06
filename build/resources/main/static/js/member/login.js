function enterkey() {
	if (window.event.keyCode == 13) {
	    let mid = $("#mid").val();
	    alert(mid);
	        if(mid == '') {
    	    alert("아이디를 입력해주세요");
    	    return;
    	    }
    	login();
    }
}

function login(){
    $.ajax({
        url: "/member/logincontroller",
        method: "POST",
        data : { "mid" : $("#mid").val()    , "mpassword" : $("#mpassword").val()   } ,
        success: function( re ){
                alert( re );
                if( re == true ){
                    alert("로그인성공");
                    location.href = "/"; // 메인페이지로 매핑
                }else{
                    alert("로그인실패");
                }
        }
    });
}