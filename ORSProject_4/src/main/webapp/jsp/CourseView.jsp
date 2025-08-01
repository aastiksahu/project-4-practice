
<%@page import="com.rays.controller.ORSView"%>
<%@page import="com.rays.util.HTMLUtility"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.rays.controller.CourseCtl"%>
<%@page import="com.rays.controller.BaseCtl"%>
<%@page import="com.rays.util.DataUtility"%>
<%@page import="com.rays.util.ServletUtility"%>

<html>
<head>
<title>User</title>
<style type="text/css">
.panel-default1 {
	border-color: red;
}
</style>
</head>
<body>
	<form action="<%=ORSView.COURSE_CTL%>" method="post">
		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="com.rays.bean.CourseBean"
			scope="request"></jsp:useBean>

		<div align="center">
			<h1 align="center" style="margin-bottom: -15; color: navy">
				<%
				if (bean != null && bean.getId() > 0) {
				%>
				Update
				<%
				} else {
				%>
				Add
				<%
				}
				%>
				Course
			</h1>

			<div style="height: 15px; margin-bottom: 12px">
				<h3 align="center">
					<font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
					</font>
				</h3>
				<h3 align="center">
					<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
					</font>
				</h3>
			</div>

			<input type="hidden" name="id" value="<%=bean.getId()%>"> <input
				type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
			<input type="hidden" name="modifiedBy"
				value="<%=bean.getModifiedBy()%>"> <input type="hidden"
				name="createdDatetime"
				value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
			<input type="hidden" name="modifiedDatetime"
				value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">

			<table>
				<tr>
					<th align="left">Name<span style="color: red">*</span></th>
					<td align="center"><input type="text" name="name"
						placeholder="Enter Course Name"
						value="<%=DataUtility.getStringData(bean.getName())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("name", request)%>
					</font></td>
				</tr>

				<tr>
					<th align="left">Duration<span style="color: red">*</span></th>
					<td>
						<%
						LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
						map.put("1 Year", "1 Year");
						map.put("2 Year", "2 Year");
						map.put("3 Year", "3 Year");
						map.put("4 Year", "4 Year");

						String htmlList = HTMLUtility.getList("duration", bean.getDuration(), map);
						%> <%=htmlList%>
					</td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("duration", request)%>
					</font></td>
				</tr>

				<tr>
					<th align="left">Description<span style="color: red">*</span></th>
					<td align="center"><textarea
							style="width: 173px; resize: none;" name="description" rows="3"
							placeholder="Enter Short description"><%=DataUtility.getStringData(bean.getDescription()).trim()%></textarea>
					</td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("description", request)%>
					</font></td>
				</tr>

				<tr>
					<th></th>
					<td></td>
				</tr>

				<tr>
					<th></th>
					<%
					if (bean != null && bean.getId() > 0) {
					%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=CourseCtl.OP_UPDATE%>"> <input
						type="submit" name="operation" value="<%=CourseCtl.OP_CANCEL%>">
					</td>
					<%
					} else {
					%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=CourseCtl.OP_SAVE%>"> <input
						type="submit" name="operation" value="<%=CourseCtl.OP_RESET%>">
					</td>
					<%
					}
					%>
				</tr>
			</table>
		</div>
	</form>
</body>
</html>
