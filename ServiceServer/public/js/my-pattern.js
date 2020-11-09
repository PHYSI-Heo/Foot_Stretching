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
		$('#client_title1').text(mName);
		$('#client_title2').text('개별 운동패턴 설정을 통해 패턴정보의 사용&공유를 지원합니다.');
	}else{
		$('#client_title1').text('Undefined!');
		$('#client_title2').text('개별 운동패턴을 등록&관리하기 위한 관리자 로그인이 필요합니다.');
	}
}

function addItemRow() {
	var rowInfo = {};
	// var leftDir = $("#left_dirs option").index($("#left_dirs option:selected"));
	var leftDir = $("#left_dirs option:selected").val();
	var leftAngle = $('#left_angle').val();
	var leftSpeed = $('#left_speed').val();
	var lefthold = $('#left_hold').val();
	var leftReturn = $('input[id="left_return"]').is(":checked");

	// var rightDir = $("right_dirs option").index($("#right_dirs option:selected"));

	var rightDir = $("#right_dirs option:selected").val();
	var rightAngle = $('#right_angle').val();
	var rightSpeed = $('#right_speed').val();
	var righthold = $('#right_hold').val();
	var rightReturn = $('input[id="right_return"]').is(":checked");

	if(leftAngle == "" || leftSpeed == "" || lefthold == "" || rightAngle == "" || rightSpeed == "" || righthold == "" ){
		alert("설정 정보를 입력하세요.");
		return;
	}

	var rowInfo = {
		"leftDir" : leftDir,
		"leftAngle" : leftAngle,
		"leftSpeed" : leftSpeed,
		"lefthold" : lefthold,
		"leftReturn" : leftReturn,
		"rightDir" : rightDir,
		"rightAngle" : rightAngle,
		"rightSpeed" : rightSpeed,
		"righthold" : righthold,
		"rightReturn" : rightReturn,
	};

	showItemRow(rowInfo);
}

function showItemRow(item) {
	var rowCount = $('#tb_items >tbody >tr').length;

	console.log(rowCount);

	var $tr = $("<tr/>");
  	$tr.attr('name', rowCount);
	// Left Dir
	var $td_left_dir = $("<td/>");
	$td_left_dir.html(getDirectionStr(item.leftDir));
	$tr.append($td_left_dir);

	// Left angle
	var $td_left_angle = $("<td/>");
	$td_left_angle.html(item.leftAngle + " °");
	$tr.append($td_left_angle);

	// Left speed
	var $td_left_speed = $("<td/>");
	$td_left_speed.html(item.leftSpeed + " Phase");
	$tr.append($td_left_speed);

	// Left hold
	var $td_left_hold = $("<td/>");
	$td_left_hold.html(item.lefthold + " Sec");
	$tr.append($td_left_hold);

	// Left return
	var $td_left_hold = $("<td/>");
	var $span_left = $("<span/>", {
		class: "badge badge-dot mr-4"
	});	
	if(item.leftReturn){
		$span_left.html("<i class='bg-success'></i> return");
	}else{
		$span_left.html("<i class='bg-warning'></i> holding");
	}
	$tr.append($td_left_hold.append($span_left));

	var $td_right_dir = $("<td/>");
	$td_right_dir.html(getDirectionStr(item.rightDir));
	$tr.append($td_right_dir);

	var $td_right_angle = $("<td/>");
	$td_right_angle.html(item.rightAngle + " °");
	$tr.append($td_right_angle);

	var $td_right_speed = $("<td/>");
	$td_right_speed.html(item.rightSpeed + " Phase");
	$tr.append($td_right_speed);

	var $td_right_hold = $("<td/>");
	$td_right_hold.html(item.righthold + " Sec");
	$tr.append($td_right_hold);

	var $td_right_hold = $("<td/>");
	var $span_right = $("<span/>", {
		class: "badge badge-dot mr-4"
	});
	if(item.rightReturn){
		$span_right.html("<i class='bg-success'></i> return");
	}else{
		$span_right.html("<i class='bg-warning'></i> holding");
	}
	$tr.append($td_right_hold.append($span_right));

	var $td_del_btn = $("<td/>");
	var $i_del = $("<i/>", {
		class: "ni ni-fat-delete click_cursor"
	});
	// Click event
	$i_del.on('click', function() {
		console.log("del button event." + rowCount);
	});
	$tr.append($td_del_btn.append($i_del));

	$('#tbd_items').append($tr);
}

function getDirectionStr(dir) {
	switch(dir){
		case "1":
		return "Up";
		case "2":
		return "Dowm";
		case "3":
		return "Left";
		default:			
		return "Right";
	}
}

function showPatternItme(item) {
	var $div = $("<div/>", {
		class: "pattern_row"
	});

	var $h3 = $("<h3/>");	
	$h3.css('padding-left', '0.5rem');
	$h3.text('Jessica Jones');
	$div.append($h3);

	var $div2 = $("<div/>", {
		class : "h5 font-weight-300"
	});	
	$div2.html('<i class="ni location_pin mr-2"></i>' + "Test String.");
	$div.append($div2);

	$('#pattern_list').append($div);
}

$(document).ready(function() {
	$('#btn_item_setup').on('click', function() {
		addItemRow();
	});

	$('#btn_reflash').on('click', function() {
		showPatternItme("");
	});
});
