<div id="loginContainer" align="center">
      <div id="title">Walsh Jesuit CMS Manager</div>
      <?php echo $error; ?>
      
      <form action="?p=home" method="post">
        <table width="190" height="80" cellpadding="0" id="login">
          <tr>
            <td width="50" height="20" align="right">
              Email:
            </td>
            <td width="140" height="20" align="left">
              <input type="text" id="username" name="username" />
            </td>
          </tr>
          <tr>
            <td width="50" height="20" align="right">
              Password:
            </td>
            <td width="140" height="20" align="left">
              <input type="password" name="pass" />
            </td>
          </tr>
        </table>
        
        <br />
        <input type="submit" name="submit" value="Login" id="submit" />
      </form>
    </div>