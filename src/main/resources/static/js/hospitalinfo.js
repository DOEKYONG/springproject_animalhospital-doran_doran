let list;
let lat;
let logt;
let address;
let score;
let tel;
let link;
info(0);
 출력()
let page = 0;
let hname = "";
let hdate = "";


function info(page){
    $.ajax({
        url: "/map/info",
        method: "POST",
        contentType : 'application/json' ,
        async : false,
        success: function(re){
        console.log(re)
        lat = re.lat
        logt = re.logt
        address = re.haddress
        tel = re.htel
        score = re.score
        link = re.link
            $("#hname").html(re.hname);
            $("#address_data").html(
            '<p>'+re.haddress+'</p>'+
            '<p>TEL : '+re.htel+'</p>'
            )

            getreviewlist(page,re.hname,re.hdate);
            getreviewstarlist(re.hname,re.hdate);
      }
    });
}
function 출력() {
    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new kakao.maps.LatLng(lat, logt), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨
        };

    var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

    // 마커가 표시될 위치입니다
    var markerPosition  = new kakao.maps.LatLng(lat, logt);

    // 마커를 생성합니다
    var marker = new kakao.maps.Marker({
        position: markerPosition
    });

    // 마커가 지도 위에 표시되도록 설정합니다
    marker.setMap(map);

}

function modal(){

    let kind="";
    let fac="";
    let price="";

    for(let i=1; i<6;i++){
   kind +=
       '<img class="mstar" id="star'+i+'" src="/img/star2.png" onclick="kind('+i+')">';
             }
     for(let i=1; i<6;i++){
        fac +=
            '<img class="mstar" id="fac'+i+'" src="/img/star2.png" onclick="fac('+i+')">';
      }
      for(let i=1; i<6;i++){
         price +=
              '<img class="mstar" id="price'+i+'" src="/img/star2.png" onclick="price('+i+')">';
                  }
    $("#kind").html(kind);
    $("#fac").html(fac);
    $("#price").html(price);

}

let ckind=5;
let cfac=5;
let cprice=5;
let cavg=5;
function kind(i){
    ckind=i;
    if(i==1){
    document.getElementById("star"+1).src = "/img/star2.png";
    document.getElementById("star"+2).src = "/img/star1.png";
    document.getElementById("star"+3).src = "/img/star1.png";
    document.getElementById("star"+4).src = "/img/star1.png";
    document.getElementById("star"+5).src = "/img/star1.png";
    }
    else if(i==2){
        document.getElementById("star"+1).src = "/img/star2.png";
        document.getElementById("star"+2).src = "/img/star2.png";
        document.getElementById("star"+3).src = "/img/star1.png";
        document.getElementById("star"+4).src = "/img/star1.png";
        document.getElementById("star"+5).src = "/img/star1.png";
    }
    else if(i==3){
        document.getElementById("star"+1).src = "/img/star2.png";
        document.getElementById("star"+2).src = "/img/star2.png";
        document.getElementById("star"+3).src = "/img/star2.png";
        document.getElementById("star"+4).src = "/img/star1.png";
         document.getElementById("star"+5).src = "/img/star1.png";
    }
    else if(i==4){
        document.getElementById("star"+1).src = "/img/star2.png";
        document.getElementById("star"+2).src = "/img/star2.png";
        document.getElementById("star"+3).src = "/img/star2.png";
        document.getElementById("star"+4).src = "/img/star2.png";
        document.getElementById("star"+5).src = "/img/star1.png";
    }
    else if(i==5){
        document.getElementById("star"+1).src = "/img/star2.png";
        document.getElementById("star"+2).src = "/img/star2.png";
        document.getElementById("star"+3).src = "/img/star2.png";
        document.getElementById("star"+4).src = "/img/star2.png";
        document.getElementById("star"+5).src = "/img/star2.png";
    }
    avg();
}
function fac(i){
    cfac=i;
    if(i==1){
    document.getElementById("fac"+1).src = "/img/star2.png";
    document.getElementById("fac"+2).src = "/img/star1.png";
    document.getElementById("fac"+3).src = "/img/star1.png";
    document.getElementById("fac"+4).src = "/img/star1.png";
    document.getElementById("fac"+5).src = "/img/star1.png";
    }
    else if(i==2){
        document.getElementById("fac"+1).src = "/img/star2.png";
        document.getElementById("fac"+2).src = "/img/star2.png";
        document.getElementById("fac"+3).src = "/img/star1.png";
        document.getElementById("fac"+4).src = "/img/star1.png";
        document.getElementById("fac"+5).src = "/img/star1.png";
    }
    else if(i==3){
        document.getElementById("fac"+1).src = "/img/star2.png";
        document.getElementById("fac"+2).src = "/img/star2.png";
        document.getElementById("fac"+3).src = "/img/star2.png";
        document.getElementById("fac"+4).src = "/img/star1.png";
         document.getElementById("fac"+5).src = "/img/star1.png";
    }
    else if(i==4){
        document.getElementById("fac"+1).src = "/img/star2.png";
        document.getElementById("fac"+2).src = "/img/star2.png";
        document.getElementById("fac"+3).src = "/img/star2.png";
        document.getElementById("fac"+4).src = "/img/star2.png";
        document.getElementById("fac"+5).src = "/img/star1.png";
    }
    else if(i==5){
        document.getElementById("fac"+1).src = "/img/star2.png";
        document.getElementById("fac"+2).src = "/img/star2.png";
        document.getElementById("fac"+3).src = "/img/star2.png";
        document.getElementById("fac"+4).src = "/img/star2.png";
        document.getElementById("fac"+5).src = "/img/star2.png";
    }
    avg();
}

function price(i){
    cprice=i;
    if(i==1){
    document.getElementById("price"+1).src = "/img/star2.png";
    document.getElementById("price"+2).src = "/img/star1.png";
    document.getElementById("price"+3).src = "/img/star1.png";
    document.getElementById("price"+4).src = "/img/star1.png";
    document.getElementById("price"+5).src = "/img/star1.png";
    }
    else if(i==2){
        document.getElementById("price"+1).src = "/img/star2.png";
        document.getElementById("price"+2).src = "/img/star2.png";
        document.getElementById("price"+3).src = "/img/star1.png";
        document.getElementById("price"+4).src = "/img/star1.png";
        document.getElementById("price"+5).src = "/img/star1.png";
    }
    else if(i==3){
        document.getElementById("price"+1).src = "/img/star2.png";
        document.getElementById("price"+2).src = "/img/star2.png";
        document.getElementById("price"+3).src = "/img/star2.png";
        document.getElementById("price"+4).src = "/img/star1.png";
         document.getElementById("price"+5).src = "/img/star1.png";
    }
    else if(i==4){
        document.getElementById("price"+1).src = "/img/star2.png";
        document.getElementById("price"+2).src = "/img/star2.png";
        document.getElementById("price"+3).src = "/img/star2.png";
        document.getElementById("price"+4).src = "/img/star2.png";
        document.getElementById("price"+5).src = "/img/star1.png";
    }
    else if(i==5){
        document.getElementById("price"+1).src = "/img/star2.png";
        document.getElementById("price"+2).src = "/img/star2.png";
        document.getElementById("price"+3).src = "/img/star2.png";
        document.getElementById("price"+4).src = "/img/star2.png";
        document.getElementById("price"+5).src = "/img/star2.png";
    }
    avg();
}

avg();
function avg(){
    cavg=((ckind+cfac+cprice)/3).toFixed(1);
    $("#avg").html(cavg);
    if(cavg<=1){
        document.getElementById("avg1").src =  "/img/star2.png";
        document.getElementById("avg2").src = "/img/star1.png";
        document.getElementById("avg3").src =  "/img/star1.png";
        document.getElementById("avg4").src = "/img/star1.png";
        document.getElementById("avg5").src = "/img/star1.png";
        }
        else if(cavg<=2){
            document.getElementById("avg1").src =  "/img/star2.png";
            document.getElementById("avg2").src = "/img/star2.png";
           document.getElementById("avg3").src =  "/img/star1.png";
            document.getElementById("avg4").src = "/img/star1.png";
           document.getElementById("avg5").src = "/img/star1.png";
        }
        else if(cavg<=3){
            document.getElementById("avg1").src =  "/img/star2.png";
            document.getElementById("avg2").src = "/img/star2.png";
           document.getElementById("avg3").src =  "/img/star2.png";
            document.getElementById("avg4").src = "/img/star1.png";
             document.getElementById("avg5").src = "/img/star1.png";
        }
        else if(cavg<=4){
           document.getElementById("avg1").src = "/img/star2.png";
            document.getElementById("avg2").src = "/img/star2.png";
            document.getElementById("avg3").src = "/img/star2.png";
            document.getElementById("avg4").src = "/img/star2.png";
            document.getElementById("avg5").src = "/img/star1.png";
        }
        else if(cavg<=5){
            document.getElementById("avg1").src = "/img/star2.png";
            document.getElementById("avg2").src = "/img/star2.png";
            document.getElementById("avg3").src = "/img/star2.png";
            document.getElementById("avg4").src = "/img/star2.png";
            document.getElementById("avg5").src = "/img/star2.png";
        }
}
function file1(){
    document.all.file1.click();
      $(function() {
                     $("#file1").on('change', function(){
                     readURL(this);
                     });
                 });
                 function readURL(input) {
                     if (input.files && input.files[0]) {
                         var reader = new FileReader();
                         reader.onload = function (e) {
                         $('#preview1').attr('src', e.target.result);
                         }
                         reader.readAsDataURL(input.files[0]);
                     }
                 }


}
function file2(){
    document.all.file2.click();
     $(function() {
         $("#file2").on('change', function(){
         readURL(this);
         });
     });
     function readURL(input) {
         if (input.files && input.files[0]) {
             var reader = new FileReader();
             reader.onload = function (e) {
             $('#preview2').attr('src', e.target.result);
             }
             reader.readAsDataURL(input.files[0]);
         }
     }
}

function addreview(){
        if(document.getElementById('rcontent').value==""){
            alert("내용을 입력해주세요");
            return false;
        }
            let formData = new FormData();
            formData.append('rcontent',document.getElementById('rcontent').value);
        if(document.getElementById('file1').files[0]==null){

        }else{
            formData.append('rimg1',document.getElementById('file1').files[0]);
        }

        if(document.getElementById('file2').files[0]==null){

        }else{
            formData.append('rimg2',document.getElementById('file2').files[0]);
        }

        formData.append('rkind',ckind);
        formData.append('rfac',cfac);
        formData.append('rprice',cprice);

       $.ajax({
               url: "/map/addreview",
               method: "POST",
               data : formData ,
               contentType: false,
               processData: false ,
               success: function(re){
                location.reload();
             }
           });
}


////전체 평점 평균
function getreviewstarlist(hname,hdate){
   this.hname=hname;
        this.hdate=hdate;
       $.ajax({
            url: "/map/getreviewstarlist",
             method: "POST",
             data: {"hname":this.hname,"hdate":this.hdate},
            success: function(reviewlist){
            html = '';
               let totalcount = 0;
                    let ravg = '';
                     let kindavg = '';
                      let facavg = '';
                       let priceavg = '';
                    let kind=0;
                    let fac =0;
                    let price=0;
                     totalcount = reviewlist.data.length;
            for( let i = 0 ; i<reviewlist.data.length ; i++ ){
            console.log("durlsms "+reviewlist.data[i]);
               kind+= parseInt( reviewlist.data[i].rkind);
             fac+= parseInt( reviewlist.data[i].rfac);
              price+= parseInt( reviewlist.data[i].rprice);
              }
               let rrk = parseFloat(parseInt(kind)/parseInt(totalcount));
               let rrf = parseFloat(parseInt(fac)/parseInt(totalcount));
               let rrp = parseFloat(parseInt(price)/parseInt(totalcount));
               let rk =	0;
               let rf =	0;
               let rp =	0;
                 if(isNaN(rrk)==true){
                    rk=0;
                     }else{
                     rk=rrk;
                      }
                      if(isNaN(rrf)==true){
                       rf=0;
                       }else{
                       rf=rrf;
                       }
                       if(isNaN(rrp)==true){
                        rp=0;
                        }else{
                        rp=rrp;
                        }
                  if(rk<=0){
                         kindavg = '<img  class="star1" src="/img/star1.png">'+
                          '<img  class="star1" src="/img/star1.png">'+
                          '<img  class="star1"  src="/img/star1.png">'+
                          '<img class="star1" src="/img/star1.png">'+
                          '<img class="star1" src="/img/star1.png">';
                         }
                       if(0<rk&&rk<=1.5){
                             kindavg = '<img  class="star1" src="/img/star2.png">'+
                              '<img  class="star1" src="/img/star1.png">'+
                              '<img  class="star1"  src="/img/star1.png">'+
                              '<img class="star1" src="/img/star1.png">'+
                              '<img class="star1" src="/img/star1.png">';
                             }
                             else if(1.5<rk&&rk<=2.5){
                                kindavg = '<img class="star1" src="/img/star2.png">'+
                              '<img class="star1" src="/img/star2.png">'+
                              '<img class="star1" src="/img/star1.png">'+
                              '<img class="star1"  src="/img/star1.png">'+
                              '<img class="star1" src="/img/star1.png">';
                             }
                             else if(2.5<rk&&rk<=3.5){
                                  kindavg = '<img class="star1" src="/img/star2.png">'+
                                        '<img class="star1"  src="/img/star2.png">'+
                                        '<img class="star1" src="/img/star2.png">'+
                                        '<img class="star1"  src="/img/star1.png">'+
                                        '<img class="star1"  src="/img/star1.png">';
                             }
                             else if(3.5<rk&&rk<=4.5){
                                       kindavg = '<img class="star1"  src="/img/star2.png">'+
                                          '<img class="star1" src="/img/star2.png">'+
                                          '<img class="star1"  src="/img/star2.png">'+
                                          '<img class="star1"  src="/img/star2.png">'+
                                          '<img class="star1"  src="/img/star1.png">';
                             }
                             else if(4.5<rk&&rk<=5){
                                    kindavg = '<img class="star1"  src="/img/star2.png">'+
                                          '<img  class="star1" src="/img/star2.png">'+
                                          '<img class="star1"  src="/img/star2.png">'+
                                          '<img class="star1"  src="/img/star2.png">'+
                                          '<img class="star1"  src="/img/star2.png">';
                             }

                       if(rf<=0){
                                     facavg = '<img  class="star1" src="/img/star1.png">'+
                                      '<img  class="star1" src="/img/star1.png">'+
                                      '<img  class="star1"  src="/img/star1.png">'+
                                      '<img class="star1" src="/img/star1.png">'+
                                      '<img class="star1" src="/img/star1.png">';
                                     }
                            else if(0<rf&&rf<=1.5){
                                 facavg = '<img  class="star1" src="/img/star2.png">'+
                                  '<img  class="star1" src="/img/star1.png">'+
                                  '<img  class="star1"  src="/img/star1.png">'+
                                  '<img class="star1" src="/img/star1.png">'+
                                  '<img class="star1" src="/img/star1.png">';
                                 }
                                 else if(1.5<rf&&rf<=2.5){
                                    facavg = '<img class="star1" src="/img/star2.png">'+
                                  '<img class="star1" src="/img/star2.png">'+
                                  '<img class="star1" src="/img/star1.png">'+
                                  '<img class="star1"  src="/img/star1.png">'+
                                  '<img class="star1" src="/img/star1.png">';
                                 }
                                 else if(2.5<rf&&rf<=3.5){
                                      facavg = '<img class="star1" src="/img/star2.png">'+
                                            '<img class="star1"  src="/img/star2.png">'+
                                            '<img class="star1" src="/img/star2.png">'+
                                            '<img class="star1"  src="/img/star1.png">'+
                                            '<img class="star1"  src="/img/star1.png">';
                                 }
                                 else if(3.5<rf&&rf<=4.5){
                                           facavg = '<img class="star1"  src="/img/star2.png">'+
                                              '<img class="star1" src="/img/star2.png">'+
                                              '<img class="star1"  src="/img/star2.png">'+
                                              '<img class="star1"  src="/img/star2.png">'+
                                              '<img class="star1"  src="/img/star1.png">';
                                 }
                                 else if(4.5<rf&&rf<=5){
                                        facavg = '<img class="star1"  src="/img/star2.png">'+
                                              '<img  class="star1" src="/img/star2.png">'+
                                              '<img class="star1"  src="/img/star2.png">'+
                                              '<img class="star1"  src="/img/star2.png">'+
                                              '<img class="star1"  src="/img/star2.png">';
                                 }
                              if(rp<=0){
                                     priceavg = '<img  class="star1" src="/img/star1.png">'+
                                      '<img  class="star1" src="/img/star1.png">'+
                                      '<img  class="star1"  src="/img/star1.png">'+
                                      '<img class="star1" src="/img/star1.png">'+
                                      '<img class="star1" src="/img/star1.png">';
                                     }
                              else if(0<rp&&rp<=1.5){
                                   priceavg = '<img  class="star1" src="/img/star2.png">'+
                                    '<img  class="star1" src="/img/star1.png">'+
                                    '<img  class="star1"  src="/img/star1.png">'+
                                    '<img class="star1" src="/img/star1.png">'+
                                    '<img class="star1" src="/img/star1.png">';
                                   }
                                   else if(1.5<rp&&rp<=2.5){
                                      priceavg = '<img class="star1" src="/img/star2.png">'+
                                    '<img class="star1" src="/img/star2.png">'+
                                    '<img class="star1" src="/img/star1.png">'+
                                    '<img class="star1"  src="/img/star1.png">'+
                                    '<img class="star1" src="/img/star1.png">';
                                   }
                                   else if(2.5<rp&&rp<=3.5){
                                        priceavg = '<img class="star1" src="/img/star2.png">'+
                                              '<img class="star1"  src="/img/star2.png">'+
                                              '<img class="star1" src="/img/star2.png">'+
                                              '<img class="star1"  src="/img/star1.png">'+
                                              '<img class="star1"  src="/img/star1.png">';
                                   }
                                   else if(3.5<rp&&rp<=4.5){
                                             priceavg = '<img class="star1"  src="/img/star2.png">'+
                                                '<img class="star1" src="/img/star2.png">'+
                                                '<img class="star1"  src="/img/star2.png">'+
                                                '<img class="star1"  src="/img/star2.png">'+
                                                '<img class="star1"  src="/img/star1.png">';
                                   }
                                   else if(4.5<rp&&rp<=5){
                                          priceavg = '<img class="star1"  src="/img/star2.png">'+
                                                '<img  class="star1" src="/img/star2.png">'+
                                                '<img class="star1"  src="/img/star2.png">'+
                                                '<img class="star1"  src="/img/star2.png">'+
                                                '<img class="star1"  src="/img/star2.png">';
                                   }
                        $("#rkind").html(kindavg);
                        $("#rfac").html(facavg);
                        $("#rprice").html(priceavg);

                let avg=parseFloat((rk+rf+rp)/3).toFixed(2);
                            if(avg<=0){
                               ravg = '<img  class="star2" src="/img/star1.png">'+
                                '<img  class="star2" src="/img/star1.png">'+
                                '<img  class="star2"  src="/img/star1.png">'+
                                '<img class="star2" src="/img/star1.png">'+
                                '<img class="star2" src="/img/star1.png">';
                               }
                            else if(0<avg&&avg<=1.5){
                                   ravg = '<img  class="star2" src="/img/star2.png">'+
                                    '<img  class="star2" src="/img/star1.png">'+
                                    '<img  class="star2"  src="/img/star1.png">'+
                                    '<img class="star2" src="/img/star1.png">'+
                                    '<img class="star2" src="/img/star1.png">';
                                   }
                                   else if(1.5<avg&&avg<=2.5){
                                      ravg = '<img class="star2" src="/img/star2.png">'+
                                    '<img class="star2" src="/img/star2.png">'+
                                    '<img class="star2" src="/img/star1.png">'+
                                    '<img class="star2"  src="/img/star1.png">'+
                                    '<img class="star2" src="/img/star1.png">';
                                   }
                                   else if(2.5<avg&&avg<=3.5){
                                        ravg = '<img class="star2" src="/img/star2.png">'+
                                              '<img class="star2"  src="/img/star2.png">'+
                                              '<img class="star2" src="/img/star2.png">'+
                                              '<img class="star2"  src="/img/star1.png">'+
                                              '<img class="star2"  src="/img/star1.png">';
                                   }
                                   else if(3.5<avg&&avg<=4.5){
                                             ravg = '<img class="star2"  src="/img/star2.png">'+
                                                '<img class="star2" src="/img/star2.png">'+
                                                '<img class="star2"  src="/img/star2.png">'+
                                                '<img class="star2"  src="/img/star2.png">'+
                                                '<img class="star2"  src="/img/star1.png">';
                                   }
                                   else if(4.5<avg&&avg<=5.5){
                                          ravg = '<img class="star2"  src="/img/star2.png">'+
                                                '<img  class="star2" src="/img/star2.png">'+
                                                '<img class="star2"  src="/img/star2.png">'+
                                                '<img class="star2"  src="/img/star2.png">'+
                                                '<img class="star2"  src="/img/star2.png">';
                                   }
                                    $("#ravg").html(ravg);
                                    $("#totalavg").html(avg);
                                    $("#daumscore").html(
                                    '<a href="'+link+'">'+score+'</a>'
                                    )
                                    }
            });
}


function getreviewlist(page,hname,hdate){
        this.hname=hname;
        this.hdate=hdate;
        this.page=page;

           $.ajax({
           		url: "/map/getreviewlist",
           		 method: "POST",
           		 data: {"hname":this.hname,"hdate":this.hdate,"page":this.page},
           		success: function(reviewlist){
           		console.log(reviewlist);
           		    html = '';
           		    let star = [];
           		    let mupdate = [];
           		       let totalcount = 0;
                            let ravg = '';
                             let kindavg = '';
                              let facavg = '';
                               let priceavg = '';
                            let kind=0;
                            let fac =0;
                            let price=0;
           		  totalcount = reviewlist.data.length;
                       if( reviewlist.data.length == 0 ){ // 검색 결과가 존재하지 않으면
                                                 html +=
                                                       '<div>'+
                                                               '<div colspan="5">검색 결과가 존재하지 않습니다.</div> '+
                                                        '</div>';
                                       }else{


                                               for( let i = 0 ; i<reviewlist.data.length ; i++ ){
                                                  let sum  = (parseInt( reviewlist.data[i].rkind)+ parseInt( reviewlist.data[i].rfac)+parseInt( reviewlist.data[i].rprice))/3;
                                                    console.log(reviewlist);
                                                       if(reviewlist.data[i].same=="true"){
                                                        mupdate.push('<button type="button"  data-bs-toggle="modal" data-bs-target="#myModal" onclick="updatemodal('+reviewlist.data[i].rno+')">수정</button>'+
                                                        '<button type="button" onclick="rdelete('+reviewlist.data[i].rno+')">삭제</button>');
                                                       }
                                                       else{
                                                         mupdate.push('');
                                                       }


                                                  if(sum<=1){
                                                  star.push('<img class="star1" src="/img/star2.png">'+
                                                   '<img class="star1" src="/img/star1.png">'+
                                                   '<img class="star1"  src="/img/star1.png">'+
                                                   '<img class="star1" src="/img/star1.png">'+
                                                   '<img class="star1" src="/img/star1.png">');
                                                  }
                                                  else if(1<sum&&sum<=2){
                                                      star.push('<img class="star1" src="/img/star2.png">'+
                                                   '<img class="star1" src="/img/star2.png">'+
                                                   '<img class="star1" src="/img/star1.png">'+
                                                   '<img class="star1"  src="/img/star1.png">'+
                                                   '<img class="star1" src="/img/star1.png">');
                                                  }
                                                  else if(2<sum&&sum<=3){
                                                        star.push('<img class="star1" src="/img/star2.png">'+
                                                             '<img class="star1"  src="/img/star2.png">'+
                                                             '<img class="star1" src="/img/star2.png">'+
                                                             '<img class="star1"  src="/img/star1.png">'+
                                                             '<img class="star1"  src="/img/star1.png">');
                                                  }
                                                  else if(3<sum&&sum<=4){
                                                             star.push('<img  class="star1" src="/img/star2.png">'+
                                                               '<img class="star1" src="/img/star2.png">'+
                                                               '<img  class="star1" src="/img/star2.png">'+
                                                               '<img  class="star1" src="/img/star2.png">'+
                                                               '<img class="star1"  src="/img/star1.png">');
                                                  }
                                                  else if(4<sum&&sum<=5){
                                                          star.push('<img  class="star1" src="/img/star2.png">'+
                                                               '<img  class="star1" src="/img/star2.png">'+
                                                               '<img  class="star1" src="/img/star2.png">'+
                                                               '<img  class="star1" src="/img/star2.png">'+
                                                               '<img  class="star1" src="/img/star2.png">');
                                                  }

                                               if(reviewlist.data[i].rimg2==null&&reviewlist.data[i].rimg1==null){
                                                     html +=
                                                     ' <div class="user_review">'+
                                                         '<div class="writer_area">'+
                                                          '<span><i class="fas fa-user-alt"></i></span> <span class="id">'+reviewlist.data[i].mid+'</span>'+
                                                            '<div id="mstar'+reviewlist.data[i].rno+'"    style = "margin-top:20px; margin-bottom:20px; "></div> '+
                                                            '<div id="mupdate'+reviewlist.data[i].rno+'"></div> '+
                                                         '</div>'+
                                                         '<div class="user_content">'+

                                                             '<div class="u_content">'+
                                                               '<div class="c1"> '+reviewlist.data[i].rcontent+'</div>'+
                                                               '<div class="c2" style="margin-top:20px"></div>'+
                                                             '</div>'+
                                                         '</div>'+
                                                     '  </div>'
                                               }
                                               else if(reviewlist.data[i].rimg2==null&&reviewlist.data[i].rimg1!=null){
                                                    html +=
                                                        ' <div class="user_review">'+
                                                            '<div class="writer_area">'+
                                                             '<span><i class="fas fa-user-alt"></i></span> <span class="id">'+reviewlist.data[i].mid+'</span> '+
                                                               '<div id="mstar'+reviewlist.data[i].rno+'"  style = "margin-top:20px; margin-bottom:20px;"></div> '+
                                                               '<div id="mupdate'+reviewlist.data[i].rno+'"></div> '+
                                                            '</div>'+
                                                            '<div class="user_content">'+
                                                                '<div class="img_area">'+
                                                                  '<div class="first_img">'+
                                                                    '<div class="img_sizing">'+
                                                                      '<img src="/upload/'+reviewlist.data[i].rimg1+'">'+
                                                                    '</div>'+
                                                                  '</div>'+
                                                                '</div>'+
                                                                '<div class="u_content">'+
                                                                  '<div class="c1"> '+reviewlist.data[i].rcontent+'</div>'+
                                                                  '<div class="c2" style="margin-top:20px"></div>'+
                                                                '</div>'+
                                                            '</div>'+
                                                        '  </div>'
                                               }
                                               else if(reviewlist.data[i].rimg2!=null&&reviewlist.data[i].rimg1==null){
                                                    html +=
                                                         ' <div class="user_review">'+
                                                             '<div class="writer_area">'+
                                                              '<span><i class="fas fa-user-alt"></i></span> <span class="id">'+reviewlist.data[i].mid+'</span> '+
                                                                '<div id="mstar'+reviewlist.data[i].rno+'"  style = "margin-top:20px; margin-bottom:20px;"></div> '+
                                                                '<div id="mupdate'+reviewlist.data[i].rno+'"></div> '+
                                                             '</div>'+
                                                             '<div class="user_content">'+
                                                                 '<div class="img_area">'+
                                                                   '<div class="first_img">'+
                                                                     '<div class="img_sizing">'+
                                                                       '<img src="/upload/'+reviewlist.data[i].rimg2+'">'+
                                                                     '</div>'+
                                                                   '</div>'+
                                                                 '</div>'+
                                                                 '<div class="u_content">'+
                                                                   '<div class="c1"> '+reviewlist.data[i].rcontent+'</div>'+
                                                                   '<div class="c2" style="margin-top:20px"></div>'+
                                                                 '</div>'+
                                                             '</div>'+
                                                         '  </div>'
                                               }
                                               else{
                                                     html +=
                                                      ' <div class="user_review">'+
                                                          '<div class="writer_area">'+
                                                           '<span><i class="fas fa-user-alt"></i></span> <span class="id">'+reviewlist.data[i].mid+'</span> '+
                                                             '<div id="mstar'+reviewlist.data[i].rno+'"  style = "margin-top:20px; margin-bottom:20px;"></div> '+
                                                             '<div id="mupdate'+reviewlist.data[i].rno+'"></div> '+
                                                          '</div>'+
                                                          '<div class="user_content">'+
                                                              '<div class="img_area">'+
                                                                '<div class="first_img">'+
                                                                  '<div class="img_sizing">'+
                                                                   ' <img src="/upload/'+reviewlist.data[i].rimg1+'">'+
                                                                    '<img src="/upload/'+reviewlist.data[i].rimg2+'">'+
                                                                  '</div>'+
                                                                '</div>'+
                                                              '</div>'+
                                                              '<div class="u_content">'+
                                                                '<div class="c1"> '+reviewlist.data[i].rcontent+'</div>'+
                                                                '<div class="c2" style="margin-top:20px"></div>'+
                                                              '</div>'+
                                                          '</div>'+
                                                      '  </div>'
                                               }
                                               }


                           }
                            let pagehtml = "";
                                                if( page == 0 ){

                                                }else{
                                                    pagehtml +=
                                                       '<li class="page-item"> '+
                                                                   '<button class="page-link" onclick="info('+ (page-1) +')"> 이전 </button>'+
                                                        '</li>';
                                                 }
                                                for( let i = reviewlist.startbtn ; i<=reviewlist.endbtn; i++ ){
                                                   pagehtml +=
                                                         '<li class="page-item"> '+
                                                           '<button class="page-link" onclick="info('+(i-1)+')"> '+i+' </button>'+
                                                         '</li>';
                                                }

                                               if( page == reviewlist.totalpages -1 ){
                                                    pagehtml +=
                                                           '<li class="page-item"> '+
                                                                       '<button class="page-link" onclick="info('+ (page)  +')"> 다음 </button>'+
                                                            '</li>';
                                               }else{
                                                    pagehtml +=
                                                       '<li class="page-item"> '+
                                                                   '<button class="page-link" onclick="info('+ (page+1)  +')"> 다음 </button>'+
                                                        '</li>';
                                               }
                        $("#table").html(html);
                       $("#pagebtnbox").html(pagehtml);

                       for( let i = 0 ; i<reviewlist.data.length ; i++ ){
                            $("#mstar"+reviewlist.data[i].rno).html(star[i]);
                            $("#mupdate"+reviewlist.data[i].rno).html(mupdate[i]);
                       }



           		}

           	});


}


function updatemodal(rno){
     let kind="";
    let fac="";
    let price="";

    for(let i=1; i<6;i++){
   kind +=
       '<img class="mstar2" id="star'+i+'" src="/img/star2.png" onclick="kind('+i+')">';
             }
     for(let i=1; i<6;i++){
        fac +=
            '<img class="mstar2" id="fac'+i+'" src="/img/star2.png" onclick="fac('+i+')">';
      }
      for(let i=1; i<6;i++){
         price +=
              '<img class="mstar2" id="price'+i+'" src="/img/star2.png" onclick="price('+i+')">';
                  }
        $("#kind").html(kind);
        $("#fac").html(fac);
        $("#price").html(price);
         $("#addbox").html('<button type="button" onclick="updatereview('+rno+')">수정하기</button>');
       get(rno);
}

function get(rno){
    $.ajax({
         url : "/map/getreview" ,
         data : { "rno" : rno } ,
          async: false,
         success: function( review ){
         console.log("get"+review.rimg1);
         console.log("get"+review.rimg2);
            $("#rcontent").html(review.rcontent);
            kind(parseInt(review.rkind));
            fac(parseInt(review.rfac));
            price(parseInt(review.rprice));
            if(review.rimg1!=null){
            $('#preview1').attr('src','/upload/'+review.rimg1);
            }
            if(review.rimg2!=null){
            $('#preview2').attr('src','/upload/'+review.rimg2);
            }//
         }
        });
}

function updatereview(rno){

     if(document.getElementById('rcontent').value==""){
                alert("내용을 입력해주세요");
                return false;
            }
            let formData = new FormData();
            formData.append('rno',rno);
            formData.append('rcontent',document.getElementById('rcontent').value);
            if(document.getElementById('file1').files[0]==null){

            }else{
                formData.append('rimg1',document.getElementById('file1').files[0]);
            }

            if(document.getElementById('file2').files[0]==null){

            }else{
                formData.append('rimg2',document.getElementById('file2').files[0]);
            }

            formData.append('rkind',ckind);
            formData.append('rfac',cfac);
            formData.append('rprice',cprice);

           $.ajax({
                   url: "/map/updatereview",
                   method: "POST",
                   data : formData ,
                   contentType: false,
                   processData: false ,
                   success: function(re){
                    location.reload();
                 }
               });
}

function rdelete(rno){
     $.ajax({
             url : "/map/rdelete" ,
             method : "Delete",
             data : { "rno" : rno } ,
             success: function( re ){
                 location.reload();
             }
    });
}