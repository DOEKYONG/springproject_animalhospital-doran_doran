function search(){
    let keyword = $("#searchbar").val();
    $.ajax({
        url: "/map/search",
        data: {"keyword" : keyword},
        success: function(result) {
            searchresult = result;
            let searchlist= "";
            if(result.length == 0){
                searchlist = '<div>일치하는 병원이 없습니다.</div>'
            }else {
                for (let i = 0; i<result.length; i++){
                    hname = result[i].name;
                    hdate = result[i].opendate;
                    hcity = result[i].city;
                    searchlist +=
                        '<div Onclick="hnamesave('+i+')" style="cursor:pointer" >'+result[i].city+' '+result[i].name+'</div>';
                }
            }
            $("#searchlist").html(searchlist);
        }
    });
}

function hnamesave(i){
    $("#hname").val(searchresult[i].name);
    $("#hospital").val(searchresult[i].name + searchresult[i].opendate);

    $("#closebtn").trigger('click');
}

function request(){
    let hospital = $("#hospital").val();
    let binimg = $("#binimg").val();
    alert(hospital+binimg);
    if(hospital == "" || binimg == ""){
        alert("모두 입력해주세요!");
    }else {
        let formData = new FormData($("#requestform")[0]);

        $.ajax({
            url: '/member/requestsave',
            method: "POST",
            enctype: 'multipart/form-data',
            async: false,
            data: formData,
            contentType: false,
            processData: false ,
            success: function(result){
                console.log(result);
                alert(result);
                if(result == 1){
                    alert("신청완료");
                }else if (result == 2){
                    alert("이미 등록된 병원입니다");
                }else{
                    alert("신청실패");
                }
            }
        });
    }

}