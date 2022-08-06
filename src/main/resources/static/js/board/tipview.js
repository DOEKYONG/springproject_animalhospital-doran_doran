gettip();

function gettip(){
            $.ajax({
                url : "/board/gettipboard" ,
                method : "GET",
                success: function( board ){
                    let imgtag = "";
                     getreply(board.bno);
                    console.log( board );
                    for( let i = 0 ; i<board.bimglist.length ; i++ ){
                         if( i == 0 ){  // 첫번째 이미지만 active 속성 추가
                            imgtag +=
                                         '<div class="carousel-item active">'+
                                             '<img src="/upload/'+board.bimglist[i]+'" class="d-block w-100 tipimgh" alt="...">'+
                                        '</div>';
                         }else{
                            imgtag +=
                                     '<div class="carousel-item">'+
                                         '<img src="/upload/'+board.bimglist[i]+'" class="d-block w-100 tipimgh" alt="...">'+
                                    '</div>';
                         }

                    }
                     if(board.same=="true"){
                             $("#deletebutton1").html(
                             '<input type="hidden" id="bno" value="'+board.bno+'">'+
                             '<button type="button" class="btn btn-primary myreplyupdate" onclick="bdelete('+board.bno+')">삭제</button>'+
                             '<button type="button" class="btn btn-primary myreplyupdate" onclick="bupdate('+board.bno+')"   data-bs-toggle="modal" data-bs-target="#myModal">수정</button>'
                          );

                    }
                    $("#bwiter").html( board.mid );
                     $("#getbtitle").html( board.btitle );
                     $("#getbcontent").html( board.bcontent );
                    $("#getbimg").html( imgtag );
                    $("#bdate").html(  board.modifiedate.substring(0, 10));
                }
            });

}

function bdelete(bno){
      $.ajax({
                 url : "/board/bdelete" ,
                 method : "Delete",
                 data : { "bno" : bno } ,
                 success: function( board ){
                    alert("삭제완료");
                      location.href = "/board/tiplist";
                 }
        });
}

function bupdate(bno){
    $.ajax({
                    url : "/board/gettipboard" ,
                    method : "GET",
                    success: function( board ){
                        let imgtag = "";
                        console.log( board );
                        for( let i = 0 ; i<board.bimglist.length ; i++ ){
                             if( i == 0 ){  // 첫번째 이미지만 active 속성 추가
                                imgtag +=
                                             '<div class="carousel-item active">'+
                                                 '<img id="img_preview" src="/upload/'+board.bimglist[i]+'" class="d-block w-100" alt="...">'+
                                            '</div>';
                             }else{
                                imgtag +=
                                         '<div class="carousel-item">'+
                                             '<img id="img_preview" src="/upload/'+board.bimglist[i]+'" class="d-block w-100" alt="...">'+
                                        '</div>';
                             }
                        }
                         $("#btitle").val( board.btitle );
                         $("#bcontent").html( board.bcontent );
                        $("#preview").html( imgtag );
                    }
                });
}

function tipupdate(){
    let form = $("#saveform")[0];
            let formdata = new FormData( form);
            $.ajax({
                url: "/board/tipupdate",
                method: "PUT",
                data : formdata ,
                contentType: false,
                processData: false ,
                success: function( re ){
                    if(re){
                    location.reload();
                    }else{
                        alert("로그인 후 이용해주세요!")
                    }
                }
            });
}
$(function() {
    $("#bimg").on('change', function(){
    readURL(this);
    });
});
function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
        $('#img_preview').attr('src', e.target.result);
        }
        reader.readAsDataURL(input.files[0]);
    }
}

function replysave(){
    let bno = $("#bno").val();
    let reply = $("#reply").val();
    $.ajax({
        url:"/board/replysave",
        method : "POST",
        data : {"reply": reply, "bno": bno},
        success : function(result){
            if(result){
                $('#reply').val('');
                getreply(bno);
            }else{
                alert("로그인 후 이용해주세요!")
            }
        }
    });
}
function getreply(bnum){

    let replyhtml = "";
    $.ajax({
        url:"/board/getreply",
        data : { "bno": bnum },
        success : function(result){
        console.log(result);
            for(let i = 0; i <result.length; i++){
                if(result[i].rindex == 0){
                    if(result[i].same == true){
                        replyhtml +=
                            '<div class="replytable">'+
                                '<div class="row">'+
                                    '<div class="col-md-6"><h5>'+result[i].mid+'</h5></div>'+
                                    '<div class="col-md-6 d-flex justify-content-end">'+result[i].createdate+'</div>'+
                                '</div>'+
                                '<div class="row"><div class="col-md-8">'+result[i].rcontent+'</div>'+
                                '<div id="repltbtn" class="col-md-4 d-flex justify-content-end">'+
                                    '<button class="myreplyupdate" type="button" onclick="replyupdate('+result[i].rno+')">수정</button><button  class="myreplyupdate"  type="button" onclick="replydelete('+result[i].rno+')">삭제</button>'+
                                '</div></div>'+
                                '<div id = "'+result[i].rno+'"></div>'+
                            '</div>';
                    }else{
                        replyhtml +=
                            '<div class="replytable">'+
                                '<div class="row">'+
                                    '<div class="col-md-6"><h5>'+result[i].mid+'</h5></div>'+
                                    '<div class="col-md-6 d-flex justify-content-end">'+result[i].createdate+'</div>'+
                                '</div>'+
                                '<div>'+result[i].rcontent+'</div>'+
                                '<div id="repltbtn">'+
                                '</div>'+
                                '<div id="'+result[i].rno+'"></div>'+
                            '</div>';
                    }
                }else{
                    getrereply(result[i].rindex);
                }

            }
             $('#replytable').html(replyhtml);

        }
    });
}

function replydelete(rno) {
let bno = $("#bno").val();
    $.ajax({
        url: '/board/replydelete',
        data : { "rno": rno },
        success : function(result){
            getreply(bno);
        }
    });
}
function replyupdate(rno) {
let bno = $("#bno").val();
    $.ajax({
        url: '/board/replyupdate',
        data : { "rno": rno },
        success : function(result){
            let html =
                '<input type="text" class="replyupdate" id="rereply" value="'+result.rcontent+'">'+
                '<button type="button"  class="replyupdatebtn" onclick="reupdate('+rno+')">수정</button>';
            $("#"+rno).html(html);
        }
    });
}

function reupdate(rno){
let bno = $("#bno").val();
    let reply = $("#rereply").val();
        $.ajax({
            url:'/board/reupdate',
            method : "POST",
            data : {"rno": rno,"reply": reply},
            success : function(result){
                $('#reply').val('');
                getreply(bno);
            }
        });
}

