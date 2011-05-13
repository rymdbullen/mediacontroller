<%@ include file="/mediaplayer/taglibs.jsp" %>

<stripes:layout-definition>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
    <html>
        <head>
            <title>MediaPlayer - ${title}</title>
            <link rel="stylesheet" type="text/css" href="${ctx}/mediaplayer/mediaplayer.css"/>
            <script type="text/javascript" src="${ctx}/mediaplayer/mediaplayer.js"></script>
            <script type="text/javascript" src="${ctx}/mediaplayer/prototype-1.7.0.0.js"></script>
            <stripes:layout-component name="html-head"/>
        </head>
        <body>
            <div id="contentPanel">
                <stripes:layout-component name="header">
                    <jsp:include page="/mediaplayer/layout/header.jsp"/>
                </stripes:layout-component>

                <div id="pageContent">
                    <div class="sectionTitle">${title}</div>
                    <stripes:messages/>
                    <stripes:layout-component name="contents"/>
                </div>

                <div id="footer">
                    <stripes:link beanclass="net.sourceforge.stripes.examples.bugzooky.ViewResourceActionBean">
                        View this JSP
                        <stripes:param name="resource" value="${pageContext.request.servletPath}"/>
                    </stripes:link>

                    | View other source files:
                    <stripes:useActionBean beanclass="net.sourceforge.stripes.examples.bugzooky.ViewResourceActionBean" var="bean"/>
                    <select style="width: 350px;" onchange="document.location = this.value;">
                        <c:forEach items="${bean.availableResources}" var="file">
                            <stripes:url beanclass="net.sourceforge.stripes.examples.bugzooky.ViewResourceActionBean" var="url">
                                <stripes:param name="resource" value="${file}"/>
                            </stripes:url>
                            <option value="${url}">${file}</option>
                        </c:forEach>
                    </select>
                    | Built on <a href="http://www.stripesframework.org">Stripes</a>
                </div>
            </div>
        </body>
    </html>
</stripes:layout-definition>
