(function($){
    $.yuenjee = {
        basePath:"www.yuenjee.com/",
        apiPath :"www.yuenjee.com/api/",
        session:{isLogin:false,Authorization:"",userName:""},

        _url:function(prefix,args){

            if(args.length == 0) return prefix;

            var path = prefix + args[0];

            for (var i = 1; i < args.length; ++i){
                path = path.replace(new RegExp("\\{"+(i-1)+"\\}","g"), args[i]);
            }

            return path;
        },

        api: function () {
            return this._url(this.apiPath,arguments);
        },

        url: function(){
            return this._url(this.basePath,arguments);
        },

        isNull: function (data) {
          if (typeof(data)== "undefined"
              || data == null
              || data == ""
          ){
              return true;
          }

          return false;
        },
        
        timeFormat: function (timestamp) {
            if (this.isNull(timestamp)) return "NA";
            var date = new Date();
            date.setTime(timestamp);

            var year = date.getFullYear();
            var month = date.getMonth()+1;
            var day = date.getDate();
            if(month<10) month = "0"+month;
            if(day<10) day = "0"+day;

            var hours = date.getHours();
            var mins = date.getMinutes();
            var secs = date.getSeconds();
            var msecs = date.getMilliseconds();
            if(hours<10) hours = "0"+hours;
            if(mins<10) mins = "0"+mins;
            if(secs<10) secs = "0"+secs;
            if(msecs<10) secs = "0"+msecs;

            return (year+"-"+month+"-"+day+" "+hours+":"+mins+":"+secs);
        },

        auth:function(name,password){
            this.session.userName=name;
            this.session.Authorization= "Basic "+ $.base64.encode(name+":"+password);
            this._saveSession();
        },

        _saveSession:function () {
            $.session.set('yuenjee', JSON.stringify(this.session));
        },

        _loadSession:function(){
            if (this.isNull($.session.get('yuenjee'))){
                return false;
            } else {
                this.session = JSON.parse($.session.get('yuenjee'));
                console.log(this.session);
                return true;
            }
        },

        _init: function () {
            if (!this.isNull(window.yuenjee.basePath)){
                this.basePath = window.yuenjee.basePath + "/";
                this.apiPath = this.basePath + "api/";
            }

            if (!this._loadSession()) {
                this._saveSession();
            }

            this.auth("admin","password");
        },

        ajax: function(url,type,data,success,error,complete){

            var that = this;
            var ajaxData= {headers:{
                Authorization:that.session.Authorization
            }};
            ajaxData.url = url;
            ajaxData.type = type;
            if (null != data) {
                ajaxData.data = JSON.stringify(data);
            }

            if (null != success) {
                ajaxData.success = success;
            }

            if (null != error) {
                ajaxData.error = error;
            }

            if (null != complete) {
                ajaxData.complete = complete;
            }

            $.ajax(ajaxData);
        }
    };

    $.yuenjee._init();

})(jQuery);