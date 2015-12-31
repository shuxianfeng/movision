$(function () {
    {
        // menu siderbar 菜单动态弹出
        $("#sidebar-menu").find("> ul > li").hover(function () {
            $(this).addClass("selected");
            $(".menu-panel", this).addClass("selected");
            $(".menu-panel", this).animate({width: "440px"});
        }, function () {
            $(this).removeClass("selected");
            $(".menu-panel", this).removeClass("selected");
            //$(".menu-panel").animate({width:"0px"});
        });

        //搜索切换
        $('#headSearchTab').find("span").click(function () {
            $(this).addClass("on").siblings().removeClass("on");
            var id = $(this).attr('id');
            if (id == 'search-demand') {
                $('#topSearchText').attr('placeholder', ' 请输入产品名称或关键字');
            } else if (id == 'search-supplier') {
                $('#topSearchText').attr('placeholder', ' 请输入供应商名称或关键字');
            } else if (id == 'search-buyer') {
                $('#topSearchText').attr('placeholder', ' 请输入采购商名称或关键字');
            }
        });
    }
    {
        //搜索框输入提示
        var subjects = ["Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida",
            "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland",
            "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska",
            "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Dakota", "North Carolina",
            "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee",
            "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming",
            "网线", "路由器", "智能电视", "交换机", "空调","空气净化器"];
        $('#topSearchText').typeahead({
            source: subjects,
            items: 10
        });
    }
    //上下滚动
    //{
    //    $("#ul-xj-list").scrollQ({
    //        line: 8,
    //        scrollNum: 8,
    //        scrollTime: 3000
    //    });
    //}

    {
        var $this = $("#div-xj-list");
        var scrollTimer;
        $this.hover(function () {
            clearInterval(scrollTimer);
        }, function () {
            scrollTimer = setInterval(function () {
                scroll($this);
            }, 1000);
        }).trigger("mouseleave");
    }
});


function scroll(obj) {
    var $self = obj.find("ul");
    var lineHeight = $self.find("li:first").height() +15;
    $self.animate({
        "marginTop": -lineHeight + "px"
    }, 600, function () {
        $self.css({
            marginTop: 0
        }).find("li:first").appendTo($self);
    })
}
// scrolltotop
{
    var scrolltotop = {
        setting: {
            startline: 100, //起始行
            scrollto: 0, //滚动到指定位置
            scrollduration: 400, //滚动过渡时间
            fadeduration: [500, 100] //淡出淡现消失
        },
        // controlHTML:'<img src="images/topback.gif" style="width:54px; height:54px; border:0;" />', //返回顶部按钮
        controlHTML: '<i class="am-icon-btn am-icon-arrow-up am-active" style="display: block;color:#f40" />', //返回顶部按钮
        controlattrs: {
            offsetx: 50,
            offsety: 80
        }, //返回按钮固定位置
        anchorkeyword: "#top",
        state: {
            isvisible: false,
            shouldvisible: false
        },
        scrollup: function() {
            if (!this.cssfixedsupport) {
                this.$control.css({
                    opacity: 0
                });
            }
            var dest = isNaN(this.setting.scrollto) ? this.setting.scrollto : parseInt(this.setting.scrollto);
            if (typeof dest == "string" && jQuery("#" + dest).length == 1) {
                dest = jQuery("#" + dest).offset().top;
            } else {
                dest = 0;
            }
            this.$body.animate({
                scrollTop: dest
            }, this.setting.scrollduration);
        },
        keepfixed: function() {
            var $window = jQuery(window);
            var controlx = $window.scrollLeft() + $window.width() - this.$control.width() - this.controlattrs.offsetx;
            var controly = $window.scrollTop() + $window.height() - this.$control.height() - this.controlattrs.offsety;
            this.$control.css({
                left: controlx + "px",
                top: controly + "px"
            });
        },
        togglecontrol: function() {
            var scrolltop = jQuery(window).scrollTop();
            if (!this.cssfixedsupport) {
                this.keepfixed();
            }
            this.state.shouldvisible = (scrolltop >= this.setting.startline) ? true : false;
            if (this.state.shouldvisible && !this.state.isvisible) {
                this.$control.stop().animate({
                    opacity: 1
                }, this.setting.fadeduration[0]);
                this.state.isvisible = true;
            } else {
                if (this.state.shouldvisible == false && this.state.isvisible) {
                    this.$control.stop().animate({
                        opacity: 0
                    }, this.setting.fadeduration[1]);
                    this.state.isvisible = false;
                }
            }
        },
        init: function() {
            jQuery(document).ready(function($) {
                var mainobj = scrolltotop;
                var iebrws = document.all;
                mainobj.cssfixedsupport = !iebrws || iebrws && document.compatMode == "CSS1Compat" && window.XMLHttpRequest;
                mainobj.$body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $("html") : $("body")) : $("html,body");
                mainobj.$control = $('<div id="topcontrol">' + mainobj.controlHTML + "</div>").css({
                    position: mainobj.cssfixedsupport ? "fixed" : "absolute",
                    bottom: mainobj.controlattrs.offsety,
                    right: mainobj.controlattrs.offsetx,
                    opacity: 0,
                    cursor: "pointer"
                }).attr({
                    title: "返回顶部"
                }).click(function() {
                    mainobj.scrollup();
                    return false;
                }).appendTo("body");
                if (document.all && !window.XMLHttpRequest && mainobj.$control.text() != "") {
                    mainobj.$control.css({
                        width: mainobj.$control.width()
                    });
                }
                mainobj.togglecontrol();
                $('a[href="' + mainobj.anchorkeyword + '"]').click(function() {
                    mainobj.scrollup();
                    return false;
                });
                $(window).bind("scroll resize", function(e) {
                    mainobj.togglecontrol();
                });
            });
        }
    };
    scrolltotop.init();
}
