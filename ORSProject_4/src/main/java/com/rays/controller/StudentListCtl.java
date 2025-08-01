package com.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.bean.BaseBean;
import com.rays.bean.StudentBean;
import com.rays.exception.ApplicationException;
import com.rays.model.CollegeModel;
import com.rays.model.StudentModel;
import com.rays.util.DataUtility;
import com.rays.util.PropertyReader;
import com.rays.util.ServletUtility;

@WebServlet(name = "StudentListCtl", urlPatterns = { "/StudentListCtl" })
public class StudentListCtl extends BaseCtl {

	@Override
	protected void preload(HttpServletRequest request) {

		CollegeModel collegeModel = new CollegeModel();

		try {
			List collegeList = collegeModel.list();
			request.setAttribute("collegeList", collegeList);

		} catch (ApplicationException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		StudentBean bean = new StudentBean();

		bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
		bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
		bean.setEmail(DataUtility.getString(request.getParameter("email")));
		bean.setCollegeId(DataUtility.getLong(request.getParameter("collegeId")));

		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		StudentBean bean = (StudentBean) populateBean(request);
		StudentModel model = new StudentModel();

		try {
			List list = model.search(bean, pageNo, pageSize);
			List next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.isEmpty()) {

				ServletUtility.setErrorMessage("No Record Found", request);
			}

			ServletUtility.setList(list, request);
			ServletUtility.setBean(bean, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			request.setAttribute("nextListSize", next.size());

			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			e.printStackTrace();
			return;
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List list = null;
		List next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		StudentBean bean = (StudentBean) populateBean(request);
		StudentModel model = new StudentModel();

		String op = DataUtility.getString(request.getParameter("operation"));
		String[] ids = request.getParameterValues("ids");

		try {
			if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {
				
				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
					
				}else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
					
				}else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
					pageNo--;
				}
			}else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.STUDENT_CTL, request, response);
				return;
			}else if (OP_DELETE.equalsIgnoreCase(op)) {
				
				pageNo= 1;
				
				if (ids != null && ids.length > 0) {
					
					StudentBean deletebean = new StudentBean();
					
					for (String id : ids) {
						
						deletebean.setId(DataUtility.getInt(id));
						model.delete(deletebean);
						ServletUtility.setSuccessMessage("Data Deleted Successfully", request);
					}
				}else {
					ServletUtility.setErrorMessage("Select At Least One Record", request);
				}
			}else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.STUDENT_LIST_CTL, request, response);
				return;
			}else if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.STUDENT_LIST_CTL, request, response);
				return;
			}
			
			list = model.search(bean, pageNo, pageSize);
			next = model.search(bean, pageNo +1, pageSize);
			
			if (!OP_DELETE.equalsIgnoreCase(op)) {
				if (list ==null || list.size() == 0) {
					ServletUtility.setErrorMessage("No Record Found", request);
				}
			}
			
			ServletUtility.setList(list, request);
			ServletUtility.setBean(bean, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			request.setAttribute("nextListSize", next.size());
			
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			e.printStackTrace();
			return;
		}

	}

	@Override
	protected String getView() {
		return ORSView.STUDENT_LIST_VIEW;
	}

}
