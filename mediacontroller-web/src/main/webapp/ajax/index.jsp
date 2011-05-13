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
           * @param container the name of the HTML container to insert the result into
           */
          function invoke(form, event, container) {
              if (!form.onsubmit) { form.onsubmit = function() { return false } };
              var params = Form.serialize(form, {submit:event});
              //alert(params);
              new Ajax.Request('/examples/ajax/Calculator.action', {
            	  method:'get',
            	  parameters:params,
            	  requestHeaders: {Accept: 'application/json'},
            	  onSuccess: function(transport) {
            	    var json = transport.responseText.evalJSON(true);
            	    //alert(json ? Object.inspect(json) : "no JSON object");
            	    var node = $("result");
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
            	  }
            	});
          }
          function init1() {
        	  alert('hej');
        	  var params = '';
              new Ajax.Request('/examples/ajax/Calculator.action', {
            	  method:'get',
            	  parameters:params,
            	  requestHeaders: {Accept: 'application/json'},
            	  onSuccess: function(transport) {
            	    var json = transport.responseText.evalJSON(true);
            	    //alert(json ? Object.inspect(json) : "no JSON object");
            	    var node = $("result");
            	    node.innerHTML = json.playerId;
            	    
            	    /* */
            	    alert('hej1');
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
            	  }
            	});
          }
          function init() {
        	  init1();
          }
      </script>
  </head>
  <body>
    <h1>MediaPlayer Control</h1>

    <p>Hi, I'm the Stripes Calculator. I can only do addition. Maybe, some day, a nice programmer
    will come along and teach me how to do other things?</p>

    <stripes:form action="/examples/ajax/Calculator.action">
        <table>
            <tr>
                <td>Number 1:</td>
                <td><stripes:text name="numberOne"/></td>
            </tr>
            <tr>
                <td>Number 2:</td>
                <td><stripes:text name="numberTwo"/></td>
            </tr>
            <tr>
                <td colspan="2">
                    <stripes:submit name="add" value="Add"
                        onclick="invoke(this.form, this.name, 'result');"/>
                    <stripes:submit name="divide" value="Divide"
                        onclick="invoke(this.form, this.name, 'result');"/>
                </td>
            </tr>
            <tr>
                <td>Result:</td>
                <td id="result"></td>
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