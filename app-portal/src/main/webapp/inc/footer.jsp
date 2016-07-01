<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>

<script src="${resPrefixUrl }/js/app.v2.js"></script> <!-- Bootstrap --> <!-- App -->
<script src="${resPrefixUrl }/js/charts/flot/jquery.flot.min.js" cache="false"></script>
<script src="${resPrefixUrl }/js/bootbox.min.js"></script>
<script src="${resPrefixUrl }/js/charts/flot/demo.js" cache="false"></script>
<script src="${resPrefixUrl }/bower_components/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>

<script>
	$(function(){
		var node = "";
		var pathSplit = location.pathname.split("/");
		if(pathSplit.length > 2){
			node = pathSplit[2]
		}
		if(node != null && node != ""){
			$('.nav-router').each(function(){
				var nav = $(this).attr('data-router');
				if(nav==node){
					$(this).addClass('active')
				}
			});
		}
	})
</script>
