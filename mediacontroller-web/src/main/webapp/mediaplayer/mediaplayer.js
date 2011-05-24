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
    	    var node2 = $("janne")
    	    node2.value = json.playerId;
    	    var node2 = $("isPlaying")
    	    node2.value = json.isPlaying;
    	    
    	    /* */
    	    var html = '<ul>';
    	    for(var key in json.itemList) {
    	    	if (!json.itemList.hasOwnProperty(key)) {
    	    		continue;
    	    	}
    	        for(var key2 in (obj = json.itemList[key]))
    	        {
    	                // obj holds the current object in the json array
						html += "<li>" + key2 + " " + obj[key2] + "</li>";
//alert(key2+' '+obj[key2]);
    	        }
    	    }
    	    html += '</ul>'
			var node2 = $("playing");
    	    node2.innerHTML = html;            	    
    	  },
    	  onFailure: function() {
    		  var node = $("playerId");
    		  node.innerHTML = '<font color="red">Error while getting dbus info</font>';
    	  }
    	});
  }

