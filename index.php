<?php
Class user {
    
    public $id;
    public $username;
    public $password;
    public $phone;
    public $lastname;
    public $firstname;
    public $postalCode;
    public $address;
    public $email;
           
    public function  __construct() {
        $this->id = 42;
        $this->username = "admin";
        $this->password = "admin";
        $this->phone = "+33601010101";
        $this->lastname = "colinet";
        $this->firstname = "steve";
        $this->postalCode = "10000";
        $this->address = "SUPINFO";
        $this->email = "steve.colinet@supinfo.com";
    
    }
            
}

if(isset($_POST["action"])){
    $result["success"] = false;
if($_POST["action"] == "login") {
    if($_POST["login"] == "admin" && $_POST["password"] == "admin"){
        $result["success"] = true;
        $result["user"] = new User();
    }
    else {
        $result["success"] = false;
    }
}
elseif($_POST["action"] == "backupsms") {
    if($_POST["login"] == "admin" && $_POST["password"] == "admin"){
        if($_POST["box"] == "sent" || $_POST["box"] == "inbox"){
            if(is_array(json_decode($_POST["sms"])) || is_object(json_decode($_POST["sms"]))) {
                $result["success"] = true;
            }
            else {
                $result["success"] = false;
            }
        }
    }
    else {
        $result["success"] = false;
    }
}
elseif($_POST["action"] == "backupcontacts") {
    if($_POST["login"] == "admin" && $_POST["password"] == "admin"){
        
        if(is_array(json_decode($_POST["contacts"])) || is_object(json_decode($_POST["contacts"]))) {
            $result["success"] = true;
        }
        else {
            $result["success"] = false;
        }
    }
    else {
        $result["success"] = false;
    }
}
echo json_encode($result);
die();
}
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Welcome!</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
   
    <meta name="robots" content="noindex" />
	<style type="text/css"><!--
    body {
        color: #444444;
        background-color: #EEEEEE;
        font-family: 'Trebuchet MS', sans-serif;
        font-size: 80%;
    }
    h1 {}
    h2 { font-size: 1.2em; }
    #page{
        background-color: #FFFFFF;
        width: 60%;
        margin: 24px auto;
        padding: 12px;
    }
    #header{
        padding: 6px ;
        text-align: center;
    }
    .header{ background-color: #83A342; color: #FFFFFF; }
    #content {
        padding: 4px 0 24px 0;
    }
    #footer {
        color: #666666;
        background: #f9f9f9;
        padding: 10px 20px;
        border-top: 5px #efefef solid;
        font-size: 0.8em;
        text-align: center;
    }
    #footer a {
        color: #999999;
    }
    --></style>
</head>
<body>
    
    <div id="page">
        <div id="header" class="header">
            <h1>Welcome to SUPSMS API</h1>
        </div>
        <div id="content">
            <h2>This is the API of SUPSMS.</h2>
            
            <p>For questions or problems please contact steve.colinet@supinfo.com .</p>
            <p></p><p></p>
            <p>This API works by HTTP POST.</p>
            <p>No data save in this server</p>
            <p><h4>Three methods are available:</h4></p>
            <p></p>
<p><h4>To check an user use the “login” action:</h4></p>
<p>Parameters:</p>
<p>- action=login</p>
		<p>- login= “an username”</p>
		<p>- password= “the password”</p>
	<p></p>
<p>If successful, returns a json of the user and a state "success" as "true".</p>
<p>If failure, returns a state "success" as "false" in a JSON object.</p>
<p>Success example: </p>
<p>{"success":true,"user":{"id":42,"username":"admin","password":"admin","phone":"+33601010101","lastname":"colinet","firstname":"steve","postalCode":"10000","address":"SUPINFO","email":"steve.colinet@supinfo.com"}}</p> 
<p>Fail example : </p>
<p>{"success":false}<br /></p>
<p><br /></p>
<p><h4>To send a SMS backup use the “backupsms” action:</h4></p>
<p>	Parameters:</p>
<p>- action=backupsms</p>
<p>		- login= “an username”</p>
<p>		- password= “a password”</p>
<p>		- box= “sent” or “inbox”</p>
<p>		- sms= “a json array of SMS object”</p>
<p>             - sms(example)={"SMS":[{"body":"SXQncyBPaw==","box":"sent","date":"1421768114621","_id":"4","address":"+33601010102","thread_id":"2"},{"body":"SGVsbG8gSSBhbSBhIGZha2UgbWVzc2FnZS4=","box":"sent","date":"1421766738222","_id":"2","address":"+33601010101","thread_id":"1"}]}</p>
                <p></p>
<p>If successful, returns a state "success" as "true" in a JSON object.</p>
<p>If failure, returns a state "success" as "false" in a JSON object.</p>
<p>Success example: {"success":true}</p> 
<p>Fail example : {"success":false}<br /></p>
<p><br /></p>
<p><h4>To send a contacts backup use the “backupcontacts” action:</h4></p>
	<p>Parameters:</p>
<p>- action=backupcontacts</p>
		<p>- login= “an username”</p>
		<p>- password= “a password”</p>
<p>- contacts= “a json array of contacts object”</p>
<p></p>
<p>If successful, returns a state "success" as "true" in a JSON object.</p>
<p>If failure, returns a state "success" as "false" in a JSON object.</p>
<p>Success example: {"success":true}</p> 
<p>Fail example : {"success":false}<br /></p>
<p><br /></p>
<p><br/><br/><br/></p>
<p></p>
<p><h3>To add a fake sms in your emulator, use these lines:</h3></p>
<p>   ContentValues values = new ContentValues();</p>
<p>   values.put("address", "+33601010101");</p>
<p>   values.put("body", "Hello I am a fake message.");</p>
<p>   Uri uri = Uri.parse("content://sms/inbox");</p>
<p>   getContentResolver().insert(uri, values);  </p>       
            
        </div>
        <div id="footer">
            <p>Powered by <a href="http://www.supinfo.com">SUPSMS</a></p>
        </div>
    </div>
</body>
</html>
