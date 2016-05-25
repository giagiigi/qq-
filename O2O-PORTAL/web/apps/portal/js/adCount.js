// @author d12150

/**
 * 保存广告统计数据pv,uv
 */
function saveAdcountData(urlParams, pageType) {
	// 显示页面时间
	var visitTime = (new Date()).valueOf();
	// 模板id
	var templateId = urlParams.templateId;
	var storeId = urlParams.nas_id;
	
	var uri = "/portal/adPageCount?&visitTime=" + visitTime + "&pageType="
			+ pageType + "&templateId=" + templateId +"&storeId=" + storeId;
	$.get(uri).done(function(data) {
		if (typeof (data.error) != 'undefined') {
			alert(data.error);
		} else if (data != "" && data.hasOwnProperty('adList')) {
			var adList = data.adList;
			// 保存页面中的广告
			adCount(urlParams, adList);
		} 
	}).fail(function(data) {
		alert($("#60000").val());
	});

}

/**
 * 取得uv
 * 
 * @returns
 */
function getUV(adId, storeId) {
	// 从cookie中取得uv值
	var uid = adId + "_" + storeId;
	var uv_str = getCookie("lvzhou_uv" + uid);
	// uv值
	var uv_new = 0;

	// cookie中不存在uv字段时，uv=1
	if (uv_str == "") {
		uv_new = 1;

		var rand1 = parseInt(Math.random() * 4000000000);
		var rand2 = parseInt(Math.random() * 4000000000);
		uv_id = String(rand1) + String(rand2);

		var value = uv_id + "|" + uid;

		// uv数存入cookies
		setCookie("lvzhou_uv" + uid, value, 1);
	} else {
		// 存在uv标识时，判断
		var arr = uv_str.split("|");
		uv_id = arr[0];
		var uids_str = arr[1];

		// 判断是否为同一天同一个用户访问

		if (uid == uids_str) {
			// 同一天同一个用户访问
			uv_new = 0;
		} else {
			uv_new = 1;
			// 如果不是同一天同一个用户访问，uv+1
			var value = uv_id + "|" + uid;
			setCookie("lvzhou_uv" + uid, value, 1);
		}
	}
	return uv_new;
}

/**
 * 读取cookie
 * 
 * @param name
 * @returns
 */
function getCookie(name) {
	var searchKey = name + "=";
	// uv标识所在位置
	var valuePositon;
	var cookie = document.cookie;

	if (searchKey == "=") {
		return cookie;
	}
	// 判断cookie是否存在uv的标识
	valuePositon = cookie.indexOf(searchKey);

	if (valuePositon < 0) {
		return "";
	}
	// 分号位置
	var semicolonPositon = cookie.indexOf(";", valuePositon + name.length);

	if (semicolonPositon < 0) {
		// 只有一条记录时，返回uv标识
		return cookie.substring(valuePositon + name.length + 1);
	} else {
		// cookie有多条记录时，返回uv标识
		return cookie.substring(valuePositon + name.length + 1,
				semicolonPositon);
	}
}

/**
 * 设置cookie
 * 
 * @param name
 * @param val
 * @param cotp
 * @returns
 */
function setCookie(name, val, cotp) {
	var vistitDate = new Date();
	var year = vistitDate.getFullYear();
	var month = vistitDate.getMonth();
	var day = vistitDate.getDate();

	var cookie = "";
	var expiresDate = new Date(year, month, day, 23, 59, 59);

	if (cotp == 1) {
		var time = expiresDate.toGMTString();
		cookie = name + "=" + val + ";expires=" + time + ";";
	}
	cookie += "path=" + "/;";
	document.cookie = cookie;
}

/**
 * 统计点击次数
 */
function adClickCount(urlParams, pageType) {
	$(".adClass").on("click", function(e) {
				// 取得广告的链接
				var aLength = $(this).find('a').length;

				if (aLength <= 0) {
					// 广告是非链接时
					return;
				}
				// 阻止跳转，会出现错误
				var href = $(this).find('a').attr("href");
			　　  var target = $(this).find('a').attr("target");
			   	if (typeof(target) == "undefined" || target == "_self") {
					e.preventDefault(); 
				}
				
				// 模板id
				var templateId = urlParams.templateId;
				// 广告id
				var adId = $(this).attr('id');
				// 场所id
				var storeId = urlParams.nas_id;
				var uv = getUV(adId, storeId);
				// 广告点击时间
				var clickTime = (new Date()).valueOf();
				
				var uri = "/portal/adClickCount?adId=" + adId
						+ "&clickTime=" + clickTime + "&pageType=" + pageType
						+ "&templateId=" + templateId + "&uv=" + uv +"&storeId=" + storeId;;

				$.get(uri).done(function(data) {
					if (target == "_self") {
						window.open(href, target)
					}
					if (typeof(target) == "undefined" ) {
						window.location=href;
					}
				}).fail(function(data) {
					alert($("#60000").val());
				});
			});
}

/**
 * 广告展示次数
 * 
 * @param adList
 */
function adCount(urlParams, adList) {
	// 页面展示的广告
	var showAdIds = [];
	// 为广告设置id
	$(".adClass").each(function(index, adElement) {
		var elementId = adElement.id;
		var thisElement = $(this);
		$.each(adList, function(index, adValue) {
			var adId = adValue.id;
			if (elementId == adValue.adElementId) {
				// 设置广告的真正id
				$(thisElement).attr("id",adId);
				showAdIds.push(adId);
			}
		});
	});
	
	// 设置微信商家主页的广告
	$(".wxAdClass").each(function(index, adElement) {
		var elementId = adElement.id;
		var thisElement = $(this);
		$.each(adList, function(index, adValue) {
			var adId = adValue.id;
			if (elementId == adValue.adElementId) {
				// 设置广告的真正id
				$(thisElement).attr("id",adId);
				showAdIds.push(adId);
			}
		});
	});
	
	
	// 保存广告展示数据
	var json=[];
	var storeId = urlParams.nas_id;
	$.each(showAdIds, function(index, id) {
		$.each(adList, function(index, adValue) {
			if (id == adValue.id) {
				// 广告id
				var adId = adValue.id;
				// 取得uv
				var uv = getUV(adId, storeId);
				// 广告访问时间
				var visitTime = (new Date()).valueOf();
				var jsonData = {"adId": adId,  "uv":uv, "visitTime":visitTime};
				json.push(jsonData);
			}
		});
	});
	
	var uri = "/portal/adCount?adJson=" + JSON.stringify(json) + "&storeId=" + storeId;
	
	$.post(uri).done(function(data) {
	}).fail(function(data) {
		alert($("#60000").val());
	});
}