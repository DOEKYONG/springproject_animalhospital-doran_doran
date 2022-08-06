
let today = new Date();

let year = today.getFullYear()+"";
let month = today.getMonth() + 1+"";
if(month < 10){
    month = 0 + month;
}
let date = today.getDate()+"";
let gettoday = year + '-' + month + '-' + date;
console.log(gettoday);
let msg;

function gettomsglist(type){
alert(type);
    $.ajax({
        url: '/member/gettomsglist',
        data :{"type" : type},
        success: function(object){
              console.log(object);
            msg = object;
            let html =
                '<tr>'+
                    '<th>선택</th><th>보낸사람</th><th>내용</th><th>받은 날짜/시간</th>'+
                '</tr>';
            if(object.length == 0){
                html +=
                    '<tr>'+
                        '<th colspan="4" class="text-center">받은 쪽지가 없습니다.</th>'+
                    '</tr>';
            }
            for(let i=0; i<object.length; i++){
                 let color ="black";
                 if(object[i].isread == true){
                     color = "#d3d3d3";
                 }
                let msgtitle ="";
                if(object[i].msg.length > 20 ){
                   msgtitle = object[i].msg.substr(0, 20)+"...";
                }else{
                    msgtitle = object[i].msg;
                }
                let mdate ="";
                let senddate = object[i].date.substr(0, 10);
                if(senddate == gettoday){
                    mdate = object[i].date.substr(11, 18)
                }else{
                    mdate = senddate;
                }
                html +=
                    '<tr style="color: '+color+'">'+
                        '<td><input name="checkbox" type="checkbox" value="'+object[i].msgno+'" onclick="oncheckbox()"></td><td>'+object[i].from+'</td><td onclick="tomsgread('+i+')" data-bs-toggle="modal" data-bs-target="#exampleModal" href="#exampleModal">'+msgtitle+'</td><td>'+mdate+'</td>'+
                    '</tr>';
            }
        $(".msgtable").html(html);
        }
    });
}

function getfrommsglist(type){
alert(type);
    $.ajax({
        url: '/member/getfrommsglist',
        data :{"type" : type},
        success: function(object){
            console.log(object);
            msg = object;
            let html =
                    '<tr>'+
                        '<th>선택</th><th>받은사람</th><th>내용</th><th>보낸 날짜/시간</th>'+
                    '</tr>';
            if(object.length == 0){
             html +=
             '<tr>'+
             '<th colspan="4" class="text-center">받은 쪽지가 없습니다.</th>'+
             '</tr>';
            }
            for(let i=0; i<object.length; i++){
                let msgtitle ="";
                if(object[i].msg.length > 20 ){
                   msgtitle = object[i].msg.substr(0, 20)+"...";
                }else{
                    msgtitle = object[i].msg;
                }
                let mdate ="";
                let senddate = object[i].date.substr(0, 10);
                console.log(senddate);
                if(senddate == gettoday){
                    mdate = object[i].date.substr(11, 18);
                }else{
                    mdate = senddate;
                }
                html +=
                    '<tr>'+
                        '<td><input name="checkbox" type="checkbox" value="'+object[i].msgno+'" onclick="oncheckbox()"></td><td>'+object[i].to+'</td><td onclick="frommsgread('+i+')" data-bs-toggle="modal" data-bs-target="#exampleModal2">'+msgtitle+'</td><td>'+mdate+'</td>'+
                    '</tr>';
            }
            $(".msgtable").html(html);
        }
    });
}

let fromid = "";
function tomsgread( i ){
    fromid = msg[i].from;


    let html =
        '<div><span>보낸사람</span><span> '+msg[i].from+'</span></div>'+
        '<div><span>받은시간</span><span> '+msg[i].date+'</span></div>'+
        '<hr>'+
        '<div>'+msg[i].msg+'</div>';

    $("#msgcontent").html(html);
    isread(msg[i].msgno);
}

function frommsgread( i ){
    let html =
        '<div><span>받은사람</span><span> '+msg[i].from+'</span></div>'+
        '<div><span>보낸시간</span><span> '+msg[i].date+'</span></div>'+
        '<hr>'+
        '<div>'+msg[i].msg+'</div>';

    $("#msgcontent2").html(html);
}

let mid = "";
$.ajax({
    url: '/member/getmid',
    success: function(result){
        mid = result+"";
    }
});

function isread(msgno){
    $.ajax({
        url:"/member/isread",
        method:"PUT",
        data:{"msgno":msgno },
        success: function(object){
            getisread();
            gettomsglist();
        }
    });
}

let deletelist = [];
function oncheckbox(){
    let checkbox = $("input[name='checkbox']");
    deletelist = [];
    for(let i = 0; i < checkbox.length; i++){
        if(checkbox[i].checked == true){
            deletelist.push(checkbox[i].value);
        }
    }
    console.log(deletelist);
}


function msgdelete(){
    $.ajax({
        url: "/member/msgdelete",
        data : JSON.stringify(deletelist),
        method : "DELETE",
        contentType : "application/json",
        success: function(object){
            if(object){
                alert("삭제 성공");
                 $(".msgtable").load(window.location.href + ".msgtable");
                deletelist=[];
                gettomsglist(1);
            }
        }
    });
}
let type;
$(document).ready(function(){
if(typeof type == 'undefined' ) {type = 1;}
gettomsglist(type);

$("#v-pills-home-tab").click(function(){
    gettomsglist(1); type = 1;
});
$("#v-pills-profile-tab").click(function(){
    gettomsglist(2); type = 2;
});

     $("#answer").click(function(){
     if(typeof type == 'undefined' ) {type = 1;}
     console.log(type);
     alert(type);
        let msg = $("#msginput").val();
        mid = mid.replace(/\n|\r|\s*/g, "");
        let to = fromid;
        let jsonmsg = {
            "from" : mid,
            "to" : to ,
            "msg" : msg,
            "type" : type
        }
        console.log(jsonmsg);
        send(jsonmsg);
        msg = "";
        $(".btn-close").trigger("click");
    });

    let msgwebsocket = new WebSocket("ws://ec2-43-200-181-29.ap-northeast-2.compute.amazonaws.com/ws/answer/"+mid);


   msgwebsocket.onopen = onOpen;
   msgwebsocket.onclose = onClose;
   msgwebsocket.onmessage = onMessage;

   function onOpen(){}

   function onClose(){}

   function onMessage(){}

   function send(jsonmsg){
        msgwebsocket.send(JSON.stringify(jsonmsg));
   }

});