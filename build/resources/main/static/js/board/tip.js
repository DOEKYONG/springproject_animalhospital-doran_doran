function tipsave() { // 유효성 처리 왜뺀건지 물어봐야할듯?
    if($("#bimg").val()==""){
     alert("이미지를 하나 이상 등록해주세요");
    }else if($("#btitle").val()==""){
     alert("제목을 입력해주세요");
    }else{
    let form = $("#saveform")[0];
        let formdata = new FormData( form);
        $.ajax({
            url: "/board/tipwrite",
            method: "POST",
            data : formdata ,
            contentType: false,
            processData: false ,
            success: function( re ){
                if(re){
                     location.href ="tiplist"

                }else{
                    alert("로그인 후 이용해주세요!")
                }
            }
        });
    }
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
//
                     $($.parseHTML('<img>')).attr('style', 'width:80%');
                }

                reader.readAsDataURL(input.files[i]);
            }
        }
    };

    $('#bimg').on('change', function() {
        imagesPreview(this, 'div.preview');
    });
});


let current_page = 0;
boardlist(0);


function boardlist( page){
    this.current_page = page;
    console.log(this.current_page);
    $.ajax({
    		url: "/board/btiplist",
    		 method: "POST",
    		 data: {"page":this.current_page},
    		success: function(boardlist){
    		console.log(boardlist);
    		    html ="";
//    		    '<div><div>번호</div><div>제목</div><div>이미지</div></div>';
                if( boardlist.blists.length == 0 ){ // 검색 결과가 존재하지 않으면
                      html +=
                        '<div>'+
                            '<div colspan="5">게시글이 존재하지 않습니다.</div> '+
                        '</div>';
                      }else{
                                for( let i = 0 ; i<boardlist.blists.length ; i++ ){
                                console.log(boardlist.blists[i]);
                                         html +=
                                            '<div class="card mb-3"  style="max-width: 1000px;"  >'+
                                                '<div class="row g-0"  onclick="bupdate('+boardlist.blists[i].bno+')"><div class="col-md-4">'+
                                                '  <img src="/upload/'+boardlist.blists[i].bimg+'"  class="img-fluid rounded-start" alt="...">'+
                                                '  </div><div class="col-md-8"><div class="card-body">'+
                                                '    <h4 class="card-title">'+boardlist.blists[i].btitle+'</h4><span>'+boardlist.blists[i].mid+'</span>'+
                                                '    <p class="card-text d-flex justify-content-end">'+boardlist.blists[i].bdate+'</p>'+
                                                '  </div>'+
                                                '  </div>'+
                                                 '  </div>'+
                                                '</div>';
                                }
                    }
                     let pagehtml = "";
                     if( page == 0 ){
                            pagehtml +=
                             '<li class="page-item"> '+
                                         '<button class="page-link" onclick="boardlist('+ (page)  +')"> 이전 </button>'+
                              '</li>';
                     }else{
                         pagehtml +=
                            '<li class="page-item"> '+
                                        '<button class="page-link" onclick="boardlist('+ (page-1)  +')"> 이전 </button>'+
                             '</li>';
                      }
                     for( let i = boardlist.blists[0].startbtn ; i<=boardlist.blists[0].endhtn; i++ ){
                        pagehtml +=
                              '<li class="page-item"> '+
                                '<button class="page-link" onclick="boardlist('+(i-1)+')"> '+i+' </button>'+
                              '</li>';
                     }

                    if( page == boardlist.totalpages -1 ){
                         pagehtml +=
                                '<li class="page-item"> '+
                                            '<button class="page-link" onclick="boardlist('+ (page)  +')"> 다음 </button>'+
                                 '</li>';
                    }else{
                         pagehtml +=
                            '<li class="page-item"> '+
                                        '<button class="page-link" onclick="boardlist('+ (page+1)  +')"> 다음 </button>'+
                             '</li>';
                    }
                $("#table").html(html);
                $("#pagebtnbox").html( pagehtml);
    		}
    	});

}



function bupdate(bno){
    location.href = '/board/tipview/'+bno;
}