var mCode;
var mName;


function setAccessInfos(session) {
   mCode = session.code;
   mName = session.name;
   console.log("Session : " + mCode + ", " + mName);

   setSignContent();
}

function setSignContent() {
	if(mCode != undefined){
		$('#auth_state').text('Logout');
		$("#auth_link").attr("href", "/manager/auth/logout");
	}
}

$(document).ready(function() {
 
});
