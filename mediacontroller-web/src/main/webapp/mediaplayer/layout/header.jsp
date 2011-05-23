<%@ include file="/mediaplayer/taglibs.jsp" %>

<div id="imageHeaderr">
    <table style="padding: 5px; margin: 0px; width: 100%;">
        <tr>
            <td id="pageHeader">mediaplayer: stripes demo application</td>
            <td id="loginInfo">
                <c:if test="${not empty user}">
                    Welcome: ${user.firstName} ${user.lastName}
                    |
                    <stripes:link href="/examples/mediaplayer/Logout.action">Logout</stripes:link>
                </c:if>
            </td>
        </tr>
    </table>
    <!--
    <div id="navLinks">
        <stripes:link href="/mediaplayer/BugList.jsp">Bug List</stripes:link>
        <stripes:link href="/mediaplayer/AddEditBug.jsp">Add Bug</stripes:link>
        <stripes:link href="/mediaplayer/BulkAddEditBugs.jsp">Bulk Add</stripes:link>
        <stripes:link href="/mediaplayer/AdministerBugzooky.jsp">Administer</stripes:link>
    </div>
    -->
</div>