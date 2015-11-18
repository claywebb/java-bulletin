<div class="boxShadow">
  <div id="modLeft">
        <div class="header">System Manager</div>
        <hr />
        STATUS IMAGE
        <br />
        <form action="?p=home" method="post">
          <button type='submit' name='restart' value='Restart' border='0'>RESTART</button>
        </form>
        <br />
        <form action="?p=home" method="post">
          <button type='submit' name='start' value='Start' border='0'>START</button>
        </form>
        
        <br />
        <br />
        <div class="header">New User</div>
        <hr />
        <form action="?p=user" method="post">
          <table width="100%" height="80" cellpadding="0" id="login">
              <tr>
              <td width="49%" height="20" align="right">
                First Name:
              </td>
              <td width="140" height="20" align="left">
                <input type="text" id="fname" name="fname" />
              </td>
            </tr>
            <tr>
              <td width="49%" height="20" align="right">
                Last Name:
              </td>
              <td width="140" height="20" align="left">
                <input type="text" id="lname" name="lname" />
              </td>
            </tr>
            <tr>
              <td width="49%" height="20" align="right">
                Email:
              </td>
              <td width="140" height="20" align="left">
                <input type="text" id="email" name="email" />
              </td>
            </tr>
            <tr>
              <td width="49%" height="20" align="right">
                Password:
              </td>
              <td width="140" height="20" align="left">
                <input type="password" name="pass" />
              </td>
            </tr>
            
          </table>
          
          <br />
          <input type="submit" name="submit" value="Create User" id="submit" />
        </form>
  </div>
</div>

<div class="boxShadow">
  <div id="modRight">
    <div class="header">File Manager</div>
    <hr />
    <div id="filesBox">
      <form action="?p=home" method="POST">
      <?php echo $files; ?>
      </form>
    </div>
    <br />
    <br />
    <div id="upload">
      <form action="?p=home" method="post" enctype="multipart/form-data">
        Upload an Image
        <input type="file" name="fileToUpload" id="fileToUpload">
        <input type="submit" value="Upload Image" name="submit">
    </form>
    </div>
  </div>
</div>