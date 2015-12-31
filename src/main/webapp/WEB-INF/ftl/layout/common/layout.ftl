<#macro layout  title currpos content="text/html; charset=utf-8">
<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]>
<#assign ctx=rc.contextPath>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>${title}</title>
    <link rel="alternate icon" type="image/png" href="${e.res('/imgs/favicon.png')}">
    <!-- bootstrap -->
    <link href="${e.res('/bootstrap/css/bootstrap.min.css')}" rel="stylesheet">
    <!-- icon-->
    <link href="${e.res('/css/icon.min.css')}" rel="stylesheet">
    <!-- layout.css-->
    <link href="${e.res('/css/layout.css')}" rel="stylesheet">
    <!-- base.css-->
    <link href="${e.res('/css/base.css')}" rel="stylesheet">
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file://-->
    <!--<script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>-->
    <!--<script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>-->
    <!--[if lt IE 9]>
    <script src="${e.res('/bootstrap/js/respond.min.js')}"></script>
    <script src="${e.res('/bootstrap/js/html5shiv.min.js')}"></script>
    <![endif]-->
    <!--[if lt IE 9]>
    <script src="${e.res('/bootstrap/js/css3-mediaqueries.min.js')}"></script>
    <![endif]-->
    <!--[if IE 7]>
    <style type="text/css">
        #sidebar-menu div.menu-panel div.link-wrapper {
            display: inline;
            zoom: 1;
        }
    </style>
    <![endif]-->


</head>
<body>
<div id="container">
    <#include "../../layout/header.ftl">

    <#nested>

    <#include "../../layout/footer.ftl">
</div>
<div class="side-bar">
    <a href="#" class="icon-qq" title="QQ客服"><i class="am-icon-qq am-icon-sm"></i>
    </a>
    <a href="#" class="icon-chat" title="微信公共平台"><i class="am-icon-wechat am-icon-sm"></i>

        <div class="chat-tips"><i></i>
            <img src="${e.res('/img/wechat.png')}" alt="微信订阅号">
        </div>
    </a>
    <a target="_blank" href="" class="icon-blog" title="微博"><i class="am-icon-weibo am-icon-sm"></i></a>
    <a href="http://www.helloweba.com/gbook.html" class="icon-mail" title="邮箱"><i
            class="am-icon-envelope am-icon-sm"></i></a>
</div>
<!-- jQuery 2.1.4 (necessary for Bootstrap's JavaScript plugins)-->
<!--<script src="jquery/jquery.min.js"></script>-->
<script src="http://cdn.bootcss.com/jquery/1.10.2/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/jquery-browser/0.0.7/jquery.browser.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<!--<script src="bootstrap/js/bootstrap.min.js"></script>-->
<script src="http://cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<!--Bootstrap-typeahead-->
<script src="${e.res('/bootstrap/js/bootstrap-typeahead.min.js')}"></script>
<!-- app.js 自定义JS -->
<script src="${e.res('/js/app.js')}"></script>
<!--jquery插件集合-->
<script src="${e.res('/js/common.js')}"></script>
</body>
</html>
</#macro>