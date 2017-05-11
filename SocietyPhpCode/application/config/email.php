<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');


// Gmail
$config['protocol'] = 'smtp';
$config['smtp_host'] = 'smtp.gmail.com';
$config['smtp_port'] = '587';
$config['smtp_timeout'] = '30';
$config['smtp_user'] = 'socialsociety2017@gmail.com';
$config['smtp_pass'] = 'Socialsociety@123';


$config['charset'] = 'utf-8';
$config['mailtype'] = 'html';
$config['wordwrap'] = TRUE;
$config['newline'] = "\r\n";

// custom values for ESI Portal
$config['from_email'] = "socialsociety2017@gmail.com";
$config['from_name'] = "Society Admin";
$config['subject_prefix'] = "Social Society | ";
$config['mail_signature'] = "<br /><br /><b><i>Thanks</i></b>,<br />Team Unnific";

/* End of file email.php */
/* Location: ./system/application/config/email.php */