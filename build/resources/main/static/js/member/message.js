//쪽지 메소드
$(document).ready(function(){
    let mid = "";
    let hospital = "";



     $("#sendmsg").click(function(){

        $.ajax({
            url: '/member/findhospital',
            data :{"hospital" : hospital},
            success: function(result){
                console.log(result);
                if(!result){
                    alert("병원계정이 등록되어있지 않은 병원입니다");
                }
            }
        });
        let from = mid;
        let to = hospital;
        let msg = $("#msginput").val();
        let jsonmsg = {
            "from" : mid,
            "to" : hospital ,
            "msg" :msg,
            "type" : 2
        }
        console.log(jsonmsg);
        send(jsonmsg);
        $("#exampleModal").load(window.location.href + "#exampleModal");
        $("#close").trigger("click");
        alert("전송 완료")
        });
        $.ajax({
                        url: '/member/getinfo',
                        success: function(result){
                            mid= result.mid;
                            hospital = result.hospital;
                            console.log(result);
                        }
                    });

   // 1. 웹소켓 객체 생성
   let msgwebsocket = new WebSocket("ws://ec2-43-200-181-29.ap-northeast-2.compute.amazonaws.com/ws/message/"+mid);

   // 2. 웹소켓 객체에 구현된 메소드 저장
   msgwebsocket.onopen = onOpen;
   msgwebsocket.onclose = onClose;
   msgwebsocket.onmessage = onMessage;

   // 3. 각 메소드 구현
  function onOpen(){}
  function onClose(){}
  function onMessage(){}

   function send(jsonmsg){  msgwebsocket.send(JSON.stringify(jsonmsg)); }

});