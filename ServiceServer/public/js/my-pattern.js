var mCode;
var mName;
var itemCnt = 0;
var itemList = [];
var selectItemId = -1;
var selectPatternId = -1;

var myPatterns;

function setAccessInfos(session) {
	mCode = session.code;
	mName = session.name;
	console.log("Session : " + mCode + ", " + mName);

	setSignContent();

	if(mCode != undefined){
		getMyPattern();
	}
}

function setSignContent() {
	if(mCode != undefined){
		$('#auth_state').text('Logout');
		$("#auth_link").attr("href", "/manager/auth/logout");
		$('#client_title1').text(mName);
		$('#client_title2').text('발&발목 치료를 위한 운동패턴 정보의 설정 & 공유를 통해 내원 환자들에게 다양하고 효율적인 서비스의 제공이 가능힙니다.');
	}else{
		$('#client_title1').text('Undefined!');
		$('#client_title2').text('발&발목 운동패턴 정보를 등록 또는 관리하기 위한 먼저 관리자 인증을 진행하세요.');
	}
}

function setItemInfo() {
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

	if(selectItemId == -1){
		addItemRow(rowInfo);
	}else{
		updateItemRow(rowInfo);
	}
}

function updateItemRow(info) {
	const pos = itemList.findIndex(function(item) {return item.no == selectItemId});
	itemList[pos].info = info;

	$('#' + selectItemId).find("td").eq(0).html(getDirectionStr(info.leftDir));
	$('#' + selectItemId).find("td").eq(1).html(info.leftAngle + " °");
	$('#' + selectItemId).find("td").eq(2).html(info.leftSpeed + " Phase");
	$('#' + selectItemId).find("td").eq(3).html(info.lefthold + " Sec");
	if(info.leftReturn){
		$('#' + selectItemId).find("td").eq(4).html("<span class='badge badge-dot mr-4'><i class='bg-success'></i> return<span/>");
	}else{
		$('#' + selectItemId).find("td").eq(4).html("<span class='badge badge-dot mr-4'><i class='bg-warning'></i> holding<span/>");
	}

	$('#' + selectItemId).find("td").eq(5).html(getDirectionStr(info.rightDir));
	$('#' + selectItemId).find("td").eq(6).html(info.rightAngle + " °");
	$('#' + selectItemId).find("td").eq(7).html(info.rightSpeed + " Phase");
	$('#' + selectItemId).find("td").eq(8).html(info.righthold + " Sec");
	if(info.rightReturn){
		$('#' + selectItemId).find("td").eq(9).html("<span class='badge badge-dot mr-4'><i class='bg-success'></i> return<span/>");
	}else{
		$('#' + selectItemId).find("td").eq(9).html("<span class='badge badge-dot mr-4'><i class='bg-warning'></i> holding<span/>");
	}
}

function addItemRow(item) {
	itemCnt++;
	console.log("Item Cnt : " + itemCnt);

	itemList.push({"no" : itemCnt, "info" : item});

	var $tr = $("<tr/>");
	$tr.attr('id', itemCnt);
	$tr.on('click', function() {
		// const pos = itemList.findIndex(function(item) {return item.no == $tr.attr('id')});
		const itemId = $tr.attr('id');
		if(selectItemId != -1){
			$('#' + selectItemId).css("background", 'transparent');
		}
		selectItemId = itemId == selectItemId ? -1 : itemId;
		if(selectItemId != -1){
			$tr.css("background","#ABC7EB");
			console.log("Selected item Id : " + selectItemId);
		}
	});

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
	if(item.leftReturn){
		$td_left_hold.html("<span class='badge badge-dot mr-4'><i class='bg-success'></i> return<span/>");
	}else{
		$td_left_hold.html("<span class='badge badge-dot mr-4'><i class='bg-warning'></i> holding<span/>");
	}
	$tr.append($td_left_hold);

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
	if(item.rightReturn){
		$td_right_hold.html("<span class='badge badge-dot mr-4'><i class='bg-success'></i> return<span/>");
	}else{
		$td_right_hold.html("<span class='badge badge-dot mr-4'><i class='bg-warning'></i> holding<span/>");
	}
	$tr.append($td_right_hold);

	var $td_del_btn = $("<td/>");
	var $i_del = $("<i/>", {
		class: "ni ni-fat-delete click_cursor"
	});
	// Click event
	$i_del.on('click', function() {
		const pos = itemList.findIndex(function(item) {return item.no == $tr.attr('id')})
		if (pos > -1){
			itemList.splice(pos, 1);
		} 
		$tr.remove();
		checkItemSize();
	});
	$tr.append($td_del_btn.append($i_del));

	$('#tbd_items').append($tr);
	checkItemSize();
}

function checkItemSize() {
	var rowCount = $('#tb_items >tbody >tr').length;
	if(rowCount != 0){
		$('#nofity_item_cnt').css('display', 'none');
	}else{
		$('#nofity_item_cnt').css('display', 'block');
	}
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

function getMyPattern() {
	$.ajax({
		type: "POST",
		dataType: 'json',
		data: {"hCode" : mCode},
		url: '/pattern/list',
		timeout: 2000,
		success: function(res) {
			console.log("pattern cnt : " + res.rows.length);
			if(res.rows.length == 0){
				$('#nofity_pattern_cnt').css('display', 'block');				
			}else{
				$('#nofity_pattern_cnt').css('display', 'none');
				myPatterns = res.rows;
				for (const pattern of res.rows){
					showMyPatterns(pattern);
				}
			}
		}
	});
}

function showMyPatterns(pattern) {
	var $div = $("<div/>", {
		class: "pattern_row"
	});
	console.log(pattern);

	$div.css('padding', '0.2rem');
	$div.attr('name', pattern.pCode);
	$div.on('click', function() {
		selectPatternId = $div.attr('name');
		console.log("Selected pattern Id : " + selectPatternId);
		showSelectInfos();
	});


	var $h3 = $("<h3/>");	
	$h3.css('padding-left', '0.5rem');
	$h3.text(pattern.pName);
	$div.append($h3);

	var $div2 = $("<div/>", {
		class : "h5 font-weight-300"
	});	
	$div2.html('<i class="ni location_pin mr-2"></i>' + pattern.explanation);
	$div.append($div2);

	$('#pattern_list').append($div);
}

function savePattern() {
	var name = $("#pattern_name").val();
	var keyword = $('#pattern_keyword').val();
	var explanation = $('#pattern_explanation').val();

	if(name == "" || keyword == "" || explanation == "" || itemList.length == 0){
		alert("패턴 정보를 설정하세요.");
		return;
	}
	var date = new Date();
	var today = date.getFullYear() + "-" + (date.getMonth() + 1) +  "-" + date.getDate();

	var reqData = {
		"patternId" : selectPatternId,
		"hCode": mCode,
		"hName": mName,
		"name": name,
		"keyword": keyword,
		"explanation": explanation,
		"today" : today,
		"items" : JSON.stringify(itemList)
	};

	$.ajax({
		type: "POST",
		dataType: 'json',
		data: reqData,
		url: '/pattern/setting',
		timeout: 2000,
		success: function(res) {
			if(res.result == 1001){
				alert("패턴 정보가 설정되었습니다.");
				location.reload();
			}else{
				alert("패턴정보 설정 오류(" +  res.result + ")");
			}
		}
	});
}


function showSelectInfos() {
	const pos = myPatterns.findIndex(function(item) {return item.pCode == selectPatternId})
	const info = myPatterns[pos];
	console.log(info);

	$("#pattern_name").val(info.pName);
	$("#pattern_keyword").val(info.keyword);
	$("#pattern_explanation").val(info.explanation);

	$.ajax({
		type: "POST",
		dataType: 'json',
		data: {"pCode" : selectPatternId},
		url: '/pattern/item/list',
		timeout: 2000,
		success: function(res) {
			if(res.result == 1001){		
				itemList = [];
				itemCnt = 0;
				selectItemId = -1;
				$('#tb_items tbody').empty();
				for (const obj of res.rows){
					var leftMotion = obj.leftMove.split(',');
					var rightMotion = obj.rightMove.split(',');;
					var rowInfo = {
						"leftDir" : leftMotion[0],
						"leftAngle" : leftMotion[1],
						"leftSpeed" : leftMotion[2],
						"lefthold" : leftMotion[3],
						"leftReturn" : leftMotion[4] == "1",
						"rightDir" : rightMotion[0],
						"rightAngle" : rightMotion[1],
						"rightSpeed" : rightMotion[2],
						"righthold" : rightMotion[3],
						"rightReturn" : rightMotion[4] == "1",
					};
					addItemRow(rowInfo);
				}				
			}
		}
	});
}


function deletePattern() {
	$.ajax({
		type: "POST",
		dataType: 'json',
		data: {"pCode" : selectPatternId},
		url: '/pattern/delete',
		timeout: 2000,
		success: function(res) {
			if(res.result == 1001){
				alert("패턴 정보가 삭제되었습니다.");
				location.reload();
			}else{
				alert("패턴정보 삭제 오류(" +  res.result + ")");
			}
		}
	});
}


$(document).ready(function() {
	$('#btn_item_setup').on('click', function() {
		if(mCode != undefined){
			setItemInfo();
		}					
	});

	$('#btn_reflash').on('click', function() {
		location.reload();
	});

	$('#btn_save_info').on('click', function() {
		if(mCode != undefined){
			savePattern();
		}		
	});

	$('#btn_del_info').on('click', function() {
		if(selectPatternId != -1)
			deletePattern();
	});
});
