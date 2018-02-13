var dateTime = {
  getDateByFormat: function (dateStr) {
    dateStr = dateStr.replace('-', '/');
    return new Date(Date.parse(dateStr));
  },
  dateFormat: function (date, formatStr) {
    // 转换成Date类型
    var date = new Date(date);
    var opt = {
      'yyyy': date.getFullYear(),
      'MM': addZero(date.getMonth() + 1),
      'M': date.getMonth() + 1,
      'dd': addZero(date.getDate()),
      'd': date.getDate(),
      'HH': addZero(date.getHours()),
      'H': date.getHours(),
      'mm': addZero(date.getMinutes()),
      'm': date.getMinutes(),
      'ss': addZero(date.getSeconds()),
      's': date.getSeconds()
    };

    // 如果是个位数则前面添加0
    function addZero(value) {
      return value < 10 ? '0' + value : value;
    }

    // 遍历替换
    for (var k in opt) {
      formatStr = formatStr.replace(k, opt[k]);
    }
    return formatStr;
  },
  formatPattern: 'yyyy-MM-dd HH:mm:ss'
}