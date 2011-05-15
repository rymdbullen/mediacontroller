<%@ include file="/mediaplayer/taglibs.jsp" %>

<stripes:layout-render name="/mediaplayer/layout/standard.jsp" title="Login">
    <stripes:layout-component name="contents">

            <script type="text/javascript" xml:space="preserve">
		          function init() {
		        	  invoke(document.forms[1], 'status');
		          }
		    </script>
        <table style="vertical-align: top;">
            <tr>
                <td style="width: 25%; vertical-align: top;">
                    <!-- Somewhat contrived example of using the errors tag 'action' attribute. -->
                    <stripes:errors action="/examples/bugzooky/Login.action"/>

                    <stripes:form action="/examples/bugzooky/Login.action" focus="">
                        <table>
                            <tr>
                                <td style="font-weight: bold;"><stripes:label for="username"/>:</td>
                            </tr>
                            <tr>
                                <td><stripes:text name="username" value="${user.username}"/></td>
                            </tr>
                            <tr>
                                <td style="font-weight: bold;"><stripes:label for="password"/>:</td>
                            </tr>
                            <tr>
                                <td><stripes:password name="password"/></td>
                            </tr>
                            <tr>
                                <td style="text-align: center;">
                                    <%-- If the security servlet attached a targetUrl, carry that along. --%>
                                    <stripes:hidden name="targetUrl" value="${request.parameterMap['targetUrl']}"/>
                                    <stripes:submit name="login" value="Login"/>
                                </td>
                            </tr>
                        </table>
                    </stripes:form>
                </td>
                <td style="vertical-align: top;">
                    <c:choose>
                        <c:when test="${empty user}">
                      		<h1 class="sectionTitle">MediaPlayer Control</h1>
	
						    <p>Press playpause to Play/Pause the media players</p>
						
						    <stripes:form action="/action/MediaPlayer.action">
						        <table>
						            <tr>
						                <td>
						                    <div class="buttons"><stripes:submit name="playPause" value="PlayPause"
						                        onclick="invoke(this.form, this.name);"/></div>
						                </td>
						                <td>
						                    <div class="buttons"><stripes:submit name="start" value="Start"
						                        onclick="invoke(this.form, this.name);"/></div>
						                </td>
						                <td>
						                    <div class="buttons"><stripes:submit name="stop" value="Stop"
						                        onclick="invoke(this.form, this.name);"/></div>
						                </td>
						            </tr>
						            <tr>
						                <td>
						                    <div class="buttons"><stripes:submit name="previous" value="Previous"
						                        onclick="invoke(this.form, this.name);"/></div>
						                </td>
						                <td>
						                    <div class="buttons"><stripes:submit name="next" value="Next"
						                        onclick="invoke(this.form, this.name);"/></div>
						                </td>
						            </tr>
						            <tr>
						                <td colspan="2">
						                    <div class="buttons"><stripes:submit name="status" value="status"
						                        onclick="invoke(this.form, this.name);"/></div>
						                </td>
						            </tr>
						            <tr>
						                <td>Player:</td>
						                <td id="playerId"></td>
						            </tr>
						            <tr>
						                <td>Playing:</td>
						                <td id="playing"></td>
						            </tr>
						        </table>
						    </stripes:form>
						    <script>init();</script>
                        </c:when>

                        <c:otherwise>
                            <p>You are already logged in as '${user.username}'.  Logging in again will cause
                            you to  be logged out, and then re-logged in with the username and password
                            supplied.</p>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>

    </stripes:layout-component>
</stripes:layout-render>
