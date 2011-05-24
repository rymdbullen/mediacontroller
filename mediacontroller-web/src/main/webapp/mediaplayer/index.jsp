<%@ include file="/mediaplayer/taglibs.jsp" %>

<stripes:layout-render name="/mediaplayer/layout/standard.jsp" title="MediaPlayer Control">
    <stripes:layout-component name="contents">
            <script type="text/javascript" xml:space="preserve">
			var index = 0;
            		function init2() {
            			var node2 = $("janplaying")
            			//alert(node2);
	            		pu = new Ajax.PeriodicalUpdater('janplaying', '/action/MediaPlayerStatus.action', {
							method: 'get',
							frequency: 3,
							decay: 2,
							onSuccess : function(jsonStatus) {
								index = index +1;
								//alert('hej ' + index + ' '+ jsonStatus.numPlayers);
								node2.innerHTML = 'hej ' + index;
							}
						});
            		}
					function init() {
						invoke(document.forms[0], 'status');
						//init2();
					}
		    </script>
		    
	    <stripes:form action="/action/MediaPlayer.action">
		<div id="controlsArea">
			<div id="controls">
				<a id="buttonBack" href="#" title="Back" onclick="invoke(document.forms[0], 'previous');"></a>
			</div>
			<div id="controls">
				<a id="buttonPlayPause" href="#" title="PlayPause" onclick="invoke(document.forms[0], 'playPause');"></a>
			</div>
			<div id="controls">
				<a id="buttonPlay" href="#" title="play" onclick="invoke(document.forms[0], 'play');"></a>
			</div>
			<div id="controls">
				<a id="buttonStop" href="#" title="Stop" onclick="invoke(document.forms[0], 'stop');"></a>
			</div>
			<div id="controls">
				<a id="buttonForward" href="#" title="Forward" onclick="invoke(document.forms[0], 'next');"></a>
			</div>
		</div>
		<div id="clear"></div>
		<div>
		    <div>Players Available:<div id="playerIds"></div></div>
		</div>
		<div>
		    <div>Player:<div id="playerId"></div></div>
		</div>
		<div>
		    <div>Playing:<div id="playing"></div></div>
		    <div>JanPlaying:<div id="janplaying"></div></div>
		</div>
		
        <div class="buttons"><stripes:submit name="status" value="refresh"
            onclick="invoke(this.form, this.name);"/></div>
            
        <stripes:hidden name="playerId" value="" id="janne" />

        <script>init();</script>
	    </stripes:form>
        
    </stripes:layout-component>
</stripes:layout-render>
