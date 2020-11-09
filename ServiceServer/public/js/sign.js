
function registerClient() {
  var email = $('#client_email').val();
  var name = $('#client_name').val();
  var pwd = $('#client_pwd').val();
  var rePwd = $('#client_retype_pwd').val();
  
  if (email == "" || name == "" || pwd == "" || rePwd == "") {
    alert("등록 정보를 입력하세요.");
    return;
  }

  if(pwd != rePwd){
    $('#nofity_err_pwd').css('display', 'block');
    return;
  }

  $('#nofity_exist_email').css('display', 'none');
  $('#nofity_err_pwd').css('display', 'none');

  var reqData = {
    "email": email,
    "name": name,
    "pwd": pwd,
  };

  $.ajax({
    type: "POST",
    dataType: 'json',
    data: reqData,
    url: '/manager/auth/signup',
    timeout: 2000,
    success: function(res) {
      if(res.result == 1001){
        window.location.replace("/manager/auth/login");
      }else if(res.result == 1101){
        $('#nofity_exist_email').css('display', 'block');
      }else{
         alert("관리자 등록 오류!!(" + res.result + ")");
      }     
    }
  });
}


function loginClient() {
  var email = $('#client_email').val();
  var pwd = $('#client_pwd').val();
  
  if (email == "" || pwd == "") {
    alert("등록 정보를 입력하세요.");
    return;
  }

  $('#nofity_exist_email').css('display', 'none');
  $('#nofity_err_pwd').css('display', 'none');

  var reqData = {
    "email": email,
    "pwd": pwd,
  };

  $.ajax({
    type: "POST",
    dataType: 'json',
    data: reqData,
    url: '/manager/auth/login',
    timeout: 2000,
    success: function(res) {
      if(res.result == 1001){
        window.location.replace("/manager/dashboard/index");
      }else if(res.result == 1102){
        $('#nofity_exist_email').css('display', 'block');
      }else if(res.result == 1103){
        $('#nofity_err_pwd').css('display', 'block');
      }else{
         alert("관리자 로그인 오류!!(" + res.result + ")");
      }     
    }
  });
}


$(document).ready(function() {
  $('#btn-signup').on('click', function() {
    registerClient();
  });

  $('#btn_login').on('click', function() {
    loginClient();
  });
});
