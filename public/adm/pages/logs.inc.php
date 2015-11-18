<div id="left">
    <table width='100%' height='30' id='header'>
    <tr>
      <td align='center'>
        <img src='./theme/images/header-left.png' id='assetHeaderLeft' alt='' border='0' />
        <span id='assetHeader'>Export Logs</span>
        <img src='./theme/images/header-right.png' id='assetHeaderRight' alt='' border='0' />
      </td>
    </tr>
  </table>
  <br />
  <a href="?p=logs&c=export">
    <div id="button">All</div>
  </a>
</div>

<div id="content">
  <div id="contentDiv">
    <?php
	    echo $header1;
		echo $tblData1;					
	?>
  </div>
</div>