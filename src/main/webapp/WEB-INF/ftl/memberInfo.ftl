<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]>
<#assign ctx=rc.contextPath>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>基本信息</title>
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
<div class="am-g">
    <div class="am-u-sm-centered am-u-lg-7">
        <h1 style="width: 100px;margin: 10px auto;color:#0e90d2;">会员基本资料</h1>
    <@form.form class="am-form am-form-horizontal" id="register-form" action="${ctx}/addMemberBaseInfo" method="post">
        <input type="hidden" name="id" value="${memberId}"/>
        <div class="am-form-group">
            <label for="doc-ipt-3" class="am-u-sm-2 am-form-label">公司名称</label>
            <div class="am-u-sm-10">
                <input type="text" id="doc-ipt-3" name="companyName" class="required display-inline" style="width: 70%"
                       placeholder="公司名称">
            </div>
        </div>

        <div class="am-form-group">
            <label for="doc-ipt-3" class="am-u-sm-2 am-form-label">会员类型</label>
            <div class="am-u-sm-10">
                <input type="radio" id="enterpriseMemberRole"  name="enterpriseMemberRole" value="1" class="required display-inline" style="width: 20%">厂家</input>
                <input type="radio" id="enterpriseMemberRole"  name="enterpriseMemberRole" value="2" class="required display-inline" style="width: 20%">代理商</input>
                <input type="radio" id="enterpriseMemberRole"  name="enterpriseMemberRole" value="3" class="required display-inline" style="width: 20%">工程商</input>
                <input type="radio" id="enterpriseMemberRole"  name="enterpriseMemberRole" value="4" class="required display-inline" style="width: 20%">渠道商户</input>
                <input type="radio" id="enterpriseMemberRole"  name="enterpriseMemberRole" value="5" class="required display-inline" style="width: 20%">企事业单位</input>
                <input type="radio" id="enterpriseMemberRole"  name="enterpriseMemberRole" value="6" class="required display-inline" style="width: 20%">行业协会</input>
            </div>
        </div>
        <div class="am-form-group">
            <label for="doc-ipt-3" class="am-u-sm-2 am-form-label">资质</label>
            <div class="am-u-sm-10">
                <input type="text" id="enterpriseLevel"  name="enterpriseLevel" class="required display-inline" style="width: 70%"
                       placeholder="资质">
            </div>
        </div>
        <div class="am-form-group">
            <label for="doc-ipt-3" class="am-u-sm-2 am-form-label">公司联系人姓名</label>
            <div class="am-u-sm-10">
                <input type="text" id="companyLinkName"  name="companyLinkName" class="required display-inline" style="width: 70%"
                       placeholder="公司联系人姓名">
            </div>
        </div>
         <div class="am-form-group">
            <label for="doc-ipt-3" class="am-u-sm-2 am-form-label">公司电话</label>
            <div class="am-u-sm-10">
                <input type="text" id="companyTelephone"  name="companyTelephone" class="required display-inline" style="width: 70%"
                       placeholder="公司电话">
            </div>
        </div>
        <div class="am-form-group">
            <label for="doc-ipt-3" class="am-u-sm-2 am-form-label">地址</label>
            <div class="am-u-sm-10">
                <input type="text" id="address"  name="address" class="required display-inline" style="width: 70%"
                       placeholder="地址">
            </div>
        </div>
        <div class="am-form-group">
            <label for="doc-ipt-3" class="am-u-sm-2 am-form-label">公司LOGO</label>
            <div class="am-u-sm-10">
                <input type="text" id="enterpriseLogo"  name="enterpriseLogo" class="required display-inline" style="width: 70%"
                       placeholder="上传公司LOGO">
            </div>
        </div>
        <div class="am-form-group">
            <label for="doc-ipt-pwd-2" class="am-u-sm-2 am-form-label">企业介绍</label>
            <div class="am-u-sm-10">
            	<input type="text" id="enterpriseDesc"  name="enterpriseDesc" class="required display-inline" style="width: 70%"
                   placeholder="企业介绍">
            </div>
        </div>

        <div class="am-form-group">
            <div class="am-u-sm-10 am-u-sm-offset-2">
                <button id="btnreg" type="submit" class="am-btn am-btn-primary">保存</button>
                <!--<span style="color:red;font-size: 13px;margin-left: 20px;">用户名已存在</span>-->
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
        $('#register-form').validate({
//            submitHandler:function(form){
//                alert("submitted");
//                form.submit();
//            $(form).ajaxSubmit();    //ajax form 提交
//            },
//            debug: true,
            errorElement: "span",
            success: "valid",
            rules: {
                password: {
                    required: true,
                    minlength: 6
                } ,
                confirm_password: {
                    required: true,
                    minlength: 6,
                    equalTo: "#password"
                }
            },
            messages: {
                username: {
                    required: "请输入手机号码"
                },
                password: {
                    required: "请输入密码",
                    minlength: $.format("密码不能小于{0}个字符")
                } ,
                confirm_password:{
                    required: "请输入确认密码",
                    minlength: $.format("密码不能小于{0}个字符"),
                    equalTo: "两次输入密码不一致"
                }
            }
        });
    })
</script>
</body>
</html>