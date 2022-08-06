var sno = getParameterByName('sno');
getitem(sno);
likecheck();
let pass;
let seller;
function getitem(sno) {
$.ajax({
url : '/member/getitem',
data : {"sno" : sno},
success : function(re) {
    seller = re.mid;
    console.log(re);
    let html = '';
   html += '<div id ="img_wrap"> <img id="viewimg" src="/upload/'+re.bimglist+'"></div>';
        html += '<div class="user_wrap"><span><i class="fa-solid fa-shield-cat"></i></span><span>'+re.mid+'</span> </div>';
        html += '<div class="title"><span>상품 이름 </span><span>'+re.btitle+'</span></div>';
          if(! re.status) {
            html += '<div class="state"> 판매 중</div>';
            }else {
                html += '<div class="state"> 판매 완료</div>';
            }
       html += '<div class="price"><span>'+re.price+'원</span></div>';

         html += '<div class="content_wrap"><div class="content">'+re.bcontent+'</div></div>';
    $("#itembox").html(html);
}

});
}

function getParameterByName(sno) {
    sno = sno.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + sno + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function likesave() {

$.ajax({
    url : '/member/likesave',
    data : {"sno" : sno},
    success : function(re) {
    if(re == 1) {
    alert("관심 상품 추가");
    likecheck();
    }
    else if(re == 2){
    alert("관심 상품 삭제");
    likecheck();
    }
    else if(re == 3){
    alert("로그인 후에 관심 상품 기능 이용이 가능합니다.");
    }
    else if(re == 4){
    alert("프로그램 오류 : 관리자에게 문의");
    }
    }
});
}
function likecheck() {
$.ajax({
    url : '/member/likecheck',
    data : {"sno" : sno},
    success : function(re) {
    if(re == 1) {
    $("#likesave").css("display", "none");
    $("#unlikesave").css("display", "block");
    }
    else if(re == 2){
    $("#likesave").css("display", "block");
    $("#unlikesave").css("display", "none");
    }
    else if(re == 3 || re == 4){ }
    }
});

}

function itemdelete() {

if(confirm("정말 상품을 삭제하시겠습니까?")) {

alert(sno+"번 상품을 삭제합니다");
$.ajax({
            url : '/member/deleteitem',
            type: "DELETE",
            data : {"sno" : sno } ,
            success : function(re) {
            console.log(re);
           if(re == true) {alert("삭제 성공"); location.href = '/member/shop';}
           else { alert("삭제 실패");}
           }
    });
}


}

function itemupdate() {

$("#sno").val(sno);


let form = $("#saveform")[0]; // [0] : 폼내 입력 데이터 //
    //[0]을 안하면 데이터 + 설정값까지 같이 보내지게 됨
    let formData = new FormData(form);
console.log(formData);
$.ajax({
            url : '/member/itemupdate',
            type: "PUT",
            data : formData,
            contentType : false, // 첨부파일 전송시 사용되는 속성
            processData : false,
            success : function(re) {
            console.log(re);
           if(re == true) {alert("수정 성공");
                    $("#stitle").val('')
                    $("#scontent").val('')
                    $("#sprcie").val('')
                    $("#simg").val('')
            getitem(sno);}
           else { alert("수정 실패");}
           }
    });

}

function idcheck(sno) {

$.ajax({
            url : '/member/idcheck',
            data : {"sno" : sno } ,
            async : false,
            success : function(re) {
            console.log(re);
           if(re == 1) {
           $("#itemupdate").css("display", "block");
           $("#itemdelete").css("display", "block");
           pass = true;
           }
           else if(re == 2) {
            $("#itemupdate").css("display", "none");
            $("#itemdelete").css("display", "none");
            pass = true;
           }
           else if(re == 3 || re == 4) {pass = false;}
           }
    });
}

$(document).ready(function(){

idcheck(sno);

      let mid ="";
        $.ajax({
            url: '/member/getmid',
            async : false,
            success: function(result){
                mid= result;
                console.log(mid);
            }
        });

           $("#sendmsg").click(function(){
             if(pass) {
                   let msg = $("#msg").val();
                   if(msg == '') {
                   alert("메시지 내용을 입력해주세요");
                   return;
                   }
                      let from = mid.replace(/\n|\r|\s*/g, "");
                      let to = seller;
                      alert(from);
                      let jsonmsg = {
                      "from" : from,
                      "to" : to ,
                      "msg" :msg,
                      "type" : 1
                      }
                      console.log(jsonmsg);
                      send(jsonmsg);
                      msg = "";
                   }
                   else {
                   alert("로그인 후에 이용이 가능합니다."); return;
                   }
            });

            let msgwebsocket = new WebSocket("ws://ec2-43-200-181-29.ap-northeast-2.compute.amazonaws.com/ws/seller/"+mid);

            msgwebsocket.onopen = onOpen;
            msgwebsocket.onclose = onClose;
            msgwebsocket.onmessage = onMessage;

             function onOpen() { }
             function onClose() { }
             function onMessage() { }
             function send(jsonmsg){  msgwebsocket.send(JSON.stringify(jsonmsg));  }
    });

