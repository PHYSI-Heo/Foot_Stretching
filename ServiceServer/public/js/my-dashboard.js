var mCode;
var mName;

var publicPatterns;
var fileterPatterns;
var myPatterns;

function setAccessInfos(session) {
	mCode = session.code;
	mName = session.name;
	console.log("Session : " + mCode + ", " + mName);

	setSignContent();
	getTotalPatterns(); 

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
		$('#client_title2').text('운동패턴 정보를 등록 또는 관리하기 위한 먼저 관리자 인증을 진행하세요.');
	}
}


function getTotalPatterns() {
	$.ajax({
		type: "POST",
		dataType: 'json',
		data: null,
		url: '/pattern/list',
		timeout: 2000,
		success: function(res) {
			if(res.result == 1001){
				$('#tb_items tbody').empty();
				fileterPatterns = publicPatterns = res.rows;
				setPatternInfos();
				for(const pattern of fileterPatterns){
					addPublicPatternRow(pattern);
				}
			}
		}
	});
}


function getMyPattern() {
	$.ajax({
		type: "POST",
		dataType: 'json',
		data: {"hCode" : mCode},
		url: '/pattern/hospital/list',
		timeout: 2000,
		success: function(res) {
			if(res.result == 1001){
				myPatterns = res.rows;
				for(const pattern of myPatterns){
					addMyPatternRow(pattern);
				}
			}
		}
	});
}



function setPatternInfos() {
	var today = new Date(); 
	var dayOfWeek = today.getDay(); 
	var day = today.getDate(); 
	var month = today.getMonth(); 
	var year = today.getYear(); 
	year += (year < 2000) ? 1900 : 0; 

	var weekStartDate = new Date(year, month, day - dayOfWeek); 
	var weekEndDate = new Date(year, month, day + (6 - dayOfWeek));

	var masterCnt = 0;
	var myCnt = 0;
	var weekItemCnt = 0;
	var weekMasterCnt = 0;
	var weekMyCnt = 0;
	for(const obj of publicPatterns){
		var setDate = obj.latDate == null ? obj.addDate : obj.lastDate;
		var dateValues = setDate.split("-");
		var updateDate = new Date(dateValues[0], Number(dateValues[1]) - 1, dateValues[2]);
		console.log("Update Date : " + updateDate);
		if(weekStartDate.getTime() <= updateDate.getTime() && weekEndDate.getTime() >= updateDate.getTime()){
			weekItemCnt++;
			if(obj.hCode == 1)
				weekMasterCnt++;
			if(obj.hCode == mCode)
				weekMyCnt++;
		}

		if(obj.hCode == 1)
			masterCnt++;
		if(obj.hCode == mCode)
			myCnt++;
	}

	if(weekItemCnt > 0){
		$('#span_week_total').addClass("text-success");
		$('#span_week_total').html('<i class="fa fa-arrow-up"></i>&nbsp&nbsp' + weekItemCnt + '</span>');
	}else{
		$('#span_week_total').addClass("text-danger");
		$('#span_week_total').html('<i class="fa fa-minus"></i>&nbsp&nbsp0</span>');
	}

	if(weekMasterCnt > 0){
		$('#span_week_master').addClass("text-success");
		$('#span_week_master').html('<i class="fa fa-arrow-up"></i>&nbsp&nbsp' + weekMasterCnt + '</span>');
	}else{
		$('#span_week_master').addClass("text-danger");
		$('#span_week_master').html('<i class="fa fa-minus"></i>&nbsp&nbsp0</span>');
	}

	var weekUserCnt = weekItemCnt - weekMasterCnt;
	if(weekUserCnt > 0){
		$('#span_week_user').addClass("text-success");
		$('#span_week_user').html('<i class="fa fa-arrow-up"></i>&nbsp&nbsp' + weekUserCnt + '</span>');
	}else{
		$('#span_week_user').addClass("text-danger");
		$('#span_week_user').html('<i class="fa fa-minus"></i>&nbsp&nbsp0</span>');
	}

	if(weekMyCnt > 0){
		$('#span_week_my').addClass("text-success");
		$('#span_week_my').html('<i class="fa fa-arrow-up"></i>&nbsp&nbsp' + weekMyCnt + '</span>');
	}else{
		$('#span_week_my').addClass("text-danger");
		$('#span_week_my').html('<i class="fa fa-minus"></i>&nbsp&nbsp0</span>');
	}

	$('#span_total_cnt').html(publicPatterns.length);
	$('#span_master_cnt').html(masterCnt);	
	$('#span_users_cnt').html(publicPatterns.length - masterCnt);
	$('#span_my_cnt').html(myCnt);	
}


function addMyPatternRow(info) {
	var $tr = $("<tr/>");
	$tr.attr('name', info.pCode);	
	const pos = publicPatterns.findIndex(function(item) {return item.pCode == info.pCode});

	var $td_hospital = $("<td/>");
	$td_hospital.html(publicPatterns[pos].hName);
	$tr.append($td_hospital);

	var $td_pattern = $("<td/>");
	$td_pattern.html(info.pName);
	$tr.append($td_pattern);


	var $td_explanation = $("<td/>");
	$td_explanation.html(publicPatterns[pos].explanation);
	$tr.append($td_explanation);

	var $td_detail_info = $("<td/>");
	var $i_detail_info = $("<i/>", {
		class: "ni ni-align-left-2 click_cursor"
	});
	$tr.append($td_detail_info.append($i_detail_info));

	var $td_delete = $("<td/>");
	var $i_delete = $("<i/>", {
		class: "ni ni-fat-remove click_cursor"
	});
	$tr.append($td_delete.append($i_delete));

	// Click event
	$i_detail_info.on('click', function() {		
		getDetailPatternInfo($tr.attr('name'));
	});

	$i_delete.on('click', function() {
		const pos = myPatterns.findIndex(function(item) {return item.pCode == $tr.attr('name')})
		if (pos > -1){
			myPatterns.splice(pos, 1);
		} 
		$tr.remove();
		checkMyPatternSize();
	});

	$('#tbd_my_pattern').append($tr);
	checkMyPatternSize();
}


function checkMyPatternSize() {
	var rowCount = $('#tb_patterns >tbody >tr').length;
	$('#span_setting_cnt').html(rowCount);	
	if(rowCount != 0){
		$('#nofity_myPattern_cnt').css('display', 'none');
	}else{
		$('#nofity_myPattern_cnt').css('display', 'block');
	}
}



function addPublicPatternRow(info) {	
	var $tr = $("<tr/>");
	$tr.attr('id', info.pCode);	

	var $td_hospital = $("<td/>");
	$td_hospital.html(info.hName);
	$tr.append($td_hospital);

	var $td_pattern = $("<td/>");
	$td_pattern.html(info.pName);
	$tr.append($td_pattern);

	var $td_explanation = $("<td/>");
	$td_explanation.html(info.explanation);
	$tr.append($td_explanation);

	var $td_motion_time = $("<td/>");
	$td_motion_time.html(info.totalTime + " Sec");
	$tr.append($td_motion_time);
	
	var $td_last_date = $("<td/>");
	if(info.lastDate == null || info.lastDate == "")
		$td_last_date.html(info.addDate);
	else
		$td_last_date.html(info.lastDate);
	$tr.append($td_last_date);

	var $td_detail_info = $("<td/>");
	var $i_detail_info = $("<i/>", {
		class: "ni ni-align-left-2 click_cursor"
	});
	$tr.append($td_detail_info.append($i_detail_info));

	var $td_set_pattern = $("<td/>");
	var $i_set_pattern = $("<i/>", {
		class: "ni ni-bold-right click_cursor"
	});
	$tr.append($td_set_pattern.append($i_set_pattern));

	// Click event
	$i_detail_info.on('click', function() {
		getDetailPatternInfo($tr.attr('id'));
	});

	$i_set_pattern.on('click', function() {
		appendMyPattern($tr.attr('id'));		
	});

	$('#tbd_items').append($tr);
	checkPublichPatternSize();
}

function appendMyPattern(id) {
	var pos = myPatterns.findIndex(function(item) {return item.pCode == id});
	if (pos == -1){
		pos = publicPatterns.findIndex(function(item) {return item.pCode == id});
		var obj = {
			"pCode" : publicPatterns[pos].pCode,
			"pName" : publicPatterns[pos].pName,
			"hCode" : mCode,
			"keyword" : publicPatterns[pos].keyword,
		};
		myPatterns.push(obj);
		addMyPatternRow(obj);
	} 	
}

function checkPublichPatternSize() {
	var rowCount = $('#tb_items >tbody >tr').length;
	if(rowCount != 0){
		$('#nofity_item_cnt').css('display', 'none');
	}else{
		$('#nofity_item_cnt').css('display', 'block');
	}
}


function showSalfPattern() {
	$('#tb_items tbody').empty();
	fileterPatterns = [];
	for(const pattern of publicPatterns){
		if(pattern.hCode == mCode)
			fileterPatterns.push(pattern);
	}
	for(const pattern of fileterPatterns){
		addPublicPatternRow(pattern);
	}
}


function updateMyPatterns() {
	$.ajax({
		type: "POST",
		dataType: 'json',
		data: {"items" : JSON.stringify(myPatterns)},
		url: '/pattern/hospital/setting',
		timeout: 2000,
		success: function(res) {
			if(res.result == 1001){
				alert("패턴운동 항목이 갱신되었습니다.");
				location.reload();
			}
		}
	});
}

function keywordFilter(word) {
	console.log("Keyword : " + word);
	if(word == null || word.length == 0)
		return;
	$('#tb_items tbody').empty();
	var filtering = [];
	for(const pattern of fileterPatterns){
		console.log(pattern);
		if(pattern.keyword.indexOf(word) != -1){			
			filtering.push(pattern);
		}
	}
	for(const pattern of filtering){
		addPublicPatternRow(pattern);
	}
}

function getDetailPatternInfo(id) {
	$.ajax({
		type: "POST",
		dataType: 'json',
		data: {"pCode" : id},
		url: '/pattern/item/list',
		timeout: 2000,
		success: function(res) {
			if(res.result == 1001){		
				for (const obj of res.rows){
					var leftMotion = obj.leftMove.split(',');
					var rightMotion = obj.rightMove.split(',');;
					var rowInfo = {
						"leftDir" : leftMotion[0],
						"leftAngle" : leftMotion[1],
						"leftSpeed" : leftMotion[2],
						"lefthold" : leftMotion[3],
						"leftReturn" : leftMotion[4],
						"rightDir" : rightMotion[0],
						"rightAngle" : rightMotion[1],
						"rightSpeed" : rightMotion[2],
						"righthold" : rightMotion[3],
						"rightReturn" : rightMotion[4],
					};
					addItemRow(rowInfo);
				}	
				$('#detail_popup').css('display', 'block');			
			}
		}
	});
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

function addItemRow(item) {
	console.log(item);
	var $tr = $("<tr/>");
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
	$td_left_hold.html(item.leftReturn);
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
	$td_right_hold.html(item.rightReturn);
	$tr.append($td_right_hold);

	$('#tbd_detail_pattern').append($tr);
}

$(document).ready(function() {	

	$('#btn_all_pattern').on('click', function() {
		$('#search_keyword').val("");
		$('#tb_items tbody').empty();
		fileterPatterns = publicPatterns;
		for(const pattern of fileterPatterns){
			addPublicPatternRow(pattern);
		}
	});

	$('#btn_my_pattern').on('click', function() {
		if(mCode != undefined){
			$('#search_keyword').val("");
			showSalfPattern();
		}		
	});

	$('#btn_my_setup').on('click', function() {
		if(mCode != undefined){
			updateMyPatterns();
		}		
	});

	$("#search_keyword").keydown(function(key) {
		if (key.keyCode == 13) {
			keywordFilter($('#search_keyword').val());
		}
	});

	$('#btn_popup_close').on('click', function() {
		$('#detail_popup').css('display', 'none');
	});

});
