list();
let html =
    '<tr>'+
    '    <th>회원아이디</th> <th>병원(이름+인허가일자)</th> <th>사업자번호</th><th>사업자 등록증</th><th>권한변경</th>'+
    '</tr>';

let blist = "";
function list(){
    $.ajax({
        url: "/admin/getbinlist",
        success: function(result){
            blist = result;
            if(result.length == 0){
                html +=
                    '<tr>'+
                    '    <td colspan="5" class="listx">현재 신청 내역이 없습니다.</td>'+
                    '</tr>';
            }
            for(let i=0; i<result.length; i++){
                html +=
                    '<tr>'+
                    '    <td>'+result[i].mid+'</td><td>'+result[i].hospital+'</td><td><input type="text" id="bin" name="bin"></td>'+
                    '<td><img onclick="fnImgPop(this.src)" data-bs-toggle="modal" data-bs-target="#exampleModal" style="width:100px;" src="/upload/'+result[i].binimg+'"></td>'+
                    '<td><button type="button" class="btn btn-primary" onclick="setrole('+i+')">허가</button></td>'+
                    '</tr>';
            }
            $("#binlisttable").html(html);
        }
    });
}

function fnImgPop(url){
    let html =
        '<img src="'+url+'" style="max-width:100%;">'
    $(".modal-body").html(html);
 }

function setrole(i){
    let mno = blist[i].mno;
    let hospital = blist[i].hospital;
    let bin = $("#bin").val();
    if(bin == ""){
        alert("사업자번호를 입력해주세요")
    }else {
        $.ajax({
            url:"/admin/setrole",
            data: {"mno" : mno, "hospital" : hospital, "bin" : bin},
            success: function(result){
                if(result){
                    alert("변경완료");
                    location.reload();
                }else{
                    alert("변경실패");
                }
            }
        });
    }
}