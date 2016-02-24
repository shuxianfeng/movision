<div id="header">
    <div class="header-top">
        <!-- 导航 -->
        <@shiro.user>
        <div class="navbar-header">您好,<a href="">
                <strong style="color: #ff500b;">
                    <@shiro.principal property="name" />
                </strong>
            </a> 欢迎进入筑慧宝平台 <a href="${ctx}/logout" style="color:#ff500b">退出</a>
        </div>
        </@shiro.user>
        <@shiro.guest>
            <div class="navbar-header"><a href="${ctx}/login" class="hh">请登录</a> <a href="${ctx}/register"> 免费注册</a></div>
        </@shiro.guest>
        <!-- top-bar-nav-->
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a href="#">
                        <span class="am-icon-shopping-cart" style="color: #ff7300;"></span>
                        <span>购物车</span>
                        <strong style="color:#14a243">0</strong>
                    </a>
                </li>
                <!--<li>-->
                <!--<a href="#">-->
                <!--<span class="am-icon-star"></span>-->
                <!--<span>收藏</span>-->
                <!--</a>-->
                <!--</li>-->
                <!--<li>|</li>-->
                <li>
                    <a href="#">
                        <span class="am-icon-home"></span>
                        <span>首页</span>
                    </a>
                </li>
                <!--<li><a href="#">我的订单</a></li>-->
                <li class="dropdown">
                    <a href="#">
                        <span>我的筑慧宝</span>
                        <span class="am-icon-caret-down" style="color:#ff7300;"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="#">我的订单 <strong style="color:green;text-decoration: underline">5</strong></a>
                        </li>
                        <li><a href="#">我的询价 <strong style="color:red;text-decoration: underline">3</strong></a>
                        </li>
                    </ul>
                </li>
                <li><a href="#">客服中心</a></li>
                <li><strong style="color:#ff7300;padding: 0 2px 0 2px"><i class="am-icon-phone"></i>
                    <span style="margin-left: 5px;">400-848-1365</span></strong>
                </li>
                <li><a href="">网站导航</a></li>
            </ul>
        </div>
    </div>
    <div class="header-buttom">
        <div class="logo">
            <span class="am-icon-rebel am-icon-lg"></span>
            <span><strong>筑慧宝</strong></span>
        </div>
        <div class="qcode">
            <a href="javascript:void(0);">
                <img src="${e.res('/img/wechat.png')}" alt="微信公众平台" title="微信公众平台">
            </a>
        </div>
        <div class="search">
            <div id="headSearchTab" class="search-switch">
                <span id="search-demand" class="search-switch-item on ">找采购</span>
                <span id="search-supplier" class="search-switch-item">供应商</span>
                <span id="search-buyer" class="search-switch-item">采购商</span>
            </div>
            <div class="search-bd clear">
                <form action="/" id="headSearchForm" method="get" class="am-form am-form-inline">
                    <div class="am-form-group am-form-icon am-block">
                        <i class="am-icon-search" style="color:#999"></i>
                        <!--search-input  icon-inside-->
                        <input type="text" class="search-input am-form-field display-inline" autocomplete="off"
                               id="topSearchText" placeholder="请输入关键字" style="width: 80%;"
                               data-provide="typeahead" data-items="4"/>
                        <button type="submit" class="search-btn " id="topSearchButton">搜索</button>
                    </div>
                </form>
            </div>
            <div id="historyList" class="search-history">
                <span>最近搜索:</span>
                <a href="">网线</a> <a href="">路由器</a> <a href="">交换机</a>
            </div>
        </div>
    </div>
</div>