<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
      <title>MediaPlayer Control</title>
      <script type="text/javascript"
              src="${pageContext.request.contextPath}/ajax/prototype-1.7.0.0.js"></script>
      <script type="text/javascript" xml:space="preserve">

          function init() {
        	  invoke(document.forms[0], 'status');
          }
      </script>
  </head>
  <body>
    <h1>MediaPlayer Control</h1>

    <p>Press playpause to Play/Pause the media players</p>

    <stripes:form action="/action/MediaPlayer.action">
        <table>
            <tr>
                <td colspan="2">
                    <stripes:submit name="playPause" value="PlayPause"
                        onclick="invoke(this.form, this.name);"/>
                    <stripes:submit name="status" value="status"
                        onclick="invoke(this.form, this.name);"/>
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
  </body>
</html>