var md5File;
var cookie_uname;

//获得cookie
function getCookie(cookie_name) {
    var allcookies = document.cookie;
    //索引长度，开始索引的位置
    var cookie_pos = allcookies.indexOf(cookie_name);

    // 如果找到了索引，就代表cookie存在,否则不存在
    if (cookie_pos != -1) {
        // 把cookie_pos放在值的开始，只要给值加1即可
        //计算取cookie值得开始索引，加的1为“=”
        cookie_pos = cookie_pos + cookie_name.length + 1;
        //计算取cookie值得结束索引
        var cookie_end = allcookies.indexOf(";", cookie_pos);

        if (cookie_end == -1) {
            cookie_end = allcookies.length;

        }
        //得到想要的cookie的值
        var value = unescape(allcookies.substring(cookie_pos, cookie_end));
    }
    return value;
}
//删除cookie
function delCookie(name)
{
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval=getCookie(name);
    if(cval!=null)
        document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}


//监听分块上传过程中的时间点
WebUploader.Uploader.register({
    "before-send-file":"beforeSendFile",  // 整个文件上传前
    "before-send":"beforeSend",  // 每个分片上传前
    "after-send-file": "afterSendFile"  // 分片上传完毕
},{
    //时间点1：所有分块进行上传之前调用此函数 ，检查文件存不存在
    beforeSendFile:function(file){
        var deferred = WebUploader.Deferred();
        md5File = hex_md5(file.name+file.size);//根据文件名称，大小确定文件唯一标记，这种方式不赞成使用
        //获得cookie
        cookie_uname=getCookie("uname");
        $.ajax({
            type:"POST",
            url:"/checkFile",
            data:{
                md5File: md5File, //文件唯一标记
                cookie_uname: cookie_uname
            },
            async: false,  // 同步
            dataType:"json",
            success:function(response){
                if(response){  //文件存在，跳过 ，提示文件存在
                    $('#' + file.id).find('p.state').text("文件已存在");
                }else{
                    deferred.resolve();  //文件不存在或不完整，发送该文件
                }
            }
        } , function (jqXHR, textStatus, errorThrown) { //任何形式的验证失败，都触发重新上传
            deferred.resolve();
        } );
        return deferred.promise();
    },
    //时间点2：如果有分块上传，则每个分块上传之前调用此函数  ，判断分块存不存在
    beforeSend:function(block){
        var deferred = WebUploader.Deferred();
        $.ajax({
            type:"POST",
            url:"/checkChunk",
            data:{
                md5File: md5File,  //文件唯一标记
                chunk:block.chunk,  //当前分块下标
            },
            dataType:"json",
            success:function(response){
                if(response){
                    deferred.reject(); //分片存在，跳过
                }else{
                    deferred.resolve();  //分块不存在或不完整，重新发送该分块内容
                }
            }
        }, function (jqXHR, textStatus, errorThrown) { //任何形式的验证失败，都触发重新上传
            deferred.resolve();
        });
        return deferred.promise();
    },
    //时间点3：分片上传完成后，通知后台合成分片
    afterSendFile: function (file) {
        var chunksTotal = Math.ceil(file.size / (5*1024*1024));
        if (chunksTotal >= 1) {
            //合并请求
            var deferred = WebUploader.Deferred();
            $.ajax({
                type: "POST",
                url: "/merge",
                data: {
                    name: file.name,
                    md5File: md5File,
                    chunks: chunksTotal
                },
                cache: false,
                async: false,  // 同步
                dataType: "json",
                success:function(response){
                    if(response){
                        $('#' + file.id).find('p.state').text('上传成功!');
                        $('#' + file.id).removeClass('active');
                        $('#' + file.id).addClass('success');
                        $('#' + file.id).find('#pro-line').removeClass('active');
                        $('#' + file.id).find('#stopup').hide();
                        $('#' + file.id).find('#continueup').hide();
                        // $('#' + file.id).find('.progress').find('#progress_info').addClass('progress-bar-success');
                    }else{
                        $('#' + file.id).find('p.state').text('合成碎片失败!');
                        deferred.reject();
                    }
                }
            })
            return deferred.promise();
        }
    }
});

var uploader = WebUploader.create({
    auto: true,// 选完文件后，是否自动上传。
    swf: 'webuploader/Uploader.swf',// swf文件路径
    server: '/upload',// 文件接收服务端。
    pick: '#picker',// 选择文件的按钮。可选。
    chunked:true,//开启分片上传
    chunkSize:5*1024*1024,//5M
    chunkRetry: 3,//错误重试次数
});


//上传添加参数
uploader.on('uploadBeforeSend', function (obj, data, headers) {
    data.md5File=md5File;
});

// 当有文件被添加进队列的时候
uploader.on( 'fileQueued', function( file ) {
    $("#picker").hide();//隐藏上传框
    // $("#thelist").append( '<div id="' + file.id + '" class="item">' +
    //     '<h4 class="info">' + file.name + '</h4>' +
    //     '<p class="state"></p>' +
    //     '</div>' );
    // var text = '';
    // text = ''+WebUploader.formatSize( file.size )+'';

    $("#thelist tbody").append( '<tr id="' + file.id + '" class="active">'+'<td>'+file.name+'</td>'+'<td>'+WebUploader.formatSize( file.size )+'</td>'+
        '<td><p class="state"></p></td>'+'<td><button class="btn btn-default stopup" id="stopup" onclick="stop()">暂停</button>&nbsp;&nbsp;<button class="btn btn-default continueup"  id="continueup" onclick="upload()">继续</button></td></tr>');
    //
    // $("#thelist #list").append( '<td>'+file.name+'<td>');
    // $("#thelist #list").append('<td>XXXM<td>');
    // $("#thelist #list").append('<td class="info">上传中...<td>');
    // $("#thelist #list").append('<td id="' + file.id + '"><p class="state"></p><td>');
});

function upstop(){
    uploader.stop(true);
}
function continueup(){
    uploader.upload(true);
}
// 文件上传过程中创建进度条实时显示。
uploader.on( 'uploadProgress', function( file, percentage ) {
    var $li = $( '#'+file.id ),
        $percent = $li.find('.progress .progress-bar');

    // 避免重复创建
    if ( !$percent.length ) {
        $percent = $('<td id="pro-line" class="progress progress-striped active">' +
            '<div id="progress_info" class="progress-bar" role="progressbar" style="width: 0%"></div>' +
            '</td>').appendTo( $li ).find('.progress-bar');
    }
    $li.find('p.state').text('上传中!');
    $percent.css( 'width', percentage * 100 + '%' );
});


function exit(){
   delCookie("uname");
   location.href="/";
}
