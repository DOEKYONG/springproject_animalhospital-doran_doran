let current_page = 0;
let updatebno = 0;

$(document).ready(function(){

getitemlist(0);

});

function getitemlist(page) {

this.current_page = page;


$.ajax({
    url : '/member/getitemlist',
    data : {"page" : this.current_page },
    success : function(json) {
    let html = "";
    let html2 = '';
    console.log(json);
    var reg = /(.*?)\.(jpg|jpeg|png|gif|bmp)$/;
    if(json.itemlist.length == 0) {
                                html += '<div>' +
                                '검색 결과가 존재하지 않습니다. ' +
                                '</div>';
                                }
                                else if(json.itemlist.length != 0){
       for(let i = 0; i < json.itemlist.length; i++) {
       html+=
              '<li onclick="itemview('+json.itemlist[i].sno+')">'+
                             '<div class="">';
                   if( json.itemlist[i].bimg != null ) {
                      html +=
                        '<div id="imgwrap">'+
                        '<img id="pimg" src="/upload/'+json.itemlist[i].bimg+'">' +
                        '<div>';
                     }
                   html +=  '<div class="caption">' +
                           '<h4 class="">상품 이름 : ' +
                               json.itemlist[i].stitle + '</h4>' +
                           '<p class="">상품 설명 : ' +
                              json.itemlist[i].scontent + '</p>' +
                          '<div class="">' +
                             '<div class="">' +
                                   '<p class="lead">가격 : ' +
                                       + json.itemlist[i].sprice +
                                       '</p>' +
                               '</div>' +
                           '</div>' +
                       '</div>' +
                   '</div>' +
               '</div>' +
               '</li>';
             }
               ////////////////////////////////////// 이전 /////////////////////////////////////////////
             if( page == 0 )
              {
              html2 += ' <div class="page-item "><a class="page-link" onclick="getitemlist('+ (page)+')">이전</a></div>' ;
              } else{
                 html2 +=
                   ' <div class="page-item "><a class="page-link" onclick="getitemlist('+(page-1)+')">이전</a></div>' ;
                    }
                               ////////////////////////////////////// 페이징 ////////////////////////////////////////////
              for( let i = json.itemlist[0].startbtn ; i <=  json.itemlist[0].endbtn; i++) {
                    html2 += '<div class="page-item "><button class="btn btn-primary mx-1" onclick="getitemlist(' + (i-1) +')">'
                    + (i) + '</button></div>';
                     }
                             ////////////////////////////////////// 이후 버튼 //////////////////////////////////////////
                             if(page == json.itemlist[0].totalpage -1 ){
                             html2 +=
                              ' <div class="page-item "><a class="page-link" onclick="getitemlist('+ (page) +')">다음</a></div>' ;
                             }
                             else {
                              html2 +=
                              ' <div class="page-item "><a class="page-link" onclick="getitemlist('+(page+1)+')">다음</a></div>' ;
                              }


        }
        else {
        alert("프로그램 오류!");
        }
         $("#products2").html(html);
        $("#btnbox").html( html2 );// 페이징버튼 html 넣기
    }
});

}


function itemsave() {

if($("#stitle").val() == "") {alert("상품 제목을 입력해주세요"); return;}
if($("#scontent").val() == "") {alert("상품에 대한 설명을 입력해주세요"); return;}
let check = /^[0-9]+$/;


if (! check.test($("#price").val() ) ) {   alert("가격은 숫자만 입력해야 합니다."); return;}
if($("#simg").val() == "") {alert("상품을 보여 줄 수 있는 사진을 올려주세요"); return;}

let form = $("#saveform")[0]; // [0] : 폼내 입력 데이터 //
    //[0]을 안하면 데이터 + 설정값까지 같이 보내지게 됨
    let formData = new FormData(form);

$.ajax({
            url : '/member/itemsave',
            type: "POST",
            data : formData,
            contentType : false, // 첨부파일 전송시 사용되는 속성
            processData : false,
            success : function(re) {
            console.log(re);
           if(re == true) {alert("등록 성공");
           getitemlist(0);
           $("#price").val('');
           $("#stitle").val('');
           $("#scontent").val('');
           $('#myModal').modal('hide')
           }
           else { alert("등록 실패");}
           }
    });

}

function itemview(sno) {

location.href = '/member/itemview?sno='+ sno;
}

$(function() {
    // Multiple images preview in browser
    var imagesPreview = function(input, placeToInsertImagePreview) {

        if (input.files) {
            var filesAmount = input.files.length;
//               $(".preview").html("");
            for (i = 0; i < 1; i++) {
                var reader = new FileReader();

                reader.onload = function(event) {
                  $($("#img_preview")).attr('src', event.target.result).appendTo(placeToInsertImagePreview);
//                    $($.parseHTML('<img>')).attr('src', event.target.result).appendTo(placeToInsertImagePreview);
                     $($.parseHTML('<img>')).attr('style', 'width:80%');
                }

                reader.readAsDataURL(input.files[i]);
            }
        }

    };

    $('#simg').on('change', function() {
        imagesPreview(this, 'div.preview');
    });
});

