<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>登录</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="format-detection" content="telephone=no">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="alternate icon" type="image/png" href="${e.res('/img/favicon.png')}">
    <link rel="stylesheet" href="${e.res('/css/base.css')}"/>
    <link rel="stylesheet" href="${e.res('/css/common.css')}"/>
    <style>
        .header {
            text-align: center;
        }

        .header h1 {
            font-size: 200%;
            color: #333;
            margin-top: 30px;
        }

        .header p {
            font-size: 14px;
        }
    </style>
</head>
<body>
<div class="header">
    <div class="am-g">
        <h1>筑慧宝</h1>

        <p>通天下,走四方</p>
    </div>
    <hr/>
</div>
<div class="am-g">
    <div class="am-u-sm-centered am-u-lg-7">
        <h1 style="width: 100px;margin: 10px auto;color:#0e90d2;">登录</h1>
        <@form.form class="am-form am-form-horizontal" id="login-form" action="/login" method="post">
            <div class="am-form-group">
                <label for="doc-ipt-3" class="am-u-sm-2 am-form-label">手机号码</label>
                <div class="am-u-sm-10">
                    <input type="text" id="doc-ipt-3" name="username" class="required mobileZH display-inline" style="width: 70%"
                           placeholder="输入你的手机号码">
                </div>
            </div>

            <div class="am-form-group">
                <label for="doc-ipt-pwd-2" class="am-u-sm-2 am-form-label">密码</label>
                <div class="am-u-sm-10">
                    <input type="password" id="doc-ipt-pwd-2"  name="password" class="required display-inline" style="width: 70%"
                           placeholder="请输入密码">
                </div>
            </div>

            <div class="am-form-group">
                <div class="am-u-sm-offset-2 am-u-sm-10">
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="rememberMe" value="1"> 记住密码
                        </label>
                    </div>
                </div>
            </div>

            <div class="am-form-group">
                <div class="am-u-sm-10 am-u-sm-offset-2">
                    <button type="submit" class="am-btn am-btn-primary">登录</button>
                    <span style="color:red;font-size: 13px;margin-left: 20px;">${error!''}</span>
                </div>
            </div>
        </@form.form>
        <hr>
        <p style="text-align: center">© 2015 W.H.E.O Seven Studio. </p>
    </div>
</div>
<!-- jQuery 2.1.4 (necessary for Bootstrap's JavaScript plugins)-->
<script src="${e.res('/js/jquery.min.js')}"></script>
<script src="${e.res('/js/validate/jquery.validate.min.js')}"></script>
<script src="${e.res('/js/validate/additional-methods.js')}"></script>
<script src="${e.res('/js/validate/location/messages_zh.js')}"></script>
<script>
    $(function () {
        $('#login-form').validate({
//            debug: true,
            errorElement: "span",
            success: "valid",
            rules: {
                password: {
                    required: true,
                    minlength: 6
                }
            },
            messages: {
                username: {
                    required: "请输入手机号码"
                },
                password: {
                    required: "请输入密码",
                    minlength: $.format("密码不能小于{0}个字符")
                }
            }
        });
    })
</script>
</body>
</html>