<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
      <title>MediaPlayer Control</title>
      <script type="text/javascript"
              src="${pageContext.request.contextPath}/ajax/prototype-1.7.0.0.js"></script>
      <script type="text/javascript" xml:space="preserve">
          /*
           * Function that uses Prototype to invoke an action of a form. Slurps the values
           * from the form using prototype's 'Form.serialize()' method, and then submits
           * them to the server using prototype's 'Ajax.Updater' which transmits the request
           * and then renders the resposne text into the named container.
           *
           * @param form reference to the form object being submitted
           * @param event the name of the event to be triggered, or null
           */
          function invoke(form, event) {
//alert('event: '+event + ' form: '+form);
              if (!form.onsubmit) { form.onsubmit = function() { return false } };
              var params = Form.serialize(form, {submit:event});
//alert(params);
              new Ajax.Request('/action/MediaPlayer.action', {
            	  method:'get',
            	  parameters:params,
            	  requestHeaders: {Accept: 'application/json'},
            	  onSuccess: function(transport) {
            	    var json = transport.responseText.evalJSON(true);
//alert(json ? Object.inspect(json) : "no JSON object");
            	    var node = $("playerId");
            	    node.innerHTML = json.playerId;
            	    
            	    /* */
            	    var html = '<ul>';
            	    for(var key in json.itemList) {
            	    	if (!json.itemList.hasOwnProperty(key)) {
            	    		continue;
            	    	}
            	        for(var key2 in (obj = json.itemList[key]))
            	            {
            	                //obj holds the current object in the json array
//alert(key2+' '+obj[key2]);
								html += "<li>" + key2 + " " + obj[key2] + "</li>";
            	            }
            	    }
            	    html += '</ul>'
 					var node2 = $("playing");
            	    node2.innerHTML = html;            	    
            	  },
            	  onFailure: function() {
            		  var node = $("playerId");
            		  node.innerHTML = '<style="font:red>No player running</style>';
            	  }
            	});
          }
          function init() {
        	  invoke(document.forms[0], 'status');
          }
      </script>
  </head>
  <body>
    <h1>MediaPlayer Control</h1>

    <p>Press playpause to Play/Pause the media players</p>

    <stripes:form action="/examples/ajax/Calculator.action">
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