<#include "layout/common/layout.ftl">
<@layout title="分页示例" currpos="pagination">
<div style="min-height: 500px;width:80%;margin:0 auto;font-size: 13px;">
    <h1>分页示例</h1>

    <table class="am-table am-table-striped am-table-hover">
        <thead>
        <tr>
            <th>姓名</th>
            <th>电话</th>
            <th>状态</th>
        </tr>
        </thead>
        <tbody>
            <#if (pager.rows?size>0) >
            <#list pager.rows as user>
            <tr <#if user_index==1>class="am-active"</#if>>
                <td>${user.name}</td>
                <td>${user.mobile}</td>
                <td <#if user.status=="1">class="am-success" <#else>class="am-danger"</#if> >
                    <#if  user.status=="1">正常<#else>异常</#if>
                </td>
            </tr>
            </#list>
            <#else>
            <tr><td colspan="4" style="color:red">没有符合条件的记录</td></tr>
            </#if>
        </tbody>
    </table>
    <#import "layout/common/pager.ftl" as q>
    <@q.pager pageNo=pager.curPage pageSize=pager.pageSize recordCount=pager.total toURL="${ctx}/pager"/>
</div>
</@layout>