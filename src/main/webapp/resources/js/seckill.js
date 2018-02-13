var seckill = {
  URL: {
    now: function () {
      return '/seckill/time/now';
    },
    exposer: function (seckillId) {
      return '/seckill/' + seckillId + '/exposer';
    },
    execution: function (seckillId, md5) {
      return '/seckill/' + seckillId + '/' + md5 + '/execution';
    },
    list: function (pageNumber, currentPage) {
      if (!pageNumber || pageNumber <= 0) {
        pageNumber = 4;
      } else {
        pageNumber = $('#pageNumber').val();
      }
      if (!currentPage || pageNumber <= 0) {
        currentPage = 1;
      }
      return '/seckill/list?pageNumber=' + pageNumber + '&currentPage='
          + currentPage;
    }
  },
  list: {
    execute: function (pageNumber, currentPage) {
      var listUrl = seckill.URL.list(pageNumber, currentPage);
      console.log("list url: " + listUrl);
      //replace使用listurl得到的结果替换当前页面
      location.replace(listUrl);
    }
  },
  validatePhone: function (phone) {
    if (phone && phone.length == 11 && !isNaN(phone)) {
      return true;
    } else {
      return false;
    }
  },
  //处理秒杀逻辑:获取秒杀地址，控制显示逻辑，执行秒杀。
  handleSeckill: function (seckillId, node) {
    node.hide().html(
        '<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
    //回调函数中执行秒杀交互
    $.post(seckill.URL.exposer(seckillId), {}, function (result) {
      if (result && result.success) {
        var exposer = result.result;
        if (exposer.exposed) {
          //根据获得的md5，组合秒杀url
          var md5 = exposer.md5;
          var killUrl = seckill.URL.execution(seckillId, md5);
          console.log('killUrl: ' + killUrl);
          //绑定一次点击事件，防止多次点击
          $('#killBtn').one('click', function () {
            //禁用按钮
            $(this).addClass('disabled');
            //发送秒杀请求
            $.post(killUrl, {}, function (result) {
              if (result && result.success) {
                var stateInfo = result.result.stateInfo;
                node.html('<span class="label label-success">' + stateInfo
                    + '</span>');
              }
            });
          });
          node.show();
        } else {
          //客户端时间和服务器时间偏差,还未开启秒杀,则重新执行倒计时逻辑
          var now = exposer.now;
          var start = exposer.start;
          var end = exposer.end;
          seckill.countdown(seckillId, now, start, end);
        }
      } else {
        console.log('result: ' + result);
      }
    });

  },
  //计时交互过程
  countdown: function (seckillId, currentTime, startTime, endTime) {
    console.log("currentTime: " + currentTime + ", startTime: " + startTime
        + ", endTime" + endTime);
    var seckillBox = $('#seckill-box');
    //根据当前时间判断操作
    if (currentTime > endTime) {
      seckillBox.html('秒杀结束');
    } else if (currentTime < startTime) {
      console.log('start countdown');
      //计时事件绑定,killTime为基准时间,+1s防止时间偏移
      var killTime = new Date(startTime + 1000);
      seckillBox.countdown(killTime, function (event) {
        var fomat = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒');
        console.log('fomat: ' + fomat);
        seckillBox.html(fomat);
      }).on('finish.countdown', function () {
        seckill.handleSeckill(seckillId, seckillBox);
      });
    } else {
      seckill.handleSeckill(seckillId, seckillBox);
    }
  },
  //详情页的逻辑
  detail: {
    init: function (params) {
      //登录交互
      //cookie中取出手机号
      var killPhone = $.cookie('killPhone');
      //验证手机号
      if (!seckill.validatePhone(killPhone)) {
        var killPhoneModal = $('#killPhoneModal');
        //显示弹出层
        killPhoneModal.modal({
          show: true,//显示弹出层
          backdrop: 'static',//禁止点击其他位置关闭
          keyboard: false//关闭键盘事件
        });
        //输入手机号
        $('#killPhoneBtn').click(function () {
          var inputPhone = $('#killPhoneKey').val();
          console.log('inputPhone=' + inputPhone);
          if (seckill.validatePhone(inputPhone)) {
            //  电话写入cookie
            $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'})
            //刷新页面
            window.location.reload();
          } else {
            $('#killPhoneMessage').hide().html(
                '<lable class="label label-danger">手机号错误</lable>').show(300);
          }
        });
      }

      //  计时交互
      var startTime = params.startTime;
      var endTime = params.endTime;
      var seckillId = params.seckillId;
      $.get(seckill.URL.now(), {}, function (result) {
        if (result && result.success) {
          //计时交互
          var currentTime = result.result;
          seckill.countdown(seckillId, currentTime, startTime, endTime);
        } else {
          console.log('result: ' + result);
        }
      });
    }
  }
};