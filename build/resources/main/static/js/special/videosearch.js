start();
function start(){

   $.ajax({
    url : '/videosearch',
    data : {"search" : "강아지 키우기"},
    success : function(json) {
        console.log(json);
        let html2 = '';

         for(let i=0; i<json.items.length; i++) {
                html2 +=  '<div class="vbox" >'

                html2 +=
                           '<iframe id="ytplayer" type="text/html" width="700px" height="600px" '+
                          'src="https://www.youtube.com/embed/'+json.items[i].id.videoId+'?rel=0&modestbranding=1"' +
                          '></iframe>'
                 html2 +=
                       '</div>';
                        }

  $("#videobox").html(html2);
                    }

    });
}
function video() {
let search = $("#searchbox").val();

if(search == "") { alert("검색어를 입력해주세요"); return; }

    $.ajax({
    url : '/videosearch',
    data : {"search" : search},
    success : function(json) {
        console.log(json);
        let html2 = '';

         for(let i=0; i<json.items.length; i++) {
                html2 +=  '<div class="vbox" >'

                               html2 +=
                                          '<iframe id="ytplayer" type="text/html" width="700px" height="600px" '+
                                         'src="https://www.youtube.com/embed/'+json.items[i].id.videoId+'?rel=0&modestbranding=1"' +
                                         '></iframe>'
                                html2 +=
                                      '</div>';
                                       }

                                $("#videobox").html(html2);
                    }

    });
}


function enter() {
    if(e.keyCode == 13){
     video();
           }

}