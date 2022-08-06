getnews();

function getnews(){
    $.ajax({
        url: '/getnews',
        success: function( re ){

            let html  = "";
            for( let i = 0; i < re.length; i++ ){

            html +=

                   ' <div class="newsbox" onclick="window.open(`'+re[i].nlink+'`)">'+
                    ' <div class="img_wrap">'+
                                '<img src="'+re[i].imgurl+'">'+
                      '</div>'+
                           ' <div class="content_wrap">'+
                               ' <div class="title_area">'+
                                  ' <h4>'+re[i].title+'</h4>'+
                                '</div>'+
                                '<div class="content_area">'+
                                    '<h6>'+re[i].content+'</h6>'+
                                '</div>'+
                            '</div>'+
                            '</div>'

            }

            $(".newswrap").html( html );
        }
    });
}