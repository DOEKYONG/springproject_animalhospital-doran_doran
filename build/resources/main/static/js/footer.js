function mdelete() {

$.ajax({
url : '/member/delete',
type: "DELETE",
success : function(re) {
if(re) {
alert("탈퇴 성공");
location.href = '/member/logout';
}
}
});
}