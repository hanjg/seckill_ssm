var manager = {
  URL: {
    update: function (seckillId) {
      return '/seckill/single/' + seckillId;
    },
    delete: function (seckillId) {
      return '/seckill/single/' + seckillId;
    },
    insert: function () {
      return '/seckill/single';
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
      return '/seckill/manager?pageNumber=' + pageNumber + '&currentPage='
          + currentPage;
    }
  },
  list: {
    execute: function (pageNumber, currentPage) {
      var listUrl = manager.URL.list(pageNumber, currentPage);
      console.log("list url: " + listUrl);
      //replace使用listurl得到的结果替换当前页面
      location.replace(listUrl);
    }
  },
  update: {
    init: function (seckillId, name, number, startTime, endTime) {
      $('#mainForm  #updateId').val(seckillId);
      $('#mainForm  #name').val(name);
      $('#mainForm  #number').val(number);
      $('#mainForm  #startTime').val(
          dateTime.dateFormat(startTime, dateTime.formatPattern));
      $('#mainForm  #endTime').val(
          dateTime.dateFormat(endTime, dateTime.formatPattern));
      $('#mainForm').attr('hidden', false);
      $('#insertButton').attr('hidden', true);
      $('#updateButton').attr('hidden', false);
    },
    execute: function () {
      var seckillId = $('#mainForm #updateId').val();
      var name = $('#mainForm  #name').val();
      var number = $('#mainForm  #number').val();
      var startTime = dateTime.getDateByFormat(
          $('#mainForm  #startTime').val());
      var endTime = dateTime.getDateByFormat($('#mainForm  #endTime').val());
      var seckill = {
        'seckillId': seckillId,
        'name': name,
        'number': number,
        'startTime': startTime,
        'endTime': endTime
      };
      $.ajax({
        type: "POST",
        url: manager.URL.update(seckillId),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8', //contentType很重要
        data: JSON.stringify(seckill),
        success: function () {
          console.log('succeed update');
          location.reload();
        },
        error: function () {
          console.log('error update');
        }
      });
    }
  },
  insert: {
    init: function () {
      $('#mainForm').attr('hidden', false);
      $('#insertButton').attr('hidden', false);
      $('#updateButton').attr('hidden', true);
    },
    execute: function () {
      var seckillId = $('#mainForm #updateId').val();
      var name = $('#mainForm  #name').val();
      var number = $('#mainForm  #number').val();
      var startTime = dateTime.getDateByFormat(
          $('#mainForm  #startTime').val());
      var endTime = dateTime.getDateByFormat($('#mainForm  #endTime').val());
      var seckill = {
        'seckillId': seckillId,
        'name': name,
        'number': number,
        'startTime': startTime,
        'endTime': endTime
      };
      $.ajax({
        type: "POST",
        url: manager.URL.insert(),
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8', //contentType很重要
        data: JSON.stringify(seckill),
        success: function () {
          console.log('succeed update');
          location.reload();
        },
        error: function () {
          console.log('error update');
        }
      });
    }

  }
  ,
  delete: {
    execution: function (seckillId) {
      $.ajax({
        type: "DELETE",
        url: manager.URL.delete(seckillId),
        success: function () {
          console.log('succeed update');
          location.reload();
        },
        error: function () {
          console.log('error update');
        }
      });
    }
  },
  //设置输入时间的格式,使用datetimepicker的格式化参数
  init: function () {
    $('#startTimePicker').datetimepicker({
      format: 'YYYY-MM-DD HH:mm:ss',
      locale: moment.locale('zh-cn')
    });
    $('#endTimePicker').datetimepicker({
      format: 'YYYY-MM-DD HH:mm:ss',
      locale: moment.locale('zh-cn')
    });
  }
};