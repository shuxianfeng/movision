<#include "layout/common/layout.ftl">
<@layout title="DEMO" currpos="demo">
<div style="min-height: 500px;width:80%;margin:0 auto;font-size: 13px;">
    <h1>示例</h1>
    我的姓名:${name!''}
    <br/>
    我的帐户状态:<#if  status=="1">正常<#else>异常</#if>

    <h2>所有用户</h2>
    <table class="am-table am-table-striped am-table-hover">
        <thead>
        <tr>
            <th>姓名</th>
            <th>电话</th>
            <th>状态</th>
        </tr>
        </thead>
        <tbody>
        <#--freemarker list 用法-->
        <#--sort_by("status") 按用户ID升序排序 ?reverse降序-->
            <#list users?sort_by("id") as user>
            <#--遍历list当用户号码是18652093798时，停止输出 使用break
                <#if user.mobile=="18652093798">
                    <#break>
                </#if>     -->
            <tr <#if user_index==1>class="am-active"</#if>>
                <td>第${user_index+1}个用户</td><#--list 隐含变量 item_index-->
                <td>${user.name}</td>
                <td>${user.mobile}</td>
                <td <#if user.status=="1">class="am-success" <#else>class="am-danger"</#if> >
                    <#if  user.status=="1">正常<#else>异常</#if>
                </td>
            </tr>
                <#if !user_has_next> <#--list 隐含变量 item_has_next-->
                <tr>
                    <td colspan="4">
                        共有 <b>${users?size}</b> 个用户,最后一个用户是: ${user.name}
                    </td>
                </tr>
                </#if>
            </#list>
        </tbody>
    </table>
</div>
</@layout>