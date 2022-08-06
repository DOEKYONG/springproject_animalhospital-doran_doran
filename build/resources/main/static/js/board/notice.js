let current_page = 0;
let updatebno = 0;
getnotice(0);

var today = new Date();
	formatDate(today);
	let date = formatDate(today);

function getnotice(page) {

this.current_page = page;
$.ajax({
    url : '/board/getnotice',
    type : 'post',
    data : {"page" : page },
    success : function(json) {
    let html = '<tr> <th width="20%" style="padding-left:50px;"> 제목 </th><th width="40%" style="text-align:center"> 내용 </th><th width="10%" style="text-align:center"> 작성날짜 </th><th width="20%" style="text-align:center">비고</th> </tr>';
    let html2 = '';
    console.log(json);
    if(json.data.length == 0) {
                                html += '<tr>' +
                                '<td colspan="5"> 검색 결과가 존재하지 않습니다. </td> ' +
                                '</tr>';
                                }else{
       for(let i = 0; i < json.data.length; i++) {

       if(json.data[i]["bindate"].split(" ")[0]==date) {
       		json.data[i]["bindate"] = json.data[i]["bindate"].split(" ")[1]

       	} else {
       		json.data[i]["bindate"] = json.data[i]["bindate"].split(" ")[0]
       	}

            html += '<tr>' +
            '<td width="20%" style="padding-left:50px;"onclick="noticedetail('+json.data[i]["bno"]+')"data-bs-toggle="modal" data-bs-target="#myModal3">'+ json.data[i]["btitle"] +'</td>' +
            '<td width="40%" style="text-align:center" onclick="noticedetail('+json.data[i]["bno"]+')"data-bs-toggle="modal" data-bs-target="#myModal3">'+ json.data[i]["bcontent"] +'</td>' +
            '<td width="20%" style="text-align:center">'+ json.data[i]["bindate"] + '</td>' +
            '<td width="20%" style="text-align:center">' ;
            if(json.username == 'admin'){
             html +=
             '<button type="button" onclick="bnosave('+json.data[i]["bno"]+')" data-bs-toggle="modal" data-bs-target="#myModal2")">공지사항 수정</button>' +
             '<button type="button" onclick="noticedelete('+json.data[i]["bno"]+')">공지사항 삭제</button>';
            }
            html +=
             '</td></tr>';
             }
               ////////////////////////////////////// 이전 /////////////////////////////////////////////
             if( page == 0 )
              {
              html2 += ' <div class="page-item "><a class="page-link" onclick="getnotice('+ (page)+')">이전</a></div>' ;
              } else{
                 html2 +=
                   ' <div class="page-item "><a class="page-link" onclick="getnotice('+(page-1)+')">이전</a></div>' ;
                    }
                               ////////////////////////////////////// 페이징 ////////////////////////////////////////////
              for( let i = json.startbtn-1 ; i <= json.endbtn - 1; i++) {
                    html2 += '<div class="page-item "><button class="btn btn-primary mx-1" onclick="getnotice(' + i +')">'
                    + (i+1) + '</button></div>';
                     }
                             ////////////////////////////////////// 이후 버튼 //////////////////////////////////////////
                             if(page == json.totalpage-1){
                             html2 +=
                              ' <div class="page-item "><a class="page-link" onclick="getnotice('+ (page) +')">다음</a></div>' ;
                             }
                             else {
                              html2 +=
                              ' <div class="page-item "><a class="page-link" onclick="getnotice('+(page+1)+')">다음</a></div>' ;
                              }


        }
        $("#noticetable").html(html);
        $("#pagebtnbox").html( html2 );// 페이징버튼 html 넣기
    }
});

}

function noticesave() {

if($("#btitle").val() == "") {alert("공지사항 제목을 입력해주세요"); return;}
if($("#bcontent").val() == "") {alert("공지사항 내용을 입력해주세요"); return;}
$.ajax({
            url : '/admin/noticesave',
            type: "POST",
            data : {"btitle" : $("#btitle").val(),"bcontent" : $("#bcontent").val()} ,
            success : function(re) {
            console.log(re);
           if(re == true) {alert("등록 성공"); getnotice(0);  $('#myModal').modal('hide');}
           else { alert("등록 실패");}
           }
    });
    $("#btitle").val("");
    $("#bcontent").val("");
}


function bnosave(bno) {
updatebno = bno;
console.log(updatebno);
$.ajax({
url : '/board/getboard',
data : {"bno" : bno},
success : function(re) {
console.log(re);
$("#btitle2").val(re.btitle);
$("#bcontent2").val(re.bcontent);
}
});
}
function noticeupdate() {
console.log(updatebno);
$.ajax({
            url : '/admin/updatenotice',
            type: "PUT",
            data : {"bno" : updatebno, "btitle" : $("#btitle2").val(),"bcontent" : $("#bcontent2").val()} ,
            success : function(re) {
            console.log(re);
           if(re == true) {alert("수정 성공"); getnotice(0); $('#myModal2').modal('hide');}
           else { alert("수정 실패");}
           }
    });
        $("#btitle2").val() = "";
        $("#bcontent2").val() = "";
}

function noticedelete(bno) {
if(confirm("정말 공지사항을 삭제하시겠습니까?")) {
$.ajax({
            url : '/admin/deletenotice',
            type: "DELETE",
            data : {"bno" : bno } ,
            success : function(re) {
            console.log(re);
           if(re == true) {alert("삭제 성공"); getnotice(0); }
           else { alert("삭제 실패");}
           }
    });
}
}


function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    return [year, month, day].join('-');
}

function noticedetail(bno) {

$.ajax({
url : '/board/getboard',
data : {"bno" : bno},
success : function(re) {
console.log(re);
$("#btitle3").html(re.btitle);
$("#bcontent3").html(re.bcontent);
}
});
}

