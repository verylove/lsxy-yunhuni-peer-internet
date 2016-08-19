<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<!DOCTYPE html>
<html>

<!-- header -->
<head>
    <%@include file="/inc/meta.jsp" %>

</head>
<body>
<section class="vbox">
    <%@include file="/inc/headerNav.jsp"%>
    <section class='aside-section'>
        <section class="hbox stretch">
            <!-- .aside -->
            <aside class="bg-Green lter aside hidden-print" id="nav"><%@include file="/inc/leftMenu.jsp"%></aside>
            <!-- /.aside -->

        <section id="content">
            <section class="hbox stretch">
                <!-- 如果没有三级导航 这段代码注释-->
                <aside class="bg-green lter aside-sm hidden-print ybox" id="subNav">
                    <section class="vbox">
                        <div class="wrapper header"><span class="margin_lr"></span><span class="margin_lr border-left">&nbsp;应用管理</span>
                        </div>
                        <section class="scrollable">
                            <div class="slim-scroll">
                                <!-- nav -->
                                <nav class="hidden-xs">
                                    <ul class="nav">
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/app/list">应用列表</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/app/index">创建应用</a>
                                            </div>
                                        </li>
                                    </ul>
                                </nav>
                            </div>
                        </section>
                    </section>
                </aside>
                <aside>
                    <section class="vbox xbox">
                        <!-- 如果没有三级导航 这段代码注释-->
                        <div class="head-box"><a href="#subNav" data-toggle="class:hide"> <i
                                class="fa fa-angle-left text"></i> <i class="fa fa-angle-right text-active"></i> </a>
                        </div>
                        <section class=" w-f application_create">
                            <div class="wrapper header">
                                <span class="border-left">&nbsp;
                                    <c:if test="${app.id==null}">创建应用</c:if>
                                    <c:if test="${app.id!=null}">编辑应用</c:if>
                                </span></div>
                            <div class="row m-l-none m-r-none bg-light lter">
                                <div class="row">

                                    <form:form role="form" action="./index.html" method="post" class="register-form" id="application_create">
                                        <c:if test="${app.id!=null}">
                                            <input type="hidden" name="id" value="${app.id}">
                                            <input type="hidden" name="status" value="${app.status}">
                                            <c:if test="${app.status==1}">
                                                <p class="app-tips ">编辑应用的选择服务项目需要下线应用，才能编辑</p>
                                            </c:if>
                                        </c:if>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">应用名称：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="name" value="${app.name}" placeholder="" class="form-control input-form limit20"/>
                                            </div>
                                            <span class="span-required">*</span>20字符内，符合<a href="">应用审核规范要求</a>
                                        </div>

                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">应用描述：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="description" value="${app.description}" placeholder="" class="form-control input-form limit300"/>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">应用类型：</lable>
                                            <div class="col-md-4 ">
                                                <select name="type" class="form-control notEmpty">
                                                    <option value="">请选择应用类型</option>
                                                    <option value="网页" <c:if test="${app.type=='网页'}">selected</c:if>>网页</option>
                                                    <option value="PC" <c:if test="${app.type=='PC'}">selected</c:if>>PC</option>
                                                    <option value="移动" <c:if test="${app.type=='移动'}">selected</c:if>>移动</option>
                                                    <option value="其他" <c:if test="${app.type=='其他'}">selected</c:if>>其他</option>
                                                </select>
                                            </div>
                                            <span class="span-required">*</span>
                                        </div>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">所属行业：</lable>
                                            <div class="col-md-4 ">
                                                <select name="industry"  class="form-control notEmpty">
                                                    <option value="">请选择所属行业</option>
                                                    <c:set var="industry" value="${app.industry}"></c:set>
                                                    <%@ include file="/inc/industry.jsp"%>
                                                </select>
                                            </div>
                                            <span class="span-required">*</span>
                                        </div>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">服务器白名单：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="whiteList" value="${app.whiteList}" placeholder="" class="form-control input-form limit300"/>
                                            </div>
                                        </div>
                                        <p class="tips">
                                            允许IP地址，以英文输入法分号分隔，例如：8.8.8.8; 8.8.8.8 设定白名单地址后，云呼你服务器在识别该应用请求时将只接收白名单内服务器发送的请求，能有效提升帐号安全性。 如未设置默认不生效
                                        </p>
                                        <div class="form-group">
                                            <lable class="col-md-3 text-right">回调URL：</lable>
                                            <div class="col-md-4">
                                                <input type="text" name="url" value="${app.url}" placeholder="" class="form-control input-form limit300"/>
                                            </div>
                                            <a href="">回调说明文档</a>
                                        </div>

                                        <p class="tips">
                                            <input type="checkbox" name="isAuth" value="1" <c:if test="${app.isAuth=='1'}">checked='checked'</c:if>> 鉴权 (网络直拨,回拨,互联网语音,视频通话会涉及鉴权流程，勾选但未实现会呼叫失败)
                                        </p>
                                        <div class="form-group min-height20">
                                            <span class="hr text-label" ><strong>选择服务:</strong></span>
                                        </div>
                                        <div class="form-group app-createbox border-bottom">
                                            <lable class="col-md-3 text-right"></lable>
                                            <div class="col-md-9" >
                                                <p><strong>基础语音服务</strong></p>
                                                <p><input type="checkbox" name="isVoiceDirectly" value="1" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isVoiceDirectly=='1'}">checked='checked'</c:if>> 启用 &nbsp;&nbsp;<a href="">语音呼叫</a>（嵌入CRM、OA、呼叫中心等产品中发起通话）</p>
                                                <p><input type="checkbox" name="isVoiceCallback" value="1" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isVoiceCallback=='1'}">checked='checked'</c:if>> 启用 &nbsp;&nbsp;<a href="">语音回拨</a>（以不同的通话方式实现匿名通话功能,保护双方号码隐私）</p>

                                            </div>
                                        </div>
                                        <div class="form-group app-createbox" >
                                            <lable class="col-md-3 text-right"></lable>
                                            <div class="col-md-9" >
                                                <p><strong>高级语音定制服务</strong></p>
                                                <p><input type="checkbox" name="isSessionService" value="1" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isSessionService=='1'}">checked</c:if>> 启用 &nbsp;&nbsp;<a href="">语音会议</a>（可与互联网会议、视频会议融合参会，提供丰富的会议管理功能）</p>
                                                <p><input type="checkbox" name="isRecording" value="1" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if>  <c:if test="${app.isRecording=='1'}">checked</c:if>> 启用 &nbsp;&nbsp;<a  href="">通话录音</a>（提供通话录音、录音存储管理等功能）</p>
                                                <p><input type="checkbox" name="isVoiceValidate" value="1" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isVoiceValidate=='1'}">checked</c:if>> 启用 &nbsp;&nbsp;<a  href="">语音验证码</a>（通过电话直呼到用户手机并语音播报验证码）</p>
                                                <p><input type="checkbox" name="isIvrService" value="1" <c:if test="${app.status=='1'}"> disabled="disabled" </c:if> <c:if test="${app.isIvrService=='1'}">checked</c:if>> 启用 &nbsp;&nbsp;<a  href="">自定义IVR</a>（即互动式语音应答可以根据用户输入的内容播放有关的信息）</p>
                                                <div class="tips ml-36">
                                                    <p class="app-tips ">开启后，该应用将产生1000的号码租用费以及100元/月的功能费，上线时开始收取，多个应用开启并上线会叠加收费</p>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-md-4 remove-padding">
                                                <a id="validateBtn" class="validateBtnNormal btn btn-primary  btn-form">
                                                    <c:if test="${app.id==null}">创建</c:if>
                                                    <c:if test="${app.id!=null}">修改</c:if>
                                                </a>
                                                <c:if test="${app.id!=null}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a id="validateBtn2"  class="btn btn-primary  btn-form">取消</a></c:if>
                                            </div>
                                        </div>
                                    </form:form>
                                </div>
                            </div>
                        </section>
                    </section>
                </aside>
            </section>
        </section>
    </section>
</section>
</section>
<div class="tips-toast"></div>
<div class="tips-toast"></div>
<%@include file="/inc/footer.jsp"%>
<script type="text/javascript" src='${resPrefixUrl }/js/application/create.js'></script>

<script>
    $('#validateBtn2').click(function(){
        window.location.href="${ctx}/console/app/detail?id=${app.id}";
    });
    $('#validateBtn').click(function(){
        $('#application_create').bootstrapValidator('validate');
        var result = $('#application_create').data('bootstrapValidator').isValid();
        if(result==true){
            $('#validateBtn').attr('disabled','disabled');
            var tempType = $('#validateBtn').html().trim()=='创建'?"create":"update";
            //提交表单
            ajaxsync(ctx + "/console/app/"+tempType,getFormJson("#application_create"),function(response){
                if(response.success){
                    var url = "";
                    if($('#validateBtn').html().trim()=='创建'){
                        url="${ctx}/console/app/list";
                        showtoast("新建应用成功",url);
                    }else{
                        url="${ctx}/console/app/detail?id=${app.id}";
                        showtoast("应用修改成功",url);
                    }
                }else{
                    showtoast(response.errorMsg);
                    $('#validateBtn').removeAttr('disabled');
                }
            },"post").fail(function(){
                $('#validateBtn').removeAttr('disabled');
            });

        }

    });

</script>

</body>
</html>
