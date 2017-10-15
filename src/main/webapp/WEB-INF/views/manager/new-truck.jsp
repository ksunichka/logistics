<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ksenia
  Date: 12.10.2017
  Time: 18:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<ol class="breadcrumb">
    <strong>Trucks</strong>
</ol>

<div class="container-fluid">
    <div class="animated fadeIn">
        <div class="row">
            <div class="col-sm-7">
                <div class="card">
                    <div class="card-header">
                        <c:choose>
                            <c:when test="${updatedTruck == null}">
                                <strong>New Truck</strong>
                            </c:when>
                            <c:otherwise>
                                <strong>Update Truck</strong>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <form action="<c:url value="/manager/truck/new"/>" method="post">
                        <input type="hidden" id="id" name="id" value="${updatedTruck.id}">
                        <div class="card-body">
                            <div class="form-group row">
                                <label class="col-md-3 form-control-label" for="number-plate">Number plate</label>
                                <div class="col-md-9">
                                    <input type="text" id="number-plate" name="numberPlate" class="form-control" value="${updatedTruck.numberPlate}"
                                           placeholder="XX99999">
                                </div>
                            </div>

                            <div class="form-group row">
                                <label class="col-md-3 form-control-label" for="working-shift">Working shift (hours)</label>
                                <div class="col-md-3">
                                    <input type="text" id="working-shift" name="workingShift" class="form-control" value="${updatedTruck.workingShift}"
                                           placeholder="10.0">
                                </div>
                                <label class="col-md-3 form-control-label" for="capacity">Capacity (tonnes)</label>
                                <div class="col-md-3">
                                    <input type="text" id="capacity" name="capacity" class="form-control" value="${updatedTruck.capacity}"
                                           placeholder="3.0">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-md-3 form-control-label">Functioning</label>
                                <div class="col-md-9">
                                    <label class="radio-inline" for="functioning1">
                                        <input type="radio" id="functioning1" name="functioning" value="true"
                                        <c:if test="${updatedTruck.functioning == true or updatedTruck == null}"> checked="checked"</c:if>>
                                        Yes&nbsp;&nbsp;&nbsp;
                                    </label>
                                    <label class="radio-inline" for="functioning2">
                                        <input type="radio" id="functioning2" name="functioning" value="false"
                                               <c:if test="${updatedTruck.functioning == false}">checked="checked"</c:if>>No
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="card-footer">
                            <c:choose>
                                <c:when test="${updatedTruck == null}">
                                    <button type="submit" class="btn btn-success" role="button">
                                        <i class="fa fa-check-circle-o fa-lg"></i> Add
                                    </button>
                                    <button type="reset" class="btn btn-danger">
                                        <i class="fa fa-times-circle-o fa-lg"></i> Reset
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <button type="submit" class="btn btn-success" role="button">
                                        <i class="fa fa-check-circle-o fa-lg"></i> Update
                                    </button>
                                </c:otherwise>
                            </c:choose>
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        </div>
                    </form>
                </div>
            </div>
            <!--/.col-->
        </div>
        <!--/.row-->
    </div>
</div>
<!-- /.conainer-fluid -->